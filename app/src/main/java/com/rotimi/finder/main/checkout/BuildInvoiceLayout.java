package com.rotimi.finder.main.checkout;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yarata.ysell.R;
import com.yarata.ysell.miniyarata.db.models.Returns;
import com.yarata.ysell.miniyarata.db.models.Sales;
import com.yarata.ysell.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayowa on 10/15/16.
 */
public class BuildInvoiceLayout {

    List<Sales> sales;
    List<Returns> returns;
    ArrayList<LinearLayout> views;
    public String ref;


    public double totalAmount;
    public Context context;
    public Utility utility;
    

    public BuildInvoiceLayout(Context context){
        this.context = context;
        utility = new Utility(context);
        views = new ArrayList<>();
        
    }
    
    public void setSales(List<Sales> sales){
        this.sales = sales;
    }
    
    public void setReturns(List<Returns> returns){
        this.returns = returns; 
    }
    
    public ArrayList<LinearLayout> getOrderItemsView(){

        createOrderItemView();
        return views;
    }

    public ArrayList<LinearLayout> getReturnViews(){
        createReturnViews();
        return views;
    }

    public void createOrderItemView(){
        
        TextView txtView;
        TextView txtViewQty;
        TextView txtViewPrice;

        LinearLayout linearLayout;

        for(int i =0; i < sales.size(); i++){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);


            txtView = new TextView(context);

            //set 8px margin top and bottom
            txtView.setText(sales.get(i).menu.load().name);
            txtView.setGravity(Gravity.CENTER_VERTICAL);
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            txtView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            txtView.setLayoutParams(params);

            LinearLayout.LayoutParams layoutParamsQty = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);

            txtViewQty = new TextView(context);

            txtViewQty.setText(String.valueOf(sales.get(i).quantity));
            txtViewQty.setGravity(Gravity.CENTER_VERTICAL);
            txtViewQty.setPadding(0, 0, 5, 0);
            txtViewQty.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            txtViewQty.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            txtViewQty.setLayoutParams(layoutParamsQty);

            LinearLayout.LayoutParams layoutParamsPrice = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);

            txtViewPrice = new TextView(context);

            txtViewPrice.setText(String.valueOf(sales.get(i).quantity * sales.get(i).menu.load().price));
            txtViewPrice.setGravity(Gravity.RIGHT);
            txtViewPrice.setPadding(0, 0, 5, 0);
            txtViewPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            txtViewPrice.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            txtViewPrice.setLayoutParams(layoutParamsPrice);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setPadding(3, 5, 3, 5);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);

            linearLayout.addView(txtViewQty);
            linearLayout.addView(txtView);
            linearLayout.addView(txtViewPrice);

            views.add(linearLayout);
        }
    }
    public void createReturnViews(){

        TextView txtView;
        TextView txtViewQty;
        TextView txtViewPrice;

        LinearLayout linearLayout;

        for(int i =0; i < returns.size(); i++){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);


            txtView = new TextView(context);

            //set 8px margin top and bottom
            txtView.setText(returns.get(i).menu.load().name);
            txtView.setGravity(Gravity.CENTER_VERTICAL);
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            txtView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            txtView.setLayoutParams(params);

            LinearLayout.LayoutParams layoutParamsQty = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);

            txtViewQty = new TextView(context);

            txtViewQty.setText(String.valueOf(returns.get(i).quantity));
            txtViewQty.setGravity(Gravity.CENTER_VERTICAL);
            txtViewQty.setPadding(0, 0, 5, 0);
            txtViewQty.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            txtViewQty.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            txtViewQty.setLayoutParams(layoutParamsQty);

            LinearLayout.LayoutParams layoutParamsPrice = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);

            txtViewPrice = new TextView(context);

            txtViewPrice.setText(String.valueOf(returns.get(i).quantity * returns.get(i).menu.load().price));
            txtViewPrice.setGravity(Gravity.RIGHT);
            txtViewPrice.setPadding(0, 0, 5, 0);
            txtViewPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            txtViewPrice.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

            txtViewPrice.setLayoutParams(layoutParamsPrice);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setPadding(3, 5, 3, 5);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);

            linearLayout.addView(txtViewQty);
            linearLayout.addView(txtView);
            linearLayout.addView(txtViewPrice);

            views.add(linearLayout);
        }

    }
}
