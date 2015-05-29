package com.kitmaker.wolfRunner;

//#if API=="Nokia"
//#define CanvasMode = "FullCanvas"
//#elif a177
//#define CanvasMode = "GameCanvas"
//#else
//#define CanvasMode = "Canvas"
//#endif

//#if (bb9800 || bb9810 || bb9850) && MIDP
//#define BackBuffer = "prohibited"
//#endif

import com.kitmaker.manager.GfxManager;
import com.kitmaker.manager.FntManager;
import com.kitmaker.manager.RmsManager;
import com.kitmaker.manager.SndManager;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;
import javak.microedition.lcdui.Graphics;
import javak.microedition.lcdui.Font;

//#if MIDP && API == "BlackBerry"
//# import net.rim.device.api.system.TrackwheelListener;
//# import net.rim.device.api.system.KeyListener;
//# import net.rim.device.api.system.Application;
//# import net.rim.device.api.system.Characters;
//# import net.rim.device.api.ui.Keypad;
//# import net.rim.device.api.ui.Trackball;
//#endif

//#if CanvasMode == "FullCanvas"
//# import com.nokia.mid.ui.FullCanvas;
//# import com.nokia.mid.ui.DeviceControl;
//#if s40asha311
//# import com.nokia.mid.ui.*;
//# import com.nokia.mid.ui.multipointtouch.MultipointTouch;
//# import com.nokia.mid.ui.multipointtouch.MultipointTouchListener;
//#endif
//#elifdef MIDP
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.game.GameCanvas;
//#elif RIM
//# import net.rim.device.api.ui.container.MainScreen;
//# import net.rim.device.api.ui.Keypad;
//#if RIM >= "5.0"
//# import net.rim.device.api.ui.TouchEvent;
//# import net.rim.device.api.ui.component.EditField;
//#endif
//#elif DOJA
//# import com.nttdocomo.ui.Canvas;
//# import com.nttdocomo.ui.Display;
//#endif

/*
 * @author Toni Nicolau
 */

//#if RIM
//# public class Main extends MainScreen implements Runnable {
//#elif CanvasMode == "FullCanvas"
//# public class Main extends FullCanvas implements Runnable
//#if s40asha311
//# , MultipointTouchListener 
//#endif
//# {
//#else

//#if CanvasMode == "GameCanvas"
//# public class Main extends GameCanvas implements Runnable 
//#elif CanvasMode == "Canvas"
public class Main extends Canvas implements Runnable 
//#endif

    //#if API =="BlackBerry"
//#     , KeyListener, TrackwheelListener
    //#endif
{
//#endif
    //#if s40asha311 || RIM >= "5.0"
//#     public static final int MAX_POINTER_SUPORTED = 2;
    //#endif
    
    //#if BackBuffer != "false" && BackBuffer != "prohibited"
    public static javax.microedition.lcdui.Image ms_vCanvas;
    public static javax.microedition.lcdui.Graphics ms_vCanvasGraphics;
    //#endif
    
    public static Main ms_vMain;
    public static WolfRunner ms_vMIDlet;
    public static Class ms_vClass;
    
    //delta time
    public long thisLoop;
    public static long deltaTime;
    public static final float SECOND = 1000;

   

    public Main(WolfRunner midlet) {
        
        //#if CanvasMode == "GameCanvas"
//#         super(false);
        //#endif
        
        //ms_vMain = this;
        ms_vMIDlet = midlet;
        ms_vClass = getClass();
        
        //#if MIDP && API == "BlackBerry"
//#         Application.getApplication().addKeyListener(this);
//#         Application.getApplication().addTrackwheelListener(this);
        //#endif

        TouchScreenCheck();
        GfxManager.InitSpriteManager();

        FntManager.LoadFont(FntManager.FONT_INACTIVE);
        FntManager.LoadFont(FntManager.FONT_ACTIVE);
        FntManager.LoadFont(FntManager.FONT_INGAME);
        FntManager.LoadFont(FntManager.FONT_TITLE);


        Debug.DebugReadPostmortemData();
        RmsManager.RmsLoadSaveData(RmsManager.FILE_SYS, RmsManager.MODE_LOAD);
        
        SndManager.LoadAllFX();

        System.gc();
        
        //////////////////////////////////////////////////////////////////////////
        // Set full screen mode
        //////////////////////////////////////////////////////////////////////////
        //#if MIDP=="2.0"
        setFullScreenMode (true);
        //setTitle(null);
        //#endif

        //////////////////////////////////////////////////////////////////////////
        // Create static graphics
        //////////////////////////////////////////////////////////////////////////
        //#if BackBuffer != "false" && BackBuffer != "prohibited"
        try {
            ms_vCanvas = javax.microedition.lcdui.Image.createImage(Define.SIZEX, Define.SIZEY);
            ms_vCanvasGraphics = ms_vCanvas.getGraphics();
            ms_XGraphics.setGraphics(ms_vCanvasGraphics);
        } catch (Exception Ex) {
        }
        //#endif
        
         //#if s40asha311
//#         MultipointTouch.getInstance().addMultipointTouchListener(this);
        //#endif
        
        //ScoresConfigurationLoad();
    }

    public static String ms_zFamily, ms_zGameCode, ms_zBinMode;
    public static void ScoresConfigurationLoad () {

        DataInputStream vInputS = null;

        try {
            vInputS = new DataInputStream(WolfRunner.ms_vInstance.getClass().getResourceAsStream("/scr.dat"));
            if (vInputS == null) {
                System.out.println("***************************************************************************************");
                System.out.println("WARNNING: scr.dat was not found in your project. Are you sure you have your build-impl.xml configured properly?");
                System.out.println("***************************************************************************************");
            }
            
            // we don't use DataInputStream.available since has been reported buggy in some old devices
            int iNumLetters = vInputS.readByte() - 48; // we substract the char value for 0
            if (iNumLetters < 4) {
                iNumLetters = (iNumLetters*10) + (vInputS.readByte() - 48);
            }

            // Read String bytes
            byte[] bChar = new byte[iNumLetters];
            for (int i=0; i<iNumLetters; i++) 
                bChar[i] = vInputS.readByte();

            // Byte array to Strings
            String zText = new String(bChar, 0, iNumLetters, "UTF-8");
            
            // Get data
            int iSeparator = zText.indexOf("_");
            ms_zFamily = zText.substring(0, iSeparator);
            zText = zText.substring(iSeparator + 1, zText.length());
            ms_zGameCode = zText.substring(0, iSeparator);
            iSeparator = zText.indexOf("_");
            ms_zBinMode = zText.substring(iSeparator + 1, zText.length());
            
            if (ms_zGameCode.equals("test_001")) {
                System.out.println("***************************************************************************************");
                System.out.println("WARNNING: You didn't configure the gameCode field in the propierties/atributtes.");
                System.out.println("Please, edit it if you are going to use the scores library, otherwise ignore this message");
                System.out.println("***************************************************************************************");
            }
            
            vInputS.close();

        } catch (Exception e) {
            System.out.println("***************************************************************************************");
            System.out.println("ERROR: Something went wrong in the read of scr.dat. Call Fran before your computer explodes!");
            System.out.println("***************************************************************************************");
            e.printStackTrace();
            
            if (vInputS != null) {
                try {
                    vInputS.close();
                } catch (IOException ex) {
                }
            }
        }
        
        System.gc();
    }
    
    public static boolean bTouchFire = false;
    
    //LOADING
    public final void run() {

        ms_vMain = this;
        ms_XGraphics.m_bGetGraphics = false;
        
        VibrationCheck(); // WARNING! vibration check must to be settled here, otherwise won't work properly

        RequestStateChange(Define.ST_INIT_SPLASH);
        while (!Define.ms_bFinishApp) {
            if (!Define.ms_bPaused) {
                //DELTATIME
                putDelta();
                
                // RUN
                Update();

                // DRAW
                Repaints();

            }
           try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        WolfRunner.ms_vInstance.quitApp();
    }

    static void Update() {
        try {
            // Synchronous state change
            if (Define.ms_bNewState || (Define.ms_iState != Define.ms_iNewState)) {
                InitState(Define.ms_iNewState);
            }

            // update
            if (Define.ms_iState < Define.ST_GAME_INIT)
                ModeMenu.Run();
            else
                ModeGame.Run();
            // Update music state
            SndManager.UpdateMusic();

        } catch (Exception ex) {
            //#if Debug
//#             //Debug.DebugMsg ("RUN: St. "+ Main.ms_iState + " - Exception:" + ex.getMessage () + "("+ex.toString () + ")" );
//#             ex.printStackTrace();
            //#endif
        }

        //#if s390g
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_UP)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_DOWN)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_LEFT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_RIGHT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
        //#endif

        //#if API=="BlackBerry" && MIDP
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_UP)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_DOWN)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_LEFT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_RIGHT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
        //#endif

        //#if API=="BlackBerry" && RIM
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_UP)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_DOWN)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_LEFT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_RIGHT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_SKLEFT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_SKRIGHT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
        //#endif

        //#if API=="LG"
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_SKLEFT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
//#         if ((ms_iKeyInt_Map & (1 << KEYINT_SKRIGHT)) != 0)
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
        //#endif
        
        //#if CanvasMode == "GameCanvas"
