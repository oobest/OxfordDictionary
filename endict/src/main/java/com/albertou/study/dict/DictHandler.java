package com.albertou.study.dict;

import android.content.Context;

import java.util.Dictionary;

/**
 * Created by oujianfeng on 2018/3/3.
 */

public class DictHandler {

    private static volatile DictHandler mDictHandler;

    private static final String DICT_API_NAME = "com.albertou.study.dict.kit.DictApi";

    private DictImpl mDict;

    private DictHandler(Context context) {
        mDict = newDictInstance(context, DICT_API_NAME);
    }

    private DictImpl newDictInstance(Context context, String className) {
        DictImpl mDict = null;
        try {
            Class<?> cls = CLoader.loadClass(context, className);
            mDict = (DictImpl) cls.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mDict;
    }

    private static DictHandler getInstance(Context context) {
        if (mDictHandler == null) {
            synchronized (DictHandler.class) {
                if (mDictHandler == null) {
                    mDictHandler = new DictHandler(context);
                }
            }
        }
        return mDictHandler;
    }

    public static boolean fetch(Context context, String word, DictCallback callback) {
        DictHandler handler = getInstance(context);
        if (handler.mDict == null) {
            return false;
        }
        handler.mDict.fitch(word, callback);
        return true;
    }
}
