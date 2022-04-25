import java.util.HashMap;
import javax.sound.sampled.Clip;

public class SoundManager {
    HashMap<String, Clip> clips;
    
    Clip backgroundClip = null; //plays continuously

    private SoundManager(){

    }
}
