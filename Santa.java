import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Image;
// import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;

public class Santa {
    //variables
    private int x, y; // x and y position of the Santa sprite
    
    private static final int SANTAWIDTH = 64; //width of Santa sprite
    private static final int SANTAHEIGHT = 64; //height of Santa sprite

    private static final int DX = 12; //amount to move Santa on the x axis
    private static final int DY = 10; //amount to move Santa on the y axis

    private JFrame window;
    private TileMap tileMap;

    //Animations for Santa
    private Animation santaAnimIdle; //animation sprite for idle santa
    private Animation santaAnimWalk; //animation sprite for walk santa
    private Animation santaAnimRun; //animation sprite for run santa
    private Animation santaAnimJump; //animation sprite for jump santa
    private Animation santaAnimSlide; //animation sprite for slide santa
    private Animation santaAnimDead; //animation sprite for dead santa

    private Animation playerAnim;
    private boolean isJumping;
    private int timeElapsed;
    private boolean jumpGoingUp;
    private boolean jumpGoingDown;
    int floorY;

    //constructor
    public Santa(JFrame jf, TileMap tm){
        window = jf;
        tileMap = tm;
        isJumping = false;
        timeElapsed = 0;

        santaAnimIdle = null;
        santaAnimWalk = null;
        santaAnimRun = null;
        santaAnimJump = null;
        santaAnimSlide = null;
        santaAnimDead = null;

        loadAnimSantaIdle();
        loadAnimSantaWalk();
        loadAnimSantaRun();
        loadAnimSantaJump();
        loadAnimSantaSlide();
        loadAnimSantaDead();

        santaAnimIdle.setWidth(SANTAWIDTH);
        santaAnimIdle.setHeight(SANTAHEIGHT);
        santaAnimIdle.setDX(DX);

        santaAnimWalk.setWidth(SANTAWIDTH);
        santaAnimWalk.setHeight(SANTAHEIGHT);
        santaAnimWalk.setDX(DX);

        santaAnimRun.setWidth(SANTAWIDTH);
        santaAnimRun.setHeight(SANTAHEIGHT);
        santaAnimRun.setDX(DX);

        santaAnimJump.setWidth(SANTAWIDTH);
        santaAnimJump.setHeight(SANTAHEIGHT);
        santaAnimJump.setDX(DX);

        santaAnimSlide.setWidth(SANTAWIDTH);
        santaAnimSlide.setHeight(SANTAHEIGHT);
        santaAnimSlide.setDX(DX);

        santaAnimDead.setWidth(SANTAWIDTH);
        santaAnimDead.setHeight(SANTAHEIGHT);
        santaAnimDead.setDX(DX);

        playerAnim = santaAnimIdle;

        x = (int) ((window.getWidth() - playerAnim.getAnimWidth())/2);
        y = 380;
    }

    public void draw(Graphics2D g2, int x, int y){
        playerAnim.setX(x);
        playerAnim.setY(y);
        playerAnim.draw(g2, x, y);
        Rectangle2D.Double animRect = playerAnim.getBoundingRectangle();
        g2.setColor(Color.RED);
        g2.drawRect((int) (x), (int) (y), SANTAWIDTH, SANTAHEIGHT);
    }

    public void update(){
        playerAnim.update();
        jumping();
    }

    public void setJump(){
        if (!window.isVisible ()){ 
            return;
        }
        isJumping = true;
        timeElapsed = 0;
        jumpGoingUp = true;
        jumpGoingDown = false;
    }

