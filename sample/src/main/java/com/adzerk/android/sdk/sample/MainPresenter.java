package com.adzerk.android.sdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.AdzerkSdk.DecisionListener;
import com.adzerk.android.sdk.rest.Content;
import com.adzerk.android.sdk.rest.Decision;
import com.adzerk.android.sdk.rest.DecisionResponse;
import com.adzerk.android.sdk.rest.Placement;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.sample.VikingGenerator.Quote;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit.RetrofitError;

public class MainPresenter {
    static final long NETWORK_ID = 9792L;
    static final long SITE_ID = 306998L;

    // flight containing only image ads
    static final int IMG_FLIGHT_ID = 699801;

    // flight containing only html ads
    static final int HTML_FLIGHT_ID = 702688;

    // the number of views to display
    static final int VIKING_COUNT = 20;

    // display an add every nth item
    static final int AD_MODULUS = 5;

    MainModel model;
    MainView view;

    public MainPresenter(MainModel model, MainView view) {
        this.model = model;
        this.view = view;
        view.setAdapter(new QuotesAdapter(
                new VikingGenerator(VIKING_COUNT),
                AD_MODULUS,
                AdzerkSdk.getInstance()
        ));
    }

    @Subscribe
    public void OnAdClick(AdClickEvent event) {
        Activity activity = view.getActivity();
        if (activity != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(event.url));
            activity.startActivity(intent);
        }
    }

    public static class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {
        static final String TAG = QuotesAdapter.class.getSimpleName();

        static final int CONTENT_CARD_VIEW_TYPE = 1;
        static final int AD_CARD_IMG_VIEW_TYPE = 2;
        static final int AD_CARD_HTML_VIEW_TYPE = 3;

        VikingGenerator generator;
        int adModulus;
        AdzerkSdk sdk;

        public QuotesAdapter(VikingGenerator generator, int adModulus, AdzerkSdk sdk) {
            this.generator = generator;
            this.adModulus = adModulus;
            this.sdk = sdk;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            switch (viewType) {
                case CONTENT_CARD_VIEW_TYPE:
                    return new ContentViewHolder(inflater.inflate(R.layout.item_quote_card, parent, false));

                case AD_CARD_IMG_VIEW_TYPE:
                    return new AdViewHolder(inflater.inflate(R.layout.item_quote_card, parent, false));

                case AD_CARD_HTML_VIEW_TYPE:
                    return new AdWebViewHolder(inflater.inflate(R.layout.item_html_ad_card, parent, false));

                default:
                    throw new IllegalArgumentException("Unsupported view type");
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder vh, int position) {

            switch (vh.getItemViewType()) {
                case CONTENT_CARD_VIEW_TYPE:
                    ContentViewHolder holder = (ContentViewHolder) vh;
                    int quotePosition = position - position / adModulus;
                    Quote q = generator.getQuote(quotePosition);
                    holder.txtName.setText(q.name);
                    holder.txtQuote.setText(q.quote);
                    setHeadShot(holder.imgView, q.url);
                    break;

                case AD_CARD_IMG_VIEW_TYPE:
                    final AdViewHolder adViewHolder = (AdViewHolder) vh;

                    sdk.requestPlacement(
                            new Request.Builder()
                                    .addPlacement(new Placement("div1", NETWORK_ID, SITE_ID, 5).setFlightId(IMG_FLIGHT_ID))
                                    .build(),
                            new DecisionListener() {

                                @Override
                                public void success(DecisionResponse response) {
                                    Decision decision = response.getDecision("div1");
                                    loadAdContent(adViewHolder, decision);
                                }

                                @Override
                                public void error(RetrofitError error) {
                                    Log.d(TAG, "Error: " + error.getMessage());
                                }
                            }
                    );
                    break;

                case AD_CARD_HTML_VIEW_TYPE:
                    final AdWebViewHolder adWebViewHolder = (AdWebViewHolder) vh;

                    sdk.requestPlacement(
                            new Request.Builder()
                                    .addPlacement(new Placement("div1", NETWORK_ID, SITE_ID, 5).setFlightId(HTML_FLIGHT_ID))
                                    .build(),
                            new DecisionListener() {
                                @Override
                                public void success(DecisionResponse response) {
                                    Decision decision = response.getDecision("div1");
                                    Content content = decision.getContents().get(0);
                                    String body = content.getBody();
                                    String html = "<html>" + body + "</html>";
                                    adWebViewHolder.webView.loadData(html, "text/html", "UTF-8");
                                    adWebViewHolder.setClickUrl(decision.getClickUrl());
                                    sdk.impression(decision.getImpressionUrl());
                                }

                                @Override
                                public void error(RetrofitError error) {
                                    Log.d(TAG, "Error: " + error.getMessage());
                                }
                            }
                    );

                    break;

                default:
                    break;
            }
        }

        /*
         * Populates the views with content from the ad.
         */
        private void loadAdContent(AdViewHolder vh, final Decision decision) {
            Content content = decision.getContents().get(0);

            // set the click through url:
            vh.setClickUrl(decision.getClickUrl());

            // display 'title' in name field
            vh.txtName.setText(content.getTitle());

            // display 'quote' from a JSON metadata returned with the ad content
            String quote = "Quote unavailable";
            Object quoteMetadata = content.getCreativeMetadata("quote");
            if (quoteMetadata != null) {
                quote = quoteMetadata.toString();
            }
            vh.txtQuote.setText(quote);

            // load the image from the URL in the ad into the ImageView
            if (content.isImage()) {
                ImageView imgView = vh.imgView;
                Picasso.with(imgView.getContext())
                        .load(content.getImageUrl())
                        .into(imgView, new Callback() {
                            @Override
                            public void onSuccess() {
                                // when the image loads successfully, the ad impression is triggered
                                sdk.impression(decision.getImpressionUrl());
                            }

                            @Override
                            public void onError() {
                                Log.d(TAG, "Ignoring ad load error");
                            }
                        });
            }
        }

        private void setHeadShot(ImageView imgView, String url) {
            Log.d(TAG, "Loading headshot from url: " + url);
            Picasso.with(imgView.getContext())
                    .load(url)
                    // TODO: add placeholder image - .placeholder(R.drawable ...
                  .into(imgView);
        }

        @Override
        public int getItemCount() {
            int contentCount = generator.getCount();                // content list items
            if (adModulus > 1) {
                contentCount += generator.getCount() / adModulus;   // ad list items
            }
            return contentCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || adModulus == 0) {
                // position === 0  => Ads are never at top of list
                // adModulus == 0  => Never show ads
                return CONTENT_CARD_VIEW_TYPE;
            }

            if (adModulus == 1) {
                return AD_CARD_IMG_VIEW_TYPE;
            }

            // Every nth card is an Ad (first one is an Image)
            int viewType = (position + 1) % adModulus == 0 ? AD_CARD_IMG_VIEW_TYPE : CONTENT_CARD_VIEW_TYPE;

            // And every other Ad is HTML
            if (viewType == AD_CARD_IMG_VIEW_TYPE) {
                viewType = (position + 1) % (2*adModulus) == 0 ? AD_CARD_HTML_VIEW_TYPE : AD_CARD_IMG_VIEW_TYPE;
            }

            return viewType;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        /**
         * View holder for app content. Each card displays a head shot of a person with a
         * name and a quote (using a Viking theme!).
         */
        public static class ContentViewHolder extends ViewHolder {

            @Bind(R.id.head_shot) ImageView imgView;
            @Bind(R.id.name) TextView txtName;
            @Bind(R.id.quote) TextView txtQuote;

            public ContentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                    imgView.setClipToOutline(true);
                    imgView.setOutlineProvider(new RoundedAvatarProvider());
                }
            }
        }

        /**
         * View holder for sponsored ads. This extends the ContentViewHolder to provide the ad
         * click-through functionality and display an indicator that the content is 'sponsored'.
         *
         * When a User clicks anywhere on the card, an AdClickEvent is fired. The result will start
         * an Intent to open the click-through URL provided by the ad.
         */
        public static class AdViewHolder extends ContentViewHolder {

            @Bind(R.id.sponsored) TextView txtSponsored;

            String clickUrl;

            public AdViewHolder(View itemView) {
                super(itemView);

                txtSponsored.setVisibility(View.VISIBLE);

                this.clickUrl = null;
            }

            public void setClickUrl(String clickUrl) {
                this.clickUrl = clickUrl;
            }

            @OnClick(R.id.card_view)
            public void onClick() {
                if (clickUrl != null) {
                    BusProvider.post(new AdClickEvent(clickUrl));
                }
            }
        }

        public static class AdWebViewHolder extends ViewHolder {

            @Bind(R.id.webView) WebView webView;
            @Bind(R.id.sponsored) TextView txtSponsored;

            String clickUrl;

            public AdWebViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);

                webView.getSettings().setJavaScriptEnabled(true);
                txtSponsored.setVisibility(View.VISIBLE);

                this.clickUrl = null;
            }

            public void setClickUrl(String clickUrl) {
                this.clickUrl = clickUrl;
            }

            @OnClick(R.id.card_view)
            public void onClick() {
                if (clickUrl != null) {
                    BusProvider.post(new AdClickEvent(clickUrl));
                }
            }

            @OnTouch(R.id.webView)
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    BusProvider.post(new AdClickEvent(clickUrl));
                }
                return true;
            }
        }
    }

    public static class AdClickEvent {
        String url;

        public AdClickEvent(String url) {
            this.url = url;
        }
    }
}

