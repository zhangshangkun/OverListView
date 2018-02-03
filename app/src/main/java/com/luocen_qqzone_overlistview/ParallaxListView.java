package com.luocen_qqzone_overlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * Description: ParallaxListView
 * Author: zsk
 * Date: 2018/2/2  14:18
 * Email: zsk559521@163.com
 */
public class ParallaxListView extends ListView {
    private ImageView mImageView;
    private ImageView mHeaderImageView;
    private int mImageViewHeight=0;
    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageViewHeight=context.getResources().getDimensionPixelSize(R.dimen.header_height);
    }
    //背景图片拉伸
    public void setZoomImageView(ImageView iv){
        mImageView=iv;
    }
    //头像旋转
    public void setHeaderImageView(ImageView iv){
        mHeaderImageView=iv;
    }
     /**
     * 过度滑动
     * <p>
     * 注意：deltaX与deltaY与坐标系方向相反！！！
     * @param deltaX         水平增量（-右拉过度，+左拉过度）
     * @param deltaY         竖直增量（-下拉过度，+上拉过度）
     **/
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if(deltaY<0){ //下拉过度  对图片进行放大
           mImageView.getLayoutParams().height=mImageView.getHeight()-deltaY;
           mImageView.requestLayout();
           //头像的旋转
           mHeaderImageView.setRotation(mHeaderImageView.getRotation()-deltaY);
        }else{//一种特殊情况：当放大后的mImageView.height+listview.height=屏幕高度时，再往上推时，会触发上拉过度，而不会调用onScrollChanged中的缩小方法
            if(mImageView.getHeight()>mImageViewHeight) {
                mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY;
                mImageView.requestLayout();
                //头像的旋转
                mHeaderImageView.setRotation(mHeaderImageView.getRotation()-deltaY);
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //让ImageView上滑时缩小，监听
        View header = (View) mImageView.getParent();
        if(header.getTop()<0 && mImageView.getHeight()>mImageViewHeight){
            //头像的旋转
            mHeaderImageView.setRotation(mHeaderImageView.getRotation() + header.getTop());
            mImageView.getLayoutParams().height=mImageView.getHeight()+header.getTop();
            header.layout(header.getLeft(),0,header.getRight(),header.getHeight());
            mImageView.requestLayout();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //监听松开手
        int action = ev.getAction();
        if(action==MotionEvent.ACTION_UP){
           ResetAnimation resetAnimation=new ResetAnimation(mImageViewHeight);
           resetAnimation.setDuration(300);
           mImageView.startAnimation(resetAnimation);
        }
        return super.onTouchEvent(ev);
    }
    public class ResetAnimation extends Animation{
        private int delay ; //高度差
        private int currentHeight ;  //当前的高度
        private int mHeaderRotation; // 起始值
        private int mHeaderDestRotation=0; //终止值
        public ResetAnimation(int targetHeight){ //targetHeight 最终恢复的高度
            delay = mImageView.getHeight()  - targetHeight;
            currentHeight =  mImageView.getHeight();
            mHeaderRotation= (int) mHeaderImageView.getRotation();
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            //interpolatedTime(0.0 to 1.0) 执行的百分比
            //减小ImageView的高度
            mImageView.getLayoutParams().height= (int) (currentHeight-delay*interpolatedTime);
            mImageView.requestLayout();
            //头像的旋转
            int rotation= (int) (mHeaderRotation+(mHeaderDestRotation-mHeaderRotation)*interpolatedTime);
            mHeaderImageView.setRotation(rotation);

            super.applyTransformation(interpolatedTime, t);
        }
    }
}
