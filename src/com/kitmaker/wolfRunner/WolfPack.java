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
    public static int ms_iPackSpeedX;
    public static boolean borderRightColision;
    public static boolean borderLeftColision;

    public static void Run() {
        movePack();
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i].Run();
        }
    }
    //creating the wolf pack
    public static int ms_iNumColsPack = 3;
    public static int ms_iLeaderCol = 1;
    //0 ,1 , 3,
    //4 ,5 , 6,
    //7 ,8 , 9,
    public static int[] ms_iWolfSpawnPositions = {
        0, 1, 0,
        1, 1, 1,
        1, 0, 1};
    public static int[] ms_iWolfPositions = {
        0, 1, 0,
        1, 0, 1,
        0, 0, 0};
    public static Wolf[] ms_wolves = new Wolf[ms_iWolfSpawnPositions.length];

    public static void init() {
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i] = new Wolf(0, 0);
            placeWolfInPack(ms_wolves[i]);
        }

    }

    public static void placeWolfInPack(Wolf wolf) {
        for (int i = 0; i < ms_iWolfSpawnPositions.length; i++) {
            if (ms_iWolfSpawnPositions[i] == 1) {
                wolf.set(ms_iX + (wolf.ms_iWidth * (i % ms_iNumColsPack)),
                        ms_iY + (wolf.ms_iHeight * (i / ms_iNumColsPack)));
                ms_iWolfSpawnPositions[i] = 2;
                wolf.setActive();
                break;
            }
        }
    }
    //Pack movement logic

    public static void movePack() {
        //X
        if (Main.GameKeyPressed(Main.KEYINT_RIGHT, false)) {
            ms_iPackSpeedX = Define.SIZEX / 2;
        } else if (Main.GameKeyPressed(Main.KEYINT_LEFT, false)) {
            ms_iPackSpeedX = -Define.SIZEX / 2;
        } else {
            ms_iPackSpeedX = 0;
        }

    }

    public static void Draw(Graphics _g) {
        for (int i = 0; i < ms_wolves.length; i++) {
            ms_wolves[i].Draw(_g);
        }
    }
}
