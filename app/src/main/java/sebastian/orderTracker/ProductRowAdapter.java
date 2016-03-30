package sebastian.orderTracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Senastian on 30/03/2016.
 */
public class ProductRowAdapter extends ArrayAdapter<String>{
    private final Context context;
    private final HashMap<String, String> clients;

    public ProductRowAdapter(Context context, HashMap<String, String> clients) {
        super(context, -1, clients.keySet().toArray(new String[0]));
        this.context = context;
        this.clients = clients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.product_row, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.product_name);
        TextView addressTextView = (TextView) rowView.findViewById(R.id.product_price);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.product_image);

        // Por ahora hardcodeado, luego hay que buscar el path donde se van a descargar las imagenes y hacerles decodeFile
        //Bitmap bitmap = BitmapFactory.decodeFile("C:/Users/Senastian/AndroidStudioProjects/tabBarExample/app/src/main/res/assets/example.png");
        //imageView.setImageBitmap(bitmap);

        imageView.setImageResource(R.drawable.example);
        Iterator it = clients.entrySet().iterator();
        HashMap.Entry<String, String> client = null;
        for(int i=0; i <= position && it.hasNext(); ++i) {
            client = (Map.Entry<String, String>)it.next();
        }
        nameTextView.setText(client.getKey());
        addressTextView.append(client.getValue());
        return rowView;
    }

}
