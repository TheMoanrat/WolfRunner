package com.kitmaker.manager;

/*
 * SndManager.java
 *
 * Created on 23 de octubre de 2006, 10:46
 */
////////////////////////////////////////////////////////////////
//
// END OF PREPROCESSOR BLOCKS
//
////////////////////////////////////////////////////////////////
//import java.io.*;

//#define NoPrecacheSounds

//#if s40asha
//# ///#define RequiresSoundDeallocation
//#endif

import com.kitmaker.wolfRunner.Define;
import com.kitmaker.wolfRunner.WolfRunner;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.ToneControl;
import javax.microedition.media.control.VolumeControl;

public class SndManager {
    
    //#if Sound != "false"
    public static final boolean SOUND_SUPPORTED = true;
    
    //#if SoundFX == "false"
//#     private static final boolean FX_SUPPORTED = false;
    //#else
    private static final boolean FX_SUPPORTED = true;
    //#endif
    
    //#else
//#     public static final boolean SOUND_SUPPORTED = false;
//#     private static final boolean FX_SUPPORTED = true;
    //#endif
    
    
   public static final byte MUSIC_NOMUSIC = -1;
   public static final byte MUSIC_MENU = 0;
   public static final byte MUSIC_MAP = 1;
   public static final byte MUSIC_WIN = 2;
   
   static final String[] MUSIC_FILES = {
      "/music_menu.mid", // 
      "/music_map.mid", // 
      "/music_win.mid", //
   };
   
    
    public static final byte FX_NOFX = -1;
    public static final byte FX_BACK = 0;
    public static final byte FX_DEATH = 1;
    public static final byte FX_RELOAD = 2;
    public static final byte FX_SELECT = 3;
    public static final byte FX_SHOOT = 4;
    
    public static final byte TOTAL_FX = 5;
    
    public static final String[] SNDFX_FILES = {
        "/fx_back.wav", // 0
        "/fx_death.wav",
        "/fx_reload.wav",
        "/fx_select.wav",
        "/fx_shoot.wav",
        
    };
    
    //#if Sound == "OnOff"
    static final int[] SOUND_VOLUME = { 0, 90 };
    //#else
//#     static final int[] SOUND_VOLUME = { 0, 30, 60, 90 };
    //#endif
    
    public static boolean ms_bSound = false;
    public static int ms_iSoundVolumeIndex = SOUND_VOLUME.length - 1;
    
    public static boolean ms_bUpdatingSound = false;
    //#if Sound != "false"
    static boolean ms_bNewClip;
    static final int NUM_MUSIC = MUSIC_FILES.length;
    static final int NUM_SNDFX = SNDFX_FILES.length;
   
    static byte ms_iCurrentFXClip = FX_NOFX;
   
    static long ms_lCurrentClipStartTime = Long.MAX_VALUE;

    private static Player  ms_vMusicPlayer;
    public static Player[] ms_vSndFxPlayer = new Player[NUM_SNDFX];
    
    
    static final int NUM_MUSIC_CLIPS = MUSIC_FILES.length;
    static byte ms_iCurrentMusicClip = MUSIC_NOMUSIC;
    static byte ms_iNewMusicClip = FX_NOFX;
    static boolean ms_bCurrentMusicLooping;
    static boolean ms_bNewMusicLooping;
   
    static final int NUM_FX_CLIPS = SNDFX_FILES.length;
   
    // media id
    static final String MEDIAID_MID = "audio/midi";
    static final String MEDIAID_WAV = "audio/x-wav";
    static final String MEDIAID_AMR = "audio/amr";
    static final String MEDIAID_MP3 = "audio/mpeg";
    static String ms_zMediaId; 

    //#endif
    
    public static void DeleteFX(){
        //#if Sound != "false"
        StopFX();
        for (byte i = 0; i < ms_vSndFxPlayer.length; i++){
            DeleteFX(i);
        }
        //#endif
    }
   
