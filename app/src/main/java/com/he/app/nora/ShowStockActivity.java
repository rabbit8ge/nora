package com.he.app.nora;

import com.he.app.nora.util.SearchSuggestionProvider;
import com.he.app.nora.util.StockSearcher;
import com.he.app.nora.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ShowStockActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    //private static final int HIDER_FLAGS = SystemUiHider.FLAG_FULLSCREEN;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private static WebView mWebView = null;

    // Intent key of stock id to show.
    public static final String INTENT_KEY_STOCK_ID = "idStock";

    // Controls.
    private Button mBtnFavoriateToggle = null;

    private String mStockId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_stock);

        // Controls configuration.
        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Get information according to start type.
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) { // It's started by search.
            String query = intent.getStringExtra(SearchManager.QUERY);

            // Add to search suggestion.
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            // Get extra data.
            Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
            if(appData != null) {
                appData.getString("ddd");
            }

            // Search to get stock.
            mStockId = StockSearcher.searchWithDesc(query);
            if(mStockId == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_can_not_find_stock) + query,
                        Toast.LENGTH_LONG).show();
                mBtnFavoriateToggle.setEnabled(false);
            }
        } else { // Normally start.
            mStockId = intent.getStringExtra(INTENT_KEY_STOCK_ID);
        }

        // Configure favorite toggle button.
        mBtnFavoriateToggle = (Button)findViewById(R.id.btn_favorite_toggle);
        mBtnFavoriateToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBtnFavoriateToggle.getText() == getApplicationContext().getString(R.string.add_to_favorite)) {
                    FavoriteContent.addItem(mStockId);
                    mBtnFavoriateToggle.setText(getApplicationContext().getString(R.string.remove_from_favorite));
                } else {
                    FavoriteContent.deleteItem(mStockId);
                    mBtnFavoriateToggle.setText(getApplicationContext().getString(R.string.add_to_favorite));
                }
                Intent intentAction = new Intent();
                intentAction.setAction(FavoriteListFragment.ACTION_FAVORITE_CHANGE);
                sendBroadcast(intentAction);
            }
        });
        if(FavoriteContent.findItem(mStockId) != null) {
            mBtnFavoriateToggle.setText(getApplicationContext().getString(R.string.remove_from_favorite));
        } else {
            mBtnFavoriateToggle.setText(getApplicationContext().getString(R.string.add_to_favorite));
        }
        mBtnFavoriateToggle.setOnTouchListener(mDelayHideTouchListener);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            long mLastClick = 0;
            @Override
            public void onClick(View view) {
//                long lTime = System.currentTimeMillis();
//                if(lTime-mLastClick < 1000) { // Double click.
//                    Log.d("show", "double click");
//                    if (TOGGLE_ON_CLICK) {
//                        //mSystemUiHider.toggle();
//                    } else {
//                        //mSystemUiHider.show();
//                    }
//                    return;
//                }
//
//                mLastClick = lTime;
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //mWebView = (WebView) findViewById(R.id.fullscreen_content);
        //mWebView.loadUrl("http://www.baidu.com/");
        //mWebView.loadUrl("http://finance.sina.com.cn/flash/cn.swf?symbol=sh000001");
        /*mWebView.loadData("<html>\n" +
                "<body>\n" +
                "\n" +
                "<div>\n" +
                "<embed src=\"http://finance.sina.com.cn/flash/cn.swf?symbol=sh000001\" quality=\"high\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" wmode=\"opaque\" height=\"490\" width=\"560\"></embed>\n" +
                "</div>" +
                "</body></html>");*/
//        try {
//            mWebView.getSettings().setJavaScriptEnabled(true);
//            mWebView.loadData(URLEncoder.encode("<html>\n" +
//                            "<body>" +
//                            "<div>abcd" +
//                            "<embed src=\"http://finance.sina.com.cn/flash/cn.swf?symbol=sh000001\" quality=\"high\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" wmode=\"opaque\" height=\"490\" width=\"560\"></embed>" +
//                            "</div>" +
//                            "</body></html>", "utf-8"),
//                    "text/html", "utf-8");
//            mWebView.loadUrl("http://image.sinajs.cn/newchart/min/n/sh000001.gif");
//        } catch(Exception e) {}

        ImageView iv = (ImageView) findViewById(R.id.fullscreen_content);
        iv.setImageBitmap(this.generatePriceImage(mStockId));
    }

    public static Bitmap generatePriceImage(String sId) {
        String sUrlPre = "http://image.sinajs.cn/newchart/min/n/";
        Bitmap img = getBitmapFromUrl(sUrlPre + "sh" + sId + ".gif");
        if(img == null)
            img = getBitmapFromUrl(sUrlPre + "sz" + sId + ".gif");
        if(img == null)
            img = null;

        return img;
    }

    private static Bitmap getBitmapFromUrl(String img) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(img);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
