package com.kitmaker.wolfRunner;

import com.kitmaker.manager.GfxManager;
import com.kitmaker.manager.TxtManager;
import com.kitmaker.manager.SndManager;
import java.io.DataInputStream;
import java.util.Random;
import javak.microedition.lcdui.Graphics;

/*
 * @author Kitmaker Team A
 */
public class ModeGame extends Define {
    //#if StaticHud
//#     private static boolean ms_isPaintHud = true;
    //#endif

    //Debug variable
    public static String ms_sDebugText = "";
    public static int ms_iInc = 0;
    private static boolean ms_isToGameExit = false;
    // GAME PAUSE menu //-----------------------------------------------------------
    private static int ms_iMenuItems;
    static final byte[] PAUSE_TITLES = {
        -1,
        -1,
        TxtManager.TXT_OPTIONS_SOUND,
        TxtManager.TXT_OPTIONS_VIBRATE,
        -1,};
    static final short[][] PAUSE_TEXTID = {
        {TxtManager.TXT_CONTINUE},
        {TxtManager.TXT_MISSION},
        //#if Sound == "OnOff"
        {TxtManager.TXT_YES, TxtManager.TXT_NO},
        //#else
        //#          {TxtManager.TXT_SOUND_OFF, TxtManager.TXT_SOUND_LOUD, TxtManager.TXT_SOUND_MEDIUM, TxtManager.TXT_SOUND_LOW},
        //#endif
        {TxtManager.TXT_YES, TxtManager.TXT_NO},
        {TxtManager.TXT_MAIN_MENU_EXIT}
    };
    static short[][] ms_iDinamicMenuTxt;
    static short[] ms_iDinamicMenuNewSt;
    static int ms_iNextState;
    // RunGame
    public static Random rnd = new Random();
    // TOUCH
    public static int ms_iTouchedPointX = 0;
    public static int ms_iTouchedPointY = 0;
    public static int ms_iTouchArea = (SIZEY8 + SIZEX8) >> 1;
    public static int ms_iTouchX0 = 0;
    public static int ms_iTouchY0 = 0;
    public static int ms_iTouchX1 = 0;
    public static int ms_iTouchY1 = 0;
    public static int ms_iTouchWidth = 0;
    public static int ms_iTouchHeight = 0;
    public static int ms_iDeadZoneX = 0;
    public static int ms_iDeadZoneY = 0;
    public static int ms_iDeadZoneWidth = 0;
    public static int ms_iDeadZoneHeight = 0;
    // Debug
    static boolean mst_bShowDebug;
    //Text questions vars
    private static String[] ms_sQuestionText;
    private static short[][] ms_sNoHuntText;
    private static int MAP_MARGIN_U;
    public static int MAP_HEIGHT;
    private static int MAP_HEIGHT2;
    private static final int MARGIN_WORLD = Define.SIZEX > Define.SIZEY ? Define.SIZEY24 + (Define.SIZEY24 % 2) : Define.SIZEX24 + (Define.SIZEX24 % 2);
    public static final int MAP_MARGIN_L = MARGIN_WORLD;
    public static final int MAP_WITDH = Define.SIZEX - (MAP_MARGIN_L << 1);
    private static final int MAP_WITDH2 = MAP_WITDH >> 1;
    public static int homeMapPosX = 0;
    public static int homeMapPosY = 0;
    public static int ms_iArrowMapRunX = 0;
    public static int ms_iArrowMapRunY = 0;
    private static int ms_iArrowMapDrawX = 0;
    private static int ms_iArrowMapDrawY = 0;
    private static int ms_iArrowMapCircle;
    public static int ms_iArrowMapPositionRunX = -1;
    public static int ms_iArrowMapPositionRunY = -1;
    static String zArray[];
    public static int ms_iBackgroundY1;
    public static int ms_iBackgroundX1;
    public static int ms_iBackgroundY2;
    public static int ms_iBackgroundX2;

    static void InitState(int _iNewState) {
        //#if StaticHud
//#         ms_isPaintHud = true;
        //#endif

        switch (_iNewState) {

            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            // This state only is executed when we come tu the menu
            case Define.ST_GAME_INIT:
                loadGameImages();
                WolfPack.init();
                Scenario.init();
                break;
            case Define.ST_GAME_ANIMATING:
            case Define.ST_GAME_RUNNING:
//                initGame();
                break;
        }
    }


