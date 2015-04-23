package com.kitmaker.wolfRunner;

import com.kitmaker.manager.GfxManager;
import com.kitmaker.manager.FntManager;
import com.kitmaker.manager.SndManager;
import javak.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

////////////////////////////////////////////////
//         ## GESTION DE MENUS ##             //
////////////////////////////////////////////////
public class Menu {
    static final int FLASHING_TIME = 500;
    static int ms_iPosX, ms_iPosY;
    static short ms_iExtraX[];
    static byte[] ms_iSubOption;
    static int ms_iNumOptions;
    static int ms_iMarkedOption;
    static boolean ms_bSelected;
    public static int ms_iGlobalFrame = 0;
    public static int ms_iMenuFrame = 0;
    static int ms_iTextPosX, ms_iTextPosY, ms_iTextAnchor;
    static int ms_iFontID;
    static int ms_iFontHighlightID;
    static boolean ms_bAllowFireChange;  // In multi-choice options, Fire changes choice instead of selecting option   
    static boolean ms_bOptChanged, ms_bSubOptChanged;
    static boolean ms_bSubOptLeft, ms_bSubOptRight;
    static int ms_iFrameBotton;
    
    // Vertical Menu
    static short[][] ms_iOptionTextID;
    static short[] ms_iHeaderTextID;
    public static int ms_iOptionWidth;
    public static int ms_iOptionHeight;
    public static int ms_iHeaderHeight;
    public static int ms_iMenuHeight;
    static int ms_iMaxVisibleItems;
    static int ms_iFirstVisibleItem;
    static short ms_iTitleTxtID;
    static boolean ms_bSplitTitle;
    static String ms_vText[];
    static String ms_vTitle[];
    static int _iFontID;
    static int _iFontHighlightID;
    
    //touch screen
    static boolean ms_bTouched;
    static boolean ms_bReleased;
    static int ms_iTouchedOption;
    static int ms_iReleasedOption;
    static int ms_iTouchedFrame;
    
    static int ms_iStateAnimation;
    static final int NONE = -1;
    static final int FOCUS = 0;
    static final int STARTING = 1;
    static final int MOVING = 2;
    static final int DRAWING = 3;
    
    static int ms_iFrameLag;//Animation flag.
    static int ms_iPointsModYFlag;
    
    static int ms_iPointsModYBall;
    static int ms_iFinalPointsModYBall;
    static int ms_iStateBall;
    static int ms_iSpeedBall;
    //Balls states 
   

