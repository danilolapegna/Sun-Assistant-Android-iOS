package com.sunassistant.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import colorResource
import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonDimen
import com.sunassistant.coderesources.CommonStrings_Keys
import com.sunassistant.coderesources.CommonStrings_User
import com.sunassistant.coderesources.enums.SkinType
import com.sunassistant.storage.AndroidStorage
import sunassistantDarkColor

interface SkinTypeSelectionListener {
    fun onGridSelectionConfirmed(selectedType: SkinType?)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SkinTypeSelectionScreen(listener: SkinTypeSelectionListener, context: Context) {
    var selectedType by remember {
        mutableStateOf(
            SkinType.getByStringMatch(
                AndroidStorage(context).get(CommonStrings_Keys.SKIN_TYPE_KEY)
            )
        )
    }

    val listState = rememberLazyListState()

    LaunchedEffect(selectedType) {
        if (selectedType != null) {
            val selectedIndex = SkinType.values().indexOf(selectedType)
            if (selectedIndex != -1 && !listState.layoutInfo.visibleItemsInfo.any { it.index == selectedIndex }) {
                listState.scrollToItem(selectedIndex)
            }
        }
    }

    Scaffold(
        topBar = {
            CustomToolbar(CommonStrings_User.skin_selection_title)
        },
        content = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = CommonDimen.padding_4.dp,
                    top = CommonDimen.padding_8.dp,
                    end = CommonDimen.padding_4.dp,
                    bottom = CommonDimen.padding_64.dp
                )
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = CommonStrings_User.skin_selection_subtitle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(CommonDimen.default_padding.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = TextUnit(CommonDimen.text_size_11.toFloat(), TextUnitType.Sp),
                    )
                }
                items(SkinType.values().size) { index ->
                    val type = SkinType.values()[index]
                    SkinTypeItem(
                        type = type,
                        isSelected = type == selectedType,
                        onSelect = { selectedType = type }
                    )
                }
            }
        },
        bottomBar = {
            val buttonColors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedType != null) sunassistantDarkColor() else Color.Gray,
                contentColor = if (selectedType != null) Color.White else Color.DarkGray
            )

            Button(
                colors = buttonColors,
                onClick = { listener.onGridSelectionConfirmed(selectedType) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CommonDimen.default_padding.dp),
                enabled = selectedType != null
            ) {
                Text(
                    text = CommonStrings_User.button_confirm,
                    fontSize = TextUnit(CommonDimen.text_size_14.toFloat(), TextUnitType.Sp),
                )
            }
        }
    )
}

@Composable
fun SkinTypeItem(type: SkinType, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .background(if (isSelected) Color.LightGray else Color.Transparent)
            .fillMaxWidth()
            .padding(
                horizontal = CommonDimen.default_padding.dp,
                vertical = CommonDimen.padding_8.dp
            )
            .clickable(onClick = onSelect)
    ) {
        Column(
            modifier = Modifier
                .background(colorResource(CommonColors.content_card_background_color))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CommonDimen.default_height_100.dp)
                    .background(colorResource(type.colorReferenceHex))
                    .padding(CommonDimen.padding_8.dp)
            )
            Column(modifier = Modifier.padding(CommonDimen.padding_8.dp))
            {
                Text(
                    text = type.typeName,
                    color = Color.Black,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = type.typeDescription,
                    color = Color.Black,
                    fontSize = TextUnit(CommonDimen.text_size_12.toFloat(), TextUnitType.Sp),
                )
            }
        }
    }
}


