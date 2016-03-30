package sebastian.orderTracker;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.client_row, parent, false);
        // Busco los Textview en los xml
        TextView nameTextView = (TextView) rowView.findViewById(R.id.client_name);
        TextView addressTextView = (TextView) rowView.findViewById(R.id.client_adress);
        // Itero sobre el hashmap
        Iterator it = clients.entrySet().iterator();
        HashMap.Entry<String, String> client = null;
        for(int i=0; i <= position && it.hasNext(); ++i) {
            client = (Map.Entry<String, String>)it.next();
        }
        // Agrego el texto a los textView
        nameTextView.setText(client.getKey());
        addressTextView.append(client.getValue());
        return rowView;
    }
}


