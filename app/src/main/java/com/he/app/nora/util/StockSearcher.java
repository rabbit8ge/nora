package com.he.app.nora.util;

import com.he.app.nora.ShowStockActivity;

/**
 * Created by he on 15-7-22.
 */
public class StockSearcher {
    public static String searchWithDesc(String sDesc) {
        if(ShowStockActivity.generatePriceImage(sDesc) == null) // Temporary code.
            return null;
        return sDesc;
    }
}
