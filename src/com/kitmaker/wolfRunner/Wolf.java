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
    public static final int ST_WOLF_INACTIVE = 0;
    public static final int ST_WOLF_WAITING = ST_WOLF_INACTIVE + 1;
    public static final int ST_WOLF_ANIMATING = ST_WOLF_WAITING + 1;
    public static final int ST_WOLF_ACTIVE = ST_WOLF_ANIMATING + 1;
    public static final int ST_WOLF_DYING = ST_WOLF_ACTIVE + 1;
    public boolean isJumping;
    public int actualFrame = 0;
    public static int width = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_WIDTH];
    public static int height = GfxManager.SPRITE_DATA[GfxManager.WOLF[0]][GfxManager.SPR_HEIGHT];
    public int colInPack;
    public int rowInPack;
    public int iSpawnTimelapse;
    public int spawnPointX;
    public int spawnPointY;

    public Wolf() {
        super(-width, -height);
    }

    public void initWolf(int rowInPack, int colInPack, int spawnTimelapse) {
        this.rowInPack = rowInPack;
        this.colInPack = colInPack;
        this.spawnPointX = (int) (WolfPack.x - (colInPack * width) + (width / 2));
        this.spawnPointY = (int) (rowInPack * (height) + WolfPack.y);
        this.iSpawnTimelapse = spawnTimelapse;
        state = ST_WOLF_WAITING;
        initRespawn();
    }
