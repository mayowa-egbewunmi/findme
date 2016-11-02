package com.rotimi.finder.main.found;

/**
 * Created by mayowa on 2/19/16.
 */

import android.content.Context;
import android.content.Intent;
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
import com.rotimi.finder.main.ReportDetails;
import com.rotimi.finder.main.publicreports.ReportItem;
import com.rotimi.finder.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoundAdapter extends RecyclerView.Adapter<FoundAdapter.MenuViewHolder> {

    private Context context;

    private List<Reports> foundItems;

    public FoundAdapter(Context context, List<Reports> foundItems) {
        this.context = context;
        this.foundItems = foundItems;
    }

    @Override
    public FoundAdapter.MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_found, parent, false);
        FoundAdapter.MenuViewHolder menuView = new FoundAdapter.MenuViewHolder(layoutView);
        return menuView;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        Reports foundItem = foundItems.get(position);
        holder.ageView.setText("Age "+foundItem.age);
        holder.nameView.setText(foundItem.name);
        holder.sexView.setText(foundItem.sex);
        holder.complexionView.setText(foundItem.complexion);
        Picasso.with(context).load(Constants.BASE_STORAGE + foundItem.imageUrl)
                .placeholder(R.drawable.empty_profile2)
                .error(R.drawable.empty_profile2)
                .into(holder.imgView);

        holder.frameView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetails.class);
            intent.putExtra(Constants.REPORT_ID, foundItem.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foundItems.size();
    }

    public void setData(List<Reports> found) {
        this.foundItems = found;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.found_age) TextView ageView;
        @BindView(R.id.found_img) ImageView imgView;
        @BindView(R.id.found_name) TextView nameView;
        @BindView(R.id.found_sex) TextView sexView;
        @BindView(R.id.found_complexion) TextView complexionView;

        @BindView(R.id.found_frame) RelativeLayout frameView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
