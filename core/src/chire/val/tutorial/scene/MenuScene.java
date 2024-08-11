package chire.val.tutorial.scene;


import chire.val.tutorial.Vars;
import chire.val.tutorial.asset.Animation;
import chire.val.tutorial.camera.Camera;
import chire.val.tutorial.camera.Vector2;
import chire.val.tutorial.core.SceneType;
import chire.val.tutorial.util.Timer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Set;

public class MenuScene implements Scene {

    public MenuScene(){
    }

    @Override
    public void on_enter() {
        //Vars.bgm_menu.start();
    }

    @Override
    public void on_update(int delta) {

    }

    @Override
    public void on_draw(Graphics g, Camera camera) {
        g.drawImage(Vars.img_menu_background, 0, 0, null);
    }

    @Override
    public void on_input(Set<Integer> keys) {
        if (keys.contains(KeyEvent.VK_1)){
            Vars.ui_confirm.start();
            Vars.sceneManager.switch_to(SceneType.Game);
        }
    }

    @Override
    public void on_exit() {
        //Vars.bgm_menu.close();
    }
}
