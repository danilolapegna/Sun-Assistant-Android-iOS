import shared
import Combine
import CoreLocation

class UvIndexViewModelWrapper: ObservableObject, UvIndexListener {
    
    enum AirQualityIndexType {
        case usa
        case eu
    }
    
    struct AirQualityIndex {
        var index: Double?
        var type: AirQualityIndexType
        
        func formattedString() -> String {
            let nonNullableIndex = index ?? 0.0

            switch self.type {
            case .usa:
                return UVAssistant.shared.getUSAirQualityString(index: KotlinDouble(double: nonNullableIndex))
            case .eu:
                return UVAssistant.shared.getEUAirQualityString(index: KotlinDouble(double: nonNullableIndex))
            }
        }
        
        func getAirQualityLevel() -> AirQualityLevel {
            let isUS = self.type == .usa
            let kotlinIndex = KotlinDouble(double: self.index ?? 0.0)
            return AirQualityLevel.Companion().getLevelByIndex(index: kotlinIndex, isUS: isUS)
        }
    }
    
    @Published var locationPermissionStatus: CLAuthorizationStatus = .notDetermined
    
    @Published var messages: [UvIndexReportMessage] = []
    @Published var currentSkinType: SkinType? = nil
    
    @Published var uvIndex: Double? = nil
    @Published var lastFetchedAtTime: KotlinLong? = nil
    @Published var isDay: DarwinBoolean? = nil
    @Published var forecastPreview: [ForecastItem] = []
    @Published var airQualityIndex: AirQualityIndex? = nil
    @Published var locationReadable: String? = nil
    @Published var currentWeatherCode: Int? = nil
    
    @Published var currentTemperature: KotlinDouble? = nil
    @Published var currentSunshineDuration : KotlinDouble? = nil
    
    @Published var lastLatitude: Double? = nil
    @Published var lastLongitude: Double? = nil
    
    private let kotlinViewModel: shared.BaseUvIndexSourceModel
    
    init() {
        kotlinViewModel = shared.BaseUvIndexSourceModel()
        kotlinViewModel.listener = self
    }
    
    func refresh() {
        if let latitude = lastLatitude, let longitude = lastLongitude {
            fetchUvIndex(latitude: latitude, longitude: longitude)
        }
    }
    
    func fetchUvIndex(latitude: Double, longitude: Double) {
        lastLatitude = latitude
        lastLongitude = longitude
        kotlinViewModel.fetchUvIndex(latitude: latitude, longitude: longitude, ignoreCache: true)
    }
    
    func onDataLoaded(data: Any?) {
        if let uvData = data as? FullUVIndexResponse {
            if (self.uvIndex == nil || uvData.uvIndexCurrent()?.doubleValue != nil) {
                if let currentUv = uvData.uvIndexCurrent()?.doubleValue {
                    self.uvIndex = currentUv
                    self.lastFetchedAtTime = uvData.getUvIndexFetchedTimeInEpochMs()
                }
            }

            self.isDay = uvData.isDay.map { DarwinBoolean($0.boolValue) }

            if let euAirQuality = uvData.EUAirQualityCurrent()?.doubleValue {
                if (self.airQualityIndex == nil || uvData.EUAirQualityCurrent()?.doubleValue != nil) {
                    self.airQualityIndex = AirQualityIndex(index: euAirQuality, type: .eu)
                }
            } else if let usAirQuality = uvData.USAirQualityCurrent()?.doubleValue {
                if (self.airQualityIndex == nil || uvData.USAirQualityCurrent()?.doubleValue != nil) {
                    self.airQualityIndex = AirQualityIndex(index: usAirQuality, type: .usa)
                }
            }

            if let kotlinInt = uvData.currentWeather {
                self.currentWeatherCode = Int(kotlinInt.intValue)
            }
            
            if let kotlinTemp = uvData.currentTemperature {
                self.currentTemperature = KotlinDouble(double: kotlinTemp.doubleValue)
            }
            
            if let kotlinDuration = uvData.todaySunshineDuration {
                self.currentSunshineDuration = KotlinDouble(double: kotlinDuration.doubleValue)
            }

            let locationName = uvData.locationName
            if let locationName = locationName, !locationName.isEmpty {
                self.locationReadable = locationName
            } else if let latitude = lastLatitude as? Double, let longitude = lastLongitude as? Double {
                self.locationReadable = LocationUtils.shared.latLonString(latitude: KotlinDouble(double: latitude), longitude: KotlinDouble(double: longitude))
            } else {
                self.locationReadable = CommonStrings_User.shared.location_unknown
            }

            if let forecastPreview = uvData.forecastPreviews, !forecastPreview.isEmpty {
                self.forecastPreview = forecastPreview
            }
            updateFromUvIndex()
        } else {
            updateMessagesOnMainThread(with: CommonStrings_User.shared.uv_index_retrieval_error)
        }
    }

    
    func updateFromUvIndex() {
        let skinType = StorageUtil.getSelectedSkinType()
        let kotlinUvIndex: KotlinDouble? = self.uvIndex != nil ? KotlinDouble(double: self.uvIndex!) : nil
        let kotlinIsDay: KotlinBoolean? = self.isDay.map { KotlinBoolean(bool: $0.boolValue) }
        if let skinType = skinType {
            let kotlinWeatherCode = currentWeatherCode.map { KotlinInt(int: Int32($0)) }
            let uvIndexMessages = UVAssistant.shared
                .getUvIndexReportMessageStructured(uvIndex: kotlinUvIndex,
                                                   skinType: skinType,
                                                   weatherCode: kotlinWeatherCode,
                                                   isDay: kotlinIsDay,
                                                   airQualityLevel: self.airQualityIndex?.getAirQualityLevel())
            updateMessagesOnMainThread(uvIndexMessages)
            updateSkinTypeOnMainThread(skinType: skinType)
        }
    }
    
    private func updateSkinTypeOnMainThread(skinType: SkinType) {
        DispatchQueue.main.async {
            self.currentSkinType = skinType
        }
    }
    
    func onLoadStart() {
        if (self.uvIndex == nil
            && self.currentWeatherCode == nil
            && self.lastFetchedAtTime == nil) {
            updateMessagesOnMainThread(with: CommonStrings_User.shared.progress_loading)
        }
    }
    
    func onLoadEnd() {
        
    }
    
    func onException(e: KotlinException) {
        updateMessagesOnMainThread(with: e.description())
    }
    
    private func updateMessagesOnMainThread(_ newMessages: [UvIndexReportMessage]) {
        DispatchQueue.main.async {
            self.messages = newMessages
        }
    }
    
    private func updateMessagesOnMainThread(with message: String) {
        let message = UvIndexReportMessage(message: message, messageComponentType: .undefined)
        updateMessagesOnMainThread([message])
    }
}
