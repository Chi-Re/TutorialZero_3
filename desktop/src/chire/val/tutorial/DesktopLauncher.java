package chire.val.tutorial;

import chire.val.tutorial.camera.Camera;
import chire.val.tutorial.core.SceneManager;
import chire.val.tutorial.util.Time;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class DesktopLauncher extends JFrame {
    protected final Set<Integer> keySet = new HashSet<>();

    public static Renderer renderer;

    protected static Camera mainCamera;

    //public static Long begin = new Date().getTime();

    public static void main (String[] arg) throws InterruptedException {
        new DesktopLauncher();
    }

    public DesktopLauncher() throws InterruptedException {
        this.setTitle("植物明星大乱斗");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);//固定窗体
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        Dimension size = new Dimension(1080, 600);
//        int width = toolkit.getScreenSize().width;
//        int height = toolkit.getScreenSize().height;
//        this.setBounds((int) (width - size.getWidth()) / 2,
//                (int) (height - size.getHeight()) / 3, (int) size.getWidth(), (int) size.getHeight());
        this.setBounds(0, 0, 1280, 720);

        mainCamera = new Camera();
        Vars.load();
        Vars.init();
        this.init();
    }

    private void init() {
        renderer = new Renderer(Vars.sceneManager);
        this.add(renderer);

        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, Vars.fontsDirectory.child("IPix.ttf").getFileURL().openStream());
            initGlobalFontSetting(f.deriveFont(32f));
        } catch (IOException | FontFormatException e){
            throw new RuntimeException(e);
        }

        this.setVisible(true);

//        try {
//            InputStream audioStream = Vars.musicDirectory.child("bgm_menu.mp3").getFileURL().openStream(); // 音频文件路径
//
//            new Thread(()->{
//                try {
//                    player = (new AdvancedPlayer(audioStream));
//                    player.play(); // 播放音频
//                } catch (JavaLayerException e){
//                    throw new RuntimeException(e);
//                }
//            }).start();
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
//        Audio audio = Audio.create(Vars.musicDirectory.child("bgm_menu.mp3"));
//        audio.start();



        //60fps=33ms
        Time.task(33, () -> {
            renderer.repaint();//相当于Vars.sceneManager.on_draw();

            //Long end = new Date().getTime();
            Vars.sceneManager.on_update(33);
            //begin = end;

            Vars.sceneManager.on_input(keySet);
        });



        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keySet.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keySet.remove(e.getKeyCode());
            }
        });
    }

    //设置全局字体
    protected void initGlobalFontSetting(Font fnt){
        FontUIResource fontRes = new FontUIResource(fnt);
        for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }


    public static class Renderer extends JPanel{
        private SceneManager manager;

        private Image image;//缓冲

        public Renderer(SceneManager manager) {
            this.manager = manager;
        }

        //TODO 不知道怎么搞的就解决图片停留的问题了
        @Override
        public void paint(Graphics g) {
            image = createImage(this.getWidth(), this.getHeight());
            manager.on_draw(image.getGraphics(), mainCamera);
            g.drawImage(image, 0, 0, this);
        }
    }
}