//#         /*
//#         int iKeyGetKeys = ms_vMain.getKeyStates();
//#         
//#         if (iKeyGetKeys != mst_iKeyGetKeys) {
//#             // down
//#             boolean bCurrentKeyPressed = ((iKeyGetKeys & GameCanvas.DOWN_PRESSED) != 0);
//#             boolean bPreviouKeyPressed = ((mst_iKeyGetKeys & GameCanvas.DOWN_PRESSED) != 0);
//#             if (bCurrentKeyPressed && !bPreviouKeyPressed)
//#                 ms_vMain.keyPressed(Canvas.KEY_NUM8);
//#             else if (!bCurrentKeyPressed && bPreviouKeyPressed)
//#                 ms_vMain.keyReleased(Canvas.KEY_NUM8);
//#             
//#             // up
//#             bCurrentKeyPressed = ((iKeyGetKeys & GameCanvas.UP_PRESSED) != 0);
//#             bPreviouKeyPressed = ((mst_iKeyGetKeys & GameCanvas.UP_PRESSED) != 0);
//#             if (bCurrentKeyPressed && !bPreviouKeyPressed)
//#                 ms_vMain.keyPressed(Canvas.KEY_NUM2);
//#             else if (!bCurrentKeyPressed && bPreviouKeyPressed)
//#                 ms_vMain.keyReleased(Canvas.KEY_NUM2);
//#             
//#             // left
//#             bCurrentKeyPressed = ((iKeyGetKeys & GameCanvas.LEFT_PRESSED) != 0);
//#             bPreviouKeyPressed = ((mst_iKeyGetKeys & GameCanvas.LEFT_PRESSED) != 0);
//#             if (bCurrentKeyPressed && !bPreviouKeyPressed)
//#                 ms_vMain.keyPressed(Canvas.KEY_NUM4);
//#             else if (!bCurrentKeyPressed && bPreviouKeyPressed)
//#                 ms_vMain.keyReleased(Canvas.KEY_NUM4);
//#             
//#             // right
//#             bCurrentKeyPressed = ((iKeyGetKeys & GameCanvas.RIGHT_PRESSED) != 0);
//#             bPreviouKeyPressed = ((mst_iKeyGetKeys & GameCanvas.RIGHT_PRESSED) != 0);
//#             if (bCurrentKeyPressed && !bPreviouKeyPressed)
//#                 ms_vMain.keyPressed(Canvas.KEY_NUM6);
//#             else if (!bCurrentKeyPressed && bPreviouKeyPressed)
//#                 ms_vMain.keyReleased(Canvas.KEY_NUM6);
//#             
//#             // fire
//#             bCurrentKeyPressed = ((iKeyGetKeys & GameCanvas.FIRE_PRESSED) != 0);
//#             bPreviouKeyPressed = ((mst_iKeyGetKeys & GameCanvas.FIRE_PRESSED) != 0);
//#             if (bCurrentKeyPressed && !bPreviouKeyPressed)
//#                 ms_vMain.keyPressed(Canvas.KEY_NUM5);
//#             else if (!bCurrentKeyPressed && bPreviouKeyPressed)
//#                 ms_vMain.keyReleased(Canvas.KEY_NUM5);
//# 
//#             // star
//#             bCurrentKeyPressed = ((iKeyGetKeys & GameCanvas.GAME_C_PRESSED) != 0);
//#             bPreviouKeyPressed = ((mst_iKeyGetKeys & GameCanvas.GAME_C_PRESSED) != 0);
//#             if (bCurrentKeyPressed && !bPreviouKeyPressed)
//#                 ms_vMain.keyPressed(Canvas.KEY_STAR);
//#             else if (!bCurrentKeyPressed && bPreviouKeyPressed)
//#                 ms_vMain.keyReleased(Canvas.KEY_STAR);
//#             
//#             // apostrophe
//#             bCurrentKeyPressed = ((iKeyGetKeys & GameCanvas.GAME_D_PRESSED) != 0);
//#             bPreviouKeyPressed = ((mst_iKeyGetKeys & GameCanvas.GAME_D_PRESSED) != 0);
//#             if (bCurrentKeyPressed && !bPreviouKeyPressed)
//#                 ms_vMain.keyPressed(Canvas.KEY_POUND);
//#             else if (!bCurrentKeyPressed && bPreviouKeyPressed)
//#                 ms_vMain.keyReleased(Canvas.KEY_POUND);
//#             
//#             mst_iKeyGetKeys = iKeyGetKeys;
//#         }
//#         */
        //#endif
        
    }
    
    /*
     * game painting
     */

    public static Graphics ms_XGraphics = new Graphics(Define.SIZEX, Define.SIZEY);

    //#if MIDP
    public void paint(javax.microedition.lcdui.Graphics _g) {
    //#elif RIM
//#     public void paint(net.rim.device.api.ui.Graphics _g) {
    //#elif DOJA
//#     public void paint(com.nttdocomo.ui.Graphics _g) {
    //#endif
        try {

            //#if CanvasMode == "GameCanvas"
//#             _g = getGraphics();
            //#endif
            
            // hack to keep portraid mode in devices with auto portraid-landscape mode
            //#if BackBuffer != "false" && BackBuffer != "prohibited"
            ms_XGraphics.setGraphics(ms_vCanvasGraphics);
            //#else
//#             ms_XGraphics.setGraphics(_g);
            //#endif
            
            // draw game
            Main.Draw(ms_XGraphics);
            
            // hack to keep portraid mode in devices with auto portraid-landscape mode
            //#if BackBuffer == "keepPortrait"
//#             if (getWidth() < getHeight())
//#                 _g.drawImage(ms_vCanvas, 0, 0, 0);
//#             else
//#                 _g.drawRegion(ms_vCanvas, 0, 0, Define.SIZEX, Define.SIZEY, javax.microedition.lcdui.game.Sprite.TRANS_ROT270, 0, 0, 0);
//#             
            //#elif BackBuffer == "keepLandscape"
//#             if (getWidth() < getHeight())
//#                 _g.drawRegion(ms_vCanvas, 0, 0, Define.SIZEX, Define.SIZEY, javax.microedition.lcdui.game.Sprite.TRANS_ROT270, 0, 0, 0);
//#             else
//#                 _g.drawRegion(ms_vCanvas, 0, 0, Define.SIZEX, Define.SIZEY, 0, 0, 0, 0);
//#             
            //#elif BackBuffer == "prohibited"
//#             if (getWidth() == Define.SIZEY) {
//#                 _g.setClip(0, 0, getWidth(), getHeight());
//#                 _g.setColor(0);
//#                 _g.fillRect(0, 0, getWidth(), getHeight());
//#                 _g.setColor(255,0,0);
//#                 _g.fillRoundRect(((getWidth() - getHeight())>>1), 0, getHeight(), getHeight(), 360, 360);
//#                 _g.setColor(255,255,255);
//#                 _g.fillRect((getWidth()>>1) - (getWidth()>>2), (getHeight()>>1) - (getHeight()>>3), (getWidth()>>1), (getHeight()>>2));
//#             }
//#             
            //#elif BackBuffer == "true"
//#             _g.drawImage(ms_vCanvas, 0, 0, 0);
            //#endif

             ///#if Debug
            /*
            _g.setClip(0, 0, Define.SIZEX, Define.SIZEY);
            _g.setColor(0xff000000);
            _g.drawString("FPS: " + ms_iFPS, 6, 16, 0);
            _g.setColor(0xffffffff);
            _g.drawString("FPS: " + ms_iFPS, 5, 15, 0);
            */
             ///#endif
            
        } catch (Exception ex) {
            //#if Debug
//#             //Debug.DebugMsg("DRAW: St." + Main.ms_iState + " Exception:" + ex.getMessage() + "(" + ex.toString() + ")");
//#             ex.printStackTrace();
            //#endif
            System.out.print("");
        }
        
        //traza
        /*
        _g.setColor(255,255,255);
        _g.drawString("KeyCode: "+Main.ms_iKeyInt_Map, 10+1, 100+1, 0);
        _g.drawString("Action : "+Main.mst_iAction, 10+1, 120+1, 0);
        _g.drawString("GetKey : "+Main.mst_iKeyGetKeys, 10+1, 140+1, 0);
        
        _g.setColor(255,0,0);
        _g.drawString("KeyCode: "+Main.ms_iKeyInt_Map, 10, 100, 0);
        _g.drawString("Action : "+Main.mst_iAction, 10, 120, 0);
        _g.drawString("GetKey : "+Main.mst_iKeyGetKeys, 10, 140, 0);
        */

        //#if CanvasMode == "GameCanvas"
//#         flushGraphics();
        //#endif
        
    }

    // loading bar stuff
    static final Font LOADING_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
   
    static final int LOADING_BARSIZEX = Define.SIZEX/3;
    static final int LOADING_BARSIZEY = LOADING_FONT.getHeight() - 2;
    static final int LOADING_BARX = Define.SIZEX2 - (LOADING_BARSIZEX>>1);
    static final int LOADING_BARY = Define.SIZEY2 - (LOADING_BARSIZEY>>1);
    
    static final int LOADINGBAR_SECTIONS = 360;//20;
    static final int LOADING_BOXSIZEY = Math.max(LOADING_BARSIZEY + 8, Define.SIZEY8);
    static final int LOADING_BOXY = Define.SIZEY2 - (LOADING_BOXSIZEY>>1);


    //#if i560
//#     static final int LOADING_Y_ADJ = 1;
    //#elif ot800 || i475 || i776
//#     static final int LOADING_Y_ADJ = 2;
    //#else
    static final int LOADING_Y_ADJ = -1;
    //#endif
    
    static final int COLOR_BACK = 0xff000000;
    static final int COLOR_MASK = 0xff8accc6;
    
    
    static void Draw(Graphics _g) {
        
        //#ifdef Debug
//#         if (Debug.ms_bShowDebug) {
//#             Debug.DebugDraw(_g);
//#             return;
//#         }
        //#endif

        //#if !(MIDP && API == "BlackBerry")
        if (GfxManager.ms_bLoadingBarActive || GfxManager.ms_bLoadingBarRefresh) {
            GfxManager.ms_bLoadingBarFrame++;

            _g.setAlpha(255);
            _g.setImageSize(256, 256);
            
            // big box
            if (GfxManager.ms_bLoadingBarFrame == 1) {
                _g.setColor(ModeMenu.COLOR_BROWN_DARK);
                _g.fillRect(0, LOADING_BOXY, Define.SIZEX, LOADING_BOXSIZEY);
                _g.setAlpha(255);
                
                _g.setColor (ModeMenu.COLOR_MARC);
                _g.fillRect (0, LOADING_BOXY, Define.SIZEX, 1);
                _g.fillRect (0, LOADING_BOXY + LOADING_BOXSIZEY - 1, Define.SIZEX, 1);
                _g.drawRect (LOADING_BARX - 2, LOADING_BARY - 2, LOADING_BARX + 3, LOADING_BARSIZEY + 3);
            }
            
            // loading bar
            _g.setColor (ModeMenu.COLOR_BROWN_DARK);
            _g.fillRect (LOADING_BARX, LOADING_BARY, LOADING_BARX, LOADING_BARSIZEY);
            _g.setColor (ModeMenu.COLOR_YELLOW);
            _g.fillRect (LOADING_BARX, LOADING_BARY,
                (LOADING_BARX*GfxManager.ms_iLoadingBarCurrent)/GfxManager.ms_iLoadingBarTotal, LOADING_BARSIZEY);

            int iLoadSections = (GfxManager.ms_iLoadingBarCurrent*LOADINGBAR_SECTIONS)/GfxManager.ms_iLoadingBarTotal;
            String zPercentage = ((iLoadSections*100)/LOADINGBAR_SECTIONS) + "%";

            _g.setFont(LOADING_FONT);
            _g.setColor (ModeMenu.COLOR_YELLOW);
            _g.drawString(zPercentage,
                LOADING_BARX + LOADING_BARSIZEX - LOADING_FONT.stringWidth(zPercentage),
                Define.SIZEY2 - (LOADING_FONT.getBaselinePosition()>>1) + LOADING_Y_ADJ, 0);

            _g.setClip(LOADING_BARX, LOADING_BARY, (LOADING_BARX*GfxManager.ms_iLoadingBarCurrent)/GfxManager.ms_iLoadingBarTotal, LOADING_BARSIZEY);
            
            _g.setColor(ModeMenu.COLOR_BROWN_DARK);
            _g.drawString(zPercentage,
                LOADING_BARX + LOADING_BARSIZEX - LOADING_FONT.stringWidth(zPercentage),
                Define.SIZEY2 - (LOADING_FONT.getBaselinePosition()>>1) + LOADING_Y_ADJ, 0);

            _g.setClip (0, 0, Define.SIZEY, Define.SIZEY);
            GfxManager.ms_bLoadingBarRefresh = false;

            return;
        }
        //#endif
        
        if (Define.ms_iState < Define.ST_GAME_INIT)
            ModeMenu.Draw(_g);
        else
            ModeGame.Draw(_g);
        
        // Especial debug traces:
        /*
        _g.setColor(0xffffffff);
        _g.drawString("Free: " + Runtime.getRuntime().freeMemory(), 10+1, 100+1, 0);
        _g.setColor(0xffffffff);
        _g.drawString("Free: " + Runtime.getRuntime().freeMemory(), 10, 100, 0);
        _g.setColor(0xffff0000);
        _g.drawString("L: " + HUNT.ms_zLanguage, 10, 100, 0);
        */
    }

    /*
    public static char mst_cCharMap;
    public static int mst_iFrameTicks10sec, mst_iDist, mst_iKeyCode;
    public static int mst_iAction = 0;
    public static int mst_keyCode;
    public static int mst_iNumKeyPressed;
    public static int mst_iNumKeyReleased;
    public static int mst_iKeyGetKeys;
    */
    
    // Key handling
    public static boolean QWERTY;
    private static final char[] QK = new char[12];
    
    //#if RIM
//#     static {
//#         QWERTY=((Keypad.getKeyCode('q', 0)!=Keypad.getKeyCode('w', 0)) &&
//#               (Keypad.getKeyCode('w', 0)!=Keypad.getKeyCode('e', 0)));
//#         
//#         if (QWERTY) {
//#             String zQWERTY_KEYS = "wersdfzxc%aq";
//#             for (int i=0; i<zQWERTY_KEYS.length() && i<QK.length; i++)
//#                 QK[i] = zQWERTY_KEYS.charAt(i);
//#         }
//#     }
    //#else
    static {
        String zQWERTY_KEYS = WolfRunner.ms_vInstance.getAppProperty ("Custom-QWERTY");
        if (zQWERTY_KEYS != null) {
            QWERTY = true;
            for (int i=0; i<zQWERTY_KEYS.length() && i<QK.length; i++)
                QK[i] = zQWERTY_KEYS.charAt(i);
        }
    }
    //#endif
    
    //#if API=="Motorola"
//#     public static final int KEYCODE_SK_LEFT    =  21;
//#     public static final int KEYCODE_SK_RIGHT   =  22;
//#     public static final int KEYCODE_SK_LEFT_2  = -21;
//#     public static final int KEYCODE_SK_RIGHT_2 = -22;
//#     public static final int KEYCODE_CLEAR      = - 8;
//#     public static final int KEYCODE_POUND      =  35;
//#     public static final int KEYCODE_STAR       =  42;
//#     
    //#elif API=="Siemens"
//#     public static final int KEYCODE_SK_LEFT  = -1;
//#     public static final int KEYCODE_SK_RIGHT = -4;
//#     public static final int KEYCODE_CLEAR = -8;
//#
    //#elif API=="BlackBerry"
//#     public static final int KEYCODE_SK_LEFT  = 1114112;
//#     public static final int KEYCODE_SK_RIGHT = 1179648;
//#     public static final int KEYCODE_SK_MENU  = 268566528;
//#     public static final int KEYCODE_SK_BACK  = 1769472;
//#     public static final int KEYCODE_CLEAR    = 524288;
//#     public static final int KEYCODE_POUND    = 17104896;
//#     public static final int KEYCODE_STAR     = 1310720;
//# 
    //#elif DOJA
//#    
    //#elif kg800
//#     public static final int KEYCODE_SK_LEFT  = -202;
//#     public static final int KEYCODE_SK_RIGHT = -203;
//#     public static final int KEYCODE_CLEAR    = -204;
//#     public static final int KEYCODE_POUND    = 35;
//#     public static final int KEYCODE_STAR     = 42;
   //#elif ot556
//#     public static final int KEYCODE_SK_LEFT  = -21;
//#     public static final int KEYCODE_SK_RIGHT = -22;
//#     public static final int KEYCODE_CLEAR    = -8;
//#     public static final int KEYCODE_POUND    = 35;
//#     public static final int KEYCODE_STAR     = 42;
    //#else
    ///#elif API=="Standard" || API=="Nokia" || API=="SonyEricsson" || API == "KDDI" || API=="Samsung" || API=="Sagem" || API=="NEC" || API=="LG"
    public static final int KEYCODE_SK_LEFT  = -6;
    public static final int KEYCODE_SK_RIGHT = -7;
    public static final int KEYCODE_CLEAR = -8;
    public static final int KEYCODE_POUND = 35;
    public static final int KEYCODE_STAR = 42;

   //#endif

    // Key processing
    //#if MIDP
    //#if API=="BlackBerry"
