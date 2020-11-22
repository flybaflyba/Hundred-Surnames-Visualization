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


public class SingleMemberImage extends View {

    private float x;
    private float y;
    private boolean firstTimeDraw = true;
    private float canvasCenterX;
    private float canvasCenterY;
    private float imageSize;

    private String text;
    private String textLast;
    private String textNext;

    private Member currentMember;
    private Member lastMember;
    private Member nextMember;

    private ArrayList<Member> threeMembers;

    private Paint textPaint;
    private float canvasWidth;
    private float canvasHeight;
    private boolean orientationJustChanged = false;

    public SingleMemberImage(Context context, String text, String textLast, String textNext) {
        super(context);
        this.text = text;
        this.textLast = textLast;
        this.textNext = textNext;

        threeMembers = new ArrayList<>();

        textPaint = new Paint();
        textPaint.setTextSize(50);
    }

    public void updateThreeMembersBitmapIds(String text, String textLast, String textNext) {
        this.text = text;
        this.textLast = textLast;
        this.textNext = textNext;
    }

    @Override
    public void onDraw(Canvas c) {

        canvasWidth = c.getWidth();
        canvasHeight = c.getHeight();
        canvasCenterX = canvasWidth / 2;
        canvasCenterY = canvasHeight / 2;

        imageSize = Math.min(canvasWidth, canvasHeight) * 1f;

        if (firstTimeDraw || orientationJustChanged) {
            threeMembers.clear();

            x = canvasCenterX;
            y = canvasCenterY;

            currentMember = new Member(text, 0f, 0f, 0f);
            lastMember = new Member(textLast, 0f, 0f, 0f);
            nextMember = new Member(textNext, 0f, 0f, 0f);
            threeMembers.add(currentMember);
            threeMembers.add(lastMember);
            threeMembers.add(nextMember);
            currentMember.setRole("current");
            lastMember.setRole("last");
            nextMember.setRole("next");

            firstTimeDraw = false;
            orientationJustChanged = false;
        }

        Paint thisMemberPaint = new Paint();
        thisMemberPaint.setColor(Color.parseColor("#def2f1"));
        thisMemberPaint.setStyle(Paint.Style.FILL);
        thisMemberPaint.setTextSize((int)(imageSize));
//        thisMemberPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics=thisMemberPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=y+distance;

        Paint thisCirclePaint = new Paint();
        thisMemberPaint.setColor(Color.parseColor("#def661"));
        thisMemberPaint.setStyle(Paint.Style.FILL);


        for (Member t: threeMembers) {
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
        for (Member t: threeMembers) {
            if (t.role.equals("current")) {
                t.setRole("last");
            } else if (t.role.equals("last")) {
                t.setRole("next");
            } else if (t.role.equals("next")) {
                t.setRole("current");
            }
        }

        for (Member t: threeMembers) {
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
                if (orientationJustChanged) {
                    endOfAnimationAction();
                } else {
                    x = (float) animation.getAnimatedValue();
                    invalidate();
                    if(x == finalSign * canvasWidth + (canvasCenterX)) {
                       endOfAnimationAction();
                    }
                }
            }
        });
        valueAnimator.start();

    }
    
}
