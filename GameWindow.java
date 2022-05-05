import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy; // needed for page flipping
//import java.awt.geom.QuadCurve2D;

public class GameWindow extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener{

    //initialize varibles
    private GraphicsDevice graphDev;
    private int screenWidth, screenHeight; 
    private BufferedImage buffImage;
    private static final int NUM_OF_BUFFERS = 2;
    private BackgroundManager backgManager;
    private Graphics graphScr;

    private volatile boolean isRunning = false;
    private volatile boolean isPaused = false;
    //private volatile boolean isStopped = false;

    private boolean finishedOff = false;
    private BufferStrategy bufferStrategy;
    private Thread gameThread = null; //this controls the game

    //Menu Buttons Images
    private Image startButtonImg;
    private Image startButtonGreen;

    private Image howToPlayButtonImg;
    private Image howToPlayButtonGreen;

    private Image exitButtonImg;
    private Image exitButtonGreen;

    //Bounding rectangles for buttons
    private Rectangle startButtonArea;
    private Rectangle howToPlayButtonArea;
    private Rectangle exitButtonArea;

    //Buttons state for if mouse on button
    private volatile boolean isOverStartButton = false;
    private volatile boolean isOverHowToPlayButton = false;
    private volatile boolean isOverExitButton = false;

    //state variables for parts of the game
    private volatile boolean onMainMenu = false;
    public static boolean onLevel1 = false;
    public boolean onLevel2 = false;

    //For tiles
    TileMap tileMapLvl1;
    TileMap tileMapLvl2;
    TileMap tileMapLvl3;
    TileMapManager tileMapManager;

    SoundManager soundManager;

    public GameWindow(){
        super("Nightmare before Christmas");
        setFullScreen();
        onMainMenu = true;
        soundManager = SoundManager.getInstance();

        startButtonImg = loadImage("Images/start_button.png");
        startButtonGreen = loadImage("Images/start_button_green.png");

        howToPlayButtonImg = loadImage("Images/how_to_play_button.png");
        howToPlayButtonGreen = loadImage("Images/how_to_play_button_green.png");
       
        exitButtonImg = loadImage("Images/exit_button.png");
        exitButtonGreen = loadImage("Images/exit_button_green.png");

        setMenuButtons();

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        buffImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        startGame();
    }

    private void setFullScreen(){ //to set the screen to full screen
        GraphicsEnvironment graphEnviro = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphDev = graphEnviro.getDefaultScreenDevice();

        setUndecorated(true); //remove menu bar, borders, scrollbars, etc
        setResizable(false); //cannot resize screen
        setIgnoreRepaint(true);

        if(!graphDev.isFullScreenSupported()){
            System.out.println("Full-screen Exclusive Mode is not supported...");
            System.exit(0);
        }
        graphDev.setFullScreenWindow(this); //set this window to full screen mode

        screenWidth = getBounds().width;
        screenHeight = getBounds().height;

        System.out.println("Width of window is " + screenWidth);
        System.out.println("Height of window is " + screenHeight);

        try{
            createBufferStrategy(NUM_OF_BUFFERS);
        }
        catch(Exception ex){
            System.out.println("Error occured while creating buffer strategy " + ex); 
            System.exit(0);
        }
        bufferStrategy = getBufferStrategy();//get the buffer 
    }
    
    private void restoreScreen(){ //to come off full screen mode
        Window win = graphDev.getFullScreenWindow(); //get the window
        if(win!=null){
            win.dispose();
        }
        graphDev.setFullScreenWindow(null); //set to no full screen
    }

    public void gameRender(Graphics graphScr){
        Graphics2D gImage = (Graphics2D) buffImage.getGraphics();

        if(onMainMenu==true){
            backgManager.draw(gImage, 0, screenWidth, screenHeight);

            Font font = new Font("Chiller", Font.BOLD, 100);
            gImage.setFont(font);
            gImage.drawString("Nightmare Before Christmas", screenWidth/10, (int) (screenHeight/8.5));

            drawMenuButtons(gImage);
        }
        else
            if(onLevel1==true){
                int bgWidth = backgManager.getBgImgWidth(1);
                backgManager.draw(gImage, 1, bgWidth, screenHeight);
                tileMapLvl1.draw(gImage);
            }
            else{
                if(onLevel2==true){
                    int bgWidth = backgManager.getBgImgWidth(2);
                    backgManager.draw(gImage, 2, bgWidth, screenHeight);
                    tileMapLvl2.draw(gImage);
                }
            }
        

        Graphics2D graph2 = (Graphics2D) graphScr;
        graph2.drawImage(buffImage, 0, 0, screenWidth, screenHeight, null);

        gImage.dispose();
        graph2.dispose();
    }

