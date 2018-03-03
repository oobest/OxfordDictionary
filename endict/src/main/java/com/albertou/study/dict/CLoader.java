package com.albertou.study.dict;

import android.content.Context;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dalvik.system.DexClassLoader;

/**
 * Created by oujianfeng on 2018/3/3.
 */
public class CLoader {

    private static final boolean DEBUG = false;

    private volatile static ClassLoader classLoader;

    public static ClassLoader getClassLoader(Context context) {
        if (classLoader == null) {
            synchronized (CLoader.class) {
                classLoader = newClassLoader(context);
            }
        }
        return classLoader;
    }


    public static ClassLoader newClassLoader(Context context) {
        if (DEBUG) {
            return context.getClassLoader();
        }
        File kitDir = context.getDir("kit", Context.MODE_PRIVATE);
        File file = new File(kitDir, "tmp.zip");
        if (!file.exists()) {
            InputStream in = null;
            FileOutputStream buffOut = null;
            try {
                in = context.getAssets().open("dictkit-release-dex2jar-jar2dex.dex"); //该处可以做很多处理，例如kit功能发生变化，可以做动态更新操作，如果文件加密，可进行解密操作
                buffOut = new FileOutputStream(file);
                byte[] buffer = new byte[0xFF];
                int len;
                while ((len = in.read(buffer)) > 0) {
                	buffOut.write(buffer, 0, len);
                }
                buffOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                closeQuietly(buffOut);
                closeQuietly(in);
            }
        }
//        DexClassLoader classLoader = new DexClassLoader(file.getAbsolutePath(), file.getParent(), null,
//                ClassLoader.getSystemClassLoader());
        DexClassLoader classLoader = new DexClassLoader(file.getAbsolutePath(), file.getParent(), null,
                context.getClassLoader());
        return classLoader;
    }

    public static Class<?> loadClass(Context context, String className) throws ClassNotFoundException {
        Class<?> cls = getClassLoader(context).loadClass(className);
        return cls;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
            }
        }
    }
}
