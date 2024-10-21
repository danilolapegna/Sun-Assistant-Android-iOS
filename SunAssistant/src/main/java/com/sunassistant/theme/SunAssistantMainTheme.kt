import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonDimen

@Composable
fun SunAssistantMainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFFBB86FC),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5),
            background = colorResource(CommonColors.background_screen_color)
        )
    } else {
        lightColors(
            primary = Color(0xFF6200EE),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5),
            background = colorResource(CommonColors.background_screen_color)
        )
    }

    val typography = SunAssistantThemeElements.sunassistantTypography

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

object SunAssistantThemeElements {

    val poppinsFontFamily = FontFamily(
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_medium_italic, FontWeight.Medium, FontStyle.Italic),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_regular_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_semibold, FontWeight.SemiBold),
        Font(R.font.poppins_semibold_italic, FontWeight.SemiBold, FontStyle.Italic)
    )

    val sunassistantTypography = Typography(
        defaultFontFamily = poppinsFontFamily,
        body1 = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = CommonDimen.text_default.sp
        )
    )
}