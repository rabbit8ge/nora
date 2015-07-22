package com.he.app.nora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.he.app.nora.DataWrapper.Stock;

/**
 * Created by he on 15-7-5.
 */
public class FavoriteContent {

    private static Map<String, Stock> mIdItemMap = new HashMap<String, Stock>();
    private static ArrayList<Stock> mItems = new ArrayList<Stock>();

    static {
        addItem(new Stock("000001", Stock.StockType.STOCK_TYPE_SH, "上证指数", "SZZS"));
        addItem(new Stock("399001", Stock.StockType.STOCK_TYPE_SZ, "深证成指", "SZCZ"));
        addItem(new Stock("601857", Stock.StockType.STOCK_TYPE_SH, "中国石油", "ZGSY"));
    }

    public static Boolean setItems(ArrayList<Stock> items) {
        mItems.clear();
        mItems.addAll(items);

        mIdItemMap.clear();
        Iterator<Stock> iter = items.iterator();
        while(iter.hasNext()) {
            Stock stock = iter.next();
            mIdItemMap.put(stock.mID, stock);
        }

        return Boolean.TRUE;
    }

    public static ArrayList<Stock> getItems() {
        return mItems;
    }

    public static Boolean addItem(String id) {
        Stock stk = new Stock(id);
//        mItems.add(stk);
        mIdItemMap.put(stk.mID, stk);
        mItems.clear();
        mItems.addAll(mIdItemMap.values());

        return Boolean.TRUE;
    }

    public static Boolean addItem(Stock item) {
//        mItems.add(item);
        mIdItemMap.put(item.mID, item);
        mItems.clear();
        mItems.addAll(mIdItemMap.values());

        return Boolean.TRUE; // TODO.
    }

    public static Stock findItem(String id) {
        if(mIdItemMap.containsKey(id)) {
            return mIdItemMap.get(id);
        } else {
            return null;
        }
    }

    public static Boolean deleteItem(String id) {
        if(mIdItemMap.containsKey(id)) {
            // Delete item in items.
/*            Iterator<Stock> iterItems = mItems.iterator();
            while(iterItems.hasNext()) {
                Stock stk = iterItems.next();
                if(stk.mID.equals(id)) {
                    iterItems.remove();
                }
            }
*/
            // Delete IdItemMap.
            Iterator<Map.Entry<String, Stock>> iterMap = mIdItemMap.entrySet().iterator();
            while(iterMap.hasNext()) {
                Map.Entry<String, Stock> entry = iterMap.next();
                if(entry.getKey().equals(id)) {
                    iterMap.remove();
                }
            }
            mItems.clear();
            mItems.addAll(mIdItemMap.values());
        }

        return Boolean.TRUE;
    }

    public static ArrayList<String> getDesc() {
        ArrayList<String> descs = new ArrayList<String>();
        for(Stock stk : mItems) {
            descs.add(stk.mID);
        }
        return descs;
    }
}
