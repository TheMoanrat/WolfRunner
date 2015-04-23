package com.kitmaker.manager;

/**
 *
 * @author Mr Matsusaka
 */

// abilities
// Fonts = GfxFonts  (graphic fonts)
// Fonts = SystemFonts  (device default fonts)

//#define UseLatin
///#define UseCzech
///#define UseArabic
///#define UseRussian
///#define UseBengali

// move effect from Bobolinkies
///#define UseEffectDeltaMove 

import com.kitmaker.wolfRunner.Debug;
import com.kitmaker.wolfRunner.Define;
import com.kitmaker.wolfRunner.WolfRunner;
import java.io.DataInputStream;
import java.io.IOException;
import javak.microedition.Settings;
import javak.microedition.lcdui.Font;
import javak.microedition.lcdui.Graphics;
import javak.microedition.lcdui.Image;

/**
 * The FntManager class is a base class for writing graphical or system fonts.
 * It works in parallel with the Kitmaker Font Engraver. <P> The Font Engraver
 * is a very powerful tool designed with the idea of creating fonts easily for
 * mobile content. The exporter can generates all the fonts needed for all the
 * set of graphics with only one click, and organize them in a comfortable way
 * for the programmer. The Font Engraver will create a .png file, and a .dat
 * file with all the coordinates’ data. It also creates a 21 byte small file for
 * week memory devices, to draw a similar font with the system fonts instead of
 * graphics. <P> The fonts that the engine will work with needs to be edited
 * inside the font editor. Constants for these indexes should be created and
 * used to point to the right font. <P> You need to add "Fonts" label in the
 * abilities. The values can be "GfxManager" for graphical fonts or
 * "SystemFonts" for using device fonts instead. Don't forget to add proper
 * folder depending of the drawing mode.
 */
public class FntManager {

    // Settings
    public static final boolean USE_DELTA = false;
    
    // FONTS ////////////////////////////////////////////////////////////////////
    //#if d500 || tripletslp || tripletshp
//#     public static final int FONT_SMALL = 0;
//#     public static final int FONT_NORMAL = 1;
//#     public static final int FONT_BIG = 1;
    //#else
    public static final int FONT_SMALL = 0;
    public static final int FONT_NORMAL = 1;
    public static final int FONT_BIG = 2;
    //#endif
    
    // graphic fonts
    private static final String FONT_URL[] = {
        "font1",
        "font2",
        "font3",
    };

    public static final String BASE_SIZE [] = {
        "128", "176", "240", "320", "480",
    };
    
    private static final byte NUM_FONTS = (byte) FONT_URL.length;
    
    /////////////////////////////////////////////////////////////////////////////
    // system font
    private static Font[] ms_SystemFont = new Font[NUM_FONTS];
    private static int SYSTEM_FONTS[][] = new int[NUM_FONTS][3];
    private static int SYSTEM_STROKE[][] = new int[NUM_FONTS][];
    private static int SYSTEM_SHADOW[][] = new int[NUM_FONTS][];
    
    private static final int[] FNTSYS_MASK_SIZE = {
        //#if API == "Motorola"
//#         Font.SIZE_SMALL,  // small
//#         Font.SIZE_SMALL,  // medium
//#         Font.SIZE_MEDIUM, // large
        //#else
        Font.SIZE_SMALL, // small
        Font.SIZE_MEDIUM, // medium
        Font.SIZE_LARGE, // large
        //#endif
    };
    
    private static final int[] FNTSYS_MASK_STYLE = {
        Font.STYLE_PLAIN,
        Font.STYLE_BOLD,
        Font.STYLE_ITALIC, 
    };
    
    // font height
    private static byte[] FNTSYS_HEIGHT = new byte[NUM_FONTS];
    private static byte[] FNTSYS_BASELI = new byte[NUM_FONTS];
    private static byte[] FNTSYS_MAXHEIGHT = new byte[NUM_FONTS];
    
    //#if Fonts == "GfxFont"
    static final byte FNTDATA_X = 0;
    static final byte FNTDATA_W = 1;
    static final byte FNTDATA_Y = 2;
    static final byte FNTDATA_H = 3;
    static final byte FNTDATA_OFFSET_Y = 4;
    static final byte FNTDATA_OFFSET_X = 5;
    static final byte FNTDATA_ADVANCE = 6;
    static final byte NUM_FNTDATA = 7;
    
    static short[][][] ms_iCharData = new short[NUM_FONTS][][];
    private static byte[][][] FNTGFX_LINE_DATA = new byte[NUM_FONTS][][];
    private static short[][][] FNTGFX_CHARPOS_X = new short[NUM_FONTS][][];
    private static String[] ms_zCharSet = new String[NUM_FONTS];

    private static byte[][] FNTGFX_OFFSET_X = new byte[NUM_FONTS][];
    private static byte[][] FNTGFX_ADVANCE_X = new byte[NUM_FONTS][];
    
    // font height
    private static byte[] FNTGFX_HEIGHT = new byte[NUM_FONTS];
    private static byte[] FNTGFX_BASELI = new byte[NUM_FONTS];
    private static byte[] FNTGFX_MAXHEIGHT = new byte[NUM_FONTS];
    //#endif
    
    public static byte[] FNT_SPACINGX = new byte[NUM_FONTS];
    public static byte[] FNT_HEIGHT = new byte[NUM_FONTS];
    public static byte[] FNT_BASELI = new byte[NUM_FONTS];
    public static byte[] FNT_MAXHEIGHT = new byte[NUM_FONTS];

    /**
     * This funtion should be used if all fonts will be loaded at the same time
     * during all the aplication running. It must to be called at the beggining
     * of the run. <P> If you need to load and unload fonts dynamically you
     * should use LoadFont() and DeleteFont() instead
     */
    public static void InitFontManager() {
        //#if d500
//#         LoadFont(1);
        //#else
        for (int i = 0; i < NUM_FONTS; i++) {
            LoadFont(i);
        }
        //#endif
    }

    /**
     * To delete a especific font
     *
     * @param _iIndex font to delete
     */
    public static void DeleteFont(int _iIndex) {

        //#if Debug
//#       Debug.DebugMsg ("FntManager - deleteFont index: " + _iIndex);
        //#endif

        FntManager.ms_vFont[_iIndex][0] = null;

        System.gc();

        //#if Fonts == "GfxFont"
        ms_zCharSet[_iIndex] = null;
        FNTGFX_CHARPOS_X[_iIndex] = null;
        FNTGFX_LINE_DATA[_iIndex] = null;
        //#else
        //#endif

        System.gc();
    }