    // Initialize vertical menu
    public static void InitTextMenu(
            String[] _zOptionTextArray,
            short[][] _iOptionTextID, 
            short[] _iHeaderTextID,
            String[] _zTitleTextArray, 
            short _iTitleTextID,
            short _iPosX,
            short _iPosY,
            int _iMarkedItem,
            int _iNumItems,
            int _iTextAnchor,
            int _iFontID,
            int _iFontHighlightID,
            int _iOptionWidth,
            int _iOptionHeight
            ) {
        int i;

        ms_vText = _zOptionTextArray;
        ms_vTitle = _zTitleTextArray;

        ms_iOptionTextID = _iOptionTextID;
        ms_iHeaderTextID = _iHeaderTextID;
        ms_iTitleTxtID = _iTitleTextID;

        // if text doesn't exist, we set ms_iTitleTxtID to -1 to avoid NullPointerException
        if (ms_iTitleTxtID != -1 && (ms_vTitle == null || ms_vTitle[ms_iTitleTxtID] == null))
            ms_iTitleTxtID = -1;

        ms_iPosX = _iPosX;
        ms_iNumOptions = _iNumItems;
        
        if (_iOptionWidth > 0)
            ms_iOptionWidth = _iOptionWidth;
        else
            ms_iOptionWidth = Define.SIZEX-(Define.SIZEX4+Define.SIZEX8);

        if (_iOptionHeight > 0) {
            ms_iOptionHeight = _iOptionHeight;
        } else {
//            ms_iOptionHeight = ms_iOptionHeight = ((GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>1) 
//                + (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>2));
        }
        
        ms_iMenuHeight = (ms_iNumOptions * ms_iOptionHeight);
        ms_iPosY = _iPosY + ((ms_iOptionHeight >> 1) + ((ms_iTitleTxtID == -1) ? 0 : (ms_iOptionHeight >> 1)) - (ms_iMenuHeight >> 1));
        
        if (ms_iPosY - (ms_iOptionHeight >> 1) < 0) {
            ms_iOptionHeight = (Define.SIZEY / (ms_iNumOptions + 1));
            ms_iMenuHeight = (ms_iNumOptions * ms_iOptionHeight);
            ms_iPosY = _iPosY + ((ms_iOptionHeight >> 1) + ((ms_iTitleTxtID == -1) ? 0 : (ms_iOptionHeight >> 1)) - (ms_iMenuHeight >> 1));
        }
        
        
        ms_iMarkedOption = _iMarkedItem;
        ms_iMaxVisibleItems = ms_iNumOptions;
        ms_iHeaderHeight = Math.min(ms_iOptionHeight >> 1, FntManager.FNT_MAXHEIGHT[_iFontID]);


        ms_iFirstVisibleItem = 0;
        ms_bSelected = false;
        ms_bOptChanged = false;
        ms_bSubOptChanged = false;

        ms_iTextAnchor = _iTextAnchor;
        ms_iFontID = _iFontID;
        ms_iFontHighlightID = _iFontHighlightID;

        ms_iSubOption = new byte[ms_iNumOptions];

        for (i = 0; i < ms_iNumOptions; i++) {
            if (_iOptionTextID[i] != null) {
                ms_iSubOption[i] = 0;
            }
        }

        ms_iExtraX = new short[ms_iNumOptions];
        for (i = 0; i < ms_iNumOptions; i++) {
            ms_iExtraX[i] = (short) (Define.SIZEX + (i * Define.SIZEX));
        }
    }

