//
//  ForecastListView.swift
//  SunAssistant
//
//  Created by Danilo on 21/12/2023.
//  Copyright © 2023 orgName. All rights reserved.
//
import SwiftUI
import shared

extension ForecastItem: Identifiable {
    public var id: String { self.date }
}

struct ForecastListView: View {
    
    @EnvironmentObject private var viewModel: UvIndexViewModelWrapper
    
    var body: some View {
        
        ZStack {
            Color.defaultBackgroundColor
                .edgesIgnoringSafeArea(.all)
            VStack {
                if viewModel.locationPermissionStatus == .denied || viewModel.locationPermissionStatus == .restricted {
                    BaseCardView(message: CommonStrings_User.shared.location_authorization_denied)
                        .padding(.top)
                } else if viewModel.forecastPreview.isEmpty {
                    Text(CommonStrings_User.shared.forecast_retrieval_error)
                        .foregroundColor(.black)
                        .padding()
                } else {
                    List(viewModel.forecastPreview) { item in
                        ForecastItemView(item: item)
                            .listRowBackground(Color.clear)
                            .padding(.vertical, CommonDimen.shared.padding_8)
                    }
                    .refreshable {
                        viewModel.refresh()
                    }
                    .listStyle(PlainListStyle())
                }
                Spacer()
            }
        }
    }
}

struct ForecastItemView: View {
    var item: ForecastItem
    
    private func convertKotlinDoubleToSwift(_ kotlinDouble: KotlinDouble?) -> Double? {
        guard let kotlinDouble = kotlinDouble else {
            return nil
        }
        return Double(truncating: kotlinDouble as NSNumber)
    }
    
    private func formattedDate(isoDate: String) -> String {
        let isoDateFormatter = ISO8601DateFormatter()
        let dateFormatter = DateFormatter()
        
        if let date = isoDateFormatter.date(from: isoDate) {
            dateFormatter.dateFormat = "dd MMMM yyyy"
            return dateFormatter.string(from: date)
        }
        return isoDate
    }
    
    private func formattedUVIndex(_ value: Double?) -> String {
        guard let value = value else { return "Unknown" }
        return String(format: "%.2f", value)
    }
    
    var body: some View {
        VStack(spacing: 4) {
            HStack {
                VStack {
                    HStack {
                        VStack(alignment: .leading, spacing: 6) {
                            Text(item.date)
                                .foregroundColor(.black)
                                .font(.headline)
                            Text(UVAssistant.shared.getWeatherEmoji(weatherCode: item.weatherCode, nightEmojis: false))
                                .font(.system(size: 30))
                        }
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
                
                var uvIndexMaxSwift: Double? = convertKotlinDoubleToSwift(item.uvIndexMax)
                Spacer()
                VStack {
                    HStack {
                        Spacer()
                        VStack(alignment: .trailing, spacing: 10) {
                            HStack(spacing: 0) {
                                Text("UV peak: ")
                                    .foregroundColor(.black)
                                Text(formattedUVIndex(uvIndexMaxSwift))
                                    .bold()
                                    .foregroundColor(colorForValue(uvIndexMaxSwift))
                            }
                            HStack(spacing: 0) {
                                Text("Peak around: ")
                                    .foregroundColor(.black)
                                Text(item.getPeakTimeString())
                                    .bold()
                                    .foregroundColor(colorForValue(uvIndexMaxSwift))
                            }
                        }
                    }
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topTrailing)
 
            }
            var skinType = StorageUtil.getSelectedSkinType() ?? SkinType.type1Pale
            Text(item.getTemperatureString())
                .font(.system(size: 12))
                .foregroundColor(Color(hex: UVAssistant.shared.getTemperatureColor(temperature: item.temperatureMax)))
                .frame(maxWidth: .infinity, alignment: .leading)
                .layoutPriority(1)
            Text(item.getShortForecastMessage())
                .foregroundColor(Color.black)
                .frame(maxWidth: .infinity, alignment: .leading)
                .font(.system(size: 12))
                .padding(.top, 8)
                .foregroundColor(.black)
                .layoutPriority(1)
        }
        .padding(.vertical, 6)
        .environment(\.colorScheme, .light)
    }
}
