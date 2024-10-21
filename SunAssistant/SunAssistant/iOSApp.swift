import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        let appearance = UINavigationBarAppearance()
        appearance.backgroundColor = UIColor(Color(hex: CommonColors.shared.toolbar_background_color))
        appearance.titleTextAttributes = [.foregroundColor: UIColor.white]

        UINavigationBar.appearance().standardAppearance = appearance
        UINavigationBar.appearance().compactAppearance = appearance
        UINavigationBar.appearance().scrollEdgeAppearance = appearance
    }
    
	var body: some Scene {
    
		WindowGroup {
            LauncherView()
                .environmentObject(UvIndexViewModelWrapper())
            
        }
    }
}
