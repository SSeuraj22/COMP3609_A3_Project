import java.awt.*;
import javax.swing.JFrame;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

public class Snowball{

    int dx = 15;
    int x, y;
    private Animation snowball;
    private int height = 50;
    private int width = 50;
    private TileMap tileMap;
    private JFrame j;
    private Santa san;
    private GraphicsConfiguration gc;

    boolean hit = false, moving = false;


    public Snowball(JFrame j, Santa s){
        //this.x = x;
        //this.y = y;
        this.j = j;
        san = s;
        x = san.getX() + san.getWidth() + 5;
        y = san.getY() + 5;
        snowball = new Animation(j);
        
        loadAnimSnowball();
        snowball.setWidth(width);
        snowball.setHeight(height);
    }

    public Image loadImage(String fileName){
        Image im = new ImageIcon(fileName).getImage();
        return im;
    }

    public void loadAnimSnowball(){
        Image sball1 = loadImage("Images/Snowball/snowball_01.png");
        Image sball2 = loadImage("Images/Snowball/snowball_02.png");
        Image sball3 = loadImage("Images/Snowball/snowball_03.png");
        Image sball4 = loadImage("Images/Snowball/snowball_04.png");
        Image sball5 = loadImage("Images/Snowball/snowball_05.png");
        Image sball6 = loadImage("Images/Snowball/snowball_06.png");
        
        snowball.addFrame(sball1, 100);
        snowball.addFrame(sball2, 100);
        snowball.addFrame(sball3, 100);
        snowball.addFrame(sball4, 100);
        snowball.addFrame(sball5, 100);
        snowball.addFrame(sball6, 100);
    }

    public void moveRight(){
        x = x + dx;
        
    }

    public void moveLeft(){
        x = x - dx;
    }

    public void update(){
        snowball.update();
    }

    public Rectangle2D.Double getBoundingRectangle(int x, int y, int width, int height){
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void draw(Graphics2D g){
        snowball.draw(g, x, y);
    }

    public void setTileMap(TileMap tm){
        tileMap = tm;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void hidesnow(){
        this.y = -500;
        this.x = -5000;
    }

    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }

    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }

    private Image getScaledImage(Image image, float x, float y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
    
}   