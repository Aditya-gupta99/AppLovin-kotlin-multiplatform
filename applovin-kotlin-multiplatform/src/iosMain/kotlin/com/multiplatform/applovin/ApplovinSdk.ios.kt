@file:OptIn(ExperimentalForeignApi::class)

package com.multiplatform.applovin

import cocoapods.AppLovinSDK.ALSdk
import cocoapods.AppLovinSDK.ALSdkInitializationConfiguration
import cocoapods.AppLovinSDK.ALSdkSettings
import cocoapods.AppLovinSDK.sharedWithSettings
import com.multiplatform.applovin.ads.ApplovinAdView
import com.multiplatform.applovin.ads.ApplovinInterstitialAd
import com.multiplatform.applovin.ads.ApplovinRewardedAd
import com.multiplatform.applovin.banner.AdFormat
import kotlinx.cinterop.ExperimentalForeignApi

actual class ApplovinSdk {

    actual fun initialize(
        sdkKey: String,
        userIdentifier: String,
        onInitialized: () -> Unit,
        debugMode: Boolean
    ) {
        val settings = ALSdkSettings()
        settings.verboseLoggingEnabled = debugMode
        settings.userIdentifier = userIdentifier

        val configuration = ALSdkInitializationConfiguration
            .builderWithSdkKey(sdkKey)
            .build()

        ALSdk.sharedWithSettings(settings).let { sdk ->
            sdk?.initializeWithConfiguration(
                initializationConfiguration = configuration,
                completionHandler = {
                    onInitialized()
                }
            )
        }
    }

    actual fun createBanner(adUnitId: String): ApplovinAdView {
        return ApplovinAdView(adUnitId, AdFormat.BANNER).apply {
            initialize()
        }
    }

    actual fun createMrec(adUnitId: String): ApplovinAdView {
        return ApplovinAdView(adUnitId, AdFormat.MREC).apply {
            initialize()
        }
    }

    actual fun createLeader(adUnitId: String): ApplovinAdView {
        return ApplovinAdView(adUnitId, AdFormat.LEADER).apply {
            initialize()
        }
    }

    actual fun createInterstitial(adUnitId: String): ApplovinInterstitialAd {
        return ApplovinInterstitialAd(adUnitId).apply {
            initialize()
        }
    }

    actual fun createRewarded(adUnitId: String): ApplovinRewardedAd {
        return ApplovinRewardedAd(adUnitId).apply {
            initialize()
        }
    }

    actual fun destroy() {
    }
}