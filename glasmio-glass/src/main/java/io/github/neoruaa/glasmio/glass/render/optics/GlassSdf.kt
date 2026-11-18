package io.github.neoruaa.glasmio.glass.render.optics

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.kyant.shapes.RoundedRectangularShape

/** Geometry stays in logical surface pixels; shape clipping retains the original continuous path. */
internal fun glassCornerRadii(shape: Shape, size: Size, direction: LayoutDirection, density: Density): FloatArray {
    val limit = size.minDimension / 2f
    fun bounded(a: Float, b: Float, c: Float, d: Float) =
        floatArrayOf(a.coerceIn(0f, limit), b.coerceIn(0f, limit), c.coerceIn(0f, limit), d.coerceIn(0f, limit))
    if (shape is RoundedRectangularShape) {
        val corners = shape.corners(size, direction, density)
        return bounded(corners.topLeft, corners.topRight, corners.bottomRight, corners.bottomLeft)
    }
    if (shape is CornerBasedShape) {
        val ltr = direction == LayoutDirection.Ltr
        return bounded(
            (if (ltr) shape.topStart else shape.topEnd).toPx(size, density),
            (if (ltr) shape.topEnd else shape.topStart).toPx(size, density),
            (if (ltr) shape.bottomEnd else shape.bottomStart).toPx(size, density),
            (if (ltr) shape.bottomStart else shape.bottomEnd).toPx(size, density),
        )
    }
    return when (val outline = shape.createOutline(size, direction, density)) {
        is Outline.Rectangle -> FloatArray(4)
        is Outline.Rounded -> bounded(outline.roundRect.topLeftCornerRadius.x,
            outline.roundRect.topRightCornerRadius.x, outline.roundRect.bottomRightCornerRadius.x,
            outline.roundRect.bottomLeftCornerRadius.x)
        is Outline.Generic -> FloatArray(4) { limit }
    }
}

/** Negative inside, matching the native SDF gradient's [-1, 0, 10] distance convention. */
internal const val GlassSdfShader = NativeGlassSdfFunctions + """
uniform float2 size;
uniform float4 cornerRadii;
float radiusAt(float2 p) {
    return p.y < size.y * 0.5
        ? (p.x < size.x * 0.5 ? cornerRadii.x : cornerRadii.y)
        : (p.x < size.x * 0.5 ? cornerRadii.w : cornerRadii.z);
}
float3 glassField(float2 p, float normalMix) {
    return nativeGlassRoundedField(p - size * 0.5, size * 0.5, float2(radiusAt(p)), normalMix);
}
float glassDistance(float2 p) { return glassField(p, 0.0).x; }
float2 glassNormal(float2 p) { return glassField(p, 0.0).yz; }
float2 glassDepthNormal(float2 p, float normalMix) { return glassField(p, normalMix).yz; }
"""
