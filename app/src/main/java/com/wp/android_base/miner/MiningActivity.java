package com.wp.android_base.miner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.wp.android_base.R;
import com.wp.android_base.base.BaseActivity;
import com.wp.android_base.base.http.ApiException;
import com.wp.android_base.base.http.HttpRequestManager;
import com.wp.android_base.api.mining.DifficultyData;
import com.wp.android_base.api.mining.LimitOrderData;
import com.wp.android_base.api.mining.MiningApi;
import com.wp.android_base.api.mining.Order;
import com.wp.android_base.api.mining.PlaceLimitOrderBody;
import com.wp.android_base.api.mining.PriceData;
import com.wp.android_base.base.http.HttpResult;
import com.wp.android_base.base.utils.AppModule;
import com.wp.android_base.base.utils.BigDecimalUtil;
import com.wp.android_base.base.utils.Sp;
import com.wp.android_base.base.utils.ToastUtil;
import com.wp.android_base.base.utils.log.Logger;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangpeng on 2018/7/12.
 */

public class MiningActivity extends BaseActivity {

    private static final String TAG = "MiningActivity";

    private String mAssessId = "F6E94B23FFAF4FA3BD16D8FE5B1CD3BD";
    private String mSecret = "2A54B3D827C54C4790FF81EAA4C17817F990E58A6598A52B";
    private String mMarket = "CETUSDT";
    private String mAmount = "1";
    private String mDepth = "5";
    private String mMerge = "0.000001";

    private Map<String, Order> mBuyOrders;
    private Map<String, Order> mSellOrders;

    private ScheduledExecutorService mOrderCountService;


    private volatile boolean BUY_ORDER_STATUS = false;
    private volatile boolean SELL_ORDER_STATUS = false;

    private volatile AtomicInteger mCount = new AtomicInteger(0);
    private int MAX_PLACE_ORDER_COUNT = 1 * 2;
    private int MAX_CANCEL_ORDER_TIME = 3 * 60;

    private MiningApi mMiningApi;
    private DateChangeReceiver mDateChangeReceiver;
    private ExecutorService mTradeService;

    private TextView mTxError;

