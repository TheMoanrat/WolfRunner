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
public class Scenario {

    public static final int TILE_HEIGHT = GfxManager.TILE_DATA[0][GfxManager.SPR_HEIGHT];
    public static final int TILE_WIDTH = GfxManager.TILE_DATA[0][GfxManager.SPR_WIDTH];
    public static int ms_iTiles[][] = new int[1 + Define.BASE_SIZEY / TILE_HEIGHT][1 + Define.BASE_SIZEX / TILE_WIDTH];
    public static int ms_iFirstTileY;
    public static boolean _isSingleObstacle;
    public static boolean _isBigObstacle;
    public static int ms_iObstacleRow;
    public static int ms_iActualRowToObstacle;
    public static int ms_iObstacleCol;
    public static int ms_iObstacle;
    public static final int GRASS_TILE = 0;
    public static final int ROCK_TILE = 1;
    //TODO Temporal
    private static final int MAX_ROW_TO_OBSTACLE = 10;
    private static final int MIN_ROW_TO_OBSTACLE = 5;

    public static void init() {
        ms_iFirstTileY = -GfxManager.TILE_DATA[0][GfxManager.SPR_HEIGHT];
        generateObstacle();
        _isSingleObstacle = true;
    }

    public static void Run() {
        moveTiles();
    }

    public static void moveTiles() {
        ms_iFirstTileY += WolfPack.ms_iPackSpeedY;
        if (ms_iFirstTileY > 0) {
            int[] aux1 = new int[ms_iTiles[0].length];
            int[] aux2 = generateTiles();

            for (int i = 0; i < ms_iTiles.length; i++) {
                for (int e = 0; e < ms_iTiles[i].length; e++) {
                    aux1[e] = ms_iTiles[i][e];
                    ms_iTiles[i][e] = aux2[e];
                    aux2[e] = aux1[e];
                }
            }
            ms_iFirstTileY = -TILE_HEIGHT;
            generateTiles();
            if (!WolfPack.isAnimating) {
                ms_iActualRowToObstacle++;
            }
        }
    }

    public static int[] generateTiles() {
        //TODO implement bigObstacles
//        if (_isSingleObstacle) {
        int[] nextRow = new int[ms_iTiles[0].length];
        if (ms_iObstacleRow == ms_iActualRowToObstacle) {
            for (int i = 0; i < ms_iTiles[0].length; i++) {
                if (i == ms_iObstacleCol) {
                    nextRow[i] = ms_iObstacle;
                } else {
                    nextRow[i] = GRASS_TILE;
                }
            }
            generateObstacle();
        } else {
            for (int i = 0; i < ms_iTiles[0].length; i++) {
                nextRow[i] = GRASS_TILE;
            }
        }
        return nextRow;
//        }
    }

    public static void generateObstacle() {
        ms_iActualRowToObstacle = 0;
        ms_iObstacleRow = Main.Random(MIN_ROW_TO_OBSTACLE, MAX_ROW_TO_OBSTACLE);
        ms_iObstacleCol = Main.Random(0, ms_iTiles[0].length);
        ms_iObstacle = ROCK_TILE;
    }

    public static void Draw(Graphics _g) {
        drawTiles(_g);
    }

    public static void drawTiles(Graphics _g) {
        _g.setClip(0, 0, Define.BASE_SIZEX, Define.BASE_SIZEY);
        for (int i = 0; i < ms_iTiles.length; i++) {
            for (int e = 0; e < ms_iTiles[i].length; e++) {
                switch (ms_iTiles[i][e]) {
                    case GRASS_TILE:
                        drawTile(_g, i, e, GfxManager.GFXID_GRASSTILE);
                        break;
                    case ROCK_TILE:
                        drawTile(_g, i, e, GfxManager.GFXID_ROCKTILE);
                        break;
                }
            }
        }
    }

    public static void drawTile(Graphics _g, int i, int e, int gfxID) {
        _g.drawImage(GfxManager.ms_vImage[gfxID],
                TILE_WIDTH * e,
                ms_iFirstTileY + (TILE_HEIGHT * i),
                0);
    }
}
