//
//  LocationAndDataLoader.swift
//  SunAssistant
//
//  Created by Danilo on 22/12/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CoreLocation

class LocationAndDataLoader {
    private var locationManager = LocationManager.shared

    func startLocationUpdates(onAuthorizationChange: @escaping (CLAuthorizationStatus) -> Void, onLocationUpdate: @escaping (Double, Double) -> Void) {
        locationManager.startUpdatingLocation()
        locationManager.onAuthorizationChange = onAuthorizationChange
        locationManager.onLocationUpdate = onLocationUpdate
    }

    func loadDataFromLocation(latitude: Double?, longitude: Double?, using viewModel: UvIndexViewModelWrapper) {
        if let latitude = latitude, let longitude = longitude {
            viewModel.fetchUvIndex(latitude: latitude, longitude: longitude)
        }
    }
}