//#     public boolean trackwheelRoll(int a, int b, int c) {
//#         return false;
//#     }
//#     public boolean trackwheelUnclick(int _iStatus, int _iTime) {
//#         ms_iKeyInt_Map &= ~(1 << KEYINT_FIRE);
//#         HandleInput();
//#         return true;
//#     }
//#     public boolean trackwheelClick(int _iStatus, int _iTime) {
//#         ms_iKeyInt_Map |= 1<<KEYINT_FIRE;
//#         HandleInput();
//#         return true;
//#     }
//#     public boolean keyStatus (int _iKeyCode, int _iTime) {
//#         return false;
//#     }
//#     public boolean keyRepeat (int _iKeyCode, int _iTime) {
//#         return false;
//#     }
//#     public boolean keyChar (char _iChar, int _iKeyCode, int _iTime) {
//#         return false;
//#     }
//# 
//#     public boolean keyDown (int _iKeyCode, int _iTime) {
//#         int iKeyCode = Keypad.key(_iKeyCode);
//#       
//#         // ## LEFT SOFTKEY ##
//#         if (_iKeyCode == KEYCODE_SK_LEFT || iKeyCode == Characters.ENTER) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_SKLEFT;
//#             return true;
//#         }
//#         // ## RIGHT SOFTKEY ##
//#         else if (_iKeyCode == KEYCODE_SK_RIGHT) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_SKRIGHT;
//#             return true;
//#         }
//#         // ## CLEAR ##
//#         else if (_iKeyCode == KEYCODE_CLEAR || iKeyCode == Characters.BACKSPACE) {
//#             ms_iKeyInt_Map |= 1 << KEYINT_CLEAR;
//#         }
//#         HandleInput();
//#         return false;
//#     }
//# 
//#     public boolean keyUp (int _iKeyCode, int _iTime) {
//#         int iKeyCode = Keypad.key(_iKeyCode);
//# 
//#         // ## LEFT SOFTKEY ##
//#         if (_iKeyCode == KEYCODE_SK_LEFT || iKeyCode == Characters.ENTER) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
//#             return true;
//#         }
//#         // ## RIGHT SOFTKEY ##
//#         else if (_iKeyCode == KEYCODE_SK_RIGHT) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
//#             return true;
//#         }
//#         // ## CLEAR ##
//#         else if(_iKeyCode == KEYCODE_CLEAR || iKeyCode == Characters.BACKSPACE) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_CLEAR);
//#         }
//#         HandleInput();
//#         return false;
//#     }
//# 
//#     public void keyPressed (int _iKeyCode) {
//# 
//#         // ## UP ##
//#         if (_iKeyCode == UP) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_UP;
//#         }
//#         // ## DOWN ##
//#         else if (_iKeyCode == DOWN) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_DOWN;
//#         }
//#         // ## LEFT ##
//#         else if (_iKeyCode == LEFT) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_LEFT;
//#         }
//#         // ## RIGHT ##
//#         else if (_iKeyCode == RIGHT) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_RIGHT;
//#         }
//#         
//#         char mapedChar = (char) _iKeyCode;
//#         ms_iKeyChar_Map = (""+mapedChar).toLowerCase().charAt(0);
//# 
//#         if (QWERTY){
//#             if      (ms_iKeyChar_Map == QK[0]  || ms_iKeyChar_Map == '1') ms_iKeyInt_Map |= 1 << KEYINT_1;
//#             else if (ms_iKeyChar_Map == QK[1]  || ms_iKeyChar_Map == '2') ms_iKeyInt_Map |= 1 << KEYINT_2;
//#             else if (ms_iKeyChar_Map == QK[2]  || ms_iKeyChar_Map == '3') ms_iKeyInt_Map |= 1 << KEYINT_3;
//#             else if (ms_iKeyChar_Map == QK[3]  || ms_iKeyChar_Map == '4') ms_iKeyInt_Map |= 1 << KEYINT_4;
//#             else if (ms_iKeyChar_Map == QK[4]  || ms_iKeyChar_Map == '5') ms_iKeyInt_Map |= 1 << KEYINT_5;
//#             else if (ms_iKeyChar_Map == QK[5]  || ms_iKeyChar_Map == '6') ms_iKeyInt_Map |= 1 << KEYINT_6;
//#             else if (ms_iKeyChar_Map == QK[6]  || ms_iKeyChar_Map == '7') ms_iKeyInt_Map |= 1 << KEYINT_7;
//#             else if (ms_iKeyChar_Map == QK[7]  || ms_iKeyChar_Map == '8') ms_iKeyInt_Map |= 1 << KEYINT_8;
//#             else if (ms_iKeyChar_Map == QK[8]  || ms_iKeyChar_Map == '9') ms_iKeyInt_Map |= 1 << KEYINT_9;
//#             else if (ms_iKeyChar_Map == QK[9]  || ms_iKeyChar_Map == '0') ms_iKeyInt_Map |= 1 << KEYINT_0;
//#             else if (ms_iKeyChar_Map == QK[10] || ms_iKeyChar_Map == '#') ms_iKeyInt_Map |= 1 << KEYINT_POUND;
//#             else if (ms_iKeyChar_Map == QK[11] || ms_iKeyChar_Map == '*') ms_iKeyInt_Map |= 1 << KEYINT_STAR;
//#             
//#         } else {
//# 
//#             switch (ms_iKeyChar_Map){
//#                 case (int)'q':  case (int)'w':  case (int)'!':  ms_iKeyInt_Map |= 1<<KEYINT_Q; break;
//#                 case (int)'a':  case (int)'s':  case (int)'?':  ms_iKeyInt_Map |= 1<<KEYINT_A; break;
//#                 case (int)'z':  case (int)'x':  case (int)'@':  ms_iKeyInt_Map |= 1<<KEYINT_Z; break;
//#                 case (int)'o':  case (int)'p':  case (int)'.':  ms_iKeyInt_Map |= 1<<KEYINT_O; break;
//#                 case (int)'l':  case (int)',':  ms_iKeyInt_Map |= 1<<KEYINT_L; break;
//#     
//#                 case (int)'1':  case (int)'e':  case (int)'r':  ms_iKeyInt_Map |= 1<<KEYINT_1; break;
//#                 case (int)'2':  case (int)'t':  case (int)'y':  ms_iKeyInt_Map |= 1<<KEYINT_2; break;
//#                 case (int)'3':  case (int)'u':  case (int)'i':  ms_iKeyInt_Map |= 1<<KEYINT_3; break;
//#                 case (int)'4':  case (int)'d':  case (int)'f':  ms_iKeyInt_Map |= 1<<KEYINT_4; break;
//#                 case (int)'5':  case (int)'g':  case (int)'h':  ms_iKeyInt_Map |= 1<<KEYINT_5; break;
//#                 case (int)'6':  case (int)'j':  case (int)'k':  ms_iKeyInt_Map |= 1<<KEYINT_6; break;
//#                 case (int)'7':  case (int)'c':  case (int)'v':  ms_iKeyInt_Map |= 1<<KEYINT_7; break;
//#                 case (int)'8':  case (int)'b':  case (int)'n':  ms_iKeyInt_Map |= 1<<KEYINT_8; break;
//#                 case (int)'9':  case (int)'m':  ms_iKeyInt_Map |= 1<<KEYINT_9; break;
//#                 case (int)'0':  case (int)' ':  ms_iKeyInt_Map |= 1<<KEYINT_0; break;
//#          }
//#       }
//#       HandleInput();
//#    }
//# 
//#    public void keyReleased(int _iKeyCode) {
//# 
//#         char mapedChar = (char) _iKeyCode;
//#         ms_iKeyChar_Map = (""+mapedChar).toLowerCase().charAt(0);
//# 
//#         if (QWERTY) {
//#             if      (ms_iKeyChar_Map == QK[0]  || ms_iKeyChar_Map == '1') ms_iKeyInt_Map &= ~(1 << KEYINT_1);
//#             else if (ms_iKeyChar_Map == QK[1]  || ms_iKeyChar_Map == '2') ms_iKeyInt_Map &= ~(1 << KEYINT_2);
//#             else if (ms_iKeyChar_Map == QK[2]  || ms_iKeyChar_Map == '3') ms_iKeyInt_Map &= ~(1 << KEYINT_3);
//#             else if (ms_iKeyChar_Map == QK[3]  || ms_iKeyChar_Map == '4') ms_iKeyInt_Map &= ~(1 << KEYINT_4);
//#             else if (ms_iKeyChar_Map == QK[4]  || ms_iKeyChar_Map == '5') ms_iKeyInt_Map &= ~(1 << KEYINT_5);
//#             else if (ms_iKeyChar_Map == QK[5]  || ms_iKeyChar_Map == '6') ms_iKeyInt_Map &= ~(1 << KEYINT_6);
//#             else if (ms_iKeyChar_Map == QK[6]  || ms_iKeyChar_Map == '7') ms_iKeyInt_Map &= ~(1 << KEYINT_7);
//#             else if (ms_iKeyChar_Map == QK[7]  || ms_iKeyChar_Map == '8') ms_iKeyInt_Map &= ~(1 << KEYINT_8);
//#             else if (ms_iKeyChar_Map == QK[8]  || ms_iKeyChar_Map == '9') ms_iKeyInt_Map &= ~(1 << KEYINT_9);
//#             else if (ms_iKeyChar_Map == QK[9]  || ms_iKeyChar_Map == '0') ms_iKeyInt_Map &= ~(1 << KEYINT_0);
//#             else if (ms_iKeyChar_Map == QK[10] || ms_iKeyChar_Map == '#') ms_iKeyInt_Map &= ~(1 << KEYINT_POUND);
//#             else if (ms_iKeyChar_Map == QK[11] || ms_iKeyChar_Map == '*') ms_iKeyInt_Map &= ~(1 << KEYINT_STAR);
//#     
//#         } else {
//# 
//#             switch (ms_iKeyChar_Map){
//#                 case (int)'q':  case (int)'w':  case (int)'!':  ms_iKeyInt_Map &= ~(1 << KEYINT_Q); break;
//#                 case (int)'a':  case (int)'s':  case (int)'?':  ms_iKeyInt_Map &= ~(1 << KEYINT_A); break;
//#                 case (int)'z':  case (int)'x':  case (int)'@':  ms_iKeyInt_Map &= ~(1 << KEYINT_Z); break;
//#                 case (int)'o':  case (int)'p':  case (int)'.':  ms_iKeyInt_Map &= ~(1 << KEYINT_O); break;
//#                 case (int)'l':  case (int)',':  ms_iKeyInt_Map &= ~(1 << KEYINT_L); break;
//#     
//#                 case (int)'1':  case (int)'e':  case (int)'r':  ms_iKeyInt_Map &= ~(1 << KEYINT_1); break;
//#                 case (int)'2':  case (int)'t':  case (int)'y':  ms_iKeyInt_Map &= ~(1 << KEYINT_2); break;
//#                 case (int)'3':  case (int)'u':  case (int)'i':  ms_iKeyInt_Map &= ~(1 << KEYINT_3); break;
//#                 case (int)'4':  case (int)'d':  case (int)'f':  ms_iKeyInt_Map &= ~(1 << KEYINT_4); break;
//#                 case (int)'5':  case (int)'g':  case (int)'h':  ms_iKeyInt_Map &= ~(1 << KEYINT_5); break;
//#                 case (int)'6':  case (int)'j':  case (int)'k':  ms_iKeyInt_Map &= ~(1 << KEYINT_6); break;
//#                 case (int)'7':  case (int)'c':  case (int)'v':  ms_iKeyInt_Map &= ~(1 << KEYINT_7); break;
//#                 case (int)'8':  case (int)'b':  case (int)'n':  ms_iKeyInt_Map &= ~(1 << KEYINT_8); break;
//#                 case (int)'9':  case (int)'m':  ms_iKeyInt_Map &= ~(1 << KEYINT_9); break;
//#                 case (int)'0':  case (int)' ':  ms_iKeyInt_Map &= ~(1 << KEYINT_0); break;
//#             }
//#         }
//#         ms_iKeyChar_Map = 0;
//#         
//#         HandleInput();
//#     };
    //#elif API == "Nextel"
