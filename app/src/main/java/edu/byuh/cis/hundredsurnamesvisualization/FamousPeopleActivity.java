package edu.byuh.cis.hundredsurnamesvisualization;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

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

        TextView textView = findViewById(R.id.title);
        textView.setText(nameList.toString());
        textView.setTextColor(Color.WHITE);
        textView.setTextScaleX(1.5f);


        Button buttonLeft = findViewById(R.id.buttonLeft);
        buttonLeft.setText("<");
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personID=(nameList.size()+personID-1)%nameList.size();
                //personID--;
                String person=nameList.get(personID);
                webView.loadUrl("https://en.wikipedia.org/wiki/"+person);
            }
        });

        Button buttonRight = findViewById(R.id.buttonRight);
        buttonRight.setText(">");
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personID=(personID+1)%nameList.size();
                //personID++;
                String person=nameList.get(personID);
                webView.loadUrl("https://en.wikipedia.org/wiki/"+person);
            }
        });



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
