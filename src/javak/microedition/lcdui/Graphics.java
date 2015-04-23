package javak.microedition.lcdui;

// Defines. WARNING! If you edit this you will need to change the defines in the Image class too!

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
//# 
    //#define RenderMode = "UIApp"
//# 
//#endif

//#if s60dp2lp || s60dp3
//#define UseSpriteForFlip
//#endif

//#if DirectGraphics && (s60dp2lp || s60dp2hp || s60dp3n73)
//#define UseDirectGraphicsGetPixels
//#endif


//#ifdef DirectGraphics
//# import com.nokia.mid.ui.DirectGraphics;
//# import com.nokia.mid.ui.DirectUtils;
//#endif

//#if RenderMode == "UIApp"
//# import net.rim.device.api.math.Fixed32;
//# import net.rim.device.api.ui.XYRect;
//#endif

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Venus-Kitmaker
 */
public class Graphics {

    //#if RenderMode == "UIApp"
//#     public static final boolean ARGB_SUPPORTED = true;
    //#elif UseRGB != "false"
    public static final boolean ARGB_SUPPORTED = true;
    //#else
//#     public static final boolean ARGB_SUPPORTED = false;
    //#endif
    
    
    private static final int FP_BITS = 13;
    
    /** Constant for centering text and images horizontally around the anchor point */
    public static final int HCENTER = 1;
    /** Constant for centering images vertically around the anchor point. */
    public static final int VCENTER = 2;
    /** Constant for positioning the anchor point of text and images to the left of the text or image. */
    public static final int LEFT = 4;
    /** Constant for positioning the anchor point of text and images to the right of the text or image. */
    public static final int RIGHT = 8;
    /** Constant for positioning the anchor point of text and images above the text or image. */
    public static final int TOP = 16;
    /** Constant for positioning the anchor point of text and images below the text or image. */
    public static final int BOTTOM = 32;
    /** Constant for positioning the anchor point at the baseline of text. */
    public static final int BASELINE = 64; 
    
    /** Constant for the SOLID stroke style. */
    public static final int SOLID = 0;
    /** Constant for the DOTTED stroke style. */
    public static final int DOTTED = 1;

    //#if RenderMode == "MIDlet" || RenderMode == "UiApp"
    /** Causes the Image to appear reflected about its vertical center. */
    public static final int HFLIP = javax.microedition.lcdui.game.Sprite.TRANS_MIRROR;
    /** Causes the Sprite to appear reflected about its vertical center and then rotated clockwise by 180 degrees. */
    public static final int VFLIP = javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180;
    //#elif RenderMode == "Doja"
//#     /** Causes the Image to appear reflected about its vertical center. */
//#     public static final int HFLIP = com.nttdocomo.opt.ui.Graphics2.FLIP_HORIZONTAL;
//#     /** Causes the Sprite to appear reflected about its vertical center and then rotated clockwise by 180 degrees. */
//#     public static final int VFLIP = com.nttdocomo.opt.ui.Graphics2.FLIP_VERTICAL;
    //#else
//#     /** Causes the Image to appear reflected about its vertical center. */
//#     public static final int HFLIP = 0;
//#     /** Causes the Sprite to appear reflected about its vertical center and then rotated clockwise by 180 degrees. */
//#     public static final int VFLIP = 0;
    //#endif
    
    private int m_iAlpha;
    private int m_iSizeX;
    private int m_iSizeY;
    private int m_iWidth;
    private int m_iHeight;

    //#if RenderMode == "UIApp"
//#     private int m_iStrokeStyle;
//#     private boolean m_bClip;
    //#endif
    
    //#if RenderMode == "Doja"
//#     private int m_iTraslateX, m_iTraslateY;
//#     private int m_iR, m_iG, m_iB;
    //#endif

    private Font m_vFont = Font.getDefaultFont();
    private int m_iClip[] = new int[4];
    private int m_iOldClip[] = new int[4];
    
    //private static int ms_iTransparentColor;
    //private static Image ms_vTransparentImage;
    
    //#if RenderMode == "MIDlet"
    public javax.microedition.lcdui.Graphics m_Graphics;
    //#elif RenderMode == "Doja"
//#     public com.nttdocomo.ui.Graphics m_Graphics;
//#     public com.nttdocomo.opt.ui.Graphics2 m_Graphics2;
    //#elif RenderMode == "UIApp"
//#     public net.rim.device.api.ui.Graphics m_Graphics;
    //#endif

    //#if DirectGraphics
//#     public DirectGraphics m_DirectGraphics = null;
    //#endif
    
    // TODO: necesito pensar mejor como hacer convivir esto con UIAplication
    // Ademas aqui no se crea ningun tipo de matriz para guardar datos, solo se obtiene el verdadero objeto Graphics del setGraphics
    // Hay que estudiar eso
    public Graphics(int _iW, int _iH) {

        m_iAlpha = 255;
        m_iSizeX = 256;
        m_iSizeY = 256;

        m_iWidth = _iW;
        m_iHeight = _iH;
        
        m_iClip[2] = m_iOldClip[2] = _iW;
        m_iClip[3] = m_iOldClip[3] = _iH;
        
        // TODO: Esto es hipoteticamente para BB
      /*
         * try { Image vImage = Image.createImage(_iW, _iH); m_Graphics = new
         * net.rim.device.api.ui.Graphics(vImage.m_vImage);
         *
         * } catch (IOException ex) { }
         */

        // TODO: esto hay que ponerlo en otro sitio, no se puede cargar la imagen cada vez que creamos un objeto Graphics nuevo

        // get the device transparent color to optimize the drawRGB funtions
        /*
        if (ms_iTransparentColor == 0) {
            try {
                int[] srcPixels = new int[1];

                //#if MIDP && !s60dp2lp && !s60dp2hp
                ms_vTransparentImage = Image.createImage("/transparent.png");
                ms_vTransparentImage.getRGB(srcPixels, 0, 1, 0, 0, 1, 1);
                //#endif

                ms_iTransparentColor = srcPixels[0];
                ms_vTransparentImage = null;

            } catch (Exception ex) {
                ms_iTransparentColor = 0x00000000;
                //#if API == "Nokia"
//#                 ms_iTransparentColor = 0xffffffff;
                //#endif
            }
        }
        */
    }

    public boolean m_bGetGraphics;
    
    //#if RenderMode == "UIApp"
//#     public void setGraphics (net.rim.device.api.ui.Graphics _g) {
    //#elif RenderMode == "Doja"
//#     public void setGraphics(com.nttdocomo.ui.Graphics _g) {
    //#else
    public void setGraphics(javax.microedition.lcdui.Graphics _g) {
    //#endif

        m_Graphics = _g;
        
        //#if DirectGraphics
//#         m_DirectGraphics = DirectUtils.getDirectGraphics (_g);
        //#endif
        
        if (!m_bGetGraphics) {

            //#if RenderMode == "MIDlet"
            m_iWidth = _g.getClipWidth();
            m_iHeight = _g.getClipHeight();
            
            //#elif RenderMode == "UIApp"
//#             m_iWidth = _g.getClippingRect().width;
//#             m_iWidth = _g.getClippingRect().height;
            //#elif RenderMode == "Doja"
    //#         m_Graphics2 = (com.nttdocomo.opt.ui.Graphics2) _g;
    //#         m_iWidth = com.nttdocomo.ui.Display.getWidth();
    //#         m_iHeight = com.nttdocomo.ui.Display.getHeight();
            //#endif
            
            m_bGetGraphics = true;
        }        
    }

    /**
     * Translates the origin of the graphics context to the point (x, y) in the 
     * current coordinate system. All coordinates used in subsequent rendering 
     * operations on this graphics context will be relative to this new origin. <P>
     * The effect of calls to translate() are cumulative. For example, calling 
     * translate(1, 2) and then translate(3, 4) results in a translation of (4, 6). <P>
     * The application can set an absolute origin (ax, ay) using the following technique: <P>
     * g.translate(ax - g.getTranslateX(), ay - g.getTranslateY()) <P>
     * @param _iX the x coordinate of the new translation origin
     * @param _iY the y coordinate of the new translation origin
     */
    public void translate(int _iX, int _iY) {
        //#if RenderMode == "Doja"
//#         m_iTraslateX = _iX;
//#         m_iTraslateY = _iY;
//#         m_Graphics.setOrigin(_iX, _iY);
        //#else
        m_Graphics.translate(_iX, _iY);
        //#endif
    }

    /**
     * Gets the X coordinate of the translated origin of this graphics context.
     * @return X of current origin
     */
    public int getTranslateX() {
        //#if RenderMode == "Doja"
//#         return m_iTraslateX;
        //#else
        return m_Graphics.getTranslateX();
        //#endif
    }
    
    /**
     * Gets the Y coordinate of the translated origin of this graphics context.
     * @return Y of current origin
     */
    public int getTranslateY() {
        //#if RenderMode == "Doja"
//#         return m_iTraslateY;
        //#else
        return m_Graphics.getTranslateY();
        //#endif
    }

    /**
     * Sets the stroke style used for drawing lines, arcs, rectangles, and rounded rectangles. This does not affect fill, text, and image operations.
     * @param _iStyle can be SOLID  or DOTTED
     * @throws IllegalArgumentException if the style is illegal
     * @see getStrokeStyle()
     */
    public void setStrokeStyle(int _iStyle) {
        //#if RenderMode == "MIDlet"
        m_Graphics.setStrokeStyle(_iStyle);
        //#elif RenderMode == "UIApp"
//#         m_iStrokeStyle = _iStyle;
        //#if RIM >= "5.0"
//#         m_Graphics.setStrokeStyle(_iStyle);
        //#endif
        //#elif RenderMode == "Doja"
        //#endif
    }
    
