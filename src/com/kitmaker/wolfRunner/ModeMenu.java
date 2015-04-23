package com.kitmaker.wolfRunner;

import com.kitmaker.manager.GfxManager;
import com.kitmaker.manager.TxtManager;
import com.kitmaker.manager.RmsManager;
import com.kitmaker.manager.SndManager;
import com.kitmaker.manager.FntManager;
import javak.microedition.lcdui.Graphics;

/*
 * @author Toni Nicolau
 */
public class ModeMenu extends Define {
    
    // ## Cheat ##
    static final int CHEAT_NUMPRESSES = 10;
    static final int[] CHEAT_KEYSEQUENCE = {0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3};
    static int ms_iCheatCorrectPresses;
    static final int DELETE_NUMPRESSES = 10;
    static int ms_iDeleteCorrectPresses;
    static boolean ms_bCheatMode = false;
    
    private static int splashTimeDuration = 3000;
    private static long splashTimeInit;
    
    
    static final byte[] MENU_TITLE = {
        -1, // 01 LOGO
        TxtManager.TXT_OPTIONS_LANGUAGE, // 02 LANGUAGE
        TxtManager.TXT_OPTIONS_SOUND, // 03 SOUND
        -1, // 05 MAIN
        TxtManager.TXT_MAIN_MENU_OPTIONS, // 06 OPTIONS
        TxtManager.TXT_OPTIONS_LANGUAGE, // 07 LANGUAGE
        -1, // 08 INFO
        -1, // 09 HELP
        -1, // 10 ABOUT
        TxtManager.TXT_TITLE_QUIT, 
        -1, // 14 SELECT MISSION
    };
    
    public static int ms_iModeGame;
    
    // Language menu //----------------------------------------------------------
    static short[][] LANGUAGE_TEXTID = {
        {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8},
    };
    
    // Sound menu //----------------------------------------------------------
    static final short[][] SOUNDMENU_TEXTID = {
        //#if Sound == "OnOff"
        {TxtManager.TXT_YES}, // Sound off
        {TxtManager.TXT_NO}, // Sound on
        //#elif Sound == "Volume"
//#         {TxtManager.TXT_SOUND_LOUD, TxtManager.TXT_SOUND_MEDIUM, TxtManager.TXT_SOUND_LOW}, // Sound off
//#         {TxtManager.TXT_SOUND_OFF}, // Sound on
        //#endif
    };
    
    // Main menu //--------------------------------------------------------------
    static final short[][] MAINMENU_TEXTID = {
        {TxtManager.TXT_MAIN_MENU_PLAY},
        {TxtManager.TXT_MAIN_MENU_OPTIONS},
        {TxtManager.TXT_MAIN_MENU_INFO},
        {TxtManager.TXT_MAIN_MENU_EXIT},
    };
    static final short[] MAINMENU_NEXTST = {
        Define.ST_MENU_PLAY,
        Define.ST_MENU_OPTIONS,
        Define.ST_MENU_INFO,
        Define.ST_MENU_EXIT,
    };
    
    
    // Info menu //--------------------------------------------------------------
    static final short[][] INFO_TEXTID = {
        {TxtManager.TXT_INFO_HELP},
        {TxtManager.TXT_INFO_ABOUT},
    };
    static final short[] INFO_NEXTST = {
        Define.ST_MENU_HELP,
        Define.ST_MENU_ABOUT,
    };
    

    // Options menu //-----------------------------------------------------------
    static final short[] OPTIONS_TITLES = {
        TxtManager.TXT_OPTIONS_SOUND,
        TxtManager.TXT_OPTIONS_VIBRATE,
        TxtManager.TXT_OPTIONS_LANGUAGE,
    };
    static final short[][] OPTIONS_TEXTID = {
        //#if Sound == "OnOff"
        {TxtManager.TXT_YES, TxtManager.TXT_NO},
        //#else
//#         {TxtManager.TXT_SOUND_OFF,TxtManager.TXT_SOUND_LOUD, TxtManager.TXT_SOUND_MEDIUM, TxtManager.TXT_SOUND_LOW},
        //#endif
        {TxtManager.TXT_YES, TxtManager.TXT_NO},
        {TxtManager.TXT_OPTIONS_LANGUAGE},
    };
    static final short[] OPTIONS_NEXTST = {
        -1,
        -1,
        Define.ST_MENU_LANGUAGE
    };
    
    // Quit menu //--------------------------------------------------------------
    static final short[][] MENUQUIT_NO_YES_TEXTID = {
        {TxtManager.TXT_YES},
        {TxtManager.TXT_NO}
    };
    // Comfirm play //--------------------------------------------------------------
    static final short[][] MENU_CONTINUE_NEW = {
        {TxtManager.TXT_CONTINUE_PLAY},
        {TxtManager.TXT_NEW_GAME}
    };
    // Warning play //--------------------------------------------------------------
    static final short[][] MENU_WARNING = {
        {TxtManager.TXT_RETURN},
        {TxtManager.TXT_CONTINUE}
    };
    
    static short[][] ms_iDinamicMenuTxt;
    static short[] ms_iDinamicMenuNewSt;
    static int ms_iNextState;
       