    public static void DeleteFX(byte _iIndex) {        
        //#if Sound != "false"
        if (ms_vSndFxPlayer != null){
            try {
                ms_vSndFxPlayer[_iIndex].close();
            } catch (Exception ex) {
            }
            ms_vSndFxPlayer[_iIndex] = null;
        }
        //#endif
    }

    public static void LoadFX(byte index) {

        //#if Sound != "false"
        if (FX_SUPPORTED) {
        
            //#if Debug
//#             Debug.DebugMsg("*InitSnd INITIALIZing FX");
//#             System.gc();
//#             Debug.DebugMsg("Mem:" + Runtime.getRuntime().freeMemory());
            //#endif
            
            if (SNDFX_FILES[index].endsWith("amr"))
                ms_zMediaId = MEDIAID_AMR;
            else if (SNDFX_FILES[index].endsWith("wav"))
                ms_zMediaId = MEDIAID_WAV;
            else if (SNDFX_FILES[index].endsWith("mp3"))
                ms_zMediaId = MEDIAID_MP3;

            try {

                if (ms_vSndFxPlayer[index] == null) {
                    ms_vSndFxPlayer[index] = Manager.createPlayer (WolfRunner.ms_vInstance.getClass().getResourceAsStream(SNDFX_FILES[index]), ms_zMediaId);
                
                    //#if Debug
//#                     System.gc();
//#                     Debug.DebugMsg("Created Snd." + index + " - Mem:" + Runtime.getRuntime().freeMemory());
                    //#endif

                    ms_vSndFxPlayer[index].prefetch();
                    //#if Debug
//#                     Debug.DebugMsg("Prefetched Snd." + index + " - Mem:" + Runtime.getRuntime().freeMemory());
                    //#endif

                    ms_vSndFxPlayer[index].realize();
                    //#if Debug
//#                     Debug.DebugMsg("Realized Snd." + index + " - Mem:" + Runtime.getRuntime().freeMemory());
                    //#endif
                }
                
            } catch (Exception e) {
                System.out.println(index + "; " + e.toString());
                //#if Debug
//#                 Debug.DebugMsg("**ERR InitSnd (Snd " + index + "): Exc " + e.toString() + " -- " + e.getMessage());
                //#endif
            }
        } else {
            ms_vSndFxPlayer[index] = null;
            //#if Debug
//#             Debug.DebugMsg("**Skipping load of Snd " + index);
            //#endif
        }
        //#endif
    }
    
    public static void LoadAllFX() {

        //#if Sound != "false"
        if (FX_SUPPORTED) {
            for (int i = 0; i < TOTAL_FX; i++) {
                
                //#if Debug
//#                 Debug.DebugMsg("*InitSnd INITIALIZing FX");
//#                 System.gc();
//#                 Debug.DebugMsg("Mem:" + Runtime.getRuntime().freeMemory());
                //#endif

                if (SNDFX_FILES[i].endsWith("amr"))
                    ms_zMediaId = MEDIAID_AMR;
                else if (SNDFX_FILES[i].endsWith("wav"))
                    ms_zMediaId = MEDIAID_WAV;
                else if (SNDFX_FILES[i].endsWith("mp3"))
                    ms_zMediaId = MEDIAID_MP3;

                try {

                    if (ms_vSndFxPlayer[i] == null)
                        ms_vSndFxPlayer[i] = Manager.createPlayer (WolfRunner.ms_vInstance.getClass().getResourceAsStream(SNDFX_FILES[i]), ms_zMediaId);

                    //#if Debug
//#                     System.gc();
//#                     Debug.DebugMsg("Created Snd." + " - Mem:" + Runtime.getRuntime().freeMemory());
                    //#endif

                    ms_vSndFxPlayer[i].prefetch();
                    //#if Debug
//#                     Debug.DebugMsg("Prefetched Snd." + " - Mem:" + Runtime.getRuntime().freeMemory());
                    //#endif

                    ms_vSndFxPlayer[i].realize();
                    //#if Debug
//#                     Debug.DebugMsg("Realized Snd." + " - Mem:" + Runtime.getRuntime().freeMemory());
                    //#endif

                } catch (Exception e) {
                    System.out.println(i + "; " + e.toString());
                    //#if Debug
//#                     Debug.DebugMsg("**ERR InitSnd (Snd " + "): Exc " + e.toString() + " -- " + e.getMessage());
                    //#endif
                }
            }
        } else {
            ms_vSndFxPlayer[0] = null;
            //#if Debug
//#             Debug.DebugMsg("**Skipping load of Snd ");
            //#endif
        }
        
        //#endif
    }
    
