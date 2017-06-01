package com.oobest.study.oxforddictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import com.oobest.study.oxforddictionary.model.api.ApiService;
import com.oobest.study.oxforddictionary.model.entity.DictionaryResult;
import com.oobest.study.oxforddictionary.model.util.EntityUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String word = "system";
        queryWords(word);
        String v = "You can\u0027t beat the system (\u003d you must accept it).";
        Log.d(TAG, "onCreate: v=" + v);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(v);
    }

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
}
