/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kitmaker.manager;

import com.kitmaker.wolfRunner.WolfRunner;
import java.io.DataInputStream;
import javak.microedition.lcdui.Font;

/**
 *
 * @author Fran
 */
public class TxtManager {
    
    ////////////////////////////////////////////////////////////////////////////
    //
    // STRING & TEXTS DEFINITIONS
    //
    ////////////////////////////////////////////////////////////////////////////
    
    public static final int TXT_HELP_MENU_TITLE = 0;
    public static final int TXT_NT_HELP_MENU_TITLE = TXT_HELP_MENU_TITLE + 1;
    public static final int TXT_NT_HELP_MENU_TEXT1 = TXT_NT_HELP_MENU_TITLE + 1;
    public static final int TXT_NT_HELP_MENU_TEXT2 = TXT_NT_HELP_MENU_TEXT1 + 1;
    public static final int TXT_NT_HELP_MENU_TEXT3 = TXT_NT_HELP_MENU_TEXT2 + 1;
    public static final int TXT_NT_HELP_MENU_TEXT4 = TXT_NT_HELP_MENU_TEXT3 + 1;
    public static final int TXT_NT_HELP_GAME_TITLE = TXT_NT_HELP_MENU_TEXT4 + 1;
    public static final int TXT_NT_HELP_GAME_TEXT1 = TXT_NT_HELP_GAME_TITLE + 1;
    public static final int TXT_NT_HELP_GAME_TEXT2 = TXT_NT_HELP_GAME_TEXT1 + 1;
    public static final int TXT_NT_HELP_GAME_TEXT3 = TXT_NT_HELP_GAME_TEXT2 + 1;
    public static final int TXT_NT_HELP_GAME_TEXT4 = TXT_NT_HELP_GAME_TEXT3 + 1;
    public static final int TXT_T_HELP_MENU_TITLE = TXT_NT_HELP_GAME_TEXT4 + 1;
    public static final int TXT_T_HELP_MENU_TEXT1 = TXT_T_HELP_MENU_TITLE + 1;
    public static final int TXT_T_HELP_MENU_TEXT2 = TXT_T_HELP_MENU_TEXT1 + 1;
    public static final int TXT_T_HELP_MENU_TEXT3 = TXT_T_HELP_MENU_TEXT2 + 1;
    public static final int TXT_T_HELP_MENU_TEXT4 = TXT_T_HELP_MENU_TEXT3 + 1;
    public static final int TXT_T_HELP_GAME_TITLE = TXT_T_HELP_MENU_TEXT4 + 1;
    public static final int TXT_T_HELP_GAME_TEXT1 = TXT_T_HELP_GAME_TITLE + 1;
    public static final int TXT_T_HELP_GAME_TEXT2 = TXT_T_HELP_GAME_TEXT1 + 1;
    public static final int TXT_T_HELP_GAME_TEXT3 = TXT_T_HELP_GAME_TEXT2 + 1;
    public static final int TXT_T_HELP_GAME_TEXT4 = TXT_T_HELP_GAME_TEXT3 + 1;
    public static final int TXT_MAIN_MENU_PLAY = TXT_T_HELP_GAME_TEXT4 + 1;//21
    public static final int TXT_MAIN_MENU_OPTIONS = TXT_MAIN_MENU_PLAY + 1;
    public static final int TXT_MAIN_MENU_INFO = TXT_MAIN_MENU_OPTIONS + 1;
    public static final int TXT_MAIN_MENU_EXIT = TXT_MAIN_MENU_INFO + 1;
    public static final int TXT_INFO_HELP = TXT_MAIN_MENU_EXIT + 1;
    public static final int TXT_INFO_ABOUT = TXT_INFO_HELP + 1;
    public static final int TXT_OPTIONS_SOUND = TXT_INFO_ABOUT + 1;
    public static final int TXT_OPTIONS_VIBRATE = TXT_OPTIONS_SOUND + 1;
    public static final int TXT_OPTIONS_LANGUAGE = TXT_OPTIONS_VIBRATE + 1;
    public static final int TXT_YES = TXT_OPTIONS_LANGUAGE + 1;
    public static final int TXT_NO = TXT_YES + 1;
    public static final int TXT_TITLE_QUIT = TXT_NO + 1;
    public static final int TXT_CONTINUE = TXT_TITLE_QUIT + 1;
    public static final int TXT_SOUND_LOW = TXT_CONTINUE + 1;
    public static final int TXT_SOUND_MEDIUM = TXT_SOUND_LOW + 1;
    public static final int TXT_SOUND_LOUD = TXT_SOUND_MEDIUM + 1;
    public static final int TXT_SOUND_OFF = TXT_SOUND_LOUD + 1;
    public static final int TXT_PAUSE = TXT_SOUND_OFF + 1;
    public static final int TXT_TITLE_TUTORIAL = TXT_PAUSE + 1;
    public static final int TXT_MISSION= TXT_TITLE_TUTORIAL + 1;
    public static final int TXT_OBJETIVE = TXT_MISSION + 1;
    public static final int TXT_RETURN= TXT_OBJETIVE + 1;
    public static final int TXT_COMPLETED= TXT_RETURN + 1;
    public static final int TXT_EXIT_RUN= TXT_COMPLETED + 1;
    public static final int TXT_PLAY_AGAIN = TXT_EXIT_RUN + 1;
    public static final int TXT_RESUME = TXT_PLAY_AGAIN + 1;

