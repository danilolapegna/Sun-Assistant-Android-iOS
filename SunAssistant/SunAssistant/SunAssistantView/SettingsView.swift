//
//  SettingsView.swift
//  SunAssistant
//
//  Created by Danilo on 14/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct SettingsScreen: View {
    let settingsSections = SettingsHelper.shared.buildSettings()
    
    var body: some View {
        ZStack {
            Color.defaultBackgroundColor
                .edgesIgnoringSafeArea(.all)
            
                List {
                    ForEach(settingsSections, id: \.text) { section in
                        Section(header: Text(section.text)
                            .font(.caption)
                            .fontWeight(.semibold)) {
                                ForEach(section.getSettingsItems(), id: \.titleText) { settingsItem in
                                    VStack {
                                        switch settingsItem {
                                        case let switchItem as SwitchSettingsItem:
                                            SwitchSettingsRow(switchSettingsItem: switchItem)
                                            
                                        case let webUrlItem as WebUrlSettingItem:
                                            WebUrlSettingsRow(webUrlSettingsItem: webUrlItem)
                                            
                                        case let customClickItem as CustomClickSettingsItem:
                                            CustomClickSettingsRow(customClickSettingsItem: customClickItem)
                                            
                                        default:
                                            EmptyView()
                                        }
                                    }
                                }
                            }
                    }
                }
                .scrollContentBackground(.hidden)
                .background(Color.defaultBackgroundColor)
            
        }
        .environment(\.colorScheme, .light)
    }
}


struct SwitchSettingsRow: View {
    let switchSettingsItem: SwitchSettingsItem
    
    var body: some View {
        Toggle(isOn: .constant(Bool(truncating: switchSettingsItem.initialState()))) {
            Text(switchSettingsItem.titleText)
                .lineLimit(1)
                .foregroundColor(.black)
                .truncationMode(.tail)
        }
        .onChange(of: switchSettingsItem.initialState()) { newValue in
            switchSettingsItem.onCheckedChange(newValue)
        }
    }
}

struct WebUrlSettingsRow: View {
    let webUrlSettingsItem: WebUrlSettingItem
    
    var body: some View {
        Button(action: handleButtonClick) {
            HStack {
                Text(webUrlSettingsItem.titleText)
                    .lineLimit(2)
                    .truncationMode(.tail)
                Spacer()
            }
        }
    }
    
    private func handleButtonClick() {
        guard let url = URL(string: webUrlSettingsItem.webUrl) else { return }
        
        if webUrlSettingsItem.openInBrowser {
            UIApplication.shared.open(url)
        } else {
            // Present WebView with the URL
            // This part requires additional implementation
        }
    }
}

struct CustomClickSettingsRow: View {
    let customClickSettingsItem: CustomClickSettingsItem
    
    var body: some View {
        Button(action: {
            customClickSettingsItem.onClick()
        }) {
            HStack {
                Text(customClickSettingsItem.titleText)
                    .lineLimit(1)
                    .foregroundColor(.black)
                    .truncationMode(.tail)
                Spacer()
            }
        }
    }
}
