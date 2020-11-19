package edu.byuh.cis.hundredsurnamesvisualization;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
    private int id;
    private int idLast;
    private int idNext;
    private int idStore;
    private int idLastStore;
    private int idNextStore;

    private Member currentMember;
    private Member lastMember;
    private Member nextMember;

    private ArrayList<Member> threeMembers;

    private Paint textPaint;
    private float canvasWidth;
    private float canvasHeight;
    private boolean orientationJustChanged = false;

    public SingleMemberImage(Context context, int id, int idLast, int idNext) {
        super(context);
        this.id = id;
        this.idLast = idLast;
        this.idNext = idNext;

        threeMembers = new ArrayList<>();

        textPaint = new Paint();
        textPaint.setTextSize(50);
    }

    private Bitmap loadAndScale(Resources res, int id, float newWidth) {
        Bitmap original = BitmapFactory.decodeResource(res, id);
        float aspectRatio = (float)original.getHeight()/(float)original.getWidth();
        float newHeight = newWidth * aspectRatio;
        return Bitmap.createScaledBitmap(original, (int)newWidth, (int)newHeight, true);
    }

    public void updateThreeTemplesBitmapIds(int id, int idLast, int idNext) {
        this.idStore = id;
        this.idLastStore = idLast;
        this.idNextStore = idNext;
    }

    @Override
    public void onDraw(Canvas c) {

        canvasWidth = c.getWidth();
        canvasHeight = c.getHeight();
        canvasCenterX = canvasWidth / 2;
        canvasCenterY = canvasHeight / 2;

        imageSize = Math.min(canvasWidth, canvasHeight) * 0.9f;

        if (firstTimeDraw || orientationJustChanged) {
            threeMembers.clear();

            x = canvasCenterX - imageSize / 2;
            y = canvasCenterY - imageSize / 2;

            Bitmap b = loadAndScale(getResources(), id, imageSize);
            Bitmap bLast = loadAndScale(getResources(), idLast, imageSize);
            Bitmap bNext = loadAndScale(getResources(), idNext, imageSize);
            currentMember = new Member(b, 0f, 0f, 0f);
            lastMember = new Member(bLast, 0f, 0f, 0f);
            nextMember = new Member(bNext, 0f, 0f, 0f);
            threeMembers.add(currentMember);
            threeMembers.add(lastMember);
            threeMembers.add(nextMember);
            currentMember.setRole("current");
            lastMember.setRole("last");
            nextMember.setRole("next");

            firstTimeDraw = false;
            orientationJustChanged = false;
        }

//        c.drawText(x + ":x", 100, 100, textPaint);
//        c.drawText(y + ":y", 100, 140, textPaint);
//        c.drawText(imageSize + ":imageSize", 100, 180, textPaint);
//

        for (Member t: threeMembers) {
            if (t.role.equals("current")) {
                c.drawBitmap(t.image, x, y, null);
            } else if (t.role.equals("last")) {
                c.drawBitmap(t.image, x - canvasWidth, y, null);
            } else if (t.role.equals("next")) {
                c.drawBitmap(t.image, x + canvasWidth, y, null);
            }
        }
    }

    public void orientationJustChanged(boolean b) {
        orientationJustChanged = b;
    }

    public void endOfAnimationAction() {
        x = canvasCenterX - imageSize / 2;
        for (Member t: threeMembers) {
            if (t.role.equals("current")) {
                t.setRole("last");
            } else if (t.role.equals("last")) {
                t.setRole("next");
            } else if (t.role.equals("next")) {
                t.setRole("current");
            }
        }
        id = idStore;
        idLast = idLastStore;
        idNext = idNextStore;
        Bitmap b = loadAndScale(getResources(), id, imageSize);
        Bitmap bLast = loadAndScale(getResources(), idLast, imageSize);
        Bitmap bNext = loadAndScale(getResources(), idNext, imageSize);

        for (Member t: threeMembers) {
            if (t.role.equals("current")) {
                t.changeImage(b);
            } else if (t.role.equals("last")) {
                t.changeImage(bLast);
            } else if (t.role.equals("next")) {
                t.changeImage(bNext);
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
        valueAnimator = ValueAnimator.ofObject(new FloatEvaluator(), x, sign * canvasWidth + (canvasCenterX - imageSize / 2));
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
                    if(x == finalSign * canvasWidth + (canvasCenterX - imageSize / 2)) {
                       endOfAnimationAction();
                    }
                }
            }
        });
        valueAnimator.start();

    }

    // maybe useful later
//    @Override
//    public boolean onTouchEvent(MotionEvent m) {
//        float touchX= m.getX();;
//        float touchY= m.getY();;
//        if (m.getAction() == MotionEvent.ACTION_DOWN) {
//        }
//        if (m.getAction() == MotionEvent.ACTION_MOVE) {
//        }
//        if (m.getAction() == MotionEvent.ACTION_UP) {
//        }
//        return true;
//    }
}
