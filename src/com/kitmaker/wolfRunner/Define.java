package com.kitmaker.wolfRunner;

import javak.microedition.Settings;

public class Define {
    /*
     * GAME STATES
     */
    // ModeInit States
    static final int ST_INIT_SPLASH = 0;
    static final int ST_INIT_LANGUAGE = ST_INIT_SPLASH+1;
    static final int ST_INIT_SOUND = ST_INIT_LANGUAGE+1;
    
    // ModeMenu States
    static final int ST_MENU_MAIN = ST_INIT_SOUND+1;
    static final int ST_MENU_OPTIONS = ST_MENU_MAIN+1;
    static final int ST_MENU_LANGUAGE = ST_MENU_OPTIONS+1;
    static final int ST_MENU_INFO = ST_MENU_LANGUAGE+1;
    static final int ST_MENU_HELP = ST_MENU_INFO+1;
    static final int ST_MENU_ABOUT = ST_MENU_HELP+1;
    static final int ST_MENU_EXIT = ST_MENU_ABOUT+1;
    static final int ST_MENU_PLAY = ST_MENU_EXIT+1;
    
    
    // ModeGame States
    static final int ST_GAME_INIT = 100;
    static final int ST_GAME_PAUSE = ST_GAME_INIT+1;
    static final int ST_GAME_START = ST_GAME_PAUSE+1;
    // COLORS
    public static final int GREEN    = 0xff0ee50e;
    public static final int RED      = 0xffA02020;
    public static final int ORANGE   = 0xffff7800;
    public static final int YELLOW   = 0xfffcff00;
    public static final int PURPLE   = 0xffcc00ff;
    public static final int BLUE     = 0xff202080;
    public static final int WHITE    = 0xffffffff;
    public static final int GREEN2   = 0xff0ee50e;
    public static final int RED2     = 0xffA02020;
    public static final int ORANGE2  = 0xffff7800;
    public static final int YELLOW2  = 0xfffcff00;
    public static final int PURPLE2  = 0xffcc00ff;
    public static final int BLUE2    = 0xff202080;
    public static final int WHITE2   = 0xffffffff;
    public static final int[] TRACE_COLORS = {0xff888888, 0xff777700, 0xffff7700, 0xffaa6600, 0xff7700cc};
    

    public static char[] key_1 = new char[9];
    public static char[] key_2 = new char[9];
    public static char[] key_3 = new char[8];
    public static char[] key_4 = new char[8];
    public static char[] key_5 = new char[8];
    public static char[] key_6 = new char[10];
    public static char[] key_7 = new char[9];
    public static char[] key_8 = new char[8];
    public static char[] key_9 = new char[9];
    public static char KEY_0 = '0';
    public static boolean lowerCase = true;
    
    //SOFTKEYS
    static final int SK_NO_ICON = -1;
    static final int SK_PAUSE = 1;
    static final int SK_OK = 2;
    static final int SK_BACK = 3;
    static final int SK_SHOT = 4;
    static int ms_iDrawSKLeft = SK_NO_ICON;
    static int ms_iDrawSKRight = SK_NO_ICON;
    
    static int ms_iState, ms_iNewState, ms_iPrevState;
    static int ms_iStateTicks;
    static int ms_iStateFrames;
    static boolean ms_bNewState;
    static boolean ms_bNoDraw = true;  // Nothing will be drawn before first state is properly initialized
    
    //game
    static int ms_iGameState;
    public static boolean ms_bPaused;
    public static boolean ms_bRequestPause, ms_bRequestUnpause;
    static boolean ms_bFinishApp = false;
    public static String ms_sCriticalMesage = "";
    public static int ms_iCounter;
    
    // Teclado virtual
    public static int contador = 0;
    public static char vector[] = new char[8];
    static long cycleStartTime;
    
    public static final boolean CHEAT_AVAILABLE = false;
    public static final String ABOUT_TEXT[][] = {
        {
            WolfRunner.ms_zMIDletName,
            "v" + WolfRunner.ms_zMIDletVersion,
            " ",
            "Kitmaker Entertainment",
            "All rights reserved",
            "Copyright 2015",
        },
        {
            "Team members",
            " ",
            "FJ. Del Pino",
            "Toni Nicolau",
            "Luis Vald" + "\u00E9" + "s",
            "Sergio Rufo",
            "B. Boselli",
        }
    };
    
    //Game constans:
    public static short TOTAL_LEVELS = 12;
    public static short MAX_ANIMALS_TRANS = 3;
    public static int MAX_BULLETS = 6;
    
    // device info
    public static String DEVICE_MODEL;
    public static int COLOR_12_BIT = 4096;
    public static int COLOR_16_BIT = 65536;
    public static int DEVICE_NUMCOLORS;
    public static int DEVICE_NUMALPHAS;