    // Language
    public static final String ms_zBlankString = "";
    public static final int TEXT_SECTIONSTRINGS = 48;
    
    public static int ms_iLanguage = -1;
    public static String[] ms_zLanguageTextFile = {"/en.lng", "/es.lng", "/br.lng"};
    public static String[] ms_vText = new String[TEXT_SECTIONSTRINGS];
    public static String[] ms_vTextLanguageWord;
    public static String[] ms_vTextLanguageMenu = {"English", "Espa" + "\u00F1" + "ol", "Brasileiro"};
    
    /**
     * load language configuration. <P> To display the supported languages 
     * correctly we need a txt file with the popper information, with the name 
     * of the language in its own respective language and the name of the language file <P>
     * This file is created automatically by the TextExporter, but can be edited manually.
     */
    public static void LanguageLoadConfiguration() {


        String zLanguage [] = new String[32];
        LanguageSectionLoad("/cf.lng", 0, zLanguage);
        
        int iNumLines = 0;
        for (int i=0; i<zLanguage.length; i++) {
            if (zLanguage[i] != null && !zLanguage[i].equals(""))
                iNumLines++;
        }
        int iNumLanguages = iNumLines/3;
        
        //String _zStringArray = new String(sb);
        String vTextLanguageMenu[] = new String[iNumLanguages];
        String zLanguageTextFile[] = new String[iNumLanguages];
        String zSelectLanguageTx[] = new String[iNumLanguages];
        for (int i=0; i<iNumLanguages; i++) {
            vTextLanguageMenu[i] = "";
            zLanguageTextFile[i] = "";
            zSelectLanguageTx[i] = "";
        }
        
        for (int i=0; i<iNumLines; i+=3) {
            zLanguageTextFile[i/3] = zLanguage[i];
            vTextLanguageMenu[i/3] = zLanguage[i+1];
            zSelectLanguageTx[i/3] = zLanguage[i+2];
        }
        zSelectLanguageTx[iNumLanguages-1] = zSelectLanguageTx[iNumLanguages-1].substring(0, zSelectLanguageTx[iNumLanguages-1].length()-1);
        
        
        // removes unsupported languages
        for (int i=0; i<zLanguageTextFile.length; i++) {
            if (zLanguageTextFile[i].startsWith("ar") && !ARABIC_SUPPORT)
                iNumLanguages--;
            else if (zLanguageTextFile[i].startsWith("bn") && !BENGAL_SUPPORT)
                iNumLanguages--;
            else if (zLanguageTextFile[i].startsWith("ru") && !RUSIAN_SUPPORT)
                iNumLanguages--;
            else if (zLanguageTextFile[i].startsWith("ja") && !JAPANS_SUPPORT)
                iNumLanguages--;
        }
        
        ms_vTextLanguageMenu = new String[iNumLanguages];
        ms_zLanguageTextFile = new String[iNumLanguages];
        ms_vTextLanguageWord = new String[iNumLanguages];
        
        iNumLanguages = 0;
        for (int i=0; i<zLanguageTextFile.length; i++) {
            if (zLanguageTextFile[i].startsWith("ar") && !ARABIC_SUPPORT) {
            } else if (zLanguageTextFile[i].startsWith("bn") && !BENGAL_SUPPORT) {
            } else if (zLanguageTextFile[i].startsWith("ru") && !RUSIAN_SUPPORT) {
            } else if (zLanguageTextFile[i].startsWith("ja") && !JAPANS_SUPPORT) {
            } else {
                ms_vTextLanguageMenu[iNumLanguages] = vTextLanguageMenu[i];
                ms_zLanguageTextFile[iNumLanguages] = "/" + zLanguageTextFile[i];
                ms_vTextLanguageWord[iNumLanguages] = zSelectLanguageTx[i];
                iNumLanguages++;
            }
        }
    }
            
    public static void LanguageSectionClear(String[] _zStringArray) {
        int i;
        for (i = 0; i < _zStringArray.length; i++) {
            _zStringArray[i] = null;
        }
    }
    