//#     // Key processing MIDP
//#     public void keyPressed(int keyCode) {
//# 
//#         // ## LEFT SOFTKEY ##
//#         if ((keyCode == -20)) {
//#             ms_iKeyInt_Map |= 1 << KEYINT_SKLEFT;
//#         // ## RIGHT SOFTKEY ##
//#         } else if ((keyCode == -21)) {
//#             ms_iKeyInt_Map |= 1 << KEYINT_SKRIGHT;
//#             
//#         } else {
//#             
//#             if (QWERTY) {
//#                 char mapedChar = (char) keyCode;
//#                 mapedChar = ("" + mapedChar).toLowerCase().charAt(0);
//# 
//#                 if (keyCode > 0) {
//# 
//#                     ms_iKeyChar_Map = mapedChar;
//# 
//#                     if      (mapedChar == QK[0]  || mapedChar == '1') ms_iKeyInt_Map |= 1 << KEYINT_1;
//#                     else if (mapedChar == QK[1]  || mapedChar == '2') ms_iKeyInt_Map |= 1 << KEYINT_2;
//#                     else if (mapedChar == QK[2]  || mapedChar == '3') ms_iKeyInt_Map |= 1 << KEYINT_3;
//#                     else if (mapedChar == QK[3]  || mapedChar == '4') ms_iKeyInt_Map |= 1 << KEYINT_4;
//#                     else if (mapedChar == QK[4]  || mapedChar == '5') ms_iKeyInt_Map |= 1 << KEYINT_5;
//#                     else if (mapedChar == QK[5]  || mapedChar == '6') ms_iKeyInt_Map |= 1 << KEYINT_6;
//#                     else if (mapedChar == QK[6]  || mapedChar == '7') ms_iKeyInt_Map |= 1 << KEYINT_7;
//#                     else if (mapedChar == QK[7]  || mapedChar == '8') ms_iKeyInt_Map |= 1 << KEYINT_8;
//#                     else if (mapedChar == QK[8]  || mapedChar == '9') ms_iKeyInt_Map |= 1 << KEYINT_9;
//#                     else if (mapedChar == QK[9]  || mapedChar == '0') ms_iKeyInt_Map |= 1 << KEYINT_0;
//#                     else if (mapedChar == QK[10] || mapedChar == '#') ms_iKeyInt_Map |= 1 << KEYINT_POUND;
//#                     else if (mapedChar == QK[11] || mapedChar == '*') ms_iKeyInt_Map |= 1 << KEYINT_STAR;
//#                     
//#                 } else {
//#                     // ## UP ##
//#                     if (keyCode == -10) {
//#                         ms_iKeyInt_Map |= 1 << KEYINT_UP;
//#                     } // ## DOWN ##
//#                     else if (keyCode == -11) {
//#                         ms_iKeyInt_Map |= 1 << KEYINT_DOWN;
//#                     } // ## RIGHT ##
//#                     else if (keyCode == -12) {
//#                         ms_iKeyInt_Map |= 1 << KEYINT_RIGHT;
//#                     } // ## LEFT ##
//#                     else if (keyCode == -13) {
//#                         ms_iKeyInt_Map |= 1 << KEYINT_LEFT;
//#                     } // ## FIRE ##
//#                     else if (keyCode == -23 && keyCode != KEY_NUM5) {
//#                         ms_iKeyInt_Map |= 1 << KEYINT_FIRE;
//#                     }
//#                 }
//#             } else {
//#                 // ## KEY "0" ##
//#                 if (keyCode == KEY_NUM0) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_0;
//#                 } // ## KEY "1" ##
//#                 else if (keyCode == KEY_NUM1) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_1;
//#                 } // ## KEY "2" ##
//#                 else if (keyCode == KEY_NUM2) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_2;
//#                 } // ## KEY "3" ##
//#                 else if (keyCode == KEY_NUM3) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_3;
//#                 } // ## KEY "4" ##
//#                 else if (keyCode == KEY_NUM4) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_4;
//#                 } // ## KEY "5" ##
//#                 else if (keyCode == KEY_NUM5) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_5;
//#                 } // ## KEY "6" ##
//#                 else if (keyCode == KEY_NUM6) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_6;
//#                 } // ## KEY "7" ##
//#                 else if (keyCode == KEY_NUM7) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_7;
//#                 } // ## KEY "8" ##
//#                 else if (keyCode == KEY_NUM8) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_8;
//#                 } // ## KEY "9" ##
//#                 else if (keyCode == KEY_NUM9) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_9;
//#                 } // ## UP ##
//#                 else if (keyCode == -10) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_UP;
//#                 } // ## DOWN ##
//#                 else if (keyCode == -11) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_DOWN;
//#                 } // ## LEFT ##
//#                 else if (keyCode == -13) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_LEFT;
//#                 } // ## RIGHT ##
//#                 else if (keyCode == -12) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_RIGHT;
//#                 } // ## FIRE ##
//#                 else if ((keyCode == -23) && keyCode != KEY_NUM5) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_FIRE;
//#                 } // ## STAR ## 
//#                 else if (keyCode == KEY_STAR) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_STAR;
//#                 } // ## POUND ##
//#                 else if (keyCode == KEY_POUND) {
//#                     ms_iKeyInt_Map |= 1 << KEYINT_POUND;
//#                 }
//#             }
//#         }
//#         HandleInput();
//#     }
//# 
//#     public void keyReleased(int keyCode) {
//#         // ## LEFT SOFTKEY ##
//#         if ((keyCode == -20)) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
//#         // ## RIGHT SOFTKEY ##
//#         } else if ((keyCode == -21)) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
//#             
//#         } else {
//#             
//#             if (QWERTY) {
//# 
//#                 char mapedChar = (char) keyCode;
//#                 mapedChar = ("" + mapedChar).toLowerCase().charAt(0);
//# 
//#                 if (keyCode > 0) {
//# 
//#                     if      (mapedChar == QK[0]  || mapedChar == '1') ms_iKeyInt_Map &= ~(1 << KEYINT_1);
//#                     else if (mapedChar == QK[1]  || mapedChar == '2') ms_iKeyInt_Map &= ~(1 << KEYINT_2);
//#                     else if (mapedChar == QK[2]  || mapedChar == '3') ms_iKeyInt_Map &= ~(1 << KEYINT_3);
//#                     else if (mapedChar == QK[3]  || mapedChar == '4') ms_iKeyInt_Map &= ~(1 << KEYINT_4);
//#                     else if (mapedChar == QK[4]  || mapedChar == '5') ms_iKeyInt_Map &= ~(1 << KEYINT_5);
//#                     else if (mapedChar == QK[5]  || mapedChar == '6') ms_iKeyInt_Map &= ~(1 << KEYINT_6);
//#                     else if (mapedChar == QK[6]  || mapedChar == '7') ms_iKeyInt_Map &= ~(1 << KEYINT_7);
//#                     else if (mapedChar == QK[7]  || mapedChar == '8') ms_iKeyInt_Map &= ~(1 << KEYINT_8);
//#                     else if (mapedChar == QK[8]  || mapedChar == '9') ms_iKeyInt_Map &= ~(1 << KEYINT_9);
//#                     else if (mapedChar == QK[9]  || mapedChar == '0') ms_iKeyInt_Map &= ~(1 << KEYINT_0);
//#                     else if (mapedChar == QK[10] || mapedChar == '#') ms_iKeyInt_Map &= ~(1 << KEYINT_POUND);
//#                     else if (mapedChar == QK[11] || mapedChar == '*') ms_iKeyInt_Map &= ~(1 << KEYINT_STAR);
//#                     
//#                 } else {             
//#                     // ## UP ##
//#                     if (keyCode == -10) {
//#                         ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
//#                     // ## DOWN ##
//#                     } else if (keyCode == -11) {
//#                         ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
//#                     // ## RIGHT ##
//#                     } else if (keyCode == -12) {
//#                         ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
//#                     // ## LEFT ##
//#                     } else if (keyCode == -13) {
//#                         ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
//#                     // ## FIRE ##
//#                     } else if (keyCode == -23 && keyCode != KEY_NUM5) {
//#                         ms_iKeyInt_Map &= ~(1 << KEYINT_FIRE);
//#                     }
//#                 }
//#             } else {
//# 
//#                 // ## KEY "0" ##
//#                 if (keyCode == KEY_NUM0) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_0);
//#                 } // ## KEY "1" ##
//#                 else if (keyCode == KEY_NUM1) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_1);
//#                 } // ## KEY "2" ##
//#                 else if (keyCode == KEY_NUM2) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_2);
//#                 } // ## KEY "3" ##
//#                 else if (keyCode == KEY_NUM3) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_3);
//#                 } // ## KEY "4" ##
//#                 else if (keyCode == KEY_NUM4) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_4);
//#                 } // ## KEY "5" ##
//#                 else if (keyCode == KEY_NUM5) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_5);
//#                 } // ## KEY "6" ##
//#                 else if (keyCode == KEY_NUM6) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_6);
//#                 } // ## KEY "7" ##
//#                 else if (keyCode == KEY_NUM7) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_7);
//#                 } // ## KEY "8" ##
//#                 else if (keyCode == KEY_NUM8) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_8);
//#                 } // ## KEY "9" ##
//#                 else if (keyCode == KEY_NUM9) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_9);
//#                 } // ## UP ##
//#                 else if (keyCode == -10) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
//#                 } // ## DOWN ##
//#                 else if (keyCode == -11) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
//#                 } // ## LEFT ##
//#                 else if (keyCode == -13) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
//#                 } // ##RIGHT ##
//#                 else if (keyCode == -12) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
//#                 } // ## FIRE ##
//#                 else if ((keyCode == -23) && keyCode != KEY_NUM5) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_FIRE);
//#                 } // ## STAR ##
//#                 else if (keyCode == KEY_STAR) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_STAR);
//#                 } // ## POUND ##
//#                 else if (keyCode == KEY_POUND) {
//#                     ms_iKeyInt_Map &= ~(1 << KEYINT_POUND);
//#                 }
//#             }
//#         }
//#         HandleInput();
//#     }
    //#else
    // Key processing MIDP
    public void keyPressed(int keyCode) {
        int iGameKeyCode = 0;

        //#if a177
//#         ms_iKeyInt_Map = 0;
        //#endif
        
        try {
            iGameKeyCode = getGameAction(keyCode);
        } catch (Exception e) {
        }
        
        // ## LEFT SOFTKEY ##
        if (keyCode == KEYCODE_SK_LEFT) {
            ms_iKeyInt_Map |= 1 << KEYINT_SKLEFT;
        } // ## RIGHT SOFTKEY ##
        else if ((keyCode == KEYCODE_SK_RIGHT)) {
            ms_iKeyInt_Map |= 1 << KEYINT_SKRIGHT;
        }
        //#if API=="Motorola"
//#             // ## LEFT SOFTKEY 2 ##
//#         else if (keyCode == KEYCODE_SK_LEFT_2) {
//#             ms_iKeyInt_Map |= 1 << KEYINT_SKLEFT;
//#         } // ## RIGHT SOFTKEY 2 ##
//#         else if (keyCode == KEYCODE_SK_RIGHT_2) {
//#             ms_iKeyInt_Map |= 1 << KEYINT_SKRIGHT;
//#         }
        //#endif
        
        else {
            if (QWERTY) {

                char mapedChar = (char) keyCode;
                mapedChar = ("" + mapedChar).toLowerCase().charAt(0);
            
                if (keyCode > 0) {
               
                    ms_iKeyChar_Map = mapedChar;
            
                    if      (mapedChar == QK[0]  || mapedChar == '1') ms_iKeyInt_Map |= 1 << KEYINT_1;
                    else if (mapedChar == QK[1]  || mapedChar == '2') ms_iKeyInt_Map |= 1 << KEYINT_2;
                    else if (mapedChar == QK[2]  || mapedChar == '3') ms_iKeyInt_Map |= 1 << KEYINT_3;
                    else if (mapedChar == QK[3]  || mapedChar == '4') ms_iKeyInt_Map |= 1 << KEYINT_4;
                    else if (mapedChar == QK[4]  || mapedChar == '5') ms_iKeyInt_Map |= 1 << KEYINT_5;
                    else if (mapedChar == QK[5]  || mapedChar == '6') ms_iKeyInt_Map |= 1 << KEYINT_6;
                    else if (mapedChar == QK[6]  || mapedChar == '7') ms_iKeyInt_Map |= 1 << KEYINT_7;
                    else if (mapedChar == QK[7]  || mapedChar == '8') ms_iKeyInt_Map |= 1 << KEYINT_8;
                    else if (mapedChar == QK[8]  || mapedChar == '9') ms_iKeyInt_Map |= 1 << KEYINT_9;
                    else if (mapedChar == QK[9]  || mapedChar == '0') ms_iKeyInt_Map |= 1 << KEYINT_0;
                    else if (mapedChar == QK[10] || mapedChar == '#') ms_iKeyInt_Map |= 1 << KEYINT_POUND;
                    else if (mapedChar == QK[11] || mapedChar == '*') ms_iKeyInt_Map |= 1 << KEYINT_STAR;
                    
                } else {
                    // ## UP ##
                    if (iGameKeyCode == UP) {
                        ms_iKeyInt_Map |= 1 << KEYINT_UP;
                    } // ## DOWN ##
                    else if (iGameKeyCode == DOWN) {
                        ms_iKeyInt_Map |= 1 << KEYINT_DOWN;
                    } // ## LEFT ##
                    else if (iGameKeyCode == LEFT) {
                        ms_iKeyInt_Map |= 1 << KEYINT_LEFT;
                    } // ## RIGHT ##
                    else if (iGameKeyCode == RIGHT) {
                        ms_iKeyInt_Map |= 1 << KEYINT_RIGHT;
                    } // ## FIRE ##
                    else if ((iGameKeyCode == FIRE) && keyCode != KEY_NUM5) {
                        ms_iKeyInt_Map |= 1 << KEYINT_FIRE;
                    }
                }
            } else {

                // ## KEY "0" ##
                if (keyCode == KEY_NUM0) {
                    ms_iKeyInt_Map |= 1 << KEYINT_0;
                } // ## KEY "1" ##
                else if (keyCode == KEY_NUM1) {
                    ms_iKeyInt_Map |= 1 << KEYINT_1;
                } // ## KEY "2" ##
                else if (keyCode == KEY_NUM2) {
                    ms_iKeyInt_Map |= 1 << KEYINT_2;
                } // ## KEY "3" ##
                else if (keyCode == KEY_NUM3) {
                    ms_iKeyInt_Map |= 1 << KEYINT_3;
                } // ## KEY "4" ##
                else if (keyCode == KEY_NUM4) {
                    ms_iKeyInt_Map |= 1 << KEYINT_4;
                } // ## KEY "5" ##
                else if (keyCode == KEY_NUM5) {
                    ms_iKeyInt_Map |= 1 << KEYINT_5;
                } // ## KEY "6" ##
                else if (keyCode == KEY_NUM6) {
                    ms_iKeyInt_Map |= 1 << KEYINT_6;
                } // ## KEY "7" ##
                else if (keyCode == KEY_NUM7) {
                    ms_iKeyInt_Map |= 1 << KEYINT_7;
                } // ## KEY "8" ##
                else if (keyCode == KEY_NUM8) {
                    ms_iKeyInt_Map |= 1 << KEYINT_8;
                } // ## KEY "9" ##
                else if (keyCode == KEY_NUM9) {
                    ms_iKeyInt_Map |= 1 << KEYINT_9;
                } // ## UP ##
                else if(iGameKeyCode == UP) {
                    ms_iKeyInt_Map |= 1 << KEYINT_UP;
                } // ## DOWN ##
                else if (iGameKeyCode == DOWN) {               
                    ms_iKeyInt_Map |= 1 << KEYINT_DOWN;
                } // ## LEFT ##
                else if (iGameKeyCode == LEFT) {                
                    ms_iKeyInt_Map |= 1 << KEYINT_LEFT;
                } // ## RIGHT ##
                else if (iGameKeyCode == RIGHT) {                
                    ms_iKeyInt_Map |= 1 << KEYINT_RIGHT;
                } // ## FIRE ##
                else if ((iGameKeyCode == FIRE) && keyCode != KEY_NUM5) {
                    ms_iKeyInt_Map |= 1 << KEYINT_FIRE;
                }
            }
        }
        //HandleInput();
    }

    public void keyReleased(int keyCode) {
        int iGameKeyCode = 0;

        try {
            iGameKeyCode = getGameAction(keyCode);
        } catch (Exception e) {
        }
      
        //#if API != "LG"
        // ## LEFT SOFTKEY ##
        if (keyCode == KEYCODE_SK_LEFT) {
            ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
        } // ## RIGHT SOFTKEY ##
        else if (keyCode == KEYCODE_SK_RIGHT) {
            ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
        } 
        else 
        //#if API=="Motorola"
//#         // ## LEFT SOFTKEY 2 ##
//#         if (keyCode == KEYCODE_SK_LEFT_2) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
//#         } // ## RIGHT SOFTKEY 2 ##
//#         else if (keyCode == KEYCODE_SK_RIGHT_2) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
//#         } 
//#         else 
        //#endif
         
        //#endif
        
        {
            if (QWERTY) {
                
                char mapedChar = (char) keyCode;
                mapedChar = ("" + mapedChar).toLowerCase().charAt(0);
            
                if (keyCode > 0) {

                    if      (mapedChar == QK[0]  || mapedChar == '1') ms_iKeyInt_Map &= ~(1 << KEYINT_1);
                    else if (mapedChar == QK[1]  || mapedChar == '2') ms_iKeyInt_Map &= ~(1 << KEYINT_2);
                    else if (mapedChar == QK[2]  || mapedChar == '3') ms_iKeyInt_Map &= ~(1 << KEYINT_3);
                    else if (mapedChar == QK[3]  || mapedChar == '4') ms_iKeyInt_Map &= ~(1 << KEYINT_4);
                    else if (mapedChar == QK[4]  || mapedChar == '5') ms_iKeyInt_Map &= ~(1 << KEYINT_5);
                    else if (mapedChar == QK[5]  || mapedChar == '6') ms_iKeyInt_Map &= ~(1 << KEYINT_6);
                    else if (mapedChar == QK[6]  || mapedChar == '7') ms_iKeyInt_Map &= ~(1 << KEYINT_7);
                    else if (mapedChar == QK[7]  || mapedChar == '8') ms_iKeyInt_Map &= ~(1 << KEYINT_8);
                    else if (mapedChar == QK[8]  || mapedChar == '9') ms_iKeyInt_Map &= ~(1 << KEYINT_9);
                    else if (mapedChar == QK[9]  || mapedChar == '0') ms_iKeyInt_Map &= ~(1 << KEYINT_0);
                    else if (mapedChar == QK[10] || mapedChar == '#') ms_iKeyInt_Map &= ~(1 << KEYINT_POUND);
                    else if (mapedChar == QK[11] || mapedChar == '*') ms_iKeyInt_Map &= ~(1 << KEYINT_STAR);
                    
                } else {
                    
                    //#if !s390g
                    // ## UP ##
                    if (iGameKeyCode == UP) {
                        ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
                    } // ## DOWN ##
                    else if (iGameKeyCode == DOWN) {
                        ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
                    } // ## LEFT ##
                    else if (iGameKeyCode == LEFT) {
                        ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
                    } // ## RIGHT ##
                    else if (iGameKeyCode == RIGHT) {
                        ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
                    } // ## FIRE ##
                    else if ((iGameKeyCode == FIRE) && keyCode != KEY_NUM5) {
                        ms_iKeyInt_Map &= ~(1 << KEYINT_FIRE);
                    }
                    //#endif
                }
            } else {

                // ## KEY "0" ##
                if (keyCode == KEY_NUM0) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_0);
                } // ## KEY "1" ##
                else if (keyCode == KEY_NUM1) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_1);
                } // ## KEY "2" ##
                else if (keyCode == KEY_NUM2) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_2);
                } // ## KEY "3" ##
                else if (keyCode == KEY_NUM3) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_3);
                } // ## KEY "4" ##
                else if (keyCode == KEY_NUM4) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_4);
                } // ## KEY "5" ##
                else if (keyCode == KEY_NUM5) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_5);
                } // ## KEY "6" ##
                else if (keyCode == KEY_NUM6) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_6);
                } // ## KEY "7" ##
                else if (keyCode == KEY_NUM7) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_7);
                } // ## KEY "8" ##
                else if (keyCode == KEY_NUM8) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_8);
                } // ## KEY "9" ##
                else if (keyCode == KEY_NUM9) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_9);
                } // ## UP ##
                //#if !s390g
                else if(iGameKeyCode == UP) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
                } // ## DOWN ##
                else if (iGameKeyCode == DOWN) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
                } // ## LEFT ##
                else if (iGameKeyCode == LEFT) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
                } // ##RIGHT ##
                else if (iGameKeyCode == RIGHT) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
                } // ## FIRE ##
                else if ((iGameKeyCode == FIRE) && keyCode != KEY_NUM5) {
                    ms_iKeyInt_Map &= ~(1 << KEYINT_FIRE);
                }
                //#endif
            }
        }
        //HandleInput();
        
        //#if e600
