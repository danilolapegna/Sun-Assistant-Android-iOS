package com.sunassistant.activity

import SunAssistantMainTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.sunassistant.SunAssistantApp
import com.sunassistant.view.MainScreen
import com.sunassistant.viewmodel.MainActivityViewModel

interface SkinTypeSelectionNavigable {
    fun onSkinTypeSelectionRequested()
}

class MainActivity : BaseActivity(), SkinTypeSelectionNavigable {

    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunAssistantMainTheme {
                MainScreen(mainViewModel, this, this)
            }
        }
        mainViewModel.refresh(
            this,
            forceUpdate = true,
            canRegisterForPermissions = true
        )
    }

    override fun onResume() {
        super.onResume()
        if (SunAssistantApp.firstBoot) {
            doOnFirstBootActions()
            SunAssistantApp.firstBoot = false
        }
    }

    private fun doOnFirstBootActions() {
        //
    }

    override fun onSkinTypeSelectionRequested() {
        startActivity(Intent(this, SkinTypeSelectionActivity::class.java))
    }
}