    //Control frames:
  
    //#if FPS == 10
//#     public static final int FPS = 10;
    //#elif FPS == 15
//#    public static final int FPS = 15;
    //#else
    public static final int FPS = 30;
    //#endif
   
    
    public static final int MAXFPS = 30;
    public static final int FP_BITS = 8;
    public static final int FRAME_SPEED = (Define.MAXFPS/Define.FPS);//Fast 1,medium 2,slow 3
    
    //#if CLDC =="1.0"
    public static final long PI = 52707178; //MathFP.PI;
    //#else
//#    public static final double PI = Math.PI;
    //#endif

    // performance settings
    //#if API == "Motorola" || OptHeap > 0
//#     public static final boolean USE_SETCLIP_OVER_DRAWREGION = false;
    //#else
    public static final boolean USE_SETCLIP_OVER_DRAWREGION = true;
    //#endif
    
    // ## Screen Width & Height ##
    //#if android
//#     public static final short SIZEX = (short) (Settings.getWidth());
//#     public static final short SIZEY = (short) (Settings.getHeight());
    //#else
    public static final short SIZEX = Settings.SCR_WIDTH;
    public static final short SIZEY = Settings.SCR_HEIGHT;
    //#endif
    
    //#if i900
//#     public static final int i900_Y_ADJUSTMENT = 21;
    //#else
    public static final int i900_Y_ADJUSTMENT = 0;
    //#endif
    
    public static final short SIZEX2 = (short) (SIZEX >> 1);
    public static final short SIZEX4 = (short) (SIZEX2 >> 1);
    public static final short SIZEX8 = (short) (SIZEX4 >> 1);
    public static final short SIZEX12 = (short) (SIZEX / 12);
    public static final short SIZEX16 = (short) (SIZEX8 >> 1);
    public static final short SIZEX24 = (short) (SIZEX / 24);
    public static final short SIZEX32 = (short) (SIZEX16 >> 1);
    public static final short SIZEX64 = (short) (SIZEX32 >> 1);
    public static final short SIZEY2 = (short) (SIZEY >> 1);
    public static final short SIZEY4 = (short) (SIZEY2 >> 1);
    public static final short SIZEY8 = (short) (SIZEY4 >> 1);
    public static final short SIZEY12 = (short) (SIZEY / 12);
    public static final short SIZEY16 = (short) (SIZEY8 >> 1);
    public static final short SIZEY24 = (short) (SIZEY / 24);
    public static final short SIZEY32 = (short) (SIZEY16 >> 1);
    public static final short SIZEY64 = (short) (SIZEY32 >> 1);
    
    public static final short BASE_SIZEX = 240;
    public static final short BASE_SIZEY = 320;
    public static final short BASE_SIZEX2 = (short) (BASE_SIZEX >> 1);
    public static final short BASE_SIZEX4 = (short) (BASE_SIZEX2 >> 1);
    public static final short BASE_SIZEX8 = (short) (BASE_SIZEX4 >> 1);
    public static final short BASE_SIZEX12 = (short) (BASE_SIZEX / 12);
    public static final short BASE_SIZEX16 = (short) (BASE_SIZEX8 >> 1);
    public static final short BASE_SIZEX24 = (short) (BASE_SIZEX / 24);
    public static final short BASE_SIZEX32 = (short) (BASE_SIZEX16 >> 1);
    public static final short BASE_SIZEY2 = (short) (BASE_SIZEY >> 1);
    public static final short BASE_SIZEY4 = (short) (BASE_SIZEY2 >> 1);
    public static final short BASE_SIZEY8 = (short) (BASE_SIZEY4 >> 1);
    public static final short BASE_SIZEY12 = (short) (BASE_SIZEY / 12);
    public static final short BASE_SIZEY16 = (short) (BASE_SIZEY8 >> 1);
    public static final short BASE_SIZEY24 = (short) (BASE_SIZEY / 24);
    public static final short BASE_SIZEY32 = (short) (BASE_SIZEY16 >> 1);
    public static final int FP_SIZEX = BASE_SIZEX << FP_BITS;
    public static final int FP_SIZEY = BASE_SIZEY << FP_BITS;
    public static final int FP_SIZEX2 = (BASE_SIZEX >> 1) << FP_BITS;
    public static final int FP_SIZEX4 = (BASE_SIZEX2 >> 1) << FP_BITS;
    public static final int FP_SIZEX8 = (BASE_SIZEX4 >> 1) << FP_BITS;
    public static final int FP_SIZEX12 = (BASE_SIZEX / 12) << FP_BITS;
    public static final int FP_SIZEX16 = (BASE_SIZEX8 >> 1) << FP_BITS;
    public static final int FP_SIZEX24 = (BASE_SIZEX / 24) << FP_BITS;
    public static final int FP_SIZEX32 = (BASE_SIZEX16 >> 1) << FP_BITS;
    public static final int FP_SIZEY2 = (BASE_SIZEY >> 1) << FP_BITS;
    public static final int FP_SIZEY4 = (BASE_SIZEY2 >> 1) << FP_BITS;
    public static final int FP_SIZEY8 = (BASE_SIZEY4 >> 1) << FP_BITS;
    public static final int FP_SIZEY12 = (BASE_SIZEY / 12) << FP_BITS;
    public static final int FP_SIZEY16 = (BASE_SIZEY8 >> 1) << FP_BITS;
    public static final int FP_SIZEY24 = (BASE_SIZEY / 24) << FP_BITS;
    public static final int FP_SIZEY32 = (BASE_SIZEY16 >> 1) << FP_BITS;

