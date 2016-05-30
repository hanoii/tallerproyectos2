package sebastian.orderTracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.listeners.NewOrderClickListener;
import sebastian.orderTracker.dto.NewOrderNavigationArrayData;
import sebastian.orderTracker.R;

/**
 * Created by matse on 24/4/2016.
 */
public class NewOrderNavigationArrayAdapter extends ArrayAdapter
{

    private ArrayList<NewOrderNavigationArrayData> list;

    public NewOrderNavigationArrayAdapter(Context context, int textViewResourceId, List objects) {
        super(context, textViewResourceId, objects);
        this.list = new ArrayList<>();
        this.list = (ArrayList<NewOrderNavigationArrayData>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        NewOrderNavigationArrayData data = (NewOrderNavigationArrayData) this.list.get(position);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.new_order_navigation_product_row, null);
        }

        Integer stock = Integer.valueOf(data.getProduct().getStock());
        TextView price = (TextView) convertView.findViewById(R.id.new_order_price);
        TextView name = (TextView) convertView.findViewById(R.id.new_order_name);
        TextView code = (TextView) convertView.findViewById(R.id.new_order_code);
        EditText quantity = (EditText) convertView.findViewById(R.id.new_order_quantity);
        ImageButton plusButton = (ImageButton) convertView.findViewById(R.id.new_order_plus);
        ImageButton minusButton = (ImageButton) convertView.findViewById(R.id.new_order_minus);
        NetworkImageView img = (NetworkImageView) convertView.findViewById(R.id.new_order_img);
        ImageLoader imageLoader = NetworkManagerSingleton.getInstance(this.getContext()).getImageLoader();

        price.setText("$" + data.getProduct().getPrecio());
        name.setText(data.getProduct().getName() + " " + data.getProduct().getMarca());
        code.setText(data.getProduct().getId());
        img.setImageUrl(data.getProduct().getImagen(), imageLoader);
        quantity.setText(data.getQuantity().toString());

        plusButton.setOnClickListener(new NewOrderClickListener(this.getContext(), NewOrderClickListener.PLUS_BUTTON_TYPE, stock, data.getProduct(), quantity));
        minusButton.setOnClickListener(new NewOrderClickListener(this.getContext(), NewOrderClickListener.MINUS_BUTTON_TYPE, stock, data.getProduct(), quantity));

        return convertView;
    }
}
