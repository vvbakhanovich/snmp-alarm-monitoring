package alarm;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.InputStream;

public class SoundPlayer  implements LineListener {

    boolean isPlaybackCompleted;
    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
            System.out.println("Playback started");
        } else if (LineEvent.Type.STOP == event.getType()) {
            isPlaybackCompleted = true;
            System.out.println("Playback completed");
        }
    }

    InputStream is = getClass().getClassLoader().getResourceAsStream("src/main/resources/alarm.wav");
    AudioInputStream audioInputStream;

    {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String path = "src/main/resources/alarm.wav";
        playMusic(path);
        JOptionPane.showMessageDialog(null, "Stop");

    }

    public static void playMusic(String location) {
        try {
            File file = new File(location);

            if (file.exists()) {
                AudioInputStream audioInputStream1 = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream1);
                clip.start();
            } else {
                System.out.println("Cant read file");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
