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
    // Debug
    static boolean mst_bShowDebug;
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
            case Define.ST_GAME_PAUSE:
            case Define.ST_GAME_OVER:
        }
    }
    public static float levelUpCountdown;
    public static final float TIME_TO_LEVEL_UP = 30 * Main.SECOND;

    private static void changeLevel() {
        points++;
        levelUpCountdown += Main.averageDt;
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
                GfxManager.AddGraphic(GfxManager.GFXID_ROCKTILE);
                GfxManager.AddGraphic(GfxManager.GFXID_FIRST_RIVER_TILE);
                GfxManager.AddGraphic(GfxManager.GFXID_SECOND_RIVER_TILE);
                GfxManager.AddGraphic(GfxManager.GFXID_THIRD_RIVER_TILE);
                GfxManager.AddGraphic(GfxManager.GFXID_DUST);
                GfxManager.AddGraphic(GfxManager.GFXID_WOLF);
                GfxManager.AddGraphic(GfxManager.GFXID_LIFES_ICON);
                GfxManager.AddGraphic(GfxManager.GFXID_SK_MENU);
                GfxManager.AddGraphic(GfxManager.GFXID_MENU_BUTTONS);
                GfxManager.LoadGraphics(true);
                break;
            case Define.ST_GAME_ANIMATING:

            case Define.ST_GAME_RUNNING:
                break;
            case Define.ST_GAME_PAUSE:
            case Define.ST_GAME_OVER:

                break;
        }
    }
     public static boolean pressAccept() {
        if (Main.GameKeyPressed(Main.KEYINT_FIRE, true)
                || Main.GameKeyPressed(Main.KEYINT_SKLEFT, true)
                || Main.GameKeyPressed(Main.KEYINT_5, true)) {
            return true;
        }
        return false;
    }
     public static boolean pressUp(){
         if (Main.GameKeyPressed(Main.KEYINT_UP, true)
                || Main.GameKeyPressed(Main.KEYINT_2, true)) {
            return true;
        }
        return false;
     }
     public static boolean pressDown(){
         if (Main.GameKeyPressed(Main.KEYINT_DOWN, true)
                || Main.GameKeyPressed(Main.KEYINT_8, true)) {
            return true;
        }
        return false;
     }
    /*
     * Menu Options (pause and game over)
     */
    private static final int OPTION1 = 0;
    private static final int OPTION2 = OPTION1 + 1;
    private static int selectedMenuOption = OPTION1;

    private static void useMenu(int options) {
        if (pressUp()) {
            selectedMenuOption--;
            if (selectedMenuOption < 0) {
                selectedMenuOption = options;
            }
        }
        if (pressDown()) {
            selectedMenuOption++;
            if (selectedMenuOption > options) {
                selectedMenuOption = 0;
            }
        }
    }

    private static void pauseGame() {
        useMenu(2);
        if (pressAccept()) {
            switch (selectedMenuOption) {
                case OPTION1:
                    Main.RequestStateChange(Define.ST_GAME_RUNNING);
                    break;
                case OPTION2:
                    Main.RequestStateChange(Define.ST_MENU_MAIN);
                    break;
            }
        }
    }

    private static void restartGame() {
        useMenu(2);
        if (pressAccept()) {
            switch (selectedMenuOption) {
                case OPTION1:
                    Main.RequestStateChange(Define.ST_GAME_INIT);
                    break;
                case OPTION2:
                    Main.RequestStateChange(Define.ST_MENU_MAIN);
                    break;
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
                if (Main.GameKeyPressed(Main.KEYINT_SKLEFT, true)) {
                    Main.RequestStateChange(ST_GAME_PAUSE);
                }
                break;
            case Define.ST_GAME_PAUSE:
                pauseGame();
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
            case Define.ST_GAME_PAUSE:
                Scenario.Draw(g);
                WolfPack.Draw(g);
                Hud.Draw(g);
                drawPause(g);
                break;
            case Define.ST_GAME_OVER:
                Scenario.Draw(g);
                WolfPack.Draw(g);
                Hud.Draw(g);
                drawGameOvermenu(g);
                break;
        }
    }

    private static void drawGameOvermenu(Graphics g) {
        Menu.DrawNavigationIcons(g, Define.SK_OK, Define.SK_BACK);
        drawMenuOption(g, selectedMenuOption == OPTION1, Define.BASE_SIZEY4, TxtManager.TXT_PLAY_AGAIN);
        drawMenuOption(g, selectedMenuOption == OPTION2, Define.BASE_SIZEY2, TxtManager.TXT_EXIT_RUN);
    }

    private static void drawPause(Graphics g) {
        Menu.DrawNavigationIcons(g, Define.SK_OK, Define.SK_BACK);
        drawMenuOption(g, selectedMenuOption == OPTION1, Define.BASE_SIZEY4, TxtManager.TXT_RESUME);
        drawMenuOption(g, selectedMenuOption == OPTION2, Define.BASE_SIZEY2, TxtManager.TXT_EXIT_RUN);
    }

    public static void drawMenuOption(Graphics g, boolean isActive, int y, int textIndex) {
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
