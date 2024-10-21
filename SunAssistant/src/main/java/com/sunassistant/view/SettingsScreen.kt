package com.sunassistant.view

import SunAssistantThemeElements
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colorResource
import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonDimen
import com.sunassistant.util.CustomClickSettingsItem
import com.sunassistant.util.SettingsHelper
import com.sunassistant.util.SwitchSettingsItem
import com.sunassistant.util.WebUrlSettingItem

@Composable
fun SettingsScreen(context: Context) {
    val settingsSections = SettingsHelper.buildSettings()
    LazyColumn {
        settingsSections.forEach { section ->
            item {
                Text(
                    text = section.text,
                    modifier = Modifier
                        .padding(
                            horizontal = CommonDimen.default_padding.dp,
                            vertical = CommonDimen.padding_8.dp
                        ),
                    style = MaterialTheme.typography.caption, // Smaller text for sections
                    color = Color.Black, // Adjust color if needed
                    fontWeight = FontWeight.SemiBold
                )
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }

            section.getSettingsItems().forEach { settingsItem ->
                when (settingsItem) {
                    is SwitchSettingsItem -> {
                        item {
                            SettingsRow(
                                text = settingsItem.titleText,
                                onClick = {}
                            ) {
                                Switch(
                                    checked = settingsItem.initialState(),
                                    onCheckedChange = settingsItem.onCheckedChange
                                )
                            }
                        }
                    }

                    is WebUrlSettingItem -> {
                        item {
                            SettingsRow(
                                text = settingsItem.titleText,
                                onClick = {
                                    if (settingsItem.openInBrowser) {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(settingsItem.webUrl)
                                        }
                                        context.startActivity(intent)
                                    } else {
                                        // TODO: Implement WebView display
                                        throw NotImplementedError("WebView display not implemented")
                                    }
                                },
                            ) {
                                // Web URL specific UI or logic
                            }
                        }
                    }

                    is CustomClickSettingsItem -> {
                        item {
                            SettingsRow(
                                text = settingsItem.titleText,
                                onClick = settingsItem.onClick
                            ) {
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(
    text: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(CommonColors.sunassistant_beige_lighter).copy(alpha = 0.95f)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ClickableText(
            text = AnnotatedString(text),
            onClick = { onClick.invoke() },
            modifier = Modifier
                .padding(CommonDimen.default_padding.dp)
                .weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontFamily = SunAssistantThemeElements.poppinsFontFamily,
                color = Color.Black,
                fontSize = CommonDimen.text_default.sp,
            )
        )
        content()
    }
    Divider(
        color = Color.LightGray,
        thickness = 1.dp
    )
}