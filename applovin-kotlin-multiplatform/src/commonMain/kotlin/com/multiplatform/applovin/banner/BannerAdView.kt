package com.multiplatform.applovin.banner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.applovin.utils.BannerSize

/**
 * Composable for displaying banner ads
 */
@Composable
expect fun BannerAdView(
    adUnitId: String,
    stopAutoRefresh : Boolean = false,
    bannerSize: BannerSize = BannerSize.BANNER,
    modifier: Modifier = Modifier,
    onAdLoaded: () -> Unit = {},
    onAdLoadFailed: (String) -> Unit = {},
    onAdClicked: () -> Unit = {}
)