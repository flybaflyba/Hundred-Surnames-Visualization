package edu.byuh.cis.hundredsurnamesvisualization;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

//implements TextToSpeech.OnInitListener
public class SpiralView extends View {

    private Paint circlePaint, redPaint, spiralPaint;
    private float screenWidth, screenHeight;
    public float theta;
    private Path spiralLine;
    private Boolean loadedImages;
    private  float centerX;
    private float centerY;
    private float initialR;
    private boolean sliderMoving;
    private static ArrayList<Member> memberObjects; // more OO be more object oriented
    private ArrayList<ArrayList<Float>> onScreenMembers;
    private ArrayList<Float> oneOnScreenMember;
    private ArrayList<ArrayList<Float>> spiralCoordinates;
    private ArrayList<Float> sizes;
    private ArrayList<String> allMemberLinks;
    private ArrayList<String> surnameCharactersSimplified;
    public ArrayList<String> allKeys;
    private int eachIndex;
    private Matrix currentMemberMatrix;
    private float topCoordinateInSpiralX;
    private float topCoordinateInSpiralY;
    private float largestSizeInSpiral;
    private boolean coordinatesAndSizesUpdated;
    private boolean orientationJustChanged;
    public boolean touchDownOnScreenMemberView;
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
    private String oneMemberInfo;
    private static ArrayList<Integer> allObjectInfoFileIds;
    private Boolean show_label;
    private String selectedKey;
    private Integer realEachIndex;
    private SingleMemberImage singleMemberImageView;
    private int staticCoordinatesGet = 0;
    private AlertDialog singleMemberDialog;
    private Paint noImageCirclePaint;
    private String MemberUrl;
    private int numOfMembers;
    private String character_option = "simplified";
    private String pinyin_option = "simplified";
    private Paint thisMemberPaint;
    private TextToSpeech textToSpeech;
    public static final String INDEX="INDEX";
    private ColorTheme colorTheme;
    //public String color,color2,color3,color4;



    public SpiralView(Context context, int num) {
        super(context);
        colorTheme=new ColorTheme();

//        textToSpeech = new TextToSpeech(context, this);
        numOfMembers = num;
        circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor(colorTheme.c2));
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setTextSize(35);
        redPaint = new Paint();
        //redPaint.setColor(Color.RED);
        redPaint.setColor(Color.parseColor(colorTheme.c1));
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setTextSize(60);
        spiralPaint = new Paint();
        spiralPaint.setColor(Color.parseColor(colorTheme.c3));
        //spiralPaint.setColor(Color.RED);
        spiralPaint.setStyle(Paint.Style.STROKE);
        spiralPaint.setStrokeWidth(5);
        spiralLine = new Path();
        loadedImages = false;
        spiralCoordinates = new ArrayList<>();
        sizes = new ArrayList<>();
        onScreenMembers = new ArrayList<>();
        oneOnScreenMember = new ArrayList<>();
        allMemberLinks = new ArrayList<>();
        surnameCharactersSimplified = new ArrayList<>();
        allKeys = new ArrayList<>();
        theta = numOfMembers * 30 / 2;
        currentMemberMatrix = new Matrix();
        coordinatesAndSizesUpdated = FALSE;
        orientationJustChanged = FALSE;
        movingCoordinatesLastTime = new ArrayList<>();
        selectedKey = "";

        noImageCirclePaint = new Paint();
        noImageCirclePaint.setColor(Color.parseColor(colorTheme.c1));
        noImageCirclePaint.setStyle(Paint.Style.FILL);

