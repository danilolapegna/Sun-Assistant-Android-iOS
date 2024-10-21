import SwiftUI
import shared

struct LauncherView: View {
    @State private var isActive = false
    @State private var skinType: SkinType? = nil
    @State private var showSkinTypeSelection = false

    var body: some View {
        VStack {
            if let skinType = skinType {
                MainBottomNavView()
            } else {
                SplashScreenView()
                    .onAppear {
                        loadSkinTypeSelection()
                    }
            }
        }
        .sheet(isPresented: $showSkinTypeSelection) {
            // Pass a closure that updates skinType and dismisses the sheet
            SkinTypeGridView(isPresented: $showSkinTypeSelection, onConfirmAction: { selectedSkinType in
                self.skinType = selectedSkinType
                self.showSkinTypeSelection = false
            })
        }
    }

    private func loadSkinTypeSelection() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            withAnimation {
                self.skinType = StorageUtil.getSelectedSkinType()
                self.showSkinTypeSelection = self.skinType == nil
            }
        }
    }
}

struct SplashScreenView: View {
    var body: some View {
        ZStack {
            defaultBackgroundView()
                .edgesIgnoringSafeArea(.all)

            Image("logo")
                .resizable()
                .scaledToFit()
                .defaultPadding()
        }
    }
}

struct defaultBackgroundView: View {
    var body: some View {
        Color.launcherBackgroundColor
            .edgesIgnoringSafeArea(.all)
    }
}
