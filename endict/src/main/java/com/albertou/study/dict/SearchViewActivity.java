package com.albertou.study.dict;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.SearchView;


public class SearchViewActivity extends AppCompatActivity {

    private static final String TAG = "SearchViewActivity";

    private SearchView searchView;


    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        webView = (WebView) findViewById(R.id.webView);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(onQueryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "handleIntent: query=" + query);
            queryWords(query);
        }
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            queryWords(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private void queryWords(@NonNull String words) {
        DictHandler.fetch(this, words, new DictCallback() {
            @Override
            public void onCallback(String data) {
                setContent(data);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void setContent(String data) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("<!DOCTYPE HTML><html><body>");
//        stringBuilder.append("<div>").append("<h2>").append(dictionaryResult.word).append("</h2>")
//                .append("<i>").append(dictionaryResult.partOfSpeech).append("</i>").append("</div>");
//
//        if (dictionaryResult.pronunciation != null) {
//            stringBuilder.append("<div>");
//            for (Pronunciation pronunciation : dictionaryResult.pronunciation) {
//                stringBuilder.append("<span>")
//                        .append("<span>").append(pronunciation.name).append("</span>")
//                        .append("<span>[<font face=\"Lucida Sans Unicode\">")
//                        .append(pronunciation.phonics)
//                        .append("</font>]</span>")
//                        .append("<audio controls='controls'><source src='" + pronunciation.audio + "' type='audio/mpeg'>").append("</audio>")
//                        .append("</span>").append("</br>");
//                Log.d(TAG, "setContent: pronunciation.phonics=" + pronunciation.phonics);
//            }
//            stringBuilder.append("</div>");
//
//            stringBuilder.append("<div>");
//            for(Semantics semantics: dictionaryResult.semantics){
//                stringBuilder.append("<h4>").append(semantics.gram).append("</br>").append(semantics.def).append("</h4>");
//                if(semantics.exp!=null) {
//                    stringBuilder.append("<ul>");
//                    for (Example example : semantics.exp) {
//                        stringBuilder.append("<li><strong>").append(example.cf).append("</strong>&nbsp;").append(example.x).append("</li>");
//                    }
//                    stringBuilder.append("</ul>");
//                }
//            }
//            stringBuilder.append("</div>");
//        }
//
//        stringBuilder.append("</body></html>");
        webView.loadData(data, "text/html", "UTF-8");
    }

}
