@file:OptIn(ExperimentalForeignApi::class)

package com.multiplatform.applovin.banner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.AppLovinSDK.MAAd
import cocoapods.AppLovinSDK.MAAdFormat
import cocoapods.AppLovinSDK.MAAdView
import cocoapods.AppLovinSDK.MAAdViewAdDelegateProtocol
import cocoapods.AppLovinSDK.MAError
import com.multiplatform.applovin.utils.BannerSize
import kotlinx.cinterop.ExperimentalForeignApi
import platform.darwin.NSObject

@Composable
actual fun BannerAdView(
    adUnitId: String,
    stopAutoRefresh: Boolean,
    bannerSize: BannerSize,
    modifier: Modifier,
    onAdLoaded: () -> Unit,
    onAdLoadFailed: (String) -> Unit,
    onAdClicked: () -> Unit
) {
    var adView by remember { mutableStateOf<MAAdView?>(null) }

    DisposableEffect(adUnitId) {
        onDispose {
//            adView?.destroy()
            adView = null
        }
    }

    UIKitView(
        factory = {
            val adFormat = when (bannerSize) {
                BannerSize.BANNER -> MAAdFormat.banner()
                BannerSize.LEADER -> MAAdFormat.leader()
                BannerSize.MREC -> MAAdFormat.mrec()
            }

            MAAdView(adUnitId, adFormat).apply {
                adView = this

                val delegate = BannerAdDelegate(
                    onAdLoaded = onAdLoaded,
                    onAdLoadFailed = onAdLoadFailed,
                    onAdClicked = onAdClicked
                )

                setDelegate(delegate)

                if (stopAutoRefresh) {
                    setExtraParameterForKey(
                        "allow_pause_auto_refresh_immediately",
                        "true"
                    )
                    stopAutoRefresh()
                }

                loadAd()
            }
        },
        modifier = modifier
    )
}

private class BannerAdDelegate(
    private val onAdLoaded: () -> Unit,
    private val onAdLoadFailed: (String) -> Unit,
    private val onAdClicked: () -> Unit
) : NSObject(), MAAdViewAdDelegateProtocol {

    override fun didLoadAd(ad: MAAd) {
        onAdLoaded()
    }

    override fun didFailToLoadAdForAdUnitIdentifier(
        adUnitIdentifier: String,
        withError: MAError
    ) {
        onAdLoadFailed(withError.message ?: "Unknown error")
    }

    override fun didClickAd(ad: MAAd) {
        onAdClicked()
    }

    override fun didDisplayAd(ad: MAAd) {}
    override fun didFailToDisplayAd(ad: MAAd, withError: MAError) {}
    override fun didHideAd(ad: MAAd) {}
    override fun didExpandAd(ad: MAAd) {}
    override fun didCollapseAd(ad: MAAd) {}
}
