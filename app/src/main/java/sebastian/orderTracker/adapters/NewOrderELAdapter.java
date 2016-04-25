package sebastian.orderTracker.adapters;

/**
 * Created by matse on 19/4/2016.
 */

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.listeners.NewOrderClickListener;
import sebastian.orderTracker.dto.NewOrderNavigationArrayData;
import sebastian.orderTracker.entities.Product;
import sebastian.orderTracker.R;
import sebastian.orderTracker.activities.NewOrderActivity;

public class NewOrderELAdapter extends BaseExpandableListAdapter
{

    private NewOrderActivity context;
    private List<String> listDataHeader;
    private HashMap<String, List<Product>> listDataChild;

    public NewOrderELAdapter(NewOrderActivity context) { this.context = context; }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final Product child = (Product) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.new_order_product_row, null);
        }

        Integer stock = 10; // Esto saldria de Product
        TextView price = (TextView) convertView.findViewById(R.id.new_order_price);
        TextView name = (TextView) convertView.findViewById(R.id.new_order_name);
        TextView code = (TextView) convertView.findViewById(R.id.new_order_code);
        EditText quantity = (EditText) convertView.findViewById(R.id.new_order_quantity);
        ImageButton plusButton = (ImageButton) convertView.findViewById(R.id.new_order_plus);
        ImageButton minusButton = (ImageButton) convertView.findViewById(R.id.new_order_minus);
        NetworkImageView img = (NetworkImageView) convertView.findViewById(R.id.new_order_img);
        ImageLoader imageLoader = NetworkManagerSingleton.getInstance(this.context).getImageLoader();

        price.setText("$" + child.getPrecio());
        name.setText(child.getName());
        code.setText(child.getId());
        img.setImageUrl(child.getImagen(), imageLoader);
        quantity.setText("0");

        NewOrderNavigationArrayData data = this.context.getItemNavigation(child);
        if (data != null)
        {
            quantity.setText(data.getQuantity().toString());
        }

        plusButton.setOnClickListener(new NewOrderClickListener(this.context, NewOrderClickListener.PLUS_BUTTON_TYPE, stock, child, quantity));
        minusButton.setOnClickListener(new NewOrderClickListener(this.context, NewOrderClickListener.MINUS_BUTTON_TYPE, stock, child, quantity));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.new_order_row, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.new_order_item);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    public void setData(List<String> dataHeader, HashMap<String, List<Product>> dataChild)
    {
        this.listDataHeader = dataHeader;
        this.listDataChild = dataChild;
    }
}
