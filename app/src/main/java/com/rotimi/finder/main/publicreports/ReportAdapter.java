package com.rotimi.finder.main.publicreports;

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
import com.rotimi.finder.util.Constants;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MenuViewHolder> {

    private Context context;

    private List<Reports> reports;

    public ReportAdapter(Context context, List<Reports> reports) {
        this.context = context;
        this.reports = reports;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reports, parent, false);
        MenuViewHolder menuView = new MenuViewHolder(layoutView);
        return menuView;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        
        Reports reportItem = reports.get(position);
        holder.ageView.setText("Age "+reportItem.age);
        holder.nameView.setText(reportItem.name);
        holder.commentView.setText(reportItem.comment);
        holder.sexView.setText(reportItem.sex);
        holder.complexionView.setText(reportItem.complexion);
        Picasso.with(context)
                .load(Constants.BASE_STORAGE + reportItem.imageUrl)
                .placeholder(R.drawable.empty_profile2)
                .error(R.drawable.empty_profile2)
                .into(holder.imgView);
        
        holder.frameView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetails.class);
            intent.putExtra(Constants.REPORT_ID, reportItem.id);
            context.startActivity(intent);
        });

//        String letter = String.valueOf(reports.get(position).name.charAt(0));
//
////      Create a new TextDrawable for our image's background
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(letter, generator.getRandomColor());
//
//        holder.img.setImageDrawable(drawable);
//        holder.name.setText(reports.get(position).name);
//        holder.price.setText("#"+ String.valueOf(reports.get(position).price));
    }


    @Override
    public int getItemCount() {

        return reports.size();
    }

    public void setData(List<Reports> reports) {
        this.reports = reports;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
 
        @BindView(R.id.reports_age) TextView ageView;
        @BindView(R.id.reports_img) ImageView imgView;
        @BindView(R.id.reports_name) TextView nameView;
        @BindView(R.id.reports_sex) TextView sexView;
        @BindView(R.id.reports_complexion) TextView complexionView;
        @BindView(R.id.reports_comment) TextView commentView;
        @BindView(R.id.reports_frame) RelativeLayout frameView;
        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
