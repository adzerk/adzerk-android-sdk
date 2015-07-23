package com.adzerk.android.sdk.sample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adzerk.android.sdk.AdzerkSdk;
import com.adzerk.android.sdk.AdzerkSdk.ResponseListener;
import com.adzerk.android.sdk.rest.Placement;
import com.adzerk.android.sdk.rest.Request;
import com.adzerk.android.sdk.rest.Response;
import com.adzerk.android.sdk.sample.VikingGenerator.Quote;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;

public class MainPresenter {

    static final int VIKING_COUNT = 10;
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

    public static class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {

        static final int CONTENT_CARD_VIEW_TYPE = 1;
        static final int AD_CARD_VIEW_TYPE = 2;

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

                case AD_CARD_VIEW_TYPE:
                    return new AdViewHolder(inflater.inflate(R.layout.item_ad_card, parent, false));

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
                    Quote q = generator.getQuote(position);
                    holder.txtName.setText(q.name);
                    holder.txtQuote.setText(q.quote);
                    setHeadShot(holder.imgHeadShot, q.url);
                    break;

                case AD_CARD_VIEW_TYPE:
                    sdk.request(
                            new Request.Builder(Arrays.asList(new Placement("div1", 9709L, 70464L, Arrays.asList(5))))
                                    .addKeyword("karnowski")
                                    .build(),
                            new ResponseListener() {
                                @Override
                                public void success(Response response) {
                                    Log.d("here", "Success!");
                                }

                                @Override
                                public void error(RetrofitError error) {
                                    Log.d("there", "Error: " + error.getMessage());
                                }
                            }
                    );
                    
                default:
                    break;
            }
        }

        private void setHeadShot(ImageView imgView, String url) {
            Picasso.with(imgView.getContext())
                    .load(url)
                    .into(imgView);
        }

        @Override
        public int getItemCount() {
            int contentCount = generator.getCount();
            if (adModulus > 1) {
                contentCount += generator.getCount() / adModulus;
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
                return AD_CARD_VIEW_TYPE;
            }

            // Every nth card is an Ad
            return (position + 1) % adModulus == 0 ? AD_CARD_VIEW_TYPE : CONTENT_CARD_VIEW_TYPE;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        public static class ContentViewHolder extends ViewHolder {
            @Bind(R.id.head_shot) ImageView imgHeadShot;
            @Bind(R.id.name) TextView txtName;
            @Bind(R.id.quote) TextView txtQuote;

            public ContentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public static class AdViewHolder extends ViewHolder {
            public AdViewHolder(View itemView) {
                super(itemView);
            }
        }

    }
}