    /**
     * To load a especific font
     *
     * @param _iIndex font to load
     */
    public static void LoadFont(int _iIndex) {
        
        int j;
        DataInputStream vInputS = null;

        //#if Debug
//#         Debug.DebugMsg ("FntManager.LoadFont() load font number: " + _iIndex);
//#         Debug.DebugMsg ("FntManager.LoadFont() - font image load");
        //#endif

        // Load fonts
        //#if Fonts == "GfxFont"
        int iCharRow, iCharCodeAcum, iCharPosYGfx;
        String zBaseSize = "-" + BASE_SIZE[Settings.ms_iSize];

        try {

            // load image
            try {
                FntManager.ms_vFont[_iIndex][0] = Image.createImage("/" + FONT_URL[_iIndex] + zBaseSize + ".png");
            } catch (Exception ex) {
                zBaseSize = "";
                if (FntManager.ms_vFont[_iIndex][0] == null)
                    FntManager.ms_vFont[_iIndex][0] = Image.createImage("/" + FONT_URL[_iIndex] + ".png");
            }

            //#if Debug
//#             if (FntManager.ms_vFont[_iIndex][0] != null)
//#                 Debug.DebugMsg ("FntManager.LoadFont() - font image loaded OK");
//#             else {
//#                 Debug.DebugMsg ("FntManager.LoadFont() - font image load FAILED; Are you sure the correct file is in the correct folder?");
//#                 return;
//#             }
//#             
//#             Debug.DebugMsg ("FntManager.LoadFont() - font image loaded OK");
//#             Debug.DebugMsg ("FntManager.LoadFont() - coordinate data load");
            //#endif

            System.gc();

            Debug.DebugMsg ("FntManager.inst - " + WolfRunner.ms_vInstance);

            // load coordenates
            vInputS = new DataInputStream(WolfRunner.ms_vInstance.getClass().getResourceAsStream("/" + FONT_URL[_iIndex] + zBaseSize + ".dat"));
            
            //#if Debug
//#             if (vInputS.available() > 0)
//#                 Debug.DebugMsg ("FntManager.LoadFont() - font data file loaded OK");
//#             else {
//#                 Debug.DebugMsg ("FntManager.LoadFont() - font data file load FAILED; Are you sure the correct file is in the correct folder?");
//#                 return;
//#             }
            //#endif

            // font name
            byte iFontNameLenght = vInputS.readByte();
            //#if Debug
//#             String zFontName = "";
//#             for (int i = 0; i < iFontNameLenght; i++) {
//#                 zFontName += vInputS.readChar();
//#             }
//#             Debug.DebugMsg("FntManager.LoadFont() - FONT NAME: " + zFontName);
            //#else
            vInputS.skipBytes(iFontNameLenght * 2);
            //#endif

            // read char set
            ms_zCharSet[_iIndex] = "";
            short iCharSetLenght = vInputS.readShort();
            for (j = 0; j < iCharSetLenght; j++) {
                ms_zCharSet[_iIndex] += String.valueOf(vInputS.readChar());
            }

            // read char pos x
            short iCharXLenght = vInputS.readShort();
            FNTGFX_CHARPOS_X[_iIndex] = new short[iCharXLenght][];
            for (int s = 0; s < iCharXLenght; s++) {
                short iSubCharXLenght = vInputS.readShort();
                FNTGFX_CHARPOS_X[_iIndex][s] = new short[iSubCharXLenght];
                for (int t = 0; t < iSubCharXLenght; t++) {
                    FNTGFX_CHARPOS_X[_iIndex][s][t] = vInputS.readShort();
                }
            }

            // read line data
            short iLineDataLenght = vInputS.readShort();
            FNTGFX_LINE_DATA[_iIndex] = new byte[iLineDataLenght][];
            for (int s = 0; s < iLineDataLenght; s++) {
                short iSubLineDataLenght = vInputS.readShort();
                FNTGFX_LINE_DATA[_iIndex][s] = new byte[iSubLineDataLenght];
                for (int t = 0; t < iSubLineDataLenght; t++) {
                    FNTGFX_LINE_DATA[_iIndex][s][t] = vInputS.readByte();
                }
            }

            // read spacing x
            FNT_SPACINGX[_iIndex] = vInputS.readByte();
            ms_iApostropheeCaseH = vInputS.readByte();
            ms_iLetterHeight = vInputS.readByte();

            //#if SIZE == "Small" || SIZE == "Standard"
//#             FNT_SPACINGX[_iIndex] = 0;
            //#endif
            
            //#if SIZE == "Small"
//#             ms_iLetterHeight++;
            //#endif

            // system fonts data
            int iSize = vInputS.readShort();

            //#if Debug
//#             Debug.DebugMsg("FntManager.LoadFont() - FONT SIZE: " + iSize);
            //#endif

            SYSTEM_FONTS[_iIndex][0] = Math.min(2, iSize / Define.SIZEY8);
            SYSTEM_FONTS[_iIndex][1] = vInputS.readByte();
            SYSTEM_FONTS[_iIndex][2] = vInputS.readInt();

            byte iStrokeSize = vInputS.readByte();
            byte iShadowSizeX = vInputS.readByte();
            byte iShadowSizeY = vInputS.readByte();

            int iStrokeColor = vInputS.readInt();
            int iShadowColor = vInputS.readInt();

            if (iStrokeSize > 0) {
                SYSTEM_STROKE[_iIndex] = new int[3];
                SYSTEM_STROKE[_iIndex][0] = iStrokeColor;
                SYSTEM_STROKE[_iIndex][1] = -1;
                SYSTEM_STROKE[_iIndex][2] = -1;
            }

            if (iShadowSizeX != 0 && iShadowSizeY != 0) {
                int size = 1;
                SYSTEM_SHADOW[_iIndex] = new int[3];
                SYSTEM_SHADOW[_iIndex][0] = iShadowColor;
                SYSTEM_SHADOW[_iIndex][1] = 1;
                SYSTEM_SHADOW[_iIndex][2] = 1;
            }

            // read chars advance & xOffset
            int iNumLetters = vInputS.readShort();

            FNTGFX_ADVANCE_X[_iIndex] = new byte[iNumLetters];
            FNTGFX_OFFSET_X[_iIndex] = new byte[iNumLetters];
            for (int i = 0; i < iNumLetters; i++) {
                FNTGFX_ADVANCE_X[_iIndex][i] = vInputS.readByte();
                FNTGFX_OFFSET_X[_iIndex][i] = vInputS.readByte();
            }
            
            //#if Debug
//#             Debug.DebugMsg ("FntManager.LoadFont() - coordinate data load OK");
            //#endif
            
        } catch (Exception ex) {
            //#if Debug
//#             Debug.DebugMsg ("(E) FntManager.LoadFont() - ERROR Preprocesing fonts. Excpt:: " + ex.toString());
            //#endif
        }
        //#else
//#         try {
//#             vInputS = new DataInputStream (Skeleton.ms_vInstance.getClass().getResourceAsStream ("/" + FONT_URL[_iIndex] + "-" + BASE_SIZE[Settings.ms_iSize] + ".syf"));
//#             if (vInputS.available() == 0)
//#                 vInputS = new DataInputStream (Skeleton.ms_vInstance.getClass().getResourceAsStream ("/" + FONT_URL[_iIndex] + ".syf"));
//#             
//#             int iSize = vInputS.readShort();
//#             SYSTEM_FONTS[_iIndex][0] = Math.min (2, iSize/Define.SIZEY8);
//#             SYSTEM_FONTS[_iIndex][1] = vInputS.readByte();
//#             SYSTEM_FONTS[_iIndex][2] = vInputS.readInt();
//# 
//#             byte iStrokeSize = vInputS.readByte();
//#             byte iShadowSizeX = vInputS.readByte();
//#             byte iShadowSizeY = vInputS.readByte();
//# 
//#             int iStrokeColor = vInputS.readInt();
//#             int iShadowColor = vInputS.readInt();
//# 
//#             if (iStrokeSize > 0) {
//#                 SYSTEM_STROKE[_iIndex] = new int [1];
//#                 SYSTEM_STROKE[_iIndex][0] = iStrokeColor; 
//#             }
//# 
//#             if (iShadowSizeX != 0 && iShadowSizeY != 0) {
//#                 int size = 1;
                //#if OptSpeed == 0
//#                 if (iStrokeSize > 0) {
//#                     size = 2;
//#                 }
                //#endif
//#                 SYSTEM_SHADOW[_iIndex] = new int[3];
//#                 SYSTEM_SHADOW[_iIndex][0] = iShadowColor;
//#                 SYSTEM_SHADOW[_iIndex][1] = iShadowSizeX > 0 ? size : -1;
//#                 SYSTEM_SHADOW[_iIndex][2] = iShadowSizeY > 0 ? size : -1;
//#             }
//#         }
//#         catch (Exception ex) {
//#             SYSTEM_FONTS[_iIndex][2] = 0xffffffff;
//#         }
        //#endif

        // close input stream
        try {
            if (vInputS != null)
                vInputS.close();
        } catch (IOException ex) {
        }

        // ## System fonts

        // In Nextel monospace font is smaller
        //#if API == "Nextel"
//#         int iFace = Font.FACE_PROPORTIONAL;
        //#else
        int iFace = Font.FACE_SYSTEM;
        //#endif

        // We create the font
        ms_SystemFont[_iIndex] = Font.getFont(iFace, FNTSYS_MASK_STYLE[SYSTEM_FONTS[_iIndex][1]], FNTSYS_MASK_SIZE[SYSTEM_FONTS[_iIndex][0]]);
        


        // check for the max system font height
        FNTSYS_MAXHEIGHT[_iIndex] = (byte) ms_SystemFont[_iIndex].getHeight();
        FNTSYS_HEIGHT[_iIndex] = (byte) ms_SystemFont[_iIndex].getHeight();
        FNTSYS_BASELI[_iIndex] = (byte) ms_SystemFont[_iIndex].getBaselinePosition();

        System.arraycopy(FNTSYS_HEIGHT, 0, FNT_HEIGHT, 0, FNTSYS_HEIGHT.length);
        System.arraycopy(FNTSYS_BASELI, 0, FNT_BASELI, 0, FNTSYS_BASELI.length);
        System.arraycopy(FNTSYS_MAXHEIGHT, 0, FNT_MAXHEIGHT, 0, FNTSYS_MAXHEIGHT.length);

        // check for the max gfx font height
        //#if Fonts=="GfxFont"
        FNTGFX_HEIGHT[_iIndex] = FNTGFX_LINE_DATA[_iIndex][0][0];
        FNTGFX_BASELI[_iIndex] = FNTGFX_LINE_DATA[_iIndex][0][1];

        FNTGFX_MAXHEIGHT[_iIndex] = 0;
        for (j = 0; j < FNTGFX_LINE_DATA[_iIndex].length; j++) {
            if (FNTGFX_LINE_DATA[_iIndex][j][0] > FNTGFX_MAXHEIGHT[_iIndex]) {
                FNTGFX_MAXHEIGHT[_iIndex] = FNTGFX_LINE_DATA[_iIndex][j][0];
            }
        }

        System.arraycopy(FNTGFX_HEIGHT, 0, FNT_HEIGHT, 0, FNT_HEIGHT.length);
        System.arraycopy(FNTGFX_BASELI, 0, FNT_BASELI, 0, FNT_BASELI.length);
        System.arraycopy(FNTGFX_MAXHEIGHT, 0, FNT_MAXHEIGHT, 0, FNT_MAXHEIGHT.length);
        //#endif


        // Set fonts coordenates to get some optimization
        //   de forma más optimizada).
        //#if Fonts=="GfxFont"
        try {
            ms_iCharData[_iIndex] = new short[ms_zCharSet[_iIndex].length()][NUM_FNTDATA];
            int iGlobalIndex = 0;

            for (j = 0; j < ms_zCharSet[_iIndex].length(); j++) {
                iCharRow = iCharCodeAcum = iCharPosYGfx = 0;

                while ((iCharRow < FNTGFX_CHARPOS_X[_iIndex].length)
                        && (j >= (iCharCodeAcum + FNTGFX_CHARPOS_X[_iIndex][iCharRow].length - 1))) {

                    iCharCodeAcum += (FNTGFX_CHARPOS_X[_iIndex][iCharRow].length - 1);  // El último valor de cada fila no cuenta como carácter
                    iCharPosYGfx += FNTGFX_LINE_DATA[_iIndex][iCharRow][0];
                    iCharRow++;
                }

                // Coord. X en gráfico
                ms_iCharData[_iIndex][j][FNTDATA_X] = FNTGFX_CHARPOS_X[_iIndex][iCharRow][j - iCharCodeAcum];
                // Ancho
                ms_iCharData[_iIndex][j][FNTDATA_W] = (short) (FNTGFX_CHARPOS_X[_iIndex][iCharRow][j - iCharCodeAcum + 1] - FNTGFX_CHARPOS_X[_iIndex][iCharRow][j - iCharCodeAcum]);
                // Coord. Y en gráfico
                ms_iCharData[_iIndex][j][FNTDATA_Y] = (short) iCharPosYGfx;
                // Alto
                ms_iCharData[_iIndex][j][FNTDATA_H] = FNTGFX_LINE_DATA[_iIndex][iCharRow][0];
                // Dif. entre línea base del carácter (según renglón) y línea base máxima (según fuente).
                ms_iCharData[_iIndex][j][FNTDATA_OFFSET_Y] = (short) (FNTGFX_BASELI[_iIndex] - FNTGFX_LINE_DATA[_iIndex][iCharRow][1]);

                ms_iCharData[_iIndex][j][FNTDATA_OFFSET_X] = FNTGFX_OFFSET_X[_iIndex][iGlobalIndex];

                ms_iCharData[_iIndex][j][FNTDATA_ADVANCE] = FNTGFX_ADVANCE_X[_iIndex][iGlobalIndex];

                iGlobalIndex++;
            }
        } catch (Exception e) {
            //#if Debug
//#             Debug.DebugMsg ("(E) FntManager.LoadFont() - ERROR Preprocesing coordinates. Excpt: "+e.getMessage ()+"("+e.toString ()+")");
            //#endif
        }

        FNTGFX_LINE_DATA[_iIndex] = null;
        FNTGFX_CHARPOS_X[_iIndex] = null;

        //#endif

    }

