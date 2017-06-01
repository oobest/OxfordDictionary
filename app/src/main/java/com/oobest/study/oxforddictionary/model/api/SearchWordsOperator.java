package com.oobest.study.oxforddictionary.model.api;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.oobest.study.oxforddictionary.model.entity.DictionaryResult;
import com.oobest.study.oxforddictionary.model.entity.Example;
import com.oobest.study.oxforddictionary.model.entity.Pronunciation;
import com.oobest.study.oxforddictionary.model.entity.Semantics;

/**
 * Created by Albert.Ou on 2017/5/5.
 */

public final class SearchWordsOperator {

    private static final String TAG = "SearchWordsOperator";

    private String words;

    protected SearchWordsOperator(String words) {
        this.words = words;
    }

    protected DictionaryResult searchWord() {
        try {
            Document doc = Jsoup.connect(ApiDefine.SEARCH_URL).data("q", words).get();
            return parseHtml(doc);
        } catch (IOException e) {
            Log.e(TAG, "searchWord: ", e);
        }
        return null;
    }

    private DictionaryResult parseHtml(Document doc) {
        DictionaryResult result = null;
        Elements entryContent = doc.select("#entryContent");
        if (entryContent != null) {
            result = new DictionaryResult();
            result.word = entryContent.select("h2.h").text();
            result.partOfSpeech = entryContent.select("div.webtop-g").select("span.pos").text();
            result.pronunciation = parsePronunciation(entryContent);
            result.semantics = parseSemantics(entryContent);
        }
        return result;
    }

    private List<Pronunciation> parsePronunciation(Elements elements) {
        List<Pronunciation> result = null;
        Elements prons = elements.select("span.pron-g");
        if (prons != null && prons.size() > 0) {
            result = new ArrayList<Pronunciation>();
            for (Element pron : prons) {
                Pronunciation pronunciation = new Pronunciation();
                pronunciation.name = pron.select("span.prefix").text();
                Elements phonics = pron.select("span.phon");
                if (phonics != null) {
                    String text = phonics.text().replace("//", "");
                    String name = pronunciation.name;
                    if (name != null) {
                        text = text.replace(name, "");
                    }
                    pronunciation.phonics = text;
                }
                Elements sound = pron.select("div.sound");
                if (sound != null) {
                    pronunciation.audio = sound.attr("data-src-mp3");
                }
                result.add(pronunciation);
            }
        }
        return result;
    }

    private List<Semantics> parseSemantics(Elements elements) {
        List<Semantics> result = null;
        Elements sns = elements.select("li.sn-g");
        if (sns != null && sns.size() > 0) {
            result = new ArrayList<Semantics>();
            for (Element sn : sns) {
                Semantics semantics = new Semantics();
                semantics.gram = sn.select("span.gram").text();
                semantics.def = sn.select("span.def").text();
                Elements xgs = sn.select("span.x-gs");
                semantics.exp = parseExp(xgs);
                result.add(semantics);
            }
        } else {
            Elements sn = elements.select("span.sn-g");
            if (sn != null) {
                Semantics semantics = new Semantics();
                semantics.gram = sn.select("span.gram").text();
                semantics.def = sn.select("span.def").text();
                Elements xgs = sn.select("span.x-gs");
                semantics.exp = parseExp(xgs);
                result = new ArrayList<Semantics>();
                result.add(semantics);
            }
        }
        return result;
    }

    private List<Example> parseExp(Elements element) {
        List<Example> result = null;
        Elements xgs = element.select("span.x-g");
        if (xgs != null && xgs.size() > 0) {
            result = new ArrayList<Example>();
            for (Element xg : xgs) {
                Example exmaple = new Example();
                exmaple.x = xg.select("span.x").text();
                exmaple.cf = xg.select("span.cf").text();
                result.add(exmaple);
            }
        }
        return result;
    }

}
