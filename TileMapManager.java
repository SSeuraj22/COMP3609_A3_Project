import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;
import java.io.*;
import java.awt.geom.AffineTransform;


public class TileMapManager {
    //Variables
    private ArrayList<Image> tilesImages; //consists of the images for the tiles
    private JFrame window;
    private GraphicsConfiguration gc;

    //Constructor
    public TileMapManager(JFrame win){
        window = win;
        loadTileImages();
    }

    //gets an image from Images folder
    public Image loadImage(String fName){//loads one tile image
        String fileName = "Images/Tiles/" + fName;

        File f = new File(fileName);

        if(!f.exists()){
            System.out.println("Error opening image file " + fileName + "...");
        }
        else{
            //System.out.println("The image file " + fileName + " opened...");
            return new ImageIcon(fileName).getImage();
        }
        return null;
    }

    //to load the images of the tiles from the images folder
    public void loadTileImages(){
        tilesImages = new ArrayList<Image>();
        char ch = 'A';
        File fileImg;

        while(true){
            String fileName = "Images/Tiles/" + ch + ".png";
            fileImg = new File(fileName); //create a file with the filename
            if(!fileImg.exists()){//check to see if that file exists
                System.out.println("Error opening image file " + fileName + "...");
                break; //exit while loop
            }
            else{//file exists
                //System.out.println(fileName + " image file has opened...");
                Image tImage = new ImageIcon(fileName).getImage(); //get image for the tile
                tilesImages.add(tImage); //add image to arraylist
            }
            ch++;
        }
    }

    //Load tile map with input from text file
    public TileMap loadTileMap(String fileName, int tileNumRow) throws IOException{
        FileReader fr = new FileReader(fileName);//create a file reader for name of the file
        BufferedReader buffRead = new BufferedReader(fr);

        ArrayList<String> linesInText = new ArrayList<String>(); //lines in the textfile with the tiles
        int mapWidth = 0;
        int mapHeight = 0;

        //read every line in the text file into the list
        while(true){
            String line = buffRead.readLine();

            if(line==null){//if there is no more lines to read in
                buffRead.close();
                break; //exit while loop
            }

            if(!line.startsWith("#")){//only add the lines without the "#" at the beginning to the textfile
                linesInText.add(line);
                mapWidth = Math.max(mapWidth, line.length());
                //System.out.println(mapWidth);
            }
        }

        mapHeight = linesInText.size(); 
        //System.out.println(mapHeight);

        TileMap newTMap = new TileMap(window, mapWidth, mapHeight, tileNumRow);

        for(int y=0; y<mapHeight; y++){//traverse through linesInText arraylist. y is rows
            String line = linesInText.get(y);
            for(int x=0; x<line.length(); x++){//traverse through the line. x is column
                char ch = line.charAt(x); //get the char value in the line at the specific index

                int tileNum = ch - 'A';
                if(tileNum>=0 && tileNum<tilesImages.size()){
                    newTMap.setTile(x, y, tilesImages.get(tileNum));
                }
            }
        }
        return newTMap;
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
