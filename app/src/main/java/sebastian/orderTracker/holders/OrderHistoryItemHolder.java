package sebastian.orderTracker.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import sebastian.orderTracker.R;

/**
 * Created by Senastian on 23/05/2016.
 */
public class OrderHistoryItemHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView company;
    public TextView state;
    public TextView date;
    public TextView total;
    View itemView;

    public OrderHistoryItemHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        name = (TextView)itemView.findViewById(R.id.order_history_client_name);
        company = (TextView)itemView.findViewById(R.id.order_history_client_enterprise);
        state = (TextView)itemView.findViewById(R.id.order_history_state);
        total = (TextView)itemView.findViewById(R.id.order_history_total);
        date = (TextView)itemView.findViewById(R.id.order_history_date);
    }
}