    /*
     * PLAYER DIRECTION
     */
    public static boolean C_UP = false;
    public static boolean C_DOWN = false;
    public static boolean C_LEFT = false;
    public static boolean C_RIGHT = false;
    
    public static final int iUP = 1;
    public static final int iDOWN = 2;
    public static final int iLEFT = 3;
    public static final int iRIGHT = 4;
       
    /*
     * MESSAGES
     */
    public static final byte MSG_WELCOME = 0;
    public static final byte MSG_OBJECTIVE = 1;
    public static final byte MSG_TUTO1 = 2;
    public static final byte MSG_TUTO2 = 3;
    public static final byte MSG_PATH = 4;
    public static final byte MSG_HUNT = 5;
    public static final byte MSG_MISSED = 6;
    public static final byte MSG_HUNTED = 7;
    public static final byte MSG_COMPLETE = 8;
    /*
     * SOUND
     */
    public static final boolean ON = true;
    public static final boolean OFF = false;


    public static final int ICON_SIZE_X = SIZEX4;
    public static final int ICON_SIZE_Y = SIZEY8;
    
    //Splash coordenates:
    //#if ScreenWidth == 128 && ScreenHeight == 116
//#     public static final int MIR_X = 98;
//#     public static final int MIR_Y = 24;
    //#elif ScreenWidth == 128 && ScreenHeight == 128
//#     public static final int MIR_X = 102;
//#     public static final int MIR_Y = 22;
    //#elif ScreenWidth == 128 && (ScreenHeight == 147 || ScreenHeight == 149)
//#     public static final int MIR_X = 108;
//#     public static final int MIR_Y = 37;
    //#elif ScreenWidth == 128 && ScreenHeight == 160
//#     public static final int MIR_X = 108;
//#     public static final int MIR_Y = 42;
    //#elif ScreenWidth == 130 && ScreenHeight == 130
//#     public static final int MIR_X = 103;
//#     public static final int MIR_Y = 22;
    //#elif ScreenWidth == 130 && ScreenHeight == 130
//#     public static final int MIR_X = 104;
//#     public static final int MIR_Y = 22;
    //#elif ScreenWidth == 176 && (ScreenHeight >= 204 && ScreenHeight < 220)
//#     public static final int MIR_X = 134;
//#     public static final int MIR_Y = 49;
    //#elif ScreenWidth == 176 && ScreenHeight == 220
//#     public static final int MIR_X = 134;
//#     public static final int MIR_Y = 49;
    //#elif ScreenWidth == 220 && (ScreenHeight == 156 || ScreenHeight == 176)
//#     public static final int MIR_X = 188;
//#     public static final int MIR_Y = 48;
    //#elif ScreenWidth == 240 && ScreenHeight == 260
//#     public static final int MIR_X = 192;
//#     public static final int MIR_Y = 60;
    //#elif ScreenWidth == 240 && (ScreenHeight == 298 || ScreenHeight == 300)
//#     public static final int MIR_X = 180;
//#     public static final int MIR_Y = 70;
    //#elif ScreenWidth == 240 && (ScreenHeight == 304 || ScreenHeight == 305)
//#     public static final int MIR_X = 181;
//#     public static final int MIR_Y = 72;
    //#elif ScreenWidth == 240 && ScreenHeight == 320
    public static final int MIR_X = 195;
    public static final int MIR_Y = 50;
    //#elif ScreenWidth == 240 && (ScreenHeight == 379 || ScreenHeight == 380 || ScreenHeight == 400)
//#     public static final int MIR_X = 141;
//#     public static final int MIR_Y = 110;
    //#elif ScreenWidth == 320 && (ScreenHeight == 240 || ScreenHeight == 216)
//#     public static final int MIR_X = 265;
//#     public static final int MIR_Y = 66;
    //#elif ScreenWidth == 320 && ScreenHeight == 480
//#     public static final int MIR_X = 242;
//#     public static final int MIR_Y = 117;
    //#elif ScreenWidth == 352 && ScreenHeight == 416
//#     public static final int MIR_X = 260;
//#     public static final int MIR_Y = 87;
    //#elif ScreenWidth == 360 && ScreenHeight == 400
//#     public static final int MIR_X = 264;
//#     public static final int MIR_Y = 78;
    //#elif ScreenWidth == 360 && ScreenHeight == 480
//#     public static final int MIR_X = 273;
//#     public static final int MIR_Y = 113;
    //#elif ScreenWidth == 360 && ScreenHeight == 640
//#     public static final int MIR_X = 268;
//#     public static final int MIR_Y = 201;
    //#elif ScreenWidth == 400 && ScreenHeight == 216
//#     public static final int MIR_X = 0;
//#     public static final int MIR_Y = 0;
    //#elif ScreenWidth == 640 && ScreenHeight == 480
//#     public static final int MIR_X = 528;
//#     public static final int MIR_Y = 130;
    //#elif ScreenWidth == 400 && ScreenHeight == 240
//#     public static final int MIR_X = 0;
//#     public static final int MIR_Y = 0;
    //#elif ScreenWidth == 400 && ScreenHeight == 360
//#     public static final int MIR_X = 340;
//#     public static final int MIR_Y = 93;
    //#elif ScreenWidth == 480 && ScreenHeight == 320
//#     public static final int MIR_X = 396;
//#     public static final int MIR_Y = 73;
    //#elif ScreenWidth == 480 && ScreenHeight == 360
//#     public static final int MIR_X = 398;
//#     public static final int MIR_Y = 95;
    //#elif ScreenWidth == 480 && ScreenHeight == 640
//#     public static final int MIR_X = 393;
//#     public static final int MIR_Y = 107;
    //#elif ScreenWidth == 640 && ScreenHeight == 480
//#         public static final int MIR_X = 530;
//#         public static final int MIR_Y = 132;
    //#elif ScreenWidth == 800 && ScreenHeight == 480
//#         public static final int MIR_X = 690;
//#         public static final int MIR_Y = 98;
    //#elif ScreenWidth == 480 && ScreenHeight == 800
//#         public static final int MIR_X = 395;
//#         public static final int MIR_Y = 187;
    //#endif
    
