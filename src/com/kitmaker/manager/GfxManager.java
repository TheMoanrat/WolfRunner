package com.kitmaker.manager;

import com.kitmaker.wolfRunner.Define;
import com.kitmaker.wolfRunner.Main;
import com.kitmaker.wolfRunner.ModeMenu;
import com.kitmaker.wolfRunner.WolfRunner;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javak.microedition.lcdui.Graphics;
import javak.microedition.lcdui.Image;

public class GfxManager {

    public static final short ANGLE_STEPS = 2048;
    public static final short ANGLE_STEPS2 = ANGLE_STEPS / 2;
    public static final short ANGLE_STEPS4 = ANGLE_STEPS / 4;
    public static final short ANGLE_STEPS8 = ANGLE_STEPS / 8;
    public static final short ANGLE_STEPS16 = ANGLE_STEPS / 16;
    public static final int SINCOS_PRECISIONBITS = 16;
    // BM = Bitmap
    private static int[] ms_iLoadedGfxBM = new int[4];  // Capacidad para (32*4)=128 grï¿½ficos (sin contar paletas)
    private static int[] ms_iLoadingGfxBM = new int[4];
    private static int ms_iGfxPrepared;
    private static int ms_iChunkSearchIndex;
    private static byte[] ms_iChunkSearchBuffer = new byte[6];  // 2 bytes de tamaño de chunk + 2 para nombre de chunk
    private static final byte[] CHNK_IHDR = {
        (byte) 'I',
        (byte) 'H',
        (byte) 'D',
        (byte) 'R'
    };
    // ## Graphics ##
    private static short[][] ms_iGfxData;
    //#if MIDP=="2.0" || RIM
    static final int TRANS_FLIPH = javax.microedition.lcdui.game.Sprite.TRANS_MIRROR;
    static final int TRANS_FLIPV = javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180;
    //#elif API=="Nokia"
    //#    static final int TRANS_FLIPH = DirectGraphics.FLIP_HORIZONTAL;
    //#    static final int TRANS_FLIPV = DirectGraphics.FLIP_VERTICAL;
    //#else
//#        static final int TRANS_FLIPH = 0;
//#        static final int TRANS_FLIPV = 0;
    //#endif
    static final String GFX_PREV = "/";
    static final String GFX_FORMAT = ".png";
    static final String[] GFX_FILENAME = {
        //init
        GFX_PREV + "logo" + GFX_FORMAT, //00 
        GFX_PREV + "boton" + GFX_FORMAT, //01
        GFX_PREV + "sk_home" + GFX_FORMAT, //02
        GFX_PREV + "sk_menu" + GFX_FORMAT, //03
        GFX_PREV + "sk_next" + GFX_FORMAT, //04
        GFX_PREV + "sk_reset" + GFX_FORMAT, //05
        //Wolves
        GFX_PREV + "wolfbrown" + GFX_FORMAT, //06
        // tiles
        GFX_PREV + "grasstile" + GFX_FORMAT, //07
        GFX_PREV + "sidetreetile" + GFX_FORMAT, //8
        GFX_PREV + "rocktile" + GFX_FORMAT, //9
        GFX_PREV + "lifeicon" + GFX_FORMAT, //10
    };
    static final byte FLG_TRANSFORMABLE = 1;  // Allows horizontal/vertical mirroring !!DEPRECATED!!
    static final byte FLG_OPAQUE = 2;  // Allows a certain memory optimization
    static byte[] GFX_FLAGS = {
        // 1=Transformable (supports mirroring)
        // 2=Opaque (enables a certain memory optimization)
        0, // 00
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 10
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 20
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 30
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 40
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 50
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 60
    };
    // Init
    //Menu
    public static final int GFXID_LOGO = 0;
    public static final int GFXID_MENU_BUTTONS = 1;
    public static final int GFXID_SK_HOME = 2;
    public static final int GFXID_SK_MENU = 3;
    public static final int GFXID_SK_NEXT = 4;
    public static final int GFXID_SK_RESET = 5;
    //Wolves
    public static final int GFXID_WOLF = 6;
    //Tiles
    public static final int GFXID_GRASSTILE = 7;
    public static final int GFXID_SIDE_TREETILE = 8;
    public static final int GFXID_ROCKTILE = 9;
    public static final int GFXID_LIFES_ICON = 10;
    public static final int SPR_ID = 0;
    public static final int SPR_POS_X = 1;
    public static final int SPR_POS_Y = 2;
    public static final int SPR_WIDTH = 3;
    public static final int SPR_HEIGHT = 4;
    public static final int SPR_HOT_X = 5;
    public static final int SPR_HOT_Y = 6;
    //Index data to acces to info graphics sprites:
    //Menu
    public static final int SPRID_BUTTON = 0;
    public static final int SPRID_SK_HOME = 2;
    public static final int SPRID_SK_MENU = 4;
    public static final int SPRID_SK_NEXT = 8;
    public static final int SPRID_SK_RESET = 10;
    //Wolves
    public static final int SPRID_WOLF = 12;
    //lifeIcon
    public static final int SPRID_LIFES_ICON = 22;
    //#if SIZE == "Small"
//#      static short [][] SPRITE_DATA = {
//#     };
    //#elif SIZE == "Standard"
//#     static short[][] SPRITE_DATA = {
//#     };
    //#elif SIZE == "Large"
    public static short[][] SPRITE_DATA = {
        {GFXID_MENU_BUTTONS, 0, 0, 182, 40, 0, 0},
        {GFXID_MENU_BUTTONS, 0, 40, 182, 40, 0, 0},
        {GFXID_SK_HOME, 0, 0, 46, 46, 0, 0},
        {GFXID_SK_HOME, 0, 46, 46, 46, 0, 0},
        {GFXID_SK_MENU, 0, 0, 46, 46, 0, 0},
        {GFXID_SK_MENU, 0, 46, 46, 46, 0, 0},
        {GFXID_SK_MENU, 46, 0, 46, 46, 0, 0},
        {GFXID_SK_MENU, 46, 46, 46, 46, 0, 0},
        {GFXID_SK_NEXT, 0, 0, 46, 46, 0, 0},
        {GFXID_SK_NEXT, 0, 46, 46, 46, 0, 0},
        {GFXID_SK_RESET, 0, 0, 46, 46, 0, 0},
        {GFXID_SK_RESET, 0, 46, 46, 46, 0, 0},
        //0,1,2,3,4 Wolf runing
        //5,6, Wolf jumping
        //7,8 ,9 Wolf dead
        {GFXID_WOLF, 0, 0, 23, 58, 0, 0},
        {GFXID_WOLF, 23, 0, 23, 58, 0, 0},
        {GFXID_WOLF, 46, 0, 23, 58, 0, 0},
        {GFXID_WOLF, 69, 0, 23, 58, 0, 0},
        {GFXID_WOLF, 92, 0, 23, 58, 0, 0},
        {GFXID_WOLF, 115, 0, 27, 68, 0, 0},
        {GFXID_WOLF, 142, 0, 29, 76, 0, 0},
        {GFXID_WOLF, 0, 58, 23, 58, 0, 0},
        {GFXID_WOLF, 23, 58, 23, 58, 0, 0},
        {GFXID_WOLF, 46, 58, 23, 58, 0, 0},
        //Lifes icon
        {GFXID_LIFES_ICON, 0, 0, 17, 21, 0, 0},
    };
    //#elif SIZE == "Extra"
//#     public static short [][] SPRITE_DATA = {
//#     };
    //#elif SIZE=="XXL"
//#     public static short [][] SPRITE_DATA = {
//#     };
    //#endif
    //Tiles
    public static final int SPRID_GRASSTILE = 0;
    public static final int SPRID_SIDE_TREETILE = 1;
    public static final int SPRID_ROCKTILE = 3;
    //#if SIZE == "Small"
//#     public static short [][] TILE_DATA = {
//#     };
    //#elif SIZE == "Standard"
//#     public static short [][] TILE_DATA = {
//#     };
//#     
    //#elif SIZE == "Large"
    public static short[][] TILE_DATA = {
        {GFXID_GRASSTILE, 0, 0, 32, 32, 0, 0},
        {GFXID_SIDE_TREETILE, 0, 0, 32, 32, 0, 0},
        {GFXID_SIDE_TREETILE, 32, 0, 32, 32, 0, 0},
        {GFXID_GRASSTILE, 0, 0, 32, 32, 0, 0},};
    //#elif SIZE == "Extra"
//#     public static short [][] TILE_DATA = {
//#     };
    //#elif SIZE=="XXL"
//#      public static short [][] TILE_DATA = {
//#     };
    //#endif
    public static final byte MENU_BUTTON[] = {
        GfxManager.SPRID_BUTTON,
        GfxManager.SPRID_BUTTON + 1,};
    public static final byte SK_HOME[] = {
        GfxManager.SPRID_SK_HOME,
        GfxManager.SPRID_SK_HOME + 1,};
    public static final byte SK_MENU[] = {
        SPRID_SK_MENU,
        SPRID_SK_MENU + 1,
        SPRID_SK_MENU + 2,
        SPRID_SK_MENU + 3,};
    public static final byte SK_NEXT[] = {
        SPRID_SK_NEXT,
        SPRID_SK_NEXT + 1,};
    public static final byte SK_RESET[] = {
        SPRID_SK_RESET,
        SPRID_SK_RESET + 1,};
    public static final byte WOLF[] = {
        SPRID_WOLF,
        SPRID_WOLF + 1,
        SPRID_WOLF + 2,
        SPRID_WOLF + 3,
        SPRID_WOLF + 4,
        SPRID_WOLF + 5,
        SPRID_WOLF + 6,
        SPRID_WOLF + 7,
        SPRID_WOLF + 8,
        SPRID_WOLF + 9,};

