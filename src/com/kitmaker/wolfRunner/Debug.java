/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kitmaker.wolfRunner;

import javak.microedition.lcdui.Font;
import javak.microedition.lcdui.Graphics;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Fran
 */
public class Debug {
    
    //#ifdef Debug
//#     private static RecordStore ms_RecordStorePM = null;  // PM = PostMortem
//#    
//#     private static final String POSTMORTEM_NAME = "PM";
//#     private static final int MAX_DEBUGSTRINGS = 200; //400;
//#     private static String[] ms_zDebugString = new String[MAX_DEBUGSTRINGS];
//#    
//#     private static final Font FONT_DEBUG = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
//#     private static final int DEBUG_FNT_HEIGHT = FONT_DEBUG.getHeight();
//#     private static final int DEBUG_NUM_LINES = (Define.SIZEY / DEBUG_FNT_HEIGHT) - 3;
//#    
//#     private static int ms_iDebugLine = 0;
//#     private static int ms_iDebugMsg_PosX = 0;
//#     private static int ms_iDebugMaxLine = 0;
    //#endif
    
    static boolean ms_bShowDebug = false;
    static boolean ms_bAllowLogging = true;   
    
    /**
     * Handle debug. <P> This function needs to be placed at the end of the
     * Canvas keyPressed method. <P>
     *
     * @param _g Graphics object.
     */
    public static void DebugHandleKeys() {
        //#ifdef Debug
//#         if (ms_bShowDebug && Main.GameKeyPressed(Main.KEYINT_FIRE, true)) {
//#             if (ms_bAllowLogging) {
//#                 DebugMsg("====== LOGGING DEACTIVATED ======");
//#                 ms_bAllowLogging = false;
//#             } else {
//#                 ms_bAllowLogging = true;
//#                 DebugMsg("------ LOGGING ACTIVATED ------");
//#             }
//#         }
//# 
//#         if (Main.GameKeyPressed(Main.KEYINT_0, true) || Main.GameKeyPressed(Main.KEYINT_STAR, true)
//#                 || Main.GameScreenTouched(0,0,Define.SIZEX4,Define.SIZEY4, false, true)) {
//#             ms_bShowDebug = (!ms_bShowDebug);
//#             Main.Repaints();
//#         }
//#         if (ms_bShowDebug) {
//#             if (Main.GameKeyPressed(Main.KEYINT_LEFT, true) || Main.GameKeyPressed(Main.KEYINT_4, true)) {
//#                 ms_iDebugMsg_PosX -= 60;
//#                 Main.Repaints();
//#             }
//#             if (Main.GameKeyPressed(Main.KEYINT_RIGHT, true) || Main.GameKeyPressed(Main.KEYINT_6, true)) {
//#                 ms_iDebugMsg_PosX += 60;
//#                 Main.Repaints();
//#             }
//#             if (Main.GameKeyPressed(Main.KEYINT_DOWN, false) || Main.GameKeyPressed(Main.KEYINT_2, false) || 
//#                 Main.GameScreenTouched(0,Define.SIZEY2,Define.SIZEX,Define.SIZEY, false, true)) {
//#                 ms_iDebugLine+=3;
//#                 if (ms_iDebugLine >= MAX_DEBUGSTRINGS) {
//#                     ms_iDebugLine = (MAX_DEBUGSTRINGS - 1);
//#                 }
//#                 Main.Repaints();
//#             }
//#             if (Main.GameKeyPressed(Main.KEYINT_UP, false) || Main.GameKeyPressed(Main.KEYINT_8, false) || 
//#                 Main.GameScreenTouched(0,Define.SIZEY4,Define.SIZEX,Define.SIZEY2, false, true)) {
//#                    
//#                 ms_iDebugLine-=3;
//#                 if (ms_iDebugLine < 0) {
//#                     ms_iDebugLine = 0;
//#                 }
//#                 Main.Repaints();
//#             }
//#         }
        //#endif
    }
    
    /**
     * Draw debug. 
     * <P>
     * This function needs to be placed at the end of the Canvas paint method.  
     * <P>
     * @param _g Graphics object.
     */

