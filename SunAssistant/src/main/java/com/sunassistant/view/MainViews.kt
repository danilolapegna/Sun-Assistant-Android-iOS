package com.sunassistant.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import colorResource
import com.sunassistant.activity.SkinTypeSelectionNavigable
import com.sunassistant.R
import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonDimen
import com.sunassistant.coderesources.CommonStrings_Keys
import com.sunassistant.coderesources.CommonStrings_System
import com.sunassistant.coderesources.CommonStrings_User
import com.sunassistant.coderesources.enums.SkinType
import com.sunassistant.storage.AndroidStorage
import com.sunassistant.util.LocationUtils.latLonString
import com.sunassistant.util.TimeUtils.formatSecondsDuration
import com.sunassistant.util.UVAssistant
import com.sunassistant.util.UVAssistant.getTemperatureString
import com.sunassistant.util.UvIndexReportMessage
import com.sunassistant.util.UvIndexReportMessageComponentType
import com.sunassistant.viewmodel.MainActivityViewModel
import sunassistantDarkColor


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainActivityViewModel,
    skinTypeSelectionNavigable: SkinTypeSelectionNavigable,
    componentActivity: ComponentActivity
) {
    val navController = rememberNavController()
    val currentRoute = currentRoute(navController)
    val toolbarTitle = remember(currentRoute) {
        Screen.fromRoute(currentRoute)?.title ?: ""
    }
    Scaffold(
        topBar = {
            CustomToolbar(title = toolbarTitle)
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavigationGraph(
                navController,
                mainViewModel,
                skinTypeSelectionNavigable,
                componentActivity
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.History,
        // Screen.SunAssistantStore,
        Screen.Settings
    )
    BottomNavigation(
        backgroundColor = colorResource(CommonColors.bottom_navbar_background_color),
        contentColor = colorResource(CommonColors.bottom_navbar_content_color_unselected)
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                selectedContentColor = colorResource(CommonColors.bottom_navbar_content_color_selected),
                unselectedContentColor = colorResource(CommonColors.bottom_navbar_content_color_unselected)
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainViewModel: MainActivityViewModel,
    skinTypeSelectionNavigable: SkinTypeSelectionNavigable,
    componentActivity: ComponentActivity
) {
    val skinType =
        AndroidStorage(navController.context).get(CommonStrings_Keys.SKIN_TYPE_KEY)
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            UvIndexScreen(
                viewModel = mainViewModel,
                skinType = SkinType.getByStringMatch(skinType)!!,
                skinTypeSelectionNavigable = skinTypeSelectionNavigable,
                componentActivity
            )
        }
        composable(Screen.History.route) {
            ForecastList(mainViewModel, skinType, componentActivity)
        }
        composable(Screen.SunAssistantStore.route) {
            SunAssistantWebView(CommonStrings_System.sunassistant_url)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController.context)
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

sealed class Screen(val route: String, val icon: Int, val title: String) {
    object Home : Screen("home", R.drawable.ic_home, CommonStrings_System.home_menu_label)
    object History :
        Screen("history", R.drawable.ic_calendar, CommonStrings_System.forecast_menu_label)

    object SunAssistantStore :
        Screen("sunassistant_store", R.drawable.ic_sunglasses, CommonStrings_System.store_menu_label)

    object Settings :
        Screen("settings", R.drawable.ic_info, CommonStrings_System.settings_menu_label)

    companion object {
        fun fromRoute(route: String?): Screen? = when (route) {
            Home.route -> Home
            History.route -> History
            SunAssistantStore.route -> SunAssistantStore
            Settings.route -> Settings
            else -> null
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UvIndexScreen(
    viewModel: MainActivityViewModel,
    skinType: SkinType,
    skinTypeSelectionNavigable: SkinTypeSelectionNavigable,
    componentActivity: ComponentActivity
) {
    val uvIndex = viewModel.uvIndex.observeAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isLoading.observeAsState().value ?: false,
        onRefresh = {
            viewModel.refresh(
                componentActivity,
                forceUpdate = true,
                canRegisterForPermissions = false
            )
        }
    )
    val messages =
        GenerateMessages(
            uvIndex.value,
            skinType,
            isDay = viewModel.isDay.value,
            viewModel.airQualityIndex.value,
            weatherCode = viewModel.currentWeather.value
        )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        UvIndexMessagesView(
            messages,
            skinTypeSelectionNavigable,
            uvIndex.value,
            viewModel
        )
        PullRefreshIndicator(
            refreshing = viewModel.isLoading.observeAsState().value ?: false,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
        )
    }
}

@Composable
fun UvIndexMessagesView(
    messages: List<UvIndexReportMessage>,
    skinTypeSelectionNavigable: SkinTypeSelectionNavigable,
    uvIndexValue: Double?,
    viewModel: MainActivityViewModel
) {

    Column(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, CommonDimen.padding_56.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val goToSkinTypeGridListener = {
            skinTypeSelectionNavigable.onSkinTypeSelectionRequested()
        }

        messages.forEach { message ->
            UvIndexMessageItem(
                message,
                goToSkinTypeGridListener,
                uvIndexValue,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun UvIndexMessageItem(
    message: UvIndexReportMessage,
    goToSkinTypeGridListener: () -> Unit,
    uvIndexValue: Double?,
    viewModel: MainActivityViewModel
) {

    val lastFetchedTime = viewModel.lastFetchedTime.observeAsState()
    val currentLocationReadable = viewModel.locationReadable.observeAsState()
    val airQualityIndex = viewModel.airQualityIndex.observeAsState()
    val currentWeather = viewModel.currentWeather.observeAsState()
    val isDay = viewModel.isDay

    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = sunassistantDarkColor(),
        contentColor = Color.White
    )

    when (message.messageComponentType) {
        UvIndexReportMessageComponentType.UV_STATUS -> {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxSize()
                    .padding(
                        CommonDimen.default_padding.dp,
                        CommonDimen.default_padding.dp,
                        CommonDimen.default_padding.dp,
                        0.dp
                    )
            ) {
                val indexType = airQualityIndex.value?.type
                val indexEstimation = when (indexType) {
                    MainActivityViewModel.AirQualityIndexType.USA -> UVAssistant.getUSAirQualityString(
                        airQualityIndex.value?.index
                    )

                    MainActivityViewModel.AirQualityIndexType.EU -> UVAssistant.getEUAirQualityString(
                        airQualityIndex.value?.index
                    )

                    null -> CommonStrings_User.general_unknown
                }
                Column(
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        "\uD83C\uDF43 Air quality index",
                        fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        indexEstimation,
                        fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        "Current location \uD83D\uDCCD",
                        fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        (currentLocationReadable.value ?: latLonString(
                            viewModel.lastFetchedLocation?.latitude,
                            viewModel.lastFetchedLocation?.longitude
                        )),
                        fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                        textAlign = TextAlign.End,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularCounter(
                        targetValue = uvIndexValue?.toFloat(),
                        lastFetchedAtTime = lastFetchedTime.value,
                        currentWeather = currentWeather.value,
                        isDay = isDay.value
                    )

                    Box {
                        Column {
                            Text(
                                (getTemperatureString(viewModel.currentTemperature.value)),
                                fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                                textAlign = TextAlign.Start,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "\uD83C\uDF21\uFE0F Temperature now",
                                fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Start,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Column(

                        ) {
                            Text(
                                (formatSecondsDuration((viewModel.currentSunshineDuration.value))),
                                fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                                textAlign = TextAlign.End,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "Sunshine today \uD83C\uDF1E",
                                fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.End,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }


                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(
                            16.dp,
                            12.dp,
                            16.dp,
                            2.dp,
                        )
                    )
                    UvScreenTextView(message.message)
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        UvIndexReportMessageComponentType.SKIN_TYPE -> {
            ContentSection(
                message = message.message,
                additionalView = {
                    Button(
                        colors = buttonColors,
                        onClick = goToSkinTypeGridListener
                    ) {
                        Text(
                            CommonStrings_User.button_change_skin_type,
                            fontSize = TextUnit(CommonDimen.text_size_12.toFloat(), TextUnitType.Sp)
                        )
                    }
                },
                position = CardComponentPosition.BELOW,
                sectionTitle = CommonStrings_User.your_skin_type
            )
        }

        UvIndexReportMessageComponentType.UNDEFINED,
        UvIndexReportMessageComponentType.VITAMIN_D_RECOMMENDATIONS -> {
            if (message.message.isNotBlank()) {
                ContentCard(message.message, sectionTitle = CommonStrings_User.tips_card_title)
            }
        }

        else -> {
            if (message.message.isNotBlank()) {
                Text(
                    message.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(CommonDimen.default_padding.dp),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(CommonDimen.text_size_11.toFloat(), TextUnitType.Sp)
                )
            }
        }
    }
}

fun GenerateMessages(
    uvIndex: Double?,
    skinType: SkinType,
    isDay: Boolean? = null,
    value: MainActivityViewModel.AirQualityIndex?,
    weatherCode: Int?
): List<UvIndexReportMessage> {
    return UVAssistant.getUvIndexReportMessageStructured(
        uvIndex,
        skinType,
        isDay = isDay,
        airQualityLevel = value?.qualityLevel,
        weatherCode = weatherCode
    )
}

enum class CardComponentPosition {
    ABOVE, BELOW
}

@Composable
fun ContentSection(
    message: String,
    additionalView: @Composable (() -> Unit)? = null,
    position: CardComponentPosition = CardComponentPosition.BELOW,
    customHeight: Dp? = null,
    sectionTitle: String? = null
) {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (customHeight != null) Modifier.height(customHeight) else Modifier.wrapContentHeight()
            )
            .padding(
                horizontal = CommonDimen.default_padding.dp,
                vertical = CommonDimen.padding_8.dp
            )
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(CommonDimen.default_padding.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (position == CardComponentPosition.ABOVE && additionalView != null) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    additionalView.invoke()
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                if (sectionTitle != null) {
                    Text(
                        modifier = Modifier
                            .padding(CommonDimen.padding_4.dp),
                        text = sectionTitle,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TextUnit(CommonDimen.text_size_16.toFloat(), TextUnitType.Sp),
                    )
                }
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    maxLines = Int.MAX_VALUE,
                    color = Color.Black,
                    fontSize = TextUnit(CommonDimen.text_size_11.toFloat(), TextUnitType.Sp)
                )
            }
            if (position == CardComponentPosition.BELOW && additionalView != null) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    additionalView.invoke()
                }
            }
        }
    }
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun ContentCard(
    message: String,
    additionalView: @Composable (() -> Unit)? = null,
    position: CardComponentPosition = CardComponentPosition.BELOW,
    customHeight: Dp? = null,
    sectionTitle: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (customHeight != null) Modifier.height(customHeight) else Modifier.wrapContentHeight()
            )
            .padding(
                horizontal = 14.dp,
                vertical = CommonDimen.default_padding.dp
            )
            .border(
                1.dp,
                Color.Black,
                RoundedCornerShape(CommonDimen.default_corner_radius.dp)
            ),
        backgroundColor = colorResource(CommonColors.content_card_background_color),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(CommonDimen.default_padding.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (position == CardComponentPosition.ABOVE && additionalView != null) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    additionalView.invoke()
                }
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .wrapContentHeight()
            ) {
                if (sectionTitle != null) {
                    Text(
                        modifier = Modifier
                            .padding(CommonDimen.padding_4.dp),
                        text = sectionTitle,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TextUnit(CommonDimen.text_size_16.toFloat(), TextUnitType.Sp),
                    )
                }
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    maxLines = Int.MAX_VALUE,
                    color = Color.Black,
                    fontSize = TextUnit(CommonDimen.text_size_11.toFloat(), TextUnitType.Sp)
                )
            }
            if (position == CardComponentPosition.BELOW && additionalView != null) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    additionalView.invoke()
                }
            }
        }
    }
}


@Composable
fun UvScreenTextView(message: String, textColor: Color = Color.Black) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(10.dp,
                CommonDimen.default_padding.dp,
                10.dp,
                CommonDimen.default_padding.dp),
            textAlign = TextAlign.Center,
            color = textColor,
            fontSize = TextUnit(CommonDimen.text_size_11.toFloat(), TextUnitType.Sp)
        )
    }
}