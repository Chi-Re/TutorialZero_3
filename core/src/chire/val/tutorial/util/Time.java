package chire.val.tutorial.util;


import chire.val.tutorial.Vars;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ���ȣ��˴��Ĵ��������{@link chire.val.tutorial.util.Timer}���棬�������������ø���;><br>
 * ���ȣ���л ��������� �Ĵ��룡
 * @author ���������
 * @Description:
 * @date 2019/3/23
 */
public class Time {
    public static void task(long period, Runnable t) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //���������ش�ʱ��������ж�ʱ��
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