//#             ms_iKeyInt_Map = 0;
        //#endif
    }
    //#endif
    
    //#elif RIM
//#     // Trackball for Blackberry devices
//#     static final int TRACK_LEFT = 1;
//#     static final int TRACK_RIGHT = 2;
//#     static final int TRACK_UP = 4;
//#     static final int TRACK_DOWN = 8;
//# 
//#     static int ms_iTrackDirX, ms_iTrackDirY;
//#     static int ms_iPrevTrackPointerX, ms_iPrevTrackPointerY;
//#     static int ms_iTrackPointerX, ms_iTrackPointerY;
//#     static int ms_iTrackLastFrame;
//# 
//#     static void ResetTrackPointer () {
//#         ms_iTrackPointerX = Define.SIZEX2;
//#         ms_iTrackPointerY = Define.SIZEY2;
//#     }
//#     static void SetTrackPointer (int _iX, int _iY) {
//#         ms_iTrackPointerX = _iX;
//#         ms_iTrackPointerY = _iY;
//#     }
//#     static int GetTrackPointerX () {
//#         return ms_iTrackPointerX;
//#     }
//#     static int GetTrackPointerY () {
//#         return ms_iTrackPointerY;
//#     }
//#     static boolean GameTrackBall (int iDir) {
//#         if ((iDir == TRACK_LEFT || iDir == TRACK_RIGHT) && ms_iTrackDirX == iDir) {
//#             ms_iTrackDirX = 0;
//#             HandleInput();
//#             return true;
//#         }
//#         if ((iDir == TRACK_UP || iDir == TRACK_DOWN) && ms_iTrackDirY == iDir) {
//#             ms_iTrackDirY = 0;
//#             HandleInput();
//#             return true;
//#         }
//#         return false;
//#     }
//# 
//#     protected boolean navigationClick(int _iStatus, int _iTime) {
//#         ms_iKeyInt_Map |= 1<<KEYINT_FIRE;
//#         HandleInput();
//#         return true;
//#     }
//#     protected boolean navigationUnclick(int _iStatus, int _iTime) {
//#         ms_iKeyInt_Map &= ~(1 << KEYINT_FIRE);
//#         HandleInput();
//#         return true;
//#     }
//# 
//#     protected boolean navigationMovement(int dx, int dy, int status, int time) {
//# 
//#         ms_iPrevTrackPointerX = ms_iTrackPointerX;
//#         ms_iPrevTrackPointerY = ms_iTrackPointerY;
//#         
//#         //Trackball.setFilter(Trackball.FILTER_XY_SNAP);
//#         //setTrackballFilter(Trackball.FILTER_XY_SNAP);
//#         
//#         ms_iTrackPointerX = dx;
//#         ms_iTrackPointerY = dy;
//# 
//#         // direct control
//#         if (dx < 0)
//#             ms_iTrackDirX = TRACK_LEFT;
//#         else if (dx > 0)
//#             ms_iTrackDirX = TRACK_RIGHT;
//# 
//#         if (dy < 0)
//#             ms_iTrackDirY = TRACK_UP;
//#         else if (dy > 0)
//#             ms_iTrackDirY = TRACK_DOWN;
//# 
//#         // key simulate control
//#         //if (ms_iTrackLastFrame+(Define.FPS>>1) < ms_iFrame) {
//#             if (dx < 0)
//#                 ms_iKeyInt_Map |= 1<<KEYINT_LEFT;
//#             else if (dx > 0)
//#                 ms_iKeyInt_Map |= 1<<KEYINT_RIGHT;
//# 
//#             if (dy < 0)
//#                 ms_iKeyInt_Map |= 1<<KEYINT_UP;
//#             else if (dy > 0)
//#                 ms_iKeyInt_Map |= 1<<KEYINT_DOWN;
//#             
//#         //}
//#         ms_iTrackLastFrame = ms_iFrame;
//#     
//#         HandleInput();
//#         return true;
//#     }
//# 
//#     public boolean keyDown (int _iKeyCode, int _iTime) {
//# 
//#         boolean bKeyPressed = false;
//#         
//#         // ## LEFT SOFTKEY ##
//#         if (_iKeyCode == KEYCODE_SK_LEFT) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_SKLEFT;
//#             bKeyPressed = true;
//#         }
//#         // ## RIGHT SOFTKEY ##
//#         else if (_iKeyCode == KEYCODE_SK_RIGHT || _iKeyCode == KEYCODE_SK_BACK) {
//#             ms_iKeyInt_Map |= 1<<KEYINT_SKRIGHT;
//#             bKeyPressed = true;
//#         }
//#         
//#         char iChar = (char) Keypad.key(_iKeyCode);
//#         iChar = (""+iChar).toLowerCase().charAt(0);
//#         
//#         if (QWERTY) {
//#             if      (iChar == QK[0]  || iChar == '1') { ms_iKeyInt_Map |= 1 << KEYINT_1; bKeyPressed = true; }
//#             else if (iChar == QK[1]  || iChar == '2') { ms_iKeyInt_Map |= 1 << KEYINT_2; bKeyPressed = true; }
//#             else if (iChar == QK[2]  || iChar == '3') { ms_iKeyInt_Map |= 1 << KEYINT_3; bKeyPressed = true; }
//#             else if (iChar == QK[3]  || iChar == '4') { ms_iKeyInt_Map |= 1 << KEYINT_4; bKeyPressed = true; }
//#             else if (iChar == QK[4]  || iChar == '5') { ms_iKeyInt_Map |= 1 << KEYINT_5; bKeyPressed = true; }
//#             else if (iChar == QK[5]  || iChar == '6') { ms_iKeyInt_Map |= 1 << KEYINT_6; bKeyPressed = true; }
//#             else if (iChar == QK[6]  || iChar == '7') { ms_iKeyInt_Map |= 1 << KEYINT_7; bKeyPressed = true; }
//#             else if (iChar == QK[7]  || iChar == '8') { ms_iKeyInt_Map |= 1 << KEYINT_8; bKeyPressed = true; }
//#             else if (iChar == QK[8]  || iChar == '9') { ms_iKeyInt_Map |= 1 << KEYINT_9; bKeyPressed = true; }
//#             else if (iChar == QK[9]  || iChar == '0') { ms_iKeyInt_Map |= 1 << KEYINT_0; bKeyPressed = true; }
//#             else if (iChar == QK[10] || iChar == '#') { ms_iKeyInt_Map |= 1 << KEYINT_POUND; bKeyPressed = true; }
//#             else if (iChar == QK[11] || iChar == '*') { ms_iKeyInt_Map |= 1 << KEYINT_STAR; bKeyPressed = true; }
//#             
//#         } else {
//# 
//#             switch (iChar){
//#                 case (int)'q':  case (int)'w':  case (int)'!':  ms_iKeyInt_Map |= 1<<KEYINT_Q; break;
//#                 case (int)'a':  case (int)'s':  case (int)'?':  ms_iKeyInt_Map |= 1<<KEYINT_A; break;
//#                 case (int)'z':  case (int)'x':  case (int)'@':  ms_iKeyInt_Map |= 1<<KEYINT_Z; break;
//#                 case (int)'o':  case (int)'p':  case (int)'.':  ms_iKeyInt_Map |= 1<<KEYINT_O; break;
//#                 case (int)'l':  case (int)',':  ms_iKeyInt_Map |= 1<<KEYINT_L; break;
//#                 
//#                 case (int)'1':  case (int)'e':  case (int)'r':  ms_iKeyInt_Map |= 1<<KEYINT_1; bKeyPressed = true; break;
//#                 case (int)'2':  case (int)'t':  case (int)'y':  ms_iKeyInt_Map |= 1<<KEYINT_2; bKeyPressed = true; break;
//#                 case (int)'3':  case (int)'u':  case (int)'i':  ms_iKeyInt_Map |= 1<<KEYINT_3; bKeyPressed = true; break;
//#                 case (int)'4':  case (int)'d':  case (int)'f':  ms_iKeyInt_Map |= 1<<KEYINT_4; bKeyPressed = true; break;
//#                 case (int)'5':  case (int)'g':  case (int)'h':  ms_iKeyInt_Map |= 1<<KEYINT_5; bKeyPressed = true; break;
//#                 case (int)'6':  case (int)'j':  case (int)'k':  ms_iKeyInt_Map |= 1<<KEYINT_6; bKeyPressed = true; break;
//#                 case (int)'7':  case (int)'c':  case (int)'v':  ms_iKeyInt_Map |= 1<<KEYINT_7; bKeyPressed = true; break;
//#                 case (int)'8':  case (int)'b':  case (int)'n':  ms_iKeyInt_Map |= 1<<KEYINT_8; bKeyPressed = true; break;
//#                 case (int)'9':  case (int)'m':  ms_iKeyInt_Map |= 1<<KEYINT_9; bKeyPressed = true; break;
//#                 case (int)'0':  case (int)' ':  ms_iKeyInt_Map |= 1<<KEYINT_0; bKeyPressed = true; break;
//#             }
//#         }
//#         HandleInput();
//#         return bKeyPressed;
//#     }
//# 
//#     public boolean keyUp(int _iKeyCode, int _iTime) {
//# 
//#         boolean bKeyPressed = false;
//#         
//#         // ## LEFT SOFTKEY ##
//#         if (_iKeyCode == KEYCODE_SK_LEFT) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
//#             bKeyPressed = true;
//#         } // ## RIGHT SOFTKEY ##
//#         else if (_iKeyCode == KEYCODE_SK_RIGHT || _iKeyCode == KEYCODE_SK_BACK) {
//#             ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
//#             bKeyPressed = true;
//#         }
//# 
//#         char iChar = (char) Keypad.key(_iKeyCode);
//#         iChar = (""+iChar).toLowerCase().charAt(0);
//# 
//#         if (QWERTY) {
//# 
//#             if      (iChar == QK[0]  || iChar == '1') { ms_iKeyInt_Map &= ~(1 << KEYINT_1); bKeyPressed = true; }
//#             else if (iChar == QK[1]  || iChar == '2') { ms_iKeyInt_Map &= ~(1 << KEYINT_2); bKeyPressed = true; }
//#             else if (iChar == QK[2]  || iChar == '3') { ms_iKeyInt_Map &= ~(1 << KEYINT_3); bKeyPressed = true; }
//#             else if (iChar == QK[3]  || iChar == '4') { ms_iKeyInt_Map &= ~(1 << KEYINT_4); bKeyPressed = true; }
//#             else if (iChar == QK[4]  || iChar == '5') { ms_iKeyInt_Map &= ~(1 << KEYINT_5); bKeyPressed = true; }
//#             else if (iChar == QK[5]  || iChar == '6') { ms_iKeyInt_Map &= ~(1 << KEYINT_6); bKeyPressed = true; }
//#             else if (iChar == QK[6]  || iChar == '7') { ms_iKeyInt_Map &= ~(1 << KEYINT_7); bKeyPressed = true; }
//#             else if (iChar == QK[7]  || iChar == '8') { ms_iKeyInt_Map &= ~(1 << KEYINT_8); bKeyPressed = true; }
//#             else if (iChar == QK[8]  || iChar == '9') { ms_iKeyInt_Map &= ~(1 << KEYINT_9); bKeyPressed = true; }
//#             else if (iChar == QK[9]  || iChar == '0') { ms_iKeyInt_Map &= ~(1 << KEYINT_0); bKeyPressed = true; }
//#             else if (iChar == QK[10] || iChar == '#') { ms_iKeyInt_Map &= ~(1 << KEYINT_POUND); bKeyPressed = true; }
//#             else if (iChar == QK[11] || iChar == '*') { ms_iKeyInt_Map &= ~(1 << KEYINT_STAR); bKeyPressed = true; }
//# 
//#         } else {
//# 
//#             switch (iChar) {
//#                 case (int)'q':  case (int)'w':  case (int)'!':  ms_iKeyInt_Map &= ~(1 << KEYINT_Q); bKeyPressed = true; break;
//#                 case (int)'a':  case (int)'s':  case (int)'?':  ms_iKeyInt_Map &= ~(1 << KEYINT_A); bKeyPressed = true; break;
//#                 case (int)'z':  case (int)'x':  case (int)'@':  ms_iKeyInt_Map &= ~(1 << KEYINT_Z); bKeyPressed = true; break;
//#                 case (int)'o':  case (int)'p':  case (int)'.':  ms_iKeyInt_Map &= ~(1 << KEYINT_O); bKeyPressed = true; break;
//#                 case (int)'l':  case (int)',':  ms_iKeyInt_Map &= ~(1 << KEYINT_L); bKeyPressed = true; break;
//#                     
//#                 case (int)'1':  case (int)'e':  case (int)'r':  ms_iKeyInt_Map &= ~(1 << KEYINT_1); bKeyPressed = true; break;
//#                 case (int)'2':  case (int)'t':  case (int)'y':  ms_iKeyInt_Map &= ~(1 << KEYINT_2); bKeyPressed = true; break;
//#                 case (int)'3':  case (int)'u':  case (int)'i':  ms_iKeyInt_Map &= ~(1 << KEYINT_3); bKeyPressed = true; break;
//#                 case (int)'4':  case (int)'d':  case (int)'f':  ms_iKeyInt_Map &= ~(1 << KEYINT_4); bKeyPressed = true; break;
//#                 case (int)'5':  case (int)'g':  case (int)'h':  ms_iKeyInt_Map &= ~(1 << KEYINT_5); bKeyPressed = true; break;
//#                 case (int)'6':  case (int)'j':  case (int)'k':  ms_iKeyInt_Map &= ~(1 << KEYINT_6); bKeyPressed = true; break;
//#                 case (int)'7':  case (int)'c':  case (int)'v':  ms_iKeyInt_Map &= ~(1 << KEYINT_7); bKeyPressed = true; break;
//#                 case (int)'8':  case (int)'b':  case (int)'n':  ms_iKeyInt_Map &= ~(1 << KEYINT_8); bKeyPressed = true; break;
//#                 case (int)'9':  case (int)'m':  ms_iKeyInt_Map &= ~(1 << KEYINT_9); bKeyPressed = true; break;
//#                 case (int)'0':  case (int)' ':  ms_iKeyInt_Map &= ~(1 << KEYINT_0); bKeyPressed = true; break;
//#             }
//#         }
//#         HandleInput();
//#         return bKeyPressed;
//#     }
//#     
    //#elif DOJA
