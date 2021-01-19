package io.zluan.teslachargingreminder

import android.app.Service
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.zluan.teslachargingreminder.extension.PREF_FILE_KEY
import io.zluan.teslachargingreminder.extension.getUseCustomViewSetting
import io.zluan.teslachargingreminder.extension.setUseCustomViewSetting
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val sharedPrefs: SharedPreferences by lazy {
        getSharedPreferences(PREF_FILE_KEY, Service.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        use_custom_view.apply {
            isChecked = sharedPrefs.getUseCustomViewSetting()
            setOnCheckedChangeListener { _, isChecked ->
                sharedPrefs.setUseCustomViewSetting(isChecked)
            }
        }
    }
}
