package com.albertou.study.dict.kit.entity;

import java.util.List;

/**
 * Created by Albert.Ou on 2017/5/5.
 */

public class DictionaryResult {
    /**
     * 单词
     */
    public String word;

    /**
     * 单词词性
     */
    public String partOfSpeech;


    /**
     * 发音
     */
    public List<Pronunciation> pronunciation;


    /**
     * 语义
     */
    public List<Semantics> semantics;
}
