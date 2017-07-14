package com.steelkiwi.library.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;

import com.steelkiwi.library.SeparateShapesView;

/**
 * Created by yaroslav on 7/13/17.
 */

public class ShapeAnimator {

    private Params params;

    public interface Listener {
        void onAnimationEnd();
        void onAnimationStart();
    }

    public ShapeAnimator(@NonNull Params params) {
        this.params = params;
    }

    public void start() {
        ValueAnimator heightAnimation = ValueAnimator.ofInt(params.fromHeight, params.toHeight);
        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = params.view.getLayoutParams();
                layoutParams.height = val;
                params.view.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator widthAnimation = ValueAnimator.ofInt(params.fromWidth, params.toWidth);
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = params.view.getLayoutParams();
                layoutParams.width = val;
                params.view.setLayoutParams(layoutParams);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(params.duration);
        animatorSet.playTogether(heightAnimation, widthAnimation);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (params.animationListener != null) {
                    params.animationListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (params.animationListener != null) {
                    params.animationListener.onAnimationStart();
                }
            }
        });
        animatorSet.setInterpolator(new AnticipateOvershootInterpolator(0f));
        animatorSet.start();
    }

    public static class Params {
        // start and end view height
        private int fromHeight;
        private int toHeight;
        // start and end view width
        private int fromWidth;
        private int toWidth;
        // animation duration
        private long duration;

        private SeparateShapesView view;
        private ShapeAnimator.Listener animationListener;

        private Params(@NonNull SeparateShapesView view) {
            this.view = view;
        }

        public static Params create(@NonNull SeparateShapesView view) {
            return new Params(view);
        }

        public Params duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Params listener(@NonNull ShapeAnimator.Listener animationListener) {
            this.animationListener = animationListener;
            return this;
        }

        public Params height(int fromHeight, int toHeight) {
            this.fromHeight = fromHeight;
            this.toHeight = toHeight;
            return this;
        }

        public Params width(int fromWidth, int toWidth) {
            this.fromWidth = fromWidth;
            this.toWidth = toWidth;
            return this;
        }
    }
}
