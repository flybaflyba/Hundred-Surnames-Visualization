package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FamousPeopleActivity extends AppCompatActivity {


    private WebView webView;
    private int surnameID;
    private int personID;
    private List<String> nameList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.famous_people_view);
        final DataHolder dataHolder = new DataHolder(this);
        Intent i=getIntent();
        int index=i.getIntExtra(SpiralView.INDEX,0);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ColorTheme.c2))); // set your desired color

        nameList=new ArrayList<String>();
        surnameID=index;
        personID=0;


        String surname=dataHolder.famousPeopleList.get(surnameID);
        String[] personName= surname.split("/");
        Collections.addAll(nameList, personName);
        //System.out.println(nameList);
        final String person=nameList.get(personID);

        webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        //String person=dataHolder.famousPeopleList.get(personID);
        webView.loadUrl("https://en.wikipedia.org/wiki/"+person);

//        TextView textView = findViewById(R.id.title);
//        String text=nameList.toString();
//
//        textView.setText(nameList.toString());
//        textView.setTextColor(Color.parseColor(ColorTheme.c4));
        //textView.setTextScaleX(1.5f);


        Button buttonLeft = findViewById(R.id.buttonPrev);
        buttonLeft.setText(R.string.prev);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personID=(nameList.size()+personID-1)%nameList.size();
                //personID--;
                String person=nameList.get(personID);
                webView.loadUrl("https://en.wikipedia.org/wiki/"+person);
            }
        });

        Button buttonRight = findViewById(R.id.buttonNext);
        buttonRight.setText(R.string.next);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personID=(personID+1)%nameList.size();
                //personID++;
                String person=nameList.get(personID);
                webView.loadUrl("https://en.wikipedia.org/wiki/"+person);
            }
        });

//        buttonLeft.setBackgroundColor(Color.parseColor(ColorTheme.c2));

        LinearLayout buttons = findViewById(R.id.buttons);
        buttons.setBackgroundColor(Color.parseColor(ColorTheme.c2));



    }



//    @Override
//    public void onBackPressed() {
//        if(webView.canGoBack()){
//            webView.goBack();
//        }else{
//            super.onBackPressed();
//        }
//    }


}
