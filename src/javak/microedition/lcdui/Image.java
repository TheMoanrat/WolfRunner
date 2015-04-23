package javak.microedition.lcdui;

// Defines. WARNING! If you edit this you will need to change the defines in the Graphics class too!

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

///#define RenderMode = "Lwuit"
///#define RenderMode = "OpenGL"

import java.io.IOException;
import java.io.InputStream;

//#if UseSpriteForFlip
//# import javax.microedition.lcdui.game.Sprite;
//#endif

//#if DirectGraphics
//# import com.nokia.mid.ui.DirectGraphics;
//# import com.nokia.mid.ui.DirectUtils;
//#endif

//#if RenderMode == "OpenGL"
//# import java.io.IOException;
//# import java.io.InputStream;
//# import java.nio.ByteBuffer;
//# import javax.microedition.khronos.opengles.GL10;
//#endif

/**
 *
 * @author Fran Matsusaka
 */
public class Image extends Object {
    
    javak.microedition.lcdui.Graphics m_vGraphics;
    
    //#if RenderMode == "MIDlet"
    javax.microedition.lcdui.Image m_vImage;
    //#if UseSpriteForFlip   
//#     javax.microedition.lcdui.game.Sprite m_vSprite;
    //#endif
    //#elif RenderMode == "UIApp"
//#     net.rim.device.api.system.Bitmap m_vImage;
//#     ImageManipulator m_vImageManip;
    //#elif RenderMode == "Doja"
//#     com.nttdocomo.ui.Image m_vImage;
    //#elif RenderMode == "OpenGL"
//#    public int m_iImageID;
    //#endif
    
    private int m_iWidth;
    private int m_iHeight;
    private int m_iWidthHalf;
    private int m_iHeightHalf;
    private int m_iTranparentColor;

    public void delete() {
        //#if RenderMode == "OpenGL"
//#         // no debe ponerse el textureID sino el index de la imagen
//#         // corregir tambien en el engine de android
//#         // TODO android fix!!
//#         ms_iOpenGL.glDeleteTextures(1, new int [] {m_iImageID}, 0);
        //#endif
    }

    //#if RenderMode == "OpenGL"
//#     static GL10 ms_iOpenGL;
//#     public static void InitOpenGL (GL10 _gl) {
//#         ms_iOpenGL = _gl;
//#     }
    //#endif
    
    public Image(String _zPath) throws IOException {
        //#if RenderMode == "MIDlet"
        m_vImage = javax.microedition.lcdui.Image.createImage(_zPath);
        
        // get height / witdh
        m_iWidth = m_vImage.getWidth();
        m_iHeight = m_vImage.getHeight();
                
        //#if UseSpriteForFlip
//#         m_vSprite = new Sprite(m_vImage);
        //#endif

        //#elif RenderMode == "UIApp"
//#         m_vImage = net.rim.device.api.system.Bitmap.getBitmapResource(_zPath.substring(1));
//#         m_vImageManip = new ImageManipulator(m_vImage);
//# 
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
//#       
        //#elif RenderMode == "Doja"
//#         com.nttdocomo.ui.MediaImage vDojaImage = 
//#                 com.nttdocomo.ui.MediaManager.getImage ("resource:///"+_zPath.substring(1, _zPath.length()-4) + ".gif");
//#         vDojaImage.use();
//#         m_vImage = vDojaImage.getImage();
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
//#         //vDojaImage.dispose();
//#         
        //#elif RenderMode == "OpenGL"
//#         try {
//#             javax.microedition.lcdui.Image vImage = javax.microedition.lcdui.Image.createImage(_zPath);
//#             m_iWidth = vImage.getWidth();
//#             m_iHeight = vImage.getHeight();
//#          
//#             int[]  iPixels = new int [m_iWidth * m_iHeight];
//#             vImage.getRGB(iPixels, 0, m_iWidth, 0, 0, m_iWidth, vImage.getHeight());
//#             ByteBuffer bPixels = ByteBuffer.allocateDirect(m_iWidth * m_iHeight * 4); 
//#         
//#             for(int y = 0; y < m_iHeight; y++) {
//#                 for(int x = 0; x < m_iWidth; x++) {
//#                     int pixel = iPixels[y * m_iWidth + x];
//#                     bPixels.put((byte) ((pixel >> 16) & 0xFF)); // Red component
//#                     bPixels.put((byte) ((pixel >> 8) & 0xFF));  // Green component
//#                     bPixels.put((byte) (pixel & 0xFF));         // Blue component
//#                     bPixels.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
//#                 }
//#             }
//#         
//#             bPixels.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
//#          
//#             // You now have a ByteBuffer filled with the color data of each pixel.
//#             // Now just create a texture ID and bind it. Then you can load it using 
//#             // whatever OpenGL method you want, for example:
//#         
//#             // TODO android fix!!
//#             final int[] iTextures = new int[1];
//#             ms_iOpenGL.glEnable(GL10.GL_TEXTURE_2D);
//#             ms_iOpenGL.glGenTextures(1, iTextures, 0); //Generate texture ID
//#             m_iImageID = iTextures[0];
//#         
//#             ms_iOpenGL.glBindTexture(GL10.GL_TEXTURE_2D, m_iImageID); //Bind texture ID
//# 
//#             //Send texel data to OpenGL
//#             ms_iOpenGL.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, 
//#                     m_iWidth, m_iHeight, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bPixels);
//# 
//#             //Setup texture scaling filtering
//#             ms_iOpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST); //GL10.GL_NEAREST
//#             ms_iOpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//# 
//#             //Setup wrap mode
//#             ms_iOpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
//#             ms_iOpenGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
//#             ms_iOpenGL.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
//#         
//#         
//#             ms_iOpenGL.glDisable(GL10.GL_TEXTURE_2D);
//#         
//#             int iError = ms_iOpenGL.glGetError();
//#             System.out.println(iError);
//#          
//#         } catch (Exception ex) {
//#             int iError = ms_iOpenGL.glGetError();
//#             System.out.println(iError);
//#         }
//#       
//#         System.gc();
//#       
        //#endif

        m_iWidthHalf = (short) (m_iWidth >> 1);
        m_iHeightHalf = (short) (m_iHeight >> 1);
        m_iTranparentColor = setTransparentColor(m_vImage);

    }

