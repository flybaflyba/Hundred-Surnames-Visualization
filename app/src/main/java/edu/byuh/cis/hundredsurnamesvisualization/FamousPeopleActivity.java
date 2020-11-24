package edu.byuh.cis.hundredsurnamesvisualization;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FamousPeopleActivity extends AppCompatActivity {


    private WebView webView;
    DataHolder dataHolder;




    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.famous_people_view);
        DataHolder dataHolder = new DataHolder(this);

        webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        String person=dataHolder.famousPeopleList.get(0);
        webView.loadUrl("https://en.wikipedia.org/wiki/"+person);

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
