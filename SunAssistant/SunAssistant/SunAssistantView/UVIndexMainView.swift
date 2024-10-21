import shared
import SwiftUI
import CoreLocation

struct UVIndexMainView: View {
    @EnvironmentObject private var viewModel: UvIndexViewModelWrapper
    
    @State private var showSkinTypeSelection = false
    @State private var latitude: Double? = nil
    @State private var longitude: Double? = nil
    @State private var lastDataLoadTime: Date? = nil
    
    //TODO: refactor and use this via composition
    private var dataLoader = LocationAndDataLoader()
    
    private var locationManager = LocationManager.shared
    
    var body: some View {
        ScrollView {
            Color.defaultBackgroundColor
                .edgesIgnoringSafeArea(.all)
            VStack {
                if viewModel.locationPermissionStatus == .denied ||
                    viewModel.locationPermissionStatus == .restricted ||
                    viewModel.locationPermissionStatus == .notDetermined {
                    BaseCardView(message: CommonStrings_User.shared.location_authorization_denied)
                } else {
                    ForEach(viewModel.messages, id: \.message) { message in
                        contentForMessage(message)
                    }
                }
            }
        }
        .refreshable {
            loadData(forceRefresh: true)
        }
        .onAppear(perform: setupView)
        .onChange(of: showSkinTypeSelection, perform: handleSkinTypeSelectionChange)
        .onDisappear(perform: cleanUp)
        .sheet(isPresented: $showSkinTypeSelection) {
            SkinTypeGridView(isPresented: $showSkinTypeSelection)
        }.defaultBackground()
            .environment(\.colorScheme, .light)
    }
    
    private func setupView() {
        startLocationUpdates()
        loadData()
    }
    
    private func handleSkinTypeSelectionChange(_ newValue: Bool) {
        if !newValue {
            viewModel.updateFromUvIndex()
        }
    }
    
    private func cleanUp() {
        locationManager.onLocationUpdate = nil
        locationManager.onAuthorizationChange = nil
    }
    
    private func startLocationUpdates() {
        locationManager.startUpdatingLocation()
        viewModel.locationPermissionStatus = locationManager.authorizationStatus
        locationManager.onAuthorizationChange = { newStatus in
            viewModel.locationPermissionStatus = newStatus
        }
        locationManager.onLocationUpdate = { newLatitude, newLongitude in
            loadDataFromLocation(latitude: newLatitude, longitude: newLongitude)
        }
    }
    
    private func loadData(forceRefresh: Bool = false) {
        loadDataFromLocation(latitude: self.latitude, longitude: self.longitude, forceRefresh: forceRefresh)
    }
    
    private func loadDataFromLocation(latitude: Double?, longitude: Double?, forceRefresh: Bool = false) {
        guard let latitude = latitude, let longitude = longitude else { return }
        
        let currentTime = Date()
        if !coordinatesAreEffectivelyEqual(lat1: self.latitude, long1: self.longitude, lat2: latitude, long2: longitude) || lastDataLoadTime == nil || currentTime.timeIntervalSince(lastDataLoadTime!) > 300 || forceRefresh {
            self.latitude = latitude
            self.longitude = longitude
            lastDataLoadTime = currentTime // Aggiorna il timestamp dell'ultimo caricamento dei dati
            viewModel.fetchUvIndex(latitude: latitude, longitude: longitude)
        }
    }
    