    public void jumping(){
        int distance;

        if(isJumping==true){//if jumping is false it wont go into the if statement and change the distance
            timeElapsed++;
            distance = (int) (80 * timeElapsed - 4.9 * timeElapsed * timeElapsed);

            if((floorY - distance) > y && jumpGoingUp==true){//if the y value starts to get smaller then we know the ball reaches the top
                jumpGoingUp = false;
                jumpGoingDown = true;
            }
            
            y = floorY - distance;

            int sanWidth = playerAnim.getWidth();
            //int tmPixWidth = tileMap.getMapWidthPixels();//tile map width in pixels
            int santaXPos = x + DX + sanWidth;
            int xAxisTiles = TileMap.pixelsToTiles(santaXPos);
            int yAxisTiles = TileMap.pixelsToTiles(y) - 1;
            Image tile = tileMap.getTile(xAxisTiles, yAxisTiles);
            int tileOffsetX = tileMap.getOffsetX();
            int tileOffsetY = tileMap.getOffsetY();
            if(tile!=null){//if there is no tiles on that position 
                if(collidesWithTile(TileMap.tilesToPixels(xAxisTiles) + tileOffsetX, TileMap.tilesToPixels(yAxisTiles) + tileOffsetY, tile.getWidth(null), tile.getHeight(null))){
                    int tileY, tileH, tileBottomY;
                    tileY = TileMap.tilesToPixels(yAxisTiles);
                    tileH = tile.getHeight(null);
                    tileBottomY = tileY + tileH;

                    if(jumpGoingUp==true){
                        System.out.println ("Jumping: Collision Going Up!");
                        System.out.println ("y of santa = " + y + " bottom y of tile = " + tileBottomY);
                        timeElapsed = advanceTime (timeElapsed + 1, distance);

                        jumpGoingUp = false;
                        jumpGoingDown = true;

                        y = tileY + tileH;
                        System.out.println ("new y of santa = " + y);
                    }
                    else
                        if(jumpGoingDown==true){
                            System.out.println ("Jumping: Collision Going Down!");
                            System.out.println ("y of santa = " + y + " top y of tile = " + tileY);
                            jumpGoingDown = false;
                            y = tileY - SANTAHEIGHT; //position the ball on top the wall that was hit on the way down
                            isJumping = false; //stop jumping
                        }
                }
                else{
                    System.out.println ("Jumping: No collision.");
                    System.out.println ("y of santa = " + y);
                }
            }
            else{
                y = y + DY;
                System.out.println ("Tile null...");
            }

            if(y > floorY){ //if the y coordinate passes the floor when coming down
                y = floorY;
                isJumping = false;
            }
        }
    }

    private int advanceTime(int currentTime, int distance){
        int s;
        while(true){
            s = (int) (80 * currentTime - 4.9 * currentTime * currentTime);
            if(s<distance){
                return currentTime;
            }
            else{
                currentTime++;
            }
        }
    }

    public boolean collidesWithTile(int x, int y, int width, int height){
        Rectangle2D.Double santaRect = getBoundingRectangle();
        Rectangle2D.Double tileRect = tileMap.getBoundingSquare(x, y, width, height);
        return santaRect.intersects(tileRect);
    }

