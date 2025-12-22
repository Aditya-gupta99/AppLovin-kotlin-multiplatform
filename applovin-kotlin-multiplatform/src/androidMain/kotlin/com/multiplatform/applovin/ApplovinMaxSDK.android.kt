package com.multiplatform.applovin

import android.content.Context
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.multiplatform.applovin.utils.AdListener
import com.multiplatform.applovin.utils.AdType

actual class ApplovinMaxSDK {

    private var context: Context? = null
    private var adListener: AdListener? = null
    private val interstitialAds = mutableMapOf<String, MaxInterstitialAd>()
    private val rewardedAds = mutableMapOf<String, MaxRewardedAd>()
    private var rewardCallback: ((String, Int) -> Unit)? = null

    fun setContext(context: Context) {
        this.context = context
    }

    actual fun initialize(
        sdkKey: String,
        userIdentifier: String,
        onInitialized: () -> Unit,
        debugMode: Boolean
    ) {
        context ?: throw IllegalStateException("Context not set. Call setContext() first")

        val initConfig = AppLovinSdkInitializationConfiguration.builder(sdkKey)
            .setMediationProvider(AppLovinMediationProvider.MAX)
            .build()

        val applovinSdk = AppLovinSdk.getInstance(context)
        applovinSdk.settings.userIdentifier = userIdentifier
        applovinSdk.initialize(initConfig) {
            onInitialized()
        }
        applovinSdk.settings.setVerboseLogging(debugMode)
    }

    actual fun loadInterstitial(
        adUnitId: String
    ) {

        interstitialAds.getOrPut(adUnitId) {
            MaxInterstitialAd(adUnitId).apply {
                setListener(object : MaxAdListener {
                    override fun onAdLoaded(ad: MaxAd) {
                        adListener?.onAdLoaded(adUnitId, AdType.INTERSTITIAL)
                    }

                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                        adListener?.onAdLoadFailed(adUnitId, AdType.INTERSTITIAL, error.message)
                    }

                    override fun onAdDisplayed(ad: MaxAd) {
                        adListener?.onAdDisplayed(adUnitId, AdType.INTERSTITIAL)
                    }

                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                        adListener?.onAdDisplayFailed(adUnitId, AdType.INTERSTITIAL, error.message)
                        loadInterstitial(adUnitId) // Retry loading
                    }

                    override fun onAdClicked(ad: MaxAd) {
                        adListener?.onAdClicked(adUnitId, AdType.INTERSTITIAL)
                    }

                    override fun onAdHidden(ad: MaxAd) {
                        adListener?.onAdHidden(adUnitId, AdType.INTERSTITIAL)
                        loadInterstitial(adUnitId) // Preload next ad
                    }
                })
                loadAd()
            }
        }
    }

    actual fun showInterstitial(adUnitId: String) {
        val interstitial = interstitialAds[adUnitId]
        if (interstitial?.isReady == true) {
            interstitial.showAd()
        }
    }

    actual fun isInterstitialReady(adUnitId: String): Boolean {
        return interstitialAds[adUnitId]?.isReady ?: false
    }

    actual fun loadRewarded(adUnitId: String) {
        rewardedAds.getOrPut(adUnitId) {
            MaxRewardedAd.getInstance(adUnitId).apply {
                setListener(object : MaxRewardedAdListener {
                    override fun onAdLoaded(ad: MaxAd) {
                        adListener?.onAdLoaded(adUnitId, AdType.REWARDED)
                    }

                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                        adListener?.onAdLoadFailed(adUnitId, AdType.REWARDED, error.message)
                    }

                    override fun onAdDisplayed(ad: MaxAd) {
                        adListener?.onAdDisplayed(adUnitId, AdType.REWARDED)
                    }

                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
                        adListener?.onAdDisplayFailed(adUnitId, AdType.REWARDED, error.message)
                        loadRewarded(adUnitId) // Retry loading
                    }

                    override fun onAdClicked(ad: MaxAd) {
                        adListener?.onAdClicked(adUnitId, AdType.REWARDED)
                    }

                    override fun onAdHidden(ad: MaxAd) {
                        adListener?.onAdHidden(adUnitId, AdType.REWARDED)
                        loadRewarded(adUnitId) // Preload next ad
                    }

                    override fun onUserRewarded(ad: MaxAd, reward: MaxReward) {
                        rewardCallback?.invoke(reward.label, reward.amount)
                    }
                })
                loadAd()
            }
        }
    }

    actual fun showRewarded(
        adUnitId: String,
        onRewarded: (String, Int) -> Unit
    ) {
        rewardCallback = onRewarded
        val rewarded = rewardedAds[adUnitId]
        if (rewarded?.isReady == true) {
            rewarded.showAd()
        }
    }

    actual fun isRewardedReady(adUnitId: String): Boolean {
        return rewardedAds[adUnitId]?.isReady ?: false
    }

    actual fun setAdListener(listener: AdListener) {
        this.adListener = listener
    }

}