package com.he.app.nora;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.he.app.nora.util.FileUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by he on 15-7-5.
 */

public class DataWrapper {

    private static DataFetcher mDataFetcher = new DataFetcher_Sina();

    public static Stock getStock(String id) {
        //if(Integer.valueOf(id));
        Stock stock = mDataFetcher.parseStockInfo(id, mDataFetcher.getStockInfo(id));
        stock.loadHistory(); // Whether to load history?

        return stock;
    }


    public static class Stock implements Serializable {
        enum StockType {
            STOCK_TYPE_SH,
            STOCK_TYPE_SZ,
            STOCK_TYPE_INDEX
        }

        ;

        public String mID = "000001"; // TODO.
        public String mName = "";
        public String mAbbr = ""; // TODO: Change to char.
        public Float mPrice = 0.0f;
        public StockType mType = StockType.STOCK_TYPE_SH;
        public Float mOpeningPrice = 0.0f;
        public Float mYesterdayClosingPrice = 1.0f;
        public Float mHighestPrice = 0.0f;
        public Float mLowestPrice = 0.0f;
        public ArrayList<Float> mBuyPrices = null;
        public ArrayList<Float> mSellPrices = null;
        public ArrayList<Integer> mBuyVol = null;
        public ArrayList<Integer> mSellVol = null;
        public String mNote = "Sample Note";

        public static Date mToday;
        public static final Date mStartDate = new Date();

        static {
//            mStartDate.getYear()

        }

        ;

        public Integer mHistoryNum;
        public ArrayList<Float> mHistoryPrice;

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
            if (json.has(JSON_NAME)) {
                mName = json.getString(JSON_NAME);
            }
            if (json.has(JSON_PRICE)) {
                mPrice = Float.valueOf(json.getString(JSON_PRICE));
            }
        }

