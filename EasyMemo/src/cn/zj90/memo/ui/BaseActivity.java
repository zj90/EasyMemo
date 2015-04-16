package cn.zj90.memo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by admin on 2015/4/8.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInit();
        initData();
        initView();
        initEvent();
    }

    protected void beforeInit() {}
    protected void initData() {}
    protected void initView() {}
    protected void initEvent() {}

    protected void forward(Class<?> classObj) {
        Intent intent = new Intent(this, classObj);
        startActivity(intent);
        finish();
    }

    protected void forward(Class<?> classObj, Bundle bundle) {
        Intent intent = new Intent(this, classObj);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    protected void open(Class<?> classObj) {
        Intent intent = new Intent(this, classObj);
        startActivity(intent);
    }

    protected void open(Class<?> classObj, Bundle bundle) {
        Intent intent = new Intent(this, classObj);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void log(String msg) {
        Log.i("debug", msg);
    }

}
