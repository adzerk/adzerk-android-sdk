package com.adzerk.android.sdk.sample;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adzerk.android.sdk.sample.VikingGenerator.Quote;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainView extends ActivityView {

    @Bind(R.id.recycler) RecyclerView recyclerView;

    public MainView(Activity activity, VikingGenerator generator) {
        super(activity);

        ButterKnife.bind(this, activity);
        setupWidgets(activity, generator);
    }

    private void setupWidgets(Context context, VikingGenerator generator) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new QuotesAdapter(generator));
    }

    public static class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {

        VikingGenerator generator;

        public QuotesAdapter(VikingGenerator generator) {
            this.generator = generator;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote_card, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Quote q = generator.getQuote(position);
            holder.txtName.setText(q.name);
            holder.txtQuote.setText(q.quote);
            setHeadShot(holder.imgHeadShot, q.url);
        }

        private void setHeadShot(ImageView imgView, String url) {
            Picasso.with(imgView.getContext())
                    .load(url)
                    .into(imgView);
        }

        @Override
        public int getItemCount() {
            return generator.getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.head_shot) ImageView imgHeadShot;
            @Bind(R.id.name) TextView txtName;
            @Bind(R.id.quote) TextView txtQuote;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