    public static void UpdateMusic() {
        //#if Sound != "false"
        if (Define.ms_bPaused) {
            return;
        }
      
        ms_bUpdatingSound = true;
      
        if (System.currentTimeMillis () >= ms_lCurrentClipStartTime) {
         
            if (ms_bSound && (ms_bNewClip || ms_bCurrentMusicLooping)) {
            
                if ((ms_iCurrentMusicClip == MUSIC_NOMUSIC) || 
                    (ms_vMusicPlayer == null) ||
                    (ms_vMusicPlayer.getState() != Player.STARTED)) {

                    // There should be a sound playing, but there isn't any.
                    if (ms_bNewClip) {
                  
                        if (ms_iCurrentMusicClip != MUSIC_NOMUSIC && ms_vMusicPlayer != null) {

                            try {
                                ms_vMusicPlayer.close();
                            } catch (Exception e) {
                                //#if Debug
//#                                 Debug.DebugMsg("*ERR UpdtSnd-close of sound(" + ms_iCurrentMusicClip + ") failed: Exc " + e.toString() 
//#                                         + " -- " + e.getMessage() + " - Frame " + Main.ms_iFrame);
                                //#endif
                            }
                            ms_vMusicPlayer = null;

                        }
                        ms_iCurrentMusicClip = ms_iNewMusicClip;
                        ms_bCurrentMusicLooping = ms_bNewMusicLooping;
                        
                    } else {
                        //#if (API!="Nokia" && !z140v && !u8110 && !x660)
                        try {
                            if (ms_vMusicPlayer.getState() != Player.STARTED)
                                ms_vMusicPlayer.deallocate();
                            
                        } catch (Exception e ) {
                            //#if Debug
//#                             Debug.DebugMsg("*ERR UpdtSnd-deallocate of sound(" + ms_iCurrentMusicClip + ") failed: Exc " + 
//#                                      e.toString() + " -- " + e.getMessage() + " - Frame " + Main.ms_iFrame);
                            //#endif
                        }
                        //#endif
                    }
                    
                    //#if Debug
//#                     Debug.DebugMsg("*UpdtSnd-Preparing sound (" + ms_iCurrentMusicClip + ")... - Frame " + Main.ms_iFrame);
                    //#endif
               
                    if (ms_vMusicPlayer == null) {
                        try {
                            //#if Debug
//#                             Debug.DebugMsg("*UpdtSnd-Creating sound stream (sound " + ms_iCurrentMusicClip + ")... - Frame " + Main.ms_iFrame);
                            //#endif
                            
                            ms_vMusicPlayer = 
                                Manager.createPlayer (WolfRunner.ms_vInstance.getClass().getResourceAsStream(MUSIC_FILES[ms_iCurrentMusicClip]), MEDIAID_MID);
                            
                            //#if Debug
//#                             Debug.DebugMsg("*UpdtSnd-sound created OK (" + ms_iCurrentMusicClip + ") - Frame " + Main.ms_iFrame);
                            //#endif
                            
                        } catch (Exception ex) {
                            //#if Debug
//#                             Debug.DebugMsg("*UpdtSnd-exception creating new sound (" + ms_iCurrentMusicClip + ") - Frame " + Main.ms_iFrame + "  Exc " + ex.toString() + " -- " + ex.getMessage());
                            //#endif
                        }
                    }
                    
                    //#if MIDP=="2.0" || RIM
                    if ((ms_vMusicPlayer != null) && 
                        (ms_vMusicPlayer.getState() != Player.REALIZED)) {
                        try {
                            ms_vMusicPlayer.realize();
                            //#if Debug
//#                             Debug.DebugMsg("*UpdtSnd-new sound (" + ms_iCurrentMusicClip + ") ready - Frame " + Main.ms_iFrame);
                            //#endif

                        } catch (Exception e) {
                            //#if Debug
//#                             Debug.DebugMsg("**ERR UpdtSnd-RLZE : Exc " + e.toString() + " -- " + e.getMessage());
                            //#endif
                            ms_vMusicPlayer = null;
                        }
                    }
                    //#endif
                    
                    try {
                        //#if Debug
//#                         Debug.DebugMsg("*UpdtSnd-Starting playback (sound " + ms_iCurrentMusicClip + ")... - Frame " + Main.ms_iFrame);
                        //#endif

                        //#if API != "LG" && API != "Samsung" && Sound == "Volume"
//#                         VolumeControl vVolumeControl = (VolumeControl) ms_vMusicPlayer.getControl("VolumeControl");
//#                         vVolumeControl.setLevel(SOUND_VOLUME[ms_iSoundVolumeIndex]);
                        //#endif
                        
                        ms_vMusicPlayer.start();
                        ms_bAsyncException = false;
                        
                        //#if Debug
//#                         Debug.DebugMsg("*UpdtSnd-Playback of sound(" + ms_iCurrentMusicClip + ") started OK: - Frame " + Main.ms_iFrame);
                        //#endif

                    } catch (Exception e) {
                        //#if Debug
//#                         Debug.DebugMsg("*ERR UpdtSnd-Playback of sound(" + ms_iCurrentMusicClip + ") failed: Exc " + e.toString() + " -- " + e.getMessage() + " - Frame " + Main.ms_iFrame + "; Player State:");
//#                         Debug.DebugMsg("*ERR UpdtSnd-Player State: " + ms_vMusicPlayer.getState());
                        //#endif 
                        
                        // player is realized and ready to play, but throws an exception
                        // we can consider this is a asyncronous problem with call interruptions
                        if (ms_vMusicPlayer.getState() == Player.REALIZED) {
                            ms_bAsyncException = true;
                            ms_iAsyncClip = ms_iCurrentMusicClip;
                            ms_bAsyncLoop = ms_bCurrentMusicLooping;
                            StopMusic();
                        }
                        return;
                    }
                }
            }
         
            //#if Debug
//#             if (ms_bNewClip)
//#                 Debug.DebugMsg("UpdtSnd-New clip (" + ms_iCurrentMusicClip + ") handled. - Frame " + Main.ms_iFrame);
            //#endif
            
            ms_bNewClip = false;
        }
        ms_bUpdatingSound = false;
        //#endif
    }

