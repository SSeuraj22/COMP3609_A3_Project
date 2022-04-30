import javax.swing.JFrame;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.Graphics2D;

public class Animation {
    //Declare variables
    private JFrame gWindow; //JFrame that the animation will be displayed on
    private ArrayList<AnimFrame> framesAnim; //collection of frames for the animation
    private int currFrameIndex; //current frame being displayed
    private long animTime; //time that the animation has run for already
    private long startTime; //start time of the animation or time since last update
    private long totalDuration; //total duration of the animation
    private int x;
    private int y;
    private int dx; //increment to move along x-axis
    private int dy; //increment to move along y-axis
    private int width; //width of image for animation
    private int height; //height of image for animation

    //Constructor
    public Animation(JFrame win){
        gWindow = win;
        //this.x = x;
        //this.y = y;
        //this.width = width;
        //this.height = height;
        framesAnim = new ArrayList<AnimFrame>();
        totalDuration = 0;
        start(x, y);
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void setDX(int dx){
        this.dx = dx;
    }

    public void setDY(int dy){
        this.dy = dy;
    }

    public int getAnimWidth(){
        int widthAnim = 934;
        int count = 0;
        for(int i=0;i<framesAnim.size(); i++){
            Image image = framesAnim.get(0).img;
            if(image.getWidth(null)==widthAnim){
                count++;
            }
        }
        if(count==framesAnim.size()){//all the images have the same width
            return widthAnim;
        }
        return 0;//all the images dont have the same width
    }

    public int getAnimHeight(){
        int heightAnim = 641;
        int count = 0;
        for(int i=0;i<framesAnim.size(); i++){
            Image image = framesAnim.get(0).img;
            if(image.getHeight(null)==heightAnim){
                count++;
            }
        }
        if(count==framesAnim.size()){//all the images have the same height
            return heightAnim;
        }
        return 0;//all the images dont have the same height
    }

    //inner class for the frames of the animation
    private class AnimFrame{
        Image img;
        long endTime;

        public AnimFrame(Image i, long endTime){
            this.img = i;
            this.endTime = endTime;
        }
    }

    //Adds an image to the animation with the specified duration (time to display the image).
    public synchronized void addFrame(Image img, long duration) {
        totalDuration += duration;
        framesAnim.add(new AnimFrame(img, totalDuration));
    }

    //Starts this animation over from the beginning.
    public synchronized void start(int x, int y){
        this.x = x;
        this.y = y;
        animTime = 0; //reset the time that the animation has run for to 0
        currFrameIndex = 0; // reset current frame to first frame
        startTime = System.currentTimeMillis(); // reset the start time to current time
    }

    //Updates this animation's current image (frame), if neccesary.
    public synchronized void update(){
        long currTime = System.currentTimeMillis();// get the current time
        long elapsedTime = currTime - startTime;// get how much time has elapsed since last update
        startTime = currTime;// set start time to current time

        if(framesAnim.size()>1){
            animTime += elapsedTime; // add elapsed time to amount of time animation has run for
            if(animTime>=totalDuration){// if the time animation has run for > total duration
                animTime = animTime % totalDuration;// reset time animation has run for
                currFrameIndex = 0;// reset current frame to first frame
            }

            while(animTime>getFrame(currFrameIndex).endTime){
                currFrameIndex++;
            }
        }
        //x = x + dx;
    }

    //Gets this Animation's current image. Returns null if this animation has no images.
    public synchronized Image getImage(){
        if(framesAnim.size()==0){
            return null;
        }
        else{
            return getFrame(currFrameIndex).img;
        }
    }

    // returns ith frame in the collection
    private AnimFrame getFrame(int i){
        return framesAnim.get(i);
    }

    // draw the current frame on the JPanel
    public void draw(Graphics2D graph2){
        graph2.drawImage(getImage(), x, y, width, height, null);
    }
}
