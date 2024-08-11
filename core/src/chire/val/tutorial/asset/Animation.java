package chire.val.tutorial.asset;

import java.awt.*;

public class Animation {
    private int timer = 0;
    private int interval = 0;
    private int idxFrame = 0;
    private boolean isLoop = true;
    private Atlas atlas;

    private Runnable callback;

    public Animation(){
    }

    public void reset(){
        timer = 0;
        idxFrame = 0;
    }

    public void setAtlas(Atlas new_atlas){
        reset();
        atlas = new_atlas;
    }

    public void setLoop(boolean flag){
        isLoop = flag;
    }

    public void setInterval(int ms){
        interval = ms;
    }

    public int getIdxFrame() {
        return idxFrame;
    }

    public Image getFrame(){
        return atlas.getImage(idxFrame);
    }

    public boolean checkFinished(){
        if (isLoop) return false;

        return (idxFrame == atlas.getSize()-1);
    }

    public void update(int delta){
        timer += delta;
        if (timer >= interval) {
            timer = 0;
            idxFrame++;
            if (idxFrame >= atlas.getSize()){
                idxFrame = isLoop ? 0 : atlas.getSize()-1;

                if (!isLoop && callback != null) callback.run();
            }
        }
    }

    public void onDraw(Graphics g, int x, int y){
        g.drawImage(getFrame(), x, y, null);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
}
