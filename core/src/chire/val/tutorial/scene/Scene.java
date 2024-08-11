package chire.val.tutorial.scene;

import chire.val.tutorial.camera.Camera;

import java.awt.*;
import java.util.Set;

public interface Scene {
    void on_enter();
    void on_update(int delta);
    void on_draw(Graphics g, Camera camera);
    void on_input(Set<Integer> keys);
    void on_exit();
}
