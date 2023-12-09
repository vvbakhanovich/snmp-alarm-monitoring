package alarm;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class AudioAlarm implements Alarm {
    private final Clip clip;
    private String filePath = "src/main/resources/alarm.wav";

    public AudioAlarm() {
        try {
            File alarm = new File(filePath);
            AudioInputStream stream = AudioSystem.getAudioInputStream(alarm);
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playAudio() {
        try {
            clip.setMicrosecondPosition(0);
            clip.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void stopAudio() {
        try {
            clip.stop();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
