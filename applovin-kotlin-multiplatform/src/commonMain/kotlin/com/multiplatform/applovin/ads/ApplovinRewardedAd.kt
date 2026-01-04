package com.multiplatform.applovin.ads

import com.multiplatform.applovin.utils.AdListener

expect class ApplovinRewardedAd(
    adUnitId: String
) {
    val adUnitId: String
    val isReady: Boolean
    fun loadAd()
    fun showAd()
    fun setListener(listener: AdListener)
    fun setRewardListener(onRewarded: (reward: String, amount: Int) -> Unit)
    fun destroy()
}