    public boolean collideWithTile(){
        Rectangle2D.Double animRect = playerAnim.getBoundingRectangle();
        Image[][] tilesArray = tileMap.getTilesArray();
        int mapHeight = tileMap.getMapHeight();
        int mapWidth = tileMap.getMapWidth();
        System.out.println("Tiles array length/columns: " + mapWidth);//61
        System.out.println("Tiles array length/rows: " + mapHeight);//7
        for(int i=0; i<mapWidth; i++){
            for(int j=0; j<mapHeight; j++){
                Image im = tilesArray[i][j];
                if(im==null){
                    //System.out.println("at row " + j + " and col " + i + " is null...");
                }
                else{
                    System.out.println("at row " + j + " and col " + i + " is not null...");
                    int tileX = tileMap.tilesToPixels(x);
                    int tileY = tileMap.tilesToPixels(y);
                    Image tileImage = tilesArray[i][j];
                    int imgWidth = tileImage.getWidth(null);
                    int imgHeight = tileImage.getHeight(null);
                    Rectangle2D.Double tileRect = tileMap.getBoundingSquare(tileX, tileY, imgWidth, imgHeight);
                    boolean isCollided = animRect.intersects(tileRect);
                    if(isCollided==true){
                        System.out.println("Santa collided with tile at row " + j + " and col " + i);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void moveRight(){

        if (!window.isVisible ()){ 
            return;
        }
        //System.out.println(collideWithTile());
        
        int sanWidth = playerAnim.getWidth();
        System.out.println("Santa width: " + sanWidth);

        int tmPixWidth = tileMap.getMapWidthPixels();//tile map width in pixels
        System.out.println("Tile Map Width: " + tmPixWidth);
        
        int santaXPos = x + DX + sanWidth;
        System.out.println("Santa Position: " + santaXPos);

        if((santaXPos) <= tmPixWidth){//if player position will be <= tile map width
            x = x + DX;
            Point p = getTileCollision(this, santaXPos, y);
            if(p!=null){

                System.out.println("Point not null");
            }
            else{
                System.out.println("Point is null");
            }

            int xAxisTiles = tileMap.pixelsToTiles(santaXPos); //column num
            int yAxisTiles = tileMap.pixelsToTiles(y) - 2; //row num

            Image tileImg = tileMap.getTile(xAxisTiles, yAxisTiles);

            

            String mess = "Coordinates in TileMap: (" + xAxisTiles + "," + yAxisTiles + ")";
            System.out.println (mess);

            

            //if(tileImg==null){//if there is no tiles on that position 
                //x = x + DX;
                //System.out.println("tile null");
            //}
            //else{
                //x = x + DX;
                //boolean collide = collidesWithTile(tileMap.tilesToPixels(xAxisTiles), tileMap.tilesToPixels(yAxisTiles), tileImg.getWidth(null), tileImg.getHeight(null));
                //System.out.println(collide);
                //int tileXPix = tileMap.tilesToPixels(xAxisTiles);
                //x = tileXPix - sanWidth;
                //System.out.println("tile not null");
            //}

        }
    }

    public void moveLeft(){

        if (!window.isVisible ()){ 
            return;
        }

        //check if x is outside the left side of the tile map
        if ((x - DX) > 0){
            x = x - DX;
        }
        /*
        else{
            x = 0;
        }
        */
    }

    public Point getTileCollision(Santa s, float newX, float newY){
        float fromX = Math.min(s.getX(), newX);
        //System.out.println("fromX: " + fromX);
        float fromY = Math.min(s.getY(), newY);
        //System.out.println("fromY: " + fromY);
        float toX = Math.max(s.getX(), newX);
        //System.out.println("toX: " + toX);
        float toY = Math.max(s.getY(), newY);
        //System.out.println("toY: " + toY);

        //get the tile locations
        int fromTileX = TileMap.pixelsToTiles(fromX);
        //System.out.println("fromTileX: " + fromTileX);
        int fromTileY = TileMap.pixelsToTiles(fromY);
        //System.out.println("fromTileY: " + fromTileY);
        int toTileX = TileMap.pixelsToTiles(toX + s.getWidth() - 1);
        //System.out.println("toTileX: " + toTileX);
        int toTileY = TileMap.pixelsToTiles(toY + s.getHeight() - 1);
        //System.out.println("toTileY: " + toTileY);

        //check each tile for a collision
        for(int x=fromTileX; x<=toTileX; x++){
            for(int y=fromTileY; y<=toTileY; y++){
                if(x>0 && x<= tileMap.getMapWidth() && tileMap.getTile(x, y) != null){
                    //collision found. Return the til 
                    Point pointCache = new Point();
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }
        //no collision found
        return null;
    }

    public Image loadImage(String fileName){
        Image im = new ImageIcon(fileName).getImage();
        return im;
    }

    public Rectangle2D.Double getBoundingRectangle(){
        return new Rectangle2D.Double(x, y, SANTAWIDTH, SANTAHEIGHT);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setFloorY(int y){
        this.floorY = y;
    }

    public int getWidth(){
        return SANTAWIDTH;
    }

    public int getHeight(){
        return SANTAHEIGHT;
    }

    public Animation getAnimation(){
        return playerAnim;
        /*
        if(type==1){
            return santaAnimIdle;
        }
        else
            if(type==2){
                return santaAnimWalk;
            }
            else
                if(type==3){
                    return santaAnimRun;
                }
                else
                    if(type==4){
                        return santaAnimJump;
                    }
                    else
                        if(type==5){
                            return santaAnimSlide;
                        }
                        else
                            if(type==6){
                                return santaAnimDead;
                            }
        return null;
        */
    }

    public void loadAnimSantaIdle(){
        // load images for animation frames
        Image idle1 = loadImage("Images/Santa/Idle/Idle1.png");
        Image idle2 = loadImage("Images/Santa/Idle/Idle2.png");
        Image idle3 = loadImage("Images/Santa/Idle/Idle3.png");
        Image idle4 = loadImage("Images/Santa/Idle/Idle4.png");
        Image idle5 = loadImage("Images/Santa/Idle/Idle5.png");
        Image idle6 = loadImage("Images/Santa/Idle/Idle6.png");
        Image idle7 = loadImage("Images/Santa/Idle/Idle7.png");
        Image idle8 = loadImage("Images/Santa/Idle/Idle8.png");
        Image idle9 = loadImage("Images/Santa/Idle/Idle9.png");
        Image idle10 = loadImage("Images/Santa/Idle/Idle10.png");
        Image idle11 = loadImage("Images/Santa/Idle/Idle11.png");
        Image idle12 = loadImage("Images/Santa/Idle/Idle12.png");
        Image idle13 = loadImage("Images/Santa/Idle/Idle13.png");
        Image idle14 = loadImage("Images/Santa/Idle/Idle14.png");
        Image idle15 = loadImage("Images/Santa/Idle/Idle15.png");
        Image idle16 = loadImage("Images/Santa/Idle/Idle16.png");

        // create animation object and insert frames
        santaAnimIdle = new Animation(window);

        //Create frame with the specified duration
        santaAnimIdle.addFrame(idle1, 100);
        santaAnimIdle.addFrame(idle2, 100);
        santaAnimIdle.addFrame(idle3, 100);
        santaAnimIdle.addFrame(idle4, 100);
        santaAnimIdle.addFrame(idle5, 100);
        santaAnimIdle.addFrame(idle6, 100);
        santaAnimIdle.addFrame(idle7, 100);
        santaAnimIdle.addFrame(idle8, 100);
        santaAnimIdle.addFrame(idle9, 100);
        santaAnimIdle.addFrame(idle10, 100);
        santaAnimIdle.addFrame(idle11, 100);
        santaAnimIdle.addFrame(idle12, 100);
        santaAnimIdle.addFrame(idle13, 100);
        santaAnimIdle.addFrame(idle14, 100);
        santaAnimIdle.addFrame(idle15, 100);
        santaAnimIdle.addFrame(idle16, 100);
    }

    public void loadAnimSantaWalk(){ 
        // load images for animation frames
        Image walk1 = loadImage("Images/Santa/Walk/Walk1.png");
        Image walk2 = loadImage("Images/Santa/Walk/Walk2.png");
        Image walk3 = loadImage("Images/Santa/Walk/Walk3.png");
        Image walk4 = loadImage("Images/Santa/Walk/Walk4.png");
        Image walk5 = loadImage("Images/Santa/Walk/Walk5.png");
        Image walk6 = loadImage("Images/Santa/Walk/Walk6.png");
        Image walk7 = loadImage("Images/Santa/Walk/Walk7.png");
        Image walk8 = loadImage("Images/Santa/Walk/Walk8.png");
        Image walk9 = loadImage("Images/Santa/Walk/Walk9.png");
        Image walk10 = loadImage("Images/Santa/Walk/Walk10.png");
        Image walk11 = loadImage("Images/Santa/Walk/Walk11.png");
        Image walk12 = loadImage("Images/Santa/Walk/Walk12.png");
        Image walk13 = loadImage("Images/Santa/Walk/Walk13.png");
        
        // create animation object and insert frames
        santaAnimWalk = new Animation(window);

        //Create frame with the specified duration
        santaAnimWalk.addFrame(walk1, 100);
        santaAnimWalk.addFrame(walk2, 100);
        santaAnimWalk.addFrame(walk3, 100);
        santaAnimWalk.addFrame(walk4, 100);
        santaAnimWalk.addFrame(walk5, 100);
        santaAnimWalk.addFrame(walk6, 100);
        santaAnimWalk.addFrame(walk7, 100);
        santaAnimWalk.addFrame(walk8, 100);
        santaAnimWalk.addFrame(walk9, 100);
        santaAnimWalk.addFrame(walk10, 100);
        santaAnimWalk.addFrame(walk11, 100);
        santaAnimWalk.addFrame(walk12, 100);
        santaAnimWalk.addFrame(walk13, 100);
    }

    public void loadAnimSantaRun(){ 
        // load images for animation frames
        Image run1 = loadImage("Images/Santa/Run/Run1.png");
        Image run2 = loadImage("Images/Santa/Run/Run2.png");
        Image run3 = loadImage("Images/Santa/Run/Run3.png");
        Image run4 = loadImage("Images/Santa/Run/Run4.png");
        Image run5 = loadImage("Images/Santa/Run/Run5.png");
        Image run6 = loadImage("Images/Santa/Run/Run6.png");
        Image run7 = loadImage("Images/Santa/Run/Run7.png");
        Image run8 = loadImage("Images/Santa/Run/Run8.png");
        Image run9 = loadImage("Images/Santa/Run/Run9.png");
        Image run10 = loadImage("Images/Santa/Run/Run10.png");
        Image run11 = loadImage("Images/Santa/Run/Run11.png");
        
        // create animation object and insert frames
        santaAnimRun = new Animation(window);

        //Create frame with the specified duration
        santaAnimRun.addFrame(run1, 100);
        santaAnimRun.addFrame(run2, 100);
        santaAnimRun.addFrame(run3, 100);
        santaAnimRun.addFrame(run4, 100);
        santaAnimRun.addFrame(run5, 100);
        santaAnimRun.addFrame(run6, 100);
        santaAnimRun.addFrame(run7, 100);
        santaAnimRun.addFrame(run8, 100);
        santaAnimRun.addFrame(run9, 100);
        santaAnimRun.addFrame(run10, 100);
        santaAnimRun.addFrame(run11, 100);
    }

    public void loadAnimSantaJump(){ 
        // load images for animation frames
        Image jump1 = loadImage("Images/Santa/Jump/Jump1.png");
        Image jump2 = loadImage("Images/Santa/Jump/Jump2.png");
        Image jump3 = loadImage("Images/Santa/Jump/Jump3.png");
        Image jump4 = loadImage("Images/Santa/Jump/Jump4.png");
        Image jump5 = loadImage("Images/Santa/Jump/Jump5.png");
        Image jump6 = loadImage("Images/Santa/Jump/Jump6.png");
        Image jump7 = loadImage("Images/Santa/Jump/Jump7.png");
        Image jump8 = loadImage("Images/Santa/Jump/Jump8.png");
        Image jump9 = loadImage("Images/Santa/Jump/Jump9.png");
        Image jump10 = loadImage("Images/Santa/Jump/Jump10.png");
        Image jump11 = loadImage("Images/Santa/Jump/Jump11.png");
        Image jump12 = loadImage("Images/Santa/Jump/Jump12.png");
        Image jump13 = loadImage("Images/Santa/Jump/Jump13.png");
        Image jump14 = loadImage("Images/Santa/Jump/Jump14.png");
        Image jump15 = loadImage("Images/Santa/Jump/Jump15.png");
        Image jump16 = loadImage("Images/Santa/Jump/Jump16.png");
        
        // create animation object and insert frames
        santaAnimJump = new Animation(window);

        //Create frame with the specified duration
        santaAnimJump.addFrame(jump1, 100);
        santaAnimJump.addFrame(jump2, 100);
        santaAnimJump.addFrame(jump3, 100);
        santaAnimJump.addFrame(jump4, 100);
        santaAnimJump.addFrame(jump5, 100);
        santaAnimJump.addFrame(jump6, 100);
        santaAnimJump.addFrame(jump7, 100);
        santaAnimJump.addFrame(jump8, 100);
        santaAnimJump.addFrame(jump9, 100);
        santaAnimJump.addFrame(jump10, 100);
        santaAnimJump.addFrame(jump11, 100);
        santaAnimJump.addFrame(jump12, 100);
        santaAnimJump.addFrame(jump13, 100);
        santaAnimJump.addFrame(jump14, 100);
        santaAnimJump.addFrame(jump15, 100);
        santaAnimJump.addFrame(jump16, 100);
    }

    public void loadAnimSantaSlide(){ 
        // load images for animation frames
        Image slide1 = loadImage("Images/Santa/Slide/Slide1.png");
        Image slide2 = loadImage("Images/Santa/Slide/Slide2.png");
        Image slide3 = loadImage("Images/Santa/Slide/Slide3.png");
        Image slide4 = loadImage("Images/Santa/Slide/Slide4.png");
        Image slide5 = loadImage("Images/Santa/Slide/Slide5.png");
        Image slide6 = loadImage("Images/Santa/Slide/Slide6.png");
        Image slide7 = loadImage("Images/Santa/Slide/Slide7.png");
        Image slide8 = loadImage("Images/Santa/Slide/Slide8.png");
        Image slide9 = loadImage("Images/Santa/Slide/Slide9.png");
        Image slide10 = loadImage("Images/Santa/Slide/Slide10.png");
        Image slide11 = loadImage("Images/Santa/Slide/Slide11.png");
        
        // create animation object and insert frames
        santaAnimSlide = new Animation(window);

        //Create frame with the specified duration
        santaAnimSlide.addFrame(slide1, 100);
        santaAnimSlide.addFrame(slide2, 100);
        santaAnimSlide.addFrame(slide3, 100);
        santaAnimSlide.addFrame(slide4, 100);
        santaAnimSlide.addFrame(slide5, 100);
        santaAnimSlide.addFrame(slide6, 100);
        santaAnimSlide.addFrame(slide7, 100);
        santaAnimSlide.addFrame(slide8, 100);
        santaAnimSlide.addFrame(slide9, 100);
        santaAnimSlide.addFrame(slide10, 100);
        santaAnimSlide.addFrame(slide11, 100);
    }

    public void loadAnimSantaDead(){
        // load images for animation frames
        Image dead1 = loadImage("Images/Santa/Dead/Dead1.png");
        Image dead2 = loadImage("Images/Santa/Dead/Dead2.png");
        Image dead3 = loadImage("Images/Santa/Dead/Dead3.png");
        Image dead4 = loadImage("Images/Santa/Dead/Dead4.png");
        Image dead5 = loadImage("Images/Santa/Dead/Dead5.png");
        Image dead6 = loadImage("Images/Santa/Dead/Dead6.png");
        Image dead7 = loadImage("Images/Santa/Dead/Dead7.png");
        Image dead8 = loadImage("Images/Santa/Dead/Dead8.png");
        Image dead9 = loadImage("Images/Santa/Dead/Dead9.png");
        Image dead10 = loadImage("Images/Santa/Dead/Dead10.png");
        Image dead11 = loadImage("Images/Santa/Dead/Dead11.png");
        Image dead12 = loadImage("Images/Santa/Dead/Dead12.png");
        Image dead13 = loadImage("Images/Santa/Dead/Dead13.png");
        Image dead14 = loadImage("Images/Santa/Dead/Dead14.png");
        Image dead15 = loadImage("Images/Santa/Dead/Dead15.png");
        Image dead16 = loadImage("Images/Santa/Dead/Dead16.png");
        Image dead17 = loadImage("Images/Santa/Dead/Dead17.png");

        // create animation object and insert frames
        santaAnimDead = new Animation(window);

        //Create frame with the specified duration
        santaAnimDead.addFrame(dead1, 100);
        santaAnimDead.addFrame(dead2, 100);
        santaAnimDead.addFrame(dead3, 100);
        santaAnimDead.addFrame(dead4, 100);
        santaAnimDead.addFrame(dead5, 100);
        santaAnimDead.addFrame(dead6, 100);
        santaAnimDead.addFrame(dead7, 100);
        santaAnimDead.addFrame(dead8, 100);
        santaAnimDead.addFrame(dead9, 100);
        santaAnimDead.addFrame(dead10, 100);
        santaAnimDead.addFrame(dead11, 100);
        santaAnimDead.addFrame(dead12, 100);
        santaAnimDead.addFrame(dead13, 100);
        santaAnimDead.addFrame(dead14, 100);
        santaAnimDead.addFrame(dead15, 100);
        santaAnimDead.addFrame(dead16, 100);
        santaAnimDead.addFrame(dead17, 100);
    }
    
}

