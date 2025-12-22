@file:OptIn(ExperimentalForeignApi::class)

package com.multiplatform.applovin

import cocoapods.AppLovinSDK.ALMediationProviderMAX
import cocoapods.AppLovinSDK.ALSdk
import cocoapods.AppLovinSDK.ALSdkInitializationConfiguration
import cocoapods.AppLovinSDK.MAAd
import cocoapods.AppLovinSDK.MAAdDelegateProtocol
import cocoapods.AppLovinSDK.MAError
import cocoapods.AppLovinSDK.MAInterstitialAd
import cocoapods.AppLovinSDK.MAReward
import cocoapods.AppLovinSDK.MARewardedAd
import cocoapods.AppLovinSDK.MARewardedAdDelegateProtocol
import com.multiplatform.applovin.utils.AdListener
import com.multiplatform.applovin.utils.AdType
import kotlinx.cinterop.ExperimentalForeignApi
import platform.darwin.NSObject

actual class ApplovinMaxSDK {

    private var adListener: AdListener? = null
    private val interstitialAds = mutableMapOf<String, MAInterstitialAd>()
    private val rewardedAds = mutableMapOf<String, MARewardedAd>()
    private var rewardCallback: ((String, Int) -> Unit)? = null

    actual fun initialize(
        sdkKey: String,
        userIdentifier: String,
        onInitialized: () -> Unit,
        debugMode: Boolean
    ) {
        val configuration = ALSdkInitializationConfiguration
            .builderWithSdkKey(sdkKey)
            .build()

        val sdk = ALSdk.shared()
        sdk.settings.userIdentifier = userIdentifier
        sdk.setMediationProvider(ALMediationProviderMAX)
        sdk.settings.verboseLoggingEnabled = debugMode

        sdk.initializeWithConfiguration(configuration) { _ ->
            onInitialized()
        }
    }

    actual fun loadInterstitial(adUnitId: String) {
        val interstitial = interstitialAds.getOrPut(adUnitId) {
            MAInterstitialAd(adUnitId).apply {
                setDelegate(InterstitialAdDelegate(adUnitId, this@ApplovinMaxSDK))
            }
        }
        interstitial.loadAd()
    }

    actual fun showInterstitial(adUnitId: String) {
        val interstitial = interstitialAds[adUnitId]
        if (interstitial?.isReady() == true) {
            interstitial.showAd()
        }
    }

    actual fun isInterstitialReady(adUnitId: String): Boolean {
        return interstitialAds[adUnitId]?.isReady() ?: false
    }

    actual fun loadRewarded(adUnitId: String) {
        val rewarded = rewardedAds.getOrPut(adUnitId) {
            MARewardedAd.sharedWithAdUnitIdentifier(adUnitId).apply {
                setDelegate(RewardedAdDelegate(adUnitId, this@ApplovinMaxSDK))
            }
        }
        rewarded.loadAd()
    }

    actual fun showRewarded(
        adUnitId: String,
        placement: String?,
        customData: String?,
        onRewarded: (String, Int) -> Unit
    ) {
        rewardCallback = onRewarded
        val rewarded = rewardedAds[adUnitId]
        rewarded?.setDelegate(

        )
        if (rewarded?.isReady() == true) {
            rewarded.showAdForPlacement(placement, customData)
        }
    }

    actual fun isRewardedReady(adUnitId: String): Boolean {
        return rewardedAds[adUnitId]?.isReady() ?: false
    }

    actual fun setAdListener(listener: AdListener) {
        this.adListener = listener
    }

    private open class InterstitialAdDelegate(
        private val adUnitId: String,
        private val sdk: ApplovinMaxSDK
    ) : NSObject(), MAAdDelegateProtocol {

        override fun didLoadAd(ad: MAAd) {
            sdk.adListener?.onAdLoaded(adUnitId, AdType.INTERSTITIAL)
        }

        override fun didFailToLoadAdForAdUnitIdentifier(
            adUnitIdentifier: String,
            withError: MAError
        ) {
            sdk.adListener?.onAdLoadFailed(
                adUnitId,
                AdType.INTERSTITIAL,
                withError.message
            )
        }

        override fun didDisplayAd(ad: MAAd) {
            sdk.adListener?.onAdDisplayed(adUnitId, AdType.INTERSTITIAL)
        }

        override fun didFailToDisplayAd(ad: MAAd, withError: MAError) {
            sdk.adListener?.onAdDisplayFailed(
                adUnitId,
                AdType.INTERSTITIAL,
                withError.message
            )
            sdk.loadInterstitial(adUnitId)
        }

        override fun didClickAd(ad: MAAd) {
            sdk.adListener?.onAdClicked(adUnitId, AdType.INTERSTITIAL)
        }

        override fun didHideAd(ad: MAAd) {
            sdk.adListener?.onAdHidden(adUnitId, AdType.INTERSTITIAL)
            sdk.loadInterstitial(adUnitId)
        }
    }

    private class RewardedAdDelegate(
        private val adUnitId: String,
        private val sdk: ApplovinMaxSDK
    ) : NSObject(), MARewardedAdDelegateProtocol {

        override fun didLoadAd(ad: MAAd) {
            sdk.adListener?.onAdLoaded(adUnitId, AdType.REWARDED)
        }

        override fun didFailToLoadAdForAdUnitIdentifier(
            adUnitIdentifier: String,
            withError: MAError
        ) {
            sdk.adListener?.onAdLoadFailed(
                adUnitId,
                AdType.REWARDED,
                withError.message
            )
        }

        override fun didDisplayAd(ad: MAAd) {
            sdk.adListener?.onAdDisplayed(adUnitId, AdType.REWARDED)
        }

        override fun didFailToDisplayAd(ad: MAAd, withError: MAError) {
            sdk.adListener?.onAdDisplayFailed(
                adUnitId,
                AdType.REWARDED,
                withError.message
            )
            sdk.loadRewarded(adUnitId)
        }

        override fun didClickAd(ad: MAAd) {
            sdk.adListener?.onAdClicked(adUnitId, AdType.REWARDED)
        }

        override fun didHideAd(ad: MAAd) {
            sdk.adListener?.onAdHidden(adUnitId, AdType.REWARDED)
            sdk.loadRewarded(adUnitId)
        }

        override fun didRewardUserForAd(ad: MAAd, withReward: MAReward) {
            sdk.rewardCallback?.invoke(
                withReward.label,
                withReward.amount.toInt()
            )
        }
    }
}