        public void loadHistory() {
            //try {
            DataFetcher df = new DataFetcher_qiniu();
            mHistoryPrice = df.parseStockInfo(mID, df.getStockInfo(mID)).mHistoryPrice;
        }
    }

    public static class CSVUtility {
        public static Map<String, ArrayList<String>> parseFile(
                String sFile, ArrayList<String> sExtractItems) throws IOException {
            return parseFile(new FileInputStream(sFile), sExtractItems);
        }

        public static Map<String, ArrayList<String>> parseFile(
                InputStream in, ArrayList<String> sExtractItems) throws IOException {
            Map<String, ArrayList<String>> ret = new HashMap<String, ArrayList<String>>();
            //InputStreamReader fr = new InputStreamReader(new FileInputStream(sFile));
            InputStreamReader fr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(fr);

            try {
                String sLine = null, sItem = null;

                // Parse item names.
                ArrayList<String> itemNames = null;
                if ((sLine = br.readLine()) == null)
                    return null;
                Pattern pCells = Pattern
                        .compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
                Matcher mCells = pCells.matcher(sLine);
                List<String> cells = new ArrayList<>();// 每行记录一个list
                // 读取每个单元格
                while (mCells.find()) {
                    sItem = mCells.group();
                    sItem = sItem.replaceAll(
                            "(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
                    sItem = sItem.replaceAll("(?sm)(\"(\"))", "$2");
                    cells.add(sItem);
                }

                // Index of each item.
                List<Integer> idxExtractItems = new ArrayList<>();
                ArrayList<ArrayList<String>> mapsEntry = new ArrayList<>();
                for (String itemName : sExtractItems) {
                    int idx = cells.indexOf(itemName);
                    idxExtractItems.add(idx);
                    mapsEntry.add(new ArrayList<String>());
                }


                // Parse each line to fulfill the list.
                while ((sLine = br.readLine()) != null) {
                    pCells = Pattern
                            .compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
                    mCells = pCells.matcher(sLine);
                    cells = new ArrayList<String>();// 每行记录一个list
                    // 读取每个单元格
                    while (mCells.find()) {
                        sItem = mCells.group();
                        sItem = sItem.replaceAll(
                                "(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
                        sItem = sItem.replaceAll("(?sm)(\"(\"))", "$2");
                        cells.add(sItem);
                    }
                    for (int i = 0; i < sExtractItems.size(); i++) {
                        int idx = idxExtractItems.get(i);
                        if (idx >= 0) {
                            mapsEntry.get(i).add(idx< cells.size() ? cells.get(idx) : "");
                        }
                    }
                }

                for (int i = 0; i < sExtractItems.size(); i++) {
                    ret.put(sExtractItems.get(i), mapsEntry.get(i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fr != null) {
                    fr.close();
                }
                if (br != null) {
                    br.close();
                }
            }
            return ret;
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
                if (writer != null)
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
                if (reader != null)
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
            private URL url = null;

            public String Download(String urlstr) {
                StringBuffer sb = new StringBuffer();
                String line = null;
                BufferedReader buff = null;
                try {
                    url = new URL(urlstr);
                    HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
                    buff = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "GB18030"));
                    while ((line = buff.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    try {
                        buff.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String str = sb.toString();
                //byte[] bbb = str.getBytes(Charset.forName("UTF-8"));
                return str;
                //return new String(bbb, Charset.forName("GB18030"));
            }

            public int DownFile(String urlstr, String path, String filename) {
                InputStream inputstream = null;
                try {
                    FileUtility fileutility = new FileUtility();
                    if (fileutility.isFileExist(path + filename)) {
                        return 1;
                    } else {
                        url = new URL(urlstr);
                        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
                        inputstream = urlconn.getInputStream();
                        Log.d("net", "start to write " + path + filename);
                        //File file=fileutility.Write2SDFromInput(path, filename, inputstream);
                        OutputStream file = fileutility.Write2InnerFromInput(MainActivity.ctxApplication, "history", filename,
                                inputstream);
                        if (file == null) {
                            Log.d("net", "failed to write sd");
                            return -1;

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                } finally {
                    try {
                        inputstream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }
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
            String s = hd.Download(sUrlPre + "sh" + id);
            if (s.length() < 25) // TODO
                s = hd.Download(sUrlPre + "sz" + id);
            if (s.length() < 25)
                return "";
            return s;
        }

        @Override
        public DataWrapper.Stock parseStockInfo(String id, String info) {
            if (info == null || info == "")
                return null;

            DataWrapper.Stock stk = new DataWrapper.Stock();
            if (id.charAt(0) == '3')
                stk.mType = DataWrapper.Stock.StockType.STOCK_TYPE_SZ;
            else
                stk.mType = DataWrapper.Stock.StockType.STOCK_TYPE_SH;

            String str = info.substring(21, info.length() - 2); // The content without head and " end.
            String ss[] = str.split(",");

            stk.mID = id;
            stk.mName = ss[0];
            stk.mOpeningPrice = Float.parseFloat(ss[1]);
            stk.mYesterdayClosingPrice = Float.parseFloat(ss[2]);
            stk.mPrice = Float.parseFloat(ss[3]);
            stk.mHighestPrice = Float.parseFloat(ss[4]);
            stk.mLowestPrice = Float.parseFloat(ss[5]);

            //
            stk.mBuyPrices = new ArrayList<>(5);
            stk.mSellPrices = new ArrayList<>(5);
            stk.mBuyVol = new ArrayList<>(5);
            stk.mSellVol = new ArrayList<>(5);
            for (int i = 0; i < 5; i++) {
                Float t = Float.parseFloat(ss[11 + i * 2]);
                stk.mBuyVol.add(i, t.intValue());
                stk.mBuyPrices.add(i, Float.parseFloat(ss[12 + i * 2]));
            }
            for (int i = 0; i < 5; i++) {
                Float t = Float.parseFloat(ss[20 + i * 2]);
                stk.mSellVol.add(i, t.intValue());
                stk.mSellPrices.add(i, Float.parseFloat(ss[21 + i * 2]));
            }

            return stk;
        }
    }

    class DataFetcher_qiniu extends DataFetcher {
        @Override
        public String getStockInfo(String id) {
            String sUrlPre = "http://7xku33.com1.z0.glb.clouddn.com/nora/history/";
            HttpDownloader hd = new HttpDownloader();
            String sFolder = "/data/data/com.he.app.nora/history/";
            String sFileName = id + ".csv";
            int ret = hd.DownFile(sUrlPre + id + ".csv", sFolder, sFileName);
            assert (ret > 0);
            return sFileName;
        }

        @Override
        public DataWrapper.Stock parseStockInfo(String id, String info) {
            DataWrapper.Stock stk = new DataWrapper.Stock(id);
            ArrayList<String> extracts = new ArrayList<>();
            extracts.add("close");
            try {
                InputStream in = MainActivity.ctxApplication.openFileInput(info);
                Map<String, ArrayList<String>> re = DataWrapper.CSVUtility.parseFile(in,
                        extracts);
                ArrayList<Float> historyPrice = new ArrayList<>();
                ArrayList<String> historyPriceString = re.get("close");
                for (int i = 0; i < historyPriceString.size(); i++) {
                    String s = "";
                    try {
                        s = historyPriceString.get(i);
                        historyPrice.add(Float.valueOf(historyPriceString.get(i)));

                    } catch (Exception e) {
                        int a = 0;
                        historyPrice.add(0.0f);
                    }
                }
                stk.mHistoryPrice = historyPrice;
            } catch (IOException e) {

            }
            return stk;
        }
    }