    /**
     * Gets the stroke style used for drawing operations.
     * @return stroke style, SOLID or DOTTED
     * @see setStrokeStyle(int)
     */
    public int getStrokeStyle() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getStrokeStyle();
        //#elif RenderMode == "UIApp"
//#         return m_iStrokeStyle;
        //#elif RenderMode == "Doja"
//#         return 0;
        //#endif
    }
    
    /**
     * Sets the current clip to the rectangle specified by the given coordinates. Rendering operations have no effect outside of the clipping area.
     * @param _iX the x coordinate of the new clip rectangle
     * @param _iY the y coordinate of the new clip rectangle
     * @param _iW the width of the new clip rectangle
     * @param _iH the height of the new clip rectangle
     * @see clipRect(int, int, int, int)
     * @see saveClip()
     * @see restoreClip()
     */
    public void setClip(int _iX, int _iY, int _iW, int _iH) {
        //#if RenderMode == "MIDlet"
        m_Graphics.setClip(_iX, _iY, _iW, _iH);
        
        //#elif RenderMode == "UIApp"
//#         m_iClip[0] = _iX;
//#         m_iClip[1] = _iY;
//#         m_iClip[2] = _iW;
//#         m_iClip[3] = _iH;
//#         
//#         m_bClip = !(_iX == 0 && _iY == 0 && _iW == m_iWidth && _iH == m_iHeight);
//#         
        //#elif RenderMode == "Doja"
//#         m_iClip[0] = _iX;
//#         m_iClip[1] = _iY;
//#         m_iClip[2] = _iW;
//#         m_iClip[3] = _iH;
//#         m_Graphics.setClip(_iX, _iY, _iW, _iH);
        //#endif
    }

    /**
     * Intersects the current clip with the specified rectangle. The resulting 
     * clipping area is the intersection of the current clipping area and the 
     * specified rectangle. This method can only be used to make the current 
     * clip smaller. To set the current clip larger, use the setClip method. 
     * Rendering operations have no effect outside of the clipping area.
     * @param _iX the x coordinate of the rectangle to intersect the clip with
     * @param _iY the y coordinate of the rectangle to intersect the clip with
     * @param _iW the width of the rectangle to intersect the clip with
     * @param _iH the height of the rectangle to intersect the clip with
     * @see setClip(int, int, int, int)
     * @see saveClip()
     * @see restoreClip()
     */
    public void clipRect(int _iX, int _iY, int _iW, int _iH) {
        
        //#if RenderMode == "MIDlet" || RenderMode == "Doja"
        m_Graphics.clipRect(_iX, _iY, _iW, _iH);
        
        //#elif RenderMode == "UIApp"
//#         int iOriX = m_iClip[0];
//#         int iOriY = m_iClip[1];
//#         m_iClip[0] = Math.max (m_iClip[0], _iX);
//#         m_iClip[1] = Math.max (m_iClip[1], _iY);
//#         m_iClip[2] = Math.min (iOriX + m_iClip[2], _iX + _iW) - m_iClip[0];
//#         m_iClip[3] = Math.min (iOriY + m_iClip[3], _iY + _iH) - m_iClip[1];
//#         
//#         m_bClip = !(_iX == 0 && _iY == 0 && _iW == m_iWidth && _iH == m_iHeight);
//#         
        //#elif RenderMode == "Doja"
//#         m_iClip[0] = _iX;
//#         m_iClip[1] = _iY;
//#         m_iClip[2] = _iW;
//#         m_iClip[3] = _iH;
//#         m_Graphics.clipRect(_iX, _iY, _iW, _iH);
//#         
        //#endif
    }

    /**
     * Save current clip.
     */
    public void saveClip() {
        
        //#if RenderMode == "MIDlet"
        m_iOldClip[0] = m_Graphics.getClipX();
        m_iOldClip[1] = m_Graphics.getClipY();
        m_iOldClip[2] = m_Graphics.getClipWidth();
        m_iOldClip[3] = m_Graphics.getClipHeight();
        //#elif RenderMode == "UIApp"
//#         m_iOldClip[0] = m_iClip[0];
//#         m_iOldClip[1] = m_iClip[1];
//#         m_iOldClip[2] = m_iClip[2];
//#         m_iOldClip[3] = m_iClip[3];
        //#elif RenderMode == "Doja"
//#         System.arraycopy(m_iClip, 0, m_iOldClip, 0, m_iClip.length);
        //#endif
    }

    /**
     * Restores previously saved clip with saveClip()
     */    
    public void restoreClip() {
        
        //#if RenderMode == "MIDlet" || RenderMode == "Doja"
        m_Graphics.setClip(m_iOldClip[0], m_iOldClip[1], m_iOldClip[2], m_iOldClip[3]);
        //#elif RenderMode == "UIApp"
//#         System.arraycopy(m_iOldClip, 0, m_iClip, 0, m_iClip.length);
//#         m_bClip = !(m_iClip[0] == 0 && m_iClip[1] == 0 && m_iClip[2] == m_iWidth && m_iClip[3] == m_iHeight);
        //#endif
    }

    /**
     * Gets the X offset of the current clipping area, relative to the coordinate 
     * system origin of this graphics context. Separating the getClip operation 
     * into two methods returning integers is more performance and memory efficient 
     * than one getClip() call returning an object.
     * @return X offset of the current clipping area
     * @see clipRect(int, int, int, int)
     * @see setClip(int, int, int, int)
     * @see saveClip()
     * @see restoreClip()
     */
    public int getClipX() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getClipX();
        //#elif RenderMode == "UIApp"
//#         return m_iClip[0];
        //#elif RenderMode == "Doja"
//#         return m_iClip[0];
        //#endif
    }

    /**
     * Gets the Y offset of the current clipping area, relative to the coordinate 
     * system origin of this graphics context. Separating the getClip operation 
     * into two methods returning integers is more performance and memory efficient 
     * than one getClip() call returning an object.
     * @return Y offset of the current clipping area
     * @see clipRect(int, int, int, int)
     * @see setClip(int, int, int, int)
     * @see saveClip()
     * @see restoreClip()
     */
    public int getClipY() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getClipY();
        //#elif RenderMode == "UIApp"
//#       return m_iClip[1];
        //#elif RenderMode == "Doja"
//#         return m_iClip[1];
        //#endif
    }

    /**
     * Gets the width of the current clipping area.
     * @return width of the current clipping area
     * @see clipRect(int, int, int, int)
     * @see setClip(int, int, int, int)
     * @see saveClip()
     * @see restoreClip()
     */
    public int getClipWidth() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getClipWidth();
        //#elif RenderMode == "UIApp"
//#         return m_iClip[2];
        //#elif RenderMode == "Doja"
//#         return m_iClip[2];
        //#endif
    }

    /**
     * Gets the height of the current clipping area.
     * @return height of the current clipping area.
     * @see clipRect(int, int, int, int)
     * @see setClip(int, int, int, int)
     * @see saveClip()
     * @see restoreClip()
     */
    public int getClipHeight() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getClipHeight();
        //#elif RenderMode == "UIApp"
//#         return m_iClip[3];
        //#elif RenderMode == "Doja"
//#         return m_iClip[3];
        //#endif
    }
    
    /**
     * Set alpha level. <P> Alpha level in J2ME is pretty tricky. Some devices 
     * can show a wide range of alpha level, others may have only few levels, 
     * and others just can not display any alpha transparency. <P>
     * Some devices has so few alpha levels (like Motorola V9, that only have 4) 
     * that it should be better to not use it at all in some situations. <P> 
     * Please note that alpha transparency on primitives only will work on 
     * SonyEricsson & Nokia devices. Images can use alpha transparency in more brands, 
     * depending on the device. <P>
     * In the proyect's propierties you can find "UseAlpha" value, that can be true or false. 
     * @param _iA alpha leves (from 0 to 255)
     * @see getAlpha()
     */
    
    public void setAlpha(int _iA) {
        m_iAlpha = Math.min(_iA, 255);
        //#if RenderMode == "UIApp"
//#         m_Graphics.setGlobalAlpha(m_iAlpha);
        //#endif
        
        //#if DirectGraphics
//#         m_DirectGraphics.setARGBColor((_iA<<24)|getColor());
        //#endif
    }
    
    /**
     * Gets the alpha component of the current color.
     * @return integer value in range 0-255
     * @see setAlpha(int)
     */
    public int getAlpha() {
        return m_iAlpha;
    }

    /**
     * Sets the current grayscale to be used for all subsequent rendering operations. 
     * For monochrome displays, the behavior is clear. For color displays, 
     * this sets the color for all subsequent drawing operations to be a gray color 
     * equivalent to the value passed in. The value must be in the range 0-255.
     * @param _iValue the desired grayscale value 
     * @throws IllegalArgumentException - if the gray value is out of range
     * @see getGrayScale()
     */
    public void setGrayScale(int _iValue) {
        //#if RenderMode == "MIDlet"
        m_Graphics.setGrayScale(_iValue);
        //#endif
    }

    /**
     * Gets the current grayscale value of the color being used for rendering operations. 
     * If the color was set by setGrayScale(), that value is simply returned. 
     * If the color was set by one of the methods that allows setting of the 
     * red, green, and blue components, the value returned is computed from the 
     * RGB color components (possibly in a device-specific fashion) that 
     * best approximates the brightness of that color.
     * @return integer value in range 0-255
     * @see setGrayScale(int)
     */
    public int getGrayScale() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getGrayScale();
        //#else
//#         return 0;
        //#endif
    }
    
    /**
     * Sets the current color to the specified RGB values. All subsequent rendering operations will use this specified color.
     * @param _iR the red component of the color being set in range 0-255
     * @param _iG the green component of the color being set in range 0-255
     * @param _iB the blue component of the color being set in range 0-255
     * @throws IllegalArgumentException - if any of the color components are outside of range 0-255
     * @see getColor()
     */
    public void setColor(int _iR, int _iG, int _iB) {
        m_Graphics.setColor((_iR << 16) | (_iG << 8) | _iB);
    }

    /**
     * Sets the current color to the specified ARGB values. All subsequent rendering operations will use this specified color.
     * @param _iA the alpha component of the color being set in range 0-255
     * @param _iR the red component of the color being set in range 0-255
     * @param _iG the green component of the color being set in range 0-255
     * @param _iB the blue component of the color being set in range 0-255
     * @throws IllegalArgumentException - if any of the color components are outside of range 0-255
     * @see getColor()
     */
    public void setColor(int _iA, int _iR, int _iG, int _iB) {
        m_iAlpha = _iA;

        //#if DirectGraphics && UseAlpha == "true"
//#         m_DirectGraphics.setARGBColor((_iA<<24)|(_iR<<16)|(_iG<<8)|_iB);
        //#elif RenderMode == "Doja"
//#         m_Graphics.setColor(com.nttdocomo.ui.Graphics.getColorOfRGB(_iR, _iG, _iB, _iA));
        //#else
        m_Graphics.setColor((_iR << 16) | (_iG << 8) | _iB);
        //#endif

        //#if RenderMode == "UIApp"
//#         m_Graphics.setGlobalAlpha(m_iAlpha);
        //#endif

    }

    /**
     * Sets the current color to the specified ARGB values. All subsequent rendering operations will use this specified color. 
     * The ARGB value passed in is interpreted with the least significant eight bits giving the blue component, 
     * the next eight more significant bits giving the green component, the next eight more significant bits giving the red component, 
     * and the next eight more significant bits giving the alpha component. That is to say, the color component is specified in the form of 0xAARRGGBB. 
     * If the high order byte of this value is 0 it will be ignored ignored in orther to get compatibility with the javax.microedition.lcdui.Graphic.setColor(int _iRGB)
     * @param _iColor the color being set
     * @ see getColor()
     */
    public void setColor(int _iARGB) {

        int iA = (_iARGB >> 24) & 0xFF;
        if (iA > 0) {
            m_iAlpha = iA;
        }

        //#if DirectGraphics && UseAlpha == "true"
//#         int iR = (_iARGB >> 16) & 0xFF;
//#         int iG = (_iARGB >> 8)  & 0xFF;
//#         int iB = (_iARGB)       & 0xFF;
//# 
//#         m_DirectGraphics.setARGBColor((m_iAlpha<<24)|(iR<<16)|(iG<<8)|iB);
//#     
        //#elif RenderMode == "Doja"
//#         int iR = (_iARGB >> 16) & 0xFF;
//#         int iG = (_iARGB >> 8)  & 0xFF;
//#         int iB = (_iARGB)       & 0xFF;
//# 
//#         m_Graphics.setColor(com.nttdocomo.ui.Graphics.getColorOfRGB(iR, iG, iB, m_iAlpha));
//#         
        //#else
        m_Graphics.setColor(_iARGB);
        //#endif

        //#if RenderMode == "UIApp"
//#         m_Graphics.setGlobalAlpha(m_iAlpha);
        //#endif

    }

    /**
     * Sets the current color to the specified ARGB values. 
     * @param _iAlpha the alpha being set
     * @param _iColor the color being set
     * @ see getColor()
     */
    public void setColor(int _iAlpha, int _iColor) {

        m_iAlpha = _iAlpha;

        //#if DirectGraphics && UseAlpha == "true"
//#         int iR = (_iColor >> 16) & 0xFF;
//#         int iG = (_iColor >> 8)  & 0xFF;
//#         int iB = (_iColor)       & 0xFF;
//# 
//#         m_DirectGraphics.setARGBColor((m_iAlpha<<24)|(iR<<16)|(iG<<8)|iB);
//# 
        //#elif RenderMode == "Doja"
//#         int iR = (_iColor >> 16) & 0xFF;
//#         int iG = (_iColor >> 8)  & 0xFF;
//#         int iB = (_iColor)       & 0xFF;
//# 
//#         m_Graphics.setColor(com.nttdocomo.ui.Graphics.getColorOfRGB(iR, iG, iB, m_iAlpha));
        //#else
        m_Graphics.setColor(_iColor);
        //#endif

        //#if RenderMode == "UIApp"
//#         m_Graphics.setGlobalAlpha(m_iAlpha);
        //#endif

    }

    /**
     * Gets the red component of the current color.
     * @return integer value in range 0-255
     * @see setColor(int, int, int)
     */
    public int getRedComponent() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getRedComponent();
        //#elif RenderMode == "UIApp"
