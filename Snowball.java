import java.awt.*;
import javax.swing.JFrame;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;

public class Snowball extends Rectangle{

    int vel = 15, initX;

    Animation snow;
    Animation impact;
    Animation curr;

    JFrame j;

    boolean hit = false, moving = false;


    public Snowball(int x, int y, JFrame j) {
        
        super(x, y, 50, 50);
        initX = x;
        this.j = j;

        snow = new Animation(j);
        impact = new Animation(j);
        curr = new Animation(j);

        loadAnim();
    }

    public Image loadImage(String fileName){
        Image im = new ImageIcon(fileName).getImage();
        return im;
    }

    public void loadAnim(){
        Image sball1 = loadImage("Images/Snowball/snowball_01.png");
        Image sball2 = loadImage("Images/Snowball/snowball_02.png");
        Image sball3 = loadImage("Images/Snowball/snowball_03.png");
        Image sball4 = loadImage("Images/Snowball/snowball_04.png");
        // Image impact1 = loadImage("Images/Santa/Idle/Idle1.png");
        // Image impact2 = loadImage("Images/Santa/Idle/Idle1.png");
        // Image impact3 = loadImage("Images/Santa/Idle/Idle1.png");

        
        snow.addFrame(sball1, 100);
        snow.addFrame(sball2, 100);
        snow.addFrame(sball3, 100);
        snow.addFrame(sball4, 100);

        // impact.addFrame(impact1, 100);
        // impact.addFrame(impact2, 100);
        // impact.addFrame(impact3, 100);
    }

    public void move(){
        if(moving && !hit){
            this.x = this.x + vel;
            curr = snow;
        }
        // if(hit){
        //     curr = impact;
        // }
        if(x > 1920){
            moving = false;
            hidesnow();
        }
    }

    public void update(){
        snow.update();
        // impact.update();
        // if(hit){
        //     curr = impact;
        //     curr.start();
        // }

        if(hit){
            hit = false;
            moving = false;
        }
        move();
    }

    public void draw(Graphics2D g){
        g.drawImage(curr.getImage(), x, y, width, height, null);

    }

    public void hidesnow(){
        this.y = -500;
        this.x = -5000;
    }
    
}   