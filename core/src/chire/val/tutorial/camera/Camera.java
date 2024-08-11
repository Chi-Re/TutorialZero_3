package chire.val.tutorial.camera;

import chire.val.tutorial.util.Timer;
import com.sun.tools.javac.Main;

import java.util.Random;

public class Camera {
    private Vector2 position = new Vector2();

    private Timer timerShake = new Timer();

    private boolean isShaking = false;

    private float shakingStrength = 0;

    private Random rand = new Random();

    public Camera(){
        timerShake.setOneShot(true);
        timerShake.setCallback(()->{
            isShaking = false;
            reset();
        });
    }

    public Vector2 getPosition() {
        return position;
    }

    public void reset(){
        position.x = 0;
        position.y = 0;
    }

    public void onUpdate(int delta){
        timerShake.onUpdate(delta);

        if (isShaking) {
            position.x = (-50 + (float)(-1 + Math.random() * 2) % 100) / 50f * shakingStrength;
            position.y = (-50 + (float)(-1 + Math.random() * 2) % 100) / 50f * shakingStrength;

            System.out.println(position.x);
            System.out.println(position.y);
        }
    }

    public void shake(float strength, int duration){
        isShaking = true;
        this.shakingStrength = strength;

        timerShake.setWaitTime(duration);
        timerShake.restart();
    }
}
