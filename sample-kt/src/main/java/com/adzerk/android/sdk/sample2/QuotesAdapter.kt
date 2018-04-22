package com.adzerk.android.sdk.sample2

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.adzerk.android.sdk.rest.Decision
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_html_ad_card.view.*
import kotlinx.android.synthetic.main.item_quote_card.view.*

class QuotesAdapter(internal val mainViewModel: MainViewModel) : RecyclerView.Adapter<QuotesAdapter.ViewHolder>() {

    internal val TAG = QuotesAdapter::class.java.simpleName

    override fun getItemViewType(position: Int): Int {
        return mainViewModel.getItemType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            MainViewModel.CONTENT_CARD_VIEW_TYPE -> return ContentViewHolder(inflater.inflate(R.layout.item_quote_card, parent, false))
            MainViewModel.AD_CARD_IMG_VIEW_TYPE -> return AdImgViewHolder(inflater.inflate(R.layout.item_quote_card, parent, false))
            MainViewModel.AD_CARD_HTML_VIEW_TYPE -> return AdWebViewHolder(inflater.inflate(R.layout.item_html_ad_card, parent, false))
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        when (vh.itemViewType) {
            MainViewModel.CONTENT_CARD_VIEW_TYPE -> displayContentCard(vh as ContentViewHolder, position)
            MainViewModel.AD_CARD_IMG_VIEW_TYPE -> displayImageAdCard(vh as AdImgViewHolder)
            MainViewModel.AD_CARD_HTML_VIEW_TYPE -> displayHtmlAdCard(vh as AdWebViewHolder)
        }
    }

    private fun displayContentCard(viewHolder: ContentViewHolder, position: Int) {
        val viking = mainViewModel.getViking(position)
        viewHolder.itemView.name.text = viking.name
        viewHolder.itemView.quote.text = viking.quote
        loadHeadShot(viewHolder.itemView.head_shot, viking.url)
    }

    private fun displayImageAdCard(viewHolder: AdImgViewHolder) {
        viewHolder.itemView.setOnClickListener {
            handleOnClick(it, viewHolder.clickUrl)
        }

        mainViewModel.requestImgPlacement(object : MainViewModel.OnDecisionResponse {
            override fun response(decision: Decision?) {
                if (decision != null) {
                    loadAdContent(viewHolder, decision)
                } else {
                    Log.d(TAG, "no decision returned for ad request")
                }
            }
        })
    }

    private fun displayHtmlAdCard(viewHolder: AdWebViewHolder) {
        viewHolder.itemView.setOnClickListener {
            handleOnClick(it, viewHolder.clickUrl)
        }
        viewHolder.itemView.webView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    handleOnClick(view, viewHolder.clickUrl)
                }
                return true;
            }
        })

        mainViewModel.requestHtmlPlacement(object : MainViewModel.OnDecisionResponse {
            override fun response(decision: Decision?) {
                if (decision == null) {
                    viewHolder.itemView.webView.loadData("<center><p>No HTML Ad to display</p></center>", "text/html", "UTF-8")
                    return
                }

                val body = decision.contents[0]!!.body
                viewHolder.itemView.webView.loadData("<html>$body</html>", "text/html", "UTF-8")
                viewHolder.setClickUrl(decision.clickUrl)
                mainViewModel.makeImpression(decision.impressionUrl)
            }
        })
    }

    private fun handleOnClick(view : View, clickUrl : String?) {
        val intent = Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(clickUrl))
        view.context.startActivity(intent)
    }

    /*
     * Populates the views with content from the ad.
     */
    private fun loadAdContent(vh: AdImgViewHolder, decision: Decision) {
        val content = decision.contents[0]

        // set the click through url:
        vh.setClickUrl(decision.clickUrl)

        // display 'title' in name field
        vh.itemView.name.text = content.title

        // display 'quote' from a JSON metadata returned with the ad content
        val metadataQuote = content.getCreativeMetadata("quote")
        vh.itemView.quote.text =  metadataQuote?.toString() ?: "Quote unavailable"

        // load the image from the URL in the ad into the ImageView
        if (content.isImage) {
            val imgView = vh.itemView.head_shot
            Picasso.get().load(content.imageUrl)
                .into(imgView, object : Callback {
                    override fun onSuccess() {
                        // when the image loads successfully, the ad impression is triggered
                        mainViewModel.makeImpression(decision.impressionUrl)
                    }

                    override fun onError(e: Exception) {
                        Log.e(TAG, "Ignoring ad load error", e)
                    }
                })
        }
    }

    private fun loadHeadShot(imgView: ImageView?, url: String) {
        Log.d(TAG, "Loading image from url: $url")
        Picasso.get().load(url).into(imgView)
    }

    override fun getItemCount(): Int {
        return mainViewModel.getItemCount()
    }

    open class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

    /**
     * View holder for app content.
     *
     * Each card displays a head shot of a person with a name and a quote (using a Viking theme!).
     */
    open class ContentViewHolder(override val containerView: View) : ViewHolder(containerView), LayoutContainer {

        init {
            itemView.head_shot.clipToOutline = true
            itemView.head_shot.outlineProvider = RoundedAvatarProvider()
        }
    }

    /**
     * View holder for sponsored ads (image type)
     *
     * This extends the ContentViewHolder to provide the ad click-through functionality and display an
     * indicator that the content is 'sponsored'.When a User clicks anywhere on the card, an Intent opens
     * the click-through URL provided by the ad.
     */
    class AdImgViewHolder(override val containerView: View) : ContentViewHolder(containerView), LayoutContainer {

        internal var clickUrl: String? = null

        init {
            itemView.sponsored.visibility = View.VISIBLE
        }

        fun setClickUrl(clickUrl: String) {
            this.clickUrl = clickUrl
        }
    }

    /**
     * View holder for sponsored ads (html type)
     *
     * This extends the base ViewHolder and uses a layout with a web view to display HTML content.
     * When a User clicks anywhere on the card, an Intent opens the click-through URL provided by the ad.
     */
    class AdWebViewHolder(override val containerView: View) : ViewHolder(containerView), LayoutContainer {

        internal var clickUrl: String? = null

        init {
            itemView.webView.settings.javaScriptEnabled = true
            itemView.html_sponsored.visibility = View.VISIBLE
        }

        fun setClickUrl(clickUrl: String) {
            this.clickUrl = clickUrl
        }
    }
}
