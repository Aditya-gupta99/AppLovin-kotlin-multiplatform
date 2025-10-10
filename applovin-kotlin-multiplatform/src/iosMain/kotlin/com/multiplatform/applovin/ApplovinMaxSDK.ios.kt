package com.multiplatform.applovin

import com.multiplatform.applovin.utils.AdListener

actual class ApplovinMaxSDK {

    actual fun initialize(
        sdkKey: String,
        userIdentifier: String,
        onInitialized: () -> Unit,
        debugMode: Boolean
    ) {
    }

    actual fun loadInterstitial(adUnitId: String) {
    }

    actual fun showInterstitial(adUnitId: String) {
    }

    actual fun isInterstitialReady(adUnitId: String): Boolean {
        TODO("Not yet implemented")
    }

    actual fun loadRewarded(adUnitId: String) {
    }

    actual fun showRewarded(
        adUnitId: String,
        onRewarded: (String, Int) -> Unit
    ) {
    }

    actual fun isRewardedReady(adUnitId: String): Boolean {
        TODO("Not yet implemented")
    }

    actual fun setAdListener(listener: AdListener) {
    }
}