package chire.val.tutorial.core;


import chire.val.tutorial.Vars;
import chire.val.tutorial.camera.Camera;
import chire.val.tutorial.scene.Scene;

import java.awt.*;
import java.util.Set;

public class SceneManager {
    private Scene current_state;

    public SceneManager(){

    }

    public void set_current_scene(Scene scene){
        current_state = scene;
        current_state.on_enter();
    }

    public void switch_to(SceneType type){
        current_state.on_exit();
        switch (type){
            case Menu -> {
                current_state = Vars.menuScene;
            }
            case Game -> {
                current_state = Vars.gameScene;
            }
            case Selector -> {
                current_state = Vars.selectorScene;
            }
            default -> {

            }
        }
        current_state.on_enter();
    }


    public void on_update(int delta) {
        current_state.on_update(delta);
    }

    public void on_draw(Graphics g, Camera camera) {
        current_state.on_draw(g, camera);
    }

    public void on_input(Set<Integer> keys) {
        current_state.on_input(keys);
    }
}
