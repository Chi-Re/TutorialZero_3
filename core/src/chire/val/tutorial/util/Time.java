package chire.val.tutorial.util;


import chire.val.tutorial.Vars;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 炽热：此处的代码可以用{@link chire.val.tutorial.util.Timer}代替，不过现在我懒得改了;><br>
 * 炽热：感谢 左边牙齿疼 的代码！
 * @author 左边牙齿疼
 * @Description:
 * @date 2019/3/23
 */
public class Time {
    public static void task(long period, Runnable t) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //当结束开关打开时，清除所有定时器
                if (Vars.time_stop) {
                    timer.cancel();
                    return;
                }
                t.run();
            }
        };
        timer.schedule(timerTask, 0, period);
    }
}
