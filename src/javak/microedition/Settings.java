package javak.microedition;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fran
 */
public class Settings {

    // ## Screen Widths ##
    //#if ScreenWidth==96
//#     public static final short SCR_WIDTH = 96;
    //#elif ScreenWidth==101
//#     public static final short SCR_WIDTH = 101;
    //#elif ScreenWidth==120
//#     public static final short SCR_WIDTH = 120;
    //#elif ScreenWidth==128
//#     public static final short SCR_WIDTH = 128;
    //#elif ScreenWidth==130
//#     public static final short SCR_WIDTH = 130;
    //#elif ScreenWidth==132
//#     public static final short SCR_WIDTH = 132;
    //#elif ScreenWidth==180
//#     public static final short SCR_WIDTH = 180;
    //#elif ScreenWidth==176
//#     public static final short SCR_WIDTH = 176;
    //#elif ScreenWidth==208
//#     public static final short SCR_WIDTH = 208;
    //#elif ScreenWidth==240
    public static final short SCR_WIDTH = 240;
    //#elif ScreenWidth==320
//#     public static final short SCR_WIDTH = 320;
    //#elif ScreenWidth==352
//#     public static final short SCR_WIDTH = 352;
    //#elif ScreenWidth==360
//#     public static final short SCR_WIDTH = 360;
    //#elif ScreenWidth==480
//#     public static final short SCR_WIDTH = 480;
    //#elif ScreenWidth==640
//#     public static final short SCR_WIDTH = 640;
    //#endif
    
    // ## Screen Heights ##
    //#if ScreenHeight==65
//#     public static final short SCR_HEIGHT = 65;
    //#elif ScreenHeight==80
//#     public static final short SCR_HEIGHT = 80;
    //#elif ScreenHeight==116
//#     public static final short SCR_HEIGHT = 116;
    //#elif ScreenHeight==128
//#     public static final short SCR_HEIGHT = 128;
    //#elif ScreenHeight==130
//#     public static final short SCR_HEIGHT = 130;
    //#elif ScreenHeight==140
//#     public static final short SCR_HEIGHT = 140;
    //#elif ScreenHeight==144
//#     public static final short SCR_HEIGHT = 144;
    //#elif ScreenHeight==147
//#     public static final short SCR_HEIGHT = 147;
    //#elif ScreenHeight==148
//#     public static final short SCR_HEIGHT = 148;
    //#elif ScreenHeight==149
//#     public static final short SCR_HEIGHT = 149;
    //#elif ScreenHeight==160
//#     public static final short SCR_HEIGHT = 160;
    //#elif ScreenHeight==176
//#     public static final short SCR_HEIGHT = 176;
    //#elif ScreenHeight==177
//#     public static final short SCR_HEIGHT = 177;
    //#elif ScreenHeight==192
//#     public static final short SCR_HEIGHT = 192;
    //#elif ScreenHeight==182
//#     public static final short SCR_HEIGHT = 182;
    //#elif ScreenHeight==198
//#     public static final short SCR_HEIGHT = 198;
    //#elif ScreenHeight==200
//#     public static final short SCR_HEIGHT = 200;
    //#elif ScreenHeight==204
//#     public static final short SCR_HEIGHT = 204;
    //#elif ScreenHeight==205
//#     public static final short SCR_HEIGHT = 205;
    //#elif ScreenHeight==206
//#     public static final short SCR_HEIGHT = 206;
    //#elif ScreenHeight==208
//#     public static final short SCR_HEIGHT = 208;
    //#elif ScreenHeight==220
//#     public static final short SCR_HEIGHT = 220;
    //#elif ScreenHeight==224
//#     public static final short SCR_HEIGHT = 224;
    //#elif ScreenHeight==240
//#     public static final short SCR_HEIGHT = 240;
    //#elif ScreenHeight==260
//#     public static final short SCR_HEIGHT = 260;
    //#elif ScreenHeight==276
//#     public static final short SCR_HEIGHT = 276;
    //#elif ScreenHeight==290
//#     public static final short SCR_HEIGHT = 290;
    //#elif ScreenHeight==294
//#     public static final short SCR_HEIGHT = 294;
    //#elif ScreenHeight==297
//#     public static final short SCR_HEIGHT = 297;
    //#elif ScreenHeight==298
//#     public static final short SCR_HEIGHT = 298;
    //#elif ScreenHeight==300
//#     public static final short SCR_HEIGHT = 300;
    //#elif ScreenHeight==302
//#     public static final short SCR_HEIGHT = 302;
    //#elif ScreenHeight==304
//#     public static final short SCR_HEIGHT = 304;
    //#elif ScreenHeight==305
//#     public static final short SCR_HEIGHT = 305;
    //#elif ScreenHeight==307
//#     public static final short SCR_HEIGHT = 307;
    //#elif ScreenHeight==320
    public static final short SCR_HEIGHT = 320;
    //#elif ScreenHeight==350
//#     public static final short SCR_HEIGHT = 350;
    //#elif ScreenHeight==360
//#     public static final short SCR_HEIGHT = 360;
    //#elif ScreenHeight==380
//#     public static final short SCR_HEIGHT = 380;
    //#elif ScreenHeight==378
//#     public static final short SCR_HEIGHT = 378;
    //#elif ScreenHeight==400
//#     public static final short SCR_HEIGHT = 400;
    //#elif ScreenHeight==416
//#     public static final short SCR_HEIGHT = 416;
    //#elif ScreenHeight==480
//#     public static final short SCR_HEIGHT = 480;
    //#elif ScreenHeight==640
//#     public static final short SCR_HEIGHT = 640;
    //#elif ScreenHeight==800
//#     public static final short SCR_HEIGHT = 800;
    //#endif
    
