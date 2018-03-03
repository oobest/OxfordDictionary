package com.albertou.study.dict.kit.api;


import com.albertou.study.dict.kit.entity.DictionaryResult;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by Albert.Ou on 2017/5/5.
 */

public final class ApiService {
    public static Observable<DictionaryResult> getSearchWord(String word){
        return Observable.just(word).map(new Function<String, DictionaryResult>() {
            @Override
            public DictionaryResult apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                SearchWordsOperator searchWordsOperator = new SearchWordsOperator(s);
                return searchWordsOperator.searchWord();
            }
        });
    }
}
