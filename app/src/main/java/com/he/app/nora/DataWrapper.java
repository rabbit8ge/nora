package com.he.app.nora;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by he on 15-7-5.
 */
public class DataWrapper {

    private static DataFetcher mDataFetcher = new DataFetcher_Sina();

    public static Stock getStock(String id) {
        return mDataFetcher.parseStockInfo(id, mDataFetcher.getStockInfo(id));
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
        String pre = "";
        if(id.charAt(0) == '3') // TODO.
            pre = "sz";
        else
            pre = "sh";

        HttpDownloader hd = new HttpDownloader();
        return hd.Download("http://hq.sinajs.cn/list="+pre+id);
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