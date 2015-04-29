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
    public boolean _isJumpingFrame1;
    public boolean _isJumpingFrame2;
    public int _iActualFrame = 0;
    public static int ms_iWidth = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_WIDTH];
    public static int ms_iHeight = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_HEIGHT];
    public int _iPositionXInPack;
    public int _iPositionYInPack;

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

    public void initWolf(float positionXInPack, float positionYInPack) {
        this._iPositionXInPack = (int) positionXInPack;
        this._iPositionYInPack = (int) positionYInPack;
        //Provisional
        //TODO provisional
        this.x = WolfPack.ms_iX + _iPositionXInPack;
        this.y = WolfPack.ms_iY + _iPositionYInPack;
        setActive();
    }

    public void Run() {
        if (_isAnimating) {
            changeFrame();
        } else if (_isActive) {
            changeFrame();
            move();
            if (_isJumping) {
                jump();
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
        x = WolfPack.ms_iX + _iPositionXInPack;
        if (!_isJumping
                &&y >  WolfPack.ms_iJumpPointRatio[1]
                && y  < WolfPack.ms_iJumpPointRatio[0]) {
            _isJumping = true;
            _isJumpingFrame1 = true;
        }
    }
    public int jumpCountDown;
    public final int JUMP_DURATION = (int) (1 * Main.SECOND);
    public int jumpFrame1CountDown;
    public final int JUMP_FRAME1_DURATION = JUMP_DURATION / 4;
    public int jumpFrame2CountDown;
    public final int JUMP_FRAME2_DURATION = JUMP_DURATION / 2;

    public void jump() {
        jumpCountDown += Main.deltaTime;
        if (_isJumpingFrame1) {
            jumpFrame1CountDown += Main.deltaTime;
        }
        if (jumpFrame1CountDown > JUMP_FRAME1_DURATION) {
            jumpFrame1CountDown = 0;
            _isJumpingFrame1 = false;
            _isJumpingFrame2 = true;
        }
        if (_isJumpingFrame2) {
            jumpFrame2CountDown += Main.deltaTime;
        }
        if (jumpFrame2CountDown > JUMP_FRAME2_DURATION) {
            jumpFrame2CountDown = 0;
            _isJumpingFrame1 = true;
            _isJumpingFrame2 = false;
        }
        if (jumpCountDown > JUMP_DURATION) {
            jumpFrame1CountDown = 0;
            _isJumpingFrame1 = false;
            _isJumping = false;
            jumpCountDown = 0;
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
        _g.setColor(0, 0, 0);
        _g.drawString("" + x, 20, 20);
        if (_isJumping) {
            if (_isJumpingFrame1) {
                _g.setClip((int) x,
                        (int) y,
                        GfxManager.SPRITE_DATA[GfxManager.WOLF[5]][GfxManager.SPR_WIDTH],
                        GfxManager.SPRITE_DATA[GfxManager.WOLF[5]][GfxManager.SPR_HEIGHT]);
                _g.drawImage(
                        GfxManager.ms_vImage[GfxManager.GFXID_WOLF],
                        (int) x - GfxManager.SPRITE_DATA[GfxManager.WOLF[5]][GfxManager.SPR_POS_X],
                        (int) y - ms_iHeight,
                        0);
            } else if (_isJumpingFrame2) {

                _g.setClip((int) x,
                        (int) y,
                        GfxManager.SPRITE_DATA[GfxManager.WOLF[6]][GfxManager.SPR_WIDTH],
                        GfxManager.SPRITE_DATA[GfxManager.WOLF[6]][GfxManager.SPR_HEIGHT]);
                _g.drawImage(
                        GfxManager.ms_vImage[GfxManager.GFXID_WOLF],
                        (int) x - GfxManager.SPRITE_DATA[GfxManager.WOLF[6]][GfxManager.SPR_POS_X],
                        (int) y - ms_iHeight,
                        0);
            }
        } else {
            _g.drawRect((int)x,
                    (int)y,
                    GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_WIDTH],
                    GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_HEIGHT]);
            _g.setClip((int) x,
                    (int) y,
                    GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_WIDTH],
                    GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_HEIGHT]);
            _g.drawImage(
                    GfxManager.ms_vImage[GfxManager.GFXID_WOLF],
                    (int) x - GfxManager.SPRITE_DATA[GfxManager.WOLF[_iActualFrame]][GfxManager.SPR_POS_X],
                    (int) y,
                    0);

            _g.setClip((int) 0,
                    (int) 0,
                    Define.BASE_SIZEX,
                    Define.BASE_SIZEY);
        }
    }
}