    public static void DebugDraw(Graphics _g) {
        //#ifdef Debug
//#         // Dibuja trazas
//#         if (ms_bShowDebug) {
//#             _g.setClip ( 0, 0, Define.SIZEX, Define.SIZEY );
//#             _g.setColor (0);
//#             _g.fillRect ( 0, 0, Define.SIZEX, Define.SIZEY );
//#             _g.setFont (Font.getFont (Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
//# 
//#             _g.setColor(0x00FFFFFF);
//#             for (int i = 0; i < 10; i++) {
//#                 if (((i + ms_iDebugLine) < MAX_DEBUGSTRINGS) && 
//#                 (ms_zDebugString[i + ms_iDebugLine] != null)) {
//#                 _g.drawString (ms_zDebugString[i+ms_iDebugLine], ms_iDebugMsg_PosX, i*14, 0);
//#                 }
//#             }
//#         }
        //#endif
    }

    /**
     * Add a message to the debug. 
     * <P>
     * @param _zMessage message to add.
     */

    public static void DebugMsg (String _zMessage) {
    //#ifdef Debug
//# 
//#         if (ms_bAllowLogging) {
//#             System.out.println (_zMessage);
//#             if (ms_iDebugMaxLine < (MAX_DEBUGSTRINGS-1)) {
//#                 ms_zDebugString[ms_iDebugMaxLine] = _zMessage;
//#                 ms_iDebugMaxLine++;
//# 
//#             } else {
//#                 for (int i=0; i<(MAX_DEBUGSTRINGS-1); i++)
//#                 ms_zDebugString[i] = ms_zDebugString[i+1];
//#                 ms_zDebugString[MAX_DEBUGSTRINGS-1] = _zMessage;
//#             }
//#         }
    //#endif
    }
    
    /**
     * Add a message to the debug. <P>
     *
     * @param _zMessage message to add.
     * @param _vException throws a print stack trace 
     */
    public static void DebugMsg (String _zMessage, Exception _vException) {
        //#ifdef Debug
//#         DebugMsg(_zMessage);
//#         if (_vException != null)
//#             _vException.printStackTrace();
        //#endif
    }
    
    public static void DebugReadPostmortemData () {

        //#ifdef Debug
//#         int iPMMessages;
//#         int i;
//# 
//#         DebugMsg ( "READING POST MORTEM DATA..." );
//#         DebugMsg ( "+++++++++++++++++++++++++++" );
//#         
//#         try {
//# 
//#             ms_RecordStorePM = RecordStore.openRecordStore (POSTMORTEM_NAME, true);
//#             iPMMessages = ms_RecordStorePM.getNumRecords ();
//#             
//#             for (i = 0; i < iPMMessages; i++)
//#                 DebugMsg ( new String (ms_RecordStorePM.getRecord (i+1)));
//#             
//#             ms_RecordStorePM.closeRecordStore ();
//#             ms_RecordStorePM.deleteRecordStore (POSTMORTEM_NAME);
//#             ms_RecordStorePM = null;
//#             
//#         } catch (RecordStoreException ex) {
//#             DebugMsg ( "ERROR READING POST MORTEM DATA... Excpt:"+ex.getMessage ()+"("+ex.toString ()+")" );
//#         }
//# 
//#         DebugMsg ( "POST MORTEM DATA END" );
//#         DebugMsg ( "++++++++++++++++++++" );
        //#endif
    }

    public static void DebugPostmortemMsg ( String _zMessage ) {
        //#ifdef Debug
//#         DebugMsg (_zMessage);
//# 
//#         try {
//#             ms_RecordStorePM = RecordStore.openRecordStore(POSTMORTEM_NAME, true);
//#             ms_RecordStorePM.addRecord( _zMessage.getBytes(), 0, _zMessage.length() );
//#         }
//#         catch (Exception ex) {
//#             DebugMsg( "ERROR writing post mortem data. Excpt:"+ex.getMessage()+"("+ex.toString()+")" );
//#         }
        //#endif
    }
}
