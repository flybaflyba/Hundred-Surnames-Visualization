package edu.byuh.cis.hundredsurnamesvisualization;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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

        TextView textView = findViewById(R.id.title);
        textView.setText(person);

        Button buttonLeft = findViewById(R.id.buttonLeft);
        buttonLeft.setText(">>");
        //buttonLeft.setOnClickListener();



            }
//    @Override
//    public boolean onTouchEvent(MotionEvent m){
//        if(m.getAction()==MotionEvent.ACTION_DOWN){
//            personID+=1;
//        }
//        return true;
//    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }


}