    // Bitmap folders (must be allocated in assets folder)
    public static final String[] BITMAP_FOLDER = {
        "drawable-xxldpi/",
        "drawable-xldpi/",
        "drawable-ldpi/",
        "drawable-mdpi/",
        "drawable-hdpi/",
        "drawable-xhdpi/",
    };

    public static final String[] ORIENTATION_FOLDER = {
        "landscape/",
        "portrait/",
    };
    
    // Screen size
    public static final int SQCIF_128X128 = 0;
    public static final int QCIF_176X220 = 1;
    public static final int QVGA_240X320 = 2;
    public static final int HVGA_320X480 = 3;
    public static final int WVGA_480X800 = 4;
    public static final int WXGA_800X1280 = 5;
    
    public static final int[][] SCREEN_SIZES = {
        {128, 128},
        {176, 220},
        {240, 320},
        {320, 480},
        {480, 800},
        {800, 1280},
    };
   
    //#if SIZE == "Small"
//#     public static final int ms_iSize = SQCIF_128X128;
    //#elif SIZE == "Standard"
//#     public static final int ms_iSize = QCIF_176X220;
    //#elif SIZE == "Large"
    public static final int ms_iSize = QVGA_240X320;
    //#elif SIZE == "Extra"
//#     public static final int ms_iSize = HVGA_320X480;
    //#elif SIZE == "XXL"
//#     public static final int ms_iSize = WVGA_480X800;
    //#endif
    
    /**
     * Gets the screen width. This is a hardcode value for each family
     */
    public static short getWidth() {
        return SCR_WIDTH;
    }

    /**
     * Gets the screen height. This is a hardcode value for each family
     */
    public static short getHeight() {
        return SCR_HEIGHT;
    }
    
    /**
     * Gets the screen set. In J2ME all screen resolutions are divided into 5 big graphic groupes: 128x128, 176x220, 240x320, 320x480, 480x800\n Each resolution scales the previous one in a 150%
     */
    public static int getSizeSet() {
        return ms_iSize;
    }
    
    /**
     * Gets the screen orientation. This is a hardcode value for each family
     */
    public static int getScreenOrientation() {
        return -1;
    }
}