//#         int iColor = m_Graphics.getColor();
//#         return (iColor >> 16) & 0xFF;
        //#elif RenderMode == "Doja"
//#         return m_iR;
        //#endif
    }

    /**
     * Gets the green component of the current color.
     * @return integer value in range 0-255
     * @see setColor(int, int, int)
     */
    public int getGreenComponent() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getGreenComponent();
        //#elif RenderMode == "UIApp"
//#         int iColor = m_Graphics.getColor();
//#         return (iColor >> 8) & 0xFF;
        //#elif RenderMode == "Doja"
//#         return m_iG;
        //#endif
    }

    /**
     * Gets the blue component of the current color.
     * @return integer value in range 0-255
     * @see setColor(int, int, int)
     */
    public int getBlueComponent() {
        //#if RenderMode == "MIDlet"
        return m_Graphics.getBlueComponent();
        //#elif RenderMode == "UIApp"
//#         int iColor = m_Graphics.getColor();
//#         return iColor & 0xFF;
        //#elif RenderMode == "Doja"
//#         return m_iB;
        //#endif
    }

    /**
     * Gets the current color.
     * @return an integer in form 0x00RRGGBB
     * @see setColor(int, int, int)
     */
    public int getColor() {
        //#if RenderMode == "MIDlet" || RenderMode == "UIApp"
        return m_Graphics.getColor();
        //#elif RenderMode == "Doja"
//#         return (m_iR<<16)|(m_iG<<8)|m_iB;
        //#endif
    }
    
    /**
     * Gets a blended color between the two ARGB colors
     * @param _iColor1 the first color
     * @param _iColor2 the second color
     * @param _iQuantity quantity (from 0 to 255)
     * @return blended color between the first color and the second color
     */
    public int getBlendedColor(int _iColor1, int _iColor2, int _iQuantity) {
        int iFinalColor = 0;
        int iA1 = (_iColor1 >> 24) & 0xFF;
        int iA2 = (_iColor2 >> 24) & 0xFF;
        int iR1 = (_iColor1 >> 16) & 0xFF;
        int iR2 = (_iColor2 >> 16) & 0xFF;
        int iG1 = (_iColor1 >> 8) & 0xFF;
        int iG2 = (_iColor2 >> 8) & 0xFF;
        int iB1 = _iColor1 & 0xFF;
        int iB2 = _iColor2 & 0xFF;

        if (iA1 > 0 && iA2 > 0) {
            iFinalColor |= (iA1 + (((iA2 - iA1) * _iQuantity) >> 8)) << 24;
        }

        iFinalColor |= (iR1 + (((iR2 - iR1) * _iQuantity) >> 8)) << 16;
        iFinalColor |= (iG1 + (((iG2 - iG1) * _iQuantity) >> 8)) << 8;
        iFinalColor |= iB1 + (((iB2 - iB1) * _iQuantity) >> 8);

        return iFinalColor;
    }

    /**
     * Gets a blended color between the two ARGB colors
     * @param _iColor1 the first color
     * @param _iColor2 the second color
     * @param _iQuantity quantity (from 0 to 255)
     */
    public void setBlendedColor(int _iColor1, int _iColor2, int _iQuantity) {
        setColor(getBlendedColor(_iColor1, _iColor2, _iQuantity));
    }

    /**
     * Set image scaling factor <P>Please note that this feature is very cpu compsuming
     * so try to use it only for small graphics and in a reduced quantity.
     * <p>
     * The scale value ranges from 0(0%) to 512(200%). A scale value of 256 wont change the image size. 
     * @param _iScaleX scale factor in x (default value 256)
     * @param _iScaleY scale factor in y (default value 256) 
     */
    public void setImageSize(int _iScaleX, int _iScaleY) {
        if (_iScaleX < 0)
            _iScaleX = 0;
        
        if (_iScaleY < 0)
            _iScaleY = 0;

        m_iSizeX = _iScaleX;
        m_iSizeY = _iScaleY;
    }

    /**
     * Get Image x scaling factor
     * @return get Image x scaling factor
     */
    public int getImageSizeX() {
        return m_iSizeX;
    }

    /**
     * Get Image y scaling factor
     * @return get Image y scaling factor
     */
    public int getImageSizeY() {
        return m_iSizeY;
    }
    
    /**
     * Draws a line between the coordinates (x1,y1) and (x2,y2) using the current color and stroke style.
     * @param _iX0 the x coordinate of the start of the line
     * @param _iY0 the y coordinate of the start of the line
     * @param _iX1 the x coordinate of the end of the line
     * @param _iY1 the y coordinate of the end of the line
     */
    public void drawLine(int _iX0, int _iY0, int _iX1, int _iY1) {
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawLine(_iX0, _iY0, _iX1, _iY1);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#else
        m_Graphics.drawLine(_iX0, _iY0, _iX1, _iY1);
        //#endif
    }

    /**
     * Draws the outline of the specified rectangle using the current color and stroke style. The resulting rectangle will cover an area (width + 1) pixels wide by (height + 1) pixels tall. If either width or height is less than zero, nothing is drawn.
     * @param _iX the x coordinate of the rectangle to be drawn
     * @param _iY the y coordinate of the rectangle to be drawn
     * @param _iW the width of the rectangle to be drawn
     * @param _iH the height of the rectangle to be drawn
     * @see fillRect(int, int, int, int)
     */
    public void drawRect(int _iX, int _iY, int _iW, int _iH) {
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawRect(_iX, _iY, _iW+1, _iH+1);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#else
        m_Graphics.drawRect(_iX, _iY, _iW, _iH);
        //#endif
    }

    /**
     * Fills the specified rectangle with the current color. If either width or height is zero or less, nothing is drawn.
     * @param _iX the x coordinate of the rectangle to be drawn
     * @param _iY the y coordinate of the rectangle to be drawn
     * @param _iW the width of the rectangle to be drawn
     * @param _iH the height of the rectangle to be drawn
     * @see drawRect(int, int, int, int)
     */
    public void fillRect(int _iX, int _iY, int _iW, int _iH) {
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.fillRect(_iX, _iY, _iW, _iH);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#else
        m_Graphics.fillRect(_iX, _iY, _iW, _iH);
        //#endif
    }

    /**
     * Draws the outline of the specified rounded corner rectangle using the current color and stroke style. 
     * The resulting rectangle will cover an area (width + 1) pixels wide by (height + 1) pixels tall. 
     * If either width or height is less than zero, nothing is drawn.
     * @param _iX the x coordinate of the rectangle to be drawn
     * @param _iY the y coordinate of the rectangle to be drawn
     * @param _iW the width of the rectangle to be drawn
     * @param _iH the height of the rectangle to be drawn
     * @see fillRoundRect(int, int, int, int, int, int)
     */
    public void drawRoundRect(int _iX, int _iY, int _iW, int _iH) {
        //#if RenderMode == "MIDlet"
        m_Graphics.drawRoundRect(_iX, _iY, _iW, _iH, _iW, _iH);
        //#if API == "Nokia"
//#         m_Graphics.drawString("x", -30, -30, 0);
        //#endif
        
        //#elif RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawRoundRect(_iX, _iY, _iW, _iH, _iW, _iH);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#elif RenderMode == "Doja"
//#         if (_iW > 0 && _iH > 0)
//#             m_Graphics.drawArc(_iX, _iY, _iW, _iH, 0, 360);
        //#endif
    }

    /**
     * Fills the specified rounded corner rectangle with the current color. If either width or height is zero or less, nothing is drawn.
     * @param _iX the x coordinate of the rectangle to be drawn
     * @param _iY the y coordinate of the rectangle to be drawn
     * @param _iW the width of the rectangle to be drawn
     * @param _iH the height of the rectangle to be drawn
     * @see drawRoundRect(int, int, int, int, int, int)
     */
    public void fillRoundRect(int _iX, int _iY, int _iW, int _iH) {
        //#if RenderMode == "MIDlet"
        m_Graphics.fillRoundRect(_iX, _iY, _iW, _iH, _iW, _iH);
        
        //#if API == "Nokia"
//#         m_Graphics.drawString("x", -50, -50, 0);
        //#endif
        //#elif RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.fillRoundRect(_iX, _iY, _iW, _iH, _iW, _iH);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#elif RenderMode == "Doja"
//#         if (_iW > 0 && _iH > 0)
//#             m_Graphics.fillArc(_iX, _iY, _iW, _iH, 0, 360);
        //#endif
    }

    /**
     * Draws the outline of the specified rounded corner rectangle using the current color and stroke style. 
     * The resulting rectangle will cover an area (width + 1) pixels wide by (height + 1) pixels tall. 
     * If either width or height is less than zero, nothing is drawn.
     * @param _iX the x coordinate of the rectangle to be drawn
     * @param _iY the y coordinate of the rectangle to be drawn
     * @param _iW the width of the rectangle to be drawn
     * @param _iH the height of the rectangle to be drawn
     * @param _iArcW the horizontal diameter of the arc at the four corners
     * @param _iArcH the vertical diameter of the arc at the four corners
     * @see fillRoundRect(int, int, int, int, int, int)
     */
    public void drawRoundRect(int _iX, int _iY, int _iW, int _iH, int _iArcW, int _iArcH) {
        //#if RenderMode == "MIDlet"
        m_Graphics.drawRoundRect(_iX, _iY, _iW, _iH, _iArcW, _iArcH);
        //#if API == "Nokia"
//#         m_Graphics.drawString("x", -50, -50, 0);
        //#endif
        
        //#elif RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawRoundRect(_iX, _iY, _iW, _iH, _iW, _iH);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
        //#elif RenderMode == "Doja"
//#         if (_iW > 0 && _iH > 0)
//#             m_Graphics.drawArc(_iX, _iY, _iW, _iH, 0, 360);
        //#endif
    }

    /**
     * Fills the specified rounded corner rectangle with the current color. If either width or height is zero or less, nothing is drawn.
     * @param _iX the x coordinate of the rectangle to be drawn
     * @param _iY the y coordinate of the rectangle to be drawn
     * @param _iW the width of the rectangle to be drawn
     * @param _iH the height of the rectangle to be drawn
     * @param _iArcW the horizontal diameter of the arc at the four corners
     * @param _iArcH the vertical diameter of the arc at the four corners
     * @see drawRoundRect(int, int, int, int, int, int)
     */
    public void fillRoundRect(int _iX, int _iY, int _iW, int _iH, int _iArcW, int _iArcH) {
        //#if RenderMode == "MIDlet"
        m_Graphics.fillRoundRect(_iX, _iY, _iW, _iH, _iArcW, _iArcH);
        //#if API == "Nokia"
//#         m_Graphics.drawString("x", -50, -50, 0);
        //#endif
        
        //#elif RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.fillRoundRect(_iX, _iY, _iW, _iH, _iArcW, _iArcH);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#elif RenderMode == "Doja"
//#         if (_iW > 0 && _iH > 0)
//#             m_Graphics.fillArc(_iX, _iY, _iW, _iH, 0, 360);
        //#endif
    }

    /**
     * Draws the outline of a circular or elliptical arc covering the specified rectangle, using the current color and stroke style.<P>
     * The resulting arc begins at startAngle and extends for arcAngle degrees, using the current color. 
     * Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive value indicates 
     * a counter-clockwise rotation while a negative value indicates a clockwise rotation.<P>
     * The center of the arc is the center of the rectangle whose origin is (x, y) and whose size is specified by the width and height arguments.<P>
     * The resulting arc covers an area width + 1 pixels wide by height + 1 pixels tall. If either width or height is less than zero, nothing is drawn.<P>
     * The angles are specified relative to the non-square extents of the bounding rectangle such that 45 degrees always falls on the line 
     * from the center of the ellipse to the upper right corner of the bounding rectangle. As a result, if the bounding rectangle is noticeably longer 
     * in one axis than the other, the angles to the start and end of the arc segment will be skewed farther along the longer axis of the bounds.
     * @param _iX the x coordinate of the upper-left corner of the arc to be drawn
     * @param _iY the y coordinate of the upper-left corner of the arc to be drawn
     * @param _iW the width of the arc to be drawn
     * @param _iH the height of the arc to be drawn
     * @param _iStartAngle the beginning angle
     * @param _iArcAngle the angular extent of the arc, relative to the start angle
     * @see fillArc(int, int, int, int, int, int)
     */
    public void drawArc(int _iX, int _iY, int _iW, int _iH, int _iStartAngle, int _iArcAngle) {
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawArc(_iX, _iY, _iW, _iH, _iStartAngle, _iArcAngle);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#else
        m_Graphics.drawArc(_iX, _iY, _iW, _iH, _iStartAngle, _iArcAngle);
        //#endif
    }

    /**
     * Draws the outline of a circular or elliptical arc covering the specified rectangle, using the current color and stroke style.<P>
     * The resulting arc begins at startAngle and extends for arcAngle degrees, using the current color. 
     * Angles are interpreted such that 0 degrees is at the 3 o'clock position. A positive value indicates 
     * a counter-clockwise rotation while a negative value indicates a clockwise rotation.<P>
     * The center of the arc is the center of the rectangle whose origin is (x, y) and whose size is specified by the width and height arguments.<P>
     * The resulting arc covers an area width + 1 pixels wide by height + 1 pixels tall. If either width or height is less than zero, nothing is drawn.<P>
     * The angles are specified relative to the non-square extents of the bounding rectangle such that 45 degrees always falls on the line 
     * from the center of the ellipse to the upper right corner of the bounding rectangle. As a result, if the bounding rectangle is noticeably longer 
     * in one axis than the other, the angles to the start and end of the arc segment will be skewed farther along the longer axis of the bounds.
     * @param _iX the x coordinate of the upper-left corner of the arc to be drawn
     * @param _iY the y coordinate of the upper-left corner of the arc to be drawn
     * @param _iW the width of the arc to be drawn
     * @param _iH the height of the arc to be drawn
     * @param _iStartAngle the beginning angle
     * @param _iArcAngle the angular extent of the arc, relative to the start angle
     * @see drawArc(int, int, int, int, int, int)
     */
    public void fillArc(int _iX, int _iY, int _iW, int _iH, int _iStartAngle, int _iArcAngle) {
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.fillArc(_iX, _iY, _iW, _iH, _iStartAngle, _iArcAngle);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#else
        m_Graphics.fillArc(_iX, _iY, _iW, _iH, _iStartAngle, _iArcAngle);
        //#endif
    }

    /**
     * Draws the specified triangle will the current color. The lines connecting each pair of points are included in the filled triangle.
     * @param _x0 the x coordinate of the first vertex of the triangle
     * @param _y0 the y coordinate of the first vertex of the triangle
     * @param _x1 the x coordinate of the second vertex of the triangle
     * @param _y1 the y coordinate of the second vertex of the triangle
     * @param _x2 the x coordinate of the third vertex of the triangle
     * @param _y2 the y coordinate of the third vertex of the triangle
     * @since MIDP 2.0
     */
    public void drawTriangle(int _x0, int _y0, int _x1, int _y1, int _x2, int _y2) {
        m_Graphics.drawLine(_x0, _y0, _x1, _y1);
        m_Graphics.drawLine(_x1, _y1, _x2, _y2);
        m_Graphics.drawLine(_x2, _y2, _x0, _y0);
    }
    
    /**
     * Fills the specified triangle will the current color. The lines connecting each pair of points are included in the filled triangle.
     * @param _x0 the x coordinate of the first vertex of the triangle
     * @param _y0 the y coordinate of the first vertex of the triangle
     * @param _x1 the x coordinate of the second vertex of the triangle
     * @param _y1 the y coordinate of the second vertex of the triangle
     * @param _x2 the x coordinate of the third vertex of the triangle
     * @param _y2 the y coordinate of the third vertex of the triangle
     * @since MIDP 2.0
     */
    public void fillTriangle(int _x0, int _y0, int _x1, int _y1, int _x2, int _y2) {

        //it may not be neccesary in many devices
        /*
        if (((_x0 < 1) && (_x1 < 1) && (_x2 < 1)) || 
            ((_x0 > m_iWidth - 1) && (_x1 > m_iWidth - 1) && (_x2 > m_iWidth - 1)) || 
            ((_y0 < 1) && (_y1 < 1) && (_y2 < 1)) || 
            ((_y0 > m_iHeight - 1) && (_y1 > m_iHeight - 1) && (_y2 > m_iHeight - 1))) {
            return;
        }
        */

        // hacks
        //#if a177
//#         if (_y0 >= _y1){
//#             m_Graphics.fillTriangle(_x0, _y0, _x1, _y1, _x2, _y2 );
//#             m_Graphics.fillTriangle(_x0, _y0, _x2, _y2, _x1, _y1 );
//#             m_Graphics.fillTriangle(_x1, _y1, _x2, _y2, _x0, _y0 );
//#             m_Graphics.fillTriangle(_x2, _y2, _x0, _y0, _x1, _y1);
//#             return;
//#         }else if (_y1 >= _y2){
//#             m_Graphics.fillTriangle(_x1, _y1, _x0, _y0, _x2, _y2);
//#             return;
//#         }else if (_y2 >= _y0){
//#             m_Graphics.fillTriangle(_x1, _y1, _x0, _y0, _x2, _y2);
//#             return;
//#         }
        //#elif vf810
//#             if (true) {
//#                 fillMIDP1Triangle (_x0, _y0, _x1, _y1, _x2, _y2);
//#                 return;
//#             }
//# //            if (_x1>Define.SIZEX)
//# //                _x1=(Define.SIZEX);
//# //            if (_y1>Define.SIZEY)
//# //                _y1=(Define.SIZEY);
//# //            if (_x2>Define.SIZEX)
//# //                _x2=(Define.SIZEX);
//# //            if (_y2>Define.SIZEY)
//# //                _y2=(Define.SIZEY);
        //#elif kg800
//#          /*
//#         if (_y1 < getClipY())
//#             clipTriangleUpperSize(_x0, _y0, _x1, _y1);
//#         if (_y2 < getClipY())
//#             clipTriangleUpperSize(_x0, _y0, _x2, _y2);
//#         */
//#         
//#         if (_y1 < getClipY() || _y2 < getClipY()) {
//#             fillMIDP1Triangle (_x0, _y0, _x1, _y1, _x2, _y2);
//#             return;
//#         }
        //#elif d500
//#         int D500_TRIANGLE_DRAW_LIMIT_Y = 220 - (220/8);
//#         if (_y0 > D500_TRIANGLE_DRAW_LIMIT_Y || 
//#             _y1 > D500_TRIANGLE_DRAW_LIMIT_Y ||
//#             _y2 > D500_TRIANGLE_DRAW_LIMIT_Y) {
//#             fillMIDP1Triangle(_x0, _y0, _x1, _y1, _x2, _y2);
//#             return;
//#         }
        //#endif
        
        //#if DirectGraphics && (API == "Nokia" && MIDP == "1.0")
//#         m_DirectGraphics.fillTriangle (_x0-1, _y0, _x1-1, _y1, _x2-1, _y2, (m_iAlpha<<24) |m_Graphics.getColor()); // ????
//# 
        //#elif DirectGraphics && (API == "Nokia" && MIDP == "2.0")
//#         // Some Nokias has buggy fillTriangles, use fillPolygon instead
//#         m_DirectGraphics.fillPolygon(new int[] {_x0, _x1, _x2}, 0,
//#               new int[] {_y0, _y1, _y2}, 0, 3, (m_iAlpha<<24) |m_Graphics.getColor()); // ????
//#               
        //#elif RenderMode == "MIDlet"
        m_Graphics.fillTriangle(_x0, _y0, _x1, _y1, _x2, _y2);

        //#elif RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawFilledPath (new int [] {_x0, _x1, _x2}, new int [] {_y0, _y1, _y2}, null, null);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#elif RenderMode == "Doja"
//#         m_Graphics.fillPolygon(new int[] {_x0, _x1, _x2}, new int[] {_y0, _y1, _y2}, 3);
//# 
        //#endif

    }

    /*
    private void clipTriangleUpperSize (int _x0, int _y0, int _x1, int _y1) {
        if (_x1 < 0) {
            _y1 = _y1 + (((_y0 - _y1) * _x0) / (_x0 - _x1));
            _x1 = 0;
        }
    }
    */
    
    private void fillMIDP1Triangle (int _x0, int _y0, int _x1, int _y1, int _x2, int _y2) {
        int temp;
        if (_y0 > _y1)
        {
            temp = _x0;
            _x0 = _x1;
            _x1 = temp;
            temp = _y0;
            _y0 = _y1;
            _y1 = temp;       
        }
        if (_y1 > _y2)
        {
            temp = _x1;
            _x1 = _x2;
            _x2 = temp;
            temp = _y1;
            _y1 = _y2;
            _y2 = temp; 
        }
        if (_y0 > _y1)
        {
            temp = _x0;
            _x0 = _x1;
            _x1 = temp;
            temp = _y0;
            _y0 = _y1;
            _y1 = temp; 
        }       

        // Calc the deltas for each edge.
        int ab_num;
        int ab_den;
        if (_y1 - _y0 > 0)
        {
            ab_num = (_x1 - _x0);
            ab_den = (_y1 - _y0);
        }
        else
        {
            ab_num = (_x1 - _x0);
            ab_den = 1;
        }

        int ac_num;
        int ac_den;
        if (_y2 - _y0 > 0)
        {
            ac_num = (_x2 - _x0);
            ac_den = (_y2 - _y0);
        }
        else
        {
            ac_num = 0;
            ac_den = 1;
        }

        int bc_num;
        int bc_den;
        if (_y2 - _y1 > 0)
        {
            bc_num = (_x2 - _x1);
            bc_den = (_y2 - _y1);
        }
        else
        {
            bc_num = 0;
            bc_den = 1;
        }
 
        // The start and end of each line.
        int sx;
        int ex;

        // The heights of the two components of the triangle.
        int h1 = _y1 - _y0;
        int h2 = _y2 - _y1;
 
        // If a is to the left of b...
        if (_x0 < _x1)
        {
            // For each row of the top component...
            for (int y = 0; y < h1; y++)
            {
                sx = _x0 + ac_num * y / ac_den;
                ex = _x0 + ab_num * y / ab_den;

                if (ex - sx > 0)
                    m_Graphics.fillRect(sx, _y0 + y, ex - sx, 1);
            }
            // For each row of the bottom component...
            for (int y = 0; y < h2; y++)
            {
                int y2 = h1 + y;
                sx = _x0 + ac_num * y2 / ac_den;
                ex = _x1 + bc_num * y / bc_den;
                
                if (ex - sx > 0)
                    m_Graphics.fillRect(sx, _y1 + y, ex - sx, 1);
            }
        }
        else
        {
            // For each row of the bottom component...
            for (int y = 0; y < h1; y++)
            {
                sx = _x0 + ab_num * y / ab_den;
                ex = _x0 + ac_num * y / ac_den;
                
                if (ex - sx > 0)
                    m_Graphics.fillRect(sx, _y0 + y, ex - sx, 1);
            }
            // For each row of the bottom component...
            for (int y = 0; y < h2; y++)
            {
                int y2 = h1 + y;
                sx = _x1 + bc_num * y / bc_den;
                ex = _x0 + ac_num * y2 / ac_den;
                
                if (ex - sx > 0)
                    m_Graphics.fillRect(sx, _y1 + y, ex - sx, 1);
            }
        }
    }
    
    /**
     * Sets the font for all subsequent text rendering operations. If font is null, it is equivalent to setFont(Font.getDefaultFont()).
     * @param _Font the specified font
     * @see getFont()
     * @see drawString(java.lang.String, int, int, int) 
     * @see drawChars(char[], int, int, int, int, int)
     */
    public void setFont(Font _Font) {
        m_vFont = _Font;
        m_Graphics.setFont(_Font.m_vFont);
    }
    
    /**
     * Gets the current font.
     * @return current font
     * @see setFont(javax.microedition.lcdui.Font)
     */
    public Font getFont() {
        return m_vFont;
    }

    /**
     * Draws the specified String using the current font and color.
     * @param _zText the String to be drawn
     * @param _iPosX the x coordinate of the anchor point
     * @param _iPosY the y coordinate of the anchor point
     * @throws NullPointerException - if str is null
     * @see drawString(String, int, int, int)
     * @see drawChars(char[], int, int, int, int, int)
     */
    public void drawString(String _zText, int _iPosX, int _iPosY) {
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawText(_zText, _iPosX, _iPosY, 0);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#elif RenderMode == "Doja"
//#         m_Graphics.drawString(_zText, _iPosX, _iPosY);
        //#else
        m_Graphics.drawString(_zText, _iPosX, _iPosY, 0);
        //#endif
    }

    /**
     * Draws the specified String using the current font and color. The x,y position is the position of the anchor point. See anchor points.
     * @param _zText the String to be drawn
     * @param _iPosX the x coordinate of the anchor point
     * @param _iPosY the y coordinate of the anchor point
     * @param _iAnchor the anchor point for positioning the text
     * @throws NullPointerException - if str is null
     * @throws IllegalArgumentException - if anchor is not a legal value
     * @see drawString(String, int, int)
     * @see drawChars(char[], int, int, int, int, int)
     */
    public void drawString(String _zText, int _iPosX, int _iPosY, int _iAnchor) {
        if ((_iAnchor & BOTTOM) != 0)
            _iPosY -= m_vFont.getHeight();
        if ((_iAnchor & BASELINE) != 0)
            _iPosY -= m_vFont.getBaselinePosition();
        if ((_iAnchor & RIGHT) != 0)
            _iPosX -= m_vFont.stringWidth(_zText);        
        if ((_iAnchor & VCENTER) != 0)
            _iPosY -= m_vFont.getBaselinePosition() >> 1;
        if ((_iAnchor & HCENTER) != 0)
            _iPosX -= m_vFont.stringWidth(_zText) >> 1;
        
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawText (_zText, _iPosX, _iPosY, 0);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#elif RenderMode == "Doja"
//#         m_Graphics.drawString(_zText, _iPosX, _iPosY);
        //#else
        m_Graphics.drawString(_zText, _iPosX, _iPosY, 0);
        //#endif
    }

    /**
     * Draws the specified String using the current font and color. The x,y position is the position of the anchor point. See anchor points.<P>
     * The offset and len parameters must specify a valid range of characters within the string str. The offset parameter must be within the range [0..(str.length())], inclusive. The len parameter must be a non-negative integer such that (offset + len) <= str.length().
     * @param _zStr the String to be drawn
     * @param _iOffset zero-based index of first character in the substring
     * @param _iLen length of the substring
     * @param _iX the x coordinate of the anchor point
     * @param _iY the y coordinate of the anchor point
     * @param _iAnchor the anchor point for positioning the text
     * @throws StringIndexOutOfBoundsException - if offset and length do not specify a valid range within the String str
     * @throws IllegalArgumentException - if anchor is not a legal value
     * @throws NullPointerException - if str is null
     * @see drawString(String, int, int)
     * @see drawString(String, int, int, int)
     */
    public void drawSubstring(String _zStr, int _iOffset, int _iLen, int _iX, int _iY, int _iAnchor) {
        drawString(_zStr.substring(_iOffset, _iLen), _iX, _iY, _iAnchor);
    }

    /**
     * Draws the specified character using the current font and color.
     * @param _iChar the character to be drawn
     * @param _iX the x coordinate of the anchor point
     * @param _iY the y coordinate of the anchor point
     * @param _iAnchor the anchor point for positioning the text; see anchor points
     */
    public void drawChar(char _iChar, int _iX, int _iY, int _iAnchor) {
        drawString(String.valueOf(_iChar), _iX, _iY, _iAnchor);
    }

    /**
     * Draws the specified characters using the current font and color.<P>        
     * The offset and length parameters must specify a valid range of characters within the character array data. 
     * The offset parameter must be within the range [0..(data.length)], inclusive. 
     * The length parameter must be a non-negative integer such that (offset + length) <= data.length.
     * @param _Data the array of characters to be drawn
     * @param _iOffset the start offset in the data
     * @param _iLength the number of characters to be drawn
     * @param _iX the x coordinate of the anchor point
     * @param _iY the y coordinate of the anchor point
     * @param _iAnchor the anchor point for positioning the text; see anchor points
     */
    public void drawChars(char[] _Data, int _iOffset, int _iLength, int _iX, int _iY, int _iAnchor) {
        drawString(String.valueOf(_Data), _iX, _iY, _iAnchor);
    }

    /**
     * Renders a series of device-independent RGB+transparency values in a specified region. 
     * The values are stored in rgbData in a format with 24 bits of RGB and an eight-bit 
     * alpha value (0xAARRGGBB), with the first value stored at the specified offset. 
     * The scanlength specifies the relative offset within the array between the corresponding 
     * pixels of consecutive rows. Any value for scanlength is acceptable (even negative values) 
     * provided that all resulting references are within the bounds of the rgbData array. 
     * The ARGB data is rasterized horizontally from left to right within each row. 
     * The ARGB values are rendered in the region specified by x, y, width and height, 
     * and the operation is subject to the current clip region and translation for this Graphics object.
     * <P>
     * Consider P(a,b) to be the value of the pixel located at column a and row b of the Image, 
     * where rows and columns are numbered downward from the top starting at zero, and columns 
     * are numbered rightward from the left starting at zero. This operation can then be defined as:
     * <P>
     * <ul>
     * <li>P(a, b) = rgbData[offset + (a - x) + (b - y) * scanlength]
     * </ul>
     * <P>
     * for
     * <P>
     * <ul>
     * <li>x <= a < x + width
     * <li>y <= b < y + height
     * </ul>
     * <P>
     * This capability is provided in the Graphics class so that it can be used to render both to 
     * the screen and to offscreen Image objects. The ability to retrieve ARGB values is provided 
     * by the Image.getRGB(int[], int, int, int, int, int, int) method.
     * <P>
     * If processAlpha is true, the high-order byte of the ARGB format specifies opacity; that is, 
     * 0x00RRGGBB specifies a fully transparent pixel and 0xFFRRGGBB specifies a fully opaque pixel. 
     * Intermediate alpha values specify semitransparency. If the implementation does not support alpha blending 
     * for image rendering operations, it must remove any semitransparency from the source data prior to 
     * performing any rendering. (See Alpha Processing for further discussion.) If processAlpha is false, 
     * the alpha values are ignored and all pixels must be treated as completely opaque.
     * <P>
     * The mapping from ARGB values to the device-dependent pixels is platform-specific and may require significant computation.
     * 
     * @param _iRgbData an array of ARGB values in the format 0xAARRGGBB
     * @param _iOffset the array index of the first ARGB value
     * @param _iScanlength the relative array offset between the corresponding pixels in consecutive rows in the rgbData array
     * @param _iX the horizontal location of the region to be rendered
     * @param _iY the vertical location of the region to be rendered
     * @param _iW the width of the region to be rendered
     * @param _iH the height of the region to be rendered
     * @param _bAlpha true if rgbData has an alpha channel, false if all pixels are fully opaque
     * @throws ArrayIndexOutOfBoundsException - if the requested operation will attempt to access an element of rgbData whose index is either negative or beyond its length
     * @throws NullPointerException - if rgbData is null
     * @since MIDP 2.0
     */
    public void drawRGB(int[] _iRgbData, int _iOffset, int _iScanlength,
        int _iX, int _iY, int _iW, int _iH, boolean _bAlpha) {
        
        //#if RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#         m_Graphics.drawARGB (_iRgbData, _iOffset, _iScanlength, _iX, _iY, _iW, _iH);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
//#         
        //#elif RenderMode == "Doja"
//#         //m_Graphics.drawRGB (_iRgbData, _iOffset, _iScanlength, _iX, _iY, _iW, _iH);
        //#elif UseDirectGraphicsGetPixels
//#         m_DirectGraphics.drawPixels(_iRgbData, _bAlpha, _iOffset, _iScanlength, _iX, _iY, _iW, _iH, 0, DirectGraphics.TYPE_USHORT_4444_ARGB);
        //#else
        m_Graphics.drawRGB(_iRgbData, _iOffset, _iScanlength, _iX, _iY, _iW, _iH, _bAlpha);
        //#endif
    }

    //#define CanvasClipping    
    
    /**
     * Draws the specified image by using the anchor point. 
     * The image can be drawn in different positions relative to the anchor point 
     * by passing the appropriate position constants. See anchor points.
     * <P>
     * If the source image contains transparent pixels, the corresponding pixels in the 
     * destination image must be left untouched. If the source image contains partially 
     * transparent pixels, a compositing operation must be performed with the destination 
     * pixels, leaving all pixels of the destination image fully opaque.
     * <P>
     * If img is the same as the destination of this Graphics object, the result is undefined. 
     * For copying areas within an Image, copyArea should be used instead.
     * @param _vImage the specified image to be drawn
     * @param _iX the x coordinate of the anchor point
     * @param _iY the y coordinate of the anchor point
     * @param _iAnchor the anchor point for positioning the image
     * @throws IllegalArgumentException - if anchor is not a legal value
     * @throws NullPointerException - if img is null
     * @see javak.microedition.lcdui.Image
     */
    public void drawImage(Image _vImage, int _iX, int _iY, int _iAnchor) {
        
        if (m_iAlpha < 1)
            return;

        // anchor
        if ((_iAnchor & BOTTOM) != 0)
            _iY -= _vImage.getHeight();
        if ((_iAnchor & RIGHT) != 0)
            _iX -= _vImage.getWidth();
        if ((_iAnchor & VCENTER) != 0)
            _iY -= _vImage.getHeight() >> 1;
        if ((_iAnchor & HCENTER) != 0)
            _iX -= _vImage.getWidth() >> 1;

        //#if UseRGB == "false"
//#       
        //#if RenderMode == "MIDlet"
//#         m_Graphics.drawImage (_vImage.m_vImage, _iX, _iY, 0);
        //#elif RenderMode == "UIApp"
//#         if (m_bClip)
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//#         m_Graphics.drawBitmap(new XYRect(_iX, _iY, _vImage.getWidth(), _vImage.getHeight()), _vImage.m_vImage, 0, 0);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
        //#elif RenderMode == "Doja"
//#         m_Graphics.drawImage(_vImage.m_vImage, _iX, _iY);
        //#endif
//# 
        //#else
        if (m_iAlpha == 255 && m_iSizeX == 256 && m_iSizeY == 256) {

            //#if RenderMode == "MIDlet"
            m_Graphics.drawImage(_vImage.m_vImage, _iX, _iY, 0);
            //#elif RenderMode == "UIApp"
//#             if (m_bClip)
//#                 m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//#             
//#             m_Graphics.drawBitmap(new XYRect(_iX, _iY, _vImage.getWidth(), _vImage.getHeight()), _vImage.m_vImage, 0, 0);
//#             if (m_bClip) 
//#                 m_Graphics.popContext();
//#             
            //#elif RenderMode == "Doja"
//#             m_Graphics.drawImage(_vImage.m_vImage, _iX, _iY);
            //#endif

        } else {

            int iPosXImg = _iX;
            int iPosYImg = _iY;
            int srcW, srcH;

            int iDrawRGB_OffsetX=0, iDrawRGB_OffsetY=0;
            //try {

            srcW = _vImage.getWidth();
            srcH = _vImage.getHeight();

            int destW = (srcW * m_iSizeX) >> 8;
            int destH = (srcH * m_iSizeY) >> 8;
            if (destW == 0 || destH == 0) {
                return;
            }

            //#if RenderMode == "MIDlet"
            // translation / transform
            if ((_iAnchor & BOTTOM) != 0) {
                iPosYImg += srcH - destH;
            }
            if ((_iAnchor & RIGHT) != 0) {
                iPosXImg += srcW - destW;
            }
            if ((_iAnchor & VCENTER) != 0) {
                iPosYImg += (srcH - destH) >> 1;
            }
            if ((_iAnchor & HCENTER) != 0) {
                iPosXImg += (srcW - destW) >> 1;
            }

            try {
                // create pixel arrays
                int[] srcPixels = new int[srcW];
                int[] destPixels = new int[destW]; // array to hold destination pixels

                // precalculate src/dest ratios
                int ratioW = (srcW << FP_BITS) / destW;
                int ratioH = (srcH << FP_BITS) / destH;


                // fix the bug when we try to paint out of the canvas
                //#if CanvasClipping
                if (iPosXImg < m_Graphics.getClipX()) {
                    iDrawRGB_OffsetX = m_Graphics.getClipX() - iPosXImg;
                }
                if (iPosXImg+destW > m_Graphics.getClipX()+m_Graphics.getClipWidth()) {
                    destW -= (iPosXImg+destW) - (m_Graphics.getClipX()+m_Graphics.getClipWidth());
                }
                if (iPosYImg < m_Graphics.getClipY()) {
                    iDrawRGB_OffsetY = m_Graphics.getClipY()-iPosYImg;
                }
                if (iPosYImg+destW > m_Graphics.getClipY()+m_Graphics.getClipHeight()) {
                    destH -= (iPosYImg+destH) - (m_Graphics.getClipY()+m_Graphics.getClipHeight());
                }
                //#endif

                int srcX, srcY;
                int iTransparentColor = _vImage.getTransparentColor();
                
                // no transparency
                //#if UseAlpha == "true"
                if (m_iAlpha > 254)
                //#endif
                {
                    for (int destY = iDrawRGB_OffsetY; destY < destH; ++destY) {
                        srcY = (destY * ratioH) >> FP_BITS; // calculate beginning of sample

                        _vImage.getRGB(srcPixels, 0, srcW, 0, srcY, srcW, 1);

                        for (int destX = iDrawRGB_OffsetX; destX < destW; ++destX) {
                            srcX = (destX * ratioW) >> FP_BITS; // calculate beginning of sample

                            //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                             if (srcPixels[srcX] == iTransparentColor)
//#                                 destPixels[destX] = 0x00000000;
//#                             else
                            //#endif

                            destPixels[destX] = srcPixels[srcX];
                        }

                        //#if RenderMode == "MIDlet"
                        m_Graphics.drawRGB(destPixels, 0, destW, iPosXImg, iPosYImg + destY, destW, 1, true);
                        //#elif RenderMode == "UIApp"
//#                         m_Graphics.drawRGB (destPixels, 0, destW, iPosXImg, iPosYImg + destY, destW, 1);
                        //#endif

                    }
                } 
                // transparency
                //#if UseAlpha == "true"
                else {

                    int iAlpha = (m_iAlpha << 24);
                    for (int destY = iDrawRGB_OffsetY; destY < destH; ++destY) {
                        srcY = (destY * ratioH) >> FP_BITS; // calculate beginning of sample

                        _vImage.getRGB(srcPixels, 0, srcW, 0, srcY, srcW, 1);

                        for (int destX = iDrawRGB_OffsetX; destX < destW; ++destX) {
                            srcX = (destX * ratioW) >> FP_BITS; // calculate beginning of sample

                            //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                             if (srcPixels[srcX] == iTransparentColor)
//#                                 destPixels[destX] = 0x00000000;
//#                             else
//#                                 destPixels[destX] = srcPixels[srcX] + iAlpha;
//# 
                            //#else
                            if (((srcPixels[srcX] >> 24) & 0xFF) == 0) {
                                destPixels[destX] = srcPixels[srcX];
                            } else {
                                destPixels[destX] = srcPixels[srcX] + iAlpha;
                            }
                            //#endif

                        }

                        //#if RenderMode == "MIDlet"
                        m_Graphics.drawRGB(destPixels, 0, destW, iPosXImg, iPosYImg + destY, destW, 1, true);
                        //#elif RenderMode == "UIApp"
//#                         m_Graphics.drawRGB (destPixels, 0, destW, iPosXImg, iPosYImg + destY, destW, 1);
                        //#endif
                    }
                }
                //#endif

                
            } catch (Exception e) {
                //#if Debug
//#                 //MiscUtils.DebugMsg ( "Graphics.drawImage() - Drawing error. Excpt:"+e.getMessage ()+"("+e.toString ()+")" );
                //#endif
            }
            
                
            //#elif RenderMode == "UIApp"
//#             // translation / transform
//#             if ((_iAnchor & BOTTOM) != 0) {
//#                 iPosYImg += srcH - destH;
//#             }
//#             if ((_iAnchor & RIGHT) != 0) {
//#                 iPosXImg += srcW - destW;
//#             }
//#             if ((_iAnchor & VCENTER) != 0) {
//#                 iPosYImg += (srcH - destH) >> 1;
//#             }
//#             if ((_iAnchor & HCENTER) != 0) {
//#                 iPosXImg += (srcW - destW) >> 1;
//#             }
//# 
//#             if (m_bClip)
//#                 m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//# 
//#             
//#             //TODO
//#             //_vSrc.m_vImageManip.transformByAngle(_iAngle, false, false);
//#             _vImage.m_vImageManip.setScaleX((Fixed32.ONE * m_iSizeX) >> 8);
//#             _vImage.m_vImageManip.setScaleY((Fixed32.ONE * m_iSizeY) >> 8);
//#             
//#             m_Graphics.translate(iPosXImg, iPosYImg);
//#             _vImage.m_vImageManip.applyTransformation();
//#             _vImage.m_vImageManip.paintBitmap(m_Graphics);
//#             m_Graphics.translate(-iPosXImg, -iPosYImg);
//#             
//#             _vImage.m_vImageManip.resetTransform();
//#             if (m_bClip) 
//#                 m_Graphics.popContext();
//#             
            //#elif RenderMode == "Doja"
//# 
//#             if ((_iAnchor & BOTTOM) != 0)
//#                 _iY += destH;
//#             if ((_iAnchor & RIGHT) != 0)
//#                 _iX += destW;
//#             if ((_iAnchor & VCENTER) != 0)
//#                 _iY += destH >> 1;
//#             if ((_iAnchor & HCENTER) != 0)
//#                 _iX += destW >> 1;
//#             
//#             _vImage.m_vImage.setAlpha(m_iAlpha);
//# 
//#             m_Graphics.drawScaledImage (_vImage.m_vImage,
//#                 _iX, _iY, destW, destH,
//#                 0, 0, srcW, srcW);
//# 
//#             _vImage.m_vImage.setAlpha(255);
            //#endif
            
        }
        //#endif
    }

    /**
     * Copies a region of the specified source image to a location within the destination, 
     * possibly transforming (rotating and reflecting) the image data using the chosen transform function.
     * <P>
     * The destination, if it is an image, must not be the same image as the source image. 
     * If it is, an exception is thrown. This restriction is present in order to avoid 
     * ill-defined behaviors that might occur if overlapped, transformed copies were permitted.
     * <p>
     * The transform function used must be one of the following, as defined in the Sprite class:
     * <ul>
     * <li>Sprite.TRANS_NONE - causes the specified image region to be copied unchanged
     * <li>Sprite.TRANS_ROT90 - causes the specified image region to be rotated clockwise by 90 degrees.
     * <li>Sprite.TRANS_ROT180 - causes the specified image region to be rotated clockwise by 180 degrees.
     * <li>Sprite.TRANS_ROT270 - causes the specified image region to be rotated clockwise by 270 degrees.
     * <li>Sprite.TRANS_MIRROR - causes the specified image region to be reflected about its vertical center.
     * <li>Sprite.TRANS_MIRROR_ROT90 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 90 degrees.
     * <li>Sprite.TRANS_MIRROR_ROT180 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 180 degrees.
     * <li>Sprite.TRANS_MIRROR_ROT270 - causes the specified image region to be reflected about its vertical center and then rotated clockwise by 270 degrees.
     * </ul>
     * If the source region contains transparent pixels, the corresponding pixels in the destination region must be left untouched. 
     * If the source region contains partially transparent pixels, a compositing operation must be performed with the destination pixels, 
     * leaving all pixels of the destination region fully opaque.
     * <p>
     * The (x_src, y_src) coordinates are relative to the upper left corner of the source image. 
     * The x_src, y_src, width, and height parameters specify a rectangular region of the source image. 
     * It is illegal for this region to extend beyond the bounds of the source image. This requires that:
     * <ul>
     * <li>x_src >= 0
     * <li>y_src >= 0
     * <li>x_src + width <= source width
     * <li>y_src + height <= source height    
     * </ul>
     * The (x_dest, y_dest) coordinates are relative to the coordinate system of this Graphics object. 
     * It is legal for the destination area to extend beyond the bounds of the Graphics object. 
     * Pixels outside of the bounds of the Graphics object will not be drawn.
     * <p>
     * The transform is applied to the image data from the region of the source image, 
     * and the result is rendered with its anchor point positioned at location (x_dest, y_dest) in the destination.
     * @param _vSrc the source image to copy from
     * @param _iSrcX the x coordinate of the upper left corner of the region within the source image to copy
     * @param _iSrcY the y coordinate of the upper left corner of the region within the source image to copy
     * @param _iWidth the width of the region to copy
     * @param _iHeight the height of the region to copy
     * @param _iTransform the desired transformation for the selected region being copied
     * @param _iDestX the x coordinate of the anchor point in the destination drawing area
     * @param _iDestY the y coordinate of the anchor point in the destination drawing area
     * @param _iAnchor the anchor point for positioning the region within the destination image
     * @throws IllegalArgumentException - if src is the same image as the destination of this Graphics object
     * @throws NullPointerException - if src is null
     * @throws IllegalArgumentException - if transform is invalid
     * @throws IllegalArgumentException - if anchor is invalid
     * @throws IllegalArgumentException - if the region to be copied exceeds the bounds of the source image
     * @since MIDP 2.0
     */
    public void drawRegion(Image _vSrc, int _iSrcX, int _iSrcY, int _iWidth, int _iHeight,
            int _iTransform, int _iDestX, int _iDestY, int _iAnchor) {
        
        if (m_iAlpha < 1)
            return;

        //#if UseSpriteForFlip
//#         int iExtraX = 0, iExtraY = 0;
//#         if ((_iAnchor & BOTTOM) != 0)
//#             iExtraY = _iHeight;
//#         if ((_iAnchor & RIGHT) != 0)
//#             iExtraX = _iWidth;
//#         if ((_iAnchor & VCENTER) != 0)
//#             iExtraY = _iHeight>>1;
//#         if ((_iAnchor & HCENTER) != 0)
//#             iExtraX = _iWidth>>1;
//#        
//#         saveClip();
//#         if (_iTransform == javax.microedition.lcdui.game.Sprite.TRANS_NONE) {
//#             m_Graphics.clipRect(_iDestX - iExtraX, _iDestY - iExtraY, _iWidth, _iHeight);
//#             m_Graphics.drawImage(_vSrc.m_vImage, _iDestX - iExtraX - _iSrcX, _iDestY - iExtraY - _iSrcY, 0);
//#             
//#         } else if (_iTransform == javax.microedition.lcdui.game.Sprite.TRANS_ROT90 ||
//#                 _iTransform == javax.microedition.lcdui.game.Sprite.TRANS_ROT270 ||
//#                 _iTransform == javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT90 ||
//#                 _iTransform == javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT270) {
//#             int iPosXClip = _iDestX - iExtraY;
//#             int iPosYClip = _iDestY - iExtraX;
//#             int iPosXImg = _iDestX - (_vSrc.getHeight() - _iSrcY - _iHeight) - iExtraY;
//#             int iPosYImg = _iDestY - iExtraX - _iSrcX;//_iDestY - (_vSrc.getHeight() - _iSrcY - _iHeight) - (_iHeight - iExtraY);
//#             
//#             m_Graphics.clipRect(iPosXClip, iPosYClip, _iHeight, _iWidth);
//#             _vSrc.m_vSprite.setTransform (_iTransform); 
//#             _vSrc.m_vSprite.setPosition (iPosXImg, iPosYImg);
//#             _vSrc.m_vSprite.paint (m_Graphics);
//#             
//#         } else if (_iTransform == javax.microedition.lcdui.game.Sprite.TRANS_MIRROR ||
//#                 _iTransform == javax.microedition.lcdui.game.Sprite.TRANS_ROT180 ||
//#                 _iTransform == javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180) {
//#             int iPosXClip = _iDestX - iExtraX;
//#             int iPosYClip = _iDestY - iExtraY;
//#             int iPosXImg = _iDestX - (_vSrc.getWidth() - _iSrcX - _iWidth) - iExtraX;
//#             int iPosYImg = _iDestY - iExtraY - _iSrcY;//_iDestY - (_vSrc.getHeight() - _iSrcY - _iHeight) - (_iHeight - iExtraY);
//#             
//#             m_Graphics.clipRect(iPosXClip, iPosYClip, _iWidth, _iHeight);
//#             _vSrc.m_vSprite.setTransform (_iTransform); 
//#             _vSrc.m_vSprite.setPosition (iPosXImg, iPosYImg);
//#             _vSrc.m_vSprite.paint (m_Graphics);
//#         
//#         }
//#         restoreClip();
        //#elif UseRGB == "false"
//#       
        //#if RenderMode == "MIDlet"
//#         m_Graphics.drawRegion(_vSrc.m_vImage, _iSrcX, _iSrcY, _iWidth, _iHeight, _iTransform, _iDestX, _iDestY, _iAnchor);
        //#elif RenderMode == "UIApp"
//#         int iExtraX = 0, iExtraY = 0;
//#         if ((_iAnchor & BOTTOM) != 0)
//#             iExtraY = _iHeight;
//#         if ((_iAnchor & RIGHT) != 0)
//#             iExtraX = _iWidth;
//#         if ((_iAnchor & VCENTER) != 0)
//#             iExtraY = _iHeight>>1;
//#         if ((_iAnchor & HCENTER) != 0)
//#             iExtraX = _iWidth>>1;
//#         if (m_bClip) 
//#             m_Graphics.pushContext(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3], 0, 0);
//#         if (_iTransform == javax.microedition.lcdui.game.Sprite.TRANS_MIRROR)
//#             m_Graphics.invert(m_iClip[0], m_iClip[1], m_iClip[2], m_iClip[3]);
//#             
//#         m_Graphics.drawBitmap(_iDestX + iExtraX, _iDestY + iExtraY, _iWidth, _iHeight, _vSrc.m_vImage, _iSrcX, _iSrcY);
//#         if (m_bClip) 
//#             m_Graphics.popContext();
        //#elif RenderMode == "Doja"
//#         int iExtraX = 0, iExtraY = 0;
//#         if ((_iAnchor & BOTTOM) != 0)
//#             iExtraY = _iHeight;
//#         if ((_iAnchor & RIGHT) != 0)
//#             iExtraX = _iWidth;
//#         if ((_iAnchor & VCENTER) != 0)
//#             iExtraY = _iHeight>>1;
//#         if ((_iAnchor & HCENTER) != 0)
//#             iExtraX = _iWidth>>1;
//# 
//#         m_Graphics.setFlipMode(_iTransform);
//#         m_Graphics.drawImage(_vSrc.m_vImage, _iDestX + iExtraX, _iDestY + iExtraY, _iSrcX, _iSrcY, _iWidth, _iHeight);
//#         m_Graphics.setFlipMode(0);
        //#endif
//#        
        //#else
        if (m_iAlpha == 255 && m_iSizeX == 256 && m_iSizeY == 256) {

            //#if RenderMode == "MIDlet"
            m_Graphics.drawRegion(_vSrc.m_vImage, _iSrcX, _iSrcY, _iWidth, _iHeight, _iTransform, _iDestX, _iDestY, _iAnchor);
            //#elif RenderMode == "UIApp"
//# 
//#             if ((_iAnchor & BOTTOM) != 0)
//#                 _iDestY -= _iHeight;
//#             if ((_iAnchor & RIGHT) != 0)
//#                 _iDestX -= _iWidth;
//#             if ((_iAnchor & VCENTER) != 0)
//#                 _iDestY -= _iHeight>>1;
//#             if ((_iAnchor & HCENTER) != 0)
//#                 _iDestX -= _iWidth>>1;
//#             
//#             boolean bTransV = ((_iTransform & javax.microedition.lcdui.game.Sprite.TRANS_MIRROR) != 0);
//#             boolean bTransH = ((_iTransform & javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180) != 0);
//#             
//#             m_Graphics.pushContext(_iDestX, _iDestY, _iWidth, _iHeight, 0, 0);
//#             
//#             _iDestX -= (bTransV?_vSrc.getWidth()-_iWidth-_iSrcX:_iSrcX);
//#             _iDestY -= (bTransH?_vSrc.getHeight()-_iHeight-_iSrcY:_iSrcY);
//#             
//#             if (bTransH || bTransV)
//#                 _vSrc.m_vImageManip.transformByAngle(0, bTransH, bTransV);
//#             
//#             m_Graphics.translate(_iDestX, _iDestY);
//#             _vSrc.m_vImageManip.applyTransformation();
//#             _vSrc.m_vImageManip.paintBitmap(m_Graphics);
//#             _vSrc.m_vImageManip.resetTransform();
//#             
//#             m_Graphics.translate(-_iDestX, -_iDestY);
//#             m_Graphics.popContext();
//#             
//#             //TODO
            //#elif RenderMode == "Doja"
//#             if ((_iAnchor & BOTTOM) != 0)
//#                 _iDestY -= _iHeight;
//#             if ((_iAnchor & RIGHT) != 0)
//#                 _iDestX -= _iWidth;
//#             if ((_iAnchor & VCENTER) != 0)
//#                 _iDestY -= _iHeight >> 1;
//#             if ((_iAnchor & HCENTER) != 0)
//#                 _iDestX -= _iWidth >> 1;
//#             
//#             m_Graphics.setFlipMode(_iTransform);
//#             m_Graphics.drawImage(_vSrc.m_vImage, _iDestX, _iDestY, _iSrcX, _iSrcY, _iWidth, _iHeight);
//#             m_Graphics.setFlipMode(0);
            //#endif

        } else {

            int destW = (_iWidth * m_iSizeX) >> 8;
            int destH = (_iHeight * m_iSizeY) >> 8;
            if (destW == 0 || destH == 0) {
                return;
            }
            
            //#if RenderMode == "MIDlet"
            // anchor
            if ((_iAnchor & BOTTOM) != 0)
                _iDestY -= (_iHeight * m_iSizeY) >> 8;
            if ((_iAnchor & RIGHT) != 0)
                _iDestX -= (_iWidth * m_iSizeX) >> 8;
            if ((_iAnchor & VCENTER) != 0)
                _iDestY -= (_iHeight * m_iSizeY) >> 9;
            if ((_iAnchor & HCENTER) != 0)
                _iDestX -= (_iWidth * m_iSizeX) >> 9;
            
            int iDrawRGB_OffsetX=0, iDrawRGB_OffsetY=0;

            try {
            // create pixel arrays
            int[] srcPixels = new int[_iWidth];
            int[] destPixels = new int[destW]; // array to hold destination pixels

            // precalculate src/dest ratios
            int ratioW = (_iWidth << FP_BITS) / destW;
            int ratioH = (_iHeight << FP_BITS) / destH;

            
            // fix the bug when we try to paint out of the canvas
            //#if CanvasClipping
            if (_iDestX < m_Graphics.getClipX()) {
                iDrawRGB_OffsetX = m_Graphics.getClipX() - _iDestX;
            }
            if (_iDestX+destW > m_Graphics.getClipX()+m_Graphics.getClipWidth()) {
                destW -= (_iDestX+destW) - (m_Graphics.getClipX()+m_Graphics.getClipWidth());
            }
            if (_iDestY < m_Graphics.getClipY()) {
                iDrawRGB_OffsetY = m_Graphics.getClipY()-_iDestY;
            }
            if (_iDestY+destW > m_Graphics.getClipY()+m_Graphics.getClipHeight()) {
                destH -= (_iDestY+destH) - (m_Graphics.getClipY()+m_Graphics.getClipHeight());
            }
            //#endif

            // simple point sampled resizing loop through the destination pixels,
            // find the matching pixel on the source and use that

            int srcX, srcY;
            int iTransparentColor = _vSrc.getTransparentColor();
            // no transparency

            //#if UseAlpha == "true"
            if (m_iAlpha > 254) 
            //#endif

            {
                
                for (int destY = iDrawRGB_OffsetY; destY < destH; ++destY) {
                    srcY = (destY * ratioH) >> FP_BITS; // calculate beginning of sample

                    _vSrc.getRGB(srcPixels, 0, _iWidth, _iSrcX, srcY + _iSrcY, _iWidth, 1);

                    if ((_iTransform & HFLIP) != 0) {
                        for (int destX = iDrawRGB_OffsetX; destX < destW; ++destX) {
                            srcX = (destX * ratioW) >> FP_BITS; // calculate beginning of sample

                            //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                             if (srcPixels[srcX] == iTransparentColor)
//#                                 destPixels[destPixels.length-1-destX] = 0x00000000;
//#                             else
                            //#endif
                                
                            destPixels[destPixels.length - 1 - destX] = srcPixels[srcX];
                        }

                    } else {
                        for (int destX = iDrawRGB_OffsetX; destX < destW; ++destX) {
                            srcX = (destX * ratioW) >> FP_BITS; // calculate beginning of sample

                            //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                             if (srcPixels[srcX] == iTransparentColor)
//#                                 destPixels[destX] = 0x00000000;
//#                             else
                            //#endif
                                
                            destPixels[destX] = srcPixels[srcX];
                        }
                    }
                    //#if RenderMode == "MIDlet"
                    m_Graphics.drawRGB(destPixels, 0, destW, _iDestX, _iDestY + destY, destW, 1, true);
                    //#elif RenderMode == "UIApp"
//#                     m_Graphics.drawRGB (destPixels, 0, destW, _iDestX, _iDestY + destY, destW, 1);
                    //#endif

                }
            }

            // transparency
            //#if UseAlpha == "true"
            else {

                int iAlpha = (m_iAlpha << 24);
                for (int destY = iDrawRGB_OffsetY; destY < destH; ++destY) {
                    srcY = (destY * ratioH) >> FP_BITS; // calculate beginning of sample

                    _vSrc.getRGB(srcPixels, 0, _iWidth, _iSrcX, srcY + _iSrcY, _iWidth, 1);

                    if ((_iTransform & HFLIP) != 0) {
                        for (int destX = iDrawRGB_OffsetX; destX < destW; ++destX) {
                            srcX = (destX * ratioW) >> FP_BITS; // calculate beginning of sample

                            //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                             if (srcPixels[srcX] == iTransparentColor)
//#                                 destPixels[destPixels.length-1-destX] = 0x00000000;
//#                             else
//#                                 destPixels[destPixels.length-1-destX] = srcPixels[srcX] + iAlpha;
//#                             
                            //#else
                            if (((srcPixels[srcX] >> 24) & 0xFF) == 0) {
                                destPixels[destPixels.length - 1 - destX] = srcPixels[srcX];
                            } else {
                                destPixels[destPixels.length - 1 - destX] = srcPixels[srcX] + iAlpha;
                            }
                            //#endif

                        }

                    } else {
                        for (int destX = iDrawRGB_OffsetX; destX < destW; ++destX) {
                            srcX = (destX * ratioW) >> FP_BITS; // calculate beginning of sample

                            //#if s60dp3 || s60dp3n73 || s60dp3e61
//#                             if (srcPixels[srcX] == iTransparentColor)
//#                                 destPixels[destX] = 0x00000000;
//#                             else
//#                                 destPixels[destX] = srcPixels[srcX] + iAlpha;
//#         
                            //#else
                            if (((srcPixels[srcX] >> 24) & 0xFF) == 0) {
                                destPixels[destX] = srcPixels[srcX];
                            } else {
                                destPixels[destX] = srcPixels[srcX] + iAlpha;
                            }
                            //#endif

                        }
                    }
                    //#if RenderMode == "MIDlet"
                    m_Graphics.drawRGB(destPixels, 0, destW, _iDestX, _iDestY + destY, destW, 1, true);
                    //#elif RenderMode == "UIApp"
//#                     m_Graphics.drawRGB (destPixels, 0, destW, _iDestX, _iDestY + destY, destW, 1);
                    //#endif
                }
            }
            //#endif

            } catch (Exception e) {
                //#if Debug
//# //                MiscUtils.DebugMsg ( "Graphics.drawRegion() - Drawing error. Excpt:"+e.getMessage ()+"("+e.toString ()+")" );
                //#endif
            }
            
            //#elif RenderMode == "UIApp"
//# 
//#             // anchor
//#             if ((_iAnchor & BOTTOM) != 0)
//#                 _iDestY -= destH;
//#             if ((_iAnchor & RIGHT) != 0)
//#                 _iDestX -= destW;
//#             if ((_iAnchor & VCENTER) != 0)
//#                 _iDestY -= destH>>1;
//#             if ((_iAnchor & HCENTER) != 0)
//#                 _iDestX -= destW>>1;
//#             
//#             boolean bTransV = ((_iTransform & javax.microedition.lcdui.game.Sprite.TRANS_MIRROR) != 0);
//#             boolean bTransH = ((_iTransform & javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180) != 0);
//#             
//#             m_Graphics.pushContext(_iDestX, _iDestY, destW, destH, 0, 0);
//#             
//#             _iDestX -= (((bTransV?_vSrc.getWidth()-_iWidth-_iSrcX:_iSrcX) * m_iSizeX) >> 8);
//#             _iDestY -= (((bTransH?_vSrc.getHeight()-_iHeight-_iSrcY:_iSrcY) * m_iSizeY) >> 8);
//#             
//#             if (bTransH || bTransV)
//#                 _vSrc.m_vImageManip.transformByAngle(0, bTransH, bTransV);
//#             
//#             m_Graphics.translate(_iDestX, _iDestY);
//#             _vSrc.m_vImageManip.setScaleX((Fixed32.ONE * m_iSizeX) >> 8);
//#             _vSrc.m_vImageManip.setScaleY((Fixed32.ONE * m_iSizeY) >> 8);
//#             _vSrc.m_vImageManip.applyTransformation();
//#             _vSrc.m_vImageManip.paintBitmap(m_Graphics);
//#             _vSrc.m_vImageManip.resetTransform();
//#             
//#             m_Graphics.translate(-_iDestX, -_iDestY);
//#             m_Graphics.popContext();
//#             
            //#elif RenderMode == "Doja"
//#             _vSrc.m_vImage.setAlpha(m_iAlpha);
//#             m_Graphics.setFlipMode(_iTransform);
//#             m_Graphics.drawScaledImage (_vSrc.m_vImage,
//#                 _iDestX, _iDestY, destW, destH,
//#                 _iSrcX, _iSrcY, _iWidth, _iHeight);
//# 
//#             _vSrc.m_vImage.setAlpha(255);
//#             m_Graphics.setFlipMode(0);
            //#endif
        }
        //#endif
    }
    //void drawImage(Image image, int _iX, int _iY, int _iAnchor, int _iTransform) {
    //}   
}
