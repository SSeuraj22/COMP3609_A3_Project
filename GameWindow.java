import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy; // needed for page flipping


public class GameWindow extends JFrame implements Runnable{

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

    public GameWindow(){
        super("Nightmare before Christmas");
        setFullScreen();
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

        //showCurrentMode();
    }

    /*
    private void showCurrentMode() {

        DisplayMode dms[] = graphDev.getDisplayModes();//gets the different display resolutions for your monitor

        for (int i=0; i<dms.length; i++) {
            System.out.println("Display Modes Available: (" + 
                           dms[i].getWidth() + "," + dms[i].getHeight() + "," +
                           dms[i].getBitDepth() + "," + dms[i].getRefreshRate() + ")  " );            
        }
        
        

        DisplayMode dm = graphDev.getDisplayMode();

        System.out.println("Current Display Mode: (" + 
                           dm.getWidth() + "," + dm.getHeight() + "," +
                           dm.getBitDepth() + "," + dm.getRefreshRate() + ")  " );
    }
    */
    
    
    private void restoreScreen(){ //to come off full screen mode
        Window win = graphDev.getFullScreenWindow(); //get the window
        if(win!=null){
            win.dispose();
        }
        graphDev.setFullScreenWindow(null); //set to no full screen
    }

    public void gameRender(Graphics graphScr){
        Graphics2D gImage = (Graphics2D) buffImage.getGraphics();
        backgManager.draw(gImage, 1);

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
            while (isRunning) {
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
        if(!isPaused){


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

            try{

            }
            catch(Exception ex){

            }

            gameThread = new Thread(this);
            gameThread.start();
        }
    }
}
