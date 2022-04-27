import javax.swing.JFrame;
import java.awt.Graphics2D;

public class BackgroundManager {
    private String bgImagesLvl1[] = {"Images/Background/Level 1/Layer_1.png",
                                     "Images/Background/Level 1/Layer_2.png",
                                     "Images/Background/Level 1/Layer_3.png",
                                     "Images/Background/Level 1/Layer_4.png",
                                     "Images/Background/Level 1/Layer_5.png",
                                     "Images/Background/Level 1/Layer_6.png",
                                     "Images/Background/Level 1/Layer_7.png"};

    private String bgImagesLvl2[] = {"Images/Background/Level 2/Layer_1.png",
                                     "Images/Background/Level 2/Layer_2.png",
                                     "Images/Background/Level 2/Layer_3.png",
                                     "Images/Background/Level 2/Layer_4.png",
                                     "Images/Background/Level 2/Layer_5.png",
                                     "Images/Background/Level 2/Layer_6.png",
                                     "Images/Background/Level 2/Layer_7.png"};

    private String bgImagesLvl3[] = {"Images/Background/Level 3/Layer_1.png",
                                     "Images/Background/Level 3/Layer_2.png",
                                     "Images/Background/Level 3/Layer_3.png",
                                     "Images/Background/Level 3/Layer_4.png",
                                     "Images/Background/Level 3/Layer_5.png",
                                     "Images/Background/Level 3/Layer_6.png",
                                     "Images/Background/Level 3/Layer_7.png",
                                     "Images/Background/Level 3/Layer_8.png"};

    private String menuBgImages[] = {"Images/Background/Menu/menu_background.jpg",
                                     //"Images/Background/Menu/menu_background2.jpg"
                                    };

    private int moveAmount[] = {1, 2, 3, 4, 4, 4, 5, 10}; 
    private JFrame window; //JFrame that background is drawn on
   
    private Background[] backgroundsLvl1;
    private int numImgBgL1;

    private Background[] backgroundsLvl2;
    private int numImgBgL2;

    private Background[] backgroundsLvl3;
    private int numImgBgL3;

    private Background[] menuBackgrounds;
    private int numMenuBg;


    //Constructor
    public BackgroundManager(JFrame win){
        window = win;
        loadMenuBg();
        loadBgLevel1();
        loadBgLevel2();
        loadBgLevel3();
    }

    public void loadMenuBg(){ //load the images for menu background
        numMenuBg = menuBgImages.length; //number of backgrounds images for menu
        menuBackgrounds = new Background[numMenuBg];

        for(int count=0; count<numMenuBg; count++){
            menuBackgrounds[count] = new Background(window, menuBgImages[count], moveAmount[count]); //ignore moveAmount
        }
    }

    public void loadBgLevel1(){ //load the images for level 1 background
        numImgBgL1 = bgImagesLvl1.length; //number of backgrounds images for level 1
        backgroundsLvl1 = new Background[numImgBgL1];

        for(int count=0; count<numImgBgL1; count++){
            backgroundsLvl1[count] = new Background(window, bgImagesLvl1[count], moveAmount[count]);
        }
    }

    public void loadBgLevel2(){ //load the images for level 2 background
        numImgBgL2 = bgImagesLvl2.length; //number of backgrounds images for level 2
        backgroundsLvl2 = new Background[numImgBgL2];

        for(int count=0; count<numImgBgL2; count++){
            backgroundsLvl2[count] = new Background(window, bgImagesLvl2[count], moveAmount[count]);
        }
    }

    public void loadBgLevel3(){ //load the images for level 3 background
        numImgBgL3 = bgImagesLvl3.length; //number of backgrounds images for level 3
        backgroundsLvl3 = new Background[numImgBgL3];

        for(int count=0; count<numImgBgL3; count++){
            backgroundsLvl3[count] = new Background(window, bgImagesLvl3[count], moveAmount[count]);
        }
    }


    public void moveRight(int level){
        if(level==1){
            for(int count=0; count<numImgBgL1; count++){
                backgroundsLvl1[count].moveRight();
            }
        }
        else
            if(level==2){
                for(int count=0; count<numImgBgL2; count++){
                    backgroundsLvl2[count].moveRight();
                }
            }
            else
                if(level==3){
                    for(int count=0; count<numImgBgL3; count++){
                        backgroundsLvl3[count].moveRight();
                    }
                }
    }

    public void moveLeft(int level){
        if(level==1){
            for(int count=0; count<numImgBgL1; count++){
                backgroundsLvl1[count].moveLeft();
            }
        }
        else
            if(level==2){
                for(int count=0; count<numImgBgL2; count++){
                    backgroundsLvl2[count].moveLeft();
                }
            }
            else
                if(level==3){
                    for(int count=0; count<numImgBgL3; count++){
                        backgroundsLvl3[count].moveLeft();
                    }
                }
    }

    public void draw(Graphics2D g2, int level, int width, int height){
        if(level==0){//for menu background image
            for(int count=0; count<numMenuBg; count++){
                menuBackgrounds[count].draw(g2, width, height);
            }
        }
        else
            if(level==1){
                for(int count=0; count<numImgBgL1; count++){
                    backgroundsLvl1[count].draw(g2, width, height);
                }
            }
            else
                if(level==2){
                    for(int count=0; count<numImgBgL2; count++){
                        backgroundsLvl2[count].draw(g2, width, height);
                    }
                }
                else
                    if(level==3){
                        for(int count=0; count<numImgBgL3; count++){
                            backgroundsLvl3[count].draw(g2, width, height);
                        }
                    }
    }
    
}
