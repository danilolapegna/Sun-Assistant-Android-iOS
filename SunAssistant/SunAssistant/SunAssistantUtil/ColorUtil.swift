//
//  ColorUtil.swift
//  SunAssistant
//
//  Created by Danilo on 21/12/2023.
//  Copyright © 2023 orgName. All rights reserved.
//
import Foundation
import SwiftUI
import shared

func colorForValue(_ value: Double?) -> Color {
    guard let value = value else { return .gray }
    return Color(hex: UVAssistant.shared.colorForValueString(value: KotlinFloat(floatLiteral: value)))
}
