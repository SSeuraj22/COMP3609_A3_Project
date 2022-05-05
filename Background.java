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
    //private int backImageHeight; 
    private Dimension dimension;
    private int background1X;
    private int background2X;

    public Background(JFrame win, String imageFile, int bgDX){
        windowFrame = win;
        backImage = loadImage(imageFile);
        backImageWidth = backImage.getWidth(null);
        //backImageHeight = backImage.getHeight(null);
        //System.out.println ("bgImageWidth = " + backImageWidth);
        //System.out.println ("bgImageHeight = " + backImageHeight);

        dimension = windowFrame.getSize();
        //System.out.println ("Window Width = " + dimension.width);
        //System.out.println ("Window Height = " + dimension.height);
        
        if(backImageWidth<dimension.width){
            //System.out.println ("Background Width < panel width ");
        }
        this.backgroundDX = bgDX;
    }

    public int getBgWidth(){
        return backImageWidth;
    }

    public Image loadImage(String fileName){
        Image im = new ImageIcon(fileName).getImage();
        return im;
    }

    public void draw(Graphics2D graph2, int width, int height){
        graph2.drawImage(backImage, background1X, 0, width, height, null);
        graph2.drawImage(backImage, background2X, 0, width, height, null);
    }

    public void moveRight(){ 
        if (bgX==0) {
            background1X = 0;
            background2X = backImageWidth;            
        }

        bgX = bgX - backgroundDX;
        background1X = background1X - backgroundDX;
        background2X = background2X - backgroundDX;

        //String mess = "Right: bgX=" + bgX + " bgX1=" + background1X + " bgX2=" + background2X;
        //System.out.println (mess);

        if((bgX + backImageWidth) % backImageWidth == 0){
            //System.out.println ("Background change: bgX = " + bgX); 
            background1X = 0;
            background2X = backImageWidth;
        }
    }

    public void moveLeft(){ 
        if (bgX==0) {
            background1X = backImageWidth * -1;
            background2X = 0;            
        }

        bgX = bgX + backgroundDX;
        background1X = background1X + backgroundDX;
        background2X = background2X + backgroundDX;

        //String mess = "Left: bgX=" + bgX + " bgX1=" + background1X + " bgX2=" + background2X;
        //System.out.println (mess);

        if((bgX + backImageWidth) % backImageWidth == 0){
            //System.out.println ("Background change: bgX = " + bgX); 
            background1X = backImageWidth * -1;
            background2X = 0;
        }
    }
}
