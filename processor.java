/**
 * This class manipulated Java BufferedImages, which are effectively 2d arrays
 * of pixels. Each pixel is a single integer packed with 4 values inside it.
 * <p>
 * I have included two useful methods for dealing with bit-shift operators so
 * you don't have to. These methods are unpackPixel() and packagePixel() and do
 * exactly what they say - extract red, green, blue and alpha values out of an
 * int, and put the same four integers back into a special packed integer. 
 * 
 * @author Owen Zhu and Larry Wu
 * @version June 2020
 */

import greenfoot.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Processor  
{
    /**
     * Example colour altering method by Mr. Cohen. This method will
     * increase the blue value while reducing the red and green values.
     * 
     * Demonstrates use of packagePixel() and unpackPixel() methods.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    
    // A class variable used to define the magnitude of pixalization of the image
    private static int factor = 2;
    
    /**
     * Allows the user to increase the blue-ness of the image
     */
    public static void blueify (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic BLUE-er
                if (blue < 254) blue += 2;
                if (red >= 50) red--;
                if (green >= 50) green--;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }
    
    /**
     * Allows the user to increase the red-ness of the image
     */
    public static void redify (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic RED-er
                if (red < 254) red += 2;
                if (blue >= 50) blue--;
                if (green >= 50) green--;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }  
    
    /**
     * Inverts the colors of the image around 
     * Red to blue, blue to green, green to red
     */
    public static void invert (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                int[] rgbValues = unpackPixel (rgb);
               
                // Switch the array orders so that red becomes green, green becomes blue, blue becomes red
                int alpha = rgbValues[0];
                int red = rgbValues[2];
                int green = rgbValues[3];
                int blue = rgbValues[1];
                
                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }  
    
    /**
     * Allows the user to flip the image about the y-axis
     */
    public static void flipHorizontal (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        //Using a temporary image to transfer the rgb pixels from the old to the new image
        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                //Flips the rgb pixels from one end of the image to the opposite side of the image
                newBi.setRGB(xSize - x - 1, y, rgb);
            }
        }
        
        //Copies the flipped image to the new image
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = newBi.getRGB(x, y);
                bi.setRGB(x,y, rgb);
            }
        }
    }
    
    /**
     * Allows the user to flip the image about the x-axis
     */
    public static void flipVertical (BufferedImage bi)
    {
        //This method does exactly the same as the flipHorizontal() method, but for the y-values
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                newBi.setRGB(x, ySize - y - 1, rgb);
            }
        }
        
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = newBi.getRGB(x, y);
                bi.setRGB(x,y, rgb);
            }
        }
    }
    
    /**
     * Allows the user to apply a negative image effect
     */
    public static void tglNegative(BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();
        
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                //Flips the values by subtracting the current rgb values from 255
                blue=255-blue;
                red=255-red;
                green=255-green;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }
    
    /**
     * Maps the values of the current rgb values to create a greyscale image
     */
    public static void greyscale(BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];
               
                //Using the rgb values of a pixel to map a new rgb value in the form of a greyscale color
                //Source: https://bit.ly/2Y8Cfek
                int gray = (int)(((double)blue*0.11) + ((double)red*0.3) + ((double)green*0.59));
                
                int newColour = packagePixel (gray, gray, gray, alpha);
                bi.setRGB (x, y, newColour);
            }
        }    
    }
    
    /**
     * Maps the values of the current rgb values to create a sepia image
     */
    public static void sepia(BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();
        
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                //Similar to the greyscale algorithm, the rgb values of the current image is used
                //to map the values of the new sepia image. Source: https://bit.ly/2ChYbLK
                int outRed = (int)(((double)red * .393) + ((double)green *.769) + ((double)blue * .189));
                int outGreen = (int)(((double)red * .349) + ((double)green *.686) + ((double)blue * .168));
                int outBlue = (int)(((double)red * .272) + ((double)green *.534) + ((double)blue * .131));
                
                //Insuring that the rgb values don't overflow
                outRed = Math.min(255,outRed);
                outGreen = Math.min(255,outGreen);
                outBlue = Math.min(255,outBlue);
                
                int newColour = packagePixel (outRed, outGreen, outBlue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }    
    }
    
    /**
     * Takes the average of the nearby pixels to create a new area of pixels of the same color
     * thus pixalating the image but not changing the resolution of the image
     * 
     * This was an original method made by Owen and Larry
     */
    public static void pixalate(BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                newBi.setRGB(x,y,rgb);
            }
        }
        
        //Using 2 forloops and the factor variable to determine the magnitude of the pixalization
        for (int x = 0; x < xSize; x+=factor)
        {
            for (int y = 0; y < ySize; y+=factor)
            {
                //Creating a 2d array to save the rgb values of the current quadrant
                int[] rgbList = new int[4];
                
                int alphaSum = 0, redSum = 0, greenSum = 0, blueSum = 0,idx = 0;
                
                //Saving the pixels of the current quadrant into rgbList
                for(int x2 = x; x2 < x+factor && x2 < xSize; x2++){
                    for(int y2 = y; y2 < y+factor && y2 < ySize; y2++){
                        rgbList = unpackPixel(newBi.getRGB(x2, y2));
                        alphaSum+=rgbList[0];
                        redSum+=rgbList[1];
                        greenSum+=rgbList[2];
                        blueSum+=rgbList[3];
                        idx++;
                    }
                }
                
                
                int newColour = packagePixel (redSum/idx, greenSum/idx, blueSum/idx, alphaSum/idx);
                
                for(int x2 = x; x2 < x+factor && x2 < xSize; x2++){
                    for(int y2 = y; y2 < y+factor && y2 < ySize; y2++){
                        bi.setRGB (x2, y2, newColour);
                    }
                }
            }
        }
        
        //Preventing integer overflow
        factor=Math.min(Math.max(xSize,ySize),factor);
        factor*=2;
    }
    
    public static GreenfootImage rotateCw90 (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight(); 

        BufferedImage newBi = new BufferedImage (ySize, xSize, 3);

        for (int i = 0; i < xSize; i++)
        {
            for (int j = ySize-1; j >= 0; j--)
            {
                newBi.setRGB(ySize - j - 1, i, bi.getRGB(i, j));
            }
        }
        GreenfootImage rotated = createGreenfootImageFromBI (newBi);
        return rotated;
    }

    public static GreenfootImage rotateCcw90 (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight(); 

        BufferedImage newBi = new BufferedImage (ySize, xSize, 3);

        for (int i = xSize-1; i >= 0; i--)
        {
            for (int j = 0; j < ySize; j++)
            {
                newBi.setRGB(j, xSize - i - 1, bi.getRGB(i, j));
            }
        }
        GreenfootImage rotated = createGreenfootImageFromBI (newBi);
        return rotated;
    }
    
    /**
     * Takes the image and increases the alpha value
     */
    public static void increaseOpacity (BufferedImage bi){
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();
        
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                if (alpha < 255) alpha +=5;
                alpha = Math.min(255,alpha);
                
                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }
        
    }
    
    /**
     * Takes the image and decreases the alpha value
     */
    public static void decreaseOpacity (BufferedImage bi){
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();
        
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                if (alpha > 0) alpha -=5;
                alpha = Math.max(0,alpha);
                
                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }
    }

    /**
     * Clones the original image and adds it to the list of images whenever a change is made
     */
    public static void addToList(ArrayList<BufferedImage> list, BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight(); 

        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);
                newBi.setRGB(x,y,rgb);
            }
        }
        
        list.add(newBi);
    }
    
    /**
     * Resets the factor variable whenever the reset button is clicked
     */
    public static void resetFactor()
    {
        factor = 2;
    }
    
    /**
    * Takes in a BufferedImage and returns a GreenfootImage.
    * Taken from Mr.Cohen
    * @param newBi The BufferedImage to convert.
    *
    * @return GreenfootImage A GreenfootImage built from the BufferedImage provided.
    */
    public static GreenfootImage createGreenfootImageFromBI (BufferedImage newBi)
    {
        GreenfootImage returnImage = new GreenfootImage (newBi.getWidth(), newBi.getHeight());
        BufferedImage backingImage = returnImage.getAwtImage();
        Graphics2D backingGraphics = (Graphics2D)backingImage.getGraphics();
        backingGraphics.drawImage(newBi, null, 0, 0);
        return returnImage;
    }
    
    /**
     * Takes in an rgb value - the kind that is returned from BufferedImage's
     * getRGB() method - and returns 4 integers for easy manipulation.
     * 
     * By Jordan Cohen
     * Version 0.2
     * 
     * @param rgbaValue The value of a single pixel as an integer, representing<br>
     *                  8 bits for red, green and blue and 8 bits for alpha:<br>
     *                  <pre>alpha   red     green   blue</pre>
     *                  <pre>00000000000000000000000000000000</pre>
     * @return int[4]   Array containing 4 shorter ints<br>
     *                  <pre>0       1       2       3</pre>
     *                  <pre>alpha   red     green   blue</pre>
     */
    public static int[] unpackPixel (int rgbaValue)
    {
        int[] unpackedValues = new int[4];
        // alpha
        unpackedValues[0] = (rgbaValue >> 24) & 0xFF;
        // red
        unpackedValues[1] = (rgbaValue >> 16) & 0xFF;
        // green
        unpackedValues[2] = (rgbaValue >>  8) & 0xFF;
        // blue
        unpackedValues[3] = (rgbaValue) & 0xFF;

        return unpackedValues;
    }

    /**
     * Takes in a red, green, blue and alpha integer and uses bit-shifting
     * to package all of the data into a single integer.
     * 
     * @param   int red value (0-255)
     * @param   int green value (0-255)
     * @param   int blue value (0-255)
     * @param   int alpha value (0-255)
     * 
     * @return int  Integer representing 32 bit integer pixel ready
     *              for BufferedImage
     */
    public static int packagePixel (int r, int g, int b, int a)
    {
        int newRGB = (a << 24) | (r << 16) | (g << 8) | b;
        return newRGB;
    }
}
