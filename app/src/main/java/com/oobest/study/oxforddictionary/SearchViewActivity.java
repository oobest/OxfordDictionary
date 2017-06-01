package com.oobest.study.oxforddictionary;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.SearchView;
import android.widget.TextView;

import com.oobest.study.oxforddictionary.model.api.ApiService;
import com.oobest.study.oxforddictionary.model.entity.DictionaryResult;
import com.oobest.study.oxforddictionary.model.entity.Pronunciation;
import com.oobest.study.oxforddictionary.model.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewActivity extends AppCompatActivity {

    private static final String TAG = "SearchViewActivity";

    private SearchView searchView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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
        ApiService.getSearchWord(words)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<DictionaryResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(DictionaryResult dictionaryResult) {
                        Log.d(TAG, "onNext: dictionaryResult=" + EntityUtils.gson.toJson(dictionaryResult));
                        setContent(dictionaryResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getLocalizedMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompositeDisposable.clear();
    }

    private void setContent(DictionaryResult dictionaryResult) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div>").append("<h2>").append(dictionaryResult.word).append("</h2>")
                .append("<i>").append(dictionaryResult.partOfSpeech).append("</i>").append("</div>");

        if (dictionaryResult.pronunciation != null) {
            stringBuilder.append("<div>");
            for (Pronunciation pronunciation : dictionaryResult.pronunciation) {
                stringBuilder.append("<span>")
                        .append("<span>").append(pronunciation.name).append("</span>")
                        .append("<span>[<font face=\"Lucida Sans Unicode\">")
                        .append(pronunciation.phonics)
                        .append("</font>]</span>")
                        .append("<audio src=" + pronunciation.audio + ">").append("</audio>")
                        .append("</span>");
                Log.d(TAG, "setContent: pronunciation.phonics=" + pronunciation.phonics);
            }
            stringBuilder.append("</div>");
        }

        webView.loadData(stringBuilder.toString(), "text/html", "UTF-8");
    }

}
