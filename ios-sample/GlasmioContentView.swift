import SwiftUI
import UIKit
import GlasmioComponents

/// Add this file to an iOS app target and link the generated GlasmioComponents framework.
struct GlasmioContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        GlasmioComponentsViewControllerKt.GlasmioComponentsViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct GlasmioPreviewAppView: View {
    var body: some View {
        GlasmioContentView().ignoresSafeArea()
    }
}
