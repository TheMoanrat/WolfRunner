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
//TODO implement the wolfsquad and the wolves
public class WolfPack {

    public static int ms_iX = Define.BASE_SIZEX2;
    public static int ms_iY = Define.BASE_SIZEY2;
    public static int ms_iWidth;
    public static int ms_iPackSpeedX;
    public static int ms_iPackSpeedY;
    public static int[] ms_iJumpPointRatio = new int[2];
    public static int ms_iWolvesAlive = 7;
    public static boolean ms_isInmortal;

    public static void Run() {
        packImputControll();
        movePack();
        jumpPointMovement();
        if (ms_isInmortal) {
            inmortalTimeController();
        } else {
            collideWithObstacle();
        }

        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i].Run();
        }
    }
    //creating the wolf pack
    //0 ,1 , 3,
    //4 ,5 , 6,
    //7 ,8 , 9,
    public static int[][] ms_iWolfPackPositions = {
        //the first dimension are rows and the second columns
        {0, 1, 0},
        {1, 1, 1},
        {1, 0, 1}
    };
    public static Wolf[] ms_wolves = new Wolf[9];

    public static void init() {
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i] = new Wolf();
        }
        for (int i = 0; i < ms_iWolvesAlive; i++) {
            placeWolfInPack(ms_wolves[i]);
        }
        checkPackWidth();
    }

    public static void placeWolfInPack(Wolf wolf) {
        for (int i = 0; i < ms_iWolfPackPositions.length; i++) {
            for (int e = 0; e < ms_iWolfPackPositions[i].length; e++) {
                if (ms_iWolfPackPositions[i][e] == 1) {
                    wolf.initWolf(wolf.ms_iWidth * e, (wolf.ms_iHeight) * i);
                    ms_iWolfPackPositions[i][e] = 2;
                    return;
                }

            }
        }
    }

    public static void checkPackWidth() {
        int _tempNumWolvesInCol = 0;
        for (int fila = 0; fila < ms_iWolfPackPositions.length; fila++) {
            for (int columna = 0; columna < ms_iWolfPackPositions[fila].length; columna++) {
                if (ms_iWolfPackPositions[fila][columna] == 2
                        && _tempNumWolvesInCol < columna) {
                    _tempNumWolvesInCol = columna;
                }
            }
        }
        ms_iWidth = (_tempNumWolvesInCol + 1) * Wolf.ms_iWidth;
    }
    //Pack movement logic
    //<editor-fold defaultstate="collapsed" desc="movement">

    public static void packImputControll() {
        //Y
        ms_iPackSpeedY = (int) ((Define.BASE_SIZEY * Main.deltaTime) / Main.SECOND);
        //X
        if (Main.GameKeyPressed(Main.KEYINT_RIGHT, false) && (ms_iX + ms_iWidth) < Define.BASE_SIZEX) {
            ms_iPackSpeedX = Define.SIZEX / 2;
        } else if (Main.GameKeyPressed(Main.KEYINT_LEFT, false) && ms_iX > 0) {
            ms_iPackSpeedX = -Define.SIZEX / 2;
        } else {
            ms_iPackSpeedX = 0;
        }
        if (Main.GameKeyPressed(Main.KEYINT_FIRE, true)) {
            ms_iJumpPointRatio[0] = ms_iY;
            ms_iJumpPointRatio[1] = Wolf.ms_iHeight;
        }
    }

    public static void movePack() {
        ms_iX += (ms_iPackSpeedX * Main.deltaTime) / Main.SECOND;
    }

    public static void jumpPointMovement() {
        if (ms_iJumpPointRatio[0] > Define.SIZEY) {
            ms_iJumpPointRatio[0] = 0;
            ms_iJumpPointRatio[1] = 0;
        } else {
            ms_iJumpPointRatio[0] += ms_iPackSpeedY;
            ms_iJumpPointRatio[1] += ms_iPackSpeedY;
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="colisions">

    public static void collideWithObstacle() {
        //Check the whole array of tiles
        for (int i = 0; i < Scenario.ms_iTiles.length; i++) {
            for (int e = 0; e < Scenario.ms_iTiles[i].length; e++) {
                //checking tiles alive
                switch (Scenario.ms_iTiles[i][e]) {
                    case Scenario.ROCK_TILE:
                        //check the wolves array
                        for (int u = 0; u < ms_wolves.length; u++) {
                            if (checkColision(ms_wolves[u],
                                    Scenario.TILE_WIDTH * e,
                                    Scenario.ms_iFirstTileY + (Scenario.TILE_HEIGHT * i),
                                    Scenario.TILE_WIDTH,
                                    Scenario.TILE_HEIGHT)) {
                                killWolf(ms_wolves[u]);
                                break;
                            }
                        }
                        break;
                }
            }
        }
    }

    public static boolean checkColision(Wolf w, int obstX, int obstY, int obstWidth, int obstHeight) {
        if (w.y < obstY + obstHeight
                && w.y + w.ms_iHeight > obstY
                && w.x < obstX + obstWidth
                && w.x + w.ms_iWidth > obstX
                && w._isActive
                && !w._isJumping) {
            return true;
        }
        return false;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Damage control">

    public static void killWolf(Wolf w) {
        w.setDying();
        ms_isInmortal = true;
        ms_iWolvesAlive--;
    }
    public static float ms_fInmortalityCountDown;
    public static final int INMORTALITY_DURATION = (int) (1 * Main.SECOND);

    public static void inmortalTimeController() {
        ms_fInmortalityCountDown += Main.deltaTime;
        if (ms_fInmortalityCountDown > INMORTALITY_DURATION) {
            ms_fInmortalityCountDown = 0;
            ms_isInmortal = false;
        }
    }
    //</editor-fold>

    public static void Draw(Graphics _g) {
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i].Draw(_g);
        }
    }
}