        //character paint
        thisMemberPaint = new Paint();
        thisMemberPaint.setColor(Color.parseColor(colorTheme.c4));
        thisMemberPaint.setStyle(Paint.Style.FILL);
        thisMemberPaint.setTextAlign(Paint.Align.CENTER);
//        AssetManager assetManager = getContext().getAssets();
//        Typeface chops = Typeface.createFromAsset(assetManager, "fonts/TengXiangFanXiaoGeJianFan.ttf");
//        thisMemberPaint.setTypeface(chops);

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
            InputStream allMemberLinksFile =  getContext().getResources().openRawResource(R.raw.all_objects_links);
            if (allMemberLinksFile != null)
            {
                InputStreamReader ir = new InputStreamReader(allMemberLinksFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                int atThisLine = 0;
                while (( line = br.readLine()) != null) {
                    allMemberLinks.add(line+"\n");
                    if (atThisLine < memberObjects.size()) {
                        memberObjects.get(atThisLine).setLink(line+"\n");
                        atThisLine ++;
                    }
                }
                allMemberLinksFile.close();
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

    public void readOneInfoFile(int id) {
        try {
            InputStream allMemberInfoFile =  this.getResources().openRawResource(id);
            if (allMemberInfoFile != null)
            {
                InputStreamReader ir = new InputStreamReader(allMemberInfoFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                while (( line = br.readLine()) != null) {
                    oneMemberInfo = oneMemberInfo + line + "\n";
                }
                allMemberInfoFile.close();
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
            touchDownOnScreenMemberView = TRUE;
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

            boolean thetaMaxReached = theta >= numOfMembers * 30;
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
            touchDownOnScreenMemberView = FALSE;
            float x = m.getX();
            float y = m.getY();

            if (y < 9 * screenHeight / 10 && period < 100) {
                boolean singleMemberViewOpened = false;

                Collections.reverse(onScreenMembers);
                for (ArrayList<Float> eachOnScreenMember : onScreenMembers) {
                    //remember each Float in inner class is a object, when convert it to int you need to use some method.
                    eachIndex = (int)(eachOnScreenMember.get(0).floatValue());
                    float eachXCoordinate = eachOnScreenMember.get(1);
                    float eachYCoordinate = eachOnScreenMember.get(2);
                    float eachSize = eachOnScreenMember.get(3);
                    float distanceToCurrentCoordinate = (float) (Math.sqrt(Math.pow(Math.abs(x - eachXCoordinate), 2) + Math.pow(Math.abs(y - eachYCoordinate), 2)));

                    if (distanceToCurrentCoordinate < eachSize) {
                        if (singleMemberViewOpened == false) {
                            if (eachIndex <= numOfMembers - 1) { // it's useless here, it;'s for the older version of the app, this number should be equal the the number of objects - 1, so that all circles can b bring up detail dialog.
                                singleMemberViewOpened = true;
                                //Log.d("eachIndex is ", eachIndex + " when click on circle");
                                singleMemberDialog();
                            } else {
                                //no link
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Nothing Here");
                                builder.setIcon(R.mipmap.ic_launcher_round);
                                builder.setCancelable(true);
                                final AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }
                }
                Collections.reverse(onScreenMembers);
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    public void singleMemberDialog() {

        final WebView wv = new WebView(getContext());

        LinearLayout.LayoutParams nice = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
        LinearLayout.LayoutParams niceTwo = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
        LinearLayout.LayoutParams niceThree = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 3);
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

//        if (eachIndex == 0) {
//            singleMemberImageView = new SingleMemberImage(getContext(), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex + 1));
//        } else if (eachIndex == numOfMembers - 1){
//            singleMemberImageView = new SingleMemberImage(getContext(), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex - 1), allLargeImageIds.get(eachIndex)); // no next event
//        } else {
//            singleMemberImageView = new SingleMemberImage(getContext(), allLargeImageIds.get(eachIndex), allLargeImageIds.get(eachIndex - 1), allLargeImageIds.get(eachIndex + 1));
//        }

        if(character_option.equals("simplified")){
            if (eachIndex == 0) {
                singleMemberImageView = new SingleMemberImage(getContext(), memberObjects.get(eachIndex).simplified, memberObjects.get(eachIndex).simplified, memberObjects.get(eachIndex+1).simplified);
            } else if (eachIndex == numOfMembers - 1){
                singleMemberImageView = new SingleMemberImage(getContext(), memberObjects.get(eachIndex).simplified, memberObjects.get(eachIndex-1).simplified, memberObjects.get(eachIndex).simplified); // no next event
            } else {
                singleMemberImageView = new SingleMemberImage(getContext(), memberObjects.get(eachIndex).simplified, memberObjects.get(eachIndex-1).simplified, memberObjects.get(eachIndex+1).simplified);
            }
        } else if (character_option.equals("traditional")) {
            if (eachIndex == 0) {
                singleMemberImageView = new SingleMemberImage(getContext(), memberObjects.get(eachIndex).traditional, memberObjects.get(eachIndex).traditional, memberObjects.get(eachIndex+1).traditional);
            } else if (eachIndex == numOfMembers - 1){
                singleMemberImageView = new SingleMemberImage(getContext(), memberObjects.get(eachIndex).traditional, memberObjects.get(eachIndex-1).traditional, memberObjects.get(eachIndex).traditional); // no next event
            } else {
                singleMemberImageView = new SingleMemberImage(getContext(), memberObjects.get(eachIndex).traditional, memberObjects.get(eachIndex-1).traditional, memberObjects.get(eachIndex+1).traditional);
            }
        }



        singleMemberImageView.setPadding(0,0,0,0);

        // milestone dates
        oneMemberInfo = "";
        readOneInfoFile(allObjectInfoFileIds.get(eachIndex));
        // TODO
        oneMemberInfo = (eachIndex+1) + "";



        final TextView singleMemberTextView = new TextView(getContext());
        singleMemberTextView.setText(oneMemberInfo);
        //singleMemberTextView.setBackgroundColor(Color.BLUE);
        singleMemberTextView.setGravity(Gravity.CENTER);
        singleMemberTextView.setLayoutParams(nice);
        singleMemberTextView.setTextSize(15);

        ScrollView sv = new ScrollView(getContext());
        //sv.setPadding(100,100,100,100);
        sv.addView(singleMemberTextView);

        // here is where we get memberUrl, to avoid the eachIndex change error
        realEachIndex = eachIndex; // we do this because each index is changing for some reason later...
        MemberUrl = memberObjects.get(realEachIndex).link;

        final TextView singleMemberDialogTitleView = new TextView(getContext());
//        singleMemberDialogTitleView.setText(surnameCharactersSimplified.get(realEachIndex));
        String title = "N/A";
        if (sliderMoving == false && show_label) {
            if(pinyin_option.equals("simplified")){
                title = memberObjects.get(realEachIndex).pinyin;
            } else if(pinyin_option.equals("cantonese")){
                title = memberObjects.get(realEachIndex).pinyinCantonese;
            } else {
                title = "N/A";
            }
        }
        singleMemberDialogTitleView.setText(title);
        singleMemberDialogTitleView.setTextSize(20);
        singleMemberDialogTitleView.setPadding(0,20,0,0);
        singleMemberDialogTitleView.setTextColor(Color.BLACK);
        singleMemberDialogTitleView.setGravity(Gravity.CENTER);

        final long[] timeStamp = new long[1];
        timeStamp[0] = 0;
        // view last or next member buttons
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
                    if (realEachIndex < numOfMembers - 1) {
                        if (System.currentTimeMillis() - timeStamp[0] > 1550) {
                            realEachIndex = realEachIndex + 1;
                            singleMemberImageView.moveImage("left");
                            String nextMemberId;
                            if(character_option.equals("simplified")){
                                if (realEachIndex + 1 > numOfMembers - 1) {
                                    nextMemberId = memberObjects.get(realEachIndex).simplified;
                                } else {
                                    nextMemberId = memberObjects.get(realEachIndex + 1).simplified;
                                }
                                singleMemberImageView.updateThreeMembersBitmapIds(memberObjects.get(realEachIndex).simplified, memberObjects.get(realEachIndex-1).simplified, nextMemberId);
                            } else if(character_option.equals("traditional")){
                                if (realEachIndex + 1 > numOfMembers - 1) {
                                    nextMemberId = memberObjects.get(realEachIndex).traditional;
                                } else {
                                    nextMemberId = memberObjects.get(realEachIndex + 1).traditional;
                                }
                                singleMemberImageView.updateThreeMembersBitmapIds(memberObjects.get(realEachIndex).traditional, memberObjects.get(realEachIndex-1).traditional, nextMemberId);
                            }


                            MemberUrl = memberObjects.get(realEachIndex).link;
//                            singleMemberDialogTitleView.setText(surnameCharactersSimplified.get(realEachIndex));
                            String title = "N/A";
                            if (sliderMoving == false && show_label) {
                                if(pinyin_option.equals("simplified")){
                                    title = memberObjects.get(realEachIndex).pinyin;
                                } else if(pinyin_option.equals("cantonese")){
                                    title = memberObjects.get(realEachIndex).pinyinCantonese;
                                } else {
                                    title = "N/A";
                                }
                            }
                            singleMemberDialogTitleView.setText(title);

                            String url = "";
                            url = "https://en.wikipedia.org/wiki/" + memberObjects.get(realEachIndex).pinyin + "_(surname)";
                            wv.loadUrl(url);

                            oneMemberInfo = "";
                            readOneInfoFile(allObjectInfoFileIds.get(realEachIndex));
                            // TODO
                            oneMemberInfo = (realEachIndex+1) + "";
                            singleMemberTextView.setText(oneMemberInfo);
                            singleMemberTextView.scrollTo(0,0);
                            timeStamp[0] = System.currentTimeMillis();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    wv.scrollTo(0, 500);
                                }
                            },900); // 延时

                        }
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.last_is_the_last), Toast.LENGTH_SHORT).show();
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
                            String lastMemberId;

                            if(character_option.equals("simplified")){
                                if (realEachIndex - 1 < 0) {
                                    lastMemberId = memberObjects.get(realEachIndex).simplified;
                                } else {
                                    lastMemberId = memberObjects.get(realEachIndex - 1).simplified;
                                }
                                singleMemberImageView.updateThreeMembersBitmapIds(memberObjects.get(realEachIndex).simplified, lastMemberId, memberObjects.get(realEachIndex + 1).simplified);
                            } else if(character_option.equals("traditional")){
                                if (realEachIndex - 1 < 0) {
                                    lastMemberId = memberObjects.get(realEachIndex).traditional;
                                } else {
                                    lastMemberId = memberObjects.get(realEachIndex - 1).traditional;
                                }
                                singleMemberImageView.updateThreeMembersBitmapIds(memberObjects.get(realEachIndex).traditional, lastMemberId, memberObjects.get(realEachIndex + 1).traditional);
                            }



                            MemberUrl = memberObjects.get(realEachIndex).link;
//                            singleMemberDialogTitleView.setText(surnameCharactersSimplified.get(realEachIndex));
                            String title = "N/A";
                            if (sliderMoving == false && show_label) {
                                if(pinyin_option.equals("simplified")){
                                    title = memberObjects.get(realEachIndex).pinyin;
                                } else if(pinyin_option.equals("cantonese")){
                                    title = memberObjects.get(realEachIndex).pinyinCantonese;
                                } else {
                                    title = "N/A";
                                }
                            }
                            singleMemberDialogTitleView.setText(title);

                            String url = "";
                            url = "https://en.wikipedia.org/wiki/" + memberObjects.get(realEachIndex).pinyin + "_(surname)";
                            wv.loadUrl(url);

                            oneMemberInfo = "";
                            readOneInfoFile(allObjectInfoFileIds.get(realEachIndex));
                            // TODO
                            oneMemberInfo = (realEachIndex+1) + "";
                            singleMemberTextView.setText(oneMemberInfo);
                            singleMemberTextView.scrollTo(0,0);
                            timeStamp[0] = System.currentTimeMillis();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    wv.scrollTo(0, 500);
                                }
                            },900); // 延时
                        }
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.first_is_the_first), Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        singleMemberImageView.setLayoutParams(nice);
        left.setLayoutParams(niceFour);
        right.setLayoutParams(niceFour);
        lnlH.setLayoutParams(niceFour);