    /**
     * Get specified string width
     *
     * @param _iFontID The font ID to get width
     * @param _zString The string to get width
     * @return font height
     */
    static int StringWidth(int _iFontID, String _zString) {
        //#if Fonts == "GfxFont"
        if (IsGfxFontWritable(_zString)) {
            int iAnchoFrase = 0;
            int iNumCharsFrase = _zString.length();

            if (iNumCharsFrase == 0) {
                return 0;
            }
            
            int iChar, iArrayIndex;
            for (int i = 0; i < iNumCharsFrase; i++) {
                iChar = _zString.charAt(i);
                iArrayIndex = Font_GetArrayCode(_iFontID, iChar) & 0x0FF;
                // barra, numeros, dospuntos, punto y coma...
                iAnchoFrase +=
                        + ms_iCharData[_iFontID][iArrayIndex][FNTDATA_ADVANCE]
                        //- ms_iCharData[_iFontID][iArrayIndex][FNTDATA_OFFSET_X] 
                        + FNT_SPACINGX[_iFontID];
            }
            ms_iApostrophe = 0;
            ms_iApostropheExtraY = 0;

            return iAnchoFrase;

        } else {
            return ms_SystemFont[_iFontID].stringWidth(_zString);
        }
        //#else
//#         return ms_SystemFont[_iFontID].stringWidth(_zString);
        //#endif
    }