    public static void Run() {

        ms_bOptChanged = false;
        ms_bSubOptChanged = false;

        // manage touch screen
        if (ms_bTouched) {
            ms_iTouchedFrame++;
        }
        
        for (byte i = 0; i < ms_iNumOptions; i++) {
            if ((Main.GameScreenTouched(
                Define.SIZEX2 - (ms_iOptionWidth>>1),
                ms_iPosY + (i * ms_iOptionHeight) - (ms_iOptionHeight >> 1),
                Define.SIZEX2 + (ms_iOptionWidth>>1), 
                ms_iPosY + (i * ms_iOptionHeight) + (ms_iOptionHeight >> 1), false, true)) && 
                (Menu.ms_iMarkedOption < Menu.ms_iNumOptions)) {

                if (!ms_bTouched) {
                    ms_iTouchedOption = i;
                    ms_iReleasedOption = i;
                    ms_bTouched = true;
                }
                if (ms_iMarkedOption != i) {
                    ms_iMarkedOption = i;
                    ms_iReleasedOption = i;
                    ms_bOptChanged = true;
                }
                break;
            }
        }
        if (!Main.GameScreenTouched(false, true) && ms_bTouched) {
            if ((ms_iTouchedOption == ms_iReleasedOption)) {
                if (!ms_bAllowFireChange || ms_iOptionTextID[ms_iMarkedOption].length == 1) {
                    ms_bSelected = true;
                    
                } else {//Menu horizontal
                    ms_iSubOption[ms_iMarkedOption] = (byte) ((ms_iOptionTextID[ms_iMarkedOption].length + ms_iSubOption[ms_iMarkedOption] + 1) % ms_iOptionTextID[ms_iMarkedOption].length);
                    ms_bSubOptChanged = true;
                }
            }
            ms_bTouched = false;
            ms_iTouchedFrame = 0;
        }

        // Manage key input //////////////////////////////////////////////////////
        if (!ms_bSelected) {

            // Option change (left/up)
            if (Main.GameKeyPressed(Main.KEYINT_UP, true) || 
                Main.GameKeyPressed(Main.KEYINT_2, true)){
               
                ms_iMarkedOption = (byte) ((ms_iNumOptions + ms_iMarkedOption - 1) % ms_iNumOptions);
                if (ms_iMaxVisibleItems < ms_iNumOptions) {
                    if (ms_iMarkedOption == (ms_iNumOptions - 1)) {
                        ms_iFirstVisibleItem = ms_iNumOptions - ms_iMaxVisibleItems;
                    } else if (((ms_iMarkedOption - ms_iFirstVisibleItem) <= 0)
                            && (ms_iFirstVisibleItem > 0)) {
                        ms_iFirstVisibleItem--;
                    }
                }
                if(ms_iNumOptions > 1)
                    ms_bOptChanged = true;

            // Option change (right/down)
            } else if (Main.GameKeyPressed(Main.KEYINT_DOWN, true) || 
                Main.GameKeyPressed(Main.KEYINT_8, true)){
              
                ms_iMarkedOption = (byte) ((ms_iNumOptions + ms_iMarkedOption + 1) % ms_iNumOptions);
                if (ms_iMaxVisibleItems < ms_iNumOptions) {
                    if (ms_iMarkedOption == 0) {
                        ms_iFirstVisibleItem = 0;
                    } else if (((ms_iMarkedOption - ms_iFirstVisibleItem) >= (ms_iMaxVisibleItems - 1))
                            && (ms_iFirstVisibleItem < (ms_iNumOptions - ms_iMaxVisibleItems))) {
                        ms_iFirstVisibleItem++;
                    }
                }
                if (ms_iNumOptions > 1)
                        ms_bOptChanged = true;
                

            // Suboption change
            } else if (ms_iOptionTextID[ms_iMarkedOption].length > 1) {
                if (Main.GameKeyPressed(Main.KEYINT_LEFT, true) || 
                    Main.GameKeyPressed(Main.KEYINT_4, true)) {
                   
                    ms_iSubOption[ms_iMarkedOption] = (byte) ((ms_iOptionTextID[ms_iMarkedOption].length 
                            + ms_iSubOption[ms_iMarkedOption] - 1) % ms_iOptionTextID[ms_iMarkedOption].length);
                    ms_bSubOptChanged = true;
                    ms_bSubOptLeft = true;
                    ms_iStateAnimation = NONE;
                    
                } else if (
                    Main.GameKeyPressed(Main.KEYINT_RIGHT, true) || 
                    Main.GameKeyPressed(Main.KEYINT_6, true)) {
                   
                    ms_iSubOption[ms_iMarkedOption] = (byte) ((ms_iOptionTextID[ms_iMarkedOption].length 
                            + ms_iSubOption[ms_iMarkedOption] + 1) % ms_iOptionTextID[ms_iMarkedOption].length);
                    ms_bSubOptChanged = true;
                    ms_bSubOptRight = true;
                    ms_iStateAnimation = NONE;
                }
            }
            
            // fire
            if (Main.GameKeyPressed(Main.KEYINT_SKLEFT, true) || 
                Main.GameKeyPressed(Main.KEYINT_FIRE, true) || 
                Main.GameKeyPressed(Main.KEYINT_5, true) || 
                Main.GameScreenSoftkey(Main.TOUCH_L_SOFTK)) {
                
                if (Define.ms_iState != Define.ST_INIT_SOUND){
                    if (!ms_bAllowFireChange || ms_iOptionTextID[ms_iMarkedOption].length == 1) {
                        ms_bSelected = true;
                    } else {
                        ms_iSubOption[ms_iMarkedOption] = (byte) ((ms_iOptionTextID[ms_iMarkedOption].length + ms_iSubOption[ms_iMarkedOption] + 1) % ms_iOptionTextID[ms_iMarkedOption].length);
                        ms_bSubOptChanged = true;
                    }
                } else {
                    ms_bSelected = true;
                }
            }
        }

        if (ms_bOptChanged || ms_bSubOptChanged) {
            SndManager.PlayFX(SndManager.FX_SELECT);
            ms_iMenuFrame = 0;
        }
        
        if (ms_bSelected)
            SndManager.PlayFX(SndManager.FX_SHOOT);
            

        for (byte i = 0; i < ms_iExtraX.length; i++) {

            // position x run
            if (ms_iExtraX[i] > 0) {
                ms_iExtraX[i] -= Math.min(Define.SIZEX4, (ms_iExtraX[i] / 3) + 1);
                if (Define.FPS == 10) {
                    ms_iExtraX[i] -= Math.min(Define.SIZEX4, (ms_iExtraX[i] / 3) + 1);
                    ms_iExtraX[i] -= Math.min(Define.SIZEX4, (ms_iExtraX[i] / 3) + 1);
                } else if (Define.FPS == 15) {
                    ms_iExtraX[i] -= Math.min(Define.SIZEX4, (ms_iExtraX[i] / 3) + 1);
                } else if (Define.FPS == 20) {
                    ms_iExtraX[i] -= Math.min(Define.SIZEX4, (ms_iExtraX[i] / 3) + 1) >> 1;
                }
            }
        }
        
        ms_iFrameLag = ms_iFrameLag == 0 ? 1 : 0;

        if (ms_bOptChanged) {
            ms_iStateAnimation = STARTING;
            ms_iFrameBotton = 0;
        }
        
        // Run ///////////////////////////////////////////////////////////////////
        ms_iMenuFrame++;
        ms_iGlobalFrame++;
    }
    
