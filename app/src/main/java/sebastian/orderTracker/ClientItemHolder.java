package sebastian.orderTracker;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;


public class ClientItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

}