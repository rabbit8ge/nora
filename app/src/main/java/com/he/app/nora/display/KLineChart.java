package com.he.app.nora.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.limc.androidcharts.event.ITouchable;
import cn.limc.androidcharts.event.OnTouchGestureListener;
import cn.limc.androidcharts.view.MACandleStickChart;

/**
 * Created by zy on 8/4/2015.
 */
public class KLineChart extends MACandleStickChart {

    private Map<Int,String> mapTextHint4Lines = null;


    public KLineChart(Context context) {
        super(context);
    }

    public KLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setOnTouchListener()
    {
        this.setOnTouchListener(
                new OnTouchGestureListener() {
                    @Override
                    public void onTouchDown(ITouchable touchable, MotionEvent event) {
                        super.onTouchDown(touchable, event);
                        this.touchDown(new PointF(event.getX(), event.getY()));
                    }

                    @Override
                    public void onTouchMoved(ITouchable touchable, MotionEvent event) {
                        super.onTouchMoved(touchable, event);
                        this.touchMoved(new PointF(event.getX(), event.getY()));
                    }

                    @Override
                    public void onTouchUp(ITouchable touchable, MotionEvent event) {
                        super.onTouchUp(touchable, event);
                        this.touchUp(new PointF(event.getX(), event.getY()));
                    }
                }
        );
    }

    Override
    public void touchDown(PointF p)
    {
        //
        super.touchDown(p);
    }
    Override
    public void touchMoved(PointF p)
    {
        // implementation
        super.touchMoved(p);
    }
    Override
    public void touchUp(PointF p)
    {
        // implementation
        super.touchUp(p);
    }
    Override
    protected void onDraw(Canvas canvas)
    {
        this.drawHint(canvas);
        supper.onDraw(canvas);
    }
    protected void drawHint(Canvas canvas)
    {

        // todo: draw the text Hint of the data
        //canvas.drawText();
    }

    // this map is for draw text: key - index of the linesData, value - title
    public  void SetTextHint4Lines(Map<Int, String> textHint)
    {
        this.mapTextHint4Lines = textHint;
    }
}
