package com.adzerk.android.sdk.sample2

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import com.adzerk.android.sdk.AdzerkSdk
import com.adzerk.android.sdk.rest.Decision
import com.adzerk.android.sdk.rest.DecisionResponse
import com.adzerk.android.sdk.rest.Placement
import com.adzerk.android.sdk.rest.Request

class MainViewModel(app: Application) : AndroidViewModel(app) {

    internal val TAG = MainViewModel::class.java.simpleName

    private val sdk = AdzerkSdk.getInstance()

    private val generator = VikingGenerator(app.resources)

    // display an ad every nth item
    val adModulus: Int = 5

    interface OnDecisionResponse {
        fun response(decision : Decision?)
    }

    inner class DecisionResponseListener(val callback: OnDecisionResponse) : AdzerkSdk.DecisionListener {
        override fun success(response: DecisionResponse) {
            callback.response(response.getDecision(DIV_1))
        }

        override fun error(error: AdzerkSdk.AdzerkError) {
            Log.d(TAG, "Error: $error.reason")
        }
    }

    fun getViking(position: Int) : VikingGenerator.Viking {
        val quotePosition = position - position / adModulus
        return generator.getViking(quotePosition);
    }

    // request ad placement for image format
    fun requestImgPlacement(callback: OnDecisionResponse) {
        sdk.requestPlacement(
                Request.Builder()
                        .addPlacement(Placement(DIV_1, NETWORK_ID, SITE_ID, AD_TYPE).setFlightId(IMG_FLIGHT_ID))
                        .build(),
                DecisionResponseListener(callback)
        );
    }

    // request ad placement for html format
    fun requestHtmlPlacement(callback: OnDecisionResponse) {
        sdk.requestPlacement(
                Request.Builder()
                        .addPlacement(Placement(DIV_1, NETWORK_ID, SITE_ID, AD_TYPE).setFlightId(HTML_FLIGHT_ID))
                        .build(),
                DecisionResponseListener(callback)
        )
    }

    // record ad click through
    fun makeImpression(impressionUrl : String) {
        sdk.impression(impressionUrl)
    }

    fun getItemCount() : Int {
        val vikingCount = generator.count()
        val adCount = if (adModulus > 1) vikingCount / adModulus else 0

        return vikingCount + adCount;
    }

    fun getItemType(position : Int) : Int {
        // Ads are never at top of list; if modulus is zero no ads are shown
        if (position == 0 || adModulus == 0) {
            return CONTENT_CARD_VIEW_TYPE
        }

        if (adModulus == 1) {
            return AD_CARD_IMG_VIEW_TYPE
        }

        // Every nth card is an Ad (first one is an Image)
        var viewType = if ((position + 1) % adModulus == 0) AD_CARD_IMG_VIEW_TYPE else CONTENT_CARD_VIEW_TYPE

        // And every other Ad is HTML
        if (viewType == AD_CARD_IMG_VIEW_TYPE) {
            viewType = if ((position + 1) % (2 * adModulus) == 0) AD_CARD_HTML_VIEW_TYPE else AD_CARD_IMG_VIEW_TYPE
        }

        return viewType
    }

    companion object {
        // network id
        internal val NETWORK_ID = 9792L

        // site id
        internal val SITE_ID = 306998L

        // flight containing only image ads
        internal val IMG_FLIGHT_ID = 699801

        // flight containing only html ads
        internal val HTML_FLIGHT_ID = 702688

        // div name for requesting placement
        internal val DIV_1 = "div1"

        // ad type for placement requests
        internal val AD_TYPE = 5

        // item types
        internal val CONTENT_CARD_VIEW_TYPE = 1
        internal val AD_CARD_IMG_VIEW_TYPE = 2
        internal val AD_CARD_HTML_VIEW_TYPE = 3

        fun create(activity: MainActivity): MainViewModel {
            return ViewModelProviders.of(activity).get(MainViewModel::class.java)
        }
    }
}