    private String mMineredCETCount = "0";


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mining;
    }

    @Override
    protected void initializeView() {
        super.initializeView();

        mTxError = findViewById(R.id.tx_error);

        mBuyOrders = new LinkedHashMap<>();
        mSellOrders = new LinkedHashMap<>();
        mOrderCountService = Executors.newSingleThreadScheduledExecutor();
        mTradeService = Executors.newFixedThreadPool(2);

        mMiningApi = HttpRequestManager.createApi(MiningApi.class);
        mMineredCETCount = Sp.from(AppModule.provideApplication()).read().getString(generateMinerCetAmountKey(), "0");
        getDifficulty();
        mDateChangeReceiver = new DateChangeReceiver();
        registerReceiver(mDateChangeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

        checkOrderStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String key = generateMinerCetAmountKey();
        Sp.from(AppModule.provideApplication()).writer().putString(key, mMineredCETCount);
    }

    private String generateMinerCetAmountKey() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        return month + "-" + day + "-" + hour;
    }

    public void minering(View v) {
        v.setEnabled(false);
        getDifficulty();
//        placeLimeOrder();
    }

    private void getDifficulty() {
        String date = dateToStamp();
        String auth = createDifficultyAuth(date);
        mMiningApi.getDifficulty(auth, mAssessId, date)
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<DifficultyData>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<DifficultyData> httpResult) {
                        DifficultyData difficultyData = httpResult.getData();
                        String difficulty = difficultyData.getDifficulty();
                        Logger.e("difficulty", "difficulty= " + difficulty);
                        if (BigDecimalUtil.compareWithZero(difficulty) > 0) {
                            if (BigDecimalUtil.compare(mMineredCETCount, difficulty) > 0) {
                                //停止
                                stopMining();
                            } else {
                                placeLimeOrder();
                            }
                        }
                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {
                        Logger.e("getDifficulty", responseThrowable.getMessage());
                        mTxError.setText("getDifficulty()->" + responseThrowable.getMessage());
                    }
                });
    }


    private void stopMining() {
        mOrderCountService.shutdownNow();
    }

    private void checkOrderStatus() {
        stopMining();
        mOrderCountService = Executors.newSingleThreadScheduledExecutor();
        mOrderCountService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Logger.e(MiningActivity.TAG, "定时任务");
                Logger.e("count= " + mCount);
                if (BUY_ORDER_STATUS && SELL_ORDER_STATUS) {
                    BUY_ORDER_STATUS = false;
                    SELL_ORDER_STATUS = false;
/*                    if (mCount.get() <= MAX_PLACE_ORDER_COUNT) {
                        placeLimeOrder();
                    } else {
                        mOrderCountService.shutdownNow();
                    }*/
                    getDifficulty();
                }
            }
        }, 10, 2, TimeUnit.SECONDS);
    }

    private void placeLimeOrder() {
        queryPrice();
    }


    private void placeOrder(String price, String orderType) {
        PlaceLimitOrderBody placeLimitOrderBody = createRequestParam(price, orderType);
        String auth = createSignature(placeLimitOrderBody);
        mMiningApi.placeLimitOrder(auth, placeLimitOrderBody)
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<LimitOrderData>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<LimitOrderData> httpResult) {
                        LimitOrderData limitOrderData = httpResult.getData();
                        Logger.e(MiningActivity.TAG, "placeOrder->" + orderType + "->price = " + price);
                        String status = limitOrderData.getStatus();
                        String type = limitOrderData.getType();
                        String orderNumber = limitOrderData.getId();
                        String price = limitOrderData.getPrice();
                        if ("done".equalsIgnoreCase(status)) {
                            mCount.addAndGet(1);
                            //已成交
                            if ("buy".equalsIgnoreCase(type)) {
                                BUY_ORDER_STATUS = true;
                                mMineredCETCount = BigDecimalUtil.add(mMineredCETCount, limitOrderData.getDeal_fee());
                            } else if ("sell".equalsIgnoreCase(type)) {
                                try {
                                    SELL_ORDER_STATUS = true;
                                    String dealFee = limitOrderData.getDeal_fee();
                                    String dealFee2CET = BigDecimalUtil.div(dealFee, price);
                                    mMineredCETCount = BigDecimalUtil.add(mMineredCETCount, dealFee2CET);
                                } catch (Exception e) {
                                    ToastUtil.show(e.getMessage());
                                }
                            }
                        } else {
                            if ("buy".equalsIgnoreCase(type)) {
                                mBuyOrders.put(orderNumber, new Order(price, orderNumber));
                            } else if ("sell".equalsIgnoreCase(type)) {
                                mSellOrders.put(orderNumber, new Order(price, orderNumber));
                            }
                            queryOrderStatus(orderNumber);
                        }
                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {
                        mTxError.setText("placeOrder()->" + responseThrowable.getMessage());
                    }
                });
    }

    //初始下单
    private void queryPrice() {
        mMiningApi.queryPrice(mMarket, mDepth, mMerge)
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<PriceData>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<PriceData> httpResult) {
                        PriceData priceData = httpResult.getData();
                        //同时下卖单，买单
                        List<String[]> asks = priceData.getAsks();
                        List<String[]> bids = priceData.getBids();
                        String[] targetAsk = asks.get(Integer.parseInt(mDepth) / 2);
                        String[] targetBid = bids.get(Integer.parseInt(mDepth) / 2);
//                        String price = priceData.getLast();
                        String price = BigDecimalUtil.div(BigDecimalUtil.add(targetAsk[0], targetBid[0]), "2");

                        PlaceOrderThread buyThread = new PlaceOrderThread(price, "buy");
                        PlaceOrderThread sellThread = new PlaceOrderThread(price, "sell");
                        mTradeService.submit(buyThread);
                        mTradeService.submit(sellThread);
/*                        placeSellOrder(price,"sell");
                        placeBuyOrder(price,"buy");*/
                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {
                        mTxError.setText("queryPrice()->" + responseThrowable.getMessage());
                    }
                });
    }

    private class PlaceOrderThread implements Runnable {

        private String mPrice;
        private String mTradeType;

        public PlaceOrderThread(String price, String tradeType) {
            this.mPrice = price;
            this.mTradeType = tradeType;
        }

        @Override
        public void run() {
            placeOrder(mPrice, mTradeType);
        }
    }


    private void queryOrderStatus(String orderNumber) {
        String date = dateToStamp();
        String auth = createQueryOrderAuth(orderNumber, date);
        mMiningApi.queryOrderStatus(auth, mAssessId, orderNumber, mMarket, date)
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<LimitOrderData>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<LimitOrderData> httpResult) {
                        LimitOrderData limitOrderData = httpResult.getData();
                        String status = limitOrderData.getStatus();
                        String type = limitOrderData.getType();
                        String orderNumber = limitOrderData.getId();
                        String price = limitOrderData.getPrice();
                        Logger.e(MiningActivity.TAG, "查询order的状态");
                        if ("done".equalsIgnoreCase(status)) {
                            mCount.addAndGet(1);
                            //已成交
                            if ("buy".equalsIgnoreCase(type)) {
                                mBuyOrders.remove(orderNumber);
                                BUY_ORDER_STATUS = true;
                                mMineredCETCount = BigDecimalUtil.add(mMineredCETCount, limitOrderData.getDeal_fee());
                            } else if ("sell".equalsIgnoreCase(type)) {
                                mSellOrders.remove(orderNumber);
                                SELL_ORDER_STATUS = true;
                                try {
                                    String dealFee = limitOrderData.getDeal_fee();
                                    String dealFee2CET = BigDecimalUtil.div(dealFee, price);
                                    mMineredCETCount = BigDecimalUtil.add(mMineredCETCount, dealFee2CET);
                                } catch (Exception e) {
                                    ToastUtil.show(e.getMessage());
                                }
                            }
                        } else if ("not_deal".equalsIgnoreCase(status) || "part_deal".equalsIgnoreCase(status)) {
                            long createTime = Long.parseLong(limitOrderData.getCreate_time());
                            Logger.e("createTime= ", createTime);
                            Date date = new Date();
                            long now = date.getTime() / 1000;
                            Logger.e("nowTime= ", now);
                            if ((now - createTime) >= MAX_CANCEL_ORDER_TIME) {
                                Logger.e("nowTime - createTime = ", (now - createTime) + "");
                                cancelOrder(orderNumber);
                            } else {
                                try {
                                    Thread.sleep(2 * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                queryOrderStatus(orderNumber);
                            }
                        }
                    }

                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {
                        mTxError.setText("queryOrderStatus()->" + responseThrowable.getMessage());
                        try {
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        queryOrderStatus(orderNumber);
                    }
                });
    }

    private String createQueryOrderAuth(String orderNumber, String date) {
        String accessId = mAssessId;
        String id = orderNumber;
        String market = mMarket;
        String tonce = date;
        String toSign = "access_id=" + accessId + "&id=" + id + "&market=" + market + "&tonce=" + tonce + "&secret_key=" + mSecret;
        return Md5(toSign);
    }

    private String createCancelOrderAuth(String orderNumber, String date) {
        String accessId = mAssessId;
        String id = orderNumber;
        String market = mMarket;
        String tonce = date;
        String toSign = "access_id=" + accessId + "&id=" + id + "&market=" + market + "&tonce=" + tonce + "&secret_key=" + mSecret;
        return Md5(toSign);
    }

    private String createDifficultyAuth(String date) {
        String accessId = mAssessId;
        String tonce = date;
        String toSign = "access_id=" + accessId + "&tonce=" + tonce + "&secret_key=" + mSecret;
        return Md5(toSign);
    }

    private void cancelOrder(String orderNumber) {
        String date = dateToStamp();
        String auth = createCancelOrderAuth(orderNumber, date);
        mMiningApi.cancelOrder(auth, mAssessId, orderNumber, mMarket, date)
                .compose(HttpRequestManager.createDefaultTransformer(this))
                .subscribe(new HttpRequestManager.SimpleObserver<HttpResult<LimitOrderData>>(this) {
                    @Override
                    protected void onSuccess(HttpResult<LimitOrderData> httpResult) {
                        LimitOrderData limitOrderData =httpResult.getData();
                        String type = limitOrderData.getType();
                        String orderNumber = limitOrderData.getId();
                        Logger.e(MiningActivity.TAG, "取消订单");
                        if ("buy".equalsIgnoreCase(type)) {
                            mBuyOrders.remove(orderNumber);
                            BUY_ORDER_STATUS = true;
                        } else if ("sell".equalsIgnoreCase(type)) {
                            mSellOrders.remove(orderNumber);
                            SELL_ORDER_STATUS = true;
                        }
                    }


                    @Override
                    protected void onError(ApiException.ResponseThrowable responseThrowable) {
                        mTxError.setText("cancelOrder()->" + responseThrowable.getMessage());
                        queryOrderStatus(orderNumber);
                    }
                });
    }

    private PlaceLimitOrderBody createRequestParam(String price, String orderType) {
        String assessId = mAssessId;
        String amount = mAmount;
        String market = mMarket;
//        String tPrice = price;
        String tonce = dateToStamp();
        String type = orderType;
        return new PlaceLimitOrderBody(assessId, amount, market, price, tonce, type);
    }

    private String createSignature(PlaceLimitOrderBody placeLimitOrderBody) {
        String assessId = placeLimitOrderBody.getAccess_id();
        String amount = placeLimitOrderBody.getAmount();
        String market = placeLimitOrderBody.getMarket();
        String price = placeLimitOrderBody.getPrice();
        String tonce = placeLimitOrderBody.getTonce();
        String type = placeLimitOrderBody.getType();
        String secret = mSecret;

        String toSign = "access_id=" + assessId + "&amount=" + amount + "&market=" + market + "&price=" + price + "&tonce=" + tonce + "&type=" + type + "&secret_key=" + secret;

        return Md5(toSign);
//        encode(toSign);
    }

    public static String Md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        Logger.e("MD5", buf.toString());
        return buf.toString().toUpperCase();
    }

    public static String encode(String text) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            Logger.e("encode", sb.toString().toUpperCase());
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /*
    * 将时间转换为时间戳
    */
    public String dateToStamp() {
        Date date = new Date();
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderCountService.shutdownNow();
        if (mDateChangeReceiver != null) {
            unregisterReceiver(mDateChangeReceiver);
        }
    }


    private class DateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                Logger.e("时间", "hour= " + hour + " min= " + min);
                if (min == 0) {
                    //整点
                    Logger.e("时间>>整点", "hour= " + hour + "min= " + min);
                    getDifficulty();
                }
            }
        }
    }
}
