package com.he.app.nora.display;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.he.app.nora.R;

import java.util.ArrayList;
import java.util.List;

import cn.limc.androidcharts.entity.DateValueEntity;
import cn.limc.androidcharts.entity.IChartData;
import cn.limc.androidcharts.entity.IStickEntity;
import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.entity.ListChartData;
import cn.limc.androidcharts.entity.OHLCEntity;
import cn.limc.androidcharts.view.GridChart;
import cn.limc.androidcharts.view.LineChart;
import cn.limc.androidcharts.view.MACandleStickChart;
import cn.limc.androidcharts.view.StickChart;


public class OverViewShow extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view_show);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_over_view_show, menu);
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


    private LineChart minuteLineChart = null;
    private StickChart stickchart = null;
    private MACandleStickChart macandlestickchart = null;

    private void test_minuteLineChart()
    {
        List<LineEntity<DateValueEntity>> data = new ArrayList<LineEntity<DateValueEntity>>();
    }

    private void initMinuteLineChart(List<LineEntity<DateValueEntity>> lines) {
        this.minuteLineChart = (LineChart) findViewById(R.id.minuteLinechart);

        minuteLineChart.setAxisXColor(Color.LTGRAY);
        minuteLineChart.setAxisYColor(Color.LTGRAY);
        minuteLineChart.setBorderColor(Color.LTGRAY);
        minuteLineChart.setLongitudeFontSize(14);
        minuteLineChart.setLongitudeFontColor(Color.WHITE);
        minuteLineChart.setLatitudeColor(Color.GRAY);
        minuteLineChart.setLatitudeFontColor(Color.WHITE);
        minuteLineChart.setLongitudeColor(Color.GRAY);
        minuteLineChart.setMaxValue(280);
        minuteLineChart.setMinValue(240);
        minuteLineChart.setMaxPointNum(36);
        minuteLineChart.setDisplayLongitudeTitle(true);
        minuteLineChart.setDisplayLatitudeTitle(true);
        minuteLineChart.setDisplayLatitude(true);
        minuteLineChart.setDisplayLongitude(true);
        minuteLineChart.setLatitudeNum(5);
        minuteLineChart.setLongitudeNum(6);
        minuteLineChart.setDataQuadrantPaddingTop(5);
        minuteLineChart.setDataQuadrantPaddingBottom(5);
        minuteLineChart.setDataQuadrantPaddingLeft(5);
        minuteLineChart.setDataQuadrantPaddingRight(5);
        minuteLineChart.setAxisYTitleQuadrantWidth(50);
        minuteLineChart.setAxisXTitleQuadrantHeight(20);
        minuteLineChart.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        minuteLineChart.setAxisYPosition(GridChart.AXIS_Y_POSITION_RIGHT);

        // 为chart1增加均线
        minuteLineChart.setLinesData(lines);
    }

    private void initStickChart(ListChartData<IStickEntity> tradeVolume /*trade volume*/) {
        this.stickchart = (StickChart) findViewById(R.id.stickchart);

        stickchart.setAxisXColor(Color.LTGRAY);
        stickchart.setAxisYColor(Color.LTGRAY);
        stickchart.setLatitudeColor(Color.GRAY);
        stickchart.setLongitudeColor(Color.GRAY);
        stickchart.setBorderColor(Color.LTGRAY);
        stickchart.setLongitudeFontColor(Color.WHITE);
        stickchart.setLatitudeFontColor(Color.WHITE);
        stickchart.setDataQuadrantPaddingTop(6);
        stickchart.setDataQuadrantPaddingBottom(1);
        stickchart.setDataQuadrantPaddingLeft(1);
        stickchart.setDataQuadrantPaddingRight(1);
        stickchart.setAxisYTitleQuadrantWidth(50);
        stickchart.setAxisXTitleQuadrantHeight(20);
        stickchart.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        stickchart.setAxisYPosition(GridChart.AXIS_Y_POSITION_RIGHT);

        // 最大显示足数
        stickchart.setMaxSticksNum(52);
        // 最大纬线数
        stickchart.setLatitudeNum(2);
        // 最大经线数
        stickchart.setLongitudeNum(3);
        // 最大价格
        stickchart.setMaxValue(10000);
        // 最小价格
        stickchart.setMinValue(100);

        stickchart.setDisplayLongitudeTitle(true);
        stickchart.setDisplayLatitudeTitle(true);
        stickchart.setDisplayLatitude(true);
        stickchart.setDisplayLongitude(true);
        stickchart.setBackgroundColor(Color.BLACK);

        stickchart.setDataMultiple(100);
        stickchart.setAxisYDecimalFormat("#,##0.00");
        stickchart.setAxisXDateTargetFormat("yyyy/MM/dd");
        stickchart.setAxisXDateSourceFormat("yyyyMMdd");


        // 为chart1增加均线
        stickchart.setStickData(tradeVolume);
    }

    private void initMACandleStickChart(IChartData<IStickEntity> dataOHLC,
                                        List<LineEntity<DateValueEntity>> dataMACDs
                                        ) {
        this.macandlestickchart = (MACandleStickChart) findViewById(R.id.macandlestickchart);
        List<LineEntity<DateValueEntity>> lines = new ArrayList<LineEntity<DateValueEntity>>();

        for(LineEntity<DateValueEntity> line : dataMACDs)
        {
            lines.add(line);
        }

        macandlestickchart.setAxisXColor(Color.LTGRAY);
        macandlestickchart.setAxisYColor(Color.LTGRAY);
        macandlestickchart.setLatitudeColor(Color.GRAY);
        macandlestickchart.setLongitudeColor(Color.GRAY);
        macandlestickchart.setBorderColor(Color.LTGRAY);
        macandlestickchart.setLongitudeFontColor(Color.WHITE);
        macandlestickchart.setLatitudeFontColor(Color.WHITE);

        // 最大显示足数
        macandlestickchart.setMaxSticksNum(52);
        // 最大纬线数
        macandlestickchart.setLatitudeNum(5);
        // 最大经线数
        macandlestickchart.setLongitudeNum(3);
        // 最大价格
        macandlestickchart.setMaxValue(1200);
        // 最小价格
        macandlestickchart.setMinValue(200);

        macandlestickchart.setDisplayLongitudeTitle(true);
        macandlestickchart.setDisplayLatitudeTitle(true);
        macandlestickchart.setDisplayLatitude(true);
        macandlestickchart.setDisplayLongitude(true);
        macandlestickchart.setBackgroundColor(Color.BLACK);

        macandlestickchart.setDataQuadrantPaddingTop(5);
        macandlestickchart.setDataQuadrantPaddingBottom(5);
        macandlestickchart.setDataQuadrantPaddingLeft(5);
        macandlestickchart.setDataQuadrantPaddingRight(5);
        macandlestickchart.setAxisYTitleQuadrantWidth(50);
        macandlestickchart.setAxisXTitleQuadrantHeight(20);
        macandlestickchart.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        macandlestickchart.setAxisYPosition(GridChart.AXIS_Y_POSITION_RIGHT);

        // MACD lines
        macandlestickchart.setLinesData(lines);

        // day K line
        macandlestickchart.setStickData(dataOHLC);

    }

}
