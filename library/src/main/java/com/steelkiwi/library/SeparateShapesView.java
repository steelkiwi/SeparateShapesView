package com.steelkiwi.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.steelkiwi.library.animation.ShapeAnimator;
import com.steelkiwi.library.drawable.SeparateShapeDrawable;
import com.steelkiwi.library.util.Constants;
import com.steelkiwi.library.util.ViewStatus;

/**
 * Created by yaroslav on 7/12/17.
 */

public class SeparateShapesView extends ViewGroup implements View.OnTouchListener {

    // drawable for two separate shapes
    private SeparateShapeDrawable drawable;
    // current parent status
    private ViewStatus currentViewStatus = ViewStatus.EXPAND_STATUS;
    // image view with done icon
    private ImageView middleIconView;
    // space between two shape
    private int middleSpace;
    // current space between two shape it can be changed
    private int currentMiddleSpace;
    // flag for checking if animation is finished
    private boolean isAnimationFinished = true;
    // left shape drawable for SeparateShapeDrawable
    private Drawable leftShapeDrawable;
    // left shape drawable for SeparateShapeDrawable
    private Drawable rightShapeDrawable;
    // middle icon drawable
    private Drawable middleIconDrawable;
    // text typeface
    private String fontName;
    // left drawable text title
    private String leftShapeTitle;
    // left drawable text title
    private String rightShapeTitle;
    // drawable text size
    private float textSize;
    // drawable text color
    private int textColor;
    // flag for check if all text caps
    private boolean isTextAllCaps;
    // previous parent width and height
    private int previousWidth;
    private int previousHeight;
    // shape buttons listener
    private OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {
        /**
         * When return true flag view will expanding
         * */
        boolean onLeftButtonClick();
        boolean onRightButtonClick();
    }
    public SeparateShapesView(Context context) {
        super(context);
        init(null);
    }

