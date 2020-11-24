package edu.byuh.cis.hundredsurnamesvisualization;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FamousPeopleActivity extends AppCompatActivity {


    private WebView webView;
    DataHolder dataHolder;
    private int personID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.famous_people_view);
        final DataHolder dataHolder = new DataHolder(this);
        personID=0;
        webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        final String person=dataHolder.famousPeopleList.get(personID);
        webView.loadUrl("https://en.wikipedia.org/wiki/"+person);
//        LinearLayout.LayoutParams nice = new LinearLayout.LayoutParams
//                (LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
//
//
//        TextView tv=new TextView(this);
//        tv.setText(personID);
//
//        Button prevButton=new Button(this);
//        Button nextButton=new Button(this);
//
//        prevButton.setText("<<");
//        nextButton.setText(">>");
//
//        LinearLayout lnl=new LinearLayout(this);
//        lnl.setOrientation(LinearLayout.VERTICAL);
//        //lnl.addView(webView);
//        lnl.addView(tv);
//        lnl.addView(prevButton);
//        lnl.addView(nextButton);
//
//        setContentView(lnl);


    }
    @Override
    public boolean onTouchEvent(MotionEvent m){
        if(m.getAction()==MotionEvent.ACTION_DOWN){
            personID+=1;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }


}
