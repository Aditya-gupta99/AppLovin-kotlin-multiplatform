package com.multiplatform.applovin.banner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.multiplatform.applovin.utils.BannerSize

@Composable
actual fun BannerAdView(
    adUnitId: String,
    stopAutoRefresh : Boolean,
    bannerSize: BannerSize,
    modifier: Modifier,
    onAdLoaded: () -> Unit,
    onAdLoadFailed: (String) -> Unit,
    onAdClicked: () -> Unit
) {

    var adView by remember { mutableStateOf<MaxAdView?>(null) }

    DisposableEffect(adUnitId) {
        onDispose {
            adView?.destroy()
            adView = null
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val adFormat = when (bannerSize) {
                BannerSize.BANNER -> MaxAdFormat.BANNER
                BannerSize.LEADER -> MaxAdFormat.LEADER
                BannerSize.MREC -> MaxAdFormat.MREC
            }

            MaxAdView(adUnitId, adFormat).apply {
                adView = this

                setListener(object : MaxAdViewAdListener {
                    override fun onAdLoaded(ad: MaxAd) {
                        onAdLoaded()
                    }

                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                        onAdLoadFailed(error.message)
                    }

                    override fun onAdClicked(ad: MaxAd) {
                        onAdClicked()
                    }

                    override fun onAdDisplayed(ad: MaxAd) {}
                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {}
                    override fun onAdHidden(ad: MaxAd) {}
                    override fun onAdExpanded(ad: MaxAd) {}
                    override fun onAdCollapsed(ad: MaxAd) {}
                })

                // Set background or background color for banners to be fully functional
                if (stopAutoRefresh) {
                    setExtraParameter("allow_pause_auto_refresh_immediately", "true")
                    stopAutoRefresh()
                }

                loadAd()
            }
        },
        update = { view ->
            // Handle updates if needed
        }
    )

}