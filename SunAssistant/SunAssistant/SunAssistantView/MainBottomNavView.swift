import SwiftUI
import Foundation
import WebKit
import shared

struct MainBottomNavView: View {
    @State private var selection = 0
    
    private var toolbarTitle: String {
        switch selection {
        case 0: return CommonStrings_System.shared.home_menu_label
        case 1: return CommonStrings_System.shared.forecast_menu_label
        case 2: return CommonStrings_System.shared.store_menu_label
        case 3: return CommonStrings_System.shared.settings_menu_label
        default: return ""
        }
    }
    
    var body: some View {
        NavigationView {
            
            TabView(selection: $selection) {
                UVIndexMainView()
                    .tabItemLabel(selection: selection, index: 0, label: CommonStrings_System.shared.home_menu_label, icon: "home", useSystemIcon: false)
                    .tag(0)
                
                ForecastListView()
                    .tabItemLabel(selection: selection, index: 1, label: CommonStrings_System.shared.forecast_menu_label, icon: "calendar", useSystemIcon: false)
                    .tag(1)
                
     //           WebView(url: URL(string: CommonStrings_System.shared.sunassistant_url)!)
      //              .tabItemLabel(selection: selection, index: 2, label: CommonStrings_System.shared.store_menu_label, icon: "sunglasses")
         //           .tag(2)
                
                SettingsScreen()
                    .tabItemLabel(selection: selection, index: 3, label: CommonStrings_System.shared.settings_menu_label, icon: "info", useSystemIcon: false)
                    .tag(3)
            }
            .accentColor(Color(hex: CommonColors.shared.bottom_navbar_content_color_selected))
            .background(Color(hex: CommonColors.shared.bottom_navbar_background_color))
            .navigationBarTitle("", displayMode: .inline)
            .navigationBarItems(leading: Spacer(), trailing: Spacer())
            .toolbar {
                ToolbarItem(placement: .principal) {
                    ZStack {
                        Rectangle()
                            .foregroundColor(Color.toolbarBackgroundColor)
                            .frame(height: 44)
                        
                        Text(toolbarTitle)
                            .font(.headline)
                            .foregroundColor(Color.toolbarContentColor)
                    }
                }
            }
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}

extension View {
    func tabItemLabel(selection: Int, index: Int, label: String, icon: String, useSystemIcon: Bool = true) -> some View {
        self.tabItem {
            if useSystemIcon {
                Image(systemName: selection == index ? "\(icon).fill" : icon)
                    .foregroundColor(selection == index ? Color(hex: CommonColors.shared.bottom_navbar_content_color_selected) : Color(hex: CommonColors.shared.bottom_navbar_content_color_unselected))
            } else {
                Image(selection == index ? "\(icon)_selected" : icon) // Assumendo che le icone selezionate abbiano un suffisso '_selected'
                    .foregroundColor(selection == index ? Color(hex: CommonColors.shared.bottom_navbar_content_color_selected) : Color(hex: CommonColors.shared.bottom_navbar_content_color_unselected))
            }
            Text(label)
                .foregroundColor(selection == index ? Color(hex: CommonColors.shared.bottom_navbar_content_color_selected) : Color(hex: CommonColors.shared.bottom_navbar_content_color_unselected))
        }
    }
}

struct WebView: UIViewRepresentable {
    let url: URL
    
    func makeUIView(context: Context) -> some UIView {
        let webView = WKWebView()
        webView.load(URLRequest(url: url))
        return webView
    }
    
    func updateUIView(_ uiView: UIViewType, context: Context) { }
}