        // the left button is actually on the right and the right button is actually on the left.
        lnlH.addView(right); // lnlH.addView(left);
        lnlH.addView(singleMemberImageView);
        lnlH.addView(left); // lnlH.addView(right);

        singleMemberTextView.setLayoutParams(niceFour);

        LinearLayout lnlWebView = new LinearLayout(getContext());
        lnlWebView.setOrientation(LinearLayout.VERTICAL);
        lnlWebView.setLayoutParams(nice);
        lnlWebView.setMinimumHeight((int)(Math.min(screenWidth, screenHeight) * 0.7));

        wv.setWebViewClient(new WebViewClient());
        String url = "";
        url = "https://en.wikipedia.org/wiki/" + memberObjects.get(realEachIndex).pinyin + "_(surname)";
        wv.loadUrl(url);
        WebSettings settings = wv.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.scrollTo(0, 500);


        wv.setLayoutParams(nice);
        lnlWebView.addView(wv);

//        setContentView(R.layout.activity_main);
//        LinearLayout lnlSinglePage = findViewById(R.id.sliderLabel);
//
//        LinearLayout lnlTitle = findViewById(R.id.LinearLayoutTitle);
//        LinearLayout lnlBig = findViewById(R.id.LinearLayoutBig);
//        LinearLayout lnlWeb = findViewById(R.id.LinearLayoutWeb);


//        lnlTitle.addView(singleMemberDialogTitleView);
//        lnlSinglePage.addView(lnlTitle);
//        lnlSinglePage.addView(lnlBig);
//        lnlSinglePage.addView(lnlWeb);

