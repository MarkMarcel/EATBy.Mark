package com.marcel.eatbymark.app

import android.app.Application
import android.content.Context
import com.marcel.eatbymark.R
import dagger.hilt.android.HiltAndroidApp
import org.acra.BuildConfig
import org.acra.config.mailSender
import org.acra.data.StringFormat
import org.acra.ktx.initAcra

@HiltAndroidApp
class EATByMarkApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        initAcra {
            buildConfigClass = BuildConfig::class.java
            reportFormat = StringFormat.JSON

            mailSender {
                mailTo = "eatbymark@gmail.com"
                subject = getString(R.string.email_header_acra)
                body = getString(R.string.email_body_acra)
            }
        }
    }
}