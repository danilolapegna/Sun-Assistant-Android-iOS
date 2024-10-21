package com.sunassistant.activity

import SunAssistantMainTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.sunassistant.coderesources.CommonStrings_Keys.SKIN_TYPE_KEY
import com.sunassistant.coderesources.enums.SkinType
import com.sunassistant.storage.AndroidStorage
import com.sunassistant.view.SkinTypeSelectionListener
import com.sunassistant.view.SkinTypeSelectionScreen

class SkinTypeSelectionActivity : BaseActivity(), SkinTypeSelectionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunAssistantMainTheme {
                SkinTypeSelectionScreen(this, this)
            }
        }
    }

    override fun onGridSelectionConfirmed(selectedType: SkinType?) {
        if (selectedType != null) {
            AndroidStorage(this).save(SKIN_TYPE_KEY, selectedType.stringCodeMatch)
            startActivity(Intent(this@SkinTypeSelectionActivity, MainActivity::class.java))
            finish()
        } else {
            //Todo: display toast. Shouldn't happen but hey
        }
    }
}
