package com.albertou.study.dict.kit;

import android.util.Log;

import com.albertou.study.dict.DictCallback;
import com.albertou.study.dict.DictImpl;
import com.albertou.study.dict.kit.api.ApiService;
import com.albertou.study.dict.kit.entity.DictionaryResult;
import com.albertou.study.dict.kit.entity.Example;
import com.albertou.study.dict.kit.entity.Pronunciation;
import com.albertou.study.dict.kit.entity.Semantics;
import com.albertou.study.dict.kit.util.EntityUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by oujianfeng on 2018/3/3.
 */

public class DictApi implements DictImpl{
    @Override
    public void fitch(String word, final DictCallback dictCallback) {
        ApiService.getSearchWord(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<DictionaryResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(DictionaryResult dictionaryResult) {
                        Log.d(TAG, "onNext: dictionaryResult=" + EntityUtils.gson.toJson(dictionaryResult));
                        if(dictCallback!=null){
                            dictCallback.onCallback(getData(dictionaryResult));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getLocalizedMessage(), e);
                        if(dictCallback!=null){
                            dictCallback.onCallback(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    private String getData(DictionaryResult dictionaryResult) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<!DOCTYPE HTML><html><body>");
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
                        .append("<audio controls='controls'><source src='" + pronunciation.audio + "' type='audio/mpeg'>").append("</audio>")
                        .append("</span>").append("</br>");
                Log.d(TAG, "setContent: pronunciation.phonics=" + pronunciation.phonics);
            }
            stringBuilder.append("</div>");

            stringBuilder.append("<div>");
            for(Semantics semantics: dictionaryResult.semantics){
                stringBuilder.append("<h4>").append(semantics.gram).append("</br>").append(semantics.def).append("</h4>");
                if(semantics.exp!=null) {
                    stringBuilder.append("<ul>");
                    for (Example example : semantics.exp) {
                        stringBuilder.append("<li><strong>").append(example.cf).append("</strong>&nbsp;").append(example.x).append("</li>");
                    }
                    stringBuilder.append("</ul>");
                }
            }
            stringBuilder.append("</div>");
        }

        stringBuilder.append("</body></html>");
        return stringBuilder.toString();
    }
}
