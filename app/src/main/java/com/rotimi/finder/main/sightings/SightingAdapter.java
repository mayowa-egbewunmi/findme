package com.rotimi.finder.main.sightings;

/**
 * Created by mayowa on 2/19/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rotimi.finder.R;
import com.rotimi.finder.db.Reports;
import com.rotimi.finder.db.Sightings;
import com.rotimi.finder.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SightingAdapter extends RecyclerView.Adapter<SightingAdapter.MenuViewHolder> {

    private Context context;

    private List<Sightings> sightings;

    public SightingAdapter(Context context, List<Sightings> sightings) {
        this.context = context;
        this.sightings = sightings;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sightings, parent, false);
        MenuViewHolder menuView = new MenuViewHolder(layoutView);
        return menuView;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {

        Sightings sightingItem = sightings.get(position);
        holder.locationView.setText(sightingItem.location);
        holder.commentView.setText(sightingItem.comment);
        holder.dateView.setText(sightingItem.created_at);

        Picasso.with(context)
                .load(sightingItem.imageUrl)
                .placeholder(R.drawable.empty_profile2)
                .error(R.drawable.empty_profile2)
                .into(holder.imgView);
    }


    @Override
    public int getItemCount() {

        return sightings.size();
    }

    public void setData(List<Sightings> sightings) {
        this.sightings = sightings;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
 
        @BindView(R.id.sightings_img) ImageView imgView;
        @BindView(R.id.sightings_comment) TextView commentView;
        @BindView(R.id.sightings_date) TextView dateView;
        @BindView(R.id.sightings_location) TextView locationView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
