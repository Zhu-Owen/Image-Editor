import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
/**
 * The class Processor contains all of the code to actually perform
 * transformation. The rest of the classes serve to support that
 * capability. This World allows the user to choose transformations
 * and open files.
 * 
 * @author Owen Zhu and Larry Wu
 * @version June 2020
 */
public class Background extends World
{
    // Constants:
    private final String STARTING_FILE = "landscape.png";
    private final int rotateXPos = 100, filtersXPos = 250, saveXPos = 400;

    // Objects and Variables:
    private ImageHolder image;
    private TextButton blueButton;
    private TextButton hRevButton;
    private TextButton vRevButton;
    private TextButton nTogButton;
    private TextButton gButton;
    private TextButton redButton;
    private TextButton invButton;
    private TextButton pixalateButton;
    private TextButton upOpacityButton;
    private TextButton downOpacityButton;
    private TextButton sepiaButton;
    private TextButton cw90Button;
    private TextButton ccw90Button;
    private TextButton r180Button;
    private TextButton openFile;
    private TextButton colorFiltersTab;
    private TextButton rotationsTab;
    private TextButton saveTab;
    private TextButton undoButton;
    private TextButton redoButton;
    private TextButton reset;
    private TextButton saveAsPNG;
    private TextButton saveAsJPG;
    private String fileName;
    
    private boolean filterOn = false, rotationsOn = false, saveOn = false;     
    private int pixalateFactor = 2;   
    private ArrayList <BufferedImage> list;
    private BufferedImage redo = null;
    
    /**
     * Constructor for objects of class Background.
     */
    public Background()
    {    
        // Create a new world with 1000x800 cells with a cell size of 1x1 pixels.
        super(1000, 800, 1); 
        
        // Initialize buttons and the image
        Processor.resetFactor();
        fileName = STARTING_FILE;
        
        image = new ImageHolder(STARTING_FILE);
        redButton = new TextButton(" [ Red-ify ] ");
        blueButton = new TextButton(" [ Blue-ify ] ");
        hRevButton = new TextButton(" [ Flip Horizontal ] ");
        vRevButton = new TextButton(" [ Flip Vertical ] ");
        nTogButton = new TextButton(" [ Toggle Negative ] ");
        gButton = new TextButton(" [ Greyscale ] ");
        invButton = new TextButton(" [ Cycle Inversion ] ");
        pixalateButton = new TextButton(" [ Pixalate ] ");
        upOpacityButton = new TextButton(" [ ↑ Opacity ] ");
        downOpacityButton = new TextButton(" [ ↓ Opacity ] ");
        cw90Button = new TextButton(" [ CW 90 Rotate ] ");
        ccw90Button = new TextButton(" [ CCW 90 Rotate ] ");
        r180Button = new TextButton(" [ Rotate 180 ] ");
        sepiaButton = new TextButton(" [ Sepia ] ");
        
        colorFiltersTab = new TextButton( " [Color Effects] ");
        rotationsTab = new TextButton( " [Rotations] ");
        saveTab = new TextButton ( " [Save] ");
        undoButton = new TextButton( " [Undo] ");
        redoButton = new TextButton( " [Redo] ");
        reset = new TextButton( " [Reset] ");
        
        openFile = new TextButton(" [ Open File: " + STARTING_FILE + " ] ");
        saveAsPNG = new TextButton (" [ Save as PNG ] ");
        saveAsJPG = new TextButton (" [ Save as JPG ] ");
        
        //Adding drop down menus on screen
        addObject (image, 500, 400);
        addObject (colorFiltersTab, filtersXPos, 30);
        addObject (rotationsTab, rotateXPos, 30);
        addObject (saveTab, saveXPos, 30);
        addObject (undoButton, 500, 30);
        addObject (redoButton, 600, 30);
        addObject (reset, 700, 30);
        addObject (openFile, 875, 30);
        
        //Draws the top black border
        GreenfootImage bg = getBackground();
        bg.setColor(Color.BLACK);
        bg.fillRect(0,0,1000,60);
        
        //Arraylist for undo function
        list = new ArrayList<>();
    }

    /**
     * Act() method just checks for mouse input
     */
    public void act ()
    {
        checkMouse();
    }
    
    /**
     * Spawns and removes the buttons in the filter tab
     */
    public void openFilters(){
        if(!filterOn) // When the button tab isn't pressed, add objects below the tab
        {
            addObject(gButton,    filtersXPos, 60);
            addObject(nTogButton, filtersXPos, 90);
            addObject(redButton,  filtersXPos, 120);
            addObject(blueButton, filtersXPos, 150);
            addObject(invButton,  filtersXPos, 180);
            addObject(sepiaButton, filtersXPos,210);
            addObject(pixalateButton,   filtersXPos, 240);
            addObject(upOpacityButton, filtersXPos, 270);
            addObject(downOpacityButton, filtersXPos, 300);
            filterOn=true;
        }
        else // When the button tab is already open, remove the objects
        {
            removeObject(gButton);
            removeObject(nTogButton);
            removeObject(redButton);
            removeObject(blueButton);
            removeObject(invButton);
            removeObject(sepiaButton);
            removeObject(pixalateButton);
            removeObject(upOpacityButton);
            removeObject(downOpacityButton);
            filterOn=false;
        }
    }
    
