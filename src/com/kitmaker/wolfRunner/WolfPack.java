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

    public static float x = Define.BASE_SIZEX2;
    public static float y = Define.BASE_SIZEY2;
    public static int maxWidth;
    public static int ms_iFirstWolfX;
    public static int ms_iFirstRow;
    public static int ms_iPackSpeedX;
    public static int ms_iPackSpeedY;
    public static int jumpPoint;
    public static boolean ms_isJumping;
    public static int ms_iWolvesAlive = 15;
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
        }
    }
    //creating the wolf pack
    //0 ,1 , 3,
    //4 ,5 , 6,
    //7 ,8 , 9,
    public static int[][] ms_iWolfPackPositions = {
        //the first dimension are rows and the second columns
        {0, 1, 0,},
        {1, 1, 1,},
        {0, 1, 0,},};
    public static Wolf[] ms_wolves = new Wolf[ms_iWolfPackPositions.length * ms_iWolfPackPositions[0].length];

    public static void init() {
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i] = new Wolf();
        }
        for (int i = 0; i < ms_iWolvesAlive; i++) {
            if (i < ms_wolves.length) {
                placeWolfInPack(ms_wolves[i]);
            }else{
                break;
            }
        }
        checkPackWidth();
    }

    public static void placeWolfInPack(Wolf wolf) {
        for (int i = 0; i < ms_iWolfPackPositions.length; i++) {
            for (int e = 0; e < ms_iWolfPackPositions[i].length; e++) {
                if (ms_iWolfPackPositions[i][e] == 1) {
                    wolf.initWolf(
                            i,
                            e,
                            (int) (Main.SECOND * i));
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

    public static void checkWolvesAnimating() {
        for (int i = 0; i < ms_wolves.length; i++) {
            if (ms_wolves[i].state > 0 && ms_wolves[i].state < Wolf.ST_WOLF_ACTIVE) {
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
        ms_iPackSpeedY = (int) ((Define.BASE_SIZEY * Main.deltaTime) / Main.SECOND);
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
            jumpPoint += ms_iPackSpeedY;
        } else {
            jumpPoint = 0;
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="colisions">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Damage control">
    public static void killWolf(Wolf w) {
        w.state = Wolf.ST_WOLF_DYING;
        isInmortal = true;
        ms_iWolfPackPositions[w.rowInPack][w.colInPack] = 1;
        ms_iWolvesAlive--;
        respawnWolf();
        findFirstWolfRow();
        checkPackWidth();
    }

    public static void respawnWolf() {
        if (ms_iWolvesAlive > 0) {
            for (int i = 0; i < ms_wolves.length; i++) {
                if (ms_wolves[i].state == 0) {
                    placeWolfInPack(ms_wolves[i]);
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
