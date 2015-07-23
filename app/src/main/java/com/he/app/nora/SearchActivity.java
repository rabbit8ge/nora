package com.he.app.nora;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.he.app.nora.display.ShowAll;


public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            // Get extra data.
            Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
            if(appData != null) {
                appData.getString("ddd");
            }

            Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG);
            searchStock(query);

            // Start the ShowActivity to show the stock.
            Intent stkIntent = new Intent(SearchActivity.this, ShowStockActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("stock", new DataWrapper.Stock("000001"));
            stkIntent.putExtras(bundle);
            startActivity(stkIntent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    public DataWrapper.Stock searchStock(String desc) {
        return new DataWrapper.Stock("000001");
    }
}
