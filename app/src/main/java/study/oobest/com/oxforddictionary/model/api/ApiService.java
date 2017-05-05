package study.oobest.com.oxforddictionary.model.api;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import study.oobest.com.oxforddictionary.model.entity.DictionaryResult;

/**
 * Created by Albert.Ou on 2017/5/5.
 */

public final class ApiService {
    public static Observable<DictionaryResult> getSearchWord(@NonNull String word){
        return Observable.just(word).map(new Function<String, DictionaryResult>() {
            @Override
            public DictionaryResult apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                SearchWordsOperator searchWordsOperator = new SearchWordsOperator(s);
                return searchWordsOperator.searchWord();
            }
        });
    }
}
