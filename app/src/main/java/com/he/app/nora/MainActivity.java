package com.he.app.nora;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.he.app.nora.fragment_buy.BuyNiuguFragment;

public class MainActivity extends ActionBarActivity implements
        SellFragment.OnFragmentInteractionListener,
        BuyFragment.OnFragmentInteractionListener,
        TaskFragment.OnFragmentInteractionListener,
        QAFragment.OnFragmentInteractionListener,
        MeFragment.OnFragmentInteractionListener,
        BuyNiuguFragment.OnFragmentInteractionListener,
        FavoriteListFragment.OnFragmentInteractionListener { // TODO: Temporary.

    // Navigate ids.
    private final int NAVIGATE_ID_HOME = Menu.FIRST + 0;
    private final int NAVIGATE_ID_FAVORITE = Menu.FIRST + 1;
    private final int NAVIGATE_ID_PERSONAL = Menu.FIRST + 2;
    private final int SEARCH_MENU = Menu.FIRST + 3;

    public static Context ctxApplication = null;

    // Fragment manager.
    private FragmentManager mFragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new PlaceholderFragment())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_navigator, new NavigatorFragment())
                    .commit();*/
        }

        ctxApplication = getApplicationContext();

        ActionBar ab = getSupportActionBar();
        ab.hide();

        //mFragManager = getSupportFragmentManager();
        mFragManager = getFragmentManager();

        // HE: Without these lines, can't connect to internet.
        /*StrictMode.setThreadPolicy(new StrictMode().ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNewWork() // or .detectAll() for all detectable problems.
        .penaltyLog()
        .build());

        StrictMode.setVmPolicy(new StrictMode().VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());*/

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                //.detectDiskReads()
                //.detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        // Search.
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        //handleSearchQuery(getIntent());

        // Test
       // Intent intent = new Intent(MainActivity.this, SearchActivity.class);
       // startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // HE_START
        menu.add(0, NAVIGATE_ID_HOME, 0, "Home");
        menu.add(0, NAVIGATE_ID_FAVORITE, 0, "Favorite");
        menu.add(0, NAVIGATE_ID_PERSONAL, 0, "My");
        // menu.findItem(); // TODO: Set menu background.
        // HE_END
        menu.add(0, SEARCH_MENU, 0, "Search Menu").setIcon(android.R.drawable.ic_menu_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == NAVIGATE_ID_HOME) { // TODO: Whether define menu in menu_main.xml?

        } else if(id == NAVIGATE_ID_FAVORITE) {

        } else if(id == NAVIGATE_ID_PERSONAL) {

        }
        else if(id == SEARCH_MENU) {
            //this.onSearchRequested();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // TODO: Show our customized menu fragment.
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onSearchRequested() {
        // Override to satisfy some requirement.
        Bundle bundle = new Bundle();
        bundle.putString("data", "ddd");
        startSearch("", false, bundle, false);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Start the ShowStockActivity to show the favorite stock.
        //Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, ShowStockActivity.class);
        i.putExtra(ShowStockActivity.INTENT_KEY_STOCK_ID, uri.toString());
        startActivity(i);
    }

    // TODO: Temporary.
    @Override
    public void onFragmentInteraction(String id) {
        // Start the ShowStockActivity to show the favorite stock.

        // Initialize stock instance.
        DataWrapper.Stock stk = new DataWrapper.Stock(id);
        stk.loadHistory();

        // Initialize intent.
        Intent i = new Intent(MainActivity.this, ShowStockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShowStockActivity.INTENT_KEY_STOCK_INS, stk);
        i.putExtras(bundle);
        i.putExtra(ShowStockActivity.INTENT_KEY_STOCK_ID, id);
        startActivity(i);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class NavigatorFragment extends Fragment {

        private FragmentManager mFragManager;

        private Button mBtnSell, mBtnBuy, mBtnTask, mBtnQA, mBtnMe;
        private ImageButton mBtnSearch;

        public NavigatorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_navigator, container, false);

            // Fragment manager.
            mFragManager = getFragmentManager();

            // Buttons.
            mBtnSell = (Button) rootView.findViewById(R.id.btn_sell);
            mBtnBuy = (Button) rootView.findViewById(R.id.btn_buy);
            mBtnTask = (Button) rootView.findViewById(R.id.btn_task);
            mBtnQA = (Button) rootView.findViewById(R.id.btn_qa);
            mBtnMe = (Button) rootView.findViewById(R.id.btn_me);
            mBtnSearch = (ImageButton) rootView.findViewById(R.id.btn_search);

            mBtnSell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //PlaceholderFragment frag = new PlaceholderFragment();
                    SellFragment frag = new SellFragment();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.frag_main, frag, "frag_sell");
                    trans.addToBackStack("frag_sell");
                    trans.commit();
                }
            });
            mBtnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BuyFragment frag = new BuyFragment();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.frag_main, frag, "frag_buy");
                    trans.addToBackStack("frag_buy");
                    trans.commit();
                }
            });
            mBtnTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskFragment frag = new TaskFragment();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.frag_main, frag, "frag_task");
                    trans.addToBackStack("frag_task");
                    trans.commit();
                }
            });
            mBtnQA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QAFragment frag = new QAFragment();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.frag_main, frag, "frag_qa");
                    trans.addToBackStack("frag_qa");
                    trans.commit();
                }
            });
            mBtnMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //MeFragment frag = new MeFragment();
                    FavoriteListFragment frag = new FavoriteListFragment();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.frag_main, frag, "frag_me");
                    trans.addToBackStack("frag_me");
                    trans.commit();
                }
            });
            mBtnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onSearchRequested();
                }
            });

            return rootView;
        }


    }
}
