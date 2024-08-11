package chire.val.tutorial.asset;

import chire.val.tutorial.Vars;
import chire.val.tutorial.io.CRFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class Atlas {
    private final ArrayList<BufferedImage> imgList = new ArrayList<>();

    public Atlas(CRFile directory, String fileName, String suffix, int num){
        for (int n = 1; n < (num+1); n++) {
            addImage(getFileImage(directory.child(fileName+"_"+n+"."+suffix)));
        }
    }

    public Atlas(String fileName, int num){
        this(Vars.spritesDirectory, fileName, "png", num);
    }

    public Atlas(){
    }

    public void clear(){
        imgList.clear();
    }

    public int getSize(){
        return imgList.size();
    }

    public BufferedImage getImage(int idx){
        if (idx < 0 || idx >= imgList.size()) return null;

        return imgList.get(idx);
    }

    public void addImage(BufferedImage image){
        imgList.add(image);
    }


    /**�����ڲ����ⲿͼƬ(TODO ��ʱ����뵽CRFile)*/
    public static BufferedImage getFileImage(CRFile file){
        try {
            return ImageIO.read(file.getLocalStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Atlas flipAtlas(Atlas source){
        var dst = new Atlas();
        dst.clear();

        for (int s = 0; s < source.getSize(); s++) {
            dst.addImage(flipImage(source.getImage(s)));
        }

        return dst;
    }

    public static BufferedImage flipImage(final BufferedImage bufferedimage) {
        // ��ȡͼƬ�Ŀ��
        final int width = bufferedimage.getWidth();
        final int height = bufferedimage.getHeight();

        // ��ȡ��ͼƬ����������
        int[] rgbs = bufferedimage.getRGB(0, 0, width, height, null, 0, width);

        // ��ͼƬ�����ؾ������ˮƽ����
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width / 2; col++) {
                int temp = rgbs[row * width + col];
                int i = row * width + (width - 1 - col);
                rgbs[row * width + col] = rgbs[i];
                rgbs[i] = temp;
            }
        }

        BufferedImage returnImg = new BufferedImage(width, height, bufferedimage.getType());
        returnImg.setRGB(0, 0, width, height, rgbs, 0, width);
        return returnImg;
    }
}
