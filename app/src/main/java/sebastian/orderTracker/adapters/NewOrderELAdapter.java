package sebastian.orderTracker.adapters;

/**
 * Created by matse on 19/4/2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.entities.Client;
import sebastian.orderTracker.entities.Discount;
import sebastian.orderTracker.listeners.NewOrderClickListener;
import sebastian.orderTracker.dto.NewOrderNavigationArrayData;
import sebastian.orderTracker.entities.Product;
import sebastian.orderTracker.R;
import sebastian.orderTracker.activities.NewOrderActivity;

public class NewOrderELAdapter extends BaseExpandableListAdapter implements Filterable
{

    private NewOrderActivity context;
    private List<String> listDataHeader;
    private HashMap<String, List<Product>> listDataChild;
    private HashMap<String, List<Product>> filteredlistDataChild;
    private List<Filter> filters;
    Filter currentFilter;


    public NewOrderELAdapter(NewOrderActivity context) {
        this.context = context;
        filters = new ArrayList<>();
        filters.add(new OrderFilterByName());
        filters.add(new OrderFilterByBrand());
        filters.add(new OrderFilterByCategory());
        currentFilter = filters.get(0);
    }

    @Override
    public Filter getFilter() {
        return currentFilter;
    }

    public void switchFilter(int filterId) {
        switch (filterId) {
            case 0: {
                currentFilter = filters.get(0);
                break;
            }
            case 1: {
                currentFilter = filters.get(1);
                break;
            }
            case 2: {
                currentFilter = filters.get(2);
                break;
            }
            default: {
                currentFilter = filters.get(0);
            }
        }
    }

    public void notifyDataSetInvalidated()
    {
        super.notifyDataSetInvalidated();
    }

    private class OrderFilterByName extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            final HashMap<String, List<Product>> filteredOrders = new HashMap<>(listDataChild.size());
            String filterableString;
            int counter = 0;
            for(Map.Entry<String, List<Product>> entry : listDataChild.entrySet()) {
                String key = entry.getKey();
                List<Product> products = entry.getValue();
                List<Product> filteredProducts = new ArrayList<>();
                for(Product p : products) {
                    filterableString = p.getName().toLowerCase();
                    if(filterableString.contains(filterString)) {
                        filteredProducts.add(p);
                        counter++;
                    }
                }
                if(filteredProducts.size() > 0) {
                    filteredOrders.put(key, filteredProducts);
                }
            }
            results.values = filteredOrders;
            results.count = counter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredlistDataChild = (HashMap<String, List<Product>>) results.values;
            notifyDataSetInvalidated();
        }
    }

    private class OrderFilterByBrand extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            final HashMap<String, List<Product>> filteredOrders = new HashMap<>(listDataChild.size());
            String filterableString;
            int counter = 0;
            for(Map.Entry<String, List<Product>> entry : listDataChild.entrySet()) {
                String key = entry.getKey();
                List<Product> products = entry.getValue();
                List<Product> filteredProducts = new ArrayList<>();
                for(Product p : products) {
                    filterableString = p.getMarca().toLowerCase();
                    if(filterableString.contains(filterString)) {
                        filteredProducts.add(p);
                        counter++;
                    }
                }
                if(filteredProducts.size() > 0) {
                    filteredOrders.put(key, filteredProducts);
                }
            }
            results.values = filteredOrders;
            results.count = counter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredlistDataChild = (HashMap<String, List<Product>>) results.values;
            notifyDataSetInvalidated();
        }
    }

    private class OrderFilterByCategory extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterString = constraint.toString().toLowerCase();
            final HashMap<String, List<Product>> filteredOrders = new HashMap<>(listDataChild.size());
            String filterableString;
            int counter = 0;
            for(Map.Entry<String, List<Product>> entry : listDataChild.entrySet()) {
                String key = entry.getKey();
                List<Product> products = entry.getValue();
                List<Product> filteredProducts = new ArrayList<>();
                for(Product p : products) {
                    filterableString = p.getCategoria().toLowerCase();
                    if(filterableString.contains(filterString)) {
                        filteredProducts.add(p);
                        counter++;
                    }
                }
                if(filteredProducts.size() > 0) {
                    filteredOrders.put(key, filteredProducts);
                }
            }
            results.values = filteredOrders;
            results.count = counter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredlistDataChild = (HashMap<String, List<Product>>) results.values;
            notifyDataSetInvalidated();
        }
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.filteredlistDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
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

        Integer stock = Integer.valueOf(child.getStock());
        TextView price = (TextView) convertView.findViewById(R.id.new_order_price);
        TextView name = (TextView) convertView.findViewById(R.id.new_order_name);
        TextView code = (TextView) convertView.findViewById(R.id.new_order_code);
        EditText quantity = (EditText) convertView.findViewById(R.id.new_order_quantity);
        ImageButton plusButton = (ImageButton) convertView.findViewById(R.id.new_order_plus);
        ImageButton minusButton = (ImageButton) convertView.findViewById(R.id.new_order_minus);
        NetworkImageView img = (NetworkImageView) convertView.findViewById(R.id.new_order_img);
        ImageLoader imageLoader = NetworkManagerSingleton.getInstance(this.context).getImageLoader();

        price.setText("$" + child.getPrecio());
        name.setText(child.getName() + " " + child.getMarca());
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

        ImageButton discountButton = (ImageButton) convertView.findViewById(R.id.new_order_product_discount_button);

        //Descuentos
        final Discount discount = this.context.getDiscountForProduct(child);
        if (discount != null)
        {
            discountButton.setVisibility(ImageButton.VISIBLE);
            discountButton.setFocusable(false);
        }
        else
        {
            discountButton.setVisibility(ImageButton.INVISIBLE);
        }
        discountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, discount.getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });
        //quantity.addTextChangedListener(new NewOrderTextListener(this.context, stock, child, quantity));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        String header = listDataHeader.get(groupPosition);
        if(filteredlistDataChild.containsKey(header)) {
            return this.filteredlistDataChild.get(this.listDataHeader.get(groupPosition)).size();
        } else {
            return 0;
        }
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
        ImageButton discountButton = (ImageButton) convertView.findViewById(R.id.new_order_category_discount_button);
        final Discount discount = this.context.getDiscountForCategory(headerTitle);
        if (discount != null)
        {
            discountButton.setVisibility(ImageButton.VISIBLE);
            discountButton.setFocusable(false);
        }
        else
        {
            discountButton.setVisibility(ImageButton.INVISIBLE);
        }
        discountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, discount.getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });
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
        this.filteredlistDataChild = dataChild;
    }
}