    // help 
    static final short[][] HELP_NONTOUCH_TEXTID = {
        //#if SIZE=="Small"
//#         {TxtManager.TXT_NT_HELP_MENU_TITLE,TxtManager.TXT_NT_HELP_MENU_TEXT1, TxtManager.TXT_NT_HELP_MENU_TEXT2},
//#         {TxtManager.TXT_NT_HELP_MENU_TITLE,TxtManager.TXT_NT_HELP_MENU_TEXT3, TxtManager.TXT_NT_HELP_MENU_TEXT4},
//#         {TxtManager.TXT_NT_HELP_GAME_TITLE,TxtManager.TXT_NT_HELP_GAME_TEXT1, TxtManager.TXT_NT_HELP_GAME_TEXT2},
//#         {TxtManager.TXT_NT_HELP_GAME_TITLE,TxtManager.TXT_NT_HELP_GAME_TEXT3, TxtManager.TXT_NT_HELP_GAME_TEXT4},
        //#else
        {TxtManager.TXT_NT_HELP_MENU_TITLE,0, TxtManager.TXT_NT_HELP_MENU_TEXT1, TxtManager.TXT_NT_HELP_MENU_TEXT2},
        {TxtManager.TXT_NT_HELP_MENU_TITLE,0, TxtManager.TXT_NT_HELP_MENU_TEXT3, TxtManager.TXT_NT_HELP_MENU_TEXT4},
        {TxtManager.TXT_NT_HELP_GAME_TITLE,0, TxtManager.TXT_NT_HELP_GAME_TEXT1, TxtManager.TXT_NT_HELP_GAME_TEXT2},
        {TxtManager.TXT_NT_HELP_GAME_TITLE,0, TxtManager.TXT_NT_HELP_GAME_TEXT3, TxtManager.TXT_NT_HELP_GAME_TEXT4},
        //#endif
    };
    static final short[][] HELP_TOUCH_TEXTID = {
        //#if SIZE=="Small"
//#         {TxtManager.TXT_T_HELP_MENU_TITLE,TxtManager.TXT_T_HELP_MENU_TEXT1, TxtManager.TXT_T_HELP_MENU_TEXT2},
//#         {TxtManager.TXT_T_HELP_MENU_TITLE,TxtManager.TXT_T_HELP_MENU_TEXT3, TxtManager.TXT_T_HELP_MENU_TEXT4},
//#         {TxtManager.TXT_T_HELP_GAME_TITLE,TxtManager.TXT_T_HELP_GAME_TEXT1, TxtManager.TXT_T_HELP_GAME_TEXT2},
//#         {TxtManager.TXT_T_HELP_GAME_TITLE,TxtManager.TXT_T_HELP_GAME_TEXT3, TxtManager.TXT_T_HELP_GAME_TEXT4},
        //#else
        {TxtManager.TXT_T_HELP_MENU_TITLE,0, TxtManager.TXT_T_HELP_MENU_TEXT1, TxtManager.TXT_T_HELP_MENU_TEXT2},
        {TxtManager.TXT_T_HELP_MENU_TITLE,0, TxtManager.TXT_T_HELP_MENU_TEXT3, TxtManager.TXT_T_HELP_MENU_TEXT4},
        {TxtManager.TXT_T_HELP_GAME_TITLE,0, TxtManager.TXT_T_HELP_GAME_TEXT1, TxtManager.TXT_T_HELP_GAME_TEXT2},
        {TxtManager.TXT_T_HELP_GAME_TITLE,0, TxtManager.TXT_T_HELP_GAME_TEXT3, TxtManager.TXT_T_HELP_GAME_TEXT4},
        //#endif
    };
    
    private static short[][] ms_iDinamicHelpId;
    private static int ms_iHelpPage = 0;
    private static int ms_iHelpNumPages = 0;
    private static int ms_iHelpFrame;

    
    //stars
    public static boolean fraseRepartida;
    
    static short[] iDinamicMenuTitles;
    static int iOptIndex;
    
    
    public static boolean ms_bOptionPressed;
    static int tempLines;
    
    public static final int COLOR_GREEN_PARDUS = 0Xff717f24;//verde pardo
    public static final int COLOR_GREEN_PARDUS_DARK = 0Xff3c4314;//verde pardo
    public static final int COLOR_MARC = 0Xfffff8d1;
    public static final int COLOR_BROWN_DARK = 0Xff643807;
    public static final int COLOR_BROWN = 0Xffb76f1d;
    public static final int COLOR_YELLOW = 0Xfffdca00;
    
    //Selectores de listados:
    public static final int COLOR_SELECTOR_1= 0xff7a1a00;
    public static final int COLOR_SELECTOR_2= 0xffae4f36;
    //Graphic menu vars:
    public static int ms_iColorSelectorFrame;
    public static int ms_iColorSelectorState;
    public static int ms_iPercentage;
    
    
    static final int SOUNDCHANGE_DELAY = 500;
    static int ms_iLastMainMenuOption;  
    static int ms_iLastInfoMenuOption;
    