//#     public void processEvent(int type, int keyCode) {
//# 
//#         /*
//#         if (type == Display.POINTER_MOVED_EVENT) {
//#             ms_iScreenTouched_X = PointingDevice.getX();
//#             ms_iScreenTouched_Y = PointingDevice.getY();
//#             //ms_iScreenTouched_Map =
//#         }*/
//#         // key pressed
//#         if (type == Display.KEY_PRESSED_EVENT) {
//# 
//#             // ## LEFT SOFTKEY ##
//#             if (keyCode == Display.KEY_SOFT1) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_SKLEFT;
//#             } // ## RIGHT SOFTKEY ##
//#             else if (keyCode == Display.KEY_SOFT2) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_SKRIGHT;
//#             } // ## POUND ##
//#             else if (keyCode == Display.KEY_POUND) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_POUND;
//#             } // ## STAR ##
//#             else if (keyCode == Display.KEY_ASTERISK) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_STAR;
//#             } // ## KEY "0" ##
//#             else if (keyCode == Display.KEY_0) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_0;
//#             } // ## KEY "1" ##
//#             else if (keyCode == Display.KEY_1) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_1;
//#             } // ## KEY "2" ##
//#             else if (keyCode == Display.KEY_2) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_2;
//#             } // ## KEY "3" ##
//#             else if (keyCode == Display.KEY_3) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_3;
//#             } // ## KEY "4" ##
//#             else if (keyCode == Display.KEY_4) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_4;
//#             } // ## KEY "5" ##
//#             else if (keyCode == Display.KEY_5) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_5;
//#             } // ## KEY "6" ##
//#             else if (keyCode == Display.KEY_6) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_6;
//#             } // ## KEY "7" ##
//#             else if (keyCode == Display.KEY_7) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_7;
//#             } // ## KEY "8" ##
//#             else if (keyCode == Display.KEY_8) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_8;
//#             } // ## KEY "9" ##
//#             else if (keyCode == Display.KEY_9) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_9;
//#             }
//# 
//#             // ## UP ##
//#             else if (keyCode == Display.KEY_UP) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_UP;
//#             } // ## DOWN ##
//#             else if (keyCode == Display.KEY_DOWN) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_DOWN;
//#             } // ## LEFT ##
//#             else if (keyCode == Display.KEY_LEFT) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_LEFT;
//#             } // ## RIGHT ##
//#             else if (keyCode == Display.KEY_RIGHT) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_RIGHT;
//#             } // ## FIRE ##
//#             else if (keyCode == Display.KEY_SELECT) {
//#                 ms_iKeyInt_Map |= 1<<KEYINT_FIRE;
//#             }
//# 
//#         // key released
//#         } else if (type == Display.KEY_RELEASED_EVENT) {
//# 
//#             // ## LEFT SOFTKEY ##
//#             if (keyCode == Display.KEY_SOFT1) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_SKLEFT);
//#             } // ## RIGHT SOFTKEY ##
//#             else if (keyCode == Display.KEY_SOFT2) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_SKRIGHT);
//#             } // ## POUND ##
//#             else if (keyCode == Display.KEY_POUND) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_POUND);
//#             } // ## STAR ##
//#             else if (keyCode == Display.KEY_ASTERISK) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_STAR);
//#             } // ## KEY "0" ##
//#             else if (keyCode == Display.KEY_0) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_1);
//#             } // ## KEY "1" ##
//#             else if (keyCode == Display.KEY_1) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_1);
//#             } // ## KEY "2" ##
//#             else if (keyCode == Display.KEY_2) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_2);
//#             } // ## KEY "3" ##
//#             else if (keyCode == Display.KEY_3) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_3);
//#             } // ## KEY "4" ##
//#             else if (keyCode == Display.KEY_4) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_4);
//#             } // ## KEY "5" ##
//#             else if (keyCode == Display.KEY_5) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_5);
//#             } // ## KEY "6" ##
//#             else if (keyCode == Display.KEY_6) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_6);
//#             } // ## KEY "7" ##
//#             else if (keyCode == Display.KEY_7) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_7);
//#             } // ## KEY "8" ##
//#             else if (keyCode == Display.KEY_8) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_8);
//#             } // ## KEY "9" ##
//#             else if (keyCode == Display.KEY_9) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_9);
//#             } // ## UP ##
//#             else if (keyCode == Display.KEY_UP) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_UP);
//#             } // ## DOWN ##
//#             else if (keyCode == Display.KEY_DOWN) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_DOWN);
//#             } // ## LEFT ##
//#             else if (keyCode == Display.KEY_LEFT) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_LEFT);
//#             } // ##RIGHT ##
//#             else if (keyCode == Display.KEY_RIGHT) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_RIGHT);
//#             } // ## FIRE ##
//#             else if ((keyCode == Display.KEY_SELECT)) {
//#                 ms_iKeyInt_Map &= ~(1 << KEYINT_FIRE);
//#             }
//#         }
//#         HandleInput();
//#     }
    //#endif
   
    
    // Flexible input handling system
    static final byte KEYINT_UP = 0;
    static final byte KEYINT_DOWN = 1;
    static final byte KEYINT_LEFT = 2;
    static final byte KEYINT_RIGHT = 3;
    static final byte KEYINT_FIRE = 4;
    static final byte KEYINT_SKLEFT = 5;  // Left softkey
    static final byte KEYINT_SKRIGHT = 6;  // Right softkey
    static final byte KEYINT_POUND = 7;
    static final byte KEYINT_STAR = 8;
    static final byte KEYINT_CLEAR = 9;
    static final byte KEYINT_0 = 10;
    static final byte KEYINT_1 = 11;
    static final byte KEYINT_2 = 12;
    static final byte KEYINT_3 = 13;
    static final byte KEYINT_4 = 14;
    static final byte KEYINT_5 = 15;
    static final byte KEYINT_6 = 16;
    static final byte KEYINT_7 = 17;
    static final byte KEYINT_8 = 18;
    static final byte KEYINT_9 = 19;
    // BB keys
    static final byte KEYINT_Q = 20;
    static final byte KEYINT_A = 21;
    static final byte KEYINT_Z = 22;
    static final byte KEYINT_O = 23;
    static final byte KEYINT_L = 24;
    static int ms_iKeyInt_Map;  // Key bitmap. If a bit is 1, its corresponding direction/button is being pressed
    static char ms_iKeyChar_Map;
    static char ms_iKeyChar_MapReleased;

    static void ResetKeys() {
        ms_iKeyInt_Map = 0;
        ms_iKeyChar_Map = 0;
    }
    public static void ResetTouch(){
    	 ms_iScreenTouched_X[0] = -1;
    	 ms_iScreenTouched_Y[0] = -1;
    	 ms_iScreenOrigin_X[0] = -1;
    	 ms_iScreenOrigin_Y[0] = -1;
    	 ms_iScreenTouched_X[1] = -1;
    	 ms_iScreenTouched_Y[1] = -1;
    	 ms_iScreenOrigin_X[1] = -1;
    	 ms_iScreenOrigin_Y[1] = -1;
    	 ms_bSoftLeft = false;
    	 ms_bSoftRight = false;
    	 ms_bSoftMedium = false;
    }

    static boolean GameKeyPressed(byte _iGameKeyCode, boolean _bResetGameKey) {
        boolean bPressed = ((ms_iKeyInt_Map & (1 << _iGameKeyCode)) != 0);
        if (bPressed && _bResetGameKey) {
            ms_iKeyInt_Map ^= (1 << _iGameKeyCode);
        }
        
        return bPressed;
    }

    // TOUCH SCREEN support
    static boolean ms_bScreenTouchReset;
    static boolean ms_bTouchScreenPressed;
    static boolean ms_bTouchSuperShot;
    static int ms_iTouchScreenHook_X;
    static int ms_iTouchScreenHook_Y;
    static int ms_iMovedScreenHook_X;
    static int ms_iMovedScreenHook_Y;
    static int ms_iTouchScreenFrames;
    static int ms_iCharIncX, ms_iCharIncY;
    static final int TOUCH_MAX_INC = ((((16 << Define.FP_BITS)) * Define.MAXFPS) / (Define.FPS));
    
    
    static boolean TOUCHSCREEN_SUPPORTED = false; //true;
    static final int ACTION_UP = 0;
    static final int ACTION_DOWN = 1;
    static final int ACTION_MOVE = 2;
    static int[] ms_iScreenTouched_X = new int[2]; 
    static int[] ms_iScreenTouched_Y = new int[2]; 
    static int[] ms_iScreenOrigin_X = new int[2]; 
    static int[] ms_iScreenOrigin_Y = new int[2];
    static int[] ms_iScreenTouched_Map = {ACTION_UP, ACTION_UP};
    static int ms_iScreenTouched_Frames = 0;

    private void TouchScreenCheck() {

        TOUCHSCREEN_SUPPORTED = false;

        //#if DOJA
//#         TOUCHSCREEN_SUPPORTED = com.nttdocomo.opt.ui.PointingDevice.isAvailable();
//#         com.nttdocomo.opt.ui.PointingDevice.setEnabled(true);
        //#elif RIM
        //#if RIM >= "6.0"
//#         TOUCHSCREEN_SUPPORTED = net.rim.device.api.ui.Touchscreen.isSupported();
        //#endif
        //#elif !i475
        TOUCHSCREEN_SUPPORTED = hasPointerEvents() && hasPointerMotionEvents();
        //#endif
    }

    
    //#if !s40asha311 && RIM < "5.0"
    protected void pointerPressed(int _iX, int _iY) {
        ms_iScreenTouched_Map[0] = ACTION_DOWN;
        ms_iScreenTouched_X[0] = _iX;
        ms_iScreenTouched_Y[0] = _iY;
        ms_iScreenTouched_Frames++;
        ms_iScreenOrigin_X[0] = ms_iScreenTouched_X[0];
        ms_iScreenOrigin_Y[0] = ms_iScreenTouched_Y[0];
        
        //#if BackBuffer == "keepPortrait"
//#         if (getWidth() > getHeight()) {
//#             ms_iScreenTouched_X[0] = Define.SIZEX - _iY;
//#             ms_iScreenTouched_Y[0] = _iX;
//#         }
        //#endif
        HandleInput();
    }
    
    protected final void pointerDragged(int _iX, int _iY) {
        ms_iScreenTouched_X[0] = _iX;
        ms_iScreenTouched_Y[0] = _iY;
            
        if ((Math.abs(ms_iScreenOrigin_X[0] - ms_iScreenTouched_X[0])) > (Define.SIZEX16) ||
            (Math.abs(ms_iScreenOrigin_Y[0] - ms_iScreenTouched_Y[0])) > (Define.SIZEY16)) {
            ms_iScreenTouched_Map[0] = ACTION_MOVE;
            ms_iScreenTouched_Frames++;
        }
                
        //#if BackBuffer == "keepPortrait"
//#         if (getWidth() > getHeight()) {
//#             ms_iScreenTouched_X[0] = Define.SIZEX - _iY;
//#             ms_iScreenTouched_Y[0] = _iX;
//#         }
        //#endif
        HandleInput();
    }

    protected void pointerReleased(int _iX, int _iY) {
        ms_iScreenTouched_Map[0] = ACTION_UP;
        ms_iScreenTouched_X[0] = _iX;
        ms_iScreenTouched_Y[0] = _iY;
        ms_iScreenTouched_Frames = 0;
        
        //#if BackBuffer == "keepPortrait"
//#         if (getWidth() > getHeight()) {
//#             ms_iScreenTouched_X[0] = Define.SIZEX - _iY;
//#             ms_iScreenTouched_Y[0] = _iX;
//#         }
        //#endif
        HandleInput();
    }
    
    //#endif
    
    //#if API == "Samsung" 
