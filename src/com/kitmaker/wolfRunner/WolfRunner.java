package com.kitmaker.wolfRunner;

//#if MIDP
import com.kitmaker.manager.GfxManager;
import com.kitmaker.manager.SndManager;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
//#elifdef RIM
//# import net.rim.device.api.system.DeviceInfo;
//# import net.rim.device.api.system.Display;
//# import net.rim.device.api.ui.UiApplication;
//#elifdef DOJA
//# import com.nttdocomo.ui.*;
//# import com.nttdocomo.ui.IApplication;
//#endif

/** @author Cleverton */
//#ifdef MIDP
public class WolfRunner extends MIDlet {
//#elifdef RIM
//# public class Skeleton extends UiApplication {
//#elifdef DOJA
//# public class HUNT extends IApplication {
//#endif

    public static Main ms_vMain;
    public static WolfRunner ms_vInstance;
    public static Display ms_vDisplay;
    public static Thread ms_vThread;
    public static String ms_zLanguage;
    public static String ms_zCountry;
    private static final String APP_VERSION = "1.0.0";
    private static final String APP_NAME = "Hunter's Prize";
    public static String ms_zMIDletVersion = APP_VERSION;
    public static String ms_zMIDletName = APP_NAME;
    
    private static boolean ms_bFirstBoot = false;
    private static boolean ms_bDestroyed;
    
    
    //#ifdef MIDP
    public final void startApp(){
        
        try{
            if (!ms_bFirstBoot) {

                ms_zMIDletVersion = getAppProperty ("MIDlet-Version");
                Define.DEVICE_NUMCOLORS = Display.getDisplay(this).numColors();
                Define.DEVICE_NUMALPHAS = Display.getDisplay(this).numAlphaLevels();
                Define.DEVICE_MODEL = System.getProperty("microedition.platform");
                
                ms_vInstance = this;
                ms_vMain = new Main(this);

                ms_vThread = new Thread (ms_vMain);
                ms_vThread.start ();

                ms_vDisplay = Display.getDisplay(this);
                ms_vDisplay.setCurrent (ms_vMain);

                ms_bFirstBoot = true;

            } else {
                ms_vMain.showNotify();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public final void pauseApp(){
        if (ms_vMain != null)
            ms_vMain.hideNotify();
    }
    //#elifdef RIM
//#     public static void main(String[] args) {
//#         ms_vInstance = new Skeleton();
//#         ms_vInstance.enterEventDispatcher();
//#     }
//# 
//#     public Skeleton() {
//# 
//#         ms_vInstance = this;
//# 
//# //        // get jad info
//# //        String name = ApplicationDescriptor.currentApplicationDescriptor().getModuleName();
//# //        CodeModuleGroup group = CodeModuleGroupManager.load(name);
//# //        CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
//# //
//# //        for (int i = 0; i < allGroups.length; i++) {
//# //            if (allGroups[i].containsModule(name)) {
//# //                ms_zMIDletName = group.getProperty("MIDlet-Name");
//# //                ms_zMIDletVersion = group.getProperty("MIDlet-Version");
//# //                break;
//# //            }
//# //        }
//#         if (ms_zMIDletName == null)
//#             ms_zMIDletName = APP_NAME;
//#         
//#         if (ms_zMIDletVersion == null)
//#             ms_zMIDletVersion = APP_VERSION;
//# 
        //#if Debug
//#         net.rim.device.api.system.EventLogger.register(0x2051fd67b72d11L, ms_zMIDletName, net.rim.device.api.system.EventLogger.VIEWER_STRING);
        //#endif
//# 
//#         //Define.DEVICE_NUMCOLORS = Display.getNumColors();
//#         Define.DEVICE_NUMALPHAS = 255;
//#         Define.DEVICE_MODEL = System.getProperty("microedition.platform");
//#         Define.DEVICE_MODEL = DeviceInfo.getDeviceName();
//#         if (Define.DEVICE_MODEL == null) {
//#             Define.DEVICE_MODEL = "";
//#         }
//# 
//#         // Active keyUp events in older versions
        //#if RIM < "4.7"
//#         enableKeyUpEvents(true);
        //#endif
//#         
//#         // Prevent UI from rotating our screen.
        //#if RIM > "4.7"
//#         if (Define.SIZEX < Define.SIZEY)
//#             net.rim.device.api.ui.Ui.getUiEngineInstance().setAcceptableDirections(Display.DIRECTION_PORTRAIT);
//#         else
//#             net.rim.device.api.ui.Ui.getUiEngineInstance().setAcceptableDirections(Display.DIRECTION_LANDSCAPE);
        //#endif
//#         
//#         ms_vMain = new Main(this);
//#         pushScreen(ms_vMain);
//#         
//#         ms_vThread = new Thread(ms_vMain);
//#         ms_vThread.start();
//#         
//#         ms_bFirstBoot = true;
//# 
//#     }
//# 
//#     public void deactivate() {
//#         if (ms_vMain != null) {
//#             ms_vMain.hideNotify();
//#         }
//#     }
//# 
//#     public void activate() {
//#         if (ms_vMain != null) {
//#             ms_vMain.showNotify();
//#         }
//#     }
//# 
//#     public boolean onClose() {
//#         SndManager.StopMusic();
//#         SndManager.DeleteFX();
//#         GfxManager.UnloadGraphics();
//#         System.exit(0);
//#         return true;
//#     }
//# 
    //#elif DOJA
//#     public void start() {
//# 
//#         ms_zMIDletVersion = getParameter ("MIDlet-Version");
//# 
//#         ms_vInstance = this;
//#         ms_vMain = new Main (this);
//# 
//#         Main.DEVICE_NUMCOLORS = Display.numColors();
//#         Main.DEVICE_MODEL = System.getProperty ("microedition.platform");
//#         Display.setCurrent (ms_vMain);
//#             
//#     }
    //#endif

    public final void destroyApp(boolean unconditional){
        try {
            if (!ms_bDestroyed) {
                
                //#if Debug
//#                 Debug.DebugMsg("***destroy App - star***");
                //#endif

                SndManager.FlushSndManager();
                GfxManager.UnloadGraphics();

                //#ifdef MIDP
                ms_vInstance.notifyDestroyed();
                //#elifdef RIM
//#                 System.exit(0);
                //#elifdef DOJA
//#                 ms_vInstance.terminate();
                //#endif

                System.gc();

                //#if Debug
//#                 Debug.DebugMsg("***destroy App - destroyed***");
//#                 Debug.DebugMsg("Have a nice day!!!");
                //#endif

                Define.ms_bFinishApp = true;
                ms_bDestroyed = true;
            }

        } catch (Exception e) {
            //#if Debug
//#             e.printStackTrace();
            //#endif
        }
    }
    
    public void quitApp() {
        ms_vInstance.destroyApp(true);
    }
    
    public static void checkLanguage() {

        String locale = System.getProperty("microedition.locale");
        
        if (locale != null) {
            int pos = locale.indexOf('-');
            if (pos == -1) {
                pos = locale.indexOf('_');
            }

            if (pos != -1) {
                ms_zLanguage = locale.substring(0, pos);
                locale = locale.substring(pos + 1);

                pos = locale.indexOf('-');
                if (pos == -1) {
                    pos = locale.indexOf('_');
                    ms_zCountry = (pos == -1) ? locale : locale.substring(0, pos);

                } else {
                    ms_zCountry = locale.substring(0, pos);
                }
            } else {
                ms_zLanguage = locale;
            }
        }
        
        
    }
}