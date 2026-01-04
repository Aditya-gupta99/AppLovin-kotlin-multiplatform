package com.multiplatform.applovin.ads

import com.multiplatform.applovin.banner.AdFormat
import com.multiplatform.applovin.utils.AdListener

expect class ApplovinAdView(
    adUnitId: String, adFormat: AdFormat
) {
    val adUnitId: String
    fun loadAd()
    fun startAutoRefresh()
    fun stopAutoRefresh()
    fun setListener(listener: AdListener)
    fun destroy()
}