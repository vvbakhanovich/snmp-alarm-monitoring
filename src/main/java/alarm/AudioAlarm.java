package alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class AudioAlarm implements Alarm {
    private static final Logger log = LoggerFactory.getLogger(AudioAlarm.class);

    private final Clip clip;

    public AudioAlarm(final String alarmPath) {
        try {
            final File alarm = new File(System.getProperty("user.dir"), alarmPath);
            final AudioInputStream stream = AudioSystem.getAudioInputStream(alarm);
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (Exception e) {
            log.error("Ошибка при чтении звукового файла. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playAudio() {
        try {
            clip.setMicrosecondPosition(0);
            clip.start();
        } catch (Exception e) {
            log.error("Ошибка при попытке проиграть звуковой файл. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopAudio() {
        try {
            clip.stop();
        } catch (Exception e) {
            log.error("Ошибка при попытке остановить проигрывание звукового файла. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