    public static void PlayMusic(byte _iSoundID, boolean _bLooping, int _iDelay) {
        //#if Sound != "false"
        if (ms_bSound) {
            
            //#if Debug
//#             Debug.DebugMsg("*PlayMusic REQUESTing snd " + _iSoundID + "... - Frame " + Main.ms_iFrame);
            //#endif
            
            ms_zMediaId = MEDIAID_MID;
            ms_bNewMusicLooping = _bLooping;
            ms_lCurrentClipStartTime = System.currentTimeMillis() + _iDelay;
            ms_iCurrentMusicClip = _iSoundID;
            ms_iNewMusicClip = _iSoundID;
            ms_bNewClip = true;
        }
        //#endif
    }

    public static void StopMusic() {
        //#if Sound != "false"
        // Stop previous sound
        //#if Debug
//#         Debug.DebugMsg("*StopSnd start... - Frame " + Main.ms_iFrame);
        //#endif
        if (ms_iCurrentMusicClip == MUSIC_NOMUSIC) {
            //#if Debug
//#             Debug.DebugMsg("* ...but not stopping (no snd being played) - Frame " + Main.ms_iFrame);
            //#endif
        } else {
            
            ms_bNewClip = false;
            ms_bCurrentMusicLooping = ms_bNewMusicLooping = false;
            //ms_lCurrentClipStartTime = Long.MAX_VALUE;
            
            if ((ms_vMusicPlayer != null) && 
                (ms_vMusicPlayer.getState() != Player.REALIZED)) {
                
                try {
                    ms_vMusicPlayer.close();
                } finally {
                    ms_vMusicPlayer = null;
                }
            }
            
            //#if Debug
//#             Debug.DebugMsg("* ...StopSnd ok. - Frame " + Main.ms_iFrame);
            //#endif
        }
        //#endif
    }

