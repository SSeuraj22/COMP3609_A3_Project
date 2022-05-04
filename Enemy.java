import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Image;
// import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.Point;

public class Enemy {
    //variables
    private int x, y; // x and y position of the Enemy sprite
    private int maxHealth, curHealth, speed;
    
    private static final int ENEMYWIDTH = 50; //width of Enemy sprite
    private static final int ENEMYHEIGHT = 100; //height of Enemy sprite

    private static final int DX = 5; //amount to move Enemy on the x axis
    private static final int DY = 10; //amount to move Enemy on the y axis

    private JFrame window;
    private TileMap tileMap;

    //Animations for Enemy
    private Animation enemyAnimIdle; //animation sprite for idle enemy
    private Animation enemyAnimWalk; //animation sprite for walk enemy
    private Animation enemyAnimRun; //animation sprite for run enemy
    private Animation enemyAnimJump; //animation sprite for jump enemy
    private Animation enemyAnimSlide; //animation sprite for slide enemy
    private Animation enemyAnimDead; //animation sprite for dead enemy

    private Animation playerAnim;
    private boolean isJumping;
    private int timeElapsed;
    private boolean jumpGoingUp;
    private boolean jumpGoingDown;
    int floorY;

    //constructor
    public Enemy(JFrame jf, TileMap tm){
        window = jf;
        tileMap = tm;
        isJumping = false;
        timeElapsed = 0;

        enemyAnimIdle = null;
        enemyAnimWalk = null;
        enemyAnimRun = null;
        enemyAnimJump = null;
        enemyAnimSlide = null;
        enemyAnimDead = null;

        // loadAnimEnemyIdle();
        // loadAnimEnemyWalk();
        // loadAnimEnemyRun();
        // loadAnimEnemyJump();
        // loadAnimEnemySlide();
        // loadAnimEnemyDead();

        enemyAnimIdle.setWidth(ENEMYWIDTH);
        enemyAnimIdle.setHeight(ENEMYHEIGHT);
        enemyAnimIdle.setDX(DX);

        enemyAnimWalk.setWidth(ENEMYWIDTH);
        enemyAnimWalk.setHeight(ENEMYHEIGHT);
        enemyAnimWalk.setDX(DX);

        enemyAnimRun.setWidth(ENEMYWIDTH);
        enemyAnimRun.setHeight(ENEMYHEIGHT);
        enemyAnimRun.setDX(DX);

        enemyAnimJump.setWidth(ENEMYWIDTH);
        enemyAnimJump.setHeight(ENEMYHEIGHT);
        enemyAnimJump.setDX(DX);

        enemyAnimSlide.setWidth(ENEMYWIDTH);
        enemyAnimSlide.setHeight(ENEMYHEIGHT);
        enemyAnimSlide.setDX(DX);

        enemyAnimDead.setWidth(ENEMYWIDTH);
        enemyAnimDead.setHeight(ENEMYHEIGHT);
        enemyAnimDead.setDX(DX);

        playerAnim = enemyAnimIdle;

        x = (int) ((window.getWidth() - playerAnim.getAnimWidth())/2);
        y = 380;
    }

    public void draw(Graphics2D g2, int x, int y){
        playerAnim.setX(x);
        playerAnim.setY(y);
        playerAnim.draw(g2, x, y);
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
            int enemyXPos = x + DX + sanWidth;
            int xAxisTiles = TileMap.pixelsToTiles(enemyXPos);
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
                        System.out.println ("y of enemy = " + y + " bottom y of tile = " + tileBottomY);
                        timeElapsed = advanceTime (timeElapsed + 1, distance);

                        jumpGoingUp = false;
                        jumpGoingDown = true;

                        y = tileY + tileH;
                        System.out.println ("new y of enemy = " + y);
                    }
                    else
                        if(jumpGoingDown==true){
                            System.out.println ("Jumping: Collision Going Down!");
                            System.out.println ("y of enemy = " + y + " top y of tile = " + tileY);
                            jumpGoingDown = false;
                            y = tileY - ENEMYHEIGHT; //position the ball on top the wall that was hit on the way down
                            isJumping = false; //stop jumping
                        }
                }
                else{
                    System.out.println ("Jumping: No collision.");
                    System.out.println ("y of enemy = " + y);
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
        Rectangle2D.Double enemyRect = getBoundingRectangle();
        Rectangle2D.Double tileRect = tileMap.getBoundingSquare(x, y);
        return enemyRect.intersects(tileRect);
    }

    public void moveRight(){

        if (!window.isVisible ()){ 
            return;
        }
        
        int sanWidth = playerAnim.getWidth();
        System.out.println("Enemy width: " + sanWidth);

        int tmPixWidth = tileMap.getMapWidthPixels();//tile map width in pixels
        System.out.println("Tile Map Width: " + tmPixWidth);
        
        int enemyXPos = x + DX + sanWidth;
        System.out.println("Enemy Position: " + enemyXPos);

        if((enemyXPos) <= tmPixWidth){//if player position will be <= tile map width
            x = x + DX;
            Point p = getTileCollision(this, enemyXPos, y);
            if(p!=null){

                System.out.println("Point not null");
            }
            else{
                System.out.println("Point is null");
            }

            //int xAxisTiles = tileMap.pixelsToTiles(enemyXPos); //column num
            //int yAxisTiles = tileMap.pixelsToTiles(y) - 2; //row num

            //Image tileImg = tileMap.getTile(xAxisTiles, yAxisTiles);

            

            //String mess = "Coordinates in TileMap: (" + xAxisTiles + "," + yAxisTiles + ")";
            //System.out.println (mess);

            

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

    public Point getTileCollision(Enemy s, float newX, float newY){
        float fromX = Math.min(s.getX(), newX);
        System.out.println("fromX: " + fromX);
        float fromY = Math.min(s.getY(), newY);
        System.out.println("fromY: " + fromY);
        float toX = Math.max(s.getX(), newX);
        System.out.println("toX: " + toX);
        float toY = Math.max(s.getY(), newY);
        System.out.println("toY: " + toY);

        //get the tile locations
        int fromTileX = TileMap.pixelsToTiles(fromX);
        System.out.println("fromTileX: " + fromTileX);
        int fromTileY = TileMap.pixelsToTiles(fromY);
        System.out.println("fromTileY: " + fromTileY);
        int toTileX = TileMap.pixelsToTiles(toX + s.getWidth() - 1);
        System.out.println("toTileX: " + toTileX);
        int toTileY = TileMap.pixelsToTiles(toY - s.getHeight() - 1);
        System.out.println("toTileY: " + toTileY);

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
        return new Rectangle2D.Double(x, y, ENEMYWIDTH, ENEMYHEIGHT);
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
        return ENEMYWIDTH;
    }

    public int getHeight(){
        return ENEMYHEIGHT;
    }

    public Animation getAnimation(){
        return playerAnim;
        /*
        if(type==1){
            return enemyAnimIdle;
        }
        else
            if(type==2){
                return enemyAnimWalk;
            }
            else
                if(type==3){
                    return enemyAnimRun;
                }
                else
                    if(type==4){
                        return enemyAnimJump;
                    }
                    else
                        if(type==5){
                            return enemyAnimSlide;
                        }
                        else
                            if(type==6){
                                return enemyAnimDead;
                            }
        return null;
        */
    }  
}