    private void screenUpdate(){
        try{
            graphScr = bufferStrategy.getDrawGraphics(); //object to draw the image using the buffer
            gameRender(graphScr);
            graphScr.dispose();

            if (!bufferStrategy.contentsLost()){ // if the contents isnt lost
                //Page flipping
                bufferStrategy.show(); //to switch buffers
            }
            else{
                System.out.println("The contents of the buffer is lost...");
            }

            Toolkit.getDefaultToolkit().sync();
        }
        catch(Exception ex){
            ex.printStackTrace();
            isRunning = false;
        }
    }

    // implementation of Runnable interface
    public void run(){
        try{
            isRunning = true;
            while (isRunning==true) {
                if (isPaused == false) {
                    gameUpdate();
                }
                screenUpdate();
                Thread.sleep (50);
            }
        }
        catch(InterruptedException e) {

        }
        finishOff();
    }

    public void gameUpdate(){
        if(tileMapLvl1.santa.finishedLevel==true){
            onLevel1 = false;
            onLevel2 = true;
        }

        if(onLevel1==true){
            tileMapLvl1.update();
        }
        else
            if(onLevel2==true){
                tileMapLvl2.update();
            }
    }

    private void finishOff(){
        if(!finishedOff){
            finishedOff = true;
            restoreScreen();
            System.exit(0);
        }
    }

