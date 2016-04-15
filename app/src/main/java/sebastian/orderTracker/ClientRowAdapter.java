package sebastian.orderTracker;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

public class ClientRowAdapter extends RecyclerView.Adapter<ClientItemHolder> implements Filterable{
    private final Context context;
    private ArrayList<Client> clients;
    private ArrayList<Client> filteredClients;

    public ClientRowAdapter(Context context, ArrayList<Client> clients) {
        this.context = context;
        this.clients = clients;
        this.filteredClients = clients;
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
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.client_row, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.client_name);
        TextView addressTextView = (TextView) rowView.findViewById(R.id.client_adress);
        TextView enterpriseTextView = (TextView) rowView.findViewById(R.id.client_enterprise);
        Iterator it = filteredClients.iterator();
        if(it.hasNext()) {
            Client client = null;
            for (int i = 0; i <= position && it.hasNext(); ++i) {
                client = (Client) it.next();
            }
            nameTextView.setText(client.getNombre());
            //TODO arreglar este adressText despues
            int posit = client.getDireccion().indexOf(",");
            addressTextView.append(client.getDireccion().substring(0,posit-1));
            enterpriseTextView.setText(client.getCompania());
            ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(context).getImageLoader();
            NetworkImageView image = (NetworkImageView)rowView.findViewById(R.id.call_icon);
            image.setImageUrl(client.getImagen(), mImageLoader);
            ((ListView) parent).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    Client c = filteredClients.get(position);
                    Gson gs = new Gson();
                    String clientString = gs.toJson(c);
                    Intent intent = new Intent(context, ClientDetails.class);
                    intent.putExtra(context.getString(R.string.serializedClientKey), clientString);
                    context.startActivity(intent);
                }
            });
        }
        return rowView;
    }
*/
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
        holder.address.setText(filteredClients.get(position).getDireccion());
        holder.company.setText(filteredClients.get(position).getCompania());

        ImageLoader mImageLoader = NetworkManagerSingleton.getInstance(context).getImageLoader();
        holder.portrait.setImageUrl(clients.get(position).getImagen(), mImageLoader);
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
}


