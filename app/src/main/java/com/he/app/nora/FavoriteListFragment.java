package com.he.app.nora;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.ListFragment;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FavoriteListFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FavoriteListAdapter mListAdapter = null;
    private final Integer mRefreshSecond = 3; // Refresh every 3 second.
    private Handler mTimerHandler = null;
    private Runnable mTimerRunnable = null;

    // TODO: Rename and change types of parameters
    public static FavoriteListFragment newInstance(String param1, String param2) {
        FavoriteListFragment fragment = new FavoriteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoriteListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        //setListAdapter(new ArrayAdapter<FavoriteContent.FavoriteItem>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, FavoriteContent.mItems));
        mListAdapter = new FavoriteListAdapter(FavoriteContent.mItems);
        setListAdapter(mListAdapter);
        //setListAdapter(new FavoriteListAdapter(FavoriteContent.mItems));

        // Load favorite list from disk. Must before auto refresh.
        DataWrapper.DataSerializer ds = new DataWrapper.DataSerializer(getActivity());
        try {
            FavoriteContent.mItems.clear();
            // FavoriteContent.mItems = ds.loadFavorites(); // TODO: This line will not take effect because the adapter's linked array not changed.
            FavoriteContent.mItems.addAll(ds.loadFavorites());
        } catch (FileNotFoundException e) {
            // ..
        } catch (IOException e) {
            // ..
        } catch (JSONException e) {
            // ..
        } finally {
            if(FavoriteContent.mItems.size() == 0) {
                FavoriteContent.mItems.add(new DataWrapper.Stock("000001"));
                FavoriteContent.mItems.add(new DataWrapper.Stock("399001"));
                FavoriteContent.mItems.add(new DataWrapper.Stock("601857"));
            }
        }


        // Auto refresh.
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                //mListAdapter.notifyDataSetChanged();
                new AsyncListUpdater().execute((String[]) FavoriteContent.getDesc().toArray(new String[0])); // Update data. TODO: Exception when no 'new String[0]'.
                mTimerHandler.postDelayed(mTimerRunnable, mRefreshSecond * 1000);
            }
        };
        mTimerHandler.postDelayed(mTimerRunnable, mRefreshSecond*1000);
    }

    @Override
    public void onDestroyView() {
        mTimerHandler.removeCallbacks(mTimerRunnable); // Stop repeater.

        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //ImageView iv = (ImageView) rootView.findViewById(R.id.frag_show);
        //iv.setBackgroundColor(Color.BLUE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            new DataWrapper.DataSerializer(getActivity()).saveFavorites(FavoriteContent.mItems);
        } catch (JSONException e) {

        } catch(IOException ie) {
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //FavoriteContent.FavoriteItem fi = (FavoriteContent.FavoriteItem) getListAdapter().getItem(position); // TODO.
            DataWrapper.Stock fi = ((FavoriteListAdapter)getListAdapter()).getItem(position);
            mListener.onFragmentInteraction(fi.mID); // TODO.

            //DataWrapper dw = new DataWrapper();
            DataWrapper.Stock stk = DataWrapper.getStock("000001");
            Toast.makeText(getActivity(), stk.mName, Toast.LENGTH_LONG);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    private class AsyncListUpdater extends AsyncTask<String, Integer, ArrayList<DataWrapper.Stock>> {
        @Override
        protected ArrayList<DataWrapper.Stock> doInBackground(String... strings) {
            ArrayList<DataWrapper.Stock> stks = new ArrayList<DataWrapper.Stock>();
            for(String str : strings) {
                stks.add(DataWrapper.getStock(str));
            }
            return stks;
        }

        @Override
        protected void onPostExecute(ArrayList<DataWrapper.Stock> stocks) {
            super.onPostExecute(stocks);
            FavoriteContent.mItems.clear();
            FavoriteContent.mItems.addAll(stocks);
            Log.i("dd", FavoriteContent.mItems.get(0).mPrice.toString());
            mListAdapter.notifyDataSetChanged();
        }
    }

    private class FavoriteListAdapter extends ArrayAdapter<DataWrapper.Stock> {
        public FavoriteListAdapter(ArrayList<DataWrapper.Stock> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.favorite_list, null);
            }

            //DataWrapper.Stock fi = getItem(position);
            DataWrapper.Stock fi = FavoriteContent.mItems.get(position);

            TextView name =
                    (TextView) convertView.findViewById(R.id.favorite_list_name);
            name.setText(fi.mName);

            TextView id =
                    (TextView) convertView.findViewById(R.id.favorite_list_id);
            id.setText(fi.mID);

            TextView price =
                    (TextView) convertView.findViewById(R.id.favorite_list_price);
            price.setText(fi.mPrice.toString());

           // Log.d("hello", fi.mPrice.toString());

            return convertView;
        }
    }

}
