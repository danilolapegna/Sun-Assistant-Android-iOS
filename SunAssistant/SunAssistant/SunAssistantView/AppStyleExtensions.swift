import shared
import SwiftUI

extension Color {

    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int = UInt64()
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (255, 0, 0, 0)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue:  Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
    
    static var defaultBackgroundColor: Color {
        let hexColor = CommonColors.shared.theme_color_background
        return Color(hex: hexColor)
    }

    static var launcherBackgroundColor: Color {
        let hexColor = CommonColors.shared.theme_color_background
        return Color(hex: hexColor)
    }

    static var defaultDarkColor: Color {
        let hexColor = CommonColors.shared.theme_color_dark
        return Color(hex: hexColor)
    }

    static var toolbarBackgroundColor: Color {
        let hexColor = CommonColors.shared.toolbar_background_color
        return Color(hex: hexColor)
    }

    static var toolbarContentColor: Color {
        let hexColor = CommonColors.shared.toolbar_content_color
        return Color(hex: hexColor)
    }
}

extension View {
    func defaultPadding() -> some View {
        self.padding(CommonDimen.shared.default_padding)
    }

    func defaultBackground() -> some View {
        self.background(Color.launcherBackgroundColor)
    }

    func logoPadding() -> some View {
        self.padding(CommonDimen.shared.padding_32)
    }
}