    //#if API == "BlackBerry"
//#    public static final int LEFT_SOFTKEY_KEYCODE  = 1114112;
//#    public static final int RIGHT_SOFTKEY_KEYCODE = 1179648;  
//#    
//#        //int iBBkey_QW = 1;
//#     static char[] BBkey_QW = {'Q','W'};
//#     static int iBBkey_ER = 2;
//#     static char[] BBkey_ER = {'E','R'};
//#     static int iBBkey_TY = 3;
//#     static char[] BBkey_TY = {'T','Y'};
//#     static int iBBkey_UI = 4;
//#     static char[] BBkey_UI = {'U','I'};
//#     static int iBBkey_OP = 5;
//#     static char[] BBkey_OP = {'O','P'};
//#     static int iBBkey_AS = 6;
//#     static char[] BBkey_AS = {'A','S'};
//#     static int iBBkey_DF = 7;
//#     static char[] BBkey_DF = {'D','F'};
//#     static int iBBkey_GH = 8;
//#     static char[] BBkey_GH = {'G','H'};
//#     static int iBBkey_JK = 9;
//#     static char[] BBkey_JK = {'J','K'};
//#     static int iBBkey_L = 10;
//#     static char[] BBkey_L = {'L'};
//#     static int iBBkey_ZX = 11;
//#     static char[] BBkey_ZX = {'Z','X'};
//#     static int iBBkey_CV = 12;
//#     static char[] BBkey_CV = {'C','V'};
//#     static int iBBkey_BN = 13;
//#     static char[] BBkey_BN = {'B','N'};
//#     static int iBBkey_M = 14;
//#     static char[] BBkey_M = {'M'};
//#    
//#     static int IBBKEY_QW = 10;
//#     static int IBBKEY_ER = 11;
//#     static int IBBKEY_TY = 12;
//#     static int IBBKEY_UI = 13;
//#     static int IBBKEY_OP = 14;
//#     static int IBBKEY_AS = 15;
//#     static int IBBKEY_DF = 16;
//#     static int IBBKEY_GH = 17;
//#     static int IBBKEY_JK = 18;
//#     static int IBBKEY_L = 19;
//#     static int IBBKEY_ZX = 20;
//#     static int IBBKEY_CV = 21;
//#     static int IBBKEY_BN = 22;
//#     static int IBBKEY_M = 23;
    //#endif 
}