    /*
     * load the images used during the gameplay
     */
    private static void loadGameImages() {
        GfxManager.ResetGraphics();

        switch (Define.ms_iState) {

            case Define.ST_GAME_INIT:
                //GfxManager.AddGraphic(GfxManager.GFXID_TILESET);
                GfxManager.AddGraphic(GfxManager.GFXID_GRASSTILE);
                GfxManager.AddGraphic(GfxManager.GFXID_SIDE_TREETILE);
                GfxManager.AddGraphic(GfxManager.GFXID_ROCKTILE);
                GfxManager.AddGraphic(GfxManager.GFXID_WOLF);

                GfxManager.LoadGraphics(true);
                break;
            case Define.ST_GAME_ANIMATING:

            case Define.ST_GAME_RUNNING:
                break;
        }
    }

    static void Draw(Graphics _g) {
        switch (Define.ms_iState) {
            case Define.ST_GAME_INIT:
                break;
            case Define.ST_GAME_ANIMATING:

            case Define.ST_GAME_RUNNING:
                Scenario.Draw(_g);
                WolfPack.Draw(_g);
                break;
        }


        //#if Debug
//#         if (mst_bShowDebug) {
//#             _g.setColor(0xff000000);
//#         }
        //#endif

        // trazas tracitas
        /*
         * _g.setColor(0xffffffff); _g.drawString(Main.ms_iKeyInt_Map + "",
         * 10+1, 80+1); _g.drawString(Main.mst_iNumKeyPressed + "", 10+1, 95+1);
         * _g.drawString(Main.mst_iNumKeyReleased + "", 10+1, 110+1);
         * _g.setColor(0xffff0000); _g.drawString(Main.ms_iKeyInt_Map + "", 10,
         * 80); _g.drawString(Main.mst_iNumKeyPressed + "", 10, 95);
         * _g.drawString(Main.mst_iNumKeyReleased + "", 10, 110);
         */
    }

    static void Run() {
        switch (Define.ms_iState) {
            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
            case Define.ST_GAME_INIT:
                Main.RequestStateChange(Define.ST_GAME_ANIMATING);
                break;
            case Define.ST_GAME_ANIMATING:
            case Define.ST_GAME_RUNNING:
                Scenario.Run();
                WolfPack.Run();
                break;
        }
    }

    static void HandleInput() {
    }

    private static void initGame() {

        //Touchpad
        if (Main.TOUCHSCREEN_SUPPORTED) {
//            ms_iDeadZoneWidth = GfxManager.ms_vImage[GfxManager.GFXID_PAD2].getWidth();
//            ms_iDeadZoneHeight = GfxManager.ms_vImage[GfxManager.GFXID_PAD2].getHeight();
//            ms_iTouchWidth = GfxManager.ms_vImage[GfxManager.GFXID_PAD1].getWidth();
//            ms_iTouchHeight = GfxManager.ms_vImage[GfxManager.GFXID_PAD1].getHeight();
        }

        //Main.RequestStateChange(Main.ST_GAME_MAP);
//        SndManager.StopMusic();
//        SndManager.PlayMusic(SndManager.MUSIC_MAP, true, 0);

    }
    //TODO make this go at acording to the wolf speed

