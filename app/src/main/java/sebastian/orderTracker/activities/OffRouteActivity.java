package sebastian.orderTracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sebastian.orderTracker.Global;
import sebastian.orderTracker.R;
import sebastian.orderTracker.TabClientList;
import sebastian.orderTracker.adapters.ClientRowAdapter;
import sebastian.orderTracker.adapters.PagerAdapter;
import sebastian.orderTracker.entities.Client;
import sebastian.orderTracker.holders.ClientItemHolder;


public class OffRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_route);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_off_route);
        viewPager.setOffscreenPageLimit(1);

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), 1, this) {

            @Override
            public Fragment getItem(int position) {
                Fragment tab;
                tab = new TabClientList() {
                    @Override
                    protected String getFormattedUrl(int day) {
                        return "http://dev-taller2.pantheonsite.io/api/clientes.json";
                    }
                    @Override
                    protected ClientRowAdapter createAdapter(Context context, ArrayList<Client> list, int day) {
                        return new ClientRowAdapter(context, list, day) {
                            @Override
                            public void setSemaphore(ClientItemHolder holder, Client client){}
                        };
                    }
                };
                Bundle b = new Bundle();
                b.putInt("day", position);
                tab.setArguments(b);
                return tab;
            }

        };

        viewPager.setAdapter(adapter);

    }
}