    /**
     * Spawns and removes the buttons in the rotation tab
     */
    public void openRotations(){
        if(!rotationsOn){
            addObject(hRevButton, rotateXPos, 60);
            addObject(vRevButton, rotateXPos, 90);
            addObject(cw90Button, rotateXPos, 120);
            addObject(ccw90Button, rotateXPos, 150);
            rotationsOn=true;
        }
        else
        {
            removeObject(hRevButton);
            removeObject(vRevButton);
            removeObject(cw90Button);
            removeObject(ccw90Button);
            rotationsOn=false;
        }
    }
    
    /**
     * Spawns and removes the buttons in the save tab
     */
    public void openSave(){
        if(!saveOn){
            addObject(saveAsPNG, saveXPos, 60);
            addObject(saveAsJPG, saveXPos, 90);
            saveOn=true;
        }
        else
        {
            removeObject(saveAsPNG);
            removeObject(saveAsJPG);
            saveOn=false;
        }
    }
    
    /**
     * Check for user clicking on a button
     */
    private void checkMouse ()
    {
        // Avoid excess mouse checks - only check mouse if somethething is clicked.
        if (Greenfoot.mouseClicked(null))
        {
            //Checking for functions that don't manipulate the image
            //So that it doesn't get saved into the arraylist
            if (Greenfoot.mouseClicked(openFile))
            {
                openFile();
                return;
            }
            if (Greenfoot.mouseClicked(undoButton))
            {
                undo();
                return;
            }
            if (Greenfoot.mouseClicked(redoButton))
            {
                redo();
                return;
            }
            if (Greenfoot.mouseClicked(colorFiltersTab)){
                openFilters();
                return;
            }
            if (Greenfoot.mouseClicked(rotationsTab)){
                openRotations();
                return;
            } 
            if (Greenfoot.mouseClicked(saveTab))
            {
                openSave();
                return;
            }
            
            if (Greenfoot.mouseClicked(saveAsJPG))
            {
                saveJPG();
                return;
            }
            if (Greenfoot.mouseClicked(saveAsPNG))
            {
                savePNG();
                return;
            }
            if (Greenfoot.mouseClicked(reset))
            {
                image.setImage(fileName);
                Processor.resetFactor();
                return;
            }
            
            //Saving a copy of the image in in the arraylist
            Processor.addToList(list, image.getBufferedImage());
            
            if (Greenfoot.mouseClicked(blueButton)){
                Processor.blueify(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(hRevButton)){
                Processor.flipHorizontal(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(vRevButton)){
                Processor.flipVertical(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(nTogButton)){
                Processor.tglNegative(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(gButton)){
                Processor.greyscale(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(redButton)){
                Processor.redify(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(invButton)){
                Processor.invert(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(sepiaButton)){
                Processor.sepia(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(pixalateButton)){
                Processor.pixalate(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(cw90Button)){
                image.setImage(Processor.rotateCw90(image.getBufferedImage()));
            }
            else if (Greenfoot.mouseClicked(ccw90Button)){
                image.setImage(Processor.rotateCcw90(image.getBufferedImage()));
            }
            else if (Greenfoot.mouseClicked(r180Button)){
                Processor.flipVertical(image.getBufferedImage());
                Processor.flipHorizontal(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(upOpacityButton)){
                Processor.increaseOpacity(image.getBufferedImage());
            }
            else if (Greenfoot.mouseClicked(downOpacityButton)){
                Processor.decreaseOpacity(image.getBufferedImage());
            }
        }
    } 
    
    /**
     * Allows the user to undo a change made to the image
     */
    private void undo()
    {
        // When the arraylist is not empty, remove the last change from the arraylist
        if (!list.isEmpty())
        {
            redo = image.getBufferedImage();
            image.setImage(Processor.createGreenfootImageFromBI(list.remove(list.size()-1)));
        }
    }
    
    /**
     * Allows the user to redo the lastest undo
     */
    private void redo()
    {
        if (redo != null)
        {
            image.setImage(Processor.createGreenfootImageFromBI(redo));
            list.clear();
            list.add(redo);
            redo = null;
        }
    }
    
    /**
     * Allows the user to open a new image file.
     */
    private void openFile ()
    {
        // Use a JOptionPane to get file name from user
        String fileName = JOptionPane.showInputDialog("Please input a file name with extension");
        // If the file opening operation is successful, update the text in the open file button
        if (image.openFile (fileName))
        {
            String display = " [ Open File: " + fileName + " ] ";
            openFile.update (display);
        }

    }
    
    /**
     * Allows the user to save the current picture as a .png file.
     */
    public void savePNG(){
        //prompts user to input the file name
        String fileName = JOptionPane.showInputDialog("Input file name"); 
        fileName+= ".png";
        // creates and saves the file in the same folder as this greenfoot project
        File f = new File (fileName);
        try{
            ImageIO.write(image.getImage().getAwtImage(), "png", f);
        } catch (IOException e){}
    }

    /**
     * Allows the user to save the current picture as a .jpg file.
     */
    public void saveJPG(){
        String fileName = JOptionPane.showInputDialog("Input file name"); 
        fileName+= ".jpg";
        //creates a new BufferedImage of a different type that has no alpha layer, since jpgs don't have them
        BufferedImage b = new BufferedImage (image.getImage().getWidth(), image.getImage().getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < b.getWidth(); i++){
            for (int j = 0; j < b.getHeight(); j++){
                int rgb = image.getBufferedImage().getRGB(i, j);
                b.setRGB(i, j, rgb);
            }
        }
        //writes the file and saves it
        File f = new File (fileName);
        try {
            ImageIO.write(b, "jpg", f);
        } catch (IOException e) {}
    }
}
