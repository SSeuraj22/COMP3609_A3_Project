import java.awt.Image;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
// import java.awt.Color;

public class TileMap {
    //Variables
    private Image[][] tiles; //2D array for tiles
    private int mapWidth, mapHeight;//2D array size
    private JFrame window;
    private Dimension dimension; //for screen dimensions
    private Santa santa;
    private int offsetX, offsetY;

    private static final int TILE_SIZE = 64; //size square

    //Constructor
    public TileMap(JFrame win, int mWidth, int mHeight){
        window = win;
        mapWidth = mWidth;
        mapHeight = mHeight;
        tiles = new Image[mWidth][mHeight];
        dimension = window.getSize();
        santa = new Santa(window, this);

        Animation santaAnim = santa.getAnimation();
        int santaHeight = santaAnim.getHeight();
        System.out.println("Santa Height: " + santaHeight);

        //to set santa coordinates on the screen for placement
        int x = 192;
        int y = dimension.height - (TILE_SIZE + santaHeight);
        santa.setX(x);
        santa.setY(y);
        santa.setFloorY(y);
    }

    //To set a tile at a specific position in the 2D array
    public void setTile(int x, int y, Image tile){
        tiles[x][y] = tile;
    }

    //Get the tile at the specified position. 
    public Image getTile(int x, int y){
        if(x<0 || y<0 || x>=mapWidth || y>=mapHeight){//if x and y location is out of bounds
            return null;
        }
        else{//returns null if there is no tile at the specified position
            return tiles[x][y];
        }
    }

    //To convert the tiles position to pixels position
    public static int tilesToPixels(int numTiles){
        int pix = numTiles * TILE_SIZE;
        return pix;
    }

    //To convert the pixels position to tiles position (int pixels)
    public static int pixelsToTiles(int pixels){
        int tile = (int)Math.floor((float)pixels / TILE_SIZE);
        return tile;
    }

    //To convert the pixels position to tiles position (float pixels)
    public static int pixelsToTiles(float pixels){
        int tile = pixelsToTiles(Math.round(pixels));
        return tile;
    }

    //Gets the width of this TileMap (number of pixels across).
    public int getMapWidthPixels(){
        int mapWidthPix = tilesToPixels(mapWidth);
        return mapWidthPix;
    }

    //Gets the height of this TileMap (number of pixels down).
    public int getMapHeightPixels(){
        int mapHeightPix = tilesToPixels(mapHeight);
        return mapHeightPix;
    }

    //Gets the width of this TileMap (number of tiles across).
    public int getMapWidth(){
        return mapWidth;
    }

    //Gets the height of this TileMap (number of tiles down).
    public int getMapHeight(){
        return mapHeight;
    }

    public Rectangle2D.Double getBoundingSquare(int xTile, int yTile, int width, int height){
        Rectangle2D.Double tileSquare = new Rectangle2D.Double(xTile, yTile, width, height);
        return tileSquare;
    }

    //To draw the TileMap
    public void draw(Graphics2D g2){
        int mapWidthPixels = getMapWidthPixels();
        int screenWidth = dimension.width;
        int screenHeight = dimension.height;

        // get the scrolling position of the map based on player's position
        int xOffset = screenWidth/2 - Math.round(santa.getX()) - TILE_SIZE;
        xOffset = Math.min(xOffset, 0);
        xOffset = Math.max(xOffset, screenWidth-mapWidthPixels);
        offsetX = xOffset;

        //get the y offset to draw all sprites and tiles
        int yOffset = screenHeight - getMapHeightPixels();
        offsetY = yOffset;

        //Draw the visible tiles
        int xFirstTile = pixelsToTiles(-xOffset);//convert xOffset to tiles
        int xLastTile = xFirstTile + pixelsToTiles(screenWidth) + 1; //convert screenWidth to tiles

        for(int y=0; y<mapHeight; y++){//num of rows
            for(int x=xFirstTile; x<=xLastTile; x++){//num of columns
                Image tileImg = getTile(x, y); //get tile at that location
                if(tileImg!=null){
                    //convert tiles back to pixels and then draw them
                    g2.drawImage(tileImg, tilesToPixels(x) + xOffset, tilesToPixels(y) + yOffset, null);
                    //g2.setColor(Color.RED);
                    //Rectangle2D.Double rect = getBoundingSquare(tilesToPixels(x) + xOffset, tilesToPixels(y) + yOffset, tileImg.getWidth(null), tileImg.getHeight(null));
                    //g2.drawRect((int) (rect.getX()), (int) (rect.getY()), (int) (rect.getWidth()), (int) (rect.getHeight()));
                }
            }
        }
        //draw santa
        santa.draw(g2, Math.round(santa.getX()) + xOffset, Math.round(santa.getY()));
    }

    public int getOffsetX(){
        return offsetX;
    }

    public int getOffsetY(){
        return offsetY;
    }

    public void update(){
        santa.update();
    }

    public void moveLeft(){
        //int x;
        //x = santa.getX();
        //String mess = "Going left. x = " + x;
        //System.out.println(mess);
        santa.moveLeft();
    }

    public void moveRight(){
        //int x;
        //x = santa.getX();
        //String mess = "Going right. x = " + x;
        //System.out.println(mess);
        santa.moveRight();
    }

    public void moveJump(){
        santa.setJump();
        //santa.jumping();
    }

}
