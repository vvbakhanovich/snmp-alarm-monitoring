package alarm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Sound player based on javax.sound library. Sound file must have .wav extension.
 */
@Component
@Slf4j
public class AudioAlarm implements Alarm {

    private Clip clip;
    @Value("${alarmPath}")
    private String alarmPath;

    /**
     * Accepts path to sound file. Path should be relative to System.getProperty("user.dir").
     */
    @PostConstruct
    public void init() {
        try {
            final File alarm = new File(System.getProperty("user.dir"), alarmPath);
            final AudioInputStream stream = AudioSystem.getAudioInputStream(alarm);
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (Exception e) {
            log.error("Ошибка при чтении звукового файла из директории {}. {}", alarmPath, e.getMessage());
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