    /**
     * Get specified font height
     *
     * @param _iFontID The font ID to get height
     * @return font height
     */
    public static int GetHeight(int _iFontID) {
        //#if Fonts == "GfxFont"
        if (ms_iFontType == FTYPE_GRAPHIC) {
            return FNTGFX_MAXHEIGHT[_iFontID];
        } else 
        //#endif
        {
            return FNTSYS_MAXHEIGHT[_iFontID];
        }
    }

    /**
     * Get specified font baseline
     *
     * @param _iFontID The font ID to get baseline
     * @return font baseline
     */
    public static int GetBaseline(int _iFontID) {
        //#if Fonts == "GfxFont"
        if (ms_iFontType == FTYPE_GRAPHIC) {
            return FNTGFX_BASELI[_iFontID];
        } else
        //#endif
        {
            return FNTSYS_BASELI[_iFontID];
        }
    }
    
    // flags
    static final int PAL0 = 0;
    static final int PAL1 = 256;
    static final int PAL2 = 512;
    
    /**
     * Constant for centering text horizontally around the anchor point.
     * Graphics.HCENTER can be used instead.
     */
    public static final int HCENTER = Graphics.HCENTER;
    /**
     * Constant for positioning the anchor point of text to the right of the
     * text or image. Graphics.RIGHT can be used instead.
     */
    public static final int RIGHT = Graphics.RIGHT;
    /**
     * Constant for positioning the anchor point of text to the left of the text
     * or image. Graphics.LEFT can be used instead.
     */
    public static final int LEFT = Graphics.LEFT;
    /**
     * Constant for centering text vertically around the anchor point.
     * Graphics.VCENTER can be used instead.
     */
    public static final int VCENTER = Graphics.VCENTER;
    /**
     * Constant for positioning the anchor point at the baseline of text.
     * Graphics.BASELINE can be used instead.
     */
    public static final int BASELINE = Graphics.BASELINE;
    /**
     * Constant for positioning the anchor point of text above the text.
     * Graphics.TOP can be used instead.
     */
    public static final int TOP = Graphics.TOP;
    /**
     * Constant for activating delta effect move to the text.
     */
    public static final int DELTAMOVE = 128;
    /*
     * public static final int PAL0 = 0; public static final int PAL1 = 256;
     * public static final int PAL2 = 512;
     */
    
