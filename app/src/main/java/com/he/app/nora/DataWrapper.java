package com.he.app.nora;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by he on 15-7-5.
 */
public class DataWrapper {

    private static DataFetcher mDataFetcher = new DataFetcher_Sina();

    public static Stock getStock(String id) {
        return mDataFetcher.parseStockInfo(mDataFetcher.getStockInfo(id));
    }


    public static class Stock {
        enum StockType {
            STOCK_TYPE_SH,
            STOCK_TYPE_SZ,
            STOCK_TYPE_INDEX
        };

        public String          mID; // TODO.
        public String          mName;
        public String          mAbbr; // TODO: Change to char.
        public Float           mPrice;
        public StockType       mType;

        public Stock() {
            mID = "000000";
            mName = "";
            mAbbr = "";
            mPrice = 0.0f;
            mType = StockType.STOCK_TYPE_SH;
        }

        public Stock(String id, String name, String abbr) {
            mID = id;
            mName = name;
            mAbbr = abbr;
            mPrice = 0.0f;
            mType = StockType.STOCK_TYPE_SH;
        }
        @Override
        public String toString() {
            return mID + ", " + mName + ", 0.00";
            //return super.toString();
        }
    }
}

abstract class DataFetcher {
    abstract public String getStockInfo(String id);

    abstract public DataWrapper.Stock parseStockInfo(String info);
}

class DataFetcher_Sina extends DataFetcher {
    @Override
    public String getStockInfo(String id) {
        try {
            URL url = new URL("http://hq.sinajs.cn/list=sh"+id); // TODO: stock type.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoInput(true);
//            conn.setConnectTimeout(5000);
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("accept", "*/*");
//            conn.connect();
//
//            InputStream stream = conn.getInputStream();
//            byte[] data = new byte[102400];
//            int len = stream.read(data);
//            String str = new String(data, 0, len);
//
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = conn.getInputStream();
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "";
            }

            byte[] data = new byte[102400];
            int len = in.read(data);
            String str = new String(data, 0, len);


            conn.disconnect();

            return str;
        } catch (Exception e) {
           int a = 0;
           System.out.println(e.toString());
        }
        return "";
    }

    @Override
    public DataWrapper.Stock parseStockInfo(String info) {
        DataWrapper.Stock stk = new DataWrapper.Stock();
        stk.mType = DataWrapper.Stock.StockType.STOCK_TYPE_SH;
        stk.mID = "000000";

        return stk;
    }
}