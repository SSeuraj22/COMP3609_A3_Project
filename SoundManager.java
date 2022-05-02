import java.util.HashMap;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;        // for playing sound clips
import javax.sound.sampled.*;
import java.io.*;

public class SoundManager {
    HashMap<String, Clip> clips;
    private static SoundManager instance = null;
    
    Clip backgroundClip = null; //plays continuously

    private SoundManager(){
        clips = new HashMap<String, Clip>();

        Clip clip = loadClip("sounds/background.wav");
        clips.put("background", clip);

        clip = loadClip("sounds/menubg.wav");
        clips.put("menu", clip);

        clip = loadClip("sounds/jump.wav");
        clips.put("jump", clip);

        clip = loadClip("sounds/win.wav");
        clips.put("win", clip);

        clip = loadClip("sounds/die.wav");
        clips.put("die", clip);

        clip = loadClip("sounds/lose.wav");
        clips.put("lose", clip);

    }

    public static SoundManager getInstance() {
        if(instance == null)
            instance = new SoundManager();

        return instance;
    }

    public Clip loadClip (String fileName) {    // gets clip from the specified file
        AudioInputStream audioIn;
        Clip clip = null;

        try {
            File file = new File(fileName);
            audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }
        catch (Exception e) {
            System.out.println ("Error opening sound files: " + e);
        }
            return clip;
    }

    public Clip getClip (String title) {
        return clips.get(title);
    }

    public void playClip(String title, Boolean looping) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (looping)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
        }
    }

    public void stopClip(String title) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.stop();
        }
    }
}