    static Image ms_vFont[][] = new Image[NUM_FONTS][5];
    static int ms_iApostrophe;
    static int ms_iApostropheExtraY;
    static int ms_iApostropheeCaseH;
    static int ms_iLetterHeight;

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Draws the specified String using the current font. The x,y position is
     * the position of the anchor point.
     *
     * @param _g Graphics object
     * @param _iFontID the font to be drawed
     * @param _zString the String to be drawed
     * @param _iPosX the x coordinate of the anchor point
     * @param _iPosY the y coordinate of the anchor point
     * @param _iAnchor the anchor point for positioning the text
     * @param _iChars the number of letters of the String to be drawed
     */
    public static void DrawFont(Graphics _g, int _iFontID, String _Frase, int _iPosX, int _iPosY,
            int _iAnchor, int _iChars) {

        //#if Fonts == "GfxFont"
        if (IsGfxFontWritable(_Frase)) {
            DrawGraphicFont(_g, _iFontID, _Frase, _iPosX, _iPosY, _iAnchor, _iChars);
        } else {
            DrawSystemFont(_g, _iFontID, _Frase, _iPosX, _iPosY, _iAnchor, _iChars);
        }

        //#else
//#         DrawSystemFont(_g, _iFontID, _Frase, _iPosX, _iPosY, _iAnchor, _iChars);
        //#endif
    }
    public static void DrawFont(Graphics _g, int _iFontID, String _Frase, int _iPosX, int _iPosY,
        int _iAnchor, int _iChars, int _iSize) {

        //#if Fonts == "GfxFont"
        if (IsGfxFontWritable(_Frase)) {
            DrawFontReg(_g, _iFontID, _Frase, _iPosX, _iPosY, _iAnchor, _iChars, _iSize);
        } else {
            DrawSystemFont(_g, _iFontID, _Frase, _iPosX, _iPosY, _iAnchor, _iChars);
        }

        //#else
//#         DrawSystemFont(_g, _iFontID, _Frase, _iPosX, _iPosY, _iAnchor, _iChars);
        //#endif
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////
    private static void DrawGraphicFont(Graphics _g, int _iFontID, String _Frase, int _iPosX, int _iPosY,
            int _iAnchor, int _iChars) {


        //#if Fonts == "GfxFont"
        int iPalette = (_iAnchor >> 8);

        int iIndex = 0;
        int iPosX = _iPosX;
        int iPosY = _iPosY;
        int iAnchoFraseStr = (_iChars >= 0) ? Math.min(_iChars, _Frase.length()) : _Frase.length();
        int iChar, iCurrentChar;

        if ((_iAnchor & HCENTER) != 0) {
            iPosX -= StringWidth(_iFontID, _Frase) >> 1;
        } else if ((_iAnchor & RIGHT) != 0) {
            iPosX -= StringWidth(_iFontID, _Frase);
        }

        if ((_iAnchor & BASELINE) != 0) {
            iPosY -= ms_iLetterHeight;
        } else if ((_iAnchor & VCENTER) != 0) {
            iPosY -= (ms_iLetterHeight >> 1);
        }


        //#ifdef UseEffectDeltaMove
//#         if ((_iAnchor & DELTAMOVE) != 0) {
//# 
//#             int deltaFrame = ((Main.ms_iFrame + _Frase.length()) % 64);
//# 
//#             while (iIndex < iAnchoFraseStr) {
//# 
//#                 iChar = _Frase.charAt(iIndex);
//#                 iChar = Font_GetArrayCode(_iFontID, iChar);
//# 
//#                 int delta_Y_Effect = DELTA_UPDOWN_MOVE[(deltaFrame + iIndex)];
//# 
//#                 //if (iPosX<Define.SIZEX)
//#                 {
//#                     iCurrentChar = iChar & 0x0FF;
//# 
//#                     _g.setClip(iPosX, iPosY + ms_iCharData[_iFontID][iCurrentChar][4] + delta_Y_Effect,
//#                             ms_iCharData[_iFontID][iCurrentChar][FNTDATA_W], ms_iCharData[_iFontID][iCurrentChar][FNTDATA_H]);
//# 
//#                     _g.drawImage(ms_vFont[_iFontID][0],
//#                             iPosX - ms_iCharData[_iFontID][iCurrentChar][FNTDATA_X] + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_X],
//#                             iPosY - ms_iCharData[_iFontID][iCurrentChar][FNTDATA_Y] + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_Y] + delta_Y_Effect, Graphics.LEFT | Graphics.TOP);
//#                 }
//# 
//#                 if (ms_iApostrophe != 0) {
//#                     _g.setClip(iPosX, iPosY + ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_OFFSET_Y] - ms_iApostropheExtraY,
//#                             ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_W], ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_H]);
//# 
//#                     _g.drawImage(ms_vFont[_iFontID][iPalette],
//#                             iPosX - ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_X],
//#                             iPosY - ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_Y] + ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_OFFSET_Y] - ms_iApostropheExtraY, Graphics.LEFT | Graphics.TOP);
//# 
//#                     ms_iApostrophe = 0;
//#                     ms_iApostropheExtraY = 0;
//#                 }
//# 
//#                 iPosX += ms_iCharData[_iFontID][iCurrentChar][FNTDATA_ADVANCE] + FNT_SPACINGX[_iFontID];
//#                 iIndex++;
//# 
//#             }
//# 
//#         } else
        //#endif
        {
            while (iIndex < iAnchoFraseStr) {

                iChar = _Frase.charAt(iIndex);
                iCurrentChar = Font_GetArrayCode(_iFontID, iChar) & 0x0FF;

                _g.setClip(
                        iPosX + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_X],
                        iPosY + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_Y],
                        ms_iCharData[_iFontID][iCurrentChar][FNTDATA_W],
                        ms_iCharData[_iFontID][iCurrentChar][FNTDATA_H]);

                _g.drawImage(ms_vFont[_iFontID][iPalette],
                        iPosX - ms_iCharData[_iFontID][iCurrentChar][FNTDATA_X] + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_X],
                        iPosY - ms_iCharData[_iFontID][iCurrentChar][FNTDATA_Y] + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_Y],
                        Graphics.LEFT | Graphics.TOP);


                if (ms_iApostrophe != 0) {

                    int iApostropheX = iPosX + (ms_iCharData[_iFontID][iCurrentChar][FNTDATA_W] >> 1) - (ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_W] >> 1);

