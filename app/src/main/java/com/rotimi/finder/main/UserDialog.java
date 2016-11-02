package com.rotimi.finder.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.rotimi.finder.R;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.SystemData;
import com.rotimi.finder.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDialog extends DialogFragment {

    public final static String TAG = UserDialog.class.getSimpleName();
    private static final String user_ID = "user_id";

    @BindView(R.id.user_number) TextView phoneView;
    @BindView(R.id.user_name) TextView nameView;
    @BindView(R.id.user_continue) TextView saveView;
    @BindView(R.id.user_cancel) TextView cancelView;
    private Utility utility;

    private String name;
    private String phone;
    private int userID;

    DialogListener listener;
    Dialog dialog;

    public UserDialog() {}

    public static UserDialog newInstance() {
        UserDialog createuserDialog = new UserDialog();
        return createuserDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utility = new Utility(getActivity());
        if(getActivity() instanceof DialogListener)
            listener = (DialogListener) getActivity();
        else if(getParentFragment() instanceof  DialogListener){
            listener = (DialogListener) getParentFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_user, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        processIntent();

        saveView.setOnClickListener(v -> handleSubmit(v));
        cancelView.setOnClickListener(v -> {dialog.dismiss();});

        dialog.show();
    }

    public boolean validate() {
        boolean valid = true;

        name = nameView.getText().toString();
        phone = phoneView.getText().toString();

        if (name.isEmpty()) {
            nameView.setError(getString(R.string.name_error));
            valid = false;
        } else {
            nameView.setError(null);
        }
        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(getActivity(), phone, Toast.LENGTH_LONG).show();
            phoneView.setError(getString(R.string.phone_not_valid));
            valid = false;
        } else {
            phoneView.setError(null);
        }

        return valid;
    }

    public void handleSubmit(View v){
        if(validate()){
            utility.registerUser(name, phone);

            listener.onReturn(TAG);
            dialog.dismiss();
        }
    }

    /**
     * Extract Data from the Intent
     */
    private void processIntent() {
        Bundle bundle = getArguments();
        if (bundle!=null && bundle.containsKey(user_ID)) {
            userID = bundle.getInt(user_ID);
        }
    }

}