    static void InitState(int _iNewState) {
        
        switch (Define.ms_iState) {
            
            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_INIT_SPLASH:
                
                LoadMenuImages(false);
                WolfRunner.checkLanguage();
                
                splashTimeInit = System.currentTimeMillis();
                break;

            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_INIT_LANGUAGE:
                ModeMenu.InitMenu(LANGUAGE_TEXTID, null);
                RmsManager.RmsLoadSaveData(RmsManager.FILE_SYS, RmsManager.MODE_SAVE);
                break;

            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_INIT_SOUND:
                ModeMenu.InitMenu(SOUNDMENU_TEXTID, null);
                Menu.ms_iMarkedOption = 1;
                break;
                
            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_MENU_MAIN:
                LoadMenuImages((Define.ms_iPrevState == Define.ST_INIT_SPLASH || Define.ms_iPrevState > Define.ST_GAME_INIT));
                InitMenu(MAINMENU_TEXTID, MAINMENU_NEXTST);
                
                if (Define.ms_iPrevState == Define.ST_INIT_SPLASH || Define.ms_iPrevState > Define.ST_GAME_INIT) {
                    SndManager.DeleteFX(SndManager.FX_DEATH);
                    SndManager.DeleteFX(SndManager.FX_RELOAD);
                    SndManager.LoadFX(SndManager.FX_SELECT);
                    SndManager.LoadFX(SndManager.FX_BACK);
                    SndManager.LoadFX(SndManager.FX_SHOOT);
                }
                break;

            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_MENU_OPTIONS:
                InitMenu(OPTIONS_TEXTID, OPTIONS_NEXTST);
                break;

            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_MENU_LANGUAGE:
                InitMenu(LANGUAGE_TEXTID, null);
                break;
            
            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_MENU_INFO:
                InitMenu(INFO_TEXTID, INFO_NEXTST);
                break;                
            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_MENU_ABOUT:
                ms_iHelpPage = 0;
                break;
                
            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_MENU_HELP:
                ms_iHelpPage = 0;

                ms_iDinamicHelpId = (Main.TOUCHSCREEN_SUPPORTED) ? HELP_TOUCH_TEXTID : HELP_NONTOUCH_TEXTID;
                ms_iHelpNumPages = ms_iDinamicHelpId.length;
                
                String zArray[] = new String[ms_iDinamicHelpId[ms_iHelpPage].length];
                for (int i = 0; i < ms_iDinamicHelpId[ms_iHelpPage].length; i++) {
                    zArray[i] = TxtManager.ms_vText[ms_iDinamicHelpId[ms_iHelpPage][i]];
                }

                FntManager.ms_iDrawRectFontLines = FntManager.SplitString(zArray, Define.SIZEX - Define.SIZEX16, FntManager.FONT_SMALL);
                fraseRepartida = true;
                break;

            // iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
            case Define.ST_MENU_EXIT:
                InitMenu(MENUQUIT_NO_YES_TEXTID, null);
                Menu.ms_iMarkedOption = 1;
                break;
        }
    }
    
