import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {

    // Class-level reference keeps track of the playing background music
    private static Clip backgroundMusicClip = null;

    public static void playMusic(String filePath) {
        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                // Prevent multiple background tracks from overlapping
                stopMusic();

                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                
                // Assign to the class-level variable instead of a local variable
                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioInput);
                
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusicClip.start();
            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Instantly ends the background music track
    public static void stopMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();   // Halt audio playback immediately
            backgroundMusicClip.close();  // Free system audio hardware resources
            backgroundMusicClip = null;   // Clear reference for safety
        }
    }

    // Sound effects should run on their own brief, non-looping local clips
    public void playSoundEffect(String filePath) {
        try {
            File soundPath = new File(filePath);
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip effectClip = AudioSystem.getClip();
                
                effectClip.open(audioInput);
                effectClip.start(); // Plays exactly once without looping
                
                // Optional: Automatically release resources when the sound finish playing
                effectClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        effectClip.close();
                    }
                });
            } else {
                System.out.println("Can't find sound effect file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