        lnl.addView(singleMemberDialogTitleView);
        lnl.addView(lnlH);
//        lnl.addView(sv);

        lnl.addView(lnlWebView);

//        ((ViewGroup)singleMemberTextView.getParent()).removeView(singleMemberTextView);
//        lnl.addView(singleMemberTextView);
//        //singleMemberTextView.setBackgroundColor(Color.RED);
//        singleMemberTextView.setHeight((int)(Math.min(screenWidth, screenHeight) * 0.7));
//        singleMemberTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        // singleMemberDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //builder.setTitle(allMemberInfo.get(realEachIndex*3));
        builder.setView(lnl);
//        builder.setView(lnlSinglePage);
        builder.setCancelable(true);

//        getResources().getString(R.string.return_button) // this is the return button text
        builder.setPositiveButton("Famous", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("click mahalo ", "mahalo");
            }
        });
        builder.setNegativeButton("Audio", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //set onclick method for this button below
                Log.d("click dismiss ", "dismiss");

            }
        });
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //set onclick method for this button below
                Log.d("click aloha ", "aloha");

            }
        });

        singleMemberDialog = builder.create();
//        singleMemberDialog.setContentView(R.layout.famous_people_view);
        singleMemberDialog.show();

        //singleMemberDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = singleMemberDialog.getWindow().getAttributes();
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
        singleMemberDialog.getWindow().setAttributes(params);
        singleMemberDialog.show();

        // this overrides the buttons actions above
        singleMemberDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click ", "positive");
                Intent famousPeopleScreen = new Intent(getContext(), FamousPeopleActivity.class);
                int k=realEachIndex;
                famousPeopleScreen.putExtra(INDEX,k);
                getContext().startActivity(famousPeopleScreen);

            }
        });
        singleMemberDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click ", "negative");
                String audioFileName = "" + (realEachIndex + 1);
                while (audioFileName.length() < 3) {
                    audioFileName = "0" + audioFileName;
                }
                audioFileName = audioFileName + "_" + memberObjects.get(realEachIndex).pinyin;
                audioFileName = audioFileName.substring(0,audioFileName.length()-1); // there is an extra space in the end.
                Log.d("audioFileName ", audioFileName);
                Log.d("audioFileName length ", audioFileName.length() + "");

                try {
                    AssetFileDescriptor afd = getContext().getAssets().openFd("audios/" + audioFileName + ".mp3");
                    MediaPlayer mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (Exception ex) {
//                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "No Audio", Toast.LENGTH_LONG).show();
                }

                // tried tts again, not working with chinese...
//                textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
//                    @Override
//                    public void onInit(int status) {
//                        if(status == TextToSpeech.SUCCESS){
//                            Log.d("tts ", "success");
//                            int result = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
//                            if(result != textToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE){
//                                Toast.makeText(getContext(), "TTS暂时不支持这种语音的朗读！", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Log.d("tts ", "fail");
//                        }
//                        Log.d("status ", status + "");
//                        Toast.makeText(getContext(), status + " status", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        textToSpeech.speak("how are you, I'm find, thank you.", TextToSpeech.QUEUE_ADD, null);
//                    }
//                },1000); // 延时


            }
        });
        singleMemberDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click ", "neutral");
                singleMemberDialog.dismiss();

            }
        });


        Button btnPositive = singleMemberDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = singleMemberDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button btnNeutral = singleMemberDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        btnNeutral.setLayoutParams(layoutParams);



    }

    public void orientationJustChanged(boolean b) {
        orientationJustChanged = b;
        if (singleMemberImageView != null) { // the rotate phone without clicking on a member
            singleMemberImageView.orientationJustChanged(b);

            // reset single member dialog size according to screen size once orientation change happens
            WindowManager.LayoutParams params = singleMemberDialog.getWindow().getAttributes();
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
            singleMemberDialog.getWindow().setAttributes(params);
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenWidth = c.getWidth() / 2;
            screenHeight = c.getHeight();
            centerX = screenWidth;
//            centerX = screenWidth / 2 + 3 * screenWidth / 16;
            centerY = screenHeight / 2;
            ultimateScreenWidth = Math.min(windowHeight, windowWidth);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            screenWidth = c.getWidth();
            screenHeight = c.getHeight();
            centerX = screenWidth / 2;
            centerY = screenHeight / 2;
            ultimateScreenWidth = screenWidth;
            //Log.d("PORTRAIT ", "|||||||||||||||||||||||||||||" + screenWidth);
        }
        //Log.d("CENTER X ", centerX + " ");
        //Log.d("CENTER Y ", centerY + " ");
        initialR = screenWidth / 10;
        initialRForLocation = ultimateScreenWidth / 10;

        String spiral_effect = PrefsActivity.getSpiralEffectPref(getContext());
        show_label = PrefsActivity.getShowLabelPref(getContext());
        //Log.d("spiral effect ", spiral_effect + " ");

        if(PrefsActivity.getCirclePref(getContext())) {
            circlePaint.setColor(Color.parseColor(colorTheme.c2));
            invalidate();
        }else{
            circlePaint.setColor(Color.parseColor(colorTheme.c1));
            invalidate();
        }




        character_option = PrefsActivity.getCharacterOptionPref(getContext());
        pinyin_option = PrefsActivity.getPinyinOptionPref(getContext());





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


        //Member View Background color

        c.drawColor(Color.parseColor(colorTheme.c1));

        //we just want to load the images once, we don't have to load it every time when we re-draw. otherwise the program is gonna be so slow
        if (loadedImages == false) {
            loadedImages = true;
            //get the member images in array list

            //when app launches, images are loaded according to screen width
            //when launches landscape, according to window height
            float temp;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                temp = windowHeight;
            } else {
                temp = screenWidth;
            }

//            DataHolder dataHolder = new DataHolder(getContext(), temp/4);
            DataHolder dataHolder = new DataHolder(getContext());
            allLargeImageIds = dataHolder.allLargeImageIds;
            allObjectInfoFileIds = dataHolder.allObjectInfoFileIds;
            memberObjects = dataHolder.memberObjects;

            readLinksFile();
//            readInfoFile();
            surnameCharactersSimplified = dataHolder.surnameCharactersSimplified;

            allKeys = dataHolder.surnamePinyinsSimplified;

        }

        placeAllCircles(c);


    }

    public void drawMemberLabels(Member t, Canvas c) { // more OO

        float newCurrentMemberRadius = t.size * screenWidth / 2;

        Paint thisMemberLabelPaint = new Paint();

        thisMemberLabelPaint.setColor(Color.parseColor(colorTheme.c1));
        thisMemberLabelPaint.setStyle(Paint.Style.FILL);
        thisMemberLabelPaint.setTextSize((int)(newCurrentMemberRadius/3));  //if we are drawing the years as main object , before: ((int)(newCurrentMemberRadius/3))
        thisMemberLabelPaint.setTextAlign(Paint.Align.CENTER);
        thisMemberLabelPaint.setShadowLayer(20,0,0,Color.BLACK);

        if (sliderMoving == false && show_label) {
            if(pinyin_option.equals("simplified")){
                c.drawText(t.pinyin, t.x, t.y - newCurrentMemberRadius + thisMemberLabelPaint.getTextSize(), thisMemberLabelPaint);
            } else if(pinyin_option.equals("cantonese")){
                c.drawText(t.pinyinCantonese, t.x, t.y - newCurrentMemberRadius + thisMemberLabelPaint.getTextSize(), thisMemberLabelPaint);
            } else {
                c.drawText("N/A", t.x, t.y - newCurrentMemberRadius + thisMemberLabelPaint.getTextSize(), thisMemberLabelPaint);
            }
        }

    }

    public void getSelectedKey(String s) {
        selectedKey = s;
    }

    public void actuallyDrawing(Member t, Canvas c, int thisMemberIndex) { // more OO

        float newCurrentMemberRadius = t.size * screenWidth / 2;



//        currentMemberMatrix.setScale(4 * t.size, 4 * t.size);
//        currentMemberMatrix.postTranslate(t.x - t.image.getWidth()  * t.size * 2, t.y - t.image.getHeight() * t.size * 2); // more OO

        // if current member is with selected year then draw a circle frame
        Paint selectedKeyMemberFramePaint = new Paint();
        selectedKeyMemberFramePaint.setColor(Color.parseColor(colorTheme.c2));
        selectedKeyMemberFramePaint.setStyle(Paint.Style.FILL);

        if (allKeys.get(thisMemberIndex).equals(selectedKey)) {
            c.drawCircle(t.x, t.y, newCurrentMemberRadius * 1.1f , selectedKeyMemberFramePaint);

        } else {
            // do nothing
        }


        thisMemberPaint.setTextSize((int)(newCurrentMemberRadius));  //if we are drawing the years as main object , before: ((int)(newCurrentMemberRadius/3))

        Paint.FontMetrics fontMetrics=thisMemberPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=t.y+distance;

//        c.drawBitmap(t.image, currentMemberMatrix, null); // more OO
//        c.drawBitmap(t.image, currentMemberMatrix, null);
        c.drawCircle(t.x, t.y, newCurrentMemberRadius * 1f, circlePaint);

        if(character_option.equals("simplified")) {
            c.drawText(t.simplified, t.x + newCurrentMemberRadius * 0.1f, baseline, thisMemberPaint);
        } else if (character_option.equals("traditional")) {
            c.drawText(t.traditional, t.x + newCurrentMemberRadius * 0.1f, baseline, thisMemberPaint);
        } else {
            c.drawText("N/A", t.x + newCurrentMemberRadius * 0.1f, baseline, thisMemberPaint);
        }



    }

    public void placeAllCircles(Canvas c) {
        //place all circles, and get the index of on screen members
        //this method also call actualDrawing method to draw

        onScreenMembers.clear();
        for (Member t : memberObjects) {
            int thisMemberIndex = memberObjects.indexOf(t);
            float ts = theta - 30 * memberObjects.indexOf(t);
            if (ts > 0 && ts < spiralCoordinates.size() - 1) {

                // set this member's size, x and y once we know it should be on screen
                t.size = sizes.get((int) (ts));
                t.x = spiralCoordinates.get((int) (ts)).get(0);
                t.y = spiralCoordinates.get((int) (ts)).get(1);

                actuallyDrawing(t, c, thisMemberIndex);
                drawMemberLabels(t, c);

                //add all on screen members index to a array list once the slider stopped moving,
                float currentMemberRadius = t.size * screenWidth / 2;
                //inner array list: (this onScreenMember index, x coordinate, y coordinate, member radius at this position)
                oneOnScreenMember.add((float)thisMemberIndex);
                oneOnScreenMember.add(t.x);
                oneOnScreenMember.add(t.y);
                oneOnScreenMember.add(currentMemberRadius);
                //be aware of adding one array list to another array list of array list then clear old one, remember you must copy.
                ArrayList<Float> oneOnScreenMemberCopy = new ArrayList<>();
                oneOnScreenMemberCopy.addAll(oneOnScreenMember);
                onScreenMembers.add(oneOnScreenMemberCopy);
                oneOnScreenMember.clear();
            }
        }
        // we need this line of code, so that in 3 d view, only the front member opens when user clicks
        Collections.reverse(onScreenMembers);
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

//    @Override
//    public void onInit(int status) {
////        if(status == TextToSpeech.SUCCESS){
////            Log.d("tts ", "success");
////            int result = textToSpeech.setLanguage(Locale.CHINA);
////            if(result != textToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE){
////                Toast.makeText(getContext(), "TTS暂时不支持这种语音的朗读！", Toast.LENGTH_SHORT).show();
////            }
////        } else {
////            Log.d("tts ", "fail");
////        }
////        textToSpeech.speak("你好吗", TextToSpeech.QUEUE_ADD, null);
//    }
}
