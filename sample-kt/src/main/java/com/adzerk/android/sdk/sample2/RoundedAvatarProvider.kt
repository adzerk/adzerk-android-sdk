package com.adzerk.android.sdk.sample2

import android.annotation.TargetApi
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

@TargetApi(21)
class RoundedAvatarProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(0, 0, view.width, view.height)
    }
}
