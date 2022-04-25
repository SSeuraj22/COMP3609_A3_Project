import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Background {
    //initialize variable
    private JFrame windowFrame;
    private int bgX;
    private int backgroundDX; //number to move background by
    private Image backImage;
    private int backImageWidth; 
    private Dimension dimension;
    private int background1X;
    private int background2X;

    public Background(JFrame win, String imageFile, int bgDX){
        windowFrame = win;
        backImage = loadImage(imageFile);
        backImageWidth = backImage.getWidth(null);
        //System.out.println ("bgImageWidth = " + backImageWidth);

        dimension = windowFrame.getSize();
        System.out.println ("Window Width = " + dimension.width);
        System.out.println ("Window Height = " + dimension.height);
        
        if(backImageWidth<dimension.width){
            System.out.println ("Background Width < panel width ");
        }
        this.backgroundDX = bgDX;
    }

    public Image loadImage(String fileName){
        Image im = new ImageIcon(fileName).getImage();
        return im;
    }

    public void draw(Graphics2D graph2){
        graph2.drawImage(backImage, background1X, 0, null);
        graph2.drawImage(backImage, background2X, 0, null);
    }

    public void moveRight(){ //to move background right
        if (bgX==0) {
            background1X = 0;
            background2X = backImageWidth;            
        }

        bgX = bgX - backgroundDX;
        background1X = background1X - backgroundDX;
        background2X = background2X - backgroundDX;

        if((bgX + backImageWidth) % backImageWidth == 0){
            System.out.println ("Background change: bgX = " + bgX); 
            background1X = 0;
            background2X = backImageWidth;
        }
    }

    public void moveLeft(){ //to move background left
        if (bgX==0) {
            background1X = backImageWidth * -1;
            background2X = 0;            
        }

        bgX = bgX + backgroundDX;
        background1X = background1X + backgroundDX;
        background2X = background2X + backgroundDX;

        if((bgX + backImageWidth) % backImageWidth == 0){
            System.out.println ("Background change: bgX = " + bgX); 
            background1X = backImageWidth * -1;
            background2X = 0;
        }
    }
}