    public Image(int _iW, int _iH) {
        //#if RenderMode == "MIDlet"
        m_vImage = javax.microedition.lcdui.Image.createImage(_iW, _iH);
        m_iWidth = m_vImage.getWidth();
        m_iHeight = m_vImage.getHeight();

        //#if UseSpriteForFlip
//#         m_vSprite = new Sprite(m_vImage);
        //#endif

        //#elif RenderMode == "UIApp"
//#         m_vImage = new net.rim.device.api.system.Bitmap(_iW, _iH);
//#         m_vImageManip = new ImageManipulator(m_vImage);
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
//#       
        //#elif RenderMode == "Doja"
//#         m_vImage = com.nttdocomo.ui.Image.createImage(_iW, _iH);
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
//#         
        //#elif RenderMode == "OpenGL"
        //#endif
        
        m_iWidthHalf = (short) (m_iWidth >> 1);
        m_iHeightHalf = (short) (m_iHeight >> 1);
        m_iTranparentColor = setTransparentColor(m_vImage);
        
    }

    public Image(Image _vImage) {
        //#if RenderMode == "MIDlet" || RenderMode == "UIApp" || RenderMode == "Doja"
        m_vImage = _vImage.m_vImage;
        //m_vImageManip = new ImageManipulator(m_vImage);
        m_iWidth = _vImage.getWidth();
        m_iHeight = _vImage.getHeight();

        //#if UseSpriteForFlip
//#         m_vSprite = new Sprite(m_vImage);
        //#endif

        //#elif RenderMode == "OpenGL"
        //#endif
        
        m_iWidthHalf = (short) (m_iWidth >> 1);
        m_iHeightHalf = (short) (m_iHeight >> 1);
        m_iTranparentColor = setTransparentColor(m_vImage);
    }

    public Image(Image _iImage, int _iX, int _iY, int _iW, int _iH, int _iTransform) {
        //#if RenderMode == "MIDlet"

        m_vImage = javax.microedition.lcdui.Image.createImage(_iImage.m_vImage, _iX, _iY, _iW, _iH, _iTransform);
        m_iWidth = m_vImage.getWidth();
        m_iHeight = m_vImage.getHeight();

        //#if UseSpriteForFlip
//#         m_vSprite = new Sprite(m_vImage);
        //#endif

        //#elif RenderMode == "UIApp"
//#         int[] bDataSrc = new int [_iImage.getWidth()*_iImage.getHeight()];
//#         int[] bDataDst = new int [_iW*_iH];
//#         _iImage.m_vImage.getARGB(bDataSrc, 0, _iW, 0, 0, _iW, _iH);
//#       
//#         for (int i=0; i<_iH; i++)
//#             System.arraycopy(bDataSrc, _iX, bDataDst, 0, _iW);
//# 
//#         m_vImage = new net.rim.device.api.system.Bitmap(_iW, _iH);
//#         m_vImage.setARGB(bDataDst, 0, _iW, 0, 0, _iW, _iH);
//#         m_vImageManip = new ImageManipulator(m_vImage);
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
        //#elif RenderMode == "OpenGL"
        //#endif
        
        m_iWidthHalf = (short) (m_iWidth >> 1);
        m_iHeightHalf = (short) (m_iHeight >> 1);
        m_iTranparentColor = setTransparentColor(m_vImage);
        
    }

