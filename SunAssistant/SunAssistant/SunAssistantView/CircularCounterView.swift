//
//  CircularCounterView.swift
//  SunAssistant
//
//  Created by Danilo on 18/12/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CircularCounterView: View {
    var currentValue: Double?
    var lastFetchedAtTime: KotlinLong?
    var additionalMessage: String
    var currentWeather: Int?
    var isDay: Bool = true
    @State private var animatedValue: Double = 0

    var body: some View {
        VStack {
            ZStack {
                Circle()
                     .fill(colorForValue(currentValue).opacity(0.05))
                     .frame(width: 240,
                            height: 240
                     )
                
                Circle()
                    .stroke(lineWidth: 15)
                    .opacity(0.3)
                    .foregroundColor(colorForValue(currentValue))

                Circle()
                    .frame(width: 15, height: 15)
                    .foregroundColor(colorForValue(currentValue))
                    .offset(x: 0, y: -120)
                    .rotationEffect(Angle(degrees: animatedValue))

                VStack {
                    Divider()
                        .frame(width: 32, height: 1)
                    
                    Text(currentWeather != nil ? UVAssistant.shared.getWeatherEmoji(weatherCode: KotlinInt(value: Int32(currentWeather!)), nightEmojis: !isDay) : "❓")
                        .font(.system(size: CommonDimen.shared.text_size_24))
                    
                    Text(currentValue != nil ? String(format: "%.2f", currentValue!) : "-.--")
                        .foregroundColor(colorForValue(currentValue))
                        .font(.system(size: 32))
                        .fontWeight(.semibold)
                        .padding(2)
                    
                    Text(CommonStrings_User.shared.uv_index_subcircular)
                        .font(.system(size: 10))
                        .foregroundColor(colorForValue(currentValue))
                        .padding(2)
                    
                    Text(TimeUtils.shared.formatFetchedAtTime(epochMillis: lastFetchedAtTime))
                        .font(.system(size: 10))
                        .foregroundColor(colorForValue(currentValue))
                    
                    Divider()
                        .frame(width: 32, height: 1)
                        .padding(.top, 6)                }
            }
            .frame(width: CommonDimen.shared.circle_counter_size, height: CommonDimen.shared.circle_counter_size)
            .onAppear {
                if let value = currentValue {
                    withAnimation(.linear(duration: 1)) {
                        self.animatedValue = value * 30 // 360/12 = 30
                    }
                }
            }
        }
    }
}
