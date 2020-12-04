package edu.byuh.cis.hundredsurnamesvisualization;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import java.util.ArrayList;

class Show {
    String text;
    String role;
    public Show(String s) {
        text = s;
    }
    public void setRole(String r) {
        role = r;
    }
    public void changeText(String s) { text = s;}

}


public class SingleMemberImage extends View {
    private ColorTheme colorTheme;

    private float x;
    private float y;
    private boolean firstTimeDraw = true;
    private float canvasCenterX;
    private float canvasCenterY;
    private float imageSize;

    private String text;
    private String textLast;
    private String textNext;

    private Show currentShow;
    private Show lastShow;
    private Show nextShow;

    private ArrayList<Show> threeShows;

    private Paint textPaint;
    private float canvasWidth;
    private float canvasHeight;
    private boolean orientationJustChanged = false;

    private boolean animationWorking = false;

    public SingleMemberImage(Context context, String s, String sLast, String sNext) {
        super(context);
        this.text = s;
        this.textLast = sLast;
        this.textNext = sNext;

        threeShows = new ArrayList<>();

        textPaint = new Paint();
        textPaint.setTextSize(50);
        colorTheme =new ColorTheme();
    }

    public void updateThreeMembersBitmapIds(String s, String sLast, String sNext) {
        this.text = s;
        this.textLast = sLast;
        this.textNext = sNext;

    }

    @Override
    public void onDraw(Canvas c) {

        canvasWidth = c.getWidth();
        canvasHeight = c.getHeight();
        canvasCenterX = canvasWidth / 2;
        canvasCenterY = canvasHeight / 2;

        imageSize = Math.min(canvasWidth, canvasHeight);

        if(!animationWorking) {
            x = canvasCenterX;
            y = canvasCenterY;
        }


        if (firstTimeDraw || orientationJustChanged) {
            threeShows.clear();



            currentShow = new Show(text);
            lastShow = new Show(textLast);
            nextShow = new Show(textNext);

            threeShows.add(currentShow);
            threeShows.add(lastShow);
            threeShows.add(nextShow);

            currentShow.setRole("current");
            lastShow.setRole("last");
            nextShow.setRole("next");

            firstTimeDraw = false;
            orientationJustChanged = false;
        }

        Paint thisMemberPaint = new Paint();
        thisMemberPaint.setColor(Color.parseColor(colorTheme.c3));
        thisMemberPaint.setStyle(Paint.Style.FILL);
        thisMemberPaint.setTextSize((int)(imageSize * 0.8));
//        thisMemberPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics=thisMemberPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=y+distance;

        Paint thisCirclePaint = new Paint();
        thisCirclePaint.setColor(Color.parseColor(colorTheme.c1));
        thisCirclePaint.setStyle(Paint.Style.FILL);


        for (Show t: threeShows) {
            if (t.role.equals("current")) {
                c.drawCircle(x, y, imageSize * 0.5f, thisCirclePaint);
                c.drawText(t.text, x-thisMemberPaint.getTextSize()/2, baseline, thisMemberPaint);
            } else if (t.role.equals("last")) {
                c.drawCircle(x - canvasWidth, y, imageSize * 0.5f, thisCirclePaint);
                c.drawText(t.text, x - canvasWidth-thisMemberPaint.getTextSize()/2, baseline, thisMemberPaint);
            } else if (t.role.equals("next")) {
                c.drawCircle(x + canvasWidth, y, imageSize * 0.5f, thisCirclePaint);
                c.drawText(t.text, x + canvasWidth-thisMemberPaint.getTextSize()/2, baseline, thisMemberPaint);
            }
        }
    }

    public void orientationJustChanged(boolean b) {
        orientationJustChanged = b;
    }

    public void endOfAnimationAction() {
        x = canvasCenterX;
        for (Show t: threeShows) {
            if (t.role.equals("current")) {
                t.setRole("last");
            } else if (t.role.equals("last")) {
                t.setRole("next");
            } else if (t.role.equals("next")) {
                t.setRole("current");
            }
        }

        for (Show t: threeShows) {
            if (t.role.equals("current")) {
                t.changeText(text);
            } else if (t.role.equals("last")) {
                t.changeText(textLast);
            } else if (t.role.equals("next")) {
                t.changeText(textNext);
            }
        }
    }

    public void moveImage(String direction) {

        float sign = 1;

        if (direction.equals("left")) {
            sign = -1;
        } else if (direction.equals("right")) {
            sign = 1;
        }

        ValueAnimator valueAnimator;
        valueAnimator = ValueAnimator.ofObject(new FloatEvaluator(), x, sign * canvasWidth + (canvasCenterX ));
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
        final float finalSign = sign;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationWorking = true;
                if (orientationJustChanged) {
                    endOfAnimationAction();
                    animationWorking = false;
                } else {
                    x = (float) animation.getAnimatedValue();
                    invalidate();
                    if(x == finalSign * canvasWidth + (canvasCenterX)) {
                       endOfAnimationAction();
                       animationWorking = false;
                    }
                }
            }
        });
        valueAnimator.start();

    }

}
