package sebastian.orderTracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sebastian.orderTracker.R;
import sebastian.orderTracker.activities.OrderHistory;
import sebastian.orderTracker.holders.ClientItemHolder;
import sebastian.orderTracker.holders.OrderHistoryItemHolder;

/**
 * Created by Senastian on 23/05/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryItemHolder>{

    List<OrderInformationContainer> orders;

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public OrderHistoryAdapter() {
        orders = new ArrayList<>();
    }

    private class OrderInformationContainer {
        String name;
        String company;
        String total;
        String state;
        String date;

        public OrderInformationContainer(String n, String c, String t, String st, String d) {
            this.name = n;
            this.company = c;
            this.total = t;
            this.state = st;
            this.date = d;
        }
    }

    public void clearData() {
        orders = new ArrayList<>();
    }

    public void addOrder(String n, String c, String t, String st, String d) {
        orders.add(new OrderInformationContainer(n, c, t, st, d));
    }

    @Override
    public void onBindViewHolder(OrderHistoryItemHolder holder, int position) {
        holder.company.setText(orders.get(position).company);
        holder.name.setText(orders.get(position).name);
        holder.date.setText(orders.get(position).date);
        holder.state.setText(orders.get(position).state);
        holder.total.setText(orders.get(position).total);
    }

    @Override
    public OrderHistoryItemHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_row, parent, false);
        OrderHistoryItemHolder rcv = new OrderHistoryItemHolder(layoutView);
        return rcv;
    }



}
