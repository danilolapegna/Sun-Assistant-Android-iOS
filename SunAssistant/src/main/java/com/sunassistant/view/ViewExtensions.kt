import androidx.compose.ui.graphics.Color
import com.sunassistant.coderesources.CommonColors
import android.content.Context
import android.widget.Toast

fun sunassistantDarkColor(): Color {
    return colorResource(CommonColors.theme_color_dark)
}

fun colorResource(colorHex: String): Color {
    return Color(android.graphics.Color.parseColor(colorHex))
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}