    public static void runPad() {

        if ((Main.ms_iScreenTouched_Map[0] == Main.ACTION_DOWN)
                && (Main.ms_iScreenTouched_Frames < 5)
                && Main.ms_iScreenOrigin_Y[0] > MAP_MARGIN_U) {
            ms_iTouchX0 = Main.ms_iScreenOrigin_X[0];
            ms_iTouchY0 = Main.ms_iScreenOrigin_Y[0];
            ms_iTouchX1 = Main.ms_iScreenTouched_X[0];
            ms_iTouchY1 = Main.ms_iScreenTouched_Y[0];

        } else if ((Main.ms_iScreenTouched_Map[0] == Main.ACTION_MOVE)
                || (Main.ms_iScreenTouched_Map[1] == Main.ACTION_MOVE)
                || (Main.ms_iScreenTouched_Map[0] == Main.ACTION_DOWN)
                || (Main.ms_iScreenTouched_Map[1] == Main.ACTION_DOWN)) {
            ms_iTouchX1 = Main.ms_iScreenTouched_X[0];
            ms_iTouchY1 = Main.ms_iScreenTouched_Y[0];

            if (((ms_iTouchX0 != 0) && (ms_iTouchX1 != 0)
                    && (ms_iTouchY0 != 0) && (ms_iTouchY1 != 0))) {

                ms_iDeadZoneX = ms_iTouchX0;
                ms_iDeadZoneY = ms_iTouchY0;

                // UP zone
                if ((ms_iTouchY1 > 0) && (ms_iTouchY1 < ms_iDeadZoneY - (ms_iDeadZoneWidth >> 2))) {
                    C_UP = true;
                } // DOWN zone
                else if ((ms_iTouchY1 > ms_iDeadZoneY + ms_iTouchWidth - (ms_iDeadZoneWidth >> 2)) && (ms_iTouchY1 < Define.SIZEY)) {
                    C_DOWN = true;
                }
                // LEFT zone
                if ((ms_iTouchX1 > 0) && (ms_iTouchX1 < ms_iDeadZoneX - (ms_iTouchWidth >> 2))) {
                    C_LEFT = true;
                } // RIGHT zone
                else if ((ms_iTouchX1 > ms_iDeadZoneX + ms_iTouchWidth - (ms_iDeadZoneWidth >> 2)) && (ms_iTouchX1 < Define.SIZEX)) {
                    C_RIGHT = true;
                }
            }
        } else if ((Main.ms_iScreenTouched_Map[0] == Main.ACTION_UP)
                || (Main.ms_iScreenTouched_Map[1] == Main.ACTION_UP)) {
            //#if StaticHud
//#             if (ms_iTouchX0 != 0) {
//#                 ms_isPaintHud = true;
//#             }
            //#endif
            ms_iTouchX1 = 0;
            ms_iTouchY1 = 0;
            ms_iTouchX0 = 0;
            ms_iTouchY0 = 0;
        }
    }

    public static void drawPad(Graphics _g) {
        _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);

        ms_iDeadZoneX = ms_iTouchX0;
        ms_iDeadZoneY = ms_iTouchY0;

//        _g.drawImage(GfxManager.ms_vImage[GfxManager.GFXID_PAD2], 
//            ms_iDeadZoneX-(GfxManager.ms_vImage[GfxManager.GFXID_PAD2].getWidth()>>1), 
//            ms_iDeadZoneY-(GfxManager.ms_vImage[GfxManager.GFXID_PAD2].getHeight()>>1), 0);

        int iPadX = ms_iDeadZoneX - (ms_iDeadZoneWidth >> 2);
        int iPadY = ms_iDeadZoneY - (ms_iDeadZoneWidth >> 2);

        if (Main.ms_iScreenTouched_Map[0] != Main.ACTION_UP) {
            // UP zone
            if (C_UP) {
                iPadY = ms_iDeadZoneY - (ms_iDeadZoneWidth >> 1) - (ms_iDeadZoneWidth >> 3);
            } // DOWN zone
            else if (C_DOWN) {
                iPadY = ms_iDeadZoneY + (ms_iDeadZoneWidth >> 2) - (ms_iDeadZoneWidth >> 3);
            }
            // LEFT zone
            if (C_LEFT) {
                iPadX = ms_iDeadZoneX - (ms_iDeadZoneWidth >> 1) - (ms_iDeadZoneWidth >> 3);
            } // RIGHT zone
            else if (C_RIGHT) {
                iPadX = ms_iDeadZoneX + (ms_iDeadZoneWidth >> 2) - (ms_iDeadZoneWidth >> 3);
            }
        }
//        _g.drawImage(GfxManager.ms_vImage[GfxManager.GFXID_PAD1], iPadX, iPadY, 0);
    }

    public static void drawMenuBackground(Graphics _g) {
        _g.saveClip();
        _g.clipRect(0, 0, Define.SIZEX, Define.SIZEY);

        int capas = 16;
        int size = Define.SIZEY / capas;
        _g.setColor(0xff635214);
        _g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
        for (int i = 0; i < capas + 1; i++) {
            _g.setBlendedColor(0xff98c465, 0xff635214, i * capas);
            _g.fillRect(0, (size * i), Define.SIZEX, size);
        }

        _g.restoreClip();
    }
}
