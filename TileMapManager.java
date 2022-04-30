import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;
import java.io.*;


public class TileMapManager {
    //Variables
    private ArrayList<Image> tilesImages; //consists of the images for the tiles
    private JFrame window;

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
            System.out.println("The image file " + fileName + " opened...");
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
                System.out.println(fileName + " image file has opened...");
                Image tImage = new ImageIcon(fileName).getImage(); //get image for the tile
                tilesImages.add(tImage); //add image to arraylist
            }
            ch++;
        }
    }

    //Load tile map with input from text file
    public TileMap loadTileMap(String fileName) throws IOException{
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
            }
        }

        mapHeight = linesInText.size(); 

        TileMap newTMap = new TileMap(window, mapWidth, mapHeight);

        for(int y=0; y<mapHeight; y++){//traverse through linesInText arraylist
            String line = linesInText.get(y);
            for(int x=0; x<line.length(); x++){//traverse through the line
                char ch = line.charAt(x); //get the char value in the line at the specific index

                int tileNum = ch - 'A';
                if(tileNum>=0 && tileNum<tilesImages.size()){
                    newTMap.setTile(x, y, tilesImages.get(tileNum));
                }
            }
        }
        return newTMap;
    }
}
