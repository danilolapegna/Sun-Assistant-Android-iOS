import com.sunassistant.util.LocationUtils.latLonString
import kotlinx.serialization.*

@Serializable
data class GeocodingResponse(
    val latitude: Double,
    val longitude: Double,
    val city: String? = null
) {
    fun getGeocodingName() = city ?: latLonString(latitude, longitude)
}