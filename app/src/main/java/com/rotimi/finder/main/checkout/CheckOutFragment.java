package com.rotimi.finder.main.checkout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marcohc.toasteroid.Toasteroid;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yarata.ysell.R;
import com.yarata.ysell.miniyarata.customers.CustomersFragment;
import com.yarata.ysell.miniyarata.db.models.CheckOut;
import com.yarata.ysell.miniyarata.db.models.Customer;
import com.yarata.ysell.miniyarata.db.models.Customer_Table;
import com.yarata.ysell.miniyarata.db.models.Menu;
import com.yarata.ysell.miniyarata.db.models.Returns;
import com.yarata.ysell.miniyarata.db.models.Returns_Table;
import com.yarata.ysell.miniyarata.db.models.Sales;
import com.yarata.ysell.miniyarata.db.models.Sales_Table;
import com.yarata.ysell.miniyarata.menuselection.MenuSelection;
import com.yarata.ysell.util.Constants;
import com.yarata.ysell.util.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckOutFragment extends Fragment {
    public static final String ORDER_ID = "order_id";
    public static final String TAG = "$CheckoutFrame";

    public int orderId;
    public List<Sales> orders;
    public static int customerID = 0;
    public double totalAmountPaid;

    public List<Returns> returns;
    private Customer customer;
    private Utility utility;

    @BindView(R.id.checkout_sales)
    LinearLayout saleListingView;
    @BindView(R.id.checkout_returns)
    LinearLayout returnsListingView;
    @BindView(R.id.checkout_returns_add)
    TextView addReturnView;
    @BindView(R.id.checkout_send_invoice)
    Button sendInvoiceView;
    @BindView(R.id.checkout_send_appreciation)
    Button sendAppreciationView;
    @BindView(R.id.checkout_customer_attach)
    Button attachCustomerView;
    @BindView(R.id.checkout_customer_name)
    TextView customerNameView;
    @BindView(R.id.checkout_close)
    Button closeView;
    @BindView(R.id.checkout_total)
    TextView totalView;
    @BindView(R.id.checkout_returns_clear)
    TextView clearView;

    public static CheckOutFragment newInstance(int orderId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ORDER_ID, orderId);

        CheckOutFragment cf = new CheckOutFragment();
        cf.setArguments(bundle);

        return cf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        utility = new Utility(getActivity());
        customerID = 0; //reinitialized

        processIntent();
        setCustomer();
        displayInvoice();
        calculateTotalAmountPaid();
    }
    
    public void displayInvoice(){

        orders = SQLite.select().from(Sales.class).where(Sales_Table.order_id.eq(orderId)).queryList();
        BuildInvoiceLayout salesInvoice = new BuildInvoiceLayout(getActivity());
        salesInvoice.setSales(orders);
        ArrayList<LinearLayout> salesInvoiceViews =salesInvoice.getOrderItemsView();

        for(LinearLayout si : salesInvoiceViews){
            saleListingView.addView(si);
        }

        returns = SQLite.select().from(Returns.class).where(Returns_Table.order_id.eq(orderId)).and(Returns_Table.deleted.eq(0)).queryList();

        BuildInvoiceLayout returnsInvoice = new BuildInvoiceLayout(getActivity());
        returnsInvoice.setReturns(returns);
        ArrayList<LinearLayout> returnInvoiceViews = returnsInvoice.getReturnViews();
        for(LinearLayout ri : returnInvoiceViews){
            returnsListingView.addView(ri);
        }

        addReturnView.setOnClickListener(v -> processAddReturns());
        sendInvoiceView.setOnClickListener(v -> sendInvoice());
        sendAppreciationView.setOnClickListener(v -> sendAppreciation());
        attachCustomerView.setOnClickListener(v -> attachCustomer());
        clearView.setOnClickListener(v -> clearReturns());
        closeView.setOnClickListener(v -> closeSales());
    }

    public void clearReturns(){
        SQLite.update(Returns.class).set(Returns_Table.deleted.eq(1)).where(Returns_Table.order_id.eq(orderId)).execute();
        onResume();
    }

    public void setCustomer(){
        if(customerID!= 0){
            customer = SQLite.select().from(Customer.class).where(Customer_Table.id.eq(customerID)).querySingle();
            customerNameView.setText(customer.name);
        }
    }

    public void processAddReturns(){
        Intent intent = new Intent(getActivity(), MenuSelection.class);
        intent.putExtra(MenuSelection.FOR_CHECKOUT, true);
        intent.putExtra(CheckOutFragment.ORDER_ID, orderId);
        startActivity(intent);
    }


    public void attachCustomer(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        CustomersFragment customersFragment = CustomersFragment.newInstance(true);
        fragmentTransaction.replace(R.id.checkout_frame, customersFragment, CustomersFragment.TAG);
        fragmentTransaction.addToBackStack(CustomersFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        saleListingView.removeAllViews();
        returnsListingView.removeAllViews();

        setCustomer();
        displayInvoice();
        calculateTotalAmountPaid();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toasteroid.show(getActivity(), R.string.permission_granted, Toasteroid.STYLES.SUCCESS);
                break;
            }
        }
    }

    public void sendInvoice(){
        if(customer == null){
            Toasteroid.show(getActivity(), R.string.not_attached, Toasteroid.STYLES.ERROR);
        }else{
            try {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},
                            Constants.MY_PERMISSIONS_REQUEST);
                    return ;
                }
                utility.sendMsg(getInvoiceContent(), customer.phone);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String getInvoiceContent(){

        String msg = "Requests\n";

        for (Sales sale : orders){
            Menu m = sale.menu.load();
            msg += sale.quantity+" "+ m.name + "      "+(m.price * sale.quantity+"\n");
        }
        msg+="\n\nReturns\n";

        for (Returns _return : returns){
            Menu m = _return.menu.load();
            msg += _return.quantity+" "+ m.name + "      "+(m.price * _return.quantity+"\n");
        }

        msg+="\nThanks for your patronage";

        return  msg;
    }

    public void sendAppreciation(){
        if(customer == null){
            Toasteroid.show(getActivity(), R.string.not_attached, Toasteroid.STYLES.ERROR);
        }else{
            try {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},
                            Constants.MY_PERMISSIONS_REQUEST);
                    return ;
                }
                utility.sendMsg(getString(R.string.appreciate_msg), customer.phone);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void closeSales(){

        CheckOut checkout = new CheckOut();
        checkout.customer = (customerID == 0)? null : checkout.getCustomer(customerID);
        checkout.amount = totalAmountPaid;
        checkout.updated_at = Utility.getCurrentDate(false);
        checkout.created_at = Utility.getCurrentDate(false);

        checkout.save();

        //update the status of orders
        SQLite.update(Sales.class).set(Sales_Table.closed.eq(1)).where(Sales_Table.order_id.eq(orderId)).execute();

        Toasteroid.show(getActivity(), R.string.yup_nice_work, Toasteroid.STYLES.SUCCESS);
        getActivity().finish();
    }

    public void calculateTotalAmountPaid(){
        totalAmountPaid = 0;
        for(Sales sale : orders){
            totalAmountPaid += sale.total_amount;
        }
        for(Returns _return : returns){
            totalAmountPaid -= _return.total_amount;
        }

        totalView.setText("#"+totalAmountPaid);
    }
    /**
     * Extract Data from the Intent
     */
    private void processIntent() {
        Bundle bundle = getArguments();
        if (bundle!=null && bundle.containsKey(ORDER_ID)) {
            orderId = bundle.getInt(ORDER_ID);
        }
    }
}
