package co.tagtalk.winemate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class NewsFeedWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_web_view);

        Intent intent= getIntent();
        String url  = intent.getStringExtra("contentUrl");
        WebView newsFeedWebView = (WebView) this.findViewById(R.id.news_feed_web_view);

        if (url != null && url.length() != 0) {
            newsFeedWebView.loadUrl(url);
        }
    }
}
