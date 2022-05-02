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
    private volatile boolean onLevel1 = false;

    //For tiles
    TileMap tileMapLvl1;
    TileMap tileMapLvl2;
    TileMap tileMapLvl3;
    TileMapManager tileMapManager;

    public GameWindow(){
        super("Nightmare before Christmas");
        setFullScreen();
        onMainMenu = true;

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

            //QuadCurve2D.Double menuCurve = new QuadCurve2D.Double(screenWidth * 0.50, 0, screenWidth * 0.732, screenHeight/2, screenWidth * 0.50, screenHeight);
            //gImage.setColor(Color.WHITE);
            //gImage.setStroke(new BasicStroke(5)); //set thickness of line
            //gImage.draw(menuCurve);

            Font font = new Font("Chiller", Font.BOLD, 100);
            gImage.setFont(font);
            gImage.drawString("Nightmare Before Christmas", screenWidth/10, (int) (screenHeight/8.5));
            //gImage.drawString("Nightmare", screenWidth/4, (int) (screenHeight/2.9));
            //gImage.drawString("Before", screenWidth/4, screenHeight/2);
            //gImage.drawString("Christmas", screenWidth/4, (int) (screenHeight/1.5));

            drawMenuButtons(gImage);
        }
        else
            if(onLevel1==true){
                int bgWidth = backgManager.getBgImgWidth(1);
                backgManager.draw(gImage, 1, bgWidth, screenHeight);
                tileMapLvl1.draw(gImage);
                //santaAnimIdle.draw(gImage); //draw santa idle animation
                //santaAnimWalk.draw(gImage); //draw santa walk animation
                //santaAnimRun.draw(gImage); //draw santa run animation
                //santaAnimJump.draw(gImage); //draw santa jump animation
                //santaAnimSlide.draw(gImage); //draw santa slide animation
                //santaAnimDead.draw(gImage); //draw santa dead animation
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
        //if(!isPaused){
        tileMapLvl1.update();
        //santaAnimWalk.update();
        //santaAnimRun.update();
        //santaAnimJump.update();
        //santaAnimSlide.update();
        //santaAnimDead.update();

        //}
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

            try{
                tileMapLvl1 = tileMapManager.loadTileMap("Maps/Level1.txt");
                //tileMapLvl2 = tileMapManager.loadTileMap("Maps/Level2Map.txt");
                //tileMapLvl3 = tileMapManager.loadTileMap("Maps/Level3Map.txt");
                int w, h;
                w = tileMapLvl1.getMapWidth();
                h = tileMapLvl1.getMapHeight();
                System.out.println ("Width of tilemap " + w);
                System.out.println ("Height of tilemap " + h);
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
            backgManager.moveLeft(1);
            tileMapLvl1.moveLeft();
        }
        else
            if((keyCode==KeyEvent.VK_RIGHT) || (keyCode==KeyEvent.VK_D)){
                backgManager.moveRight(1);
                tileMapLvl1.moveRight();
            }
            else
                if(keyCode==KeyEvent.VK_SPACE){
                    tileMapLvl1.moveJump();
                }
    }

    public void keyReleased(KeyEvent ke){

    }

    public void keyTyped(KeyEvent ke){

    }

    //methods for the MouseListener interface
    public void mousePressed(MouseEvent me){
        //System.out.println("mouse coordinates: x-" + me.getX() + " y- " + me.getY());
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
        }

        if(isOverExitButton==true){ //if the mouse clicked on the Quit button
            isRunning = false; //set isRunning to false to terminate the game
        }
    }


}