    /**
     * Loads a section of Strings from a binary file. <P> This binary file is
     * created with a program that can be found in /res/txt/ <P>
     *
     * @param _zLanguageFile language file to load. The index needs to concordate with
     * the language order we used when we created the binary file
     * @param _iLanguageSection section to load. This index needs to concordate
     * witch the section order we used when we created the binary file
     * @param _zStringArray the String array in witch the data will be stored
     */
    public static void LanguageSectionLoad (
        String _zLanguageFile, int _iLanguageSection,
        String[] _zStringArray) {

        int[] iStringLength;
        int iStrings = 0, iLanguageStartPos = 0;
        int iLongestStringLength;
        int i = 0, j;
        int iReadBytes;
        DataInputStream vInputS = null;
        int iLanguageSectionIndex = (_iLanguageSection);

        System.gc();

        int iMoment = 0;

        //#ifdef Debug
//#         Debug.DebugMsg("LoadLanguageSection() - Language: " + _zLanguageFile);
//#         Debug.DebugPostmortemMsg ( "LoadLanguageSection() - Loading lang. section "+_iLanguageSection+" ("+ TEXT_SECTIONSTRINGS + " strings)...");
        //#endif

        try {
            vInputS = new DataInputStream(WolfRunner.ms_vInstance.getClass().getResourceAsStream(_zLanguageFile));

            // Find out language data start position
            vInputS.skip(iLanguageSectionIndex * 4);
            iLanguageStartPos = vInputS.readInt();
            vInputS.skip(iLanguageStartPos - (iLanguageSectionIndex * 4) - 4);

            // Read number of strings
            iStrings = vInputS.readShort();
            iStringLength = new int[iStrings];

            // Read maximum string length
            iLongestStringLength = vInputS.readShort();

            byte[] bChar = new byte[iLongestStringLength];

            // Read strings length
            for (i = 0; i < iStrings; i++) {
                iStringLength[i] = (int) vInputS.readByte();
                if ((iStringLength[i] & 128) != 0) {
                    iStringLength[i] = (iStringLength[i] & 0x7F) | (vInputS.readByte() << 7);
                }
            }
            
            // Read strings
            for (i = 0; i < iStrings; i++) {
                iReadBytes = 0;
                while (iReadBytes < iStringLength[i]) {
                    bChar[iReadBytes] = vInputS.readByte();
                    iReadBytes++;
                }
                
                try {
                    if (i==0 && bChar[0]==-17 && bChar[1]==-69 && bChar[2]==-65)
                        _zStringArray[i] = new String(bChar, 3, iStringLength[i]-3, "UTF-8");
                    else
                        _zStringArray[i] = new String(bChar, 0, iStringLength[i], "UTF-8");
                    
                } catch (Exception ex) {
                    _zStringArray[i] = new String(bChar, 0, iStringLength[i]);
                }
            }
            
            vInputS.close();

            //#ifdef Debug
//#             Debug.DebugPostmortemMsg ( "LoadLanguageSection() - Language section "+_iLanguageSection+" loaded.");
            //#endif

        } catch (Exception e) {
            //#ifdef Debug
//#             Debug.DebugMsg("LoadLanguageSection() - Moment " + iMoment + " - ERROR loading language section "+_iLanguageSection+ ". Excpt:"+e.getMessage ()+"("+e.toString ()+")");
//#             Debug.DebugPostmortemMsg ("LoadLanguageSection() - Moment " + iMoment + " - ERROR loading language section " +_iLanguageSection+
//#                 ". Excpt:"+e.getMessage ()+"("+e.toString ()+")");
//#             Debug.DebugPostmortemMsg ("iLanguageStartPos:" + iLanguageStartPos);
//#             Debug.DebugPostmortemMsg ("iStrings:"+_zStringArray);
//#             Debug.DebugPostmortemMsg ("i:" + i);
//#             Debug.DebugPostmortemMsg ("vInputS:"+vInputS);
//#             Debug.DebugPostmortemMsg ("_iLanguageSection:" + _iLanguageSection);
            //#endif
        }
        
        // arabic 
        
        if (ARABIC_SUPPORT && !ARABIC_PROPER_SUPPORT) {
            if (_zLanguageFile.startsWith("/ar") || _zLanguageFile.startsWith("/cf")) {
                for (i = 0; i < iStrings; i++) {
                    for (j = 0; j < _zStringArray[i].length(); j++) {
                        if ((_zStringArray[i].charAt(j) >= 0x0600 && _zStringArray[i].charAt(j) <= 0x06FF) ||
                            (_zStringArray[i].charAt(j) >= 0x0750 && _zStringArray[i].charAt(j) <= 0x077F)) {
                            String zNewtext = ArabicReshaper.Convert(_zStringArray[i]);
                            _zStringArray[i] = zNewtext;
                            break;
                        }
                    }
                }
            }
        }
        
        WolfRunner.ms_zLanguage = _zLanguageFile.substring(1, 3);
        
        FntManager.setFontType(FntManager.FTYPE_GRAPHIC);
        if (ARABIC_SUPPORT && WolfRunner.ms_zLanguage.equals("ar") ||
            BENGAL_SUPPORT && WolfRunner.ms_zLanguage.equals("bn") ||
            RUSIAN_SUPPORT && WolfRunner.ms_zLanguage.equals("ru") ||
            JAPANS_SUPPORT && WolfRunner.ms_zLanguage.equals("ja"))
            FntManager.setFontType(FntManager.FTYPE_SYSTEM);

        ms_bArabicLanguage = ARABIC_SUPPORT && WolfRunner.ms_zLanguage.equals("ar");
        ms_bJapaneseLanguage = JAPANS_SUPPORT && WolfRunner.ms_zLanguage.equals("ja");
        System.gc();
    }

