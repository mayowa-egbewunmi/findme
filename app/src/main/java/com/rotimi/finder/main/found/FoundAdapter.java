package com.rotimi.finder.main.found;

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

public class FoundAdapter extends RecyclerView.Adapter<FoundAdapter.MenuViewHolder> {

    private Context context;

    private List<FoundItem> foundItems;

    public FoundAdapter(Context context, List<FoundItem> foundItems) {
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
        FoundItem foundItem = foundItems.get(position);
        holder.frameView.setOnClickListener(v -> {
            Toast.makeText(context, "Yes", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return foundItems.size();
    }

    public void setData(List<FoundItem> found) {
        this.foundItems = found;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.found_age) TextView ageView;
        @BindView(R.id.found_img) ImageView imgView;
        @BindView(R.id.found_name) TextView nameView;
        @BindView(R.id.found_location) TextView locationView;
        @BindView(R.id.found_date) TextView dateView;
        @BindView(R.id.found_frame) RelativeLayout frameView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