    // Stops sound but enables it to be restarted the in the next game loop
    //   (provided the sound is looping)

    private static boolean ms_bPausedClip;
    private static byte ms_iPausedCurrentClip;
    private static boolean ms_bPausedCurrentLooping;
    private static boolean ms_bAsyncException = false;
    private static boolean ms_bAsyncLoop = false;
    private static byte ms_iAsyncClip;

    public static void PauseMusic() {
        //#if Sound != "false"
        // Stop previous sound
        //#if Debug
//#         Debug.DebugMsg("*PauseSnd start... - Frame " + Main.ms_iFrame);
        //#endif
        
        if (ms_iCurrentMusicClip == MUSIC_NOMUSIC) {
            //#if Debug
//#             Debug.DebugMsg("* ...but not stopping (no snd being played) - Frame " + Main.ms_iFrame);
            //#endif
        } else {
            
            if (!ms_bPausedClip) {
                ms_bPausedClip = true;
                ms_iPausedCurrentClip = ms_iCurrentMusicClip;
                ms_bPausedCurrentLooping = ms_bCurrentMusicLooping;
            }
            
            ms_bNewClip = false;
            ms_bCurrentMusicLooping = ms_bNewMusicLooping = false;
            //ms_lCurrentClipStartTime = Long.MAX_VALUE;
            
            if (ms_vMusicPlayer != null &&
                ms_vMusicPlayer.getState() != Player.REALIZED) {

                //TODO !!! provar
                try {
                    ms_vMusicPlayer.stop();
                } catch (MediaException ex) {
                }
            }
            
            //#if Debug
//#             Debug.DebugMsg("* ...PauseSnd ok. - Frame " + Main.ms_iFrame);
            //#endif
            
        }
        //#endif

    }
    
    public static void UnpauseMusic() {
        //#if Sound != "false"
        if (ms_bSound && ms_bAsyncException) {
            PlayMusic(ms_iAsyncClip, ms_bAsyncLoop, 1000);
            ms_bAsyncException = false;
        }
        
        if (ms_bSound && ms_bPausedClip) {
            
            //#if Debug
//#             Debug.DebugMsg("* ...UnpaseSnd. - Frame " + Main.ms_iFrame);
            //#endif
            ms_bNewMusicLooping = ms_bPausedCurrentLooping;
            ms_lCurrentClipStartTime = System.currentTimeMillis() + 100;
            ms_iNewMusicClip = ms_iPausedCurrentClip;
            ms_bNewClip = true;
            ms_bPausedClip = false;
        }
        //#endif
    }
    
