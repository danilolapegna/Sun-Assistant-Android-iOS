package com.sunassistant.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colorResource
import com.sunassistant.coderesources.CommonDimen
import com.sunassistant.coderesources.CommonStrings_User
import com.sunassistant.rest.meteo.model.ForecastItem
import com.sunassistant.util.UVAssistant
import com.sunassistant.viewmodel.MainActivityViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForecastList(
    mainViewModel: MainActivityViewModel,
    skinType: String?,
    componentActivity: ComponentActivity
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = mainViewModel.isLoading.observeAsState().value ?: false,
        onRefresh = {
            mainViewModel.refresh(
                componentActivity,
                forceUpdate = true,
                canRegisterForPermissions = false
            )
        }
    )
    val items = mainViewModel.forecastPreviews
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (items.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, CommonDimen.padding_56.dp)
            ) {
                items(items) { item ->
                    ForecastItemView(item)
                }
            }
        } else {
            Text(
                modifier = Modifier
                    .padding(
                        CommonDimen.padding_16.dp,
                        CommonDimen.padding_16.dp,
                        CommonDimen.padding_16.dp,
                        CommonDimen.padding_16.dp
                    ),
                text = CommonStrings_User.forecast_retrieval_error,
                color = Color.Black,
                fontSize = TextUnit(CommonDimen.text_size_12.toFloat(), TextUnitType.Sp),
            )
        }
        PullRefreshIndicator(
            refreshing = mainViewModel.isLoading.observeAsState().value ?: false,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )
    }
}

@Composable
fun ForecastItemView(item: ForecastItem) {
    val formattedDate = item.date

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                0.dp,
                CommonDimen.default_padding.dp,
                0.dp,
                0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    CommonDimen.default_padding.dp,
                    0.dp,
                    CommonDimen.default_padding.dp,
                    0.dp
                ),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = formattedDate,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(CommonDimen.text_size_14.toFloat(), TextUnitType.Sp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = UVAssistant.getWeatherEmoji(item.weatherCode),
                    fontSize = TextUnit(22.toFloat(), TextUnitType.Sp),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.End // Aligns content to the end within the Column
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = TextUnit(
                                    CommonDimen.text_size_14.toFloat(),
                                    TextUnitType.Sp
                                )
                            )
                        ) {
                            append("UV peak: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = colorForValue(item.uvIndexMax?.toFloat()),
                                fontSize = TextUnit(
                                    CommonDimen.text_size_14.toFloat(),
                                    TextUnitType.Sp
                                )
                            )
                        ) {
                            val formattedUv = if(item.uvIndexMax == null) "Unknown" else "%.2f".format(item.uvIndexMax)
                            append(formattedUv)
                        }
                    },
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = TextUnit(
                                    CommonDimen.text_size_14.toFloat(),
                                    TextUnitType.Sp
                                )
                            )
                        ) {
                            append("Peak around: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = colorForValue(item.uvIndexMax?.toFloat()),
                                fontSize = TextUnit(
                                    CommonDimen.text_size_14.toFloat(),
                                    TextUnitType.Sp
                                )
                            )
                        ) {
                            append(item.getPeakTimeString())
                        }
                    },
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(
                CommonDimen.default_padding.dp,
                0.dp,
                CommonDimen.default_padding.dp,
                0.dp
            ),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Normal,
                        color = colorResource(UVAssistant.getTemperatureColor(temperature = item.temperatureMax)),
                        fontSize = TextUnit(
                            CommonDimen.text_size_10.toFloat(),
                            TextUnitType.Sp
                        )
                    )
                ) {
                    append(item.getTemperatureString())
                }
            },
            color = Color.Black
        )
        Text(
            text = item.getShortForecastMessage(),
            modifier = Modifier.padding(
                CommonDimen.default_padding.dp,
                CommonDimen.padding_8.dp,
                CommonDimen.default_padding.dp,
                CommonDimen.padding_16.dp
            ),
            fontWeight = FontWeight.Normal,
            fontSize = CommonDimen.text_size_10.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
        )
    }
}