//wolf has independent states
    public void Run() {
        switch (Define.ms_iState) {
            case Define.ST_GAME_ANIMATING:
                //<editor-fold defaultstate="collapsed" desc="Animatioon stats">
                switch (state) {
                    case ST_WOLF_WAITING:
                        spawnDelayController();
                        break;
                    case ST_WOLF_ANIMATING:
                        animateRespawn();
                    case ST_WOLF_ACTIVE:
                        changeFrame();
                        break;
                }
                //</editor-fold>
                break;
            case Define.ST_GAME_RUNNING:
                
                //<editor-fold defaultstate="collapsed" desc="Game runing stats">
                switch (state) {
                    case ST_WOLF_WAITING:
                        spawnDelayController();
                        break;
                    case ST_WOLF_ANIMATING:
                        animateRespawn();
                        changeFrame();
                        collideWithObstacle();
                        if (isJumping) {
                            jump();
                        }
                        break;
                    case ST_WOLF_ACTIVE:
                        move();
                        if (isJumping) {
                            jump();
                        } else {
                            collideWithObstacle();
                            changeFrame();
                        }
                        break;
                    case ST_WOLF_DYING:
                        dustAnimation();
                        deathAnimation();
                        break;
                }
                //</editor-fold>
                break;
            case Define.ST_GAME_OVER:
                dustAnimation();
                deathAnimation();
                break;
        }

    }
    //Change frame over time to animate wolf
    private float frameTimeRemaining;
    private final int FRAME_LIFETIME = (int) (Main.SECOND / 15);

    public void changeFrame() {
        frameTimeRemaining += Main.averageDt;
        if (frameTimeRemaining >= FRAME_LIFETIME) {
            frameTimeRemaining = 0;
            actualFrame++;
            if (actualFrame > 4) {
                actualFrame = 0;
            }
        }
    }
    //<editor-fold defaultstate="collapsed" desc="Spawn and animations">
    public long spawnCountdown;

    public void spawnDelayController() {
        spawnCountdown += Main.averageDt;
        if (spawnCountdown > iSpawnTimelapse) {
            spawnCountdown = 0;
            state = ST_WOLF_ANIMATING;
        }
    }
    public int animationInitPointX;
    public int animationInitPointY;

    public void initRespawn() {
        if (colInPack > WolfPack.positionOfWolvesInPack.length / 2) {
            x = animationInitPointX = -width;
            y = animationInitPointY = Define.BASE_SIZEY2;
        } else if (colInPack < WolfPack.positionOfWolvesInPack[0].length / 2) {
            x = animationInitPointX = Define.BASE_SIZEX + width;
            y = animationInitPointY = Define.BASE_SIZEY2;
        } else {
            x = animationInitPointX = Define.BASE_SIZEX2;
            y = animationInitPointY = Define.BASE_SIZEY + height;
        }
    }
    public float animationPixelsPerSecX;
    public float animationPixelsPerSecY;

    public void animateRespawn() {
        this.spawnPointX = (int) (WolfPack.x - (colInPack * width) + (width / 2));
        animationPixelsPerSecX = (spawnPointX - x);
        animationPixelsPerSecY = (spawnPointY - y);
        if (Math.abs(animationPixelsPerSecX) < 2 && Math.abs(animationPixelsPerSecY) < 2) {
            state = ST_WOLF_ACTIVE;
            WolfPack.positionOfWolvesInPack[rowInPack][colInPack] = 3;
        } else {
            Vector2 direction = new Vector2(animationPixelsPerSecX, animationPixelsPerSecY);
            float distance = direction.length();
            direction.normalize();
            direction.multiply(Math.min(Define.BASE_SIZEX2 * (float) Main.averageDt / Main.SECOND, distance));
            x += direction.x;
            y += direction.y;
        }
    }
    //</editor-fold>
    //movement

    public void move() {
        //x of wolfpack - the total amount of wolves /2 * a wolf width
        //+ half width of a wolf + the position of the wolf in the pack * its width 
        x = (int) (WolfPack.x - (colInPack * width) + (width / 2));
        if (y < WolfPack.jumpPoint
                && y + height > WolfPack.jumpPoint) {
            tryJumping();
        }
    }

    public void tryJumping() {
        isJumping = true;
        actualFrame = 3;
    }

    public void stopJumping() {
        isJumping = false;
        actualFrame = 3;
    }
    public int jumpCountDown;
    public final int JUMP_DURATION = (int) (1 * Main.SECOND);
    public final int JUMP_FRAME1_DURATION = JUMP_DURATION / 4;
    public final int JUMP_FRAME2_DURATION = JUMP_DURATION / 2 + JUMP_FRAME1_DURATION;

    public void jump() {
        jumpCountDown += Main.averageDt;
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
            stopJumping();
        }
    }

    public void collideWithObstacle() {
        //Check the whole array of tiles
        for (int i = 0; i < Scenario.iMapTiles.length; i++) {
            for (int e = 0; e < Scenario.iMapTiles[i].length; e++) {
                //checking tiles alive
                switch (Scenario.iMapTiles[i][e]) {
                    case Scenario.ROCK_TILE:
                        //check the wolves array
                        if (checkColision(
                                Scenario.TILE_WIDTH * e,
                                Scenario.iFirstTileY + (Scenario.TILE_HEIGHT * i),
                                Scenario.TILE_WIDTH,
                                Scenario.TILE_HEIGHT)) {
                            if (WolfPack.isInmortal || state != ST_WOLF_ACTIVE) {
                                tryJumping();
                            } else {
                                WolfPack.isInmortal = true;
                                WolfPack.killWolf(this);
                            }
                        }
                        break;
                    case Scenario.RIVER_TILE_START + 1:
                        if (checkColision(
                                Scenario.TILE_WIDTH * e,
                                Scenario.iFirstTileY + (Scenario.TILE_HEIGHT * i),
                                Scenario.TILE_WIDTH,
                                Scenario.TILE_HEIGHT)) {
                            if (WolfPack.isInmortal || state != ST_WOLF_ACTIVE) {
                                tryJumping();
                            } else {
                                WolfPack.killWolf(this);
                            }
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
    public int deathCountdown;
    public final float DEATH_DURATION = 1 * Main.SECOND;
    public final float DEATH_FRAME1_DURATION = DEATH_DURATION / 4;
    public final float DEATH_FRAME2_DURATION = DEATH_DURATION / 2;

    public void deathAnimation() {
        deathCountdown += Main.averageDt;
        if (deathCountdown < DEATH_DURATION) {
            if (deathCountdown < DEATH_FRAME1_DURATION) {
                actualFrame = 5;
                y -= WolfPack.packSpeedY / 2;
            } else if (deathCountdown < DEATH_FRAME2_DURATION) {
                y += WolfPack.packSpeedY / 2;
                actualFrame = 8;
                isEatingDust = true;
            } else {
                actualFrame = 9;
                y += WolfPack.packSpeedY;
            }
        } else {
            if (y > Define.SIZEY) {
                deathCountdown = 0;
                dustCloudCountdown = 0;
                dustFrame = 0;
                state = ST_WOLF_INACTIVE;
            }
            y += WolfPack.packSpeedY;
        }
    }
    public boolean isEatingDust;
    public int dustCloudCountdown;
    public byte dustFrame;
    public final float DUST_CLOUD_DURATION = (DEATH_DURATION / 4) * 3;
    public final float DUST_CLOUD_FRAMES = GfxManager.DUST.length;
    public final float DUST_CLOUD_FRAME_DURATION = DUST_CLOUD_DURATION / DUST_CLOUD_FRAMES;

    public void dustAnimation() {
        if (isEatingDust) {
            dustCloudCountdown += Main.averageDt;
            if (dustCloudCountdown < DUST_CLOUD_DURATION) {
                if (dustCloudCountdown > DUST_CLOUD_FRAME_DURATION * (dustFrame + 1)) {
                    dustFrame++;
                }
            } else {
                if (y > Define.BASE_SIZEY) {
                    isEatingDust = false;
                    dustCloudCountdown = 0;
                    dustFrame = 0;
                }
            }
        }
    }

    public void Draw(Graphics g) {
        switch (state) {
            case ST_WOLF_ANIMATING:
            case ST_WOLF_ACTIVE:
                drawWolf(g);
                break;
            case ST_WOLF_DYING:
                if (dustFrame < 5 && isEatingDust) {
                    drawDust(g);
                }
                drawWolf(g);
                if (dustFrame >= 5 && dustFrame < GfxManager.DUST.length && isEatingDust) {
                    drawDust(g);
                }
                break;
        }
    }

    public void drawWolf(Graphics g) {
        g.setClip((int) x,
                (int) y,
                GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_WIDTH],
                GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_HEIGHT]);
        g.drawImage(
                GfxManager.ms_vImage[GfxManager.GFXID_WOLF],
                (int) x - GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_POS_X],
                (int) y - GfxManager.SPRITE_DATA[GfxManager.WOLF[actualFrame]][GfxManager.SPR_POS_Y],
                0);
        g.setClip((int) 0,
                (int) 0,
                Define.BASE_SIZEX,
                Define.BASE_SIZEY);
    }

    public void drawDust(Graphics g) {
        g.setClip(((int) x + width / 2) - GfxManager.SPRITE_DATA[GfxManager.DUST[dustFrame]][GfxManager.SPR_WIDTH] / 2,
                (int) y
                - GfxManager.SPRITE_DATA[GfxManager.DUST[dustFrame]][GfxManager.SPR_HEIGHT] / 2,
                GfxManager.SPRITE_DATA[GfxManager.DUST[dustFrame]][GfxManager.SPR_WIDTH],
                GfxManager.SPRITE_DATA[GfxManager.DUST[dustFrame]][GfxManager.SPR_HEIGHT]);
        g.drawImage(
                GfxManager.ms_vImage[GfxManager.GFXID_DUST],
                (int) (((int) x + width / 2) - GfxManager.SPRITE_DATA[GfxManager.DUST[dustFrame]][GfxManager.SPR_WIDTH] / 2) - GfxManager.SPRITE_DATA[GfxManager.DUST[dustFrame]][GfxManager.SPR_POS_X],
                (int) y
                - GfxManager.SPRITE_DATA[GfxManager.DUST[dustFrame]][GfxManager.SPR_HEIGHT] / 2,
                0);
        g.setClip((int) 0,
                (int) 0,
                Define.BASE_SIZEX,
                Define.BASE_SIZEY);
    }
}