    // language support
    public static boolean ms_bArabicLanguage;
    public static boolean ms_bJapaneseLanguage;
    
    private static final String ARABIC_TEST1 = "\u0627\u0644\u0645\u0633\u062D";//+ "!!!";
    private static final String ARABIC_TEST2 = "\u0627\u0644\u0645\u0633";//!!!";
    private static final String JAPANS_TEST1 = "\u52C9"; // kanji 1
    private static final String JAPANS_TEST2 = "\u5F37"; // kanji 1
    private static final String JAPANS_TEST3 = "\u30CF"; // hiragana
    private static final String JAPANS_TEST4 = "\u30D2"; // hiragana
    private static final String RUSIAN_TEST1 = "\u0460";
    private static final String RUSIAN_TEST2 = "\u0416";
    
    //#if f480
//#     public static final boolean BENGAL_SUPPORT = false;
    //#else
    public static final boolean BENGAL_SUPPORT = Font.getDefaultFont().stringWidth(String.valueOf((char)0x09f7)) != Font.getDefaultFont().stringWidth(String.valueOf((char)0x0985));
    //#endif
    
    //#if ot800
//#     public static final boolean RUSIAN_SUPPORT = false;//!LanguageCheckFontSupport(RUSIAN_TEST1, RUSIAN_TEST2);
    //#else
    public static final boolean RUSIAN_SUPPORT = !LanguageCheckFontSupport(RUSIAN_TEST1, RUSIAN_TEST2);
    //#endif
    
    public static final boolean ARABIC_SUPPORT = Font.getDefaultFont().stringWidth(String.valueOf((char)0x0627)) != Font.getDefaultFont().stringWidth(String.valueOf((char)0x0628));
    public static final boolean ARABIC_PROPER_SUPPORT = !LanguageCheckFontSupport(ARABIC_TEST1, ARABIC_TEST2);
    public static final boolean JAPANS_SUPPORT = !LanguageCheckFontSupport(JAPANS_TEST1, JAPANS_TEST2) && !LanguageCheckFontSupport(JAPANS_TEST3, JAPANS_TEST4);
    
    public static boolean LanguageCheckFontSupport (String _zText1, String _zText2) {
        javax.microedition.lcdui.Image vImage1 = javax.microedition.lcdui.Image.createImage(Font.getDefaultFont().getHeight(), Font.getDefaultFont().getHeight());
        javax.microedition.lcdui.Image vImage2 = javax.microedition.lcdui.Image.createImage(Font.getDefaultFont().getHeight(), Font.getDefaultFont().getHeight());
        
        javax.microedition.lcdui.Graphics g1 = vImage1.getGraphics();
        g1.drawString(_zText1, 0, 0, 0);

        javax.microedition.lcdui.Graphics g2 = vImage2.getGraphics();
        g2.drawString(_zText2, 0, 0, 0);
         
        int iData1[] = new int [vImage1.getWidth() * vImage1.getHeight()];
        int iData2[] = new int [vImage2.getWidth() * vImage2.getHeight()];
        vImage1.getRGB(iData1, 0, vImage1.getWidth(), 0, 0, vImage1.getWidth(), vImage1.getHeight());
        vImage2.getRGB(iData2, 0, vImage2.getWidth(), 0, 0, vImage2.getWidth(), vImage2.getHeight());
        
        boolean bEqual = true;
        for (int i=0; i<iData1.length; i++) {
            if (iData1[i] != iData2[i]) {
                bEqual = false;
                break;
            }
        }        
        return bEqual;
    }
}
