package me.funnyzhao.feelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by fz on 2017/11/28 14:19.
 * 描述:仿掘金悬停页面的跳过按钮
 */

public class RotatingSkip extends View{

    private int mHeight;
    private int mWidth;

    private Paint mCicPaint;//圆画笔
    private float mSweepAngle;//旋转角度
    private boolean isFinish=false;//完成标识
    private float mCicRadius;//圆的半径
    private RectF OuterRect;

    private float mAngleOffset;//角度变化值，用来控制速度
    private int mBgColor;

    private LoadingListener mListener;
    public RotatingSkip(Context context) {
        super(context);
        init();
    }

    public RotatingSkip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RotatingSkip);
        mBgColor =typedArray.getColor(R.styleable.RotatingSkip_bgColor,0xff000000);
        mAngleOffset=typedArray.getInteger(R.styleable.RotatingSkip_angleOffset,2);
        typedArray.recycle();
        init();
    }

    public RotatingSkip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCenterCircle(canvas);
        drawOutArc(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取控件的宽高，后面需要设置圆心位置
        mHeight=h;
        mWidth=w;
        //确定圆的半径
        if (mHeight>mWidth){
            mCicRadius=mWidth/2-20;
        }else if (mWidth>mHeight){
            mCicRadius=mHeight/2-20;
        }else {
            mCicRadius=mHeight/2-20;
        }
        //外圈
        float start=mCicRadius+10;
        float end=mCicRadius+10;
        OuterRect = new RectF(-start,-start, end,end);
    }

    //初始化
    private void init(){
        //画笔
        mCicPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    //绘制中心圆
    private void drawCenterCircle(Canvas canvas){
        //移动画布中心
        canvas.translate(mWidth/2,mHeight/2);

        //绘制圆形
        mCicPaint.setColor(mBgColor);
        mCicPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0,0,mCicRadius,mCicPaint);
    }

    //绘制圆外边的弧
    private void drawOutArc(Canvas canvas){
        //绘制圆形外边
        mCicPaint.setStyle(Paint.Style.STROKE);
        mCicPaint.setStrokeWidth(20);

        if (!isFinish){
            mSweepAngle=mSweepAngle+mAngleOffset;
            if (mSweepAngle==360)
                isFinish=true;
            canvas.drawArc(OuterRect,0,mSweepAngle,true,mCicPaint);
            invalidate();
        }else{
                if (mListener==null)
                return;
                mListener.loadingFinish();
        }
    }
    //设置加载监听器
    public void setLoadingListener(LoadingListener loadingListener){
        mListener=loadingListener;
    }
    //加载监听器，当动画执行完成时调动loadingFinish
    public interface LoadingListener{
        void loadingFinish();
    }

}