                    _g.setClip(iApostropheX, iPosY + ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_OFFSET_Y] - ms_iApostropheExtraY,
                            ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_W], ms_iCharData[_iFontID][ms_iApostrophe][3]);

                    _g.drawImage(ms_vFont[_iFontID][iPalette],
                            iApostropheX - ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_X],
                            iPosY - ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_Y] + ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_OFFSET_Y] - ms_iApostropheExtraY,
                            Graphics.LEFT | Graphics.TOP);

                    ms_iApostrophe = 0;
                    ms_iApostropheExtraY = 0;

                }

                iPosX += ms_iCharData[_iFontID][iCurrentChar][FNTDATA_ADVANCE] + FNT_SPACINGX[_iFontID];
                iIndex++;
            }
        }

        _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
        //#endif
    }
    
    private static void DrawFontReg(Graphics _g, int _iFontID, String _Frase, int _iPosX, int _iPosY,
            int _iAnchor, int _iChars, int _iSize) {


        //#if Fonts == "GfxFont"
        int iPalette = (_iAnchor >> 8);

        int iIndex = 0;
        int iPosX = _iPosX;
        int iPosY = _iPosY;
        int iAnchoFraseStr = (_iChars >= 0) ? Math.min(_iChars, _Frase.length()) : _Frase.length();
        int iChar, iCurrentChar;

        if ((_iAnchor & HCENTER) != 0) {
            iPosX -= StringWidth(_iFontID, _Frase) >> 1;
        } else if ((_iAnchor & RIGHT) != 0) {
            iPosX -= StringWidth(_iFontID, _Frase);
        }

        if ((_iAnchor & BASELINE) != 0) {
            iPosY -= ms_iLetterHeight;
        } else if ((_iAnchor & VCENTER) != 0) {
            iPosY -= (ms_iLetterHeight >> 1);
        }
        {
            _g.setImageSize(_iSize, _iSize);
            while (iIndex < iAnchoFraseStr) {

                iChar = _Frase.charAt(iIndex);
                iChar = Font_GetArrayCode(_iFontID, iChar);

                {
                    iCurrentChar = iChar & 0x0FF;
                    
                    _g.drawRegion(ms_vFont[_iFontID][iPalette], //
                            ms_iCharData[_iFontID][iCurrentChar][FNTDATA_X], //ini x
                            ms_iCharData[_iFontID][iCurrentChar][FNTDATA_Y] + ms_iCharData[_iFontID][iCurrentChar][4], //ini y
                            ms_iCharData[_iFontID][iCurrentChar][FNTDATA_W], //width
                            ms_iCharData[_iFontID][iCurrentChar][FNTDATA_H], //height
                            0, 
                           _iPosX, _iPosY, //
                           _iAnchor);
                          
                    

                    /*
                    _g.setClip(iPosX, iPosY + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_Y],
                            ms_iCharData[_iFontID][iCurrentChar][FNTDATA_W], ms_iCharData[_iFontID][iCurrentChar][FNTDATA_H]);
                    _g.drawImage(ms_vFont[_iFontID][iPalette],
                            iPosX - ms_iCharData[_iFontID][iCurrentChar][FNTDATA_X],
                            iPosY - ms_iCharData[_iFontID][iCurrentChar][FNTDATA_Y] + ms_iCharData[_iFontID][iCurrentChar][FNTDATA_OFFSET_Y], Graphics.LEFT | Graphics.TOP);
                            * 
                            */

                }

                /*
                if (ms_iApostrophe != 0) {

                    int iApostropheX = iPosX + (ms_iCharData[_iFontID][iCurrentChar][FNTDATA_W] >> 1) - (ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_W] >> 1);

                    _g.setClip(iApostropheX, iPosY + ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_OFFSET_Y] - ms_iApostropheExtraY,
                            ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_W], ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_H]);

                    _g.drawImage(ms_vFont[_iFontID][iPalette],
                            iApostropheX - ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_X],
                            iPosY - ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_Y] + ms_iCharData[_iFontID][ms_iApostrophe][FNTDATA_OFFSET_Y] - ms_iApostropheExtraY, Graphics.LEFT | Graphics.TOP);

                    ms_iApostrophe = 0;
                    ms_iApostropheExtraY = 0;

                }
                * 
                */

                iPosX += ms_iCharData[_iFontID][iCurrentChar][FNTDATA_ADVANCE] + FNT_SPACINGX[_iFontID];
                iIndex++;

            }
            _g.setImageSize(256, 256);
        }

        _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
        //#endif
    }
    
    private static final int SHADOW_PIXELS = Math.max(1, Define.SIZEX / 240);
    
    
    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////
    static void DrawSystemFont(
            Graphics _g, int _iFontID, String _Frase,
            int _iPosX, int _iPosY, int _iAnchor, int _iChars) {

        int iAnchoFraseStr = (_iChars >= 0) ? Math.min(_iChars, _Frase.length()) : _Frase.length();

        _g.setFont(ms_SystemFont[_iFontID]);
        
        if ((_iAnchor & VCENTER) != 0) {
            _iPosY -= ms_SystemFont[_iFontID].getHeight() >> 1;
            _iAnchor ^= VCENTER;
            _iAnchor |= TOP;
        }
        
        if (SYSTEM_SHADOW[_iFontID] != null) {
            _g.setColor(SYSTEM_SHADOW[_iFontID][0]);
            _g.drawString(_Frase.substring(0, iAnchoFraseStr), _iPosX + SYSTEM_SHADOW[_iFontID][1], _iPosY + SYSTEM_SHADOW[_iFontID][2], _iAnchor);
        }

        if (SYSTEM_STROKE[_iFontID] != null) {
            _g.setColor(SYSTEM_STROKE[_iFontID][0]);
            _g.drawString(_Frase.substring(0, iAnchoFraseStr), _iPosX + SYSTEM_STROKE[_iFontID][1], _iPosY + SYSTEM_STROKE[_iFontID][2], _iAnchor);
        }

        _g.setColor(SYSTEM_FONTS[_iFontID][2]);
        _g.drawString(_Frase.substring(0, iAnchoFraseStr), _iPosX, _iPosY, _iAnchor);
        
    }
    /////////////////////////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////////////////////////
    //#if UseLatin
    private static final char APOSTROPHE_LATIN[][] = {
        {0x60, 0xb4, 0x2c6, 0x2dc, 0xa8}, // a, o
        {0x60, 0xb4, 0x2c6, 0xa8}, // e, i, u
    };
    //#endif

    //#if Fonts == "GfxFont"
    public static int Font_GetArrayCode(int _iFontID, int _iCharCode) {
        int iFinalArrayCode = 0;
        try {
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf(_iCharCode);
        } catch (Exception ex) {
            System.err.checkError();
        }

        // apostrophes latin
        //#if UseLatin
        if ((_iCharCode >= 0xC0) && (_iCharCode <= 0xC4)) {  // À .. Ä
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("A");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[0][_iCharCode - 0xC0]);
            ms_iApostropheExtraY = ms_iApostropheeCaseH;

        } else if ((_iCharCode >= 0xC8) && (_iCharCode <= 0xCB)) {  // È .. Ë
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("E");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[1][_iCharCode - 0xC8]);
            ms_iApostropheExtraY = ms_iApostropheeCaseH;

        } else if ((_iCharCode >= 0xCC) && (_iCharCode <= 0xCF)) {  // Ì .. Ï
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("I");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[1][_iCharCode - 0xCC]);
            ms_iApostropheExtraY = ms_iApostropheeCaseH;

        } else if ((_iCharCode >= 0xD2) && (_iCharCode <= 0xD6)) {  // Ò .. Ö
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("O");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[0][_iCharCode - 0xD2]);
            ms_iApostropheExtraY = ms_iApostropheeCaseH;

        } else if ((_iCharCode >= 0xD9) && (_iCharCode <= 0xDC)) {  // Ù .. Ü
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("U");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[1][_iCharCode - 0xD9]);
            ms_iApostropheExtraY = ms_iApostropheeCaseH;

        } else if ((_iCharCode >= 0xE0) && (_iCharCode <= 0xE4)) {  // á .. ä
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("a");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[0][_iCharCode - 0xE0]);

        } else if ((_iCharCode >= 0xE8) && (_iCharCode <= 0xEB)) {  // è .. ë
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("e");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[1][_iCharCode - 0xE8]);

        } else if ((_iCharCode >= 0xEC) && (_iCharCode <= 0xEF)) {  // ì .. ï
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf(0x131);
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[1][_iCharCode - 0xEC]);

        } else if ((_iCharCode >= 0xF2) && (_iCharCode <= 0xF6)) {  // ò .. ö
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("o");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[0][_iCharCode - 0xF2]);

        } else if ((_iCharCode >= 0xF9) && (_iCharCode <= 0xFC)) {  // ù .. ü
            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("u");
            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[1][_iCharCode - 0xF9]);
        }
        //#endif

        // apostrophee czech
        //#if UseCzech
