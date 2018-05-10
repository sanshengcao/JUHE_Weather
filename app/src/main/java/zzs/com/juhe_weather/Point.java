package zzs.com.juhe_weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ENIAC on 2018/4/13.
 */

public class Point extends View{

    private int radius = 10;
    private Paint paint = new Paint();
    private int color = R.color.lightgrey;


    public Point(Context context) {
        super(context);
        init();
    }

    public Point(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setColor(int color){
        this.color = color;
        paint.setColor(getResources().getColor(color));
        invalidate();
    }

    public void setRadius(int r){
        radius = r;
        invalidate();
    }

    private void init(){
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(color));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST&&heightMode== MeasureSpec.AT_MOST){
            setMeasuredDimension(radius,1);
        }else if(widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(radius,heightSpec);
        }else if(heightMode== MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpec,radius);
        }else{
            setMeasuredDimension(widthSpec,heightSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int paddingLeft=getPaddingLeft();
        int paddingRight=getPaddingRight();
        int paddingTop=getPaddingTop();
        int paddingBottom=getPaddingBottom();
        int drawX = (x-paddingLeft-paddingRight)/2;
        int drawY = (y-paddingTop-paddingBottom)/2;
        int radius = Math.max(drawX,drawY);
        canvas.drawCircle(paddingLeft+radius,paddingTop+radius,radius,paint);
    }

}
