package com.rotimi.finder.main.menuselection;

/**
 * Created by mayowa on 2/19/16.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.yarata.ysell.R;
import com.yarata.ysell.miniyarata.db.models.Menu;
import com.yarata.ysell.util.Constants;
import com.yarata.ysell.util.SystemData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuSelectionAdapter extends RecyclerView.Adapter<MenuSelectionAdapter.MenuViewHolder> {

    private static final String LOG = MenuSelectionAdapter.class.getName();
    private Context context;
    private ColorGenerator generator;

    private List<Menu> menus;
    private SystemData sharedPref;
    private JSONObject jsonSelectedMenu;

    public MenuSelectionAdapter(Context context, List<Menu> menus) {
        this.menus = menus;
        this.context = context;
        this.generator = ColorGenerator.MATERIAL;

        this.sharedPref = new SystemData(context);
        setSelectedMenu();

    }
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_selection_item, parent, false);
        MenuViewHolder menuView = new MenuViewHolder(layoutView);

        return menuView;
    }

    public void setSelectedMenu(){
        String jsonString = sharedPref.getString(Constants.JSON_SELECTED_MENU);
        Log.d(LOG, jsonString+" ==== ");
        try {
            jsonSelectedMenu = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(menu.name.charAt(0)), generator.getRandomColor());
        holder.imgView.setImageDrawable(drawable);
        holder.nameView.setText(menu.name);
        try {
            String count = jsonSelectedMenu.has(String.valueOf(menu.id))? jsonSelectedMenu.getString(String.valueOf(menu.id)):"0";
            holder.countView.setText(count);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.addView.setOnClickListener(v -> {
            int selectedCount = 1; //This will be used if it is the first selection
            try {
                if(jsonSelectedMenu.has(String.valueOf(menu.id))){
                    selectedCount = jsonSelectedMenu.getInt(String.valueOf(menu.id)) + 1;
                }
                jsonSelectedMenu.put( String.valueOf(menu.id), selectedCount);
                sharedPref.writeString( jsonSelectedMenu.toString(), Constants.JSON_SELECTED_MENU);

                holder.countView.setText(String.valueOf(selectedCount));
                if(selectedCount > 0){
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
                }else{
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        holder.subtractView.setOnClickListener(v -> {
            int selectedCount = 0; //This will be used if it is the first selection
            try {
                if(jsonSelectedMenu.has(String.valueOf(menu.id))){
                    selectedCount = jsonSelectedMenu.getInt(String.valueOf(menu.id)) - 1;
                    if(selectedCount == 0){
                        jsonSelectedMenu.remove(String.valueOf(menu.id));
                    }else{
                        jsonSelectedMenu.put( String.valueOf(menu.id), selectedCount);
                    }
                    sharedPref.writeString(jsonSelectedMenu.toString(), Constants.JSON_SELECTED_MENU);
                }
                holder.countView.setText(String.valueOf(selectedCount));
                if(selectedCount > 0){
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
                }else{
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {

        return menus.size();
    }

    public void setData(List<Menu> menu) {
        this.menus = menu;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.menu_selection_add)
        ImageView addView;
        @BindView(R.id.menu_selection_count)
        TextView countView;
        @BindView(R.id.menu_selection_img)
        ImageView imgView;
        @BindView(R.id.menu_selection_name)
        TextView nameView;
        @BindView(R.id.menu_selection_subtract)
        ImageView subtractView;
        @BindView(R.id.menu_selection_item_lyt)
        RelativeLayout itemView;

        public MenuViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
