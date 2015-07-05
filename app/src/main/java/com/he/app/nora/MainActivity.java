package com.he.app.nora;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements FavoriteListFragment.OnFragmentInteractionListener {

    // Navigate ids.
    private final int NAVIGATE_ID_HOME = Menu.FIRST + 0;
    private final int NAVIGATE_ID_FAVORITE = Menu.FIRST + 1;
    private final int NAVIGATE_ID_PERSONAL = Menu.FIRST + 2;

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

        ActionBar ab = getSupportActionBar();
        //ab.hide();

        mFragManager = getSupportFragmentManager();
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // TODO: Show our customized menu fragment.
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onFragmentInteraction(String id) {
        //Toast.makeText(this, id, Toast.LENGTH_LONG);

        // Start the ShowStockActivity to show the favorite stock.
        Intent i = new Intent(MainActivity.this, ShowStockActivity.class);
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

        private Button mBtnHome;
        private Button mBtnFavorite;
        private Button mBtnMy;

        public NavigatorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_navigator, container, false);

            // Fragment manager.
            mFragManager = getFragmentManager();

            // Buttons.
            mBtnHome = (Button) rootView.findViewById(R.id.btn_home);
            mBtnFavorite = (Button) rootView.findViewById(R.id.btn_favorite);
            mBtnMy = (Button) rootView.findViewById(R.id.btn_my);

            mBtnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlaceholderFragment frag = new PlaceholderFragment();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.frag_main, frag, "frag_home");
                    trans.addToBackStack("frag_home");
                    //Toast.makeText(getCon, "Home", Toast.LENGTH_LONG);
                    trans.commit();
                }
            });
            mBtnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //FavoriteFragment frag = new FavoriteFragment();
                    FavoriteListFragment frag = new FavoriteListFragment();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.frag_main, frag, "frag_favorite");
                    trans.addToBackStack("frag_favorite");
                    trans.commit();
                }
            });

            return rootView;
        }


    }
}