    static void Run() {
        switch (Define.ms_iState) {
            
            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_INIT_SPLASH:
                UpdateLogoScreen();
                break;

            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_INIT_LANGUAGE:
                boolean bSelected = false;
                if (Main.GameKeyPressed(Main.KEYINT_SKLEFT, true) || 
                    Main.GameKeyPressed(Main.KEYINT_FIRE, true) || 
                    Main.GameKeyPressed(Main.KEYINT_5, true) || 
                    Main.GameScreenSoftkey(Main.TOUCH_L_SOFTK)) {
                    bSelected = true;
                    
                } else {
                    
                    Menu.Run();
                    if (Menu.ms_iNumOptions == 1 && (Menu.ms_bSubOptChanged || Menu.ms_bSelected)) {
                        Menu.ms_iTitleTxtID = (byte) Menu.ms_iSubOption[0];
                        Menu.ms_bSplitTitle = false;
                    }
                    
                    if (Menu.ms_bSelected) {
                        bSelected = true;
                    }
                }
                if (bSelected) {
                    if (Menu.ms_iNumOptions == 1)
                        TxtManager.ms_iLanguage = ModeMenu.LANGUAGE_TEXTID[Menu.ms_iSubOption[0]][0];
                    else
                        TxtManager.ms_iLanguage = ModeMenu.LANGUAGE_TEXTID[Menu.ms_iMarkedOption][0];

                    TxtManager.LanguageSectionLoad(TxtManager.ms_zLanguageTextFile[TxtManager.ms_iLanguage], 0, TxtManager.ms_vText);
                    RmsManager.RmsLoadSaveData(RmsManager.FILE_SYS, RmsManager.MODE_SAVE);

                    //#if Sound!="false"
                    Main.RequestStateChange(Define.ST_INIT_SOUND);
                    //#else
//#                     Main.RequestStateChange(Main.ST_INIT_SPLASH);
                    //#endif
                }
                break;

            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_INIT_SOUND:
                Menu.Run();
                if (Menu.ms_bSelected) {
                    //#if Sound == "OnOff"
                    SndManager.ms_bSound = (Menu.ms_iMarkedOption == 0);
                    //#else
//#                  if (Menu.ms_iMarkedOption == 0) {
//#                      SndManager.ms_bSound = true;
//#                      SndManager.ms_iSoundVolumeIndex = (Menu.ms_iSubOption[Menu.ms_iMarkedOption] + 1) % 4;
//# 
//#                  } else {
//#                      SndManager.ms_bSound = false;
//#                      SndManager.ms_iSoundVolumeIndex = 0;
//#                  }
                    //#endif
                    Main.RequestStateChange(Define.ST_MENU_MAIN);
                }
                break;

            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_MENU_MAIN:
                Menu.Run();
                if (Menu.ms_bSelected) {
                    if (ms_iDinamicMenuNewSt[Menu.ms_iMarkedOption] != -1) {
                        ms_iLastMainMenuOption = Menu.ms_iMarkedOption;
                        Main.RequestStateChange(ms_iDinamicMenuNewSt[Menu.ms_iMarkedOption]);
                    }
                }
                break;

            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_MENU_OPTIONS:
                if (Main.GameKeyPressed(Main.KEYINT_SKRIGHT, true) ||  
                    Main.GameScreenSoftkey(Main.TOUCH_R_SOFTK)) {
                    SndManager.PlayFX (SndManager.FX_BACK);
                    Main.RequestStateChange(Define.ST_MENU_MAIN);
                } else {
                    Menu.Run();

                    if (Menu.ms_bSelected) {
                        if (ms_iDinamicMenuNewSt[Menu.ms_iMarkedOption] != -1) {    
                            Main.RequestStateChange(ms_iDinamicMenuNewSt[Menu.ms_iMarkedOption]);
                        }

                    } else if (Menu.ms_bSubOptChanged) {
                        int i = 0;
                        // Player changed sound volume
                        if (SndManager.SOUND_SUPPORTED) {
                            if (Menu.ms_iMarkedOption == i) {
                                //#if Sound == "Volume"
//#                                 SndManager.ms_bSound = (Menu.ms_iSubOption[Menu.ms_iMarkedOption] == 0) ? false : true;
//#                                 SndManager.ms_iSoundVolumeIndex = Menu.ms_iSubOption[Menu.ms_iMarkedOption];
                                //#elif Sound == "OnOff"
                                SndManager.ms_bSound = (Menu.ms_iSubOption[Menu.ms_iMarkedOption] == 0);
                                //#endif

                                if (SndManager.ms_bSound) {
                                    SndManager.StopMusic();
                                    SndManager.PlayMusic(SndManager.MUSIC_MENU, true, 0);
                                } else {
                                    SndManager.StopMusic();
                                    //SndManager.DeleteFX();
                                }
                            }
                            i++;
                        }

                        // Vibration
                        if (Main.VIBRATION_SUPPORTED) {
                            if (Menu.ms_iMarkedOption == i) {
                                Main.ms_bVibration = (Menu.ms_iSubOption[i] == 0);
                                if (Main.ms_bVibration)
                                    Main.VibrationStart(150);

                                RmsManager.RmsLoadSaveData(RmsManager.FILE_SYS, RmsManager.MODE_SAVE);
                            }
                            i++;
                        }

                        // Language
                        if (Menu.ms_iMarkedOption == i) {
                            if (Menu.ms_bSubOptRight) {
                                TxtManager.ms_iLanguage = (TxtManager.ms_iLanguage + 1) % TxtManager.ms_vTextLanguageMenu.length;
                                Menu.ms_bSubOptRight = false;
                            } else if (Menu.ms_bSubOptLeft) {
                                TxtManager.ms_iLanguage = (TxtManager.ms_iLanguage > 0) ? TxtManager.ms_iLanguage - 1 : TxtManager.ms_vTextLanguageMenu.length - 1;
                                Menu.ms_bSubOptLeft = false;
                            }
                        }
                        i++;
                    }
                }
                break;
                
            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_MENU_LANGUAGE:
                bSelected = false;
                
                if (Main.GameKeyPressed(Main.KEYINT_SKRIGHT, true) || 
                    Main.GameScreenSoftkey(Main.TOUCH_R_SOFTK)) {
                    SndManager.PlayFX (SndManager.FX_BACK);
                    Main.RequestStateChange(Define.ST_MENU_OPTIONS);
                    
                } else if (Main.GameKeyPressed(Main.KEYINT_SKLEFT, true) || 
                    Main.GameKeyPressed(Main.KEYINT_FIRE, true) || 
                    Main.GameKeyPressed(Main.KEYINT_5, true) || 
                    Main.GameScreenSoftkey(Main.TOUCH_L_SOFTK)) {
                    bSelected = true;
                    
                } else {
                    
                    Menu.Run();
                    
                    if (Menu.ms_iNumOptions == 1 && (Menu.ms_bSubOptChanged || Menu.ms_bSelected)) {
                        Menu.ms_iTitleTxtID = (byte) Menu.ms_iSubOption[0];
                        Menu.ms_bSplitTitle = false;
                    }
                    
                    if (Menu.ms_bSelected)
                        bSelected = true;
                }
                if (bSelected) {
                    if (Menu.ms_iNumOptions == 1)
                        TxtManager.ms_iLanguage = LANGUAGE_TEXTID[Menu.ms_iSubOption[0]][0];
                    else
                        TxtManager.ms_iLanguage = LANGUAGE_TEXTID[Menu.ms_iMarkedOption][0];

                    TxtManager.LanguageSectionLoad(TxtManager.ms_zLanguageTextFile[TxtManager.ms_iLanguage], 0, TxtManager.ms_vText);
                    RmsManager.RmsLoadSaveData(RmsManager.FILE_SYS, RmsManager.MODE_SAVE);

                    Main.RequestStateChange(Define.ST_MENU_OPTIONS);
                }
                break;
                
            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_MENU_ABOUT:
                if (!ms_bCheatMode) {
                    // Cheat activation sequence
                    if (((Main.GameScreenTouched(Define.SIZEX4, 0, Define.SIZEX - Define.SIZEX4, Define.SIZEY4, true, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 0)) || 
                        ((Main.GameScreenTouched(Define.SIZEX4, Define.SIZEY2 + Define.SIZEY4, Define.SIZEX - Define.SIZEX4, Define.SIZEY, true, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 1)) || 
                        ((Main.GameScreenTouched(0, Define.SIZEY4, Define.SIZEX4, Define.SIZEY - Define.SIZEY4, true, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 2)) || 
                        ((Main.GameScreenTouched(Define.SIZEX2 + Define.SIZEX4, Define.SIZEY4, Define.SIZEX, Define.SIZEY - Define.SIZEY4, true, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 3))) {
                        if (ms_iCheatCorrectPresses < CHEAT_KEYSEQUENCE.length) {
                            ms_iCheatCorrectPresses++;
                        }
                    }
                    if (((Main.GameKeyPressed(Main.KEYINT_UP, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 0)) || 
                        ((Main.GameKeyPressed(Main.KEYINT_DOWN, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 1)) || 
                        ((Main.GameKeyPressed(Main.KEYINT_LEFT, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 2)) || 
                        ((Main.GameKeyPressed(Main.KEYINT_RIGHT, false)) && (CHEAT_KEYSEQUENCE[ms_iCheatCorrectPresses] == 3))) {
                        if (ms_iCheatCorrectPresses < CHEAT_KEYSEQUENCE.length) {
                            ms_iCheatCorrectPresses++;
                        }
                    }

                    // Cheat mode ON
                    if (ms_iCheatCorrectPresses == CHEAT_KEYSEQUENCE.length) {
                        ms_bCheatMode = true;
                        
                        if (Define.CHEAT_AVAILABLE) {
                            
                            RmsManager.RmsLoadSaveData(RmsManager.FILE_PLY, RmsManager.MODE_SAVE);
                        }
                    }
                }
                
                if (Main.GameKeyPressed(Main.KEYINT_SKRIGHT, true) ||
                    Main.GameScreenSoftkey(Main.TOUCH_R_SOFTK)) {
                    SndManager.PlayFX (SndManager.FX_BACK);
                    Main.RequestStateChange(Define.ST_MENU_INFO);
                }
                if (ms_bCheatMode) {
                    if (Main.GameKeyPressed(Main.KEYINT_SKLEFT, true) ||
                        Main.GameKeyPressed(Main.KEYINT_FIRE, true) ||
                        Main.GameKeyPressed(Main.KEYINT_5, true) ||
                        Main.GameScreenTouched(0, 0, Define.SIZEX, Define.SIZEY - 
                            (GfxManager.ms_vImage[GfxManager.GFXID_SK_MENU].getHeight()>>1), true, false) ||
                        Main.GameScreenSoftkey(Main.TOUCH_L_SOFTK)) {
                        if (ms_iHelpPage == 0) {
                            ms_iHelpPage++;
                        } else {
                            Main.RequestStateChange(Define.ST_MENU_INFO);
                        }
                    }
                }
                break;

            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_MENU_HELP:
                if (Main.GameKeyPressed(Main.KEYINT_SKRIGHT, true) || 
                    Main.GameScreenSoftkey(Main.TOUCH_R_SOFTK)) {
                    SndManager.PlayFX (SndManager.FX_BACK);
                    Main.RequestStateChange(Define.ST_MENU_INFO);
                }

                if (Main.GameKeyPressed(Main.KEYINT_SKLEFT, true) || 
                    Main.GameKeyPressed(Main.KEYINT_FIRE, true) || 
                    Main.GameKeyPressed(Main.KEYINT_5, true) || 
                    Main.GameScreenSoftkey(Main.TOUCH_L_SOFTK)) {

                    if (ms_iHelpFrame < FntManager.ms_iDrawRectFontLetters) {

                    } else {
                        if ((ms_iHelpPage + 1) < ms_iHelpNumPages) {
                            ms_iHelpPage++;
                            fraseRepartida = false;
                        } else {
                            Main.RequestStateChange(Define.ms_iPrevState);
                        }
                    }
                }
                
                if (Main.GameKeyPressed(Main.KEYINT_LEFT, true) ||
                    Main.GameKeyPressed(Main.KEYINT_4, true)) {
                  
                    if (ms_iHelpPage > 0) {
                        ms_iHelpPage--;
                        fraseRepartida = false;
                    }
                }
                if (Main.GameKeyPressed(Main.KEYINT_RIGHT, true) ||
                    Main.GameKeyPressed(Main.KEYINT_6, true)) {
                 
                    if ((ms_iHelpPage + 1) < ms_iHelpNumPages) {
                        ms_iHelpPage++;
                        fraseRepartida = false;
                    }
                }
                
                if (!fraseRepartida) {
                    String zArray[] = new String[ms_iDinamicHelpId[ms_iHelpPage].length];
                    for (int i = 0; i < ms_iDinamicHelpId[ms_iHelpPage].length; i++) {
                        zArray[i] = TxtManager.ms_vText[ms_iDinamicHelpId[ms_iHelpPage][i]];
                    }
                    
                    FntManager.ms_iDrawRectFontLines = FntManager.SplitString(zArray, Define.SIZEX - Define.SIZEX16, FntManager.FONT_BIG);
                    fraseRepartida = true;
                }
                break;

            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr            
            case Define.ST_MENU_INFO:
                
                if (Main.GameKeyPressed(Main.KEYINT_SKRIGHT, true) || 
                    Main.GameScreenSoftkey(Main.TOUCH_R_SOFTK)) {
                    SndManager.PlayFX (SndManager.FX_BACK);
                    Main.RequestStateChange(Define.ST_MENU_MAIN);
                }
                 Menu.Run();
                if (Menu.ms_bSelected) {
                    if (ms_iDinamicMenuNewSt[Menu.ms_iMarkedOption] != -1) {
                        ms_iLastMainMenuOption = Menu.ms_iMarkedOption;
                        Main.RequestStateChange(ms_iDinamicMenuNewSt[Menu.ms_iMarkedOption]);
                    }
                }
                break;

            // rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr      
            case Define.ST_MENU_EXIT:
                
                Menu.Run();
                if (Menu.ms_bSelected) {
                    if (Menu.ms_iMarkedOption == 0) {
                        Define.ms_bFinishApp = true;
                    } else if (Menu.ms_iMarkedOption == 1) {
                        Main.RequestStateChange(Define.ST_MENU_MAIN);
                    }
                }
                break;
           

            //#if CONFIG_128X128 || CONFIG_128x160 || CONFIG_176X208 || CONFIG_176X220
            //#                 case INTRO:
            //#                 case LEVEL:
            //#                 case WIN:
            //#                 case LOST:
            //#                 case ENDING:
            //#                     if(System.currentTimeMillis() - timeScreen > MS_PER_FRAME * 2)
            //#                         for(byte i = 0; i < lines; i++)
            //#                         {
            //#                             if((i == 0) || ((optionY[i - 1] < SIZEY - (H_SK * 3) - imgLines[0].getHeight())))
            //#                                 optionY[i]--;
            //#
            //#                             timeScreen = System.currentTimeMillis();
            //#
            //#                             if(optionY[lines - 1] < 0)
            //#                                 keyPressed(-5);
            //#                         }
            //#                 break;
            //#endif
        }
    }

    static void Draw(javak.microedition.lcdui.Graphics _g) {
        switch (Define.ms_iState) {
            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_INIT_SPLASH:
                
                _g.drawImage(GfxManager.ms_vImage[GfxManager.GFXID_LOGO],
                Define.SIZEX2, Define.SIZEY2, Graphics.HCENTER | Graphics.VCENTER);                
                break;

            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_INIT_LANGUAGE:
                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                drawMenuBackground(_g);
                Menu.Draw(_g);
                Main.DrawNavigationIcons(_g, Define.SK_OK, Define.SK_NO_ICON);
                break;

            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_INIT_SOUND:
                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                drawMenuBackground(_g);
                Menu.Draw(_g);
                Main.DrawNavigationIcons(_g, Define.SK_OK, Define.SK_NO_ICON);
                break;

            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_MENU_MAIN:
                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                drawMenuBackground(_g);
                Menu.Draw(_g);
                Main.DrawNavigationIcons(_g, Define.SK_OK, Define.SK_NO_ICON);
                break;
            
            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_MENU_OPTIONS:
            case Define.ST_MENU_LANGUAGE:
                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                drawMenuBackground(_g);
                Menu.Draw(_g);
                Main.DrawNavigationIcons(_g, Define.SK_OK, Define.SK_BACK);
                break;
                
            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_MENU_INFO:
                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                drawMenuBackground(_g);
                Menu.Draw(_g);
                Main.DrawNavigationIcons(_g, Define.SK_OK, Define.SK_BACK);
                break;
                
            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_MENU_ABOUT:
                Main.DrawNavigationIcons(_g, Define.SK_NO_ICON, Define.SK_BACK);
                break;
                
            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_MENU_HELP:
                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                drawMenuBackground(_g);

                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);

                Main.DrawNavigationIcons(_g, Define.SK_OK, Define.SK_BACK);
                break;

            // ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd
            case Define.ST_MENU_EXIT:
                _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
                drawMenuBackground(_g);
                Menu.Draw(_g);
                Main.DrawNavigationIcons(_g, Define.SK_OK, Define.SK_NO_ICON);
                break;
        }

        //traza
        //_g.setColor(255,0,0);
        //_g.drawString("KeyCode: "+Main.mst_keyCode, 20, 20, 0);
        //_g.drawString("Action : "+Main.mst_iAction, 10, 140, 0);
        //_g.drawString(""+ms_iKeyInt_Map, 10, 140, 0);
        //_g.drawString(""+mMain.ST_iAction, 10, 160, 0);
        //_g.drawString(""+mMain.ST_keyC, 10, 180, 0);
    }
    
    public static void LoadMenuImages(boolean _bShowLoadingBar) {
        
        GfxManager.ResetGraphics();
        GfxManager.AddGraphic(GfxManager.GFXID_SK_MENU);
                
        switch (Define.ms_iState) {
            
            case Define.ST_INIT_SPLASH:
                GfxManager.AddGraphic(GfxManager.GFXID_LOGO);
                GfxManager.AddGraphic(GfxManager.GFXID_MENU_BUTTONS);
                break;
                
            default:
                GfxManager.AddGraphic(GfxManager.GFXID_MENU_BUTTONS);
                break;
        }
        
        GfxManager.LoadGraphics(_bShowLoadingBar);
    }
    
    public static void HandleInput() {
    }
    
    static void InitMenu(short[][] _iTextID, short[] _iNewSt) {

        int iNumItems = _iTextID.length;
        
        switch (Define.ms_iState) {
            ////////////////////////////////////////////////////////////////////
            case Define.ST_MENU_LANGUAGE:
            case Define.ST_INIT_LANGUAGE:
                
                boolean bLandscapeMenu = iNumItems*GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeightHalf()> Define.SIZEY - Define.SIZEY8;
                iNumItems = TxtManager.ms_vTextLanguageMenu.length;
                
                if (bLandscapeMenu) {
                    ms_iDinamicMenuTxt = new short [1][iNumItems];
                    for (int i=0; i<ms_iDinamicMenuTxt[0].length; i++) {
                        ms_iDinamicMenuTxt[0][i] = (byte) i;
                    }
                } else {
                    ms_iDinamicMenuTxt = new short [iNumItems][1];
                    for (int i=0; i<ms_iDinamicMenuTxt.length; i++) {
                        ms_iDinamicMenuTxt[i][0] = (byte) i;
                    }
                }
                
                iNumItems = ms_iDinamicMenuTxt.length;
                
                Menu.InitTextMenu(
                    TxtManager.ms_vTextLanguageMenu, ms_iDinamicMenuTxt, null, 
                    TxtManager.ms_vTextLanguageWord, (byte) (bLandscapeMenu?TxtManager.ms_iLanguage:-1),
                    Define.SIZEX2, Define.SIZEY2, 0, iNumItems, Graphics.HCENTER | Graphics.VCENTER, 
                    FntManager.FONT_BIG, FntManager.FONT_NORMAL, -1, -1);

                if (bLandscapeMenu)
                    Menu.ms_iSubOption[0] = (byte)TxtManager.ms_iLanguage;
                else
                    Menu.ms_iMarkedOption = TxtManager.ms_iLanguage;
                
                Menu.ms_bAllowFireChange = bLandscapeMenu;
                break;
            
            ////////////////////////////////////////////////////////////////////
            case Define.ST_MENU_OPTIONS:
                // precalcule how many options we need
                iNumItems = 0;
                if (SndManager.SOUND_SUPPORTED)
                    iNumItems++;
                if (Main.VIBRATION_SUPPORTED)
                    iNumItems++;
                if (TxtManager.ms_zLanguageTextFile.length > 1)
                    iNumItems++;
                
                // we create the menu items
                ms_iDinamicMenuTxt = new short[iNumItems][];
                ms_iDinamicMenuNewSt = new short[iNumItems];
                iDinamicMenuTitles = new short[iNumItems];
                iNumItems = 0;

                // sound
                if (SndManager.SOUND_SUPPORTED) {
                    iDinamicMenuTitles[iNumItems] = OPTIONS_TITLES[0];
                    ms_iDinamicMenuTxt[iNumItems] = _iTextID[0];
                    ms_iDinamicMenuNewSt[iNumItems] = _iNewSt[0];
                    iNumItems++;
                }
                // vibration
                if (Main.VIBRATION_SUPPORTED) {
                    iDinamicMenuTitles[iNumItems] = OPTIONS_TITLES[1];
                    ms_iDinamicMenuTxt[iNumItems] = _iTextID[1];
                    ms_iDinamicMenuNewSt[iNumItems] = _iNewSt[1];
                    iNumItems++;
                }
                // language
                if (TxtManager.ms_zLanguageTextFile.length > 1) {
                    iDinamicMenuTitles[iNumItems] = -1;//_iTextID[0][0];
                    ms_iDinamicMenuTxt[iNumItems] = _iTextID[2];
                    ms_iDinamicMenuNewSt[iNumItems] = _iNewSt[2];
                    iNumItems++;
                }

                Menu.InitTextMenu(
                    TxtManager.ms_vText, ms_iDinamicMenuTxt, iDinamicMenuTitles, 
                    TxtManager.ms_vText, MENU_TITLE[Define.ms_iState],
                    Define.SIZEX2, Define.SIZEY2, 0, iNumItems, Graphics.HCENTER | Graphics.VCENTER,
                    FntManager.FONT_BIG, FntManager.FONT_NORMAL, -1,-1);

                Menu.ms_bAllowFireChange = true;

                iOptIndex = 0;
                if (SndManager.SOUND_SUPPORTED) {
                    //#if Sound == "OnOff"
                    Menu.SetSubOpcion(iOptIndex, (SndManager.ms_bSound) ? 0 : 1);
                    //#elif Sound == "Volume"
//#                     Menu.SetSubOpcion (iOptIndex, SndManager.ms_iSoundVolumeIndex);
                    //#endif
                    iOptIndex++;
                }
                
                if (Main.VIBRATION_SUPPORTED) {
                    Menu.SetSubOpcion(iOptIndex, Main.ms_bVibration ? 0 : 1);
                    iOptIndex++;
                }
                break;
                
            ////////////////////////////////////////////////////////////////////
            case Define.ST_MENU_MAIN:
                // precalcule how many options we need
                iNumItems = _iTextID.length;
                if (!SndManager.SOUND_SUPPORTED && 
                    !Main.VIBRATION_SUPPORTED && 
                    !(TxtManager.ms_zLanguageTextFile.length > 1)) {
                    iNumItems--;
                }

                // we create the menu items
                ms_iDinamicMenuTxt = new short[iNumItems][];
                ms_iDinamicMenuNewSt = new short[iNumItems];
                iNumItems = 0;

                // play 
                ms_iDinamicMenuTxt[iNumItems] = _iTextID[0];
                ms_iDinamicMenuNewSt[iNumItems] = Define.ST_GAME_INIT;
                iNumItems++;

                // option
                if (SndManager.SOUND_SUPPORTED || 
                    Main.VIBRATION_SUPPORTED || 
                    (TxtManager.ms_zLanguageTextFile.length > 1)) {
                    ms_iDinamicMenuTxt[iNumItems] = _iTextID[1];
                    ms_iDinamicMenuNewSt[iNumItems] = Define.ST_MENU_OPTIONS;
                    iNumItems++;
                }

                // info
                ms_iDinamicMenuTxt[iNumItems] = _iTextID[2];
                ms_iDinamicMenuNewSt[iNumItems] = Define.ST_MENU_INFO;
                iNumItems++;

                // exit
                ms_iDinamicMenuTxt[iNumItems] = _iTextID[3];
                ms_iDinamicMenuNewSt[iNumItems] = Define.ST_MENU_EXIT;
                iNumItems++;

                Menu.InitTextMenu(
                    TxtManager.ms_vText, ms_iDinamicMenuTxt, null, 
                    TxtManager.ms_vText, MENU_TITLE[Define.ms_iState],
                    Define.SIZEX2, Define.SIZEY2, 0, iNumItems, Graphics.HCENTER | Graphics.VCENTER, 
                    FntManager.FONT_BIG, FntManager.FONT_NORMAL, -1,-1);
                
                Menu.ms_bAllowFireChange = true;
                break;
  
            ////////////////////////////////////////////////////////////////////
            case Define.ST_INIT_SOUND:
                 ms_iDinamicMenuTxt = _iTextID;
                if (_iNewSt != null)
                    ms_iDinamicMenuNewSt = _iNewSt;

                Menu.InitTextMenu(
                        TxtManager.ms_vText, ms_iDinamicMenuTxt, null, 
                        TxtManager.ms_vText, MENU_TITLE[Define.ms_iState],
                        Define.SIZEX2, Define.SIZEY2, 0, iNumItems, Graphics.HCENTER | Graphics.VCENTER,
                        FntManager.FONT_BIG, FntManager.FONT_NORMAL, -1, Define.SIZEY4);
                
                Menu.ms_bAllowFireChange = true;
                break;
                
            default:
                ms_iDinamicMenuTxt = _iTextID;
                if (_iNewSt != null)
                    ms_iDinamicMenuNewSt = _iNewSt;

                Menu.InitTextMenu(
                        TxtManager.ms_vText, ms_iDinamicMenuTxt, null, 
                        TxtManager.ms_vText, MENU_TITLE[Define.ms_iState],
                        Define.SIZEX2, Define.SIZEY2, 0, iNumItems, Graphics.HCENTER | Graphics.VCENTER,
                        FntManager.FONT_BIG, FntManager.FONT_NORMAL, -1, -1);
                
                Menu.ms_bAllowFireChange = true;
                break;
        }
    }
    
    private static void UpdateLogoScreen() {
        
        long lTime = System.currentTimeMillis() - splashTimeInit;

        if (lTime > splashTimeDuration) {
            
            RmsManager.RmsLoadSaveData(RmsManager.FILE_SYS, RmsManager.MODE_LOAD);
            TxtManager.LanguageLoadConfiguration();

            if (TxtManager.ms_iLanguage == -1) {

                WolfRunner.checkLanguage();
                
                //#if d500 || kg800 || z140v
//#                 TxtManager.ms_iLanguage = 0;
//#                 Main.RequestStateChange(Main.ST_INIT_LANGUAGE);
//#                             
                //#else
                if ((WolfRunner.ms_zLanguage != null) && !(WolfRunner.ms_zLanguage.equals(""))) {

                    TxtManager.ms_iLanguage = -1;
                    // first search for country specific language files
                    for (int j = 0; j < TxtManager.ms_zLanguageTextFile.length; j++) {
                        if (("/" + WolfRunner.ms_zLanguage + "-" + WolfRunner.ms_zCountry + ".lng").equals(TxtManager.ms_zLanguageTextFile[j])) {
                            TxtManager.ms_iLanguage = j;
                            break;
                        }
                    }
                    // if not found, serach for language file
                    if (TxtManager.ms_iLanguage == -1) {
                        for (int j = 0; j < TxtManager.ms_zLanguageTextFile.length; j++) {
                            if (("/" + WolfRunner.ms_zLanguage + ".lng").equals(TxtManager.ms_zLanguageTextFile[j])) {
                                TxtManager.ms_iLanguage = j;
                                break;
                            }
                        }
                    }
                    // not found, set first language as defauls (usually english)
                    if (TxtManager.ms_iLanguage == -1)
                        Main.RequestStateChange(Define.ST_INIT_LANGUAGE);

                } else {
                    Main.RequestStateChange(Define.ST_INIT_LANGUAGE);
                }
                //#endif
                RmsManager.RmsLoadSaveData(RmsManager.FILE_SYS, RmsManager.MODE_SAVE);
                
            } else {
                TxtManager.LanguageSectionLoad(TxtManager.ms_zLanguageTextFile[TxtManager.ms_iLanguage], 0, TxtManager.ms_vText);
                Main.RequestStateChange((SndManager.SOUND_SUPPORTED)?Define.ST_INIT_SOUND:Define.ST_INIT_SPLASH);
                
            }
        }
    }
 
    private static void drawMenuBackground(Graphics g) {
        g.setColor(0xffffffff);
        g.fillRect(0, 0, Define.SIZEX, Define.SIZEY);
    }
}
