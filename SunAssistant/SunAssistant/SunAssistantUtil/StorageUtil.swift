//
//  StorageUtil.swift
//  SunAssistant
//
//  Created by Danilo on 05/12/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class StorageUtil {
    static func getSelectedSkinType() -> SkinType? {
        let storage = IosStorage()
        if let stringValue = storage.get(key: CommonStrings_Keys.shared.SKIN_TYPE_KEY) {
            return SkinType.Companion().getByStringMatch(stringMatch: stringValue)
        }
        return nil
    }
}
