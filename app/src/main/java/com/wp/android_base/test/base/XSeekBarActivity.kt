package com.wp.android_base.test.base

import android.widget.SeekBar
import com.wp.android_base.R
import com.wp.android_base.base.BaseActivity
import com.wp.android_base.base.widget.seekbar.XSeekBar
import kotlinx.android.synthetic.main.activity_x_seekbar.*

/**
 *
 * Created by wp on 2020/8/28.
 *
 * Description:
 *
 */
class XSeekBarActivity :BaseActivity(){
    override fun getContentLayoutId(): Int {
        return R.layout.activity_x_seekbar
    }

    override fun registerListener() {
        super.registerListener()
        xseekbar.setOnSeekBarChangeListener(object :XSeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: String, fromUser: Boolean) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar, progress: String) {

            }

        })
    }
}