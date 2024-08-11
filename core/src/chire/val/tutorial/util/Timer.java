package chire.val.tutorial.util;

public class Timer {
    private int passTime = 0,
            waitTime = 0;

    private boolean paused = false,
            shotted = false,
            oneShot = false;

    private Runnable callback;

    public Timer(){
    }

    public void restart(){
        passTime = 0;
        shotted = false;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setOneShot(boolean oneShot) {
        this.oneShot = oneShot;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public void pause(){
        paused = true;
    }

    public void resume(){
        paused = false;
    }

    public void onUpdate(int delta){
        if (paused) return;

        passTime += delta;

        if (passTime >= waitTime){
            if ((!oneShot || (oneShot && !shotted)) && (callback != null)) callback.run();
            shotted = true;
            passTime = 0;
        }
    }
}
