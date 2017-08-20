package qdx.swipemenucontainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by qdxxxx on 2017/8/15.
 */

public class SwipeMenuLayout extends ViewGroup {
    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);
        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.SwipeMenuLayout_isLeftMenu) {
                isLeftMenu = ta.getBoolean(attr, true);
            } else if (attr == R.styleable.SwipeMenuLayout_enableParentLongClick) {
                enableParentLongClick = ta.getBoolean(attr, false);
            } else if (attr == R.styleable.SwipeMenuLayout_expandRatio) {
                expandRatio = ta.getFloat(attr, 0.3f);
            } else if (attr == R.styleable.SwipeMenuLayout_collapseRatio) {
                collapseRatio = 1 - ta.getFloat(attr, 0.3f);
            } else if (attr == R.styleable.SwipeMenuLayout_expandDuration) {
                expandDuration = ta.getInt(attr, 150);
            } else if (attr == R.styleable.SwipeMenuLayout_collapseDuration) {
                collapseDuration = ta.getInt(attr, 150);
            }
        }
        ta.recycle();


        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    private int mExpandLimit;//展开的阈值
    private int mCollapseLimit;//关闭的阈值
    private float expandRatio = 0.3f;
    private float collapseRatio = 0.7f;

    private int expandDuration = 150;
    private int collapseDuration = 150;

    private boolean isLeftMenu = true;     //左侧为菜单

    private int mWidthofMenu;       //菜单布局宽度
    private boolean isExpand = false;       //菜单是否打开
    private boolean isClickEvent = true;    //是否是单击事件

    private int mScaleTouchSlop;            //滑动的最小判断距离
    private int mMaxVelocity;               //scrollView抛掷的最大速度
    private VelocityTracker mVelocityTracker;//滑动速度变量
    private int mPointerId;                 //多点触摸只算第一根手指的速度


    private static boolean sIsTouching = false;         //是否已经有手指触碰。防止第二个手指捣乱
    private static SwipeMenuLayout sSwipeMenuLayout;    //静态类写入内存共享。用来判断当前界面是否有menu打开

    private boolean enableParentLongClick = false;//拦截子类的点击事件，给父类享用

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setClickable(true);

        mWidthofMenu = 0;       //防止多次measure导致累加
        int widthOfContent = 0;//content的宽度
        int heightOfContent = 0;//content的高度
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setClickable(true);
            if (childView.getVisibility() == GONE)
                continue;

            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            heightOfContent = Math.max(heightOfContent, childView.getMeasuredHeight());
            if (i == 0) {
                widthOfContent = getMeasuredWidth();
            } else {
                mWidthofMenu += childView.getMeasuredWidth();
            }

        }

        mExpandLimit = (int) (mWidthofMenu * expandRatio);
        mCollapseLimit = (int) (mWidthofMenu * collapseRatio);
        setMeasuredDimension(widthOfContent, heightOfContent);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = l;
        int right = r;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE)
                continue;
            if (i == 0) {
                childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
            } else {
                if (isLeftMenu) {
                    childView.layout(left - childView.getMeasuredWidth(), getPaddingTop(), left, getPaddingTop() + childView.getMeasuredHeight());
                    left = left - childView.getMeasuredWidth();
                } else {//菜单在右边
                    childView.layout(right, getPaddingTop(), right + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                    right = right + childView.getMeasuredWidth();
                }
            }
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private PointF mPointDownF = new PointF();//记录手指第一次点击down的位置
    private PointF mPointGapF = new PointF();//用来设置scrollX，即展开/关闭menu

    private boolean isInterceptTouch = false;//已经有menu打开，再次点击的时候需要拦截父滑动事件，还要拦截子view事件
    private boolean isInterceptParent = false;//是否拦截了父滑动，即是否要自己要处理滑动

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://如果return false，那么后续事件将不会传达到此
                if (sIsTouching) {
                    return false;
                }
                sIsTouching = true;
                isClickEvent = true;        //down的时候默认为单击事件
                isInterceptTouch = false;   //没有menu开启
                isInterceptParent = false;  //不拦截父类滑动


                if (sSwipeMenuLayout != null) {//判断一下是否有Menu已经开启过了
                    if (sSwipeMenuLayout != this) {
                        sSwipeMenuLayout.collapseSmooth();
                        sIsTouching = false;

                        getParent().requestDisallowInterceptTouchEvent(true);//如果已经有menu打开，不让父类拦截down事件
                        isInterceptTouch = true;//有menu开启了.
                        //return false;
                        //如果down事件return false，后面一系列事件不再进入。只能交给parent，这样的话parent就能滑动了
                        //所以只能先拦截父类，然后自己move/up不处理，子view也不给它处理。
                    }

                }
                mPointGapF.x = ev.getX();

                mPointDownF.x = ev.getX();
                mPointDownF.y = ev.getY();
                mPointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInterceptTouch) {
                    return false;//已经有menu开启，不处理事件，也不给子view处理
                }
                isClickEvent = (Math.abs(mPointDownF.x - ev.getX()) < mScaleTouchSlop);//判断是否为点击事件

                float gapX = mPointDownF.x - ev.getX();
                float gapY = mPointDownF.y - ev.getY();

                if (Math.abs(gapX) < mScaleTouchSlop && Math.abs(gapX) > Math.abs(gapY) * 2f) {
                    isInterceptParent = true;
                } else if (Math.abs(gapX) > mScaleTouchSlop || Math.abs(getScrollX()) > mScaleTouchSlop) {
                    isInterceptParent = true;
                }

                if (!isInterceptParent) {
                    break;
                }

                getParent().requestDisallowInterceptTouchEvent(true);
                scrollBy((int) (mPointGapF.x - ev.getX()), 0);
                mPointGapF.x = ev.getX();
                if (isLeftMenu) {//如果左边是菜单，允许向右滑动
                    if (-getScrollX() < 0) {//向右滑动getScrollX()是<0的
                        scrollTo(0, 0);
                        isExpand = false;
                    }
                    if (getScrollX() <= -mWidthofMenu) {
                        scrollTo(-mWidthofMenu, 0);
                        isExpand = true;
                    }

                } else {//右边是菜单，向左滑动
                    if (getScrollX() < 0) {//向左滑动getScrollX()是>0的
                        scrollTo(0, 0);
                        isExpand = false;
                    }
                    if (getScrollX() >= mWidthofMenu) {
                        scrollTo(mWidthofMenu, 0);
                        isExpand = true;
                    }
                }


                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                sIsTouching = false;
                if (isInterceptTouch) {
                    return false;
                }

                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = mVelocityTracker.getXVelocity(mPointerId);


                if (isLeftMenu) {//左侧是菜单
                    if (!isExpand) { //如果还没展开
                        if (-getScrollX() > mExpandLimit || (velocityX > 1000 && isInterceptParent)) {
                            expandSmooth();
                        } else {
                            collapseSmooth();
                        }
                    } else {//已经展开的
                        if (-getScrollX() < mCollapseLimit || (velocityX < -1000 && isInterceptParent)) {
                            collapseSmooth();
                        } else if (!isClickEvent) {
                            expandSmooth();
                        } else if (ev.getX() > -getScrollX()) {
                            collapseSmooth();
                        }

                    }

                } else { //右侧是菜单
                    if (!isExpand) { //如果还没展开
                        if (getScrollX() > mExpandLimit || (velocityX < -1000 && isInterceptParent)) {
                            expandSmooth();
                        } else {
                            collapseSmooth();
                        }
                    } else {//已经展开的
                        if (getScrollX() < mCollapseLimit || (velocityX > 1000 && isInterceptParent)) {
                            collapseSmooth();
                        } else if (!isClickEvent) {//如果是滑动
                            expandSmooth();
                        } else if (ev.getX() < getWidth() - getScrollX()) {
                            collapseSmooth();
                        }
                    }

                }

                //释放eVelocityTracker
                releaseVelocityTracker();
                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    /**
     * 事件拦截，即事件不传递给子view
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isInterceptTouch || (!isInterceptParent && !isClickEvent) || !isClickEvent) {//如果滑动了或已经有菜单打开，就不分发给子view
            return true;
        } else if (isExpand) {//如果是点击，且除了menu范围，其他都拦截
            if (isLeftMenu && ev.getX() > mWidthofMenu) {
                return true;
            } else if (!isLeftMenu && ev.getX() < getWidth() - mWidthofMenu) {
                return true;
            }
        } else if (enableParentLongClick) {
            return true;
        }


        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 处理长按拖拽事件,展开menu的时候不能长按
     */
    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mScaleTouchSlop || isInterceptTouch) {
            return false;
        }
        return super.performLongClick();
    }

    /**
     * 平滑展开
     */
    private ValueAnimator mExpandAnim, mCollapseAnim;

    public void expandSmooth() {
        sSwipeMenuLayout = SwipeMenuLayout.this;
        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftMenu ? -mWidthofMenu : mWidthofMenu);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = true;

            }
        });
        mExpandAnim.setInterpolator(new LinearInterpolator());
        mExpandAnim.setDuration(expandDuration).start();
    }

    /**
     * 平滑关闭
     */
    public void collapseSmooth() {
        sSwipeMenuLayout = null;
        cancelAnim();

        mCollapseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCollapseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCollapseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = false;
            }
        });
        mCollapseAnim.setInterpolator(new AccelerateInterpolator());
        mCollapseAnim.setDuration(collapseDuration).start();
    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCollapseAnim != null && mCollapseAnim.isRunning()) {
            mCollapseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (sSwipeMenuLayout != null) {
            sSwipeMenuLayout.collapseInstant();
            sSwipeMenuLayout = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * 立刻关闭，不显示动画
     */
    public void collapseInstant() {
        if (this == sSwipeMenuLayout) {
            cancelAnim();
            sSwipeMenuLayout.scrollTo(0, 0);
            sSwipeMenuLayout = null;
        }
    }

    public void setLeftMenu(boolean leftMenu) {
        isLeftMenu = leftMenu;
    }

    public void setEnableParentLongClick(boolean enableParentLongClick) {
        this.enableParentLongClick = enableParentLongClick;
    }

    public void setExpandRatio(float expandRatio) {
        this.expandRatio = expandRatio;
    }

    public void setCollapseRatio(float collapseRatio) {
        this.collapseRatio = 1 - collapseRatio;
    }

    public void setExpandDuration(int expandDuration) {
        this.expandDuration = expandDuration;
    }

    public void setCollapseDuration(int collapseDuration) {
        this.collapseDuration = collapseDuration;
    }

}
