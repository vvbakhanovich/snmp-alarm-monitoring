package alarm;

/**
 * Interface for sound alarm playback.
 */
public interface Alarm {
    /**
     * Start alarm playback
     */
    void playAudio();

    /**
     * End alarm playback
     */
    void stopAudio();
}
