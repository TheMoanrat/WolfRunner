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
    public boolean _isJumping;
    public int _iActualFrame = 0;
    public static int ms_iWidth = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_WIDTH];
    public static int ms_iHeight = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_HEIGHT];
    public int _iPositionXInPack;
    public int _iPositionYInPack;
    public int _iColInPack;
    public int _iRowInPack;
    
    public Wolf() {
        this.x = 0;
        this.y = 0;
        _isAnimating = true;
    }
    //<editor-fold defaultstate="collapsed" desc="State changes">

    public void setActive() {
        _isActive = true;
        _isAnimating = false;
        _isDying = false;
    }

    public void setAnimating() {
        _isActive = false;
        _isAnimating = true;
        _isDying = false;
    }

    public void setDying() {
        _isActive = false;
        _isAnimating = false;
        _isDying = true;
    }

    public void deactivate() {
        _isActive = false;
        _isAnimating = false;
        _isDying = false;
    }
    //</editor-fold>

    public void initWolf(float positionXInPack, float positionYInPack, int rowInPack, int colInPack) {
        this._iPositionXInPack = (int) positionXInPack;
        this._iPositionYInPack = (int) positionYInPack;
        this._iRowInPack = rowInPack;
        this._iColInPack = colInPack;
        //Provisional
        //TODO provisional
        this.x = WolfPack.ms_fX + _iPositionXInPack;
        this.y = WolfPack.ms_fY + _iPositionYInPack;
        setActive();
    }

    public void Run() {
        if (_isAnimating) {
            changeFrame();
        } else if (_isActive) {
            move();
            if (_isJumping) {
                jump();
            } else {
                changeFrame();
            }
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
            if (_iActualFrame > 4) {
                _iActualFrame = 0;
            }
        }
    }

    //movement
    public void move() {
        x = (WolfPack.ms_fX + _iPositionXInPack);
        if (y < WolfPack.ms_iJumpPoint
                && y + ms_iHeight > WolfPack.ms_iJumpPoint) {
            _isJumping = true;
            _iActualFrame = 3;
        }
    }
    public int jumpCountDown;
    public final int JUMP_DURATION = (int) (1 * Main.SECOND);
    public final int JUMP_FRAME1_DURATION = JUMP_DURATION / 4;
    public final int JUMP_FRAME2_DURATION = JUMP_DURATION / 2 + JUMP_FRAME1_DURATION;

    public void jump() {
        jumpCountDown += Main.deltaTime;
        if (jumpCountDown < JUMP_DURATION) {
            if (jumpCountDown < JUMP_FRAME1_DURATION) {
                _iActualFrame = 5;
            } else if (jumpCountDown < JUMP_FRAME2_DURATION) {
                _iActualFrame = 6;
            } else {
                _iActualFrame = 5;
            }
        } else {
            jumpCountDown = 0;
            _iActualFrame = 3;
            _isJumping = false;
        }
    }

    public void Draw(Graphics _g) {
        if (_isAnimating) {
        } else if (_isActive) {
            drawWolf(_g);
        } else if (_isDying) {
        }
    }

    public void drawWolf(Graphics _g) {
        _g.drawRect((int) x,
                (int) y,
                GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_WIDTH],
                GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_HEIGHT]);
        _g.setClip((int) x,
                (int) y,
                GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_WIDTH],
                GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_HEIGHT]);
        _g.drawImage(
                GfxManager.ms_vImage[GfxManager.GFXID_WOLF],
                (int) x - GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_POS_X],
                (int) y - GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_POS_Y],
                0);
        _g.setClip((int) 0,
                (int) 0,
                Define.BASE_SIZEX,
                Define.BASE_SIZEY);
    }
}
