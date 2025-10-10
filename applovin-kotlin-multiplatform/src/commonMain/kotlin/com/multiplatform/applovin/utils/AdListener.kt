package com.multiplatform.applovin.utils

/**
 * Ad event listener interface
 */
interface AdListener {
    fun onAdLoaded(adUnitId: String, adType: AdType)
    fun onAdLoadFailed(adUnitId: String, adType: AdType, error: String)
    fun onAdDisplayed(adUnitId: String, adType: AdType)
    fun onAdDisplayFailed(adUnitId: String, adType: AdType, error: String)
    fun onAdClicked(adUnitId: String, adType: AdType)
    fun onAdHidden(adUnitId: String, adType: AdType)
}