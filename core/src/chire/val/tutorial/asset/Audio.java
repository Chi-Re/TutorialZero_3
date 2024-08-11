package chire.val.tutorial.asset;

import chire.val.tutorial.io.CRFile;
import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.io.InputStream;

/**
 * 使用{@link javazoom.jl.player.advanced.AdvancedPlayer}来创建的音频库，进行小部分重构(继承无法解决关闭播放的问题)
 * @author 炽热S
 */
public class Audio {
    private Runnable player;
    private boolean stop = false;

    private Bitstream bitstream;

    private CRFile file;

    private Decoder decoder;

    private AudioDevice audio;

    private boolean closed = false;

    private boolean complete = false;

    private int lastPosition = 0;

    private Audio(CRFile file) throws IOException, JavaLayerException {
        this.file = file;
        bitstream = new Bitstream(file.getLocalStream().openStream());
        audio = FactoryRegistry.systemRegistry().createAudioDevice();
        audio.open(decoder = new Decoder());

        player = this::play;
    }

    public static Audio create(CRFile file){
        try {
            return new Audio(file);
        } catch (IOException | JavaLayerException e){
            throw new RuntimeException(e);
        }
    }

    public void play() {
        play(Integer.MAX_VALUE);
    }

    public void play(int frames) {
        if (closed) return;
        for (int i = lastPosition; i < frames; i++) {
            if (stop) return;
            decodeFrame();
            System.out.println(stop);
        }
    }

    public void start(){
        if (closed) {
            return;
        }
        stop = false;
        new Thread(player).start();
    }

    public void stop(){
        if (closed) return;
        stop = true;
    }

    public synchronized void close(){
        if (closed) return;
        try {
            stop = true;
            closed = true;

            bitstream.close();
            audio.close();
            lastPosition = 0;
        } catch (BitstreamException e){
            throw new RuntimeException(e);
        }
    }


    protected void decodeFrame(){
        if (closed) return;
        try{
            AudioDevice out = audio;
            if (out == null) {
                close();
                return;
            };

            Header h = bitstream.readFrame();
            if (h == null) {
                close();
                return;
            }

            // sample buffer set when decoder constructed
            SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);

            synchronized (this) {
                out = audio;
                if(out != null)
                {
                    out.write(output.getBuffer(), 0, output.getBufferLength());
                }
            }

            bitstream.closeFrame();
            lastPosition++;
        } catch (RuntimeException | JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }
}
