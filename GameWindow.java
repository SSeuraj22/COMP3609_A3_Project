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

    //Animations for Santa
    private Animation santaAnimIdle; //animation sprite for idle santa
    private Animation santaAnimWalk; //animation sprite for walk santa
    private Animation santaAnimRun; //animation sprite for run santa
    private Animation santaAnimJump; //animation sprite for jump santa
    private Animation santaAnimSlide; //animation sprite for slide santa
    private Animation santaAnimDead; //animation sprite for dead santa

    public GameWindow(){
        super("Nightmare before Christmas");
        setFullScreen();
        onMainMenu = true;

        santaAnimIdle = null;
        santaAnimWalk = null;
        santaAnimRun = null;
        santaAnimJump = null;
        santaAnimDead = null;

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

        loadAnimSantaIdle();
        loadAnimSantaWalk();
        loadAnimSantaRun();
        loadAnimSantaJump();
        loadAnimSantaSlide();
        loadAnimSantaDead();

        buffImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        startGame();
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
        santaAnimIdle = new Animation(this, 100, 500, 250, 225);

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
        santaAnimWalk = new Animation(this, 100, 500, 250, 225);

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
        santaAnimRun = new Animation(this, 100, 500, 250, 225);

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
        santaAnimJump = new Animation(this, 100, 500, 250, 225);

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
        santaAnimSlide = new Animation(this, 100, 500, 250, 225);

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
        santaAnimDead = new Animation(this, 100, 500, 250, 225);

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
                santaAnimIdle.draw(gImage); //draw santa idle animation
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
        santaAnimIdle.update();
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
        }
        else
            if((keyCode==KeyEvent.VK_RIGHT) || (keyCode==KeyEvent.VK_D)){
                backgManager.moveRight(1);
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
