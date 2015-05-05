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

    public int state;
    public static final int ST_WOLF_WAITING = 1;
    public static final int ST_WOLF_ANIMATING = ST_WOLF_WAITING + 1;
    public static final int ST_WOLF_ACTIVE = ST_WOLF_ANIMATING + 1;
    public static final int ST_WOLF_DYING = ST_WOLF_ACTIVE + 1;
    public boolean isJumping;
    public boolean isFirstAnimation;
    public int actualFrame = 0;
    public static int width = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_WIDTH];
    public static int height = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_HEIGHT];
    public int colInPack;
    public int rowInPack;
    public int iSpawnTimelapse;
    public int actualPositionX;
    public int actualPositionY;

    public Wolf() {
        super(-width, -height);
    }

    public void initWolf(int rowInPack, int colInPack, int spawnTimelapse) {
        this.rowInPack = rowInPack;
        this.colInPack = colInPack;
        this.x = (int) (WolfPack.x - ((WolfPack.ms_iWolfPackPositions[0].length / 2) * width) + (width / 2) + (colInPack * width));
        this.y = (int) rowInPack * (height) + WolfPack.y;
        this.iSpawnTimelapse = spawnTimelapse;
        state = ST_WOLF_WAITING;
        initFirstSpawnAnimation();
        isFirstAnimation = true;
    }

    public void Run() {
        switch (state) {
            case ST_WOLF_WAITING:
                spawnDelayController();
                break;
            case ST_WOLF_ANIMATING:
                if (isFirstAnimation) {
                    animateFirstSpawn();
                }
                changeFrame();
                break;
            case ST_WOLF_ACTIVE:
                move();
                if (isJumping) {
                    jump();
                } else {
                    if (!WolfPack.isInmortal) {
                        collideWithObstacle();
                    }
                    changeFrame();
                }
                break;
            case ST_WOLF_DYING:
                break;
        }
    }
    //Change frame over time to animate wolf
    private float frameTimeRemaining;
    private final int FRAME_LIFETIME = (int) (Main.SECOND / 15);

    public void changeFrame() {
        frameTimeRemaining += Main.deltaTime;
        if (frameTimeRemaining >= FRAME_LIFETIME) {
            frameTimeRemaining = 0;
            actualFrame++;
            if (actualFrame > 4) {
                actualFrame = 0;
            }
        }
    }
    public long spawnCountdown;

    public void spawnDelayController() {
        spawnCountdown += Main.deltaTime;
        if (spawnCountdown > iSpawnTimelapse) {
            spawnCountdown = 0;
            state = ST_WOLF_ANIMATING;
        }
    }
    public BezierCurve _bez;
    public long animElapsedTime;
    public final int ANIMATION_DURATION = (int) (3 * Main.SECOND);
    public Vector2 _temporalPosition = new Vector2();

    public void initFirstSpawnAnimation() {
        if (colInPack < WolfPack.ms_iWolfPackPositions.length / 2) {
            _bez = new BezierCurve(
                    //primer punto
                    -width,
                    Define.BASE_SIZEY2,
                    //Segundo punto
                    Define.BASE_SIZEX2,
                    Define.BASE_SIZEY2,
                    //Tercer punto
                    Define.BASE_SIZEX2,
                    (int) (y),
                    //Quarto punto
                    (int) (x),
                    (int) (y));
        } else if (colInPack > WolfPack.ms_iWolfPackPositions[0].length / 2) {
            _bez = new BezierCurve(
                    //primer punto
                    Define.BASE_SIZEX + width,
                    Define.BASE_SIZEY2,
                    //Segundo punto
                    Define.BASE_SIZEX2 + Define.BASE_SIZEX4,
                    Define.BASE_SIZEY2,
                    //Tercer punto
                    Define.BASE_SIZEX2 + Define.BASE_SIZEX4,
                    (int) (y),
                    //Quarto punto
                    (int) (x),
                    (int) y);
        } else {
            _bez = new BezierCurve(
                    //primer punto
                    Define.BASE_SIZEX2,
                    Define.BASE_SIZEY + height,
                    //Segundo punto
                    Define.BASE_SIZEX2,
                    Define.BASE_SIZEY - height,
                    //Tercer punto
                    (int) x,
                    (int) (y + ((Define.BASE_SIZEY - y) / 2)),
                    //Quarto punto
                    (int) x,
                    (int) y);
        }
    }

    public void animateFirstSpawn() {
        animElapsedTime += Main.deltaTime;
        float elapsedTimeNormalized = (float) animElapsedTime / ANIMATION_DURATION;
        if (elapsedTimeNormalized < 1) {
            _bez.getPos(elapsedTimeNormalized, _temporalPosition);
            x = _temporalPosition.x;
            y = _temporalPosition.y;
        } else {
            animElapsedTime = 0;
            isFirstAnimation = false;
            state = ST_WOLF_ACTIVE;
        }
    }

    //movement
    public void move() {
        x = (int) (WolfPack.x - ((WolfPack.ms_iWolfPackPositions[0].length / 2) * width) + (width / 2) + (colInPack * width));
        if (y < WolfPack.jumpPoint
                && y + height > WolfPack.jumpPoint) {
            isJumping = true;
            actualFrame = 3;
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
                actualFrame = 5;
            } else if (jumpCountDown < JUMP_FRAME2_DURATION) {
                actualFrame = 6;
            } else {
                actualFrame = 5;
            }
        } else {
            jumpCountDown = 0;
            actualFrame = 3;
            isJumping = false;
        }
    }

    public void collideWithObstacle() {
        //Check the whole array of tiles
        for (int i = 0; i < Scenario.ms_iTiles.length; i++) {
            for (int e = 0; e < Scenario.ms_iTiles[i].length; e++) {
                //checking tiles alive
                switch (Scenario.ms_iTiles[i][e]) {
                    case Scenario.ROCK_TILE:
                        //check the wolves array
                        if (checkColision(
                                Scenario.TILE_WIDTH * e,
                                Scenario.ms_iFirstTileY + (Scenario.TILE_HEIGHT * i),
                                Scenario.TILE_WIDTH,
                                Scenario.TILE_HEIGHT)) {
                            WolfPack.killWolf(this);
                            break;
                        }
                }
            }
        }
    }

    public boolean checkColision(int obstX, int obstY, int obstWidth, int obstHeight) {
        if (y < obstY + obstHeight
                && y + height > obstY
                && x < obstX + obstWidth
                && x + width > obstX
                && !isJumping) {
            return true;
        }
        return false;
    }

    public void Draw(Graphics g) {
        switch (state) {
            case ST_WOLF_WAITING:
                break;
            case ST_WOLF_ANIMATING:
            case ST_WOLF_ACTIVE:
                drawWolf(g);
                break;
            case ST_WOLF_DYING:
                break;
        }
    }

    public void drawWolf(Graphics _g) {
        _g.setClip((int) x,
                (int) y,
                GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_WIDTH],
                GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_HEIGHT]);
        _g.drawImage(
                GfxManager.ms_vImage[GfxManager.GFXID_WOLF],
                (int) x - GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_POS_X],
                (int) y - GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_POS_Y],
                0);
        _g.setClip((int) 0,
                (int) 0,
                Define.BASE_SIZEX,
                Define.BASE_SIZEY);
    }
}