    public static final byte GRASS_TILE[] = {
        SPRID_GRASSTILE,};
    public static final byte SIDE_TREE_TILE[] = {
        SPRID_SIDE_TREETILE,
        SPRID_SIDE_TREETILE + 1,};
    public static final byte ROCK_TILE[] = {
        SPRID_ROCKTILE,};
        public static final byte LIFES_ICON[] = {
        SPRID_LIFES_ICON,};
    // GRAPHICS
    public static Image[] ms_vImage;
    // Sprites y paletas empaquetados
    static int[] ms_iGfxStart;
    static int[] ms_iGfxPixelsArea;  // Area in pixels
    //static int[] ms_iGfxImageSize;
    static byte ms_iNumGfx;
    public static InputStream ms_vRscPalInput;
    // Barra de carga
    public static int ms_iLoadingBarTotal, ms_iLoadingBarCurrent;
    public static boolean ms_bLoadingBarActive;
    public static boolean ms_bLoadingBarRefresh;
    public static int ms_bLoadingBarFrame;
    public static int ms_iNumLoadings;

    ////////////////////////////////////////////////////////////////////
    // Execute only one time
    ////////////////////////////////////////////////////////////////////
    public static void InitSpriteManager() {
        int i;
        DataInputStream vDataInput = null;

        try {
            // Anota el area en bytes de cada archivo. Suponemos que no hay grï¿½ficos
            //   de menos de 100 bytes, y que la informaciï¿½n de cabecera comienza en
            //   los primeros 92 bytes.
            // El area se utilizara para cargar los graficos en orden de mayor a
            //   menor area, combatiendo la posible fragmentacion de memoria.
            ms_iNumGfx = (byte) GFX_FILENAME.length;
            ms_iGfxPixelsArea = new int[ms_iNumGfx];

            vDataInput = new DataInputStream(WolfRunner.ms_vInstance.getClass().getResourceAsStream("/imagecoords.dat"));
            if (vDataInput != null) {
                short iNumImages = vDataInput.readShort();

                for (i = 0; i < iNumImages; i++) {
                    ms_iGfxPixelsArea[i] = vDataInput.readInt();
                }

                vDataInput.close();

            } else {
                int iGfxHdrPos;  // Pos. de inicio de la cabecera dentro de cada grï¿½fico
                int iWidth, iHeight;
                byte[] iHeaderBuffer = new byte[100];

                for (i = 0; i < ms_iNumGfx; i++) {
                    iWidth = iHeight = 0;
                    if (GFX_FILENAME[i] == null) {
                        continue;
                    }

                    InputStream vInputStream = WolfRunner.ms_vInstance.getClass().getResourceAsStream(GFX_FILENAME[i]);
                    if (vInputStream != null) {
                        vInputStream.read(iHeaderBuffer, 0, iHeaderBuffer.length);
                        iGfxHdrPos = SearchPNGChunk(iHeaderBuffer, CHNK_IHDR, 0, 100);
                        iWidth = ((((char) iHeaderBuffer[iGfxHdrPos]) & 0xff) << 24)
                                + ((((char) iHeaderBuffer[iGfxHdrPos + 1]) & 0xff) << 16)
                                + ((((char) iHeaderBuffer[iGfxHdrPos + 2]) & 0xff) << 8)
                                + (((char) iHeaderBuffer[iGfxHdrPos + 3]) & 0xff);
                        iHeight = ((((char) iHeaderBuffer[iGfxHdrPos + 4]) & 0xff) << 24)
                                + ((((char) iHeaderBuffer[iGfxHdrPos + 5]) & 0xff) << 16)
                                + ((((char) iHeaderBuffer[iGfxHdrPos + 6]) & 0xff) << 8)
                                + (((char) iHeaderBuffer[iGfxHdrPos + 7]) & 0xff);

                        vInputStream.close();
                    }
                    ms_iGfxPixelsArea[i] = iWidth * iHeight;
                }
            }
        } catch (Exception e) {
            //#if Debug
//#             Debug.DebugMsg ("InitSprite() - ERROR leyendo info de graficos" +
//#                 " Excpt:"+e.getMessage ()+"("+e.toString ()+")");
            //#endif

            try {
                if (vDataInput != null) {
                    vDataInput.close();
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
            }

        }

        ms_iGfxData = new short[ms_iNumGfx][2];
        ms_vImage = new Image[ms_iNumGfx];
    }

    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    public static void ResetGraphics() {
        for (int i = 0; i < ms_iLoadingGfxBM.length; i++) {
            ms_iLoadingGfxBM[i] = 0;
        }
        ms_iGfxPrepared = 0;
    }

    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    public static void UnloadGraphics() {
        for (int i = 0; i < ms_vImage.length; i++) {
            ms_vImage[i] = null;
        }
        System.gc();
    }

    ////////////////////////////////////////////////////////////////////
    //  -1: Blank graphic
    ////////////////////////////////////////////////////////////////////
    public static void SetGraphics(short[] _iGfxList) {
        ResetGraphics();
        for (int i = 0; i < _iGfxList.length; i++) {
            AddGraphic(_iGfxList[i]);
        }
    }

    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    public static void AddGraphic(int _iGfxID) {
        // Add the graphic in the bit map, if is not loaded already
        if ((_iGfxID != -1)
                && (ms_iLoadingGfxBM[_iGfxID >> 5] & (1 << (_iGfxID & 0x1F))) == 0) {
            ms_iLoadingGfxBM[_iGfxID >> 5] |= (1 << (_iGfxID & 0x1F));
            ms_iGfxPrepared++;
        }
    }

    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    public static void FlushGraphics() {
        int i, j, iBit;
        byte iGfxBit, iGfxBitmap;

        // Flush all graphics
        //#if Debug
//#         Debug.DebugMsg ("------------------------------------");
//#         Debug.DebugMsg ("FlushGfx(): Start");
        //#endif

        iGfxBit = iGfxBitmap = 0;
        for (i = 0; i < ms_iNumGfx; i++) {
            iBit = 1 << iGfxBit;

            // We wont use the graphic anymore
            if ((ms_iLoadedGfxBM[iGfxBitmap] & iBit) != 0) {
                // The graphics was loaded: we need to free it
                ms_vImage[i] = null;
            }

            if ((++iGfxBit) >= 32) {
                iGfxBit = 0;
                iGfxBitmap++;
            }
        }

        for (i = 0; i < ms_iLoadedGfxBM.length; i++) {
            ms_iLoadedGfxBM[i] = 0;
        }
    }

    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    public static void LoadGraphics(boolean _bShowLoadingBar) {
        int i, j, k, iBit, iAux;
        byte iGfxBit, iGfxBitmap;
        int[] iSortedGfx;
        int[] iSortedGfxArea;
        int iLoadingGfxIndex;

        //#if Debug
//#         Debug.DebugPostmortemMsg ("------------------------------------");
//#         Debug.DebugPostmortemMsg ("LoadGfx(): Start");
        //#endif

        if (ms_iNumLoadings > 0 && _bShowLoadingBar) {
            ms_bLoadingBarActive = true;
            ms_bLoadingBarFrame = 0;
        }

        // Draw empty progress bar before loading
        if (ms_bLoadingBarActive) {
            ms_iLoadingBarCurrent = 0;
            ms_iLoadingBarTotal = 1;

            //#if RIM
//#             ms_bLoadingBarRefresh = true;
//#             Main.ms_vMain.invalidate(0, Main.LOADING_BOXY, Define.SIZEX, Main.LOADING_BOXSIZEY);
            //#else
            Main.ms_vMain.repaint();
            Main.ms_vMain.serviceRepaints();
            //#endif

        }

        // Flush the graphics that won't be used
        //#if Debug
//#         Debug.DebugMsg ("LoadGfx(): INIT FLUSH PHASE");
        //#endif   
        iGfxBit = iGfxBitmap = 0;

        for (i = 0; i < ms_iNumGfx; i++) {
            iBit = 1 << iGfxBit;

            if ((ms_iLoadingGfxBM[iGfxBitmap] & iBit) == 0) {
                // We wont use the graphic anymore
                if ((ms_iLoadedGfxBM[iGfxBitmap] & iBit) != 0) {
                    ms_vImage[i] = null;
                }
            }

            if ((++iGfxBit) >= 32) {
                iGfxBit = 0;
                iGfxBitmap++;
            }
        }


        // Load new graphics
        System.gc();

        //#if Debug
//#         Debug.DebugMsg ("LoadGfx(): INIT LOAD PHASE");
        //#endif

        iSortedGfx = new int[ms_iGfxPrepared];
        iSortedGfxArea = new int[ms_iGfxPrepared];
        iLoadingGfxIndex = 0;
        iGfxBit = iGfxBitmap = 0;

        for (i = 0; i < ms_iNumGfx; i++) {
            iBit = 1 << iGfxBit;
            if ((ms_iLoadingGfxBM[iGfxBitmap] & iBit) != 0) {
                iSortedGfx[iLoadingGfxIndex] = i;
                iSortedGfxArea[iLoadingGfxIndex] = ms_iGfxPixelsArea[i];
                iLoadingGfxIndex++;
            }

            if ((++iGfxBit) >= 32) {
                iGfxBit = 0;
                iGfxBitmap++;
            }
        }

        // Ordena los grï¿½ficos de mayor a menor ï¿½rea. Se cargarï¿½n en este orden,
        //   para combatir la fragmentaciï¿½n de memoria.
        for (i = 0; i < (ms_iGfxPrepared - 1); i++) {
            for (j = (ms_iGfxPrepared - 2); j >= i; j--) {
                if (iSortedGfxArea[j] < iSortedGfxArea[j + 1]) {
                    iAux = iSortedGfxArea[j];
                    iSortedGfxArea[j] = iSortedGfxArea[j + 1];
                    iSortedGfxArea[j + 1] = iAux;
                    iAux = iSortedGfx[j];
                    iSortedGfx[j] = iSortedGfx[j + 1];
                    iSortedGfx[j + 1] = iAux;
                }
            }
        }

        iGfxBit = iGfxBitmap;
        ms_iLoadingBarTotal = ms_iGfxPrepared;
        for (i = 0; i < ms_iGfxPrepared; i++) {

            // Draw progress bar while loading
            if (ms_bLoadingBarActive) {
                ms_iLoadingBarCurrent = i;

                //#if RIM
//#                 ms_bLoadingBarRefresh = true;
//#                 Main.ms_vMain.invalidate(Main.LOADING_BARX, Main.LOADING_BARY, Main.LOADING_BARX, Main.LOADING_BARSIZEY);
                //#else
                Main.ms_vMain.repaint();
                Main.ms_vMain.serviceRepaints();
                //#endif
            }


            iBit = 1 << ((iSortedGfx[i] % 32));
            iGfxBitmap = (byte) (iSortedGfx[i] / 32);

            // The graphic will be used

            if ((ms_iLoadingGfxBM[iGfxBitmap] & iBit) != 0) {
                // The graphic was not loaded but it won't use palettes: load it directly
                if ((ms_iLoadedGfxBM[iGfxBitmap] & iBit) == 0) {
                    ms_vImage[iSortedGfx[i]] = LoadStandardGraphic(iSortedGfx[i]);
                }
            }

            if ((++iGfxBit) >= 32) {
                iGfxBit = 0;
                iGfxBitmap++;
            }
        }

        // Draw full progress bar while loading
        if (ms_bLoadingBarActive) {
            ms_iLoadingBarCurrent = i;

            //#if RIM
//#             ms_bLoadingBarRefresh = true;
//#             Main.ms_vMain.invalidate(Main.LOADING_BARX, Main.LOADING_BARY, Main.LOADING_BARX, Main.LOADING_BARSIZEY);
            //#else
            Main.ms_vMain.repaint();
            Main.ms_vMain.serviceRepaints();
            //#endif

        }

        for (i = 0; i < ms_iLoadedGfxBM.length; i++) {
            ms_iLoadedGfxBM[i] = ms_iLoadingGfxBM[i];
            ms_iLoadingGfxBM[i] = 0;
        }

        ms_bLoadingBarActive = false;

        //#if Debug
//#         Debug.DebugPostmortemMsg ("LoadGfx(): End");
//#         Debug.DebugPostmortemMsg ("------------------------------------");
        //#endif

        System.gc();
        ms_iNumLoadings++;
    }

    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    static Image LoadStandardGraphic(int _iGfxID) {
        Image vImg = null;

        //#if API=="Motorola"
//#         System.gc();
        //#endif

        //#if Debug
//#         Debug.DebugPostmortemMsg ("LoadGfx(): Gfx."+_iGfxID+" - Loading graph ...");
        //#endif

        try {

            Image vSourceImg = Image.createImage(GFX_FILENAME[_iGfxID]);

            if ((GFX_FLAGS[_iGfxID] & FLG_OPAQUE) != 0) {
                vImg = Image.createImage(vSourceImg.getWidth(), vSourceImg.getHeight());
                ((Image) vImg).getGraphics().drawImage(vSourceImg, 0, 0, 0);

            } else {
                vImg = vSourceImg;
            }

            // get height / witdh
            ms_iGfxData[_iGfxID][0] = (short) (vImg.getWidth());
            ms_iGfxData[_iGfxID][1] = (short) (vImg.getHeight());

            //#if Debug
//#             Debug.DebugPostmortemMsg ("LoadGfx(): Gfx."+_iGfxID+" - Loading graph OK ...");
            //#endif

        } catch (Exception ex) {
            //#if Debug
//#             Debug.DebugMsg ("LoadGfx(): Gfx."+_iGfxID+" - LOADING ERROR");
//#             Debug.DebugMsg ( "ex: "  + ex.toString());
            //#endif
            System.gc();
            Runtime.getRuntime().gc();

        }
        //#if !SlowGarbageCollector
        System.gc();
        //#endif

        return vImg;
    }

    static int SearchPNGChunk(byte[] _iBuffer,
            byte[] _iChunkString,
            int _iStartPos,
            int _iMaxBlock) {
        int i, j;
        int iChunkDataStart = -1;

        for (i = 0; i < ms_iChunkSearchBuffer.length; i++) {
            ms_iChunkSearchBuffer[i] = 0;
        }

        ms_iChunkSearchIndex = 0;
        i = _iStartPos;
        while ((iChunkDataStart == (-1)) && (i < (_iStartPos + _iMaxBlock))) {
            ms_iChunkSearchBuffer[ms_iChunkSearchIndex] = _iBuffer[i];
            ms_iChunkSearchIndex = (byte) ((ms_iChunkSearchIndex + 1) % 6);
            j = 0;
            while ((j < 4) && (ms_iChunkSearchBuffer[(ms_iChunkSearchIndex + 2 + j) % 6] == _iChunkString[j])) {
                j++;
            }

            if (j == 4) {
                iChunkDataStart = i + 1;
            }
            i++;
        }

        return iChunkDataStart;
    }
    // fixed point constants 
    private static final int FP_SHIFT = 13;
    /**
     * fast resampling mode - no antialiase
     */
    public static final int FAST_RESAMPLE = 0;
    /**
     * slow resampling mode - with antialiase
     */
    public static final int SLOW_RESAMPLE = 1;
    public static final int RESAMPLE = SLOW_RESAMPLE;

    /**
     * resizeImage Gets a source image along with new size for it and resizes
     * it.
     *
     * @param src The source image.
     * @param destW The new width for the destination image.
     * @param destH The new heigth for the destination image.
     * @param mode A flag indicating what type of resizing we want to do:
     * FAST_RESAMPLE or SLOW_RESAMPLE.
     * @return The resized image.
     */
    static Image resizeImage(Image src, int mode) {
        //#if MIDP=="2.0"
        try {
            int srcW = src.getWidth();
            int srcH = src.getHeight();

            int destW = (((srcW << FP_SHIFT) * Define.SIZEX) / Define.BASE_SIZEX) >> FP_SHIFT;
            int destH = (((srcH << FP_SHIFT) * Define.SIZEY) / Define.BASE_SIZEY) >> FP_SHIFT;

            // create pixel arrays
            int[] srcPixels = new int[srcW];
            int[] destPixels = new int[destW * destH]; // array to hold destination pixels

            // precalculate src/dest ratios
            int ratioW = (srcW << FP_SHIFT) / destW;
            int ratioH = (srcH << FP_SHIFT) / destH;

            if (mode == FAST_RESAMPLE) {
                long ini = System.currentTimeMillis();
                // simple point smapled resizing
                // loop through the destination pixels, find the matching pixel on the source and use that
                int p = 0;
                for (int destY = 0; destY < destH; ++destY) {
                    int srcY = (destY * ratioH) >> FP_SHIFT; // calculate beginning of sample
                    // int srcY = (destY * srcH) / destH;
                    src.getRGB(srcPixels, 0, srcW, 0, srcY, srcW, 1);
                    for (int destX = 0; destX < destW; ++destX) {
                        int srcX = (destX * ratioW) >> FP_SHIFT; // calculate beginning of sample
                        // int srcX = (destX * srcW) / destW;
                        destPixels[p++] = srcPixels[srcX];
                    }
                }
                System.out.println("fast: " + (System.currentTimeMillis() - ini) + "ms");

            } else {
                long ini = System.currentTimeMillis();

                byte[] tmpA = new byte[destW * srcH]; // temporary buffer for the horizontal resampling step
                byte[] tmpR = new byte[tmpA.length];  // temporary buffer for the horizontal resampling step
                byte[] tmpG = new byte[tmpA.length];
                byte[] tmpB = new byte[tmpA.length];

                // variables to perform additive blending
                int argb; // color extracted from source
                int a, r, g, b; // separate channels of the color
                int count; // number of pixels sampled for calculating the average

                // the resampling will be separated into 2 steps for simplicity
                // the first step will keep the same height and just stretch the picture horizontally
                // the second step will take the intermediate result and stretch it vertically

                // horizontal resampling
                int p = 0;
                for (int y = 0; y < srcH; ++y) {
                    src.getRGB(srcPixels, 0, srcW, 0, y, srcW, 1);
                    for (int x = 0; x < destW; ++x) {
                        int srcX = (x * ratioW) >> FP_SHIFT; // calculate beginning of sample
                        int srcX2 = ((x + 1) * ratioW) >> FP_SHIFT; // calculate end of sample
                        if (srcX2 >= srcW) {
                            srcX2 = srcW - 1;
                        }

                        count = srcX2 - srcX + 1;
                        // now loop from srcX to srcX2 and add up the values for each channel
                        for (a = r = g = b = 0; srcX <= srcX2; srcX++) {
                            argb = srcPixels[srcX];
                            a += (argb >> 24) & 0xFF;
                            r += (argb >> 16) & 0xFF;
                            g += (argb >> 8) & 0xFF;
                            b += argb & 0xFF;
                        }
                        // average out the channel values
                        tmpA[p] = (byte) (a / count);
                        tmpR[p] = (byte) (r / count);
                        tmpG[p] = (byte) (g / count);
                        tmpB[p] = (byte) (b / count);
                        p++;
                    }
                }

                // vertical resampling of the temporary buffer (which has been horizontally resampled)
                for (int x = 0; x < destW; ++x) {
                    for (int y = 0, xx = x; y < destH; y++, xx += destW) {
                        int srcY = (y * ratioH) >> FP_SHIFT; // calculate beginning of sample
                        int srcY2 = ((y + 1) * ratioH) >> FP_SHIFT; // calculate end of sample
                        if (srcY2 >= srcH) {
                            srcY2 = srcH - 1;
                        }

                        count = srcY2 - srcY + 1;
                        // now loop from srcY to srcY2 and add up the values for each channel
                        p = x + srcY * destW;
                        for (a = r = b = g = 0; srcY <= srcY2; srcY++, p += destW) {
                            a += tmpA[p] & 0xFF; // alpha channel
                            r += tmpR[p] & 0xFF; // red channel
                            g += tmpG[p] & 0xFF; // green channel
                            b += tmpB[p] & 0xFF; // blue channel
                        }
                        // recreate color from the averaged channels and place it into the destination buffer
                        destPixels[xx] = ((a / count) << 24) | ((r / count) << 16) | ((g / count) << 8) | (b / count);
                    }
                }
                System.out.println("slow: " + (System.currentTimeMillis() - ini) + "ms");
            }

            // return a new image created from the destination pixel buffer
            return Image.createRGBImage(destPixels, destW, destH, true);


        } catch (Exception e) {
            return null;
        }
        //#else
//#         return src;
        //#endif

    }

    static int GetImageWidth(int _iGfxID) {
        return ms_iGfxData[_iGfxID][0];
    }

    static int GetImageHeight(int _iGfxID) {
        return ms_iGfxData[_iGfxID][1];
    }

    /*
     * static int GetSpriteWidth (int _iSprID) { return SPRITE_DATA[_iSprID][3];
     * } static int GetSpriteHeight (int _iGfxID) { return
     * SPRITE_DATA[_iSprID][4]; }
     */
    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////
    ///#define TestSpeed
    ///#if Nokia6280
    ///#define CanvasClipping
    ///#endif
    private static final int FP_BITS = 13;
    private static int ms_iRGBTransparentColor;
    private static int ms_iTransparentColor;
    private static Image ms_vTransparentImage;

    static void DrawFlashSprite(int _iSprID, int _iPosX, int _iPosY,
            int _iTransform, int _iColor) {

        //#if MIDP=="1.0" || s60dp2lp || s60dp2hp || x660
        //#elif MIDP == "2.0"

        short[] vData;
        int iPosXImg, iPosXClip;
        int iPosYImg, iPosYClip;
        int srcW, srcH;
        int iDrawRGB_Offset = 0;

        vData = SPRITE_DATA[_iSprID];

        try {
            switch (vData.length) {

                case 1:
                    // Grafico Entero
                    iPosXImg = _iPosX;
                    iPosYImg = _iPosY;
                    iPosXClip = iPosYClip = 0;
                    srcW = ms_iGfxData[vData[0]][0];
                    srcH = ms_iGfxData[vData[0]][1];
                    break;

                case 5:
                    // Grafico trozo
                    iPosXImg = _iPosX;
                    iPosYImg = _iPosY;
                    iPosXClip = vData[1];
                    iPosYClip = vData[2];
                    srcW = vData[3];
                    srcH = vData[4];
                    break;

                case 7:
                    // Grafico trozo con hotspots
                    iPosXImg = _iPosX - vData[5];
                    iPosYImg = _iPosY - vData[6];
                    iPosXClip = vData[1];
                    iPosYClip = vData[2];
                    srcW = vData[3];
                    srcH = vData[4];

                    if ((_iTransform & TRANS_FLIPH) != 0) {
                        iPosXImg = _iPosX - (vData[3] - vData[5]);
                    }
                    if ((_iTransform & TRANS_FLIPV) != 0) {
                        iPosYImg = _iPosY - (vData[4] - vData[6]);
                    }

                    break;

                default:
                    //#if Debug
//#                    Debug.DebugMsg ("SPR: DrawFlashSprite() - ERROR: Datos sprite " +
//#                       _iSprID+" mal definidos (nï¿½ incorrecto de datos:"+vData.length+").");
                    //#endif
                    return;
            }

            // create pixel arrays
            int[] srcPixels = new int[srcW];
            int[] destPixels = new int[srcW]; // array to hold destination pixels


            int iInitialY = 0;
            if (iPosYImg < 0) {
                iInitialY = -iPosYImg;
            }

            for (int iY = iInitialY; iY < srcH; ++iY) {
                ms_vImage[vData[0]].getRGB(srcPixels, 0, srcW, iPosXClip, iPosYClip + iY, srcW, 1);

                if ((_iTransform & TRANS_FLIPH) != 0) {
                    for (int iX = 0; iX < srcW; ++iX) {
                        destPixels[srcW - 1 - iX] = srcPixels[iX];

                        //#if API=="Motorola" || API=="SonyEricsson" || ku380
//#                         if (((destPixels[srcW - 1 - iX] >> 24) & 0xFF) != 0) //#else
                        //#                   if (destPixels[srcW-1-iX] != ms_iTransparentColor)
                        //#endif
                        {
                            destPixels[srcW - 1 - iX] = _iColor;
                        }
                        //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                       else
//#                          destPixels[srcW-1-iX] = 0x00000000;
                        //#endif

                        /////////////////////////////////////////////////////////////

                    }

                } else {
                    for (int iX = 0; iX < srcW; ++iX) {
                        destPixels[iX] = srcPixels[iX];

                        //#if API=="Motorola" || API=="SonyEricsson" || ku380
//#                         if (((destPixels[iX] >> 24) & 0xFF) != 0) //#else
                        //#                   if (destPixels[iX] != ms_iTransparentColor)
                        //#endif
                        {
                            destPixels[iX] = _iColor;
                        }

                        //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                       else
//#                          destPixels[iX] = 0x00000000;
                        //#endif

                    }
                }

                Main.ms_XGraphics.drawRGB(destPixels, iDrawRGB_Offset, srcW, iPosXImg, iPosYImg + iY, srcW, 1, true);

            }
        } catch (Exception e) {
            //#if Debug
//#              Debug.DebugMsg ( "SPR: DrawFlashSprite() - Spr." + _iSprID + " ( Gfx. "+vData[0]+" ) - Error de dibujado. Excpt:"+e.getMessage ()+"("+e.toString ()+")" );
            //#endif
        }
        //#endif
    }

    //#if API=="Nokia"
//#     static int[] ms_iPolygonX=new int[4];
//#     static int[] ms_iPolygonY=new int[4];
    //#endif
    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    static void DrawSprite(int _iSprID, int _iPosX, int _iPosY, int _iTransform) {
        short[] vData;
        int iPosXImg, iPosXClip;
        int iPosYImg, iPosYClip;

        if (_iSprID == -1) {
            return;
        }

        vData = SPRITE_DATA[_iSprID];

        if (ms_vImage[vData[0]] == null) {
            return;
        }
        try {
            switch (vData.length) {

                case 1:
                    // Grafico Entero
                    iPosXImg = _iPosX;
                    iPosYImg = _iPosY;
                    iPosXClip = iPosYClip = 0;
                    break;

                case 5:

                    // Grafico trozo
                    iPosXImg = _iPosX - vData[1];
                    iPosYImg = _iPosY - vData[2];
                    iPosXClip = _iPosX;
                    iPosYClip = _iPosY;

                    if ((_iTransform & TRANS_FLIPH) != 0) {
                        iPosXImg = _iPosX - (ms_iGfxData[vData[SPR_ID]][0] - vData[1] - vData[3]);
                    }

                    if ((_iTransform & TRANS_FLIPV) != 0) {
                        iPosYImg = _iPosY - (ms_iGfxData[vData[SPR_ID]][1] - vData[2] - vData[4]);
                    }

                    break;

                case 7:
                    // Grafico trozo con hotspots
                    iPosXClip = _iPosX - vData[5];
                    iPosYClip = _iPosY - vData[6];
                    iPosXImg = _iPosX - vData[1] - vData[5];
                    iPosYImg = _iPosY - vData[2] - vData[6];

                    if ((_iTransform & TRANS_FLIPH) != 0) {
                        iPosXClip = _iPosX - (vData[3] - vData[5]);
                        iPosXImg = _iPosX - (ms_iGfxData[vData[SPR_ID]][0] - vData[1] - vData[3]) - (vData[3] - vData[5]);
                    }
                    if ((_iTransform & TRANS_FLIPV) != 0) {
                        iPosYClip = _iPosY + (vData[4] - vData[6]);
                        iPosYImg = _iPosY - (ms_iGfxData[vData[SPR_ID]][1] - vData[2] - vData[4]) + (vData[4] - vData[6]);
                        iPosYClip -= vData[4];
                        iPosYImg -= vData[4];
                    }
                    break;

                default:
                    return;
            }

            if (!Define.USE_SETCLIP_OVER_DRAWREGION || _iTransform != 0) {
                Main.ms_XGraphics.drawRegion(ms_vImage[vData[0]],
                        vData[1], vData[2], vData[3], vData[4], _iTransform,
                        iPosXClip, iPosYClip, 0);
            } else {

                if (vData.length != 1) {
                    Main.ms_XGraphics.saveClip();
                    Main.ms_XGraphics.clipRect(iPosXClip, iPosYClip, vData[3], vData[4]);
                }

                Main.ms_XGraphics.drawImage(ms_vImage[vData[0]],
                        iPosXImg, iPosYImg, Graphics.LEFT | Graphics.TOP);

                if (vData.length != 1) {
                    Main.ms_XGraphics.restoreClip();
                }

            }
        } catch (Exception e) {
            System.out.println("" + e.toString());
            System.out.println("" + vData[0]);
        }
    }

    static void DrawTile(Graphics _g, int _iSprID, int _iPosX, int _iPosY) {

        int iTransform = 0;
        if (_iSprID < 0) {
            iTransform = TRANS_FLIPH;
            _iSprID -= Byte.MIN_VALUE;
        }

        try {

            if ((iTransform & TRANS_FLIPH) != 0) {
                _g.drawRegion(ms_vImage[TILE_DATA[_iSprID][0]],
                        TILE_DATA[_iSprID][1],
                        TILE_DATA[_iSprID][2],
                        TILE_DATA[_iSprID][3],
                        TILE_DATA[_iSprID][4], iTransform,
                        _iPosX, _iPosY, 0);
            } else {

                if (Define.USE_SETCLIP_OVER_DRAWREGION) {
                    _g.clipRect(_iPosX, _iPosY, TILE_DATA[_iSprID][3], TILE_DATA[_iSprID][4]);
                    _g.drawImage(ms_vImage[TILE_DATA[_iSprID][0]],
                            _iPosX - TILE_DATA[_iSprID][1],
                            _iPosY - TILE_DATA[_iSprID][2], 0);


                } else {
                    _g.drawRegion(ms_vImage[TILE_DATA[_iSprID][0]],
                            TILE_DATA[_iSprID][1],
                            TILE_DATA[_iSprID][2],
                            TILE_DATA[_iSprID][3],
                            TILE_DATA[_iSprID][4], iTransform,
                            _iPosX, _iPosY, 0);
                }
                _g.restoreClip();
            }

        } catch (Exception e) {
            System.out.println("" + _iSprID);
            //#if Debug
//#             Debug.DebugMsg("ERROR Drawtile; e:" + e.toString());
//#             System.out.println(""+_iSprID);
            //#endif
        }
    }
    public static final int GAMEBOX_ROUND_W = Define.SIZEY16;

    public static void drawGameBox(Graphics _g, int _iX, int _iY, int _iW, int _iH) {
        _g.setColor(ModeMenu.COLOR_GREEN_PARDUS_DARK);
        _g.setAlpha(0xdd);
        _g.fillRoundRect(_iX, _iY, _iW, _iH, GAMEBOX_ROUND_W, GAMEBOX_ROUND_W);
        _g.setAlpha(255);
    }
}