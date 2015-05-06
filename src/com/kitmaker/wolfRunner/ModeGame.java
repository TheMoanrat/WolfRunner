package com.kitmaker.wolfRunner;

import com.kitmaker.manager.FntManager;
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
    public static int level;
    public static int points;

    static void InitState(int _iNewState) {
        //#if StaticHud
//#         ms_isPaintHud = true;
        //#endif

        switch (_iNewState) {
            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            // This state only is executed when we come tu the menu
            case Define.ST_GAME_INIT:
                loadGameImages();
                level = 1;
                points = 0;
                WolfPack.init();
                Scenario.init();
                break;
            case Define.ST_GAME_ANIMATING:
            case Define.ST_GAME_RUNNING:
                break;
        }
    }
    public static float levelUpCountdown;
    public static final float TIME_TO_LEVEL_UP = 30 * Main.SECOND;

    private static void changeLevel() {
        points++;
        levelUpCountdown += Main.deltaTime;
        if (levelUpCountdown > TIME_TO_LEVEL_UP) {
            levelUpCountdown = 0;
            level++;
        }
    }

    private static void loadGameImages() {
        GfxManager.ResetGraphics();

        switch (Define.ms_iState) {

            case Define.ST_GAME_INIT:
                //GfxManager.AddGraphic(GfxManager.GFXID_TILESET);
                GfxManager.AddGraphic(GfxManager.GFXID_GRASSTILE);
                GfxManager.AddGraphic(GfxManager.GFXID_SIDE_TREETILE);
                GfxManager.AddGraphic(GfxManager.GFXID_ROCKTILE);
                GfxManager.AddGraphic(GfxManager.GFXID_WOLF);
                GfxManager.AddGraphic(GfxManager.GFXID_SK_NEXT);
                GfxManager.AddGraphic(GfxManager.GFXID_MENU_BUTTONS);
                GfxManager.AddGraphic(GfxManager.GFXID_LIFES_ICON);


                GfxManager.LoadGraphics(true);
                break;
            case Define.ST_GAME_ANIMATING:

            case Define.ST_GAME_RUNNING:
                break;
        }
    }
    private static final int OPT_RESTART_GAME = 0;
    private static final int OPT_BACK_TO_MENU = OPT_RESTART_GAME + 1;
    private static int gameOverMenuOption = OPT_RESTART_GAME;

    private static void restartGame() {
        if (Main.GameKeyPressed(Main.KEYINT_FIRE, true)
                || Main.GameKeyPressed(Main.KEYINT_SKLEFT, true)) {
            if (gameOverMenuOption == OPT_RESTART_GAME) {
                Main.RequestStateChange(Define.ST_GAME_INIT);
            } else if (gameOverMenuOption == OPT_BACK_TO_MENU) {
                Main.RequestStateChange(Define.ST_MENU_MAIN);
            }
        }
        if (Main.GameKeyPressed(Main.KEYINT_DOWN, true)
                || Main.GameKeyPressed(Main.KEYINT_UP, true)) {
            if (gameOverMenuOption == OPT_RESTART_GAME) {
                gameOverMenuOption = OPT_BACK_TO_MENU;
                return;
            }
            if (gameOverMenuOption == OPT_BACK_TO_MENU) {
                gameOverMenuOption = OPT_RESTART_GAME;
                return;
            }
        }
    }
    /*
     * load the images used during the gameplay
     */

    static void Run() {
        switch (Define.ms_iState) {
            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
            case Define.ST_GAME_INIT:
                Main.RequestStateChange(Define.ST_GAME_ANIMATING);
                break;
            case Define.ST_GAME_ANIMATING:
                Scenario.Run();
                WolfPack.Run();
                break;
            case Define.ST_GAME_RUNNING:
                Scenario.Run();
                WolfPack.Run();
                changeLevel();
                break;
            case Define.ST_GAME_OVER:
                Scenario.Run();
                WolfPack.Run();
                restartGame();
        }
    }

    static void HandleInput() {
    }

    static void Draw(Graphics g) {
        switch (Define.ms_iState) {
            case Define.ST_GAME_INIT:
                break;
            case Define.ST_GAME_ANIMATING:
            case Define.ST_GAME_RUNNING:
                Scenario.Draw(g);
                WolfPack.Draw(g);
                Hud.Draw(g);
                break;
            case Define.ST_GAME_OVER:
                Scenario.Draw(g);
                WolfPack.Draw(g);
                Hud.Draw(g);
                drawGameOvermenu(g);
                break;
        }
    }

    static void drawGameOvermenu(Graphics g) {
        g.setClip(0,
                Define.BASE_SIZEY - GfxManager.SPRITE_DATA[GfxManager.SK_NEXT[0]][GfxManager.SPR_HEIGHT],
                GfxManager.SPRITE_DATA[GfxManager.SK_NEXT[0]][GfxManager.SPR_WIDTH],
                GfxManager.SPRITE_DATA[GfxManager.SK_NEXT[0]][GfxManager.SPR_HEIGHT]);
        g.drawImage(GfxManager.ms_vImage[GfxManager.GFXID_SK_NEXT],
                0,
                Define.BASE_SIZEY,
                Graphics.BOTTOM);
        g.setClip(0, 0, Define.BASE_SIZEX, Define.BASE_SIZEY);

        drawGameOvermenuOption(g, gameOverMenuOption == OPT_RESTART_GAME,Define.BASE_SIZEY4,TxtManager.TXT_EXIT_RUN);
        drawGameOvermenuOption(g, gameOverMenuOption == OPT_BACK_TO_MENU,Define.BASE_SIZEY2, TxtManager.TXT_PLAY_AGAIN);
    }

    public static void drawGameOvermenuOption(Graphics g, boolean isActive, int y, int textIndex) {
        int font, button;

        if (isActive) {
            font = FntManager.FONT_ACTIVE;
            button = 1;
        } else {
            font = FntManager.FONT_INACTIVE;
            button = 0;
        }
        g.setClip(Define.BASE_SIZEX2 - (GfxManager.SPRITE_DATA[GfxManager.MENU_BUTTON[button]][GfxManager.SPR_WIDTH] / 2),
                y,
                GfxManager.SPRITE_DATA[GfxManager.MENU_BUTTON[button]][GfxManager.SPR_WIDTH],
                GfxManager.SPRITE_DATA[GfxManager.MENU_BUTTON[button]][GfxManager.SPR_HEIGHT]);
        g.drawImage(GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS],
                Define.BASE_SIZEX2,
                y - GfxManager.SPRITE_DATA[GfxManager.MENU_BUTTON[button]][GfxManager.SPR_POS_Y],
                Graphics.HCENTER);
        FntManager.DrawFont(g,
                font,
                TxtManager.ms_vText[textIndex],
                Define.BASE_SIZEX2,
                y + (GfxManager.SPRITE_DATA[GfxManager.MENU_BUTTON[button]][GfxManager.SPR_HEIGHT] / 2),
                Graphics.HCENTER | Graphics.VCENTER,
                TxtManager.ms_vText[textIndex].length());
        g.setClip(0, 0, Define.BASE_SIZEX, Define.BASE_SIZEY);

    }
}
