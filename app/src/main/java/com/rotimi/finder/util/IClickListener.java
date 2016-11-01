package com.rotimi.finder.util;

import android.view.View;

/**
 * Created by oladapo on 07/09/2016.
 * as part of com.softcom.tndn.utils in NPDN
 */
public interface IClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);

}
