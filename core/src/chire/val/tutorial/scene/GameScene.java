package chire.val.tutorial.scene;


import chire.val.tutorial.camera.Camera;
import chire.val.tutorial.core.SceneType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

public class GameScene implements Scene {
    public GameScene(){
    }

    @Override
    public void on_enter() {
        System.out.println("������Ϸ����");
    }

    @Override
    public void on_update(int delta) {
        //System.out.println("��Ϸ�������");
    }

    @Override
    public void on_draw(Graphics g, Camera camera) {
        System.out.println("��Ϸ�������");
    }

    @Override
    public void on_input(Set<Integer> keys) {

    }

    @Override
    public void on_exit() {
        System.out.println("��Ϸ�����˳�");
    }
}
