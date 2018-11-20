package comt.leo.picker.testline.release.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import comt.leo.picker.testline.R;
import comt.leo.picker.testline.UIUtil;
import comt.leo.picker.testline.release.bean.PointLeo;


/**
 * 连线view
 */

public class LineConcentView extends View {


    //test
    private Paint mPaint;
    private ArrayList<PointLeo> points;

    public LineConcentView(Context context) {
        this(context, null);
    }

    public LineConcentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineConcentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_luo));
        mPaint.setStyle(Paint.Style.FILL);//设置填充样式
        mPaint.setStrokeWidth(UIUtil.dip2px(getContext(), 1));//设置画笔宽度
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (clearAbout) {
//            Paint paint = new Paint();
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            canvas.drawPaint(paint);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//            clearAbout = false;
//        }


        if (points != null) {
            for (int i = 0; i < points.size(); i++) {
                PointLeo pointLeo = points.get(i);
                if (pointLeo.getIsClickType().equals("高亮")) {
                    mPaint.setColor(getResources().getColor(R.color.color_luo));
                } else if (pointLeo.getIsClickType().equals("暗色")) {
                    mPaint.setColor(getResources().getColor(R.color.color_luo_alpha));
                } else if (pointLeo.getIsClickType().equals("默认显示")) {
                    mPaint.setColor(getResources().getColor(R.color.color_mor));
                }
                canvas.drawLine(pointLeo.getX(), pointLeo.getY(), pointLeo.getX1(), pointLeo.getY1(), mPaint);
            }
        }

    }


    public void setLines(ArrayList<PointLeo> points) {
        this.points = points;
        Log.e("这里是几条线啊", points.size() + "======");
        invalidate();
    }


}
