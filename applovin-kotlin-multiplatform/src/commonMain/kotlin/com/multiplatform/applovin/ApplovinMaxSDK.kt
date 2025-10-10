package com.multiplatform.applovin

import com.multiplatform.applovin.utils.AdListener

expect class ApplovinMaxSDK {

    /**
     * Initialize the AppLovin SDK
     * @param sdkKey Your AppLovin SDK key
     */
    fun initialize(
        sdkKey: String,
        userIdentifier: String,
        onInitialized: () -> Unit,
        debugMode: Boolean = false
    )

    /**
     * Load an interstitial ad
     * @param adUnitId The ad unit ID
     */
    fun loadInterstitial(adUnitId: String)

    /**
     * Show an interstitial ad
     * @param adUnitId The ad unit ID
     */
    fun showInterstitial(adUnitId: String)

    /**
     * Check if interstitial ad is ready
     * @param adUnitId The ad unit ID
     */
    fun isInterstitialReady(adUnitId: String): Boolean

    /**
     * Load a rewarded ad
     * @param adUnitId The ad unit ID
     */
    fun loadRewarded(adUnitId: String)

    /**
     * Show a rewarded ad
     * @param adUnitId The ad unit ID
     */
    fun showRewarded(adUnitId: String, onRewarded: (reward: String, amount: Int) -> Unit)

    /**
     * Check if rewarded ad is ready
     * @param adUnitId The ad unit ID
     */
    fun isRewardedReady(adUnitId: String): Boolean

    /**
     * Set ad listener
     */
    fun setAdListener(listener: AdListener)
}