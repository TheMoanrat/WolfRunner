package javak.microedition.lcdui;

//#if MIDP

//#define RenderMode = "MIDlet"

//#if API=="Nokia" || API == "SonyEricsson"
//#define DirectGraphics
//#endif

//#elif DOJA
//#     
//#define RenderMode = "Doja"
//# 
//#elif RIM
//#define RenderMode = "UIApp"
//#endif
///#define RenderMode = "OpenGL"
/**
 *
 * @author Venus-Kitmaker
 */
public class Font {

    //#if RenderMode == "MIDlet"
    javax.microedition.lcdui.Font m_vFont;
    //#elif RenderMode == "UIApp"
//#     net.rim.device.api.ui.Font m_vFont;
    //#elif RenderMode == "Doja"
//#     com.nttdocomo.ui.Font m_vFont;
    //#endif
    //#if RenderMode == "UIApp"
    //#if ScreenHeight > 240
//#      private static final int FONT_SIZE[] = {
//#      net.rim.device.api.system.Display.getHeight() / 30,
//#      net.rim.device.api.system.Display.getHeight() / 26,
//#      net.rim.device.api.system.Display.getHeight() / 22,};
     //#else
//#     private static final int FONT_SIZE[] = {
//#         net.rim.device.api.system.Display.getHeight() / 24,
//#         net.rim.device.api.system.Display.getHeight() / 20,
//#         net.rim.device.api.system.Display.getHeight() / 16,};
    //#endif
    //#endif
    
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_UNDERLINED = 4;
    public static final int SIZE_SMALL = 8;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_LARGE = 16;
    public static final int FACE_SYSTEM = 0;
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;
    public static final int FONT_STATIC_TEXT = 0;
    public static final int FONT_INPUT_TEXT = 1;

    /*
     * public static final float FNT_SIZE_PROPORTION [] = {0.75f, 1f, 1.5f};
     * public static final float FNT_SIZE_SMALL = 16 *
     * FNT_SIZE_PROPORTION[Settings.ms_iSize]; public static final float
     * FNT_SIZE_MEDIU = 24 * FNT_SIZE_PROPORTION[Settings.ms_iSize]; public
     * static final float FNT_SIZE_LARGE = 32 *
     * FNT_SIZE_PROPORTION[Settings.ms_iSize]; public static int FNT_SIZE [] = {
     * (int) FNT_SIZE_SMALL, (int) FNT_SIZE_MEDIU, (int) FNT_SIZE_LARGE };
   *
     */
    private int m_iFontFace, m_iFontStyle, m_iFontSize;
    private int m_iFontHeight;

    public Font(int _iFontSpecifier) {
    }

    public Font(int _iFace, int _iStyle, int _iSize) {

        m_iFontFace = _iFace;
        m_iFontStyle = _iStyle;
        m_iFontSize = _iSize;

        //#if RenderMode == "MIDlet"
        m_vFont = javax.microedition.lcdui.Font.getFont(_iFace, _iStyle, _iSize);
        
        //#elif RenderMode == "UIApp"
//#         int iHeight = FONT_SIZE[2];
//#         if (m_iFontSize == SIZE_MEDIUM) {
//#             iHeight = FONT_SIZE[1];
//#         } else if (m_iFontSize == SIZE_LARGE) {
//#             iHeight = FONT_SIZE[0];
//#         }
//# 
//#         m_vFont = net.rim.device.api.ui.Font.getDefault().derive(m_iFontStyle, iHeight);
//#         
        //#elif RenderMode == "Doja"
//#         
        //#endif

    }

    //#if RenderMode == "MIDlet"
    public Font (javax.microedition.lcdui.Font _vFont) {
        m_vFont = _vFont;
    }
    //#elif RenderMode == "UIApp"
//#     public Font(net.rim.device.api.ui.Font _vFont) {
//#         m_vFont = _vFont;
//#     }
    //#elif RenderMode == "Doja"
//#     public Font (com.nttdocomo.ui.Font _vFont) {
//#         m_vFont = _vFont;
//#     }
    //#endif

    public static Font getFont(int _iFontSpecifier) {
        return new Font(_iFontSpecifier);
    }

    public static Font getFont(int _iFace, int _iStyle, int _iSize) {
        return new Font(_iFace, _iStyle, _iSize);
    }

    public static Font getDefaultFont() {
        //#if RenderMode == "MIDlet"
        return new Font (javax.microedition.lcdui.Font.getDefaultFont());
        //#elif RenderMode == "UIApp"
//#         return new Font(net.rim.device.api.ui.Font.getDefault());
        //#elif RenderMode == "Doja"
//#         return new Font (com.nttdocomo.ui.Font.getDefaultFont());
        //#endif
    }

    public int getStyle() {
        return m_iFontStyle;
    }

    public int getSize() {
        return m_iFontSize;
    }

    public int getFace() {
        return m_iFontFace;
    }

    public boolean isPlain() {
        return ((m_iFontStyle & STYLE_BOLD) == 0);
    }

    public boolean isBold() {
        return ((m_iFontStyle & STYLE_BOLD) != 0);
    }

    public boolean isItalic() {
        return ((m_iFontStyle & STYLE_ITALIC) != 0);
    }

    public boolean isUnderlined() {
        return ((m_iFontStyle & STYLE_UNDERLINED) != 0);
    }

    public int getHeight() {
        return m_vFont.getHeight();
    }

    public int getBaselinePosition() {
        //#if RenderMode == "MIDlet"
        return m_vFont.getBaselinePosition();
        //#elif RenderMode == "UIApp"
//#         return m_vFont.getBaseline();
        //#elif RenderMode == "Doja"
//#         return m_vFont.getDescent();
        //#endif
    }

    public int charWidth(char ch) {
        //#if RenderMode == "MIDlet"
        return m_vFont.charWidth(ch);
        //#elif RenderMode == "UIApp"
//#         return m_vFont.getAdvance(ch);
        //#elif RenderMode == "Doja"
//#         return m_vFont.stringWidth(String.valueOf(ch));
        //#endif
    }

    public int stringWidth(String _zStr) {
        //#if RenderMode == "MIDlet"
        return m_vFont.stringWidth(_zStr);
        //#elif RenderMode == "UIApp"
//#         return m_vFont.getAdvance(_zStr);
        //#elif RenderMode == "Doja"
//#         return m_vFont.stringWidth(_zStr);
        //#endif
    }

    public int substringWidth(String _zStr, int _iOffset, int _iLen) {
        //#if RenderMode == "MIDlet"
        return m_vFont.substringWidth(_zStr, _iOffset, _iLen);
        //#elif RenderMode == "UIApp"
//#         return m_vFont.getAdvance(_zStr, _iOffset, _iLen);
        //#elif RenderMode == "Doja"
//#         return m_vFont.stringWidth(_zStr.substring(_iOffset, _iLen));
        //#endif
    }
}
