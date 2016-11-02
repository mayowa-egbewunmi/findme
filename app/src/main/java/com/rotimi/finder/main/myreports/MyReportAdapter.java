package com.rotimi.finder.main.myreports;

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
import com.rotimi.finder.main.ReportDetails;
import com.rotimi.finder.main.publicreports.ReportItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyReportAdapter extends RecyclerView.Adapter<MyReportAdapter.MenuViewHolder> {

    private Context context;

    private List<ReportItem> myReportItems;

    public MyReportAdapter(Context context, List<ReportItem> myReportItems) {
        this.context = context;
        this.myReportItems = myReportItems;
    }

    @Override
    public MyReportAdapter.MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myreports, parent, false);
        MyReportAdapter.MenuViewHolder menuView = new MyReportAdapter.MenuViewHolder(layoutView);
        return menuView;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        ReportItem reportItem = myReportItems.get(position);
        holder.ageView.setText(reportItem.age);
        holder.nameView.setText(reportItem.name);
        holder.sexView.setText(reportItem.sex);
        holder.complexionView.setText(reportItem.complexion);
        Picasso.with(context).load(reportItem.imageUrl)
                .placeholder(R.drawable.empty_profile2)
                .centerCrop()
                .error(R.drawable.empty_profile2)
                .into(holder.imgView);

        holder.frameView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetails.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return myReportItems.size();
    }

    public void setData(List<ReportItem> myreports) {
        this.myReportItems = myreports;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.myreports_age) TextView ageView;
        @BindView(R.id.myreports_img) ImageView imgView;
        @BindView(R.id.myreports_name) TextView nameView;
        @BindView(R.id.myreports_sex) TextView sexView;
        @BindView(R.id.myreports_complexion) TextView complexionView;
        @BindView(R.id.myreports_frame)
        RelativeLayout frameView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
