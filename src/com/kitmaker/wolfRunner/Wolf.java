/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kitmaker.wolfRunner;

import com.kitmaker.manager.GfxManager;
import javak.microedition.lcdui.Graphics;

/**
 *
 * @author Tomeu
 */
public class Wolf extends Vector2 {

    public boolean _isAnimating;
    public boolean _isActive;
    public boolean _isDying;
    public int _iActualFrame = 0;
    public static int ms_iWidth = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_WIDTH];
    public static int ms_iHeight = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_HEIGHT];

    public Wolf(float x, float y) {
        super(x, y);
        _isAnimating = true;
    }
    //<editor-fold defaultstate="collapsed" desc="State changes">
    public void setActive() {
        _isActive= true;
        _isAnimating = false;
        _isDying = false;
    }
    public void setAnimating() {
        _isActive= false;
        _isAnimating = true;
        _isDying = false;
    }
    public void setDying() {
        _isActive= false;
        _isAnimating = false;
        _isDying = true;
    }
    public void deactivate(){
        _isActive= false;
        _isAnimating = false;
        _isDying = false;
    }
    //</editor-fold>
    public void Run() {
        if (_isAnimating) {
            changeFrame();
        } else if (_isActive) {
            changeFrame();
            move();
        } else if (_isDying) {
        }
    }
    //Change frame over time to animate wolf
    private float frameTimeRemaining;
    private final int FRAME_LIFETIME = (int) (Main.SECOND / 15);

    public void changeFrame() {
        frameTimeRemaining += Main.deltaTime;
        if (frameTimeRemaining >= FRAME_LIFETIME) {
            frameTimeRemaining = 0;
            _iActualFrame++;
            if (_iActualFrame >= GfxManager.WOLF.length) {
                _iActualFrame = 0;
            }
        }
    }

    //movement
    public void move() {
        // if none of the speeds is 0
        x += (WolfPack.ms_iPackSpeedX * Main.deltaTime) / Main.SECOND;
    }

    public void Draw(Graphics _g) {
        if (_isAnimating) {
        } else if (_isActive) {
            drawWolf(_g);
        } else if (_isDying) {
        }
    }

    public void drawWolf(Graphics _g) {
        _g.setColor(0, 0, 0);
        _g.drawString("" + y, 20, 20);
        _g.setClip((int) x,
                (int) y,
                ms_iWidth,
                ms_iHeight);
        _g.drawImage(
                GfxManager.ms_vImage[GfxManager.GFXID_WOLF],
                (int) x - GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_POS_X],
                (int) y,
                0);
    }
}
