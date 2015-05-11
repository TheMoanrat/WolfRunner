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
public class WolfPack {

    public static float x;
    public static float y;
    public static int maxWidth;
    public static int ms_iFirstWolfX;
    public static int ms_iFirstRow;
    public static int ms_iPackSpeedX;
    public static int packSpeedY;
    public static int jumpPoint;
    public static boolean ms_isJumping;
    public static int wolfLifes;
    public static int wolvesAlive;
    public static boolean isInmortal;

    public static void Run() {
        switch (Define.ms_iState) {
            case Define.ST_GAME_ANIMATING:
                checkWolvesAnimating();
                calculateWolfYSpeed();
                for (int i = 0; i < ms_wolves.length; i++) {
                    ms_wolves[i].Run();
                }
                break;
            case Define.ST_GAME_RUNNING:
                packInputControll();
                jumpPointMovement();
                movePack();
                findFirstWolfRow();
                checkWolvesJumping();
                if (isInmortal) {
                    inmortalTimeController();
                }
                calculateWolfYSpeed();
                for (int i = 0; i < ms_wolves.length; i++) {
                    ms_wolves[i].Run();
                }
                break;
            case Define.ST_GAME_OVER:
                for (int i = 0; i < ms_wolves.length; i++) {
                    ms_wolves[i].Run();
                }
        }
    }
    //creating the wolf pack
    //0 ,1 , 3,
    //4 ,5 , 6,
    //7 ,8 , 9,
    public static int[][] positionOfWolvesInPack = {
        //the first dimension are rows and the second columns
        {0, 1, 0,},
        {1, 1, 1,},
        {0, 1, 0,},};
    public static Wolf[] ms_wolves = new Wolf[2*(positionOfWolvesInPack.length * positionOfWolvesInPack[0].length)];

    public static void init() {
        wolfLifes = 100;
        x = Define.BASE_SIZEX2;
        y = Define.BASE_SIZEY2;
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i] = new Wolf();
        }
        for (int i = 0; i < wolfLifes; i++) {
            if (i < ms_wolves.length) {
                if (placeNewWolfInPack(ms_wolves[i])) {
                    continue;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        checkPackWidth();
    }

    public static boolean placeNewWolfInPack(Wolf wolf) {
        for (int i = 0; i < positionOfWolvesInPack.length; i++) {
            for (int e = 0; e < positionOfWolvesInPack[i].length; e++) {
                if (positionOfWolvesInPack[i][e] == 1) {
                    wolf.initWolf(i,e,(int) (Main.SECOND * i));
                    positionOfWolvesInPack[i][e] = 2;
                    return true;
                }
            }
        }
        return false;
    }

    public static void checkPackWidth() {
        int _tempNumWolvesInCol = 0;
        for (int fila = 0; fila < positionOfWolvesInPack.length; fila++) {
            for (int columna = 0; columna < positionOfWolvesInPack[fila].length; columna++) {
                if (positionOfWolvesInPack[fila][columna] >= 2
                        && _tempNumWolvesInCol < columna) {
                    _tempNumWolvesInCol = columna;
                }
            }
        }
        maxWidth = (_tempNumWolvesInCol + 1) * Wolf.width;
    }

    public static void findFirstWolfRow() {
        int tempPosX = Define.BASE_SIZEX;
        for (int i = 0; i < ms_wolves.length; i++) {
            if (ms_wolves[i].state == Wolf.ST_WOLF_ACTIVE) {
                if (ms_wolves[i].x < tempPosX) {
                    tempPosX = (int) ms_wolves[i].x;
                }
            }
        }
        ms_iFirstWolfX = tempPosX;
    }

    public static void checkWolvesAlive() {
        wolvesAlive = 0;
        for (int i = 0; i < positionOfWolvesInPack.length; i++) {
            for (int e = 0; e < positionOfWolvesInPack[i].length; e++) {
                if (positionOfWolvesInPack[i][e] >= 2) {
                    wolvesAlive++;
                }
            }
        }
    }

    public static void checkWolvesAnimating() {
        for (int i = 0; i < ms_wolves.length; i++) {
            if (ms_wolves[i].state > Wolf.ST_WOLF_INACTIVE && ms_wolves[i].state < Wolf.ST_WOLF_ACTIVE) {
                return;
            }
        }
        Main.RequestStateChange(Define.ST_GAME_RUNNING);
    }

    public static void checkWolvesJumping() {
        for (int i = 0; i < ms_wolves.length; i++) {
            if (!ms_wolves[i].isJumping) {
                ms_isJumping = false;
            }
        }
    }

    public static void calculateWolfYSpeed() {
        //Y
        packSpeedY = (int) ((Define.BASE_SIZEY * Main.deltaTime) / Main.SECOND);
    }
    //Pack movement logic
    //<editor-fold defaultstate="collapsed" desc="movement">

    public static void packInputControll() {
        //X
        if (Main.GameKeyPressed(Main.KEYINT_RIGHT, false) && (ms_iFirstWolfX + maxWidth) < Define.BASE_SIZEX) {
            ms_iPackSpeedX = Define.BASE_SIZEX / 2;
        } else if (Main.GameKeyPressed(Main.KEYINT_LEFT, false) && ms_iFirstWolfX > 0) {
            ms_iPackSpeedX = -Define.BASE_SIZEX / 2;
        } else {
            ms_iPackSpeedX = 0;
        }
        if (Main.GameKeyPressed(Main.KEYINT_FIRE, true)) {
            jumpPoint = (int) y;
        }
    }

    public static void movePack() {
        if (ms_isJumping) {
            x += (((ms_iPackSpeedX / 4) * Main.deltaTime) / Main.SECOND);
        } else {
            x += ((ms_iPackSpeedX * Main.deltaTime) / Main.SECOND);
        }
    }

    public static void jumpPointMovement() {
        if (jumpPoint >= y) {
            jumpPoint += packSpeedY;
        } else {
            jumpPoint = 0;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Damage control">
    public static void killWolf(Wolf w) {
        wolfLifes--;
        w.state = Wolf.ST_WOLF_DYING;
        positionOfWolvesInPack[w.rowInPack][w.colInPack] = 1;
        checkWolvesAlive();
        if (wolfLifes > 0) {
            if (wolfLifes - wolvesAlive >= 0) {
                respawnWolf();
            }
        } else {
            Main.RequestStateChange(Define.ST_GAME_OVER);
        }
        findFirstWolfRow();
        checkPackWidth();
    }

    public static void respawnWolf() {
        for (int i = 0; i < ms_wolves.length; i++) {
            if (ms_wolves[i].state == Wolf.ST_WOLF_INACTIVE) {
                if (placeNewWolfInPack(ms_wolves[i])) {
                    return;
                }
            }
        }
    }
    public static float ms_fInmortalityCountDown;
    public static final int INMORTALITY_DURATION = (int) (1 * Main.SECOND);

    public static void inmortalTimeController() {
        ms_fInmortalityCountDown += Main.deltaTime;
        if (ms_fInmortalityCountDown > INMORTALITY_DURATION) {
            ms_fInmortalityCountDown = 0;
            isInmortal = false;
        }
    }
    //</editor-fold>

    public static void Draw(Graphics _g) {
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i].Draw(_g);
        }
    }
}
