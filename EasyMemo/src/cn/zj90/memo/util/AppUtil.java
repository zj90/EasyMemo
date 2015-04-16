package cn.zj90.memo.util;

import android.app.Activity;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppUtil extends Activity {

    public static String returnTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(d);
        return time;
    }


}
