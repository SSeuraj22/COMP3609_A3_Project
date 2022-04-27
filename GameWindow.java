import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy; // needed for page flipping
import java.awt.geom.QuadCurve2D;

public class GameWindow extends JFrame implements Runnable {

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

    //Menu Buttons
    private Image startButtonImg;
    private Image howToPlayButtonImg;
    private Image exitButtonImg;

    //Bounding rectangles for buttons
    private Rectangle startButtonArea;
    private Rectangle howToPlayButtonArea;
    private Rectangle exitButtonArea;


    public GameWindow(){
        super("Nightmare before Christmas");
        setFullScreen();

        startButtonImg = loadImage("Images/start_button.png");
        howToPlayButtonImg = loadImage("Images/how_to_play_button1.png");
        exitButtonImg = loadImage("Images/exit_button.png");

        setMenuButtons();

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
        g.drawImage(startButtonImg, startButtonArea.x, startButtonArea.y, null);
        g.drawImage(howToPlayButtonImg, howToPlayButtonArea.x, howToPlayButtonArea.y, null);
        g.drawImage(exitButtonImg, exitButtonArea.x, exitButtonArea.y, null);
    }

    public Image loadImage(String fileName){
        Image im = new ImageIcon(fileName).getImage();
        return im;
    }
}