    public SeparateShapesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SeparateShapesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet set) {
        obtainViewAttributes(set);
        setOnTouchListener(this);
        prepareDefaultsParameters();
        prepareSeparateShapeDrawable();
    }

    private void prepareDefaultsParameters() {
        setBackgroundResource(android.R.color.transparent);
        setMiddleSpace(getContext().getResources().getDimensionPixelSize(R.dimen.view_middle_space));
        setCurrentMiddleSpace(getMiddleSpace());
    }

    private void prepareSeparateShapeDrawable() {
        drawable = new SeparateShapeDrawable(getContext());
        drawable.setTextAllCaps(isTextAllCaps());
        drawable.setTextSize(getTextSize());
        drawable.setTextColor(getTextColor());
        drawable.setLeftShapeTitle(getLeftShapeTitle());
        drawable.setRightShapeTitle(getRightShapeTitle());
        drawable.setTypeface(obtainTypeface(getFontName()));
    }

    private void obtainViewAttributes(AttributeSet set) {
        if(set != null) {
            TypedArray array = getContext().obtainStyledAttributes(set, R.styleable.SeparateShapesView);
            setLeftShapeDrawable(array.getDrawable(R.styleable.SeparateShapesView_ssv_left_shape_drawable));
            setRightShapeDrawable(array.getDrawable(R.styleable.SeparateShapesView_ssv_right_shape_drawable));
            setMiddleIconDrawable(array.getDrawable(R.styleable.SeparateShapesView_ssv_done_drawable));
            setFontName(array.getString(R.styleable.SeparateShapesView_ssv_text_font));
            setLeftShapeTitle(array.getString(R.styleable.SeparateShapesView_ssv_left_shape_text));
            setRightShapeTitle(array.getString(R.styleable.SeparateShapesView_ssv_right_shape_text));
            setTextSize(array.getDimensionPixelSize(R.styleable.SeparateShapesView_ssv_text_size,
                    getResources().getDimensionPixelSize(R.dimen.default_text_size)));
            setTextColor(array.getColor(R.styleable.SeparateShapesView_ssv_text_color,
                    ContextCompat.getColor(getContext(), android.R.color.white)));
            setTextAllCaps(array.getBoolean(R.styleable.SeparateShapesView_ssv_all_text_caps, true));
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateMiddleIconView();
    }

    private void inflateMiddleIconView() {
        middleIconView = new ImageView(getContext());
        Drawable drawable = getMiddleIconDrawable();
        if(drawable != null) {
            middleIconView.setImageDrawable(drawable);
        }
        middleIconView.setVisibility(GONE);
        addView(middleIconView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = reconcileSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec);
        int height = reconcileSize(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec);
        int viewSize = Math.max(width, height);
        measureChild(getChildAt(Constants.ZERO), viewSize, viewSize);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        onDrawableLayout();
        onMiddleIconViewLayout();
    }

    private void onMiddleIconViewLayout() {
        View view = getChildAt(Constants.ZERO);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int left = centerX - view.getMeasuredWidth() / 2;
        int top = centerY - view.getMeasuredHeight() / 2;
        int right = centerX + view.getMeasuredWidth() / 2;
        int bottom = centerY + view.getMeasuredHeight() / 2;
        view.layout(left, top, bottom, right);
    }

    private void onDrawableLayout() {
        int drawableWidth = getWidth() / 2 - getCurrentMiddleSpace();
        int drawableHeight = getHeight();
        if(drawableWidth > Constants.ZERO && drawableHeight > Constants.ZERO) {
            drawable.createLeftDrawablePart(getLeftShapeDrawable(), drawableWidth, drawableHeight);
            drawable.createRightDrawablePart(getRightShapeDrawable(), drawableWidth, drawableHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.draw(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(isAnimationFinished()) {
            onTouch(event);
        }
        return false;
    }

    private boolean onTouch(MotionEvent event) {
        Rect leftShapeBounds = drawable.getLeftShapeBounds();
        Rect rightShapeBounds = drawable.getRightShapeBounds();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            if (leftShapeBounds.contains(x, y)) {
                onLeftButtonClick();
                return true;
            }
            if (rightShapeBounds.contains(x, y)) {
                onRightButtonClick();
                return true;
            }
        }
        return false;
    }

    private void onLeftButtonClick() {
        if (onButtonClickListener != null) {
            boolean isExpand = onButtonClickListener.onLeftButtonClick();
            if(isExpand) {
                expand();
            }
        }
    }

    private void onRightButtonClick() {
        if (onButtonClickListener != null) {
            boolean isExpand = onButtonClickListener.onRightButtonClick();
            if(isExpand) {
                expand();
            }
        }
    }

    private void updateViewStatus() {
        if(getCurrentViewStatus() == ViewStatus.DONE_STATUS) {
            setCurrentViewStatus(ViewStatus.EXPAND_STATUS);
        } else if(getCurrentViewStatus() == ViewStatus.EXPAND_STATUS) {
            setCurrentViewStatus(ViewStatus.DONE_STATUS);
        }
    }

    private void expand() {
        if(isAnimationFinished()) {
            setAnimationFinished(false);
            SeparateShapesView.Params expandsParams = SeparateShapesView.Params.create()
                    .duration(Constants.DURATION_500)
                    .width(getHeight())
                    .height(getHeight());
            previousWidth = getWidth();
            previousHeight = getHeight();
            animate(expandsParams);
        }
    }

    private void done() {
        SeparateShapesView.Params doneParams = SeparateShapesView.Params.create()
                .duration(Constants.DURATION_500)
                .width(previousWidth)
                .height(previousHeight);
        animate(doneParams);
    }

    private void animate(@NonNull final Params params) {
        updateViewStatus();
        ShapeAnimator.Params animationParams = ShapeAnimator.Params.create(this)
                .height(getHeight(), params.height)
                .width(getWidth(), params.width)
                .duration(params.duration)
                .listener(animationListener);
        ShapeAnimator animation = new ShapeAnimator(animationParams);
        animation.start();
    }

    private ShapeAnimator.Listener animationListener = new ShapeAnimator.Listener() {
        @Override
        public void onAnimationEnd() {
            if (getCurrentViewStatus() == ViewStatus.EXPAND_STATUS) {
                setAnimationFinished(true);
                drawable.setDrawTitle(true);
                invalidateParent(getMiddleSpace());
            } else if (getCurrentViewStatus() == ViewStatus.DONE_STATUS) {
                middleIconView.setVisibility(VISIBLE);
                scaleXAnimation();
                scaleYAnimation();
            }
        }

        @Override
        public void onAnimationStart() {
            if (getCurrentViewStatus() == ViewStatus.DONE_STATUS) {
                drawable.setDrawTitle(false);
                invalidateParent(Constants.ZERO);
            } else if (getCurrentViewStatus() == ViewStatus.EXPAND_STATUS) {
                middleIconView.setVisibility(GONE);
            }
        }
    };

    private void invalidateParent(int startSpace) {
        setCurrentMiddleSpace(startSpace);
        invalidate();
    }

    private void scaleXAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(middleIconView, "scaleX",
                Constants.DEFAULT_SCALE, Constants.MAX_SCALE, Constants.DEFAULT_SCALE);
        animator.setDuration(Constants.DURATION_1000);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    private void scaleYAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(middleIconView, "scaleY",
                Constants.DEFAULT_SCALE, Constants.MAX_SCALE, Constants.DEFAULT_SCALE);
        animator.setDuration(Constants.DURATION_1000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                done();
            }
        });
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    private int reconcileSize(int contentSize, int measureSpec) {
        final int mode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        switch(mode) {
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                if (contentSize < specSize) {
                    return contentSize;
                } else {
                    return specSize;
                }
            case MeasureSpec.UNSPECIFIED:
            default:
                return contentSize;
        }
    }

    private Typeface obtainTypeface(String name) {
        if (name != null) {
            return Typeface.createFromAsset(getContext().getAssets(), "fonts/" + name);
        }
        return null;
    }

    private static class Params {
        private int width;
        private int height;
        private long duration;

        private Params() { /*private constructor*/ }

        public static Params create() {
            return new Params();
        }

        public Params width(int width) {
            this.width = width;
            return this;
        }

        public Params height(int height) {
            this.height = height;
            return this;
        }

        public Params duration(long duration) {
            this.duration = duration;
            return this;
        }
    }

    public ViewStatus getCurrentViewStatus() {
        return currentViewStatus;
    }

    private void setCurrentViewStatus(ViewStatus currentViewStatus) {
        this.currentViewStatus = currentViewStatus;
    }

    private int getMiddleSpace() {
        return middleSpace;
    }

    private void setMiddleSpace(int middleSpace) {
        this.middleSpace = middleSpace;
    }

    private int getCurrentMiddleSpace() {
        return currentMiddleSpace;
    }

    private void setCurrentMiddleSpace(int currentMiddleSpace) {
        this.currentMiddleSpace = currentMiddleSpace;
    }

    private boolean isAnimationFinished() {
        return isAnimationFinished;
    }

    private void setAnimationFinished(boolean animationFinished) {
        isAnimationFinished = animationFinished;
    }

    private Drawable getLeftShapeDrawable() {
        return leftShapeDrawable;
    }

    private void setLeftShapeDrawable(Drawable leftShapeDrawable) {
        this.leftShapeDrawable = leftShapeDrawable;
    }

    private Drawable getRightShapeDrawable() {
        return rightShapeDrawable;
    }

    private void setRightShapeDrawable(Drawable rightShapeDrawable) {
        this.rightShapeDrawable = rightShapeDrawable;
    }

    private Drawable getMiddleIconDrawable() {
        return middleIconDrawable;
    }

    private void setMiddleIconDrawable(Drawable middleIconDrawable) {
        this.middleIconDrawable = middleIconDrawable;
    }

    private String getFontName() {
        return fontName;
    }

    private void setFontName(String fontName) {
        this.fontName = fontName;
    }

    private String getLeftShapeTitle() {
        return leftShapeTitle;
    }

    private void setLeftShapeTitle(String leftShapeTitle) {
        this.leftShapeTitle = leftShapeTitle;
    }

    private String getRightShapeTitle() {
        return rightShapeTitle;
    }

    private void setRightShapeTitle(String rightShapeTitle) {
        this.rightShapeTitle = rightShapeTitle;
    }

    private float getTextSize() {
        return textSize;
    }

    private void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    private int getTextColor() {
        return textColor;
    }

    private void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    private boolean isTextAllCaps() {
        return isTextAllCaps;
    }

    private void setTextAllCaps(boolean textAllCaps) {
        isTextAllCaps = textAllCaps;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
