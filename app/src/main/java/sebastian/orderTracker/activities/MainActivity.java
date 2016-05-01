package sebastian.orderTracker.activities;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import sebastian.orderTracker.Global;
import sebastian.orderTracker.adapters.PagerAdapter;
import sebastian.orderTracker.R;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setTabs(tabLayout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Seteo Viewpager para poder switchear los fragmentos

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(7);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // Esto es el menú lateral
        //mDrawerList = (ListView)findViewById(R.id.navList);
        //addDrawerItems();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TextView txtVendedor = (TextView) findViewById(R.id.txtNombreVendedor);
        txtVendedor.setText(((Global) getBaseContext().getApplicationContext()).getUsername());
        return true;
    }

    private void setTabs(TabLayout tabLayout){
        String[] days = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        for(int i = 0; i < 7; ++i) {
            tabLayout.addTab(tabLayout.newTab().setText(days[i]));
        }
        tabLayout.addTab(tabLayout.newTab().setText(R.string.products_tab));
    }

    private ListView mDrawerList;


    private void addDrawerItems() {
        ArrayAdapter<String> mAdapter;
        String[] osArray = { getString(R.string.fuera_de_ruta), getString(R.string.agenda),
                getString(R.string.catalogo), getString(R.string.configuracion) };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Hacer algo con cada botón del menú
        Log.d("Item: ", item.toString());
        return false;
    }
}
