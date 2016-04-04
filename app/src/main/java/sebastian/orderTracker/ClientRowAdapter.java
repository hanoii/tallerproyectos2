package sebastian.orderTracker;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClientRowAdapter extends ArrayAdapter<Client> implements Filterable{
    private final Context context;
    private ArrayList<Client> clients;
    private ArrayList<Client> filteredClients;

    public ClientRowAdapter(Context context, ArrayList<Client> clients) {
        super(context, -1, clients);
        this.context = context;
        this.clients = clients;
        this.filteredClients = clients;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public int getCount() {
        return filteredClients.size();
    }

    @Override
    public Filter getFilter() {
        return new ClientFilter();
    }

    public Client getByName(String name) {
        for(int i=0; i < clients.size();++i) {
            Client c = (Client)clients.get(i);
            if(name == c.getName()){
                return c;
            }
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.client_row, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.client_name);
        TextView addressTextView = (TextView) rowView.findViewById(R.id.client_adress);
        TextView enterpriseTextView = (TextView) rowView.findViewById(R.id.client_enterprise);
        ImageView callIcon = (ImageView) rowView.findViewById(R.id.call_icon);
        // Itero sobre el hashmap
        Iterator it = filteredClients.iterator();
        if(it.hasNext()) {
            Client client = null;
            for (int i = 0; i <= position && it.hasNext(); ++i) {
                client = (Client) it.next();
            }
            // Agrego el texto a los textView
            nameTextView.setText(client.getName());
            addressTextView.append(client.getAdress());
            enterpriseTextView.setText(client.getCompany());
            callIcon.setImageResource(R.drawable.example);

            ((ListView) parent).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    Client c = filteredClients.get(position);
                    Gson gs = new Gson();
                    String clientString = gs.toJson(c);
                    Intent intent = new Intent(getContext(), ClientDetails.class);
                    intent.putExtra(getContext().getString(R.string.serializedClientKey), clientString);
                    getContext().startActivity(intent);
                }
            });
        }
        return rowView;
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
                filterableString = clients.get(i).getName().toLowerCase();
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

}