    public static void PlayFX(byte _iFXID) {
        //#if Sound != "false"
        if (FX_SUPPORTED) {
            if (ms_bSound) {
                
                //#if Debug
//#                 Debug.DebugMsg("*PlayFX REQUESTing snd " + _iFXID + "... - Frame " + Main.ms_iFrame);
                //#endif
                
                try {
                    if (ms_iCurrentFXClip != FX_NOFX && 
                        ms_vSndFxPlayer[ms_iCurrentFXClip] != null) {
                        
                        //#if API == "Nokia"
//#                         if (ms_vSndFxPlayer[ms_iCurrentFXClip].getState () == Player.STARTED)
//#                             return;
                        //#endif
                       
                        if (ms_vSndFxPlayer[ms_iCurrentFXClip].getState () == Player.STARTED && ms_iCurrentFXClip != _iFXID)
                            ms_vSndFxPlayer[ms_iCurrentFXClip].stop();
                    }
                } catch (Exception ex) {
                    ms_vSndFxPlayer[ms_iCurrentFXClip] = null;
                    //#if Debug
//#                     Debug.DebugMsg("**ERR PlayFX - stopping previous sound : Exc " + ex.toString() + " -- " + ex.getMessage());
                    //#endif
                }
                ms_iCurrentFXClip = _iFXID;

                //#if s40asha
//#                  DeleteFX(_iFXID);
//#                  LoadFX(_iFXID);
                //#else
                if (ms_vSndFxPlayer[ms_iCurrentFXClip] == null) {
                    LoadFX(_iFXID);
                }
                //#endif
                
                try {
                    if (ms_vSndFxPlayer[ms_iCurrentFXClip].getState() != Player.STARTED) {
                        //#if API != "LG" && API != "Samsung"  && Sound == "Volume"
//#                         VolumeControl vVolumeControl = (VolumeControl) ms_vSndFxPlayer[ms_iCurrentFXClip].getControl("VolumeControl");
//#                         vVolumeControl.setLevel(SOUND_VOLUME[ms_iSoundVolumeIndex]);
                        //#endif
                        ms_vSndFxPlayer[ms_iCurrentFXClip].start();
                    }
                } catch (Exception ex) {
                    //#if Debug
//#                     Debug.DebugMsg("**ERR PlayFX - start() : Exc " + ex.toString() + " -- " + ex.getMessage());
                    //#endif
                }
            }
        }
        //#endif
    }
    
    public static void StopFX() {
         
        if (FX_SUPPORTED) {
            //#if Sound != "false"
            
            // Stop previous sound
            //#if Debug
//#             Debug.DebugMsg("*StopSnd start... - Frame " + Main.ms_iFrame);
            //#endif
            
            if(ms_iCurrentFXClip != -1){
                if ((ms_vSndFxPlayer[ms_iCurrentFXClip] != null)
                        && (ms_vSndFxPlayer[ms_iCurrentFXClip].getState() != Player.REALIZED)) {
                    try {
                        ms_vSndFxPlayer[ms_iCurrentFXClip].stop();
                    } catch (MediaException ex) {
                    }
                }
            }

            //#if Debug
//#             Debug.DebugMsg("* ...StopSnd ok. - Frame " + Main.ms_iFrame);
            //#endif
            
            //#endif
        }
    }
    
    public static final int TONE_MOVE = 0; 
    public static final int TONE_SELECT = 1; 
    public static final int TONE_BACK = 2;
   
    public static void PlayTone (int _iType) {
        //#if MIDP || RIM
        try {
            if (ms_bSound) {
                if (_iType == TONE_MOVE) {
                    Manager.playTone(ToneControl.C4+8, 50, SOUND_VOLUME[ms_iSoundVolumeIndex]);

                } else if (_iType == TONE_SELECT) {
                    Manager.playTone(ToneControl.C4, 50, SOUND_VOLUME[ms_iSoundVolumeIndex]);
                    Manager.playTone(ToneControl.C4+8, 200, SOUND_VOLUME[ms_iSoundVolumeIndex]);

                } else if (_iType == TONE_BACK) {
                    Manager.playTone(ToneControl.C4, 50, SOUND_VOLUME[ms_iSoundVolumeIndex]);
                    Manager.playTone(ToneControl.C4-8, 200, SOUND_VOLUME[ms_iSoundVolumeIndex]);
                }
            }
        } catch (Exception ex){}
        //#endif
    }

    public static void FlushSndManager() {
        ms_bSound = false;
        SndManager.StopMusic();
        SndManager.DeleteFX();
    }
}
