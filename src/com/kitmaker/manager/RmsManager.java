package com.kitmaker.manager;

/*
 * MiscUtils.java
 *
 * Created on 12 de diciembre de 2006, 16:50
 *
 * To change this template, choose Tools | Template Manager and open the
 * template in the editor.
 */

import com.kitmaker.wolfRunner.Main;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class RmsManager {

    public static final int MODE_SAVE = 0;
    public static final int MODE_LOAD = 1;
    
    public static final String FILE_PLY = "HTPLY"; // play data
    public static final String FILE_SYS = "HTSYS"; // system data
    public static final String FILE_ACH = "HTACH"; // achievements
    
    private static int ms_iRmsOffset;
    private static int ms_iRmsMode;
    private static byte ms_iRmsData[];
    private static byte ms_iSaveData[];
    private static String ms_zRmsId;
    
    private static RecordStore ms_RecordStore = null;
    
    public static boolean ms_bSavedData;

    static long longFromBytes(byte[] data, int offset) {
        long iRet = (((char) data[offset  ]) & 0xff) << 56;
        iRet +=     (((char) data[offset+1]) & 0xff) << 48;
        iRet +=     (((char) data[offset+2]) & 0xff) << 40;
        iRet +=     (((char) data[offset+3]) & 0xff) << 32;
        iRet +=     (((char) data[offset+4]) & 0xff) << 24;
        iRet +=     (((char) data[offset+5]) & 0xff) << 16;
        iRet +=     (((char) data[offset+6]) & 0xff) << 8;
        iRet +=     (((char) data[offset+7]) & 0xff);
        return iRet;
    }
    
    private static int intFromBytes(byte[] data, int offset) {
        int iRet = (((char) data[offset]) & 0xff) << 24;
        iRet += (((char) data[offset + 1]) & 0xff) << 16;
        iRet += (((char) data[offset + 2]) & 0xff) << 8;
        iRet += (char) data[offset + 3] & 0xff;
        return iRet;
    }

    private static short shortFromBytes(byte[] data, int offset) {
        int iRet = (((char) data[offset]) & 0xff) << 8;
        iRet += (char) data[offset + 1] & 0xff;
        return (short) iRet;
    }

    private static boolean booleanFromBytes(byte[] data, int[] offset) {
        final boolean bRet = (data[offset[0]] == 0) ? false : true;
        offset[0]++;
        return bRet;
    }

   static int asBytes(long i, byte[] data, int offset) {
      data[offset++] = (byte) ( (i >> 56) & 0xff);
      data[offset++] = (byte) ( (i >> 48) & 0xff);
      data[offset++] = (byte) ( (i >> 40) & 0xff);
      data[offset++] = (byte) ( (i >> 32) & 0xff);
      data[offset++] = (byte) ( (i >> 24) & 0xff);
      data[offset++] = (byte) ( (i >> 16) & 0xff);
      data[offset++] = (byte) ( (i >> 8) & 0xff);
      data[offset]   = (byte) (i & 0xff);
      return 8;
   }
    
    private static int asBytes(int i, byte[] data, int offset) {
        data[offset++] = (byte) ((i >> 24) & 0xff);
        data[offset++] = (byte) ((i >> 16) & 0xff);
        data[offset++] = (byte) ((i >> 8) & 0xff);
        data[offset] = (byte) (i & 0xff);
        return 4;
    }

    private static int asBytes(short i, byte[] data, int offset) {
        data[offset++] = (byte) ((i >> 8) & 0xff);
        data[offset] = (byte) (i & 0xff);
        return 2;
    }

    private static int asBytes(byte i, byte[] data, int offset) {
        data[offset] = i;
        return 1;
    }

    private static int asBytes(boolean i, byte[] data, int offset) {
        data[offset] = (i == true) ? (byte) 1 : (byte) 0;
        return 1;
    }

    private static byte byteFromBytes(byte[] data, int[] offset) {
        final byte cRet = data[offset[0]];
        offset[0]++;
        return cRet;
    }

    private static void RmsInit(String _zFile, int _iRmsMode) {

        ms_zRmsId = _zFile;
        ms_iRmsMode = _iRmsMode;
        ms_iRmsOffset = 0;

        if (ms_iRmsMode == MODE_LOAD) {
            ms_iRmsData = null;
            ms_RecordStore = null;
            //String vVersionString = MIDlet.ms_vInstance.getAppProperty ("MIDlet-Version");
            
            try {
                ms_RecordStore = RecordStore.openRecordStore(_zFile, false);
                final int iNumRecords = ms_RecordStore.getNumRecords();
                if (iNumRecords == 0) {
                    RmsDelete(_zFile);
                    return;
                }

                for (int i = 0; i < iNumRecords; i++) {
                    ms_iRmsData = ms_RecordStore.getRecord(i + 1);
                }
                ms_RecordStore.closeRecordStore();
                
            } catch (Exception e) {
                RmsDelete(_zFile);
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {
            ms_iRmsData = new byte[512];
        }
    }

    private static void RmsClose() {
        if (ms_iRmsMode == MODE_SAVE) {

            ms_iSaveData = new byte[ms_iRmsOffset];
            System.arraycopy(ms_iRmsData, 0, ms_iSaveData, 0, ms_iRmsOffset);
            ms_iRmsData = null;

            if (RmsDelete(ms_zRmsId)) {
                ms_RecordStore = null;

                try {
                    ms_RecordStore = RecordStore.openRecordStore(ms_zRmsId, true);
                    if (ms_iRmsOffset > ms_RecordStore.getSizeAvailable()) {
                        return;
                    }

                    ms_RecordStore.addRecord(ms_iSaveData, 0, ms_iRmsOffset);
                    ms_RecordStore.closeRecordStore();

                } catch (Exception e) {
                    RmsDelete(ms_zRmsId);
                }
            }
        }
    }

    private static boolean RmsProcessData(boolean _bData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _bData = ((ms_iRmsData[ms_iRmsOffset++] == 1) ? true : false);

        } else { //if (ms_iRmsMode == MODE_SAVE) {
            ms_iRmsData[ms_iRmsOffset++] = (byte) (_bData ? 1 : 0);
        }
        return _bData;
    }

    private static byte RmsProcessData(byte _bData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _bData = ms_iRmsData[ms_iRmsOffset++];

        } else { //if (ms_iRmsMode == MODE_SAVE) {
            ms_iRmsData[ms_iRmsOffset++] = _bData;
        }
        return _bData;
    }

    private static short RmsProcessData(short _sData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _sData = shortFromBytes(ms_iRmsData, ms_iRmsOffset);
            ms_iRmsOffset += 2;
        } else { //if (ms_iRmsMode == MODE_SAVE) {
            ms_iRmsOffset += asBytes(_sData, ms_iRmsData, ms_iRmsOffset);
        }
        return _sData;
    }

    private static int RmsProcessData(int _iData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _iData = intFromBytes(ms_iRmsData, ms_iRmsOffset);
            ms_iRmsOffset += 4;
        } else { //if (ms_iRmsMode == MODE_SAVE) {
            ms_iRmsOffset += asBytes(_iData, ms_iRmsData, ms_iRmsOffset);

        }
        return _iData;
    }

    private static long RmsProcessData(long _iData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _iData = longFromBytes(ms_iRmsData, ms_iRmsOffset);
            ms_iRmsOffset += 8;
        } else { //if (ms_iRmsMode == MODE_SAVE) {
            ms_iRmsOffset += asBytes(_iData, ms_iRmsData, ms_iRmsOffset);

        }
        return _iData;
    }
    
    private static String RmsProcessData(String _zData) {
        if (ms_iRmsMode == MODE_LOAD) {

            int iLength = ms_iRmsData[ms_iRmsOffset++];
            _zData = "";
            for (int i = 0; i < iLength; i++) {
                _zData += (char) ms_iRmsData[ms_iRmsOffset++];
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _zData.length();
            char[] iChars = _zData.toCharArray();
            for (int k = 0; k < iChars.length; k++) {
                ms_iRmsData[ms_iRmsOffset++] = (byte) iChars[k];
            }

        }
        return _zData;
    }

    private static boolean[] RmsProcessData(boolean[] _bData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _bData = new boolean[ms_iRmsData[ms_iRmsOffset++]];

            for (int b = 0; b < _bData.length; b++) {
                _bData[b] = ((ms_iRmsData[ms_iRmsOffset++] == 1) ? true : false);
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _bData.length;
            for (int i = 0; i < _bData.length; i++) {
                ms_iRmsData[ms_iRmsOffset++] = (byte) (_bData[i] ? 1 : 0);
            }

        }
        return _bData;
    }

    private static byte[] RmsProcessData(byte[] _bData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _bData = new byte[ms_iRmsData[ms_iRmsOffset++]];

            for (int b = 0; b < _bData.length; b++) {
                _bData[b] = ms_iRmsData[ms_iRmsOffset++];
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {
            ms_iRmsData[ms_iRmsOffset++] = (byte) _bData.length;
            for (int i = 0; i < _bData.length; i++) {
                ms_iRmsData[ms_iRmsOffset++] = _bData[i];
            }
        }
        return _bData;
    }

    private static short[] RmsProcessData(short[] _sData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _sData = new short[ms_iRmsData[ms_iRmsOffset++]];

            for (int s = 0; s < _sData.length; s++) {
                _sData[s] = shortFromBytes(ms_iRmsData, ms_iRmsOffset);
                ms_iRmsOffset += 2;
            }
        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _sData.length;
            for (int i = 0; i < _sData.length; i++) {
                ms_iRmsOffset += asBytes(_sData[i], ms_iRmsData, ms_iRmsOffset);
            }

        }
        return _sData;
    }

    private static int[] RmsProcessData(int[] _iData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _iData = new int[ms_iRmsData[ms_iRmsOffset++]];

            for (int i = 0; i < _iData.length; i++) {
                _iData[i] = intFromBytes(ms_iRmsData, ms_iRmsOffset);
                ms_iRmsOffset += 4;
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _iData.length;
            for (int i = 0; i < _iData.length; i++) {
                ms_iRmsOffset += asBytes(_iData[i], ms_iRmsData, ms_iRmsOffset);
            }
        }
        return _iData;
    }

    private static String[] RmsProcessData(String[] _zData) {
        if (ms_iRmsMode == MODE_LOAD) {

            int iArrayLength = ms_iRmsData[ms_iRmsOffset++];
            for (int i = 0; i < iArrayLength; i++) {
                int iStringLength = ms_iRmsData[ms_iRmsOffset++];
                _zData[i] = "";
                for (int k = 0; k < iStringLength; k++) {
                    _zData[i] += (char) ms_iRmsData[ms_iRmsOffset++];
                }
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _zData.length;
            for (int i = 0; i < _zData.length; i++) {
                ms_iRmsData[ms_iRmsOffset++] = (byte) _zData[i].length();
                char[] iChars = _zData[i].toCharArray();
                for (int k = 0; k < iChars.length; k++) {
                    ms_iRmsData[ms_iRmsOffset++] = (byte) iChars[k];
                }
            }
        }
        return _zData;
    }

    private static boolean[][] RmsProcessData(boolean[][] _bData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _bData = new boolean[ms_iRmsData[ms_iRmsOffset++]][];
            for (int i = 0; i < _bData.length; i++) {
                _bData[i] = new boolean[ms_iRmsData[ms_iRmsOffset++]];
            }

            for (int i = 0; i < _bData.length; i++) {
                for (int j = 0; j < _bData[i].length; j++) {
                    _bData[i][j] = ((ms_iRmsData[ms_iRmsOffset++] == 1) ? true : false);
                }
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _bData.length;
            for (int i = 0; i < _bData.length; i++) {
                ms_iRmsData[ms_iRmsOffset++] = (byte) _bData[i].length;
            }

            for (int i = 0; i < _bData.length; i++) {
                for (int j = 0; j < _bData[i].length; j++) {
                    ms_iRmsData[ms_iRmsOffset++] = (byte) (_bData[i][j] ? 1 : 0);
                }
            }
        }
        return _bData;
    }

    private static byte[][] RmsProcessData(byte[][] _bData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _bData = new byte[ms_iRmsData[ms_iRmsOffset++]][];
            for (int i = 0; i < _bData.length; i++) {
                _bData[i] = new byte[ms_iRmsData[ms_iRmsOffset++]];
            }

            for (int i = 0; i < _bData.length; i++) {
                for (int j = 0; j < _bData[i].length; j++) {
                    _bData[i][j] = ms_iRmsData[ms_iRmsOffset++];
                }
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _bData.length;
            for (int i = 0; i < _bData.length; i++) {
                ms_iRmsData[ms_iRmsOffset++] = (byte) _bData[i].length;
            }

            for (int i = 0; i < _bData.length; i++) {
                for (int j = 0; j < _bData[i].length; j++) {
                    ms_iRmsData[ms_iRmsOffset++] = _bData[i][j];
                }
            }
        }
        return _bData;
    }

    private static short[][] RmsProcessData(short[][] _sData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _sData = new short[ms_iRmsData[ms_iRmsOffset++]][];
            for (int i = 0; i < _sData.length; i++) {
                _sData[i] = new short[ms_iRmsData[ms_iRmsOffset++]];
            }

            for (int i = 0; i < _sData.length; i++) {
                for (int j = 0; j < _sData[i].length; j++) {
                    _sData[i][j] = shortFromBytes(ms_iRmsData, ms_iRmsOffset);
                    ms_iRmsOffset += 2;
                }
            }
        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _sData.length;
            for (int i = 0; i < _sData.length; i++) {
                ms_iRmsData[ms_iRmsOffset++] = (byte) _sData[i].length;
            }

            for (int i = 0; i < _sData.length; i++) {
                for (int j = 0; j < _sData[i].length; j++) {
                    ms_iRmsOffset += asBytes(_sData[i][j], ms_iRmsData, ms_iRmsOffset);
                }
            }

        }
        return _sData;
    }

    private static int[][] RmsProcessData(int[][] _iData) {
        if (ms_iRmsMode == MODE_LOAD) {
            _iData = new int[ms_iRmsData[ms_iRmsOffset++]][];
            for (int i = 0; i < _iData.length; i++) {
                _iData[i] = new int[ms_iRmsData[ms_iRmsOffset++]];
            }

            for (int i = 0; i < _iData.length; i++) {
                for (int j = 0; j < _iData[i].length; j++) {
                    _iData[i][j] = intFromBytes(ms_iRmsData, ms_iRmsOffset);
                    ms_iRmsOffset += 4;
                }
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _iData.length;
            for (int i = 0; i < _iData.length; i++) {
                ms_iRmsData[ms_iRmsOffset++] = (byte) _iData[i].length;
            }

            for (int i = 0; i < _iData.length; i++) {
                for (int j = 0; j < _iData[i].length; j++) {
                    ms_iRmsOffset += asBytes(_iData[i][j], ms_iRmsData, ms_iRmsOffset);
                }
            }
        }
        return _iData;
    }

    private static String[][] RmsProcessData(String[][] _zData) {
        if (ms_iRmsMode == MODE_LOAD) {

            int iArrayLength = ms_iRmsData[ms_iRmsOffset++];
            for (int i = 0; i < iArrayLength; i++) {

                int iSubArrayLength = ms_iRmsData[ms_iRmsOffset++];

                for (int j = 0; j < iSubArrayLength; j++) {

                    int iStringLength = ms_iRmsData[ms_iRmsOffset++];
                    _zData[i][j] = "";
                    for (int k = 0; k < iStringLength; k++) {
                        _zData[i][j] += (char) ms_iRmsData[ms_iRmsOffset++];
                    }
                }
            }

        } else { //if (ms_iRmsMode == MODE_SAVE) {

            ms_iRmsData[ms_iRmsOffset++] = (byte) _zData.length;

            for (int i = 0; i < _zData.length; i++) {

                ms_iRmsData[ms_iRmsOffset++] = (byte) _zData[i].length;

                for (int j = 0; j < _zData[i].length; j++) {

                    ms_iRmsData[ms_iRmsOffset++] = (byte) _zData[i][j].length();
                    char[] iChars = _zData[i][j].toCharArray();
                    for (int k = 0; k < iChars.length; k++) {
                        ms_iRmsData[ms_iRmsOffset++] = (byte) iChars[k];
                    }
                }
            }
        }
        return _zData;
    }

    private static boolean RmsDelete(String _File) {

        //#if OptHeap < 2

        ms_RecordStore = null;

        try {
            ms_RecordStore = RecordStore.openRecordStore(_File, true);

            if (ms_RecordStore != null) {
                ms_RecordStore.closeRecordStore();
                RecordStore.deleteRecordStore(_File);
                ms_RecordStore = null;
            }
        } catch (RecordStoreException rse) {
            return true;
        } catch (Exception e) {
            return false;
        }
        //#endif
        return true;
    }

    /**
     * To load data
     * <P>
     * All the data is saved in different groups (system, play data, records, etc...) In this class you will find defined FILE_PLY and FILE_SYS. 
     * If you need more data file just create the constant and add an if stament in this class. Inside the ifs add the data you need to save.
     * The format is always the same, independing on the kind of data, and allowing until two dimension arrays. The format is as follows:
     * <P>
     *          ms_iDataToSaveOrLoad           = RmsProcessData(ms_iDataToSaveOrLoad);
     * <P>
     * @param _zFile the name of the file in where we load data. It is recomended to use constants like FILE_PLY and then create a consistent extructure inside this method.
     */
    public static void RmsLoadData (String _zFile) {
        RmsLoadSaveData (_zFile, MODE_LOAD);
    }
   
    /**
     * To save data
     * <P>
     * All the data is saved in different groups (system, play data, records, etc...) In this class you will find defined FILE_PLY and FILE_SYS. 
     * If you need more data file just create the constant and add an if stament in this class. Inside the ifs add the data you need to save.
     * The format is always the same, independing on the kind of data, and allowing until two dimension arrays. The format is as follows:
     * <P>
     *          ms_iDataToSaveOrLoad           = RmsProcessData(ms_iDataToSaveOrLoad);
     * <P>
     * @param _zFile the name of the file in where we load data. It is recomended to use constants like FILE_PLY and then create a consistent extructure inside this method.
     * @param _iMode can be MODE_SAVE or MODE_LOAD
     */
    public static void RmsSaveData (String _zFile) {
        RmsLoadSaveData (_zFile, MODE_SAVE);
    }
    
    /**
     * To save or load data. <P> All the data is saved in different groups
     * (system, play data, records, etc...) In this class you will find defined
     * FILE_PLY and FILE_SYS. If you need more data file just create the
     * constant and add an if stament in this method. Inside the ifs add the
     * data you need to save. The format is always the same, independing on the
     * kind of data, and allowing until two dimension arrays. The format is as
     * follows: <P> ms_iDataToSaveOrLoad = RmsProcessData(ms_iDataToSaveOrLoad);
     * <P>
     *
     * @param _zFile the name of the file in where we save/load data. It is
     * recomended to use constants like FILE_PLY and then create a consistent
     * extructure inside this method.
     * @param _iMode can be MODE_SAVE or MODE_LOAD
     */
    public static void RmsLoadSaveData(String _zFile, int _iMode) {

        RmsInit(_zFile, _iMode);
        
        ms_bSavedData = (ms_iRmsData != null);
        
        if (ms_iRmsData == null) {
            return;
        }

        //////////////////////////////////////////////////////////////////////////
        if (_zFile.equals(FILE_PLY)) {
            // player data
            //ModeGame.walkthroughFinished =  RmsProcessData(ModeGame.walkthroughFinished);
            
        //////////////////////////////////////////////////////////////////////////
        } else if (_zFile.equals(FILE_SYS)) {
            Main.ms_bVibration = RmsProcessData(Main.ms_bVibration);
            TxtManager.ms_iLanguage = RmsProcessData(TxtManager.ms_iLanguage);
            
        } else if (_zFile.equals(FILE_ACH)) {
//            ModeMenu.ms_bAchievements = RmsProcessData(ModeMenu.ms_bAchievements);
        }
        //////////////////////////////////////////////////////////////////////////

        RmsClose();

    }
}
