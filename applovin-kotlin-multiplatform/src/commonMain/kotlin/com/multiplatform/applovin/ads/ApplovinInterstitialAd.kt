package com.multiplatform.applovin.ads

import com.multiplatform.applovin.utils.AdListener

expect class ApplovinInterstitialAd(
    adUnitId: String
) {
    val adUnitId: String
    val isReady: Boolean
    fun loadAd()
    fun showAd()
    fun setListener(listener: AdListener)
    fun destroy()
}