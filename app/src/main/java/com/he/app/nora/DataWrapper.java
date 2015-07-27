package com.he.app.nora;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by he on 15-7-5.
 */

public class DataWrapper {

    private static DataFetcher mDataFetcher = new DataFetcher_Sina();

    public static Stock getStock(String desc) {
        Integer.valueOf(desc);
        return mDataFetcher.parseStockInfo(desc, mDataFetcher.getStockInfo(desc));
    }


    public static class Stock implements Serializable {
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
            mID = "000001";
            mName = "";
            mAbbr = "";
            mPrice = 0.0f;
            mType = StockType.STOCK_TYPE_SH;
        }

        public Stock(String id) {
            mID = id;
            mName = "";
            mAbbr = "";
            mPrice = 0.0f;
            mType = StockType.STOCK_TYPE_SH;
        }

        public Stock(String id, StockType type, String name, String abbr) {
            mID = id;
            mName = name;
            mAbbr = abbr;
            mPrice = 0.0f;
            mType = type;
        }
        @Override
        public String toString() {
            return mID + ", " + mName + ", 0.00";
            //return super.toString();
        }

        private static final String JSON_ID = "id";
        private static final String JSON_NAME = "name";
        private static final String JSON_PRICE = "price";
        public JSONObject toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put(JSON_ID, mID);
            json.put(JSON_NAME, mName);
            json.put(JSON_PRICE, mPrice.toString());
            return json;
        }

        public Stock(JSONObject json) throws JSONException {
            //mID = UUID.fromString(json.getString(JSON_ID));
            mID = json.getString(JSON_ID);
            if(json.has(JSON_NAME)) {
                mName = json.getString(JSON_NAME);
            }
            if(json.has(JSON_PRICE)) {
                mPrice = Float.valueOf(json.getString(JSON_PRICE));
            }
        }
    }

    public static class DataSerializer {
        private Context mContext;
        private final static String mFavoriteFile = "nora_favorite.json";

        public DataSerializer(Context c) {
            mContext = c;
        }

        public void saveFavorites(ArrayList<Stock> items)
        throws JSONException, IOException {
            JSONArray array = new JSONArray();
            for (Stock stk : items) {
                array.put(stk.toJSON());
            }

            // Write to disk.
            Writer writer = null;
            try {
                OutputStream out = mContext
                        .openFileOutput(mFavoriteFile, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(array.toString());
            } finally {
                if(writer != null)
                    writer.close();
            }
        }

        public ArrayList<Stock> loadFavorites() throws IOException, JSONException {
            ArrayList<Stock> stocks = new ArrayList<Stock>();
            BufferedReader reader = null;

            try {
                InputStream in = mContext.openFileInput(mFavoriteFile);
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }

                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0; i < array.length(); i++) {
                    stocks.add(new Stock(array.getJSONObject(i)));
                }
            } finally {
                if(reader != null)
                    reader.close();
            }

            return stocks;
        }
    }
}

abstract class DataFetcher {
    abstract public String getStockInfo(String id);

    abstract public DataWrapper.Stock parseStockInfo(String id, String info);

    public class HttpDownloader {
        private URL url=null;

        public String Download(String urlstr){
            StringBuffer sb=new StringBuffer();
            String line=null;
            BufferedReader buff=null;
            try{
                url=new URL(urlstr);
                HttpURLConnection urlconn=(HttpURLConnection)url.openConnection();
                buff=new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "GB18030"));
                while((line=buff.readLine())!=null){
                    sb.append(line);
                }
            }
            catch(Exception e){
                e.printStackTrace();

            }
            finally{
                try{
                    buff.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            String str = sb.toString();
            //byte[] bbb = str.getBytes(Charset.forName("UTF-8"));
            return str;
            //return new String(bbb, Charset.forName("GB18030"));
        }

       /* public int DownFile(String urlstr,String path,String filename){
            InputStream inputstream=null;
            try{
                FileUtility fileutility=new FileUtility();
                if(fileutility.isFileExist(path+filename)){
                    return 1;
                }
                else{
                    url=new URL(urlstr);
                    HttpURLConnection urlconn=(HttpURLConnection)url.openConnection();
                    inputstream=urlconn.getInputStream();
                    File file=fileutility.Write2SDFromInput(path, filename, inputstream);
                    if(file==null){
                        return -1;
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
                return -1;

            }
            finally{
                try{
                    inputstream.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            return 0;
        }
*/
    }
}

class DataFetcher_Sina extends DataFetcher {
    @Override
    public String getStockInfo(String id) {
        StringBuffer sb = new StringBuffer();
        BufferedReader buff = null;
        HttpURLConnection conn = null;

        /*
        try {
            URL url = new URL("http://hq.sinajs.cn/list=sh000001"); // TODO: stock type.
            //URL url = new URL("http://www.baidu.com/");
            conn = (HttpURLConnection) url.openConnection();
            buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = null;
            while((line = buff.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
               buff.close();
               conn.disconnect();
           } catch (Exception e) {
               e.printStackTrace();
           }
        }
        return sb.toString();
*/

        String sUrlPre = "http://hq.sinajs.cn/list=";
        HttpDownloader hd = new HttpDownloader();
        String s = hd.Download(sUrlPre+"sh"+id);
        if(s.length() < 25) // TODO
            s = hd.Download(sUrlPre+"sz"+id);
        if(s.length() < 25)
            return "";
        return s;
    }

    @Override
    public DataWrapper.Stock parseStockInfo(String id, String info) {
        DataWrapper.Stock stk = new DataWrapper.Stock();

        if(info == null || info == "")
            return stk;

        if(id.charAt(0) == '3')
            stk.mType = DataWrapper.Stock.StockType.STOCK_TYPE_SZ;
        else
            stk.mType = DataWrapper.Stock.StockType.STOCK_TYPE_SH;

        String str = info.substring(21, info.length()-2); // The content without head and " end.
        Log.d("DataWrap", str);
        String ss[] = str.split(",");
        //if(ss.length() != )

        stk.mName = ss[0];
        stk.mID = id;
        stk.mPrice = Float.parseFloat(ss[3]);

        return stk;
    }
}