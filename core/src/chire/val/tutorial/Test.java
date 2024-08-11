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
            //��ȡ��Ƶ������
            audioStream = AudioSystem.getAudioInputStream(new File(path));
            //��ȡ��Ƶ��ʽ
            audioFormat = audioStream.getFormat();

            System.out.println("��Ƶ�ļ�: "+path);
            System.out.println("��ƵEncoding: "+audioFormat.getEncoding());

            //�������wav��ʽ��ת��mp3�ļ����롣MPEG1L3��mp3��ʽ��תΪPCM_SIGNED��wav��ʽ��
            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        audioFormat.getSampleRate(), 16, audioFormat
                        .getChannels(), audioFormat.getChannels() * 2,
                        audioFormat.getSampleRate(), false);

                audioStream = AudioSystem.getAudioInputStream(audioFormat,
                        audioStream);
            } //ת��mp3�ļ��������
            //��װ��Ƶ��Ϣ
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
                    audioFormat,AudioSystem.NOT_SPECIFIED);
            //��ȡ������������SourceDataLine��ʵ��
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            //������Ƶ
            while((count = audioStream.read(buf,0,buf.length)) != -1){
                sourceDataLine.write(buf,0,count);
            }
            //���Ž������ͷ���Դ
            sourceDataLine.drain();
            sourceDataLine.close();
            audioStream.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



    public static void mirror(BufferedImage bufImage) throws IOException {
        // ��ȡͼƬ�Ŀ��
        final int width = bufImage.getWidth();
        final int height = bufImage.getHeight();

        // ��ȡ��ͼƬ����������
        int[] rgbs = bufImage.getRGB(0, 0, width, height, null, 0, width);

        // ��ͼƬ�����ؾ������ˮƽ����
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width / 2; col++) {
                int temp = rgbs[row * width + col];
                int i = row * width + (width - 1 - col);
                rgbs[row * width + col] = rgbs[i];
                rgbs[i] = temp;
            }
        }

        // ��ˮƽ���������ؾ������û� bufImage
        bufImage.setRGB(0, 0, width, height, rgbs, 0, width);
        System.out.println(bufImage);
        ImageIO.write(bufImage, "PNG", new File("F:\\TutorialZero\\assets\\����.png"));
    }

    public static BufferedImage getImage(CRFile file){
        try {
            return ImageIO.read(file.getLocalStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
