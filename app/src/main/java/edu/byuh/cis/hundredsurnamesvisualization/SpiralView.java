package edu.byuh.cis.hundredsurnamesvisualization;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class SpiralView extends View {

    private Paint bluePaint, redPaint, spiralPaint, yearDisplayPaint;
    private float screenWidth, screenHeight;
    public float theta;
    private Path spiralLine;
    private Boolean loadedImages;
    private  float centerX;
    private float centerY;
    private float initialR;
    private boolean sliderMoving;
    private static ArrayList<Member> memberObjects; // more OO be more object oriented
    private ArrayList<ArrayList<Float>> onScreenTemples;
    private ArrayList<Float> oneOnScreenTemple;
    private ArrayList<ArrayList<Float>> spiralCoordinates;
    private ArrayList<Float> sizes;
    private ArrayList<String> allTempleLinks;
    private ArrayList<String> allEventsDates;
    public ArrayList<String> allYears;
    private int eachIndex;
    private Matrix currentTempleMatrix;
    private float topCoordinateInSpiralX;
    private float topCoordinateInSpiralY;
    private float largestSizeInSpiral;
    private boolean coordinatesAndSizesUpdated;
    private boolean orientationJustChanged;
    public boolean touchDownOnScreenTempleView;
    private float downX;
    private float downY;
    private ArrayList<Float> movingCoordinatesLastTime;
    private long downTime;
    private float ultimateScreenWidth;
    private float initialRForLocation;
    private float windowWidth;
    private float windowHeight;
    private String lastSpiralEffectHolder;
    private static ArrayList<Integer> allLargeImageIds;
    private String oneTempleInfo;
    private static ArrayList<Integer> allTempleInfoFileIds;
    private Boolean show_label;
    private String selectedYear;
    private Integer realEachIndex;
    private String templeUrl;
    private SingleMemberImage singleMemberImageView;
    private int staticCoordinatesGet = 0;
    private AlertDialog singleTempleDialog;
    private Paint noImageCirclePaint;


    public SpiralView(Context context) {
        super(context);

        bluePaint = new Paint();
        bluePaint.setColor(Color.parseColor("#17252a"));
        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setTextSize(35);
        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setTextSize(60);
        spiralPaint = new Paint();
        spiralPaint.setColor(Color.RED);
        spiralPaint.setStyle(Paint.Style.STROKE);
        spiralPaint.setStrokeWidth(5);
        spiralLine = new Path();
        loadedImages = false;
        spiralCoordinates = new ArrayList<>();
        sizes = new ArrayList<>();
        onScreenTemples = new ArrayList<>();
        oneOnScreenTemple = new ArrayList<>();
        allTempleLinks = new ArrayList<>();
        allEventsDates = new ArrayList<>();
        allYears = new ArrayList<>();
        theta = 4400;
        currentTempleMatrix = new Matrix();
        coordinatesAndSizesUpdated = FALSE;
        orientationJustChanged = FALSE;
        movingCoordinatesLastTime = new ArrayList<>();
        yearDisplayPaint = new Paint();
        selectedYear = "";

        noImageCirclePaint = new Paint();
        noImageCirclePaint.setColor(Color.parseColor("#17252a"));
        noImageCirclePaint.setStyle(Paint.Style.FILL);

    }

    public void setDegree(int sliderP) {
        theta = sliderP;
        //Log.d("theta is ", theta + " ***************************************************************************************");
    }

    public float getLastProgress() {
        //Log.d("theta", " is " + theta + " ");
        return theta;
    }

    public boolean sliderMovingOrAnimationInProgress() {
        return sliderMoving;
    }

    public void sliderStart(boolean s) {
        sliderMoving = s;
    }

    public void sliderStop(boolean s) {
        sliderMoving = s;
    }

    public void sliderInProgress(boolean s) {
        sliderMoving = s;
    }

    public void readLinksFile() {
        try {
            InputStream allTempleLinksFile =  getContext().getResources().openRawResource(R.raw.all_temple_links);
            if (allTempleLinksFile != null)
            {
                InputStreamReader ir = new InputStreamReader(allTempleLinksFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                int atThisLine = 0;
                while (( line = br.readLine()) != null) {
                    allTempleLinks.add(line+"\n");
                    if (atThisLine < memberObjects.size()) {
                        memberObjects.get(atThisLine).setLink(line+"\n");
                        atThisLine ++;
                    }
                }
                allTempleLinksFile.close();
            }
        }
        catch (java.io.FileNotFoundException e)
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        catch (IOException e)
        {
            Log.d("TestFile", e.getMessage());
        }
        //Log.d("allTempleLinks is ", allTempleLinks.get(1) + "");
    }

    public void readOneInfoFile(int id) {
        try {
            InputStream allTempleInfoFile =  this.getResources().openRawResource(id);
            if (allTempleInfoFile != null)
            {
                InputStreamReader ir = new InputStreamReader(allTempleInfoFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                while (( line = br.readLine()) != null) {
                    oneTempleInfo = oneTempleInfo + line + "\n";
                }
                allTempleInfoFile.close();
            }
        }
        catch (java.io.FileNotFoundException e)
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        catch (IOException e)
        {
            Log.d("TestFile", e.getMessage());
        }
    }

    public void readInfoFile() {
        try {
            InputStream allTempleInfoFile =  this.getResources().openRawResource(R.raw.all_events_dates);
            if (allTempleInfoFile != null)
            {
                InputStreamReader ir = new InputStreamReader(allTempleInfoFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                while (( line = br.readLine()) != null) {
                    allEventsDates.add(line+"\n");
                    allYears.add(line.substring(line.length() - 5));
                }
                allTempleInfoFile.close();

            }
        }
        catch (java.io.FileNotFoundException e)
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        catch (IOException e)
        {
            Log.d("TestFile", e.getMessage());
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        float touchX= m.getX();;
        float touchY= m.getY();;
        //Log.d("TOUCH EVENT",  " touch event happens on screen at ************* " + touchX + " " + touchY );
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            downX = m.getX();
            downY = m.getY();
            //Toast.makeText(getContext(), "touched DOWN at " + downX + " " + downY, Toast.LENGTH_SHORT).show();
            movingCoordinatesLastTime.clear();
            movingCoordinatesLastTime.add(downX);
            movingCoordinatesLastTime.add(downY);
            //Log.d("DOWN",  " finger down on screen at |||||||||||||||" + downX + " " + downY );
            touchDownOnScreenTempleView = TRUE;
            downTime = System.currentTimeMillis();
        }

        if (m.getAction() == MotionEvent.ACTION_MOVE) {
            //Toast.makeText(getContext(), "finger moving on screen", Toast.LENGTH_SHORT).show();

            float movingX = m.getX();
            float movingY = m.getY();
            //Log.d("MOVING",  " finger moving on screen at " + movingX + " " + movingY );

            float lastX = movingCoordinatesLastTime.get(0);
            float lastY = movingCoordinatesLastTime.get(1);
            float xDisplacementFromLastMove = movingX - lastX;
            float yDisplacementFromLastMove = movingY - lastY;
            //Log.d("movingCoordinates", "movingCoordinatesLastTime is " + movingCoordinatesLastTime);

            movingCoordinatesLastTime.clear();
            movingCoordinatesLastTime.add(movingX);
            movingCoordinatesLastTime.add(movingY);
            //Log.d("xy displacementFLT ", xDisplacementFromLastMove + " " + yDisplacementFromLastMove);

            boolean top = (touchY <= centerY);
            boolean bottom = (touchY < 9 * screenHeight / 10 && touchY > centerY);

            boolean leftThirdVertical = (touchX <= centerX - screenWidth / 6 );
            boolean middleThirdVertical = (touchX > centerX - screenWidth / 6 && touchX < centerX + screenWidth / 6);
            boolean rightThirdVertical = (touchX >= centerX + screenWidth / 6 );

            int moveTheta = 10;

            boolean thetaMaxReached = theta >= 4400;
            boolean thetaMinReached = theta <= 30;

            if (leftThirdVertical) {
                if (yDisplacementFromLastMove > 0) {
                    if (thetaMaxReached) {
                    } else {
                        theta = theta + moveTheta;
                    }
                } else if (yDisplacementFromLastMove < 0) {
                    if (thetaMinReached) {
                    } else {
                        theta = theta - moveTheta;
                    }
                }
            } else if (rightThirdVertical) {
                if (yDisplacementFromLastMove > 0) {
                    if (thetaMinReached) {
                    } else {
                        theta = theta - moveTheta;
                    }
                } else if (yDisplacementFromLastMove < 0) {
                    if (thetaMaxReached) {
                    } else {
                        theta = theta + moveTheta;
                    }
                }
            } else if (middleThirdVertical) {
                if (touchY > centerY - screenWidth / 6 && touchY < centerY + screenWidth / 6) {
                    //do nothing, touch movement in center of spiral is disabled
                } else if (top) {
                    //check xd
                    if (xDisplacementFromLastMove > 0) {
                        if (thetaMinReached) {
                        } else {
                            theta = theta - moveTheta;
                        }
                    } else if (xDisplacementFromLastMove < 0) {
                        if (thetaMaxReached) {
                        } else {
                            theta = theta + moveTheta;
                        }
                    }
                } else if (bottom) {
                    //check xd
                    if (xDisplacementFromLastMove > 0) {
                        if (thetaMaxReached) {
                        } else {
                            theta = theta + moveTheta;
                        }
                    } else if (xDisplacementFromLastMove < 0) {
                        if (thetaMinReached) {
                        } else {
                            theta = theta - moveTheta;
                        }
                    }
                }
            }
        }

        if (m.getAction() == MotionEvent.ACTION_UP) {
            long upTime = System.currentTimeMillis();
            long period = upTime - downTime;
            touchDownOnScreenTempleView = FALSE;
            //helper--time test
            //Long timeLong = System.currentTimeMillis();
            //String time = String.valueOf(timeLong);
            //Toast.makeText(getContext(), "current time is " + time, Toast.LENGTH_SHORT).show();
            float x = m.getX();
            float y = m.getY();
            //Toast.makeText(getContext(), "touched a circle when UP at " + x + " " + y, Toast.LENGTH_SHORT).show();

            if (y < 9 * screenHeight / 10 && period < 100) {
                boolean singleTempleViewOpened = false;

                Collections.reverse(onScreenTemples);
                for (ArrayList<Float> eachOnScreenTemple : onScreenTemples) {
                    //remember each Float in inner class is a object, when convert it to int you need to use some method.
                    eachIndex = (int)(eachOnScreenTemple.get(0).floatValue());
                    float eachXCoordinate = eachOnScreenTemple.get(1);
                    float eachYCoordinate = eachOnScreenTemple.get(2);
                    float eachSize = eachOnScreenTemple.get(3);
                    float distanceToCurrentCoordinate = (float) (Math.sqrt(Math.pow(Math.abs(x - eachXCoordinate), 2) + Math.pow(Math.abs(y - eachYCoordinate), 2)));

                    if (distanceToCurrentCoordinate < eachSize) {
                        //Toast.makeText(getContext(), "touched a circle at " + x + " " + y + " and eachIndex here is " + eachIndex , Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getContext(), "how many onScreenTemples last time? " + onScreenTemples.size(), Toast.LENGTH_SHORT).show();
                        //Log.d("singleTempleViewOpen? ", singleTempleViewOpened + "");
                        if (singleTempleViewOpened == false) {
                            if (eachIndex <= 144) { // it's useless here, it;'s for the older version of the app, this number should be equal the the number of objects - 1, so that all circles can b bring up detail dialog.
                                singleTempleViewOpened = true;
                                //Log.d("eachIndex is ", eachIndex + " when click on circle");
                                singleTempleDialog();
                            } else {
                                //no link
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Nothing Here");
                                builder.setMessage("future temples to come!");
                                builder.setIcon(R.mipmap.ic_launcher_round);
                                builder.setCancelable(true);
                                final AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }
                }
                Collections.reverse(onScreenTemples);
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    public void singleTempleDialog() {


        LinearLayout.LayoutParams nice = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
        LinearLayout.LayoutParams niceTwo = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
        LinearLayout.LayoutParams niceFour = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 4);

        LinearLayout.LayoutParams wrapContent = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        LinearLayout lnl = new LinearLayout(getContext());
        lnl.setOrientation(LinearLayout.VERTICAL);

        LinearLayout lnlH = new LinearLayout(getContext());
        lnlH.setOrientation(LinearLayout.HORIZONTAL);

        if (eachIndex == 0) {
            singleMemberImageView = new SingleMemberImage(getContext(), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex + 1));
        } else if (eachIndex == 144){
            singleMemberImageView = new SingleMemberImage(getContext(), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex - 1), allLargeImageIds.get(eachIndex)); // no next event
        } else {
            singleMemberImageView = new SingleMemberImage(getContext(), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex - 1), allLargeImageIds.get(eachIndex + 1));

        }

        singleMemberImageView.setPadding(0,0,0,0);
        //singleTempleImageView.setBackgroundColor(Color.RED);

        // milestone dates
        oneTempleInfo = "";
        readOneInfoFile(allTempleInfoFileIds.get(eachIndex));

        final TextView singleTempleTextView = new TextView(getContext());
        singleTempleTextView.setText(oneTempleInfo);
        //singleTempleTextView.setBackgroundColor(Color.BLUE);
        singleTempleTextView.setGravity(Gravity.CENTER);
        singleTempleTextView.setLayoutParams(nice);
        singleTempleTextView.setTextSize(15);

        ScrollView sv = new ScrollView(getContext());
        //sv.setPadding(100,100,100,100);
        sv.addView(singleTempleTextView);

        // here is where we get templeUrl, to avoid the eachIndex change error
        //final String templeUrl = allTempleLinks.get(eachIndex);
        realEachIndex = eachIndex; // we do this because each index is changing for some reason later...
        templeUrl = memberObjects.get(realEachIndex).link;

        final TextView singleTempleDialogTitleView = new TextView(getContext());
        singleTempleDialogTitleView.setText(allEventsDates.get(realEachIndex));
        singleTempleDialogTitleView.setTextSize(20);
        singleTempleDialogTitleView.setPadding(0,20,0,0);
        singleTempleDialogTitleView.setTextColor(Color.BLACK);
        singleTempleDialogTitleView.setGravity(Gravity.CENTER);

        final long[] timeStamp = new long[1];
        timeStamp[0] = 0;
        // view last or next temple buttons
        Button left = new Button(getContext());
        left.setWidth((int)screenWidth / 10);
        left.setText(">");
        left.setTextSize(20);
        left.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    // do nothing
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (realEachIndex < 144) {
                        if (System.currentTimeMillis() - timeStamp[0] > 1550) {
                            realEachIndex = realEachIndex + 1;
                            singleMemberImageView.moveImage("left");
                            int nextTempleId = 0;
                            if (realEachIndex + 1 > 144) {
                                nextTempleId = allLargeImageIds.get(realEachIndex);
                            } else {
                                nextTempleId = allLargeImageIds.get(realEachIndex + 1);
                            }
                            singleMemberImageView.updateThreeTemplesBitmapIds(allLargeImageIds.get(realEachIndex), allLargeImageIds.get(realEachIndex - 1), nextTempleId);
                            templeUrl = memberObjects.get(realEachIndex).link;
                            singleTempleDialogTitleView.setText(allEventsDates.get(realEachIndex));
                            oneTempleInfo = "";
                            readOneInfoFile(allTempleInfoFileIds.get(realEachIndex));
                            singleTempleTextView.setText(oneTempleInfo);
                            singleTempleTextView.scrollTo(0,0);
                            timeStamp[0] = System.currentTimeMillis();
                        }
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.dubai_temple_is_the_most_recent_temple), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        Button right = new Button(getContext());
        right.setWidth((int)screenWidth / 10);
        right.setText("<");
        right.setTextSize(20);
        right.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    // do nothing
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    // do something
                    if (realEachIndex > 0) {
                        if (System.currentTimeMillis() - timeStamp[0] > 1550) {
                            realEachIndex = realEachIndex - 1;
                            singleMemberImageView.moveImage("right");
                            int lastTempleId = 0;
                            if (realEachIndex - 1 < 0) {
                                lastTempleId = allLargeImageIds.get(realEachIndex);
                            } else {
                                lastTempleId = allLargeImageIds.get(realEachIndex - 1);
                            }
                            singleMemberImageView.updateThreeTemplesBitmapIds(allLargeImageIds.get(realEachIndex), lastTempleId, allLargeImageIds.get(realEachIndex + 1));
                            templeUrl = memberObjects.get(realEachIndex).link;
                            singleTempleDialogTitleView.setText(allEventsDates.get(realEachIndex));
                            oneTempleInfo = "";
                            readOneInfoFile(allTempleInfoFileIds.get(realEachIndex));
                            singleTempleTextView.setText(oneTempleInfo);
                            singleTempleTextView.scrollTo(0,0);
                            timeStamp[0] = System.currentTimeMillis();
                        }
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.kirtland_temple_is_the_oldest_temple), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        singleMemberImageView.setLayoutParams(nice);
        left.setLayoutParams(niceFour);
        right.setLayoutParams(niceFour);
        lnlH.setLayoutParams(nice);


        // the left button is actually on the right and the right button is actually on the left.
        lnlH.addView(right); // lnlH.addView(left);
        lnlH.addView(singleMemberImageView);
        lnlH.addView(left); // lnlH.addView(right);

        lnl.addView(singleTempleDialogTitleView);
        //singleTempleDialogTitleView.setBackgroundColor(Color.YELLOW);
        lnl.addView(lnlH);
        //lnlH.setBackgroundColor(Color.GREEN);
//        lnl.addView(sv);
//        sv.setBackgroundColor(Color.RED);
        ((ViewGroup)singleTempleTextView.getParent()).removeView(singleTempleTextView);
        lnl.addView(singleTempleTextView);
        //singleTempleTextView.setBackgroundColor(Color.RED);
        singleTempleTextView.setHeight((int)(Math.min(screenWidth, screenHeight) * 0.3));
        singleTempleTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        // singleTempleDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //builder.setTitle(allTempleInfo.get(realEachIndex*3));
        builder.setView(lnl);
        builder.setCancelable(true);
        builder.setCancelable(true);
//        builder.setPositiveButton(getResources().getString(R.string.website_button), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                //do nothing
//            }
//        });
        builder.setNegativeButton(getResources().getString(R.string.return_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //set onclick method for this button below
            }
        });
        singleTempleDialog = builder.create();
        singleTempleDialog.show();

        //singleTempleDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = singleTempleDialog.getWindow().getAttributes();
        int h = 0;
        int w = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            h = (int)(Math.min(windowHeight, windowWidth) * 1.5);
            w = (int)Math.min(windowHeight, windowWidth);
        } else {
            h = (int)(Math.min(windowHeight, windowWidth) * 0.9);
            w = (int)Math.min(windowHeight, windowWidth);
        }
        params.height = h;
        params.width =  w;
        singleTempleDialog.getWindow().setAttributes(params);
        singleTempleDialog.show();

//        Button btnPositive = singleTempleDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = singleTempleDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams mNegativeButtonLL = (LinearLayout.LayoutParams) btnNegative.getLayoutParams();
        mNegativeButtonLL.gravity = Gravity.CENTER;
        mNegativeButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        btnNegative.setLayoutParams(mNegativeButtonLL);


//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
//        layoutParams.weight = 10;
//        btnPositive.setLayoutParams(layoutParams);
//        btnNegative.setLayoutParams(layoutParams);

//        singleTempleDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //singleTempleDialog.dismiss();
//                //singleTempleDialog stays when click on website button
//
//                // for some reason, i don't why, but each index is changed in here,
//                // so we get templeUrl before this, according to the correct eachIndex
//                //String templeUrl = allTempleLinks.get(eachIndex);
//                //Log.d("eachIndex is ", eachIndex + " when click on website button");
//                //Log.d("templeUrl is ", templeUrl + "");
//
//                if (templeUrl.equals("" + "\n")) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    builder.setTitle("No Link Available");
//                    builder.setMessage("Member does not have a website yet");
//                    builder.setIcon(R.mipmap.ic_launcher_round);
//                    //点击对话框以外的区域是否让对话框消失
//                    builder.setCancelable(true);
//                    final AlertDialog dialog = builder.create();
//                    dialog.show();
//
//                } else {
//                    Intent eachTemplePage= new Intent();
//                    eachTemplePage.setAction("android.intent.action.VIEW");
//                    Uri eachTemplePage_url = Uri.parse(templeUrl);
//                    eachTemplePage.setData(eachTemplePage_url);
//                    getContext().startActivity(eachTemplePage);
//                }
//            }
//        });
    }

    public void orientationJustChanged(boolean b) {
        orientationJustChanged = b;
        //singleTempleImageView.updatePositionAndSizeOnceOrientationChanged();
        if (singleMemberImageView != null) { // the rotate phone without clicking on a temple
            singleMemberImageView.orientationJustChanged(b);
            //singleTempleImageView.invalidate();

            // reset single temple dialog size according to screen size once orientation change happens
            WindowManager.LayoutParams params = singleTempleDialog.getWindow().getAttributes();
            int h = 0;
            int w = 0;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                h = (int)(Math.min(windowHeight, windowWidth) * 1.5);
                w = (int)Math.min(windowHeight, windowWidth);
            } else {
                h = (int)(Math.min(windowHeight, windowWidth) * 0.9);
                w = (int)Math.min(windowHeight, windowWidth);
            }
            params.height = h;
            params.width =  w;
            singleTempleDialog.getWindow().setAttributes(params);
        }

    }

    public void getWindowSize(float w, float h) {
        windowWidth = w;
        windowHeight = h;
    }

    public void resetStaticCoordinatesGet() {
        staticCoordinatesGet = 0;
    }
    @Override
    public void onDraw(Canvas c) {
        //Toast.makeText(getContext(), "onscreen temples" + onScreenTemples.size(), Toast.LENGTH_SHORT).show();
        //Log.d("onscreen temples ", "" + onScreenTemples.size());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenWidth = c.getWidth() / 2;
            screenHeight = c.getHeight();
            centerX = screenWidth / 2 + 3 * screenWidth / 16;
            centerY = screenHeight / 2;
            ultimateScreenWidth = Math.min(windowHeight, windowWidth);
            yearDisplayPaint.setTextSize((int)(2 * screenHeight / 25));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            screenWidth = c.getWidth();
            screenHeight = c.getHeight();
            centerX = screenWidth / 2;
            centerY = screenHeight / 2;
            ultimateScreenWidth = screenWidth;
            yearDisplayPaint.setTextSize((int)(screenHeight / 25));
            //Log.d("PORTRAIT ", "|||||||||||||||||||||||||||||" + screenWidth);
        }
        //Log.d("CENTER X ", centerX + " ");
        //Log.d("CENTER Y ", centerY + " ");
        initialR = screenWidth / 10;
        initialRForLocation = ultimateScreenWidth / 10;

        String spiral_effect = PrefsActivity.getSpiralEffectPref(getContext());
        show_label = PrefsActivity.getShowLabelPref(getContext());
        //Log.d("spiral effect ", spiral_effect + " ");

        if (spiral_effect.equalsIgnoreCase("static") && staticCoordinatesGet <= 10) {
            spiralCoordinates.clear();
            getCoordinates();
            staticCoordinatesGet += 1;
        }


        if (orientationJustChanged == TRUE) {
            spiralCoordinates.clear();
            //sizes.clear();
            getCoordinates();
            //getSizes();
            orientationJustChanged = FALSE;
            //Log.d("coordinates and sizes ", " just reset ");
            //Log.d("orChanged coorSize ", " ++++++++++++++++ "
                    //+ spiralCoordinates.size() + " "
                    //+ sizes.size());
            //Log.d("spiralCoordinates", spiralCoordinates + " ");
            //Log.d("sizes", sizes + " ");
        }
        //when app first launch this got called.
        if (coordinatesAndSizesUpdated == FALSE) {
            spiralCoordinates.clear();
            getCoordinates();
            getSizes();
            coordinatesAndSizesUpdated = TRUE;
            //Log.d("launch coorSize ", " ++++++++++++++++ "
                    //+ spiralCoordinates.size() + " "
                    //+ sizes.size());
            //Log.d("spiralCoordinates", spiralCoordinates + " ");
            //Log.d("sizes", sizes + " ");
            //Log.d("screenWidth", screenWidth + " ");
            //Log.d("screenHeight", screenHeight + " ");
        }


        // we need to update the coordinates when switching to static mode from other effect. other wise coordinates for other effect will be kept.
        if (spiral_effect.equalsIgnoreCase("static")) {
            //just turn to static or it was static before?
            if (lastSpiralEffectHolder == null) {
                //first time run, do noting
            } else {
                if (lastSpiralEffectHolder.equalsIgnoreCase(spiral_effect)) {
                    //do nothing, it have been static last time
                } else {
                    //just turn to static, get a new coordinates
                    spiralCoordinates.clear();
                    getCoordinates();
                }
            }
        } else {
            //not static, do nothing
        }
        lastSpiralEffectHolder = spiral_effect;


        if (spiral_effect.equalsIgnoreCase("spin")) {
            spiralCoordinates.clear();
            getCoordinatesRotateRegular();
        } else if (spiral_effect.equalsIgnoreCase("zoom")) {
            spiralCoordinates.clear();
            getCoordinatesRotateZoom();
        } else if (spiral_effect.equalsIgnoreCase("threeD")) {
            spiralCoordinates.clear();
            getCoordinatesThreeD();
        }

        //c.drawColor(Color.parseColor("#66ccff"));

        //Member View Background color
        //c.drawColor(Color.parseColor("#24292b"));
//        c.drawColor(Color.parseColor("#17252a"));
        c.drawColor(Color.parseColor("#c64e67"));

        //we just want to load the images once, we don't have to load it every time when we re-draw. otherwise the program is gonna be so slow
        if (loadedImages == false) {
            loadedImages = true;
            //get the temple images in array list

            //when app launches, images are loaded according to screen width
            //when launches landscape, according to window height
            float temp;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                temp = windowHeight;
            } else {
                temp = screenWidth;
            }
            ImageCache.init(getResources(), temp, screenHeight);

            allLargeImageIds = ImageCache.getAllLargeImagesIds();
            allTempleInfoFileIds = ImageCache.getAllInfoFilesIds();

            //temples = ImageCache.getTemplesList();
            memberObjects = ImageCache.getTempleObjectsList(); // more OO

            readLinksFile();
            readInfoFile();


//            for (Member t: memberObjects) {
//                t.setLink(allTempleLinks.get(memberObjects.indexOf(t)));
//            }

            yearDisplayPaint.setColor(Color.parseColor("#def2f1"));
            yearDisplayPaint.setStyle(Paint.Style.FILL);
            yearDisplayPaint.setTextAlign(Paint.Align.CENTER);
        }

        //helper
        //c.drawText("Screen Width and Height are " + screenWidth + " " + screenHeight, 0, screenHeight - 100, bluePaint);
        //c.drawText("how many temples " + temples.size() + " ", 0, screenHeight - 200, redPaint);
        //c.drawRect(0,3 * screenHeight/4, screenWidth, 3 * screenHeight/4 + 10, bluePaint);
        //the middle circle image is here ==============================================
        //drawMiddleCircle(c);

        placeAllCircles(c);

        //c.drawText( "slider progress is: " + theta, screenWidth / 2, 39 * screenHeight / 40, yearDisplayPaint);
        //c.drawText( allYears.get(allYears.size()-1).length() + "", screenWidth / 2, 39 * screenHeight / 40, yearDisplayPaint);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            yearDisplayLandscape(c);

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //yearDisplay(c);
        }


    }

    public void drawTempleLabels(float ts, Member t, Canvas c) { // more OO

        float newCurrentTempleRadius = t.size * screenWidth / 2;

        Paint thisTempleLabelPaint = new Paint();

        thisTempleLabelPaint.setColor(Color.parseColor("#def2f1"));
        thisTempleLabelPaint.setStyle(Paint.Style.FILL);
        thisTempleLabelPaint.setTextSize((int)(newCurrentTempleRadius/3));  //if we are drawing the years as main object , before: ((int)(newCurrentTempleRadius/3))
        thisTempleLabelPaint.setTextAlign(Paint.Align.CENTER);
        thisTempleLabelPaint.setShadowLayer(20,0,0,Color.BLACK);

//        int thisTempleIndex = temples.indexOf(t);
        int thisTempleIndex = memberObjects.indexOf(t); // more OO

        String thisTempleName = allEventsDates.get(thisTempleIndex);


        if (sliderMoving == false && show_label) {
            c.drawText(thisTempleName.substring(thisTempleName.length() - 5), t.x, t.y + newCurrentTempleRadius - thisTempleLabelPaint.getTextSize()/2, thisTempleLabelPaint);
        }




//        Locale curLocale = getResources().getConfiguration().locale;
//
//        String thisTempleLocation = "";
//        //通过Locale的equals方法，判断出当前语言环境
//        if (curLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
//            //中文
//            thisTempleLocation = thisTempleName.substring(0, thisTempleName.length() - 3);
//        } else {
//            //英文
//            thisTempleLocation = thisTempleName.substring(0, thisTempleName.length() - 7);
//        }
//        //String thisTempleLocation = thisTempleName ;//.substring(0, thisTempleName.length() - 7);
//
//        String[] thisTempleLocationWords = thisTempleLocation.split(" ");
//
//        String thisTempleNameOne = "";
//        String thisTempleNameTwo = "";
//        if (thisTempleLocationWords.length % 2 == 0) { // if there are even number of words in location, then each line has the same number of words
//            for (int i = 0; i < thisTempleLocationWords.length / 2; i ++) {
//                thisTempleNameOne += thisTempleLocationWords[i] + " ";
//            }
//            for (int i = thisTempleLocationWords.length / 2; i < thisTempleLocationWords.length ; i ++) {
//                thisTempleNameTwo += thisTempleLocationWords[i] + " ";
//            }
//        } else { // if there are odd number of words in location, then first line has one more line than second line
//            for (int i = 0; i < thisTempleLocationWords.length / 2 + 1; i ++) {
//                thisTempleNameOne += thisTempleLocationWords[i] + " ";
//            }
//            for (int i = thisTempleLocationWords.length / 2 + 1; i < thisTempleLocationWords.length ; i ++) {
//                thisTempleNameTwo += thisTempleLocationWords[i] + " ";
//            }
//        }
//
//        if (sliderMoving == false && ts < 200 && thisTempleIndex < 185 && show_label) {
//            //c.drawText(thisTempleName, currentTempleX, currentTempleY + newCurrentTempleRadius + thisTempleLabelPaint.getTextSize(), thisTempleLabelPaint);
//            c.drawText(thisTempleNameOne, t.x, t.y + newCurrentTempleRadius - thisTempleLabelPaint.getTextSize(), thisTempleLabelPaint);
//            c.drawText(thisTempleNameTwo, t.x, t.y + newCurrentTempleRadius, thisTempleLabelPaint);
//        }
    }

    public void getSelectedYear(String s) {
        selectedYear = s;
    }

    public void actuallyDrawing(Member t, Canvas c, int thisTempleIndex) { // more OO


        float newCurrentTempleRadius = t.size * screenWidth / 2;

        currentTempleMatrix.setScale(4 * t.size, 4 * t.size);
//        currentTempleMatrix.postTranslate(currentTempleX - t.getWidth()  *currentTempleSize*2, currentTempleY - t.getHeight() * currentTempleSize*2);
        currentTempleMatrix.postTranslate(t.x - t.image.getWidth()  * t.size * 2, t.y - t.image.getHeight() * t.size * 2); // more OO

        Paint selectedYearTempleFramePaint = new Paint();
        selectedYearTempleFramePaint.setColor(Color.parseColor("#287a78"));
        selectedYearTempleFramePaint.setStyle(Paint.Style.FILL);

        // if current temple is with selected year then draw a circle frame
        if (allYears.get(thisTempleIndex).equals(selectedYear)) {
            c.drawCircle(t.x, t.y, newCurrentTempleRadius * 1.1f , selectedYearTempleFramePaint);

        } else {
            // do nothing
        }

//        c.drawBitmap(t, currentTempleMatrix, null);

        // we are not drawing the images!!! because we don't have photos...
        c.drawBitmap(t.image, currentTempleMatrix, null); // more OO
        //c.drawCircle(t.x, t.y, newCurrentTempleRadius, noImageCirclePaint);

    }

    public void placeAllCircles(Canvas c) {
        //place all circles, and get the index of on screen temples
        //this method also call actualDrawing method to draw

        //Log.d("spiralcoors: ", " in placeallcircles " + spiralCoordinates + " ");
        //Log.d("spiralcoors: ", " in placeallcircles " + spiralCoordinates + " ");

        onScreenTemples.clear();
        for (Member t : memberObjects) { //more OO: for (Bitmap t : temples) {
            int thisTempleIndex = memberObjects.indexOf(t); // more OO: int thisTempleIndex = temples.indexOf(t);
            float ts = theta - 30 * memberObjects.indexOf(t); // more OO: float ts = theta - 30 * temples.indexOf(t);
            if (ts > 0 && ts < spiralCoordinates.size() - 1) {

                // set this temple's size, x and y once we know it should be on screen
                t.size = sizes.get((int) (ts));
                t.x = spiralCoordinates.get((int) (ts)).get(0);
                t.y = spiralCoordinates.get((int) (ts)).get(1);

                actuallyDrawing(t, c, thisTempleIndex);
                drawTempleLabels(ts, t, c);

                //add all on screen temples index to a array list once the slider stopped moving,
                float currentTempleRadius = t.size * screenWidth / 2;
                //inner array list: (this onScreenTemple index, x coordinate, y coordinate, temple radius at this position)
                oneOnScreenTemple.add((float)thisTempleIndex);
                oneOnScreenTemple.add(t.x);
                oneOnScreenTemple.add(t.y);
                oneOnScreenTemple.add(currentTempleRadius);
                //be aware of adding one array list to another array list of array list then clear old one, remember you must copy.
                ArrayList<Float> oneOnScreenTempleCopy = new ArrayList<>();
                oneOnScreenTempleCopy.addAll(oneOnScreenTemple);
                onScreenTemples.add(oneOnScreenTempleCopy);
                oneOnScreenTemple.clear();
            }
        }
        // we need this line of code, so that in 3 d view, only the front temple opens when user clicks
        Collections.reverse(onScreenTemples);
        //Log.d("onscreen temples ", "" + onScreenTemples.size());
    }

    public void yearDisplay(Canvas c) {

        //get the index of on screen temples,
        //the first one in on screen temples to the last
        //go to temple info file, the specific line to get years
        //3 lines each temple in the file

        //c.drawRect(0, 9 * screenHeight / 10, screenWidth, screenHeight, bluePaint);
        float firstOnScreenTempleIndex = 0;
        float lastOnScreenTempleIndex = 0;

        // new year display logic
        if (onScreenTemples.size() != 0) {
            lastOnScreenTempleIndex = (onScreenTemples.get(onScreenTemples.size()-1).get(0));
            firstOnScreenTempleIndex = (onScreenTemples.get(0).get(0));
        }

        String endYear = allEventsDates.get((int)(firstOnScreenTempleIndex));
        String startYear = allEventsDates.get((int)(lastOnScreenTempleIndex)) ;



        Locale curLocale = getResources().getConfiguration().locale;
        if (curLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
            // do nothing //中文
            startYear = startYear.substring(0,4);
            endYear = endYear.substring(0,4);
        } else {
            startYear = startYear.substring(startYear.length()-5);
            endYear = endYear.substring(endYear.length()-5);
            //英文
        }



        if (theta <= 40){
            c.drawText( getResources().getString(R.string.first_temple) + "" + "1836", screenWidth / 2, 39 * screenHeight / 40, yearDisplayPaint);
        } else if (theta > 5550 ) {
            c.drawText( getResources().getString(R.string.future_temples), screenWidth / 2, 39 * screenHeight / 40, yearDisplayPaint);
        } else if (endYear.contains("0000") || endYear.contains("1111")){
            c.drawText( getResources().getString(R.string.years_of_temples) + " "  + startYear + "--- " + 2020, screenWidth / 2, 39 * screenHeight / 40, yearDisplayPaint);
        } else {
            //Log.d("endYeas is ", endYear);
            c.drawText( getResources().getString(R.string.years_of_temples) + " "  + startYear + "--- " + endYear, screenWidth / 2, 39 * screenHeight / 40, yearDisplayPaint);
        }
    }

    public void yearDisplayLandscape(Canvas c) {

          c.drawText(getResources().getString(R.string.welcome_to_view), 6.5f * screenWidth / 4, 18 * screenHeight / 40, yearDisplayPaint);

//        c.drawRect( 5 * screenWidth / 4, 0, 2 * screenWidth, screenHeight, bluePaint);
//        float firstOnScreenTempleIndex = 0;
//        float lastOnScreenTempleIndex = 0;
//
//        // new year display logic
//        if (onScreenTemples.size() != 0) {
//            lastOnScreenTempleIndex = (onScreenTemples.get(onScreenTemples.size()-1).get(0));
//            firstOnScreenTempleIndex = (onScreenTemples.get(0).get(0));
//        }
//        String endYear = allEventsDates.get((int)(firstOnScreenTempleIndex));
//        String startYear = allEventsDates.get((int)(lastOnScreenTempleIndex)) ;
//
//        Locale curLocale = getResources().getConfiguration().locale;
//        if (curLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
//            // do nothing //中文
//            startYear = startYear.substring(0,4);
//            endYear = endYear.substring(0,4);
//        } else {
//            startYear = startYear.substring(startYear.length()-5);
//            endYear = endYear.substring(endYear.length()-5);
//            //英文
//        }
//        if (theta <= 40){
//            c.drawText(getResources().getString(R.string.first_temple), 6.5f * screenWidth / 4, 18 * screenHeight / 40, yearDisplayPaint);
//            c.drawText("1836", 6.5f * screenWidth / 4, 22 * screenHeight / 40, yearDisplayPaint);
//        } else if (theta > 5550 ) {
//            c.drawText(getResources().getString(R.string.future_temples), 6.5f * screenWidth / 4, 20 * screenHeight / 40, yearDisplayPaint);
//        } else if (endYear.contains("0000") || endYear.contains("1111")){
//            c.drawText(getResources().getString(R.string.years_of_temples) + " " , 6.5f * screenWidth / 4, 15 * screenHeight / 40, yearDisplayPaint);
//            c.drawText(startYear + " --- " + 2020, 6.5f * screenWidth / 4, 25 * screenHeight / 40, yearDisplayPaint);
//        } else {
//            c.drawText(getResources().getString(R.string.years_of_temples) + " " , 6.5f * screenWidth / 4, 15 * screenHeight / 40, yearDisplayPaint);
//            c.drawText(startYear + " --- " + endYear, 6.5f * screenWidth / 4, 25 * screenHeight / 40, yearDisplayPaint);
//       }
    }

    public void getCoordinates() {
        //spiral are impacted a lot by initialR.
        //circles locations remain whether landscape or portrait
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //initialRForLocation is 0 when app launches, causing spiral becomes a dot.
            //when first launch, I treat windowHeight as initial R, which is just screen width later
            //(有差距，因为有状态栏，so window height is slightly smaller than screen width)
            if (coordinatesAndSizesUpdated == FALSE) {
                initialR = windowHeight / 10;
            } else {
                initialR = initialRForLocation;
            }
            //Log.d("initialR", " " + initialR);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initialR = screenWidth / 10;
        }

        //for (float t = -30; t < 30; t += 0.02f) {
        for (float t = -18; t < 17.5; t += 0.02f) {
            //Equiangular spiral function：
            //x = p * cosA, y = p * sinA, where p = N * e^(B * cotC)
            //When C = PI/2, graph is a circle, when C = 0, graph is a straight line
            float x = centerX + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t));
            float y = centerY + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t));

            //intendSize is the new size compare to original (original has width of 0.1 * screenWidth)

            ArrayList<Float> oneSpiralCoordinate = new ArrayList<>();
            oneSpiralCoordinate.add(x);
            oneSpiralCoordinate.add(y);

            ArrayList<Float> oneSpiralCoordinateCopy = new ArrayList<>();
            oneSpiralCoordinateCopy.addAll(oneSpiralCoordinate);
            //Log.d("x y coordinate", oneSpiralCoordinate.get(0) + "<- x, y -> " + oneSpiralCoordinate.get(1));
            spiralCoordinates.add(oneSpiralCoordinateCopy);
            //Log.d("x y coordinate", "right after adding, spiralCoordinates are " + spiralCoordinates);
            oneSpiralCoordinate.clear();
        }

        topCoordinateInSpiralX = spiralCoordinates.get(spiralCoordinates.size()-1).get(0);
        topCoordinateInSpiralY = spiralCoordinates.get(spiralCoordinates.size()-1).get(1);

        //when q += 12f, top lines circles next to each other the whole time\
        //must change the same time as getCoordinates()
        for (float q = 0; q < 20; q += 1) {

            ArrayList<Float> oneSpiralCoordinateTop = new ArrayList<>();
            oneSpiralCoordinateTop.add(topCoordinateInSpiralX + q * 20);
            oneSpiralCoordinateTop.add(topCoordinateInSpiralY);

            ArrayList<Float> oneSpiralCoordinateTopCopy = new ArrayList<>();
            oneSpiralCoordinateTopCopy.addAll(oneSpiralCoordinateTop);
            spiralCoordinates.add(oneSpiralCoordinateTopCopy);
            oneSpiralCoordinateTop.clear();
        }
        //Toast.makeText(getContext(), spiralCoordinates.size() + " ", Toast.LENGTH_SHORT).show();
        Collections.reverse(spiralCoordinates);
    }

    public void getCoordinatesRotateRegular() {
        //spiral are impacted a lot by initialR.
        //circles locations remain whether landscape or portrait
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //initialRForLocation is 0 when app launches, causing spiral becomes a dot.
            //when first launch, I treat windowHeight as initial R, which is just screen width later
            //(有差距，因为有状态栏，so window height is slightly smaller than screen width)
            if (coordinatesAndSizesUpdated == FALSE) {
                initialR = windowHeight / 10;
            } else {
                initialR = initialRForLocation;
            }
            //Log.d("initialR", " " + initialR);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initialR = screenWidth / 10;
        }
        //for (float t = -30; t < 30; t += 0.02f) {
        for (float t = -18; t < 17.5; t += 0.02f) {
            //Equiangular spiral function：
            //x = p * cosA, y = p * sinA, where p = N * e^(B * cotC)
            //When C = PI/2, graph is a circle, when C = 0, graph is a straight line
            float x = centerX + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t));
            float y = centerY + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t));

            //intendSize is the new size compare to original (original has width of 0.1 * screenWidth)

            float angle = theta / 100;
            float xNew = (x - centerX) * (float) (Math.cos(angle)) - (y - centerY) * (float) (Math.sin(angle)) + centerX;
            float yNew = (y - centerY) * (float) (Math.cos(angle)) + (x - centerX) * (float) (Math.sin(angle)) + centerY;

            ArrayList<Float> oneSpiralCoordinate = new ArrayList<>();
            oneSpiralCoordinate.add(xNew);
            oneSpiralCoordinate.add(yNew);

            ArrayList<Float> oneSpiralCoordinateCopy = new ArrayList<>();
            oneSpiralCoordinateCopy.addAll(oneSpiralCoordinate);
            //Log.d("x y coordinate", oneSpiralCoordinate.get(0) + "<- x, y -> " + oneSpiralCoordinate.get(1));
            spiralCoordinates.add(oneSpiralCoordinateCopy);
            //Log.d("x y coordinate", "right after adding, spiralCoordinates are " + spiralCoordinates);
            oneSpiralCoordinate.clear();
        }

        topCoordinateInSpiralX = spiralCoordinates.get(spiralCoordinates.size()-1).get(0);
        topCoordinateInSpiralY = spiralCoordinates.get(spiralCoordinates.size()-1).get(1);

        float secondTopCoordinateInSpiralX = spiralCoordinates.get(spiralCoordinates.size()-2).get(0);
        float secondTopCoordinateInSpiralY = spiralCoordinates.get(spiralCoordinates.size()-2).get(1);

        //when q += 12f, top lines circles next to each other the whole time\
        //must change the same time as getCoordinates()
        for (float q = 0; q < 20; q += 1) {

            ArrayList<Float> oneSpiralCoordinateTop = new ArrayList<>();

            float xDirection = topCoordinateInSpiralX - secondTopCoordinateInSpiralX;
            float yDirection = topCoordinateInSpiralY - secondTopCoordinateInSpiralY;

            float step = q * 30;
            oneSpiralCoordinateTop.add(xDirection / Math.abs(xDirection) * step + secondTopCoordinateInSpiralX);
            oneSpiralCoordinateTop.add(yDirection / Math.abs(yDirection) * step + secondTopCoordinateInSpiralY);

            ArrayList<Float> oneSpiralCoordinateTopCopy = new ArrayList<>();
            oneSpiralCoordinateTopCopy.addAll(oneSpiralCoordinateTop);
            spiralCoordinates.add(oneSpiralCoordinateTopCopy);
            oneSpiralCoordinateTop.clear();
        }
        //Toast.makeText(getContext(), spiralCoordinates.size() + " ", Toast.LENGTH_SHORT).show();
        Collections.reverse(spiralCoordinates);
    }

    public void getCoordinatesRotateZoom() {
        //spiral are impacted a lot by initialR.
        //circles locations remain whether landscape or portrait
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //initialRForLocation is 0 when app launches, causing spiral becomes a dot.
            //when first launch, I treat windowHeight as initial R, which is just screen width later
            //(有差距，因为有状态栏，so window height is slightly smaller than screen width)
            if (coordinatesAndSizesUpdated == FALSE) {
                initialR = windowHeight / 10;
            } else {
                initialR = initialRForLocation;
            }
            //Log.d("initialR", " " + initialR);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initialR = screenWidth / 10;
        }
        //for (float t = -30; t < 30; t += 0.02f) {
        for (float t = -18; t < 17.5; t += 0.02f) {
            //Equiangular spiral function：
            //x = p * cosA, y = p * sinA, where p = N * e^(B * cotC)
            //When C = PI/2, graph is a circle, when C = 0, graph is a straight line
            float x = centerX + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t));
            float y = centerY + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t));

            //intendSize is the new size compare to original (original has width of 0.1 * screenWidth)

            float angle = theta / 50;
            float xNew = (x - centerX) * (float) (Math.cos(angle)) - (y - centerY) * (float) (Math.sin(angle)) + centerX;
            float yNew = (y - centerY) * (float) (Math.cos(angle)) + (x - centerX) * (float) (Math.sin(angle)) + centerY;

            ArrayList<Float> oneSpiralCoordinate = new ArrayList<>();
            oneSpiralCoordinate.add(xNew);
            oneSpiralCoordinate.add(yNew);

            ArrayList<Float> oneSpiralCoordinateCopy = new ArrayList<>();
            oneSpiralCoordinateCopy.addAll(oneSpiralCoordinate);
            //Log.d("x y coordinate", oneSpiralCoordinate.get(0) + "<- x, y -> " + oneSpiralCoordinate.get(1));
            spiralCoordinates.add(oneSpiralCoordinateCopy);
            //Log.d("x y coordinate", "right after adding, spiralCoordinates are " + spiralCoordinates);
            oneSpiralCoordinate.clear();
        }

        topCoordinateInSpiralX = spiralCoordinates.get(spiralCoordinates.size()-1).get(0);
        topCoordinateInSpiralY = spiralCoordinates.get(spiralCoordinates.size()-1).get(1);

        //when q += 12f, top lines circles next to each other the whole time\
        //must change the same time as getCoordinates()
        for (float q = 0; q < 20; q += 1) {

            ArrayList<Float> oneSpiralCoordinateTop = new ArrayList<>();

            float xDirection = topCoordinateInSpiralX - centerX;
            float yDirection = topCoordinateInSpiralY - centerY;

            float step = q * 10;
            oneSpiralCoordinateTop.add((xDirection) / Math.abs(xDirection) * step + topCoordinateInSpiralX);
            oneSpiralCoordinateTop.add((yDirection) / Math.abs(yDirection) * step + topCoordinateInSpiralY);

            ArrayList<Float> oneSpiralCoordinateTopCopy = new ArrayList<>();
            oneSpiralCoordinateTopCopy.addAll(oneSpiralCoordinateTop);
            spiralCoordinates.add(oneSpiralCoordinateTopCopy);
            oneSpiralCoordinateTop.clear();
        }
        //Toast.makeText(getContext(), spiralCoordinates.size() + " ", Toast.LENGTH_SHORT).show();
        Collections.reverse(spiralCoordinates);
    }

    public void getCoordinatesThreeD() {
        //spiral are impacted a lot by initialR.
        //circles locations remain whether landscape or portrait
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //initialRForLocation is 0 when app launches, causing spiral becomes a dot.
            //when first launch, I treat windowHeight as initial R, which is just screen width later
            //(有差距，因为有状态栏，so window height is slightly smaller than screen width)
            if (coordinatesAndSizesUpdated == FALSE) {
                initialR = windowHeight / 10;
            } else {
                initialR = initialRForLocation;
            }
            //Log.d("initialR", " " + initialR);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initialR = screenWidth / 10;
        }
        //for (float t = -30; t < 30; t += 0.02f) {
        for (float t = -18; t < 17.5; t += 0.02f) {
            //Equiangular spiral function：
            //x = p * cosA, y = p * sinA, where p = N * e^(B * cotC)
            //When C = PI/2, graph is a circle, when C = 0, graph is a straight line
            float x = centerX + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t));
            float y = centerY + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t));

            //intendSize is the new size compare to original (original has width of 0.1 * screenWidth)

            float angle = theta / 500;
            x = (x - centerX) * (float) (Math.cos(angle)) - (y - centerY) * (float) (Math.sin(angle)) + centerX;
            y = (y - centerY) * (float) (Math.cos(angle)) + (x - centerX) * (float) (Math.sin(angle)) + centerY;

            ArrayList<Float> oneSpiralCoordinate = new ArrayList<>();
            oneSpiralCoordinate.add(x);
            oneSpiralCoordinate.add(y);

            ArrayList<Float> oneSpiralCoordinateCopy = new ArrayList<>();
            oneSpiralCoordinateCopy.addAll(oneSpiralCoordinate);
            //Log.d("x y coordinate", oneSpiralCoordinate.get(0) + "<- x, y -> " + oneSpiralCoordinate.get(1));
            spiralCoordinates.add(oneSpiralCoordinateCopy);
            //Log.d("x y coordinate", "right after adding, spiralCoordinates are " + spiralCoordinates);
            oneSpiralCoordinate.clear();
        }

        topCoordinateInSpiralX = spiralCoordinates.get(spiralCoordinates.size()-1).get(0);
        topCoordinateInSpiralY = spiralCoordinates.get(spiralCoordinates.size()-1).get(1);

        //when q += 12f, top lines circles next to each other the whole time\
        //must change the same time as getCoordinates()
        for (float q = 0; q < 20; q += 1) {

            ArrayList<Float> oneSpiralCoordinateTop = new ArrayList<>();
            oneSpiralCoordinateTop.add(topCoordinateInSpiralX + q * 20);
            oneSpiralCoordinateTop.add(topCoordinateInSpiralY);

            ArrayList<Float> oneSpiralCoordinateTopCopy = new ArrayList<>();
            oneSpiralCoordinateTopCopy.addAll(oneSpiralCoordinateTop);
            spiralCoordinates.add(oneSpiralCoordinateTopCopy);
            oneSpiralCoordinateTop.clear();
        }
        //Toast.makeText(getContext(), spiralCoordinates.size() + " ", Toast.LENGTH_SHORT).show();
        Collections.reverse(spiralCoordinates);
    }

    public void getSizes() {
        float pi = (float) Math.PI;

        //circles sizes remain whether landscape or portrait
        initialR = screenWidth / 10;
        //Toast.makeText(getContext(), "getSizes called, sizes.length is " + sizes.size(), Toast.LENGTH_SHORT).show();

        float newSize = 0;
        //for (float t = -30; t < 30; t += 0.02f) {
        for (float t = -18; t < 17.5; t += 0.02f) {
            float x = centerX + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t));
            float y = centerY + initialR * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t));

            float t2 = t - 2 * pi;
            float x2 = centerX + initialR * (float) (Math.exp(t2 * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t2));
            float y2 = centerY + initialR * (float) (Math.exp(t2 * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t2));
            newSize = (float) (Math.sqrt(Math.pow(Math.abs(x - x2), 2) + Math.pow(Math.abs(y - y2), 2)));
            newSize = (newSize / screenWidth * 1.3f);
            sizes.add(newSize);
        }
        //Log.d("sizes 1400 are ", " " + sizes.get(1400));
        //Log.d("sizes size is ", " " + sizes.size());
        //Log.d("initialR is ", " " + initialR);

        int sizesSizeInSpiralPart = sizes.size();
        largestSizeInSpiral = sizes.get(sizesSizeInSpiralPart - 1);

        //when q += 12f, top lines circles next to each other the whole time
        //must change the same time as getCoordinates()
        for (float q = 0; q < 20; q += 1) {
            sizes.add(largestSizeInSpiral);
//            if (sizes.size() >= spiralCoordinates.size()) {
//                break;
//            }
        }
        Collections.reverse(sizes);
    }

    public void drawSpiral(Canvas c) {
        float e = (float) (Math.E);
        float a = screenWidth / 10;
        //draw spiral
        spiralLine.reset();
        spiralLine.moveTo(centerX, centerY);
        //radius of the circle in the middle
        //c.drawCircle(centerX, centerY, initialR, spiralPaint);
        Log.d("theta ", "is " + theta);
        for (float t = -18; t < 17.5; t += 0.02f) {
            //Equiangular spiral function：
            //x = p * cosA, y = p * sinA, where p = N * e^(B * cotC)
            //When C = PI/2, graph is a circle, when C = 0, graph is a straight line
//            float x = centerX + a * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t));
//            float y = centerY + a * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t));
            float x = centerX + a * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.cos(t));
            float y = centerY + a * (float) (Math.exp(t * 1 / (Math.tan(47 * Math.PI / 100)))) * (float) (Math.sin(t));

            // rotates about (0, 0)
            //float xNew = x * (float)(Math.cos(theta)) + y * (float)(Math.sin(theta));
            //float yNew = y * (float)(Math.cos(theta)) - x * (float)(Math.sin(theta));

            float angle = theta / 500;
            float xNew = (x - centerX) * (float) (Math.cos(angle)) - (y - centerY) * (float) (Math.sin(angle)) + centerX;
            float yNew = (y - centerY) * (float) (Math.cos(angle)) + (x - centerX) * (float) (Math.sin(angle)) + centerY;

            //spiral doesn't rotates
            //spiralLine.lineTo(x, y);

            //spiral rotates
            spiralLine.lineTo(xNew, yNew);
        }
        //draw the spiral ****************************************
        c.drawPath(spiralLine, spiralPaint);
        //Toast.makeText(getContext(), count + " ", Toast.LENGTH_SHORT).show();
    }

}