//#     protected void mTouchPressed(int _iX, int _iY, int index) {
//#         if (index == 0) { 
//#             ms_iScreenTouched_Map[0] = ACTION_DOWN;
//#             ms_iScreenTouched_X[0] = _iX;
//#             ms_iScreenTouched_Y[0] = _iY;
//#             ms_iScreenTouched_Frames = 0;
//#             ms_iScreenOrigin_X[0] = ms_iScreenTouched_X[0];
//#             ms_iScreenOrigin_Y[0] = ms_iScreenTouched_Y[0];
//#             HandleInput();
//#         }
//#     }
//#     
//#     protected void mTouchMove(int _iX, int _iY, int index) {
//#         if (index == 0) { 
//#             ms_iScreenTouched_Map[0] = ACTION_MOVE;
//#             ms_iScreenTouched_X[0] = _iX;
//#             ms_iScreenTouched_Y[0] = _iY;
//#             ms_iScreenTouched_Frames++;
//#             HandleInput();
//#         }
//#     }
//#     
//#     protected void mTouchRelease(int _iX, int _iY, int index) {
//#         if (index == 0) { 
//#             ms_iScreenTouched_Map[0] = ACTION_UP;
//#             ms_iScreenTouched_X[0] = _iX;
//#             ms_iScreenTouched_Y[0] = _iY;
//#             ms_iScreenTouched_Frames = 0;
//#             HandleInput();
//#         }
//#     }
    //#endif
    
    //#if s40asha311
//#     
//#     //public static int[] ms_multiTouch;
//#     
//#     
//# //    static int[] ms_iScreenTouched_X = new int[2]; 
//# //    static int[] ms_iScreenTouched_Y = new int[2]; 
//# //    static int[] ms_iScreenOrigin_X = new int[2]; 
//# //    static int[] ms_iScreenOrigin_Y = new int[2];
//#     
//#     public void pointersChanged(int[] pointerIds){
//#         int state;
//# 
//#         // Go through the array
//#         //for(int i=0; i < pointerIds.length; i++){
//#         for(int i=0; i < MAX_POINTER_SUPORTED; i++){
//# 
//#             // Get pointerId.
//#             //pointerId = pointerIds[i];
//# 
//#             // Read the pointer state.
//#             state = MultipointTouch.getState(i);
//# 
//#             // Read the pointer X and Y coordinate.
//#             ms_iScreenTouched_X[i] = MultipointTouch.getX(i);
//#             ms_iScreenTouched_Y[i] = MultipointTouch.getY(i);
//#             
//#             
//# 
//#             switch (state) {
//#                 case 1: //MultipointTouch.POINTER_PRESSED:
//#                     pointerPressed(ms_iScreenTouched_X[i], ms_iScreenTouched_Y[i]);
//#                     ms_iScreenTouched_Map[i] = ACTION_DOWN;
//#                     ms_iScreenTouched_Frames++;
//#                     
//#                     ms_iScreenOrigin_X[i] = ms_iScreenTouched_X[i] ;
//#                     ms_iScreenOrigin_Y[i] = ms_iScreenTouched_Y[i] ;
//#                     HandleInput();
//#                     break;
//# 
//#                 case 3: //MultipointTouch.POINTER_DRAGGED:
//#                     pointerDragged(ms_iScreenTouched_X[i], ms_iScreenTouched_Y[i]);
//#                     ms_iScreenTouched_Map[i] = ACTION_MOVE;
//#                     ms_iScreenTouched_Frames++;
//#                     HandleInput();
//#                     break;
//# 
//#                 case 2: //MultipointTouch.POINTER_RELEASED:
//#                     pointerReleased(ms_iScreenTouched_X[i], ms_iScreenTouched_Y[i]);
//#                     ms_iScreenTouched_Map[i] = ACTION_UP;
//#                     ms_iScreenTouched_Frames = 0;
//#                     HandleInput();
//#                     break;
//#             }
//#         }
//#        
//#     }
    //#endif
    
    //#if RIM >= "5.0"
