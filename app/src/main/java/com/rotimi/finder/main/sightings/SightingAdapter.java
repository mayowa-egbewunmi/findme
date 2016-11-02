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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SightingAdapter extends RecyclerView.Adapter<SightingAdapter.MenuViewHolder> {

    private Context context;

    private List<SightingItem> sightings;

    public SightingAdapter(Context context, List<SightingItem> sightings) {
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

        SightingItem sightingItem = sightings.get(position);

//        String letter = String.valueOf(sightings.get(position).name.charAt(0));
//
////      Create a new TextDrawable for our image's background
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(letter, generator.getRandomColor());
//
//        holder.img.setImageDrawable(drawable);
//        holder.name.setText(sightings.get(position).name);
//        holder.price.setText("#"+ String.valueOf(sightings.get(position).price));
    }


    @Override
    public int getItemCount() {

        return sightings.size();
    }

    public void setData(List<SightingItem> sightings) {
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