    private func coordinatesAreEffectivelyEqual(lat1: Double?, long1: Double?, lat2: Double?, long2: Double?) -> Bool {
        guard let lat1 = lat1, let long1 = long1, let lat2 = lat2, let long2 = long2 else {
            return false
        }
        
        let isLatitudeEqual = round(lat1 * 1000) == round(lat2 * 1000)
        let isLongitudeEqual = round(long1 * 1000) == round(long2 * 1000)
        
        return isLatitudeEqual && isLongitudeEqual
    }
    
    
    @ViewBuilder
    private func contentForMessage(_ message: UvIndexReportMessage) -> some View {
        switch message.messageComponentType {
        case .skinType:
            UvIndexMessageSection(
                message: message.message,
                additionalView: { ChangeSkinTypeButton { showSkinTypeSelection = true } },
                position: .below,
                sectionTitle: CommonStrings_User.shared.your_skin_type
            )
        case .uvStatus:
            let additionalMessage = viewModel.messages.first { $0.messageComponentType == .uvStatus }?.message ?? ""
            ZStack(alignment: .top) {
                VStack {
                    Spacer()
                    HStack {
                        VStack(alignment: .leading, spacing: 6) {
                            Text("Air quality index")
                                .font(.system(size: 10, weight: .semibold))
                                .foregroundColor(.black)
                            Text(viewModel.airQualityIndex?.formattedString() ?? CommonStrings_User.shared.general_unknown)
                                .font(.system(size: 10))
                                .foregroundColor(.black)
                        }
                        Spacer()
                        VStack(alignment: .trailing, spacing: 6) {
                            Text("Current location")
                                .font(.system(size: 10, weight: .semibold))
                                .foregroundColor(.black)
                            Text(viewModel.locationReadable ?? CommonStrings_User.shared.location_unknown)
                                .font(.system(size: 10))
                                .foregroundColor(.black)
                        }
                    }
                    .padding(.horizontal, CommonDimen.shared.padding_16)
                    .padding(.vertical, CommonDimen.shared.padding_4)
                    
                    CircularCounterView(currentValue: viewModel.uvIndex,
                                        lastFetchedAtTime: viewModel.lastFetchedAtTime,
                                        additionalMessage: additionalMessage,
                                        currentWeather: viewModel.currentWeatherCode,
                                        isDay: viewModel.isDay != nil ? viewModel.isDay!.boolValue : true)
                    .padding(.bottom, CommonDimen.shared.padding_16)
                    .padding(.leading, CommonDimen.shared.padding_16)
                    .padding(.trailing, CommonDimen.shared.padding_16)
                    .zIndex(1)
                    
                    // New section for temperature and sunshine duration
                    HStack {
                        VStack(alignment: .leading, spacing: 6) {
                            Text(UVAssistant.shared.getTemperatureString(temperature: viewModel.currentTemperature))
                                .font(.system(size: 10))
                                .foregroundColor(.black)
                            Text("🌡️ Temperature now")
                                .font(.system(size: 10, weight: .semibold))
                                .foregroundColor(.black)
                        }
                        Spacer()
                        VStack(alignment: .trailing, spacing: 6) {
                            Text(TimeUtils.shared.formatSecondsDuration(secondsInput: viewModel.currentSunshineDuration)) // Assuming a method to format sunshine duration
                                .font(.system(size: 10))
                                .foregroundColor(.black)
                            Text("Sunshine today 🌞")
                                .font(.system(size: 10, weight: .semibold))
                                .foregroundColor(.black)
                        }
                    }
                    .padding(.horizontal, CommonDimen.shared.padding_16)
                    
                    Divider()
                        .padding(.horizontal, 32)
                        .padding(.top, 20)
                        .padding(.bottom, 20)
                    
                    Text(additionalMessage)
                        .multilineTextAlignment(.center)
                        .foregroundColor(.black)
                        .font(.system(size: 15))
                        .padding([.bottom], CommonDimen.shared.padding_8)
                        .padding([.horizontal], 20)
                    
                    Divider()
                        .padding(.horizontal, 32)
                        .padding(.top, CommonDimen.shared.padding_8)
                }
            }
            
            
        case .undefined:
            BaseCardView(message: message.message)
            
        case .vitaminDRecommendations:
            BaseCardView(message: message.message, sectionTitle: CommonStrings_User.shared.tips_card_title)
            
        default:
            Text(message.message)
                .padding(CommonDimen.shared.default_padding)
                .foregroundColor(.black)
                .font(.system(size: 15))
        }
    }
}

