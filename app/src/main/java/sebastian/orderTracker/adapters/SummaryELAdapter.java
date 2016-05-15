package sebastian.orderTracker.adapters;

/**
 * Created by matse on 19/4/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import sebastian.orderTracker.NetworkManagerSingleton;
import sebastian.orderTracker.R;
import sebastian.orderTracker.activities.Summary;
import sebastian.orderTracker.entities.Client;

public class SummaryELAdapter extends BaseExpandableListAdapter
{

    private Summary context;
    private List<String> listDataHeader;
    private HashMap<String, List<Client>> listDataChild;
    private HashMap<String, Boolean> isMine;

    public SummaryELAdapter(Summary context) {
        this.context = context;
        this.listDataHeader = new ArrayList<String>();
        this.listDataChild = new HashMap<String, List<Client>>();
        this.isMine = new HashMap<String, Boolean>();
        listDataHeader.add(context.getResources().getString(R.string.summary_visited_clients_text));
        listDataHeader.add(context.getResources().getString(R.string.summary_offroute_visited_clients_text));
        listDataChild.put(listDataHeader.get(0), new ArrayList<Client>());
        listDataChild.put(listDataHeader.get(1), new ArrayList<Client>());
    }


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
        final Client child = (Client) getChild(groupPosition, childPosition);
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.client_row, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.client_name);
        TextView enterprise = (TextView) convertView.findViewById(R.id.client_enterprise);
        TextView address = (TextView) convertView.findViewById(R.id.client_adress);
        NetworkImageView img = (NetworkImageView) convertView.findViewById(R.id.client_portrait);
        ImageLoader imageLoader = NetworkManagerSingleton.getInstance(this.context).getImageLoader();

        name.setText(child.getNombre());
        enterprise.setText(child.getCompania());
        address.setText(child.getDireccion());
        img.setImageUrl(child.getImagen(), imageLoader);

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
            convertView = inflater.inflate(R.layout.summary_row, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.summary_item);
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

    public void setDataChild(HashMap<String, List<Client>> dataChild)
    {
        this.listDataChild = dataChild;
    }

    public void addChild(String category, Client c, boolean mine) {
        List<Client> clientList = listDataChild.get(category);
        if(clientList == null) {
            List<Client> tempCl = new ArrayList<Client>();
            tempCl.add(c);
            listDataChild.put(category, tempCl);
        } else {
            boolean found = false;
            for (Client currectClient : clientList) {
                if (currectClient.getId() == c.getId())
                    found = true;
            }
            if (!found) {
                clientList.add(c);
                listDataChild.put(category, clientList);
            }
        }
        isMine.put(c.getId(), mine);
    }
}
