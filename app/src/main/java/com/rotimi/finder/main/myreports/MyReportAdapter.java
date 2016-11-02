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
        ReportItem myReportItem = myReportItems.get(position);
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
        @BindView(R.id.myreports_when_last_seen) TextView whenView;
        @BindView(R.id.myreports_where_last_seen) TextView whereView;
        @BindView(R.id.myreports_frame)
        RelativeLayout frameView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