    public Image(byte[] _bData, int _iOffset, int _iLength) {
        //#if RenderMode == "MIDlet"
        m_vImage = javax.microedition.lcdui.Image.createImage(_bData, _iOffset, _iLength);
        m_iWidth = m_vImage.getWidth();
        m_iHeight = m_vImage.getHeight();

        //#elif RenderMode == "UIApp"
//#         m_vImage = net.rim.device.api.system.Bitmap.createBitmapFromPNG(_bData, _iOffset, _iLength);
//#         m_vImageManip = new ImageManipulator(m_vImage);
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
//#       
        //#elif RenderMode == "OpenGL"
        //#endif
        
        m_iWidthHalf = (short) (m_iWidth >> 1);
        m_iHeightHalf = (short) (m_iHeight >> 1);
        m_iTranparentColor = setTransparentColor(m_vImage);
        
    }

    public Image(InputStream _iInputStream) throws IOException {
        //#if RenderMode == "MIDlet"
        m_vImage = javax.microedition.lcdui.Image.createImage(_iInputStream);
        m_iWidth = m_vImage.getWidth();
        m_iHeight = m_vImage.getHeight();
        //#elif RenderMode == "UIApp"
//#         byte[] data = new byte[_iInputStream.available()];
//#         _iInputStream.read(data, 0, data.length);
//#         _iInputStream.close();
//#         m_vImage = net.rim.device.api.system.Bitmap.createBitmapFromPNG(data, 0, data.length);
//#         m_vImageManip = new ImageManipulator(m_vImage);
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
        //#elif RenderMode == "OpenGL"
        //#endif
        
        m_iWidthHalf = (short) (m_iWidth >> 1);
        m_iHeightHalf = (short) (m_iHeight >> 1);
        m_iTranparentColor = setTransparentColor(m_vImage);
        
    }

    public Image(int[] _iRgb, int _iW, int _iH, boolean _bAlpha) {
        //#if RenderMode == "MIDlet"
        m_vImage = javax.microedition.lcdui.Image.createRGBImage(_iRgb, _iW, _iH, _bAlpha);
        m_iWidth = m_vImage.getWidth();
        m_iHeight = m_vImage.getHeight();
        //#elif RenderMode == "UIApp"
//#         m_vImage = new net.rim.device.api.system.Bitmap(_iW, _iH);
//#         m_vImage.setARGB(_iRgb, 0, _iW, 0, 0, _iW, _iH);
//#         m_vImageManip = new ImageManipulator(m_vImage);
//#         m_iWidth = m_vImage.getWidth();
//#         m_iHeight = m_vImage.getHeight();
        //#elif RenderMode == "OpenGL"
        //#endif

        m_iWidthHalf = (short) (m_iWidth >> 1);
        m_iHeightHalf = (short) (m_iHeight >> 1);
        m_iTranparentColor = setTransparentColor(m_vImage);
        
    }

    // UIApp puede no funcionar adcuadamente, necesita un buen testeo
    public javak.microedition.lcdui.Graphics getGraphics() {
        //#if RenderMode == "MIDlet" || RenderMode == "Doja"
        if (m_vGraphics == null) {
            m_vGraphics = new Graphics(m_iWidth, m_iHeight);
            m_vGraphics.setGraphics(m_vImage.getGraphics());
        }
        //#elif RenderMode == "UIApp"
//#         if (m_vGraphics == null) {
//#             m_vGraphics = new Graphics(m_iWidth, m_iHeight);
            //#if RIM >= "4.7"
    //#         m_vGraphics.setGraphics(net.rim.device.api.ui.Graphics.create(m_vImage));
            //#else
//#             net.rim.device.api.ui.Graphics bbGraphics = new net.rim.device.api.ui.Graphics(m_vImage);
//#             m_vGraphics.setGraphics(bbGraphics);
            //#endif
//#         }
//#         
        //#elif RenderMode == "OpenGL"
        //#endif
        return m_vGraphics;
    }