    public static void SetSubOpcion(int _iOpcion, int _iSubOpcion) {
        ms_iSubOption[_iOpcion] = (byte) _iSubOpcion;
    }

    public static void Draw(Graphics _g) {
        int i;
        int iPosX, iPosY, iCursorY = 0;
        int iOptionTextLength;

        // title
        if (ms_iTitleTxtID != -1) {
            int iAddH = FntManager.FNT_HEIGHT[FntManager.FONT_BIG] * 2;
            int iAddY = ms_iPosY - ms_iOptionHeight - ((FntManager.FNT_HEIGHT[FntManager.FONT_BIG] + iAddH)>>1);

            _g.setColor(ModeMenu.COLOR_BROWN_DARK);
            _g.fillRect(0, iAddY, Define.SIZEX, iAddH);

            _g.setColor(ModeMenu.COLOR_MARC);
            _g.fillRect(0, iAddY, Define.SIZEX, Define.SIZEY16>>3);
            _g.fillRect(0, iAddY + iAddH - (Define.SIZEY16>>3), Define.SIZEX, Define.SIZEY16>>3);
                
            FntManager.DrawFont(_g, FntManager.FONT_BIG, ms_vTitle[ms_iTitleTxtID], 
                Define.SIZEX2, iAddY + (iAddH>>1), Graphics.VCENTER | Graphics.HCENTER, -1);
        }
        
        // menu txts
        iPosY = ms_iPosY;
        
        boolean bSelected;
        int iColor = FntManager.getSystemColor(ms_iFontID);
        
        for (i = 0; i < ms_iNumOptions; i++) {
            bSelected = (i == Menu.ms_iMarkedOption);
            
            //iPosX = ms_iPosX + ((i % 2 == 0) ? ms_iExtraX[i] : (-ms_iExtraX[i]));
            iPosX = ms_iPosX + (-ms_iExtraX[i]);
            
            // Botton
            _g.setClip(0, iPosY - (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>2), 
                    Define.SIZEX, (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>1));
            _g.drawImage(GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS], iPosX,
                    bSelected?
                    iPosY - (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>2):
                    iPosY + (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>2), 
                    Graphics.VCENTER | Graphics.HCENTER);
            
            // draw text
            if (!bSelected)
                FntManager.setSystemColor(ms_iFontID, 0xfffff600);
            
            if (ms_iHeaderTextID != null && ms_iHeaderTextID[i] != -1) {
                FntManager.DrawFont(_g, bSelected?ms_iFontHighlightID:ms_iFontID,
                        ms_vText[ms_iHeaderTextID[i]] + " " + ms_vText[ms_iOptionTextID[i][ms_iSubOption[i]]],
                        iPosX, iPosY, ms_iTextAnchor, -1);
            } else {
                FntManager.DrawFont(_g, bSelected?ms_iFontHighlightID:ms_iFontID, 
                        ms_vText[ms_iOptionTextID[i][ms_iSubOption[i]]], 
                        iPosX, iPosY, ms_iTextAnchor, -1);
            }
            FntManager.setSystemColor(ms_iFontID, iColor);

            if (bSelected)
                iCursorY = iPosY;
            
            // draw left/right arrows when over a multi-suboption option
            if (ms_iOptionTextID[i].length > 1) {

                iOptionTextLength = Define.SIZEX-(Define.SIZEX4+Define.SIZEX8);
                
                 _g.setColor(0x003a02f0);
                 
                 if (i == ms_iMarkedOption) {
                    _g.fillTriangle(
                        iPosX - (iOptionTextLength >> 1) - Define.SIZEX24 - Define.SIZEX32, iPosY,
                        iPosX - (iOptionTextLength >> 1) - Define.SIZEX24, iPosY - Define.SIZEX32,
                        iPosX - (iOptionTextLength >> 1) - Define.SIZEX24, iPosY + Define.SIZEX32);

                    _g.fillTriangle(
                        iPosX + (iOptionTextLength >> 1) + Define.SIZEX24, iPosY - Define.SIZEX32,
                        iPosX + (iOptionTextLength >> 1) + Define.SIZEX24 + Define.SIZEX32, iPosY,
                        iPosX + (iOptionTextLength >> 1) + Define.SIZEX24, iPosY + Define.SIZEX32);
                }
            } 
            iPosY += ms_iOptionHeight;
        }
        
