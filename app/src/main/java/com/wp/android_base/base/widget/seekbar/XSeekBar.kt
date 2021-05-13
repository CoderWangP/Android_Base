package com.wp.android_base.base.widget.seekbar

import android.content.Context
import android.os.Build
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import com.wp.android_base.R
import com.wp.android_base.base.utils.log.Logger
import kotlinx.android.synthetic.main.layout_x_seekbar.view.*
import java.math.BigDecimal

/**
 *
 * Created by wp on 2020/8/27.
 *
 * Description:
 *
 */
class XSeekBar : LinearLayout {

    private var TAG = "XSeekBar"

    private var mDecimals: Int = 0
    private var mMin: Int = 0
    private var mMax: Int = 100

    private var mPow: BigDecimal = BigDecimal("1")

    constructor(context: Context) : this(context, null)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr, 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor (context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, i: Int) {
        LayoutInflater.from(context).inflate(R.layout.layout_x_seekbar, this, true)
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.XSeekBar)
        mDecimals = attrArray.getInt(R.styleable.XSeekBar_decimals, 0)
        mMin = attrArray.getInt(R.styleable.XSeekBar_min, 0)
        mMax = attrArray.getInt(R.styleable.XSeekBar_max, 100)
        mPow = 10.toBigDecimal().pow(mDecimals)
        mMin = mMin.toBigDecimal().multiply(mPow).toInt()
        mMax = mMax.toBigDecimal().multiply(mPow).toInt()
        attrArray.recycle()
        initSeekBar()
    }

    private fun initSeekBar() {
        seekbar.max = mMax
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Logger.d(TAG, "SeekBar progress = $progress")
                mOnSeekBarChangeListener?.run {
                    val realProgress = (mMin.toBigDecimal() + progress.toBigDecimal().divide(mPow,
                            mDecimals, BigDecimal.ROUND_HALF_UP)).toPlainString()
                    Logger.d(TAG, "Real progress = $realProgress")
                    onProgressChanged(seekBar, realProgress, fromUser)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mOnSeekBarChangeListener?.run {
                    val progress = seekBar.progress
                    val realProgress = (mMin.toBigDecimal() + progress.toBigDecimal().divide(mPow,
                            mDecimals, BigDecimal.ROUND_HALF_UP)).toPlainString()
                    onStopTrackingTouch(seekBar, realProgress)
                }
            }
        })
    }

    interface OnSeekBarChangeListener {
        /**
         * @param seekBar The SeekBar whose progress has changed
         * @param progress string类型，自己转为需要的类型
         * @param fromUser True if the progress change was initiated by the user.
         */
        fun onProgressChanged(seekBar: SeekBar, progress: String, fromUser: Boolean)


        fun onStopTrackingTouch(seekBar: SeekBar, progress: String)
    }

    private var mOnSeekBarChangeListener: OnSeekBarChangeListener? = null


    fun setOnSeekBarChangeListener(onSeekBarChangeListener: OnSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener
    }
}