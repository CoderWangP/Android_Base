package com.wp.android_base.base.utils.log;

import android.text.TextUtils;
import android.util.Log;

import com.wp.android_base.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;

/**
 * Created by wangpeng on 2018/6/21.
 * Log工具类
 */

public class Logger {

    private static String sTag = "android_base";

    //Logcat对单个日志输出限制为 4 * 1024个字符
    private static final int MAX_LEN = 4000;
    private static final String TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final String LEFT_BORDER = "║ ";
    private static final String BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final String NULL_TIPS = "Log with null object.";
    private static final String NULL = "null";
    private static final String ARGS = "args";

    private static final boolean ENABLE_LOG = BuildConfig.DEBUG;

    private Logger() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void v(Object contents) {
        log(LogLevelDef.V, sTag, contents);
    }

    public static void v(String tag, Object... contents) {
        log(LogLevelDef.V, tag, contents);
    }

    public static void d(Object contents) {
        log(LogLevelDef.D, sTag, contents);
    }

    public static void d(String tag, Object... contents) {
        log(LogLevelDef.D, tag, contents);
    }

    public static void i(Object contents) {
        log(LogLevelDef.I, sTag, contents);
    }

    public static void i(String tag, Object... contents) {
        log(LogLevelDef.I, tag, contents);
    }

    public static void w(Object contents) {
        log(LogLevelDef.W, sTag, contents);
    }

    public static void w(String tag, Object... contents) {
        log(LogLevelDef.W, tag, contents);
    }

    public static void e(Object contents) {
        log(LogLevelDef.E, sTag, contents);
    }

    public static void e(String tag, Object... contents) {
        log(LogLevelDef.E, tag, contents);
    }

    public static void a(Object contents) {
        log(LogLevelDef.A, sTag, contents);
    }

    public static void a(String tag, Object... contents) {
        log(LogLevelDef.A, tag, contents);
    }

    private static void log(@LogLevelDef int logLevel, String tag, Object... contents) {
        if (!ENABLE_LOG) {
            return;
        }
        String[] logContents = createLogContents(tag, contents);
        Log.d("AAA",logContents[0]);
        tag = logContents[0];
        String msg = logContents[1];
        printLog(logLevel, tag, msg);
    }

    public static void json(String tag, String json) {
        printLog(LogLevelDef.D,tag,json);
    }

    public static void json(@LogLevelDef int logLevel,String tag, String json) {
        if (!ENABLE_LOG) {
            return;
        }
        if (TextUtils.isEmpty(json)) {
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.toString(4);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                json = jsonArray.toString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        printLog(logLevel,tag,json);
    }

    private static void printLog(@LogLevelDef int logLevel, String tag, String msg) {

//        printBorder(logLevel, tag, true);

        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {
            int index = 0;
            String sub;
            for (int i = 0; i < countOfSub; i++) {
                sub = msg.substring(index, index + MAX_LEN);
                printSubLog(logLevel, tag, sub);
                index += MAX_LEN;
            }
            printSubLog(logLevel, tag, msg.substring(index, len));
        } else {
            printSubLog(logLevel, tag, msg);
        }
//        printBorder(logLevel, tag, false);
    }

    private static void printSubLog(final @LogLevelDef int logLevel, final String tag, String msg) {
        msg = LEFT_BORDER + msg;
        switch (logLevel) {
            case LogLevelDef.V:
                Log.v(tag, msg);
                break;
            case LogLevelDef.D:
                Log.d(tag, msg);
                break;
            case LogLevelDef.I:
                Log.i(tag, msg);
                break;
            case LogLevelDef.W:
                Log.w(tag, msg);
                break;
            case LogLevelDef.E:
                Log.e(tag, msg);
                break;
            case LogLevelDef.A:
                Log.wtf(tag, msg);
                break;
        }
    }

    private static void printBorder(final @LogLevelDef int logLevel, final String tag, boolean isTop) {
        String border = isTop ? TOP_BORDER : BOTTOM_BORDER;
        switch (logLevel) {
            case LogLevelDef.V:
                Log.v(tag, border);
                break;
            case LogLevelDef.D:
                Log.d(tag, border);
                break;
            case LogLevelDef.I:
                Log.i(tag, border);
                break;
            case LogLevelDef.W:
                Log.w(tag, border);
                break;
            case LogLevelDef.E:
                Log.e(tag, border);
                break;
            case LogLevelDef.A:
                Log.wtf(tag, border);
                break;
        }
    }

    private static String[] createLogContents(String tag, Object... contents) {
        StackTraceElement targetElement = getTargetStackTraceElement();
        String className = targetElement.getFileName();
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        String header = new Formatter()
                .format("%s(%s:%d)" + LINE_SEPARATOR,
                        methodName,
                        className,
                        lineNumber)
                .toString();

        if (TextUtils.isEmpty(sTag)) {
            if (TextUtils.isEmpty(tag)) {
                tag = className;
            }
        } else {
            if (TextUtils.isEmpty(tag)) {
                tag = sTag;
            }
        }

        String msg = NULL_TIPS;
        if (contents != null) {
            StringBuilder sb = new StringBuilder();
            if (contents.length == 1) {
                Object object = contents[0];
                sb.append(object == null ? NULL : object.toString());
                sb.append(LINE_SEPARATOR);
            } else {
                for (int i = 0, len = contents.length; i < len; ++i) {
                    Object content = contents[i];
                    sb.append(ARGS)
                            .append("[")
                            .append(i)
                            .append("]")
                            .append(" = ")
                            .append(content == null ? NULL : content.toString())
                            .append(LINE_SEPARATOR);
                }
            }
            msg = sb.toString();
        }

        StringBuilder sb = new StringBuilder();
        String[] lines = msg.split(LINE_SEPARATOR);
        for (String line : lines) {
            sb.append(LEFT_BORDER).append(line).append(LINE_SEPARATOR);
        }
        msg = sb.toString();
        return new String[]{tag, header + msg};
    }


    private static StackTraceElement getTargetStackTraceElement() {
        // find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(Logger.class.getName()) || stackTraceElement.getClassName().startsWith("java.lang");
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }
}
