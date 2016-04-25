package sebastian.orderTracker.holders;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import sebastian.orderTracker.activities.ClientDetails;
import sebastian.orderTracker.R;
import sebastian.orderTracker.entities.Client;


public class ClientItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public View itemView;
    public TextView name;
    public TextView company;
    public NetworkImageView portrait;
    public TextView mobilePhoneNumber;
    public TextView staticPhoneNumber;
    public TextView mailAdress;
    public TextView address;
    private Client client;
    private double lat;
    private double lng;

    public ClientItemHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.itemView = itemView;
        name = (TextView)itemView.findViewById(R.id.client_name);
        company = (TextView)itemView.findViewById(R.id.client_enterprise);
        address = (TextView)itemView.findViewById(R.id.client_adress);
        //mobilePhoneNumber = (TextView)itemView.findViewById(R.id.client_details_mobile_phone_number);
        //staticPhoneNumber = (TextView)itemView.findViewById(R.id.client_details_static_phone_number);
        //mailAdress = (TextView)itemView.findViewById(R.id.client_details_mail);
        portrait = (NetworkImageView)itemView.findViewById(R.id.client_portrait);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ClientDetails.class);
        Gson gs = new Gson();
        String clientString = gs.toJson(client);
        intent.putExtra("clientString", clientString);
        view.getContext().startActivity(intent);
    }

    public void setSemaphore(int state) {
        if(state > 0)
            itemView.getBackground().setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.Green500), PorterDuff.Mode.DARKEN);
        else if(state < 0)
            itemView.getBackground().setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.Yellow500), PorterDuff.Mode.DARKEN);
        else
            itemView.getBackground().setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.Red500), PorterDuff.Mode.DARKEN);
    }

}
