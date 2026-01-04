package com.multiplatform.applovin.ads

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.multiplatform.applovin.utils.AdListener
import com.multiplatform.applovin.utils.AdType

actual class ApplovinInterstitialAd actual constructor(actual val adUnitId: String) {

    private var interstitialAds: MaxInterstitialAd? = null
    private var currentListener: AdListener? = null

    internal fun initialize() {
        if (interstitialAds == null) {
            interstitialAds = MaxInterstitialAd(adUnitId)
        }
    }

    actual val isReady: Boolean
        get() = interstitialAds?.isReady ?: false

    actual fun loadAd() {
        interstitialAds?.loadAd()
            ?: throw IllegalStateException("Ad not initialized. SDK must be initialized first.")
    }

    actual fun showAd() {
        if (interstitialAds?.isReady == true) {
            interstitialAds?.showAd()
        }
    }

    actual fun setListener(listener: AdListener) {

        currentListener = listener

        interstitialAds?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd) {
                listener.onAdLoaded(adUnitId, AdType.INTERSTITIAL)
            }

            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                listener.onAdLoadFailed(adUnitId, AdType.INTERSTITIAL, error.message)
            }

            override fun onAdDisplayed(ad: MaxAd) {
                listener.onAdDisplayed(adUnitId, AdType.INTERSTITIAL)
            }

            override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                listener.onAdDisplayFailed(adUnitId, AdType.INTERSTITIAL, error.message)
            }

            override fun onAdClicked(ad: MaxAd) {
                listener.onAdClicked(adUnitId, AdType.INTERSTITIAL)
            }

            override fun onAdHidden(ad: MaxAd) {
                listener.onAdHidden(adUnitId, AdType.INTERSTITIAL)
            }
        })
    }

    actual fun destroy() {
        interstitialAds?.destroy()
        interstitialAds = null
        currentListener = null
    }
}