//#         else if (_iCharCode == 0xDD) {  // Ý
//#            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("Y");
//#            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[0][0]);
//#            ms_iApostropheExtraY = ms_iApostropheeCaseH;
//#          
//#         } else if(_iCharCode == 0xFD) {  // ý
//#            iFinalArrayCode = ms_zCharSet[_iFontID].indexOf("y");
//#            ms_iApostrophe = ms_zCharSet[_iFontID].indexOf(APOSTROPHE_LATIN[0][0]);
//#         }
        //#endif

        if (iFinalArrayCode == -1) {
            iFinalArrayCode = ms_zCharSet[_iFontID].length() - 1;
        }

        return iFinalArrayCode;

    }
    //#endif
    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////
    public static final int FTYPE_SYSTEM = 0;
    public static final int FTYPE_GRAPHIC = 1;
    private static int ms_iFontType = FTYPE_GRAPHIC;

    /**
     * Set font type <P> Set the type of font to draw. It can be FTYPE_SYSTEM or
     * FTYPE_GRAPHIC. If the ability Fonts value is "GfxFont", otherwise it will
     * be ignored. This function should be used if we're going to write
     * languages that are not supported in the graphic font (like arabic,
     * chinese, etc...) <P>
     *
     * @param _iType the type of font to draw
     */
    public static void setFontType(int _iType) {
        ms_iFontType = _iType;
    }
    /**
    * @param Return the type font
    */
    public static int getFontType() {
        return ms_iFontType;
    }
    /**
     * setSystemColor <P>  Set a color only the default system font.
     * @param _index_font
     * @param _color 
     */
    public static void setSystemColor(int _index_font, int _color){
        SYSTEM_FONTS[_index_font][2] = _color;
    }
    /**
     * getSystemColor <P>  Get a color only the default system font.
     * @param _index_font
     */
    public static int getSystemColor(int _index_font){
        return SYSTEM_FONTS[_index_font][2];
    }
    
    //#if Fonts == "GfxFont"
    /**
     * IsGfxFontWritable <P> Checks if given string is writable with the current loaded font
     *
     * @param _iFontID font id to tocheck
     * @param _zString String to check
     */
    public static boolean IsGfxFontWritable(String _zString) {
        return ms_iFontType == FTYPE_GRAPHIC && _zString.charAt(0) < 256;
    }
    //#endif
    
    /////////////////////////////////////////////////////////////////////////////
    //                                                                         //
    //   Splits and draw a set of strings in a rectangle with the given width  //
    //                                                                         //
    /////////////////////////////////////////////////////////////////////////////
    private static String[] ms_zDrawRectFontStr = new String[32];
    public static byte ms_iDrawRectFontLines;
    public static short ms_iDrawRectFontLetters;
    private static int ms_iDrawRectChars;
    private static int ms_iDrawRectY;

    /**
     * Draws the specified String array using the current font into a defined
     * box. The x,y position is the position of the anchor point.
     * DrawFontInRectangle draws a array of texts into a defined box
     *
     * @param _g Graphic object
     * @param _iFontID the font to be drawed
     * @param _iTextArray array String to be drawed
     * @param _iPosX the x coordinate of the anchor point
     * @param _iPosY the y coordinate of the anchor point
     * @param _iW the box width in witch the strings will be drawed
     * @param _iFontSpacingY extra spacing height
     * @param _iFontAnchor the anchor point for positioning the text
     * @param _iBoxAnchorY the anchor of the box. This anchor only admits
     * Graphics.VCENTER as param
     * @param _iChars the number of letters of the String to be drawed
     * @param _bCalculateStrings this should be called at least the very firts
     * frame to be drawed. If you have more than one DrawFontInRectangle at the
     * same time you need to set this to true in all of them
     */
    public static void DrawFontInRectangle(
            Graphics _g,
            int _iFontID, 
            String[] _iTextArray,
            int _iPosX, 
            int _iPosY, 
            int _iW, 
            int _iFontSpacingY,
            int _iFontAnchor, 
            int _iBoxAnchorY, 
            int _iChars, 
            boolean _bCalculateStrings) {

        ms_iDrawRectChars = 0;

        if (_iChars < 0) {
            _iChars = 999;
        }

        /////////////////////////////////////////////////////////////////
        //  Precalculate string splitting to save process              //
        /////////////////////////////////////////////////////////////////

        ms_iDrawRectY = _iPosY;

        if (_bCalculateStrings) {
            ms_iDrawRectFontLines = SplitString(_iTextArray, _iW, _iFontID);
        }

        if ((_iBoxAnchorY & BASELINE) != 0) {
            ms_iDrawRectY -= _iFontSpacingY * ms_iDrawRectFontLines;
        } else if ((_iBoxAnchorY & VCENTER) != 0) {
            ms_iDrawRectY -= (_iFontSpacingY * ms_iDrawRectFontLines) >> 1;
        }

        /////////////////////////////////////////////////////////////////
        //  Let's write!!!                                             //
        /////////////////////////////////////////////////////////////////

        if ((ms_zDrawRectFontStr == null) || (_iChars == 0)) {
            return;
        }

        int iPY = ms_iDrawRectY;

        for (int i = 0; i < ms_zDrawRectFontStr.length; i++) {
            if (ms_zDrawRectFontStr[i] == null) {
                break;
            }

            ms_iDrawRectChars += ms_zDrawRectFontStr[i].length() - 1;

            if (_iChars >= ms_iDrawRectChars) {
                DrawFont(_g, _iFontID, ms_zDrawRectFontStr[i], _iPosX, iPY, _iFontAnchor, -1);

            } else {
                DrawFont(_g, _iFontID, ms_zDrawRectFontStr[i], _iPosX, iPY, _iFontAnchor, 
                        ms_zDrawRectFontStr[i].length() - 1 - (ms_iDrawRectChars - _iChars));

                break;
            }
            iPY += _iFontSpacingY;

        }
    }

    public static void DrawFontInRectangle(Graphics _g,
            int _iFontID, String[] _iTextArray, short[] _iIndexArray,
            int _iPosX, int _iPosY, int _iW, int _iFontSpacingY,
            int _iFontAnchor, int _iBoxAnchorY,
            int _iChars, boolean _bCalculateStrings) {

        /////////////////////////////////////////////////////////////////
        //  Precalculate string splitting to save process              //
        /////////////////////////////////////////////////////////////////

        ms_iDrawRectChars = 0;
        ms_iDrawRectY = _iPosY;

        if (_bCalculateStrings) {
            String zArray[] = new String[_iIndexArray.length];
            for (int i = 0; i < _iIndexArray.length; i++) {
                zArray[i] = _iTextArray[_iIndexArray[i]];
            }

            ms_iDrawRectFontLines = SplitString(zArray, _iW, _iFontID);
        }

        if ((_iBoxAnchorY & BASELINE) != 0) {
            ms_iDrawRectY -= _iFontSpacingY * ms_iDrawRectFontLines;
        } else if ((_iBoxAnchorY & VCENTER) != 0) {
            ms_iDrawRectY -= (_iFontSpacingY * ms_iDrawRectFontLines) >> 1;
        }

        /////////////////////////////////////////////////////////////////
        //  Let's write!!!                                             //
        /////////////////////////////////////////////////////////////////

        if (_iChars < 0) {
            _iChars = 999;
        }
        
        if ((ms_zDrawRectFontStr == null) || (_iChars == 0)) {
            return;
        }

        int iPY = ms_iDrawRectY;

        for (int i = 0; i < ms_zDrawRectFontStr.length; i++) {
            if (ms_zDrawRectFontStr[i] == null) {
                break;
            }

            ms_iDrawRectChars += ms_zDrawRectFontStr[i].length() - 1;

            if (_iChars >= ms_iDrawRectChars) {
                DrawFont(_g, _iFontID, ms_zDrawRectFontStr[i], _iPosX, iPY, _iFontAnchor, -1);

            } else {
                DrawFont(_g, _iFontID, ms_zDrawRectFontStr[i], _iPosX, iPY, _iFontAnchor, 
                        ms_zDrawRectFontStr[i].length() - 1 - (ms_iDrawRectChars - _iChars));

                break;
            }
            iPY += _iFontSpacingY;

        }
    }
    
    /**
     * SplitString <P> splits a set of Strings into lines to fit into a given rectangle
     *
     * @param _iTextArray text array to split
     * @param _iWidth maximum reference witdh for splitting
     * @param _iFontID font id to split
     */
    public static byte SplitString(String[] _iTextArray, short[] _iTextIndexes, int _iWidth, int _iFontID) {
        String[] zText = new String [_iTextIndexes.length];
        for (int i=0; i<zText.length; i++) {
            zText[0] = _iTextArray[_iTextIndexes[i]];
        }
        return SplitString (zText, _iWidth, _iFontID);
    }
    
    public static byte SplitString(String[] _iTextArray, int _iWidth, int _iFontID) {
    
        boolean bJapanese;
        boolean bCheckPoint;

        int iIndex = 0;
        int iInicio = 0;
        int iFinal = 0;
        int iFrase = 0;
        int iWidth = 0;
        byte iLineasGuardadas = 0;
        ms_iDrawRectFontLetters = 0;
        
        String zFrase;
        
        // format strings
        for (int i = 0; i < ms_zDrawRectFontStr.length; i++) {
            ms_zDrawRectFontStr[i] = null;
        }
        
        // phrases
        while ((iFrase < _iTextArray.length)) {
            zFrase = _iTextArray[iFrase];

            while (zFrase != null && iIndex < zFrase.length()) {

                bJapanese = (WolfRunner.ms_zLanguage.equals("ja")) && (zFrase.charAt (iIndex) > 255);
                bCheckPoint = zFrase.charAt(iIndex) == ' ' || zFrase.charAt(iIndex) == '/' || bJapanese;

                // add
                if (bCheckPoint || (iIndex + 1 == zFrase.length())) {
                    //#if Fonts != "SystemFont"
                    iWidth = (iInicio < iIndex + 1) ? StringWidth(_iFontID, zFrase.substring(iInicio, iIndex + 1)):0;
                    //#else
//#                     iWidth = (iInicio < iIndex) ? ms_SystemFont[_iFontID].stringWidth(zFrase.substring(iInicio, iIndex + 1)):0;
                    //#endif
                }
                
                // marck the string
                if ((bCheckPoint || 
                    (iIndex + 1 == zFrase.length())) && iWidth < _iWidth) {
                    iFinal = iIndex + 1;
                }
                
                // split the string
                if ((bCheckPoint && iWidth >= _iWidth) || 
                    iIndex + 1 == zFrase.length()) {

                    if (iInicio == iFinal)
                        iFinal = iIndex;
                    
                    if (iInicio == iFinal)
                        iFinal = iIndex;
                    
                    ms_zDrawRectFontStr[iLineasGuardadas] = zFrase.substring(iInicio, iFinal);
                    ms_iDrawRectFontLetters += iFinal - iInicio;

                    iLineasGuardadas++;
                    iInicio = iFinal;
                    iFinal = iIndex + 1;
                }
                
                iIndex++;
                
                if (iIndex >= zFrase.length() && iInicio < iIndex) {
                    ms_zDrawRectFontStr[iLineasGuardadas] = zFrase.substring(iInicio, iIndex);
                    ms_iDrawRectFontLetters += iIndex - iInicio;
                    iLineasGuardadas++;

                    //#if Debug
//#                     if (iLineasGuardadas >= ms_zDrawRectFontStr.length) {
//#                         Debug.DebugMsg ("SplitString() - ERROR: ArrayIndexOutOfBoundsException");
//#                         Debug.DebugMsg ("SplitString() - ERROR: too many strings!!! Increase ms_zDrawRectFontStr array bounds");
//#                     }
                    //#endif

                }
            }
            iFrase++;
            iIndex = 0;
            iInicio = 0;
            iFinal = 0;
            iWidth = 0;
        }
        
        // adjustments for japanese
        if (TxtManager.ms_bJapaneseLanguage) {
            if (iLineasGuardadas > 1) {
                if (ms_zDrawRectFontStr[iLineasGuardadas-1].equals("\u3002")) {
                    iLineasGuardadas--;
                }
            }
        }
        
        // adjustments for arabic
        if (TxtManager.ms_bArabicLanguage && !TxtManager.ARABIC_PROPER_SUPPORT) {
            String zBuffer;
            if (ms_zDrawRectFontStr[0].charAt(0) > 255) {
                if (iLineasGuardadas > 1) {
                    for (int i=0; i<iLineasGuardadas/2; i++) {
                        if (iLineasGuardadas-1-i != i) {
                            zBuffer = ms_zDrawRectFontStr[iLineasGuardadas-1-i];
                            ms_zDrawRectFontStr[iLineasGuardadas-1-i] = ms_zDrawRectFontStr[i];
                            ms_zDrawRectFontStr[i] = zBuffer;
                        }
                    }
                }
            }
        }
        
        ms_iDrawRectFontLines = iLineasGuardadas;
        return iLineasGuardadas;

    }
    // Delta UP DOWN effect
    //#ifdef UseEffectDeltaMove
//#     public static final int DELTA_INC = (16 * Define.MAXFPS) / Define.FPS;
//#     public static byte[] DELTA_UPDOWN_MOVE = new byte[128];
//# 
//#     static {
//#         int iMove = (Define.SIZEX < Define.SIZEY) ? (Define.SIZEX / 40) : (Define.SIZEY / 40);
//#         for (int i = 0; i < 64; i++) {
//#             DELTA_UPDOWN_MOVE [i] = (byte)((iMove*GfxManager.SINCOS[(i*DELTA_INC)%512])>>GfxManager.SINCOS_PRECISIONBITS);
//#             DELTA_UPDOWN_MOVE [i+64] = (byte)((iMove*GfxManager.SINCOS[(i*DELTA_INC)%512])>>GfxManager.SINCOS_PRECISIONBITS);
//#         }
//#     }
    //#endif
}
