package com.example.max.iGo.Base;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import com.example.max.iGo.R;

/**
 * Created by Max on 2016/3/10.
 */
public abstract class BaseActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // 对返回键按下的响应
                    this.finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                    return false;
        }
        return super.dispatchKeyEvent(event);
    }
}