    private void startGame(){
        if(gameThread==null){
            backgManager = new BackgroundManager(this);
            tileMapManager = new TileMapManager(this);
            soundManager.playClip("menu", true);
            try{
                tileMapLvl1 = tileMapManager.loadTileMap("Maps/Level1Map.txt", 1);
                tileMapLvl2 = tileMapManager.loadTileMap("Maps/Level2Map.txt", 2);
                int w1, h1, w2, h2;
                w1 = tileMapLvl1.getMapWidth();
                h1 = tileMapLvl1.getMapHeight();
                w2 = tileMapLvl2.getMapWidth();
                h2 = tileMapLvl2.getMapHeight();
                
            }
            catch(Exception ex){
                System.out.println(ex);
                System.exit(0);
            }

            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    private void setMenuButtons(){
        //get buttons height
        int startHeight = startButtonImg.getHeight(null);
        int howToPlayHeight = howToPlayButtonImg.getHeight(null);
        int exitHeight = exitButtonImg.getHeight(null);

        //get buttons width
        int startWidth = startButtonImg.getWidth(null);
        System.out.println("startWidth: " + startWidth);
        int howToPlayWidth = howToPlayButtonImg.getWidth(null);
        int exitWidth = exitButtonImg.getWidth(null);

        //The distance of a button from the top of the screen
        int topOffset = (screenHeight - startHeight - howToPlayHeight - exitHeight - (2 * 40)) / 3;
        
        int menuArea = (int) (screenWidth * 0.7); //area to place menu button
        System.out.println("menuArea: " + menuArea);

        int startXPlacement = menuArea; 
        startButtonArea = new Rectangle(startXPlacement, topOffset, startWidth, startHeight);

        int howToPlayXPlacement = menuArea - 40; 
        topOffset = topOffset + startHeight + 40;
        howToPlayButtonArea = new Rectangle(howToPlayXPlacement, topOffset, howToPlayWidth, howToPlayHeight);

        int exitXPlacement = menuArea; 
        topOffset = topOffset + startHeight + 40;
        exitButtonArea = new Rectangle(exitXPlacement, topOffset, exitWidth, exitHeight);

    }

    private void drawMenuButtons(Graphics g){
        if(isOverStartButton==true){//if mouse is on start button
            g.drawImage(startButtonGreen, startButtonArea.x, startButtonArea.y, null);
        }
        else{
            g.drawImage(startButtonImg, startButtonArea.x, startButtonArea.y, null);
        }
        
        if(isOverHowToPlayButton==true){//if mouse is on how to play button
            g.drawImage(howToPlayButtonGreen, howToPlayButtonArea.x, howToPlayButtonArea.y, null);
        }
        else{
            g.drawImage(howToPlayButtonImg, howToPlayButtonArea.x, howToPlayButtonArea.y, null);
        }
        
        if(isOverExitButton==true){//if mouse is on exit button
            g.drawImage(exitButtonGreen, exitButtonArea.x, exitButtonArea.y, null);
        }
        else{
            g.drawImage(exitButtonImg, exitButtonArea.x, exitButtonArea.y, null);
        }   
    }

    public Image loadImage(String fileName){
        Image im = new ImageIcon(fileName).getImage();
        return im;
    }

    //methods for the KeyListener interface
    public void keyPressed(KeyEvent ke){
        int keyCode = ke.getKeyCode();

        if((keyCode==KeyEvent.VK_LEFT) || (keyCode==KeyEvent.VK_A)){
            if(onLevel1==true){
                tileMapLvl1.moveLeft();
                if(tileMapLvl1.stopBackground==false){
                    backgManager.moveLeft(1);
                }
            }
            else    
                if(onLevel2==true){
                    tileMapLvl2.moveLeft();
                    if(tileMapLvl2.stopBackground==false){
                        backgManager.moveLeft(2);
                    }
                }
        }
        else
            if((keyCode==KeyEvent.VK_RIGHT) || (keyCode==KeyEvent.VK_D)){
                if(onLevel1==true){
                    tileMapLvl1.moveRight();
                    if(tileMapLvl1.stopBackground==false){
                        backgManager.moveRight(1);
                    }
                }
                else
                    if(onLevel2==true){
                        tileMapLvl2.moveRight();
                        if(tileMapLvl2.stopBackground==false){
                            backgManager.moveRight(2);
                        }
                    }
            }
            else
                if(keyCode==KeyEvent.VK_SPACE){
                    if(onLevel1==true){
                        tileMapLvl1.moveJump();
                        soundManager.playClip("jump", false);
                    }
                    else
                        if(onLevel2==true){
                            tileMapLvl2.moveJump();
                            soundManager.playClip("jump", false);
                        }
                }
                else
                    if(keyCode==KeyEvent.VK_ESCAPE){
                        isRunning = false;        
                        return;                         
                    }
                    else
                        if(keyCode==KeyEvent.VK_Z){
                            if(onLevel1==true){
                                tileMapLvl1.attack(this);
                            }
                            else    
                                if(onLevel2==true){
                                    tileMapLvl2.attack(this);
                                }
                        }
    }

    public void keyReleased(KeyEvent ke){

    }

    public void keyTyped(KeyEvent ke){

    }

    //methods for the MouseListener interface
    public void mousePressed(MouseEvent me){
        mousePressedOnButton(me.getX(), me.getY());
    }

    public void mouseReleased(MouseEvent me){

    }

    public void mouseEntered(MouseEvent me){

    }

    public void mouseExited(MouseEvent me){

    }

    public void mouseClicked(MouseEvent me){
        
    }

    //methods for the MouseMotionListener interface
    public void mouseDragged(MouseEvent mme){

    }

    public void mouseMoved(MouseEvent mme){
        mouseMovedOnButton(mme.getX(), mme.getY());
    }

    //To check whether the mouse is on a particular button
    private void mouseMovedOnButton(int x, int y){
        if(isRunning==true){
            //if mouse is in startButtonArea, set isOverStartButton to true else false
            isOverStartButton = startButtonArea.contains(x, y) ? true : false;
            isOverHowToPlayButton = howToPlayButtonArea.contains(x, y) ? true : false;
            isOverExitButton = exitButtonArea.contains(x, y) ? true : false;
        }
    }

    private void mousePressedOnButton(int x, int y){
        //if(!isOverExitButton){
            //return;
        //}

        if(isOverStartButton==true){
            onMainMenu = false;
            onLevel1 = true;
            soundManager.stopClip("menu");
            soundManager.playClip("background", true);
        }

        if(isOverExitButton==true){ //if the mouse clicked on the Quit button
            isRunning = false; //set isRunning to false to terminate the game
        }
    }
}
