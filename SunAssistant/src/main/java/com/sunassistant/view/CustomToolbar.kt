package com.sunassistant.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import colorResource
import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonDimen

@Composable
fun CustomToolbar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = TextUnit(CommonDimen.text_size_14.toFloat(), TextUnitType.Sp)
            )
        },
        backgroundColor = colorResource(CommonColors.toolbar_background_color),
        contentColor = colorResource(CommonColors.toolbar_content_color),
        modifier = Modifier.fillMaxWidth()
    )
}