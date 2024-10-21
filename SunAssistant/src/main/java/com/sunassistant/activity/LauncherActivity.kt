package com.sunassistant.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sunassistant.R
import com.sunassistant.coderesources.CommonDimen
import com.sunassistant.coderesources.CommonStrings_Keys
import com.sunassistant.storage.AndroidStorage
import kotlinx.coroutines.delay

class LauncherActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LauncherScreen()
        }
    }

    @Composable
    fun LauncherScreen() {
        LaunchedEffect(key1 = true) {
            val context = this@LauncherActivity
            val skinType = AndroidStorage(context).get(CommonStrings_Keys.SKIN_TYPE_KEY)
            val activityRunClass = if (skinType == null) {
                SkinTypeSelectionActivity::class.java
            } else {
                MainActivity::class.java
            }
            startActivity(Intent(context, activityRunClass))
            finish()
        }
    }
}
