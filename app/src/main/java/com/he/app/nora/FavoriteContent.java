package com.he.app.nora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.he.app.nora.DataWrapper.Stock;

/**
 * Created by he on 15-7-5.
 */
public class FavoriteContent {

    public static ArrayList<Stock> mItems = new ArrayList<Stock>();
    public static Map<String, Stock> mIdItemMap = new HashMap<String, Stock>();

    static {
        addItem(new Stock("000001", Stock.StockType.STOCK_TYPE_SH, "上证指数", "SZZS"));
        addItem(new Stock("399001", Stock.StockType.STOCK_TYPE_SZ, "深证成指", "SZCZ"));
        addItem(new Stock("601857", Stock.StockType.STOCK_TYPE_SH, "中国石油", "ZGSY"));
    }

    public static Boolean addItem(Stock item) {
        mItems.add(item);
        mIdItemMap.put(item.mID, item);

        return Boolean.TRUE; // TODO.
    }

    public static Boolean deleteItem(String id) {
       /* FavoriteItem item = mIdItemMap[id];
        mItems.remove(item);
        mIdItemMap.remove(item);
*/
        return Boolean.TRUE; // TODO;
    }

    public static ArrayList<String> getDesc() {
        ArrayList<String> descs = new ArrayList<String>();
        for(Stock stk : mItems) {
            descs.add(stk.mID);
        }
        return descs;
    }
}