    //#if RenderMode == "MIDlet"
    private static int setTransparentColor(javax.microedition.lcdui.Image vImage) {
    //#elif RenderMode == "UIApp"
//#     private static int setTransparentColor(net.rim.device.api.system.Bitmap vImage) {
    //#elif RenderMode == "Doja"
//#     private static int setTransparentColor(com.nttdocomo.ui.Image vImage) {
    //#elif RenderMode == "OpenGL"
//#     private static int setTransparentColor(int vImage) {
    //#endif
        int iTransparentColor = 0;
        
        //#if s60dp3 || s60dp3n73 || s60dp3e61
//#         
//#         int[] srcPixels = new int[1];
//#         
        //#if RenderMode == "MIDlet"
//#         vImage.getRGB(srcPixels, 0, 1, 0, 0, 1, 1);
        //#elif RenderMode == "OpenGL"
        //#endif
//#         
//#         iTransparentColor = srcPixels[0];
//#         
//#         if (((iTransparentColor >> 24) & 0xFF) != 0 && iTransparentColor != 0xffffffff)
//#             iTransparentColor = 0;
        //#endif
        
        return iTransparentColor;
    }
    public int getTransparentColor() {
        return m_iTranparentColor;
    }
    
    public int getHeight() {
        return m_iHeight;
    }

    public int getWidth() {
        return m_iWidth;
    }

    public int getHeightHalf() {
        return m_iHeightHalf;
    }

    public int getWidthHalf() {
        return m_iWidthHalf;
    }

    public boolean isMutable() {
        //#if RenderMode == "MIDlet"
        return m_vImage.isMutable();
        //#elif RenderMode == "UIApp"
//#         return m_vImage.isWritable();
        //#elif RenderMode == "Doja"
//#         return true; // ???
        //#elif RenderMode == "OpenGL"
//#         return true;
        //#endif
    }

    public void getRGB(int[] _iRgbData, int _iOffset, int _iScanlength, int _iX, int _iY, int _iW, int _iH) {
        //#if UseDirectGraphicsGetPixels
//#         if (m_vImage.isMutable()) {
//#             DirectGraphics directGraphics = DirectUtils.getDirectGraphics(m_vImage.getGraphics());
//#             directGraphics.getPixels(_iRgbData, 0, _iScanlength, _iX, _iY, _iW, _iH, DirectGraphics.TYPE_INT_8888_ARGB);
//#         } else 
//#             m_vImage.getRGB(_iRgbData, _iOffset, _iScanlength, _iX, _iY, _iW, _iH);
//#         
        //#elif RenderMode == "MIDlet"
        m_vImage.getRGB(_iRgbData, _iOffset, _iScanlength, _iX, _iY, _iW, _iH);
        //#elif RenderMode == "UIApp"
//#         m_vImage.getARGB(_iRgbData, _iOffset, _iScanlength, _iX, _iY, _iW, _iH);
        //#elif RenderMode == "Doja"
//#         m_vImage.getGraphics().getRGBPixels(_iX, _iY, _iW, _iH, _iRgbData, _iOffset);
        //#elif RenderMode == "OpenGL"
        //#endif
    }

    public static Image createImage(String _zSource) throws IOException {
        return new Image(_zSource);
    }

    public static Image createImage(int _iW, int _iH) {
        return new Image(_iW, _iH);
    }

    public static Image createImage(Image _vImage) {
        return new Image(_vImage);
    }

    public static Image createImage(Image _vImage, int _iX, int _iY, int _iW, int _iH, int _iTransform) throws IOException {
        if ((_iTransform & 0xFFFFFFF8) != 0)
            throw new IllegalArgumentException();

        if ((_iX < 0) || (_iY < 0) || (_iX + _iW > _vImage.getWidth()) || (_iY + _iH > _vImage.getHeight()) || (_iW <= 0) || (_iH <= 0))
            throw new IllegalArgumentException();

        if ((_iX == 0) && (_iY == 0) && (_iW == _vImage.getWidth()) && (_iH == _vImage.getHeight()) && (_iTransform == 0))
            return _vImage;

        return new Image(_vImage, _iX, _iY, _iW, _iH, _iTransform);
    }

    public static Image createImage(byte[] _bData, int _iOffset, int _iLength) {
        return new Image(_bData, _iOffset, _iLength);
    }

    public static Image createRGBImage(int[] _iRgb, int _iW, int _iH, boolean _bAlpha) {
        if ((_iW <= 0) || (_iH <= 0))
            throw new IllegalArgumentException();
        if (_iW * _iH > _iRgb.length)
            throw new ArrayIndexOutOfBoundsException();

        return new Image(_iRgb, _iW, _iH, _bAlpha);
    }
}