        _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);

        //ModeMenu.DrawCursor(_g, iCursorY);
    }
    
    private static StringBuffer ms_sReplaceText;
    public static void DrawInDown(Graphics _g) {
        int i;
        int iPosX, iPosY, iCursorY = 0;
        int iOptionTextLength;

        // title
        if (ms_iTitleTxtID != -1) {
            if (!ms_bSplitTitle) {
                FntManager.SplitString(new String [] {ms_vTitle[ms_iTitleTxtID]}, 
                    Define.SIZEX - Define.SIZEX8, FntManager.FONT_BIG);
                ms_bSplitTitle = true;
            }

            int iAddH = FntManager.GetHeight(FntManager.FONT_SMALL)*(FntManager.ms_iDrawRectFontLines+1);
            int iAddY = ms_iPosY - ms_iOptionHeight - ((FntManager.FNT_HEIGHT[FntManager.FONT_BIG] + iAddH)>>1);

            _g.setColor(ModeMenu.COLOR_BROWN_DARK);
            _g.fillRect(0, iAddY, Define.SIZEX, iAddH);

            _g.setColor(ModeMenu.COLOR_MARC);
            _g.fillRect(0, iAddY, Define.SIZEX, Define.SIZEY16>>3);
            _g.fillRect(0, iAddY + iAddH - (Define.SIZEY16>>3), Define.SIZEX, Define.SIZEY16>>3);
                
            //ms_sReplaceText = new String[]{ms_vTitle[ms_iTitleTxtID]};
            ms_sReplaceText = new StringBuffer(ms_vTitle[ms_iTitleTxtID].length());
            for (int sb = 0; sb < ms_vTitle[ms_iTitleTxtID].length();sb++){
                ms_sReplaceText.append(ms_vTitle[ms_iTitleTxtID].charAt(sb));
            }
            
            FntManager.DrawFontInRectangle(_g, FntManager.FONT_SMALL, new String[]{ms_sReplaceText.toString()},
                Define.SIZEX2, 
                iAddY + (iAddH>>1), 
                Define.SIZEX - Define.SIZEX64,
                //#if SIZE == "Small"
//#                 (FntManager.GetHeight(FntManager.FONT_SMALL)*3)/4, 
                //#else
                FntManager.GetHeight(FntManager.FONT_SMALL), 
                //#endif
                FntManager.TOP | FntManager.HCENTER, Graphics.VCENTER, -1, true);
        }
        
        // menu txts
        iPosY = ms_iPosY;// + Define.SIZEX>Define.BASE_SIZEY?Define.SIZEX24:Define.SIZEY24;
        
        for (i = 0; i < ms_iNumOptions; i++) {
            //iPosX = ms_iPosX + ((i % 2 == 0) ? ms_iExtraX[i] : (-ms_iExtraX[i]));
            iPosX = ms_iPosX + (-ms_iExtraX[i]);
            
            // Botton
            _g.setClip(0, iPosY - (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>2), 
                    Define.SIZEX, (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>1));
            
            _g.drawImage(GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS],iPosX,
                    ms_iMarkedOption == i?
                    iPosY - (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>2):
                    iPosY + (GfxManager.ms_vImage[GfxManager.GFXID_MENU_BUTTONS].getHeight()>>2), 
                    Graphics.VCENTER | Graphics.HCENTER);
            
            // draw text
            int color = FntManager.getSystemColor(FntManager.FONT_BIG);
            FntManager.setSystemColor(FntManager.FONT_BIG, 0xfffff600);
            
            if (ms_iHeaderTextID != null && ms_iHeaderTextID[i] != -1) {
                FntManager.DrawFont(_g, (i == Menu.ms_iMarkedOption)?ms_iFontHighlightID:ms_iFontID,
                        ms_vText[ms_iHeaderTextID[i]] + " " + ms_vText[ms_iOptionTextID[i][ms_iSubOption[i]]],
                        iPosX, iPosY, ms_iTextAnchor, -1);
            } else {
                FntManager.DrawFont(_g, (i == Menu.ms_iMarkedOption)?ms_iFontHighlightID:ms_iFontID, 
                        ms_vText[ms_iOptionTextID[i][ms_iSubOption[i]]], 
                        iPosX, iPosY, ms_iTextAnchor, -1);
            }
            
            FntManager.setSystemColor(FntManager.FONT_BIG, color);

            if (i == Menu.ms_iMarkedOption)
                iCursorY = iPosY;
            
            // draw left/right arrows when over a multi-suboption option
            if (ms_iOptionTextID[i].length > 1) {

                iOptionTextLength = Define.SIZEX-(Define.SIZEX4+Define.SIZEX8);
                
                _g.setColor(0x003a02f0);
                
                if (i == ms_iMarkedOption) {
                    _g.fillTriangle(
                        iPosX - (iOptionTextLength >> 1) - Define.SIZEX24 - Define.SIZEX32, iPosY,
                        iPosX - (iOptionTextLength >> 1) - Define.SIZEX24, iPosY - Define.SIZEX32,
                        iPosX - (iOptionTextLength >> 1) - Define.SIZEX24, iPosY + Define.SIZEX32);

                    _g.fillTriangle(
                        iPosX + (iOptionTextLength >> 1) + Define.SIZEX24, iPosY - Define.SIZEX32,
                        iPosX + (iOptionTextLength >> 1) + Define.SIZEX24 + Define.SIZEX32, iPosY,
                        iPosX + (iOptionTextLength >> 1) + Define.SIZEX24, iPosY + Define.SIZEX32);
                }
            } 
            iPosY += ms_iOptionHeight;
        }
        
        _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
        //ModeMenu.DrawCursor(_g, iCursorY);
    }
}
