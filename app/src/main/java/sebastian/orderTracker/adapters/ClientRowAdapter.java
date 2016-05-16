package sebastian.orderTracker.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;

import sebastian.orderTracker.entities.Client;
import sebastian.orderTracker.holders.ClientItemHolder;
import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.R;

public class ClientRowAdapter extends RecyclerView.Adapter<ClientItemHolder> implements Filterable{
    private final Context context;
    private ArrayList<Client> clients;
    private ArrayList<Client> filteredClients;
    int day;

    public ClientRowAdapter(Context context, ArrayList<Client> clients, int day) {
        this.context = context;
        this.clients = clients;
        this.filteredClients = clients;
        this.day = day;
    }

    public ClientRowAdapter(Context context) {
        this.context = context;
        this.clients = new ArrayList<Client>();
        this.filteredClients = new ArrayList<Client>();
    }



    @Override
    public int getItemCount() {
        return filteredClients.size();
    }

    @Override
    public Filter getFilter() {
        return new ClientFilter();
    }

    public Client getByName(String name) {
        for(int i=0; i < clients.size();++i) {
            Client c = (Client)clients.get(i);
            if(name == c.getNombre()){
                return c;
            }
        }
        return null;
    }

    public Client getByPosition(int position) {
        return clients.get(position);
    }

    public int size() {
        return clients.size();
    }

    private class ClientFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            int count = clients.size();
            final ArrayList<Client> filteredList = new ArrayList<Client>(count);
            String filterableString;
            for(int i=0; i < count; ++i) {
                filterableString = clients.get(i).getNombre().toLowerCase();
                if(filterableString.contains(filterString)) {
                    filteredList.add(clients.get(i));
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredClients = (ArrayList<Client>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ClientItemHolder holder, int position) {
        holder.setClient(filteredClients.get(position));
        holder.name.setText(filteredClients.get(position).getNombre());
        //holder.image.setImageResource(products.get(position).getImgId());
        //holder.mailAdress.setText(clients.get(position).getCorreo());
        //holder.staticPhoneNumber.setText(clients.get(position).getTelefono());
        //holder.mobilePhoneNumber.setText(clients.get(position).getMobilePhoneNumber());
        holder.address.setText(filteredClients.get(position).getDireccion().substring(0,filteredClients.get(position).getDireccion().lastIndexOf(",")));
        holder.company.setText(filteredClients.get(position).getCompania());

        ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(context).getImageLoader();
        holder.portrait.setImageUrl(filteredClients.get(position).getImagen(), mImageLoader);
        setSemaphore(holder, filteredClients.get(position));

    }

    @Override
    public ClientItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_row, parent, false);
        ClientItemHolder rcv = new ClientItemHolder(layoutView);
        return rcv;
    }

    public void add(Client client) {
        clients.add(client);
    }

    public void setSemaphore(ClientItemHolder holder, Client client) {
        // >0 verde, <0 rojo, =0 amarillo
        Calendar c = Calendar.getInstance();
        if(client.hasBeenVisited()) {
            holder.setSemaphore(1);
        } else if(c.get(Calendar.DAY_OF_WEEK) > day) {
            holder.setSemaphore(0);
        } else {
            holder.setSemaphore(-1);
        }
    }
}


