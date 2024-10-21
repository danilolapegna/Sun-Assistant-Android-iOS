import Foundation
import shared

public class IosStorage {

    let defaults = UserDefaults.standard

    public func save(key: String, value: String) {
        defaults.set(value, forKey: key)
    }

    public func get(key: String) -> String? {
        return defaults.string(forKey: key)
    }

    public func save(key: Float, value: Float) {
        defaults.set(value, forKey: String(key))
    }

    public func get(key: Float) -> Float? {
        return defaults.float(forKey: String(key))
    }

    public func save(key: Bool, value: Bool) {
        defaults.set(value, forKey: String(describing: key))
    }

    public func get(key: Bool) -> Bool? {
        return defaults.bool(forKey: String(describing: key))
    }
}
