package sebastian.orderTracker;

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

public class NewOrderELAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<Product>> _listDataChild; // child data in format of header title, child title
    private HashMap<String, Integer> chosenProductsMap;

    public NewOrderELAdapter(Context context)
    {
        this._context = context;
        this.chosenProductsMap = new HashMap<>();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Product child = (Product) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.new_order_product_row, null);
        }

        Integer stock = 10; // Esto saldria de Product
        TextView price = (TextView) convertView.findViewById(R.id.new_order_price);
        TextView name = (TextView) convertView.findViewById(R.id.new_order_name);
        TextView code = (TextView) convertView.findViewById(R.id.new_order_code);
        EditText quantity = (EditText) convertView.findViewById(R.id.new_order_quantity);
        ImageButton plusButton = (ImageButton) convertView.findViewById(R.id.new_order_plus);
        ImageButton minusButton = (ImageButton) convertView.findViewById(R.id.new_order_minus);
        NetworkImageView img = (NetworkImageView) convertView.findViewById(R.id.new_order_img);
        ImageLoader imageLoader = NetworkManagerSingleton.getInstance(this._context).getImageLoader();

        price.setText(child.getPrecio());
        name.setText(child.getName());
        code.setText(child.getId());
        img.setImageUrl(child.getImagen(), imageLoader);
        if (this.chosenProductsMap.containsKey(child.getId()))
        {
            quantity.setText(this.chosenProductsMap.get(child.getId()).toString());
        }
        else
        {
            quantity.setText("0");
        }

        plusButton.setOnClickListener(new NewOrderClickListener(this._context, NewOrderClickListener.PLUS_BUTTON_TYPE, stock, child.getId(), quantity, chosenProductsMap));
        minusButton.setOnClickListener(new NewOrderClickListener(this._context, NewOrderClickListener.MINUS_BUTTON_TYPE, stock, child.getId(), quantity, chosenProductsMap));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.new_order_row, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.new_order_item);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(List<String> dataHeader, HashMap<String, List<Product>> dataChild)
    {
        this._listDataHeader = dataHeader;
        this._listDataChild = dataChild;
    }

    public HashMap<String, Integer> getChosenProductsMap()
    {
        return this.chosenProductsMap;
    }
}
