package chire.val.tutorial;

import chire.val.tutorial.io.CRFile;
import chire.val.tutorial.io.FileType;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Test {
    public static CRFile spritesDirectory = new CRFile("sprites", FileType.internal);

    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceDataLine;

    public static void main(String[] args) {
        //new Test("F:\\TutorialZero\\assets\\sounds\\sun_explode.mp3");
    }

    public Test(String path) {
        playMusic(path);
    }

    private void playMusic(String path){
        try{
            int count;
            byte buf[] = new byte[2048];
            //获取音频输入流
            audioStream = AudioSystem.getAudioInputStream(new File(path));
            //获取音频格式
            audioFormat = audioStream.getFormat();

            System.out.println("音频文件: "+path);
            System.out.println("音频Encoding: "+audioFormat.getEncoding());

            //如果不是wav格式，转换mp3文件编码。MPEG1L3（mp3格式）转为PCM_SIGNED（wav格式）
            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        audioFormat.getSampleRate(), 16, audioFormat
                        .getChannels(), audioFormat.getChannels() * 2,
                        audioFormat.getSampleRate(), false);

                audioStream = AudioSystem.getAudioInputStream(audioFormat,
                        audioStream);
            } //转换mp3文件编码结束
            //封装音频信息
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
                    audioFormat,AudioSystem.NOT_SPECIFIED);
            //获取虚拟扬声器（SourceDataLine）实例
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            //播放音频
            while((count = audioStream.read(buf,0,buf.length)) != -1){
                sourceDataLine.write(buf,0,count);
            }
            //播放结束，释放资源
            sourceDataLine.drain();
            sourceDataLine.close();
            audioStream.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



    public static void mirror(BufferedImage bufImage) throws IOException {
        // 获取图片的宽高
        final int width = bufImage.getWidth();
        final int height = bufImage.getHeight();

        // 读取出图片的所有像素
        int[] rgbs = bufImage.getRGB(0, 0, width, height, null, 0, width);

        // 对图片的像素矩阵进行水平镜像
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width / 2; col++) {
                int temp = rgbs[row * width + col];
                int i = row * width + (width - 1 - col);
                rgbs[row * width + col] = rgbs[i];
                rgbs[i] = temp;
            }
        }

        // 把水平镜像后的像素矩阵设置回 bufImage
        bufImage.setRGB(0, 0, width, height, rgbs, 0, width);
        System.out.println(bufImage);
        ImageIO.write(bufImage, "PNG", new File("F:\\TutorialZero\\assets\\镜像.png"));
    }

    public static BufferedImage getImage(CRFile file){
        try {
            return ImageIO.read(file.getLocalStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
