package sebastian.orderTracker;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClientRowAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final HashMap<String, String> clients; // Para nombre de cliente y dirección

    public ClientRowAdapter(Context context, HashMap<String, String> clients) {
        super(context, -1, clients.keySet().toArray(new String[0]));
        this.context = context;
        this.clients = clients;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
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
        Iterator it = clients.entrySet().iterator();
        HashMap.Entry<String, String> client = null;
        for (int i = 0; i <= position && it.hasNext(); ++i) {
            client = (Map.Entry<String, String>)it.next();
        }
        // Agrego el texto a los textView
        nameTextView.setText(client.getKey());
        addressTextView.append(client.getValue());
        enterpriseTextView.setText("Ford Argentina S.A.");
        callIcon.setImageResource(R.drawable.example);

        ((ListView)parent).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                /*view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        */
                Log.d("test", "clicked");
                Intent intent = new Intent(getContext(), ClientDetails.class);
                getContext().startActivity(intent);
            }
        });
        return rowView;
    }



}


