package com.wp.android_base.base.http;

import com.google.gson.JsonParseException;
import com.wp.android_base.R;
import com.wp.android_base.base.utils.AppModule;

import org.json.JSONException;
import java.net.ConnectException;
import retrofit2.HttpException;


/**
 * Created by fcn-mq on 2017/4/19.
 */

public class ApiException {

    public static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.code = httpException.code();
                    ex.message = AppModule.provideApplication().getString(R.string.net_error);
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                /*|| e instanceof ParseException*/) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = AppModule.provideApplication().getString(R.string.parse_error);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORK_ERROR);
            ex.message = AppModule.provideApplication().getString(R.string.connect_error);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = AppModule.provideApplication().getString(R.string.ssl_handshake_error);
            return ex;
        }else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.SSL_SOCKET_TIMEOUT_ERROR);
            ex.message = AppModule.provideApplication().getString(R.string.socket_timeout_error);
            return ex;
        }else if (e instanceof java.net.SocketException) {
            ex = new ResponseThrowable(e, ERROR.NETWORK_IS_UNREACHABLE);
            ex.message = AppModule.provideApplication().getString(R.string.network_unable_use);
            return ex;
        }else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.NETWORK_UNKNOWNHOST);
            ex.message = AppModule.provideApplication().getString(R.string.network_unable_use);
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
//            ex.message = AppModule.provideApplication().getString(R.string.unknown_error);
            ex.message = e.getMessage();
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int SSL_SOCKET_TIMEOUT_ERROR = 1006;


        /**
         * 网络不可用
         */
        public static final int NETWORK_IS_UNREACHABLE = 1007;

        /**
         * 网络不可用
         */
        public static final int NETWORK_UNKNOWNHOST = 1008;
    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    /**
     * ServerException发生后，将自动转换为ResponseThrowable 返回在onError(e)中
     */
    class ServerException extends RuntimeException {
        int code;
        String message;
    }

}