//#     private static int ms_iEvent;
//#     protected boolean touchEvent(TouchEvent message) {
//#         
//#         //message.getMovePoints();
//#         ms_iEvent = message.getEvent();
//#         switch(ms_iEvent) {
//#             case TouchEvent.DOWN:
//#                 ms_iScreenTouched_Frames = 0;
//#                 
//#                 ms_iScreenTouched_Map[0] = ACTION_DOWN;
//#                 if(message.getX(1) != -1)ms_iScreenTouched_X[0] = message.getX(1);
//#                 if(message.getY(1) != -1)ms_iScreenTouched_Y[0] = message.getY(1);
//#                 
//#                 ms_iScreenOrigin_X[0] = ms_iScreenTouched_X[0];
//#                 ms_iScreenOrigin_Y[0] = ms_iScreenTouched_Y[0];
//#                 
//#                 ms_iScreenTouched_Map[1] = ACTION_DOWN;
//#                 if(message.getX(2) != -1)ms_iScreenTouched_X[1] = message.getX(2);
//#                 if(message.getY(2) != -1)ms_iScreenTouched_Y[1] = message.getY(2);
//#                 
//#                 ms_iScreenOrigin_X[1] = ms_iScreenTouched_X[1];
//#                 ms_iScreenOrigin_Y[1] = ms_iScreenTouched_Y[1];
//#     
//#                 HandleInput();
//#                 return true;
//# 
//#             case TouchEvent.MOVE:
//#                 ms_iScreenTouched_Frames++;
//#                 
//#                 ms_iScreenTouched_Map[0] = ACTION_MOVE;
//#                 if(message.getX(1) != -1)ms_iScreenTouched_X[0] = message.getX(1);
//#                 if(message.getY(1) != -1)ms_iScreenTouched_Y[0] = message.getY(1);
//#                 
//#                 ms_iScreenTouched_Map[1] = ACTION_MOVE;
//#                 if(message.getX(2) != -1)ms_iScreenTouched_X[1] = message.getX(2);
//#                 if(message.getY(2) != -1)ms_iScreenTouched_Y[1] = message.getY(2);
//#     
//#                 HandleInput();
//#                 return true;
//# 
//#             case TouchEvent.UP:
//#                 ms_iScreenTouched_Frames = 0;     
//#                  
//#                 ms_iScreenTouched_Map[0] = ACTION_UP;
//#                 if(message.getX(1) != -1)ms_iScreenTouched_X[0] = message.getX(1);
//#                 if(message.getY(1) != -1)ms_iScreenTouched_Y[0] = message.getY(1);
//#                 
//#                 ms_iScreenTouched_Map[1] = ACTION_UP;
//#                 if(message.getX(2) != -1)ms_iScreenTouched_X[1] = message.getX(2);
//#                 if(message.getY(2) != -1)ms_iScreenTouched_Y[1] = message.getY(2);
//#     
//#                 HandleInput();
//#                 return true;
//#         }
//#         
//#         
//#         return false;
//#     }
    //#endif
    
    static boolean GameScreenTouched (boolean _bResetTouch, boolean _bGetMove) {
        boolean bPressed =
            ((ms_iScreenTouched_Map[0]==ACTION_DOWN) ||
            ((ms_iScreenTouched_Map[0]==ACTION_MOVE) && ms_iScreenTouched_Frames < 5) ||
            (ms_iScreenTouched_Map[0]==ACTION_MOVE && _bGetMove)) &&
            TOUCHSCREEN_SUPPORTED;

        if (bPressed && _bResetTouch && !_bGetMove) {
            ms_iScreenTouched_Map[0]=ACTION_UP;
            ms_iScreenTouched_Frames = 5;
        }

        return bPressed;
    }

    static boolean GameScreenTouched (int _iX0, int _iY0, int _iX1, int _iY1,
        boolean _bResetTouch, boolean _bGetMove) {

        boolean bPressed =
            ((ms_iScreenTouched_Map[0]==ACTION_DOWN) ||
            ((ms_iScreenTouched_Map[0]==ACTION_MOVE) && ms_iScreenTouched_Frames < 5) ||
            (ms_iScreenTouched_Map[0]==ACTION_MOVE && _bGetMove)) &&
            TOUCHSCREEN_SUPPORTED &&

            ((ms_iScreenTouched_X[0] > _iX0 && ms_iScreenTouched_X[0] < _iX1) &&
            (ms_iScreenTouched_Y[0] > _iY0 && ms_iScreenTouched_Y[0] < _iY1));

        if (bPressed && _bResetTouch && !_bGetMove) {
            ms_iScreenTouched_Map[0] = ACTION_UP;
            ms_iScreenTouched_Frames = 5;
        }

        return bPressed;
    }
 
    static final int TOUCH_L_SOFTK = 0;
    static final int TOUCH_R_SOFTK = 1;
    static final int TOUCH_M_SOFTK = 2;
    static boolean ms_bSoftLeft;
    static boolean ms_bSoftRight;
    static boolean ms_bSoftMedium;

    
    static int TOUCH_SOFTKEY_W = 0;
    static int TOUCH_SOFTKEY_H = 0;
    
    static boolean GameScreenSoftkey(int _iSoftKey) {
        TOUCH_SOFTKEY_W = GfxManager.ms_vImage[GfxManager.GFXID_SK_MENU].getWidth()>>1;
        TOUCH_SOFTKEY_H = GfxManager.ms_vImage[GfxManager.GFXID_SK_MENU].getHeight()>>1;
        
        boolean bPressed = false;

        if (ms_iScreenTouched_Map[0] == ACTION_DOWN || 
            ms_iScreenTouched_Map[0] == ACTION_MOVE) {

            if (_iSoftKey == TOUCH_M_SOFTK) {
                ms_bSoftMedium = 
                        ((ms_iScreenTouched_X[0] > TOUCH_SOFTKEY_W) && 
                        (ms_iScreenTouched_X[0] < Define.SIZEX - TOUCH_SOFTKEY_W) && 
                        (ms_iScreenTouched_Y[0] > Define.SIZEY - TOUCH_SOFTKEY_H));
                
            } else if (_iSoftKey == TOUCH_L_SOFTK) {
                ms_bSoftLeft = ((ms_iScreenTouched_X[0] < TOUCH_SOFTKEY_W) && 
                        (ms_iScreenTouched_Y[0] > Define.SIZEY - TOUCH_SOFTKEY_H) &&
                        (ms_iScreenOrigin_X[0] < TOUCH_SOFTKEY_W) && 
                        (ms_iScreenOrigin_Y[0] > Define.SIZEY - TOUCH_SOFTKEY_H));
                
            } else if (_iSoftKey == TOUCH_R_SOFTK) {
                ms_bSoftRight = ((ms_iScreenTouched_X[0] > Define.SIZEX - TOUCH_SOFTKEY_W) && 
                        (ms_iScreenTouched_Y[0] > Define.SIZEY - TOUCH_SOFTKEY_H)) && 
                        ((ms_iScreenOrigin_X[0] > Define.SIZEX - TOUCH_SOFTKEY_W) && 
                        (ms_iScreenOrigin_Y[0] > Define.SIZEY - TOUCH_SOFTKEY_H));
            }
        } else if (ms_iScreenTouched_Map[0] == ACTION_UP) {

            if (_iSoftKey == TOUCH_M_SOFTK && ms_bSoftMedium) {
                ms_bSoftMedium = false;
                bPressed = true;
                
            } else if (_iSoftKey == TOUCH_L_SOFTK && ms_bSoftLeft) {
                ms_bSoftLeft = false;
                bPressed = true;
                
            } else if (_iSoftKey == TOUCH_R_SOFTK && ms_bSoftRight) {
                ms_bSoftRight = false;
                bPressed = true;
            }
        }

        return bPressed;
    }

    private static void HandleInput() {
        if (Define.ms_iState < Define.ST_GAME_INIT)
            ModeMenu.HandleInput();
        else
            ModeGame.HandleInput();

        //#if Debug
//#             Debug.DebugHandleKeys();
        //#endif
    }

    // VIBRATION support
    static void VibrationCheck() {

        //#if MIDP=="1.0" || z140v || s40dp2long
//#         VIBRATION_SUPPORTED = false;
        //#elif MIDP=="2.0"
        VIBRATION_SUPPORTED = javax.microedition.lcdui.Display.getDisplay(ms_vMIDlet).vibrate(0);
        //#elif RIM >= "4.5"
//#         VIBRATION_SUPPORTED = net.rim.device.api.system.Alert.isVibrateSupported();
        //#else
//#         VIBRATION_SUPPORTED = false;
        //#endif

        //#if API=="Nokia"
//#         if (!VIBRATION_SUPPORTED) {
//#             try {
//#                 DeviceControl.startVibra(1, 1);
//#                 VIBRATION_SUPPORTED = true;
//#                 DeviceControl.stopVibra();
//#             } catch (Exception ex) {
//#             }
//#         }
        //#endif
    }
    
    // Vibration
    public static boolean ms_bVibration = true;
    public static boolean VIBRATION_SUPPORTED = false;

    static void VibrationStart(int _iDuration) {
        try {
            if (VIBRATION_SUPPORTED && ms_bVibration) {
                //#if API=="Nokia"
//#                 DeviceControl.startVibra (50, _iDuration);
                //#elif MIDP=="2.0"
                javax.microedition.lcdui.Display.getDisplay(ms_vMIDlet).vibrate(_iDuration);
                //#elif RIM >= "4.5"
//#                 net.rim.device.api.system.Alert.startVibrate(_iDuration);
                //#endif
            }
        } catch (Exception e) {
        }
    }
    //////////////////////////////////////////////////////////////////////
    // Particles
    //////////////////////////////////////////////////////////////////////
    public static int ms_iMaxParticle = 0;
    public static final int PART_PRECISIONBITS = 8;
    public static final short MAX_PARTICLE = 64;
    public static final char[] ms_Particles = new char[MAX_PARTICLE];

    static {
        for (int p = 0; p < MAX_PARTICLE; p++) {
            ms_Particles[p] = (char) p;
        }
    }
    static int[] ms_pTime = new int[MAX_PARTICLE];
    static int[] ms_pTimeLimit = new int[MAX_PARTICLE];
    static int[] ms_pPosX = new int[MAX_PARTICLE];
    static int[] ms_pPosY = new int[MAX_PARTICLE];
    static int[] ms_pVelX = new int[MAX_PARTICLE];
    static int[] ms_pVelY = new int[MAX_PARTICLE];
    static int[] ms_pAccX = new int[MAX_PARTICLE];
    static int[] ms_pAccY = new int[MAX_PARTICLE];
    static int[] ms_pSizeX = new int[MAX_PARTICLE];
    static int[] ms_pSizeY = new int[MAX_PARTICLE];
    static int[][] ms_pColor = new int[MAX_PARTICLE][2];
    static byte[] ms_pType = new byte[MAX_PARTICLE];
    boolean paused = false;
    boolean paused2 = false;

    protected void hideNotify() {
        Main.Pause();
    }

    protected void showNotify() {
        Main.Unpause();
    }

    public int compare(byte[] bytes, byte[] bytes1) {
        return 0;
    }

    public static Random ms_Random = new Random(0);
    
    //Obtiene un randon entre el primer parametro(Numero menor) y el segundo parametro(numero mayor). Ambos incluidos.
    static int Random(int _i0, int _i1) {
        return _i0 + Math.abs(ms_Random.nextInt() % (1 + _i1 - _i0));
    }
    
    static int Random(int _iNumber) {
        if (_iNumber < 0) {
            return (ms_Random.nextInt() % -_iNumber);
        }
        try {
            return Math.abs(ms_Random.nextInt()) % _iNumber;
        } catch (Exception e) {
            //#if Debug
//#             e.printStackTrace();
            //#endif
            return 0;
        }
    }
    
    static void Pause () {
      //long lStartTime;
      
      if (!Define.ms_bPaused) {

         //#ifdef Debug
//#          Debug.DebugPostmortemMsg ("** GAME PAUSED");
         //#endif

         Define.ms_bPaused = true;
         
         while (SndManager.ms_bUpdatingSound) {
            try {
               Thread.sleep (150);
            } catch (Exception e) {}
         }

         SndManager.PauseMusic();
         
      }
   }
//   
    static void Unpause () {
        if (Define.ms_bPaused) {
            //#ifdef Debug
//#             Debug.DebugPostmortemMsg ("** GAME UNPAUSED");
            //#endif
            
            ms_XGraphics.m_bGetGraphics = false;
            Define.ms_bPaused = false;
            
            SndManager.UnpauseMusic();
            
        }
    }
    
    static void Repaints() {
        //#if RIM
//#         if (ms_XGraphics.getClipWidth() > 0 && ms_XGraphics.getClipHeight() > 0)
//#             ms_vMain.invalidate(ms_XGraphics.getClipX(), ms_XGraphics.getClipY(), ms_XGraphics.getClipWidth(), ms_XGraphics.getClipHeight());
        //#else
        //#if CanvasMode == "GameCanvas"
//#         ms_vMain.paint(null);
        //#else
        ms_vMain.repaint();
        ms_vMain.serviceRepaints();
        //#endif
        //#endif
        
    }

    static void RequestStateChange(int _iNewState) {
        Define.ms_iNewState = _iNewState;
        Define.ms_bNewState = true;
    }   
    
    static void InitState(int _iNewState) {
        try {
            Debug.DebugPostmortemMsg("===================================");
            Debug.DebugPostmortemMsg("** Initializing State " + _iNewState);

           
            Define.ms_bNoDraw = true;
            Define.ms_iPrevState = Define.ms_iState;
            Define.ms_iState = Define.ms_iNewState = _iNewState;
            Define.ms_iStateTicks = Define.ms_iStateFrames = 0;
            Define.ms_bNewState = false;
            
            if (Define.ms_iState < Define.ST_GAME_INIT)
                ModeMenu.InitState (_iNewState);
            else
                ModeGame.InitState(_iNewState);
            Debug.DebugPostmortemMsg("** State " + _iNewState + " initialized");
            Debug.DebugPostmortemMsg("===================================");

        } catch (Exception ex) {
            Debug.DebugPostmortemMsg("ERR InitState: St." + Define.ms_iState + " - Exception:" + ex.getMessage() + "(" + ex.toString() + ")");
        }

        Define.ms_bNoDraw = false;
    }
       
    // blackberry destroyApp
    public boolean onClose() {
        WolfRunner.ms_vInstance.quitApp();
        return true;
    }
    
    static int renderX(int _iX) {
        //#if ScreenWidth == "240"
        return _iX;
        //#else
//#         int iX = (((_iX) * Define.SIZEX) / Define.BASE_SIZEX);
//#         return iX;
        //#endif
    }

    static int renderY(int _iY) {
        //#if ScreenHeight == "320"
        return _iY;
        //#else
//#         int iY = (((_iY) * Define.SIZEY) / Define.BASE_SIZEY);
//#         return iY;
        //#endif
    }
    public static int averageDt;
    public void putDelta() {
        deltaTime = System.currentTimeMillis() - thisLoop;

        if (deltaTime > 150) {
            deltaTime = 150;
        } else if (deltaTime < 1) {
            deltaTime = 1;
        }
        thisLoop = System.currentTimeMillis();
        averageDt += (Math.min(deltaTime, 100) - averageDt) >> 1;
    }
}