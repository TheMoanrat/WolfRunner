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
    public static int iMapTiles[][] = new int[1 + Define.BASE_SIZEY / TILE_HEIGHT][1 + Define.BASE_SIZEX / TILE_WIDTH];
    public static int iFirstTileY;
    public static final int GRASS_TILE = 0;
    public static final int ROCK_TILE = 1;
    public static final int RIVER_TILE_START = 2;
    public static final int RIVER_TILE_END = 4;

    public static void init() {
        iFirstTileY = -GfxManager.TILE_DATA[1][GfxManager.SPR_HEIGHT];
        isSingleObstacle = true;
        generateSmallObstacle();
        generateBigObstacle();
    }

    public static void Run() {

        switch (Define.ms_iState) {
            case Define.ST_GAME_ANIMATING:
                moveTiles();
                break;
            case Define.ST_GAME_RUNNING:
                checkObstacleIncome();
                moveTiles();
                break;
            case Define.ST_GAME_OVER:
                moveTiles();
        }
    }
    public static boolean isSingleObstacle;
    public static boolean isBigObstacle;

    public static void moveTiles() {
        iFirstTileY += WolfPack.packSpeedY;
        if (iFirstTileY >= 0) {
            //arrays to help move the tiles down
//            int[] aux1 = new int[iMapTiles[0].length];
//            int[] aux2 = generateTiles();
            
            
            for(int i = iMapTiles.length-2; i >= 0; i--){
                System.arraycopy(iMapTiles[i], 0, iMapTiles[i+1], 0, iMapTiles[i].length);
            }
            generateTiles(iMapTiles[0]);
//            for (int i = 0; i < iMapTiles.length; i++) {
//                for (int e = 0; e < iMapTiles[i].length; e++) {
//                    aux1[e] = iMapTiles[i][e];
//                    iMapTiles[i][e] = aux2[e];
//                    aux2[e] = aux1[e];
//                }
//            }
            iFirstTileY = -TILE_HEIGHT;
            rowsToObstacle--;
        }
    }
    public static boolean bObstacleThrown;

    public static void generateTiles(int[] nextRow) {
        if (isSingleObstacle && !bObstacleThrown) {
            for (int i = 0; i < nextRow.length; i++) {
                if (i == iObstacleCol) {
                    nextRow[i] = iObstacleTileType;
                } else {
                    nextRow[i] = GRASS_TILE;
                }
            }
            bObstacleThrown = true;
            obstaclesToBig--;
        } else if (isBigObstacle && !bObstacleThrown) {

            iBigObstTileType++;
            if (iBigObstTileType > iEndBig) {
                isBigObstacle = false;
                isSingleObstacle = true;
                bObstacleThrown = true;
            } else {
                for (int i = 0; i < nextRow.length; i++) {
                    nextRow[i] = iBigObstTileType;
                }
            }
        } else {
            for (int i = 0; i < iMapTiles[0].length; i++) {
                nextRow[i] = GRASS_TILE;
            }
        }
    }

    public static void checkObstacleIncome() {
        if (rowsToObstacle <= 0 && bObstacleThrown) {
            bObstacleThrown = false;
            isSingleObstacle = true;
            generateSmallObstacle();
        } else if (obstaclesToBig <= 0 && bObstacleThrown) {
            bObstacleThrown = false;
            isSingleObstacle = false;
            isBigObstacle = true;
            generateBigObstacle();

        }
    }
    public static int rowsToObstacle;
    public static int iObstacleCol;
    public static int iObstacleTileType;
    //TODO Temporal
    private static final int MAX_ROW_TO_OBSTACLE = 10;
    private static final int MIN_ROW_TO_OBSTACLE = 5;

    public static void generateSmallObstacle() {
        rowsToObstacle = Main.Random(MIN_ROW_TO_OBSTACLE, MAX_ROW_TO_OBSTACLE);
        iObstacleCol = Main.Random(0, iMapTiles[0].length);
        iObstacleTileType = ROCK_TILE;
    }
    public static int obstaclesToBig;
    public static int iEndBig;
    public static int iBigObstTileType;

    public static void generateBigObstacle() {
        obstaclesToBig = Main.Random(MIN_ROW_TO_OBSTACLE, MAX_ROW_TO_OBSTACLE);
        iBigObstTileType = RIVER_TILE_START - 1;
        iEndBig = RIVER_TILE_END;
    }

    public static void Draw(Graphics _g) {
        drawTiles(_g);
    }

    public static void drawTiles(Graphics _g) {
        _g.setClip(0, 0, Define.BASE_SIZEX, Define.BASE_SIZEY);
        for (int i = 0; i < iMapTiles.length; i++) {
            for (int e = 0; e < iMapTiles[i].length; e++) {
                switch (iMapTiles[i][e]) {
                    case GRASS_TILE:
                        drawTile(_g, i, e, GfxManager.GFXID_GRASSTILE);
                        break;
                    case ROCK_TILE:
                        drawTile(_g, i, e, GfxManager.GFXID_ROCKTILE);
                        break;
                    case RIVER_TILE_START:
                        drawTile(_g, i, e, GfxManager.GFXID_FIRST_RIVER_TILE);
                        break;
                    case RIVER_TILE_START + 1:
                        drawTile(_g, i, e, GfxManager.GFXID_SECOND_RIVER_TILE);
                        break;
                    case RIVER_TILE_END:
                        drawTile(_g, i, e, GfxManager.GFXID_THIRD_RIVER_TILE);
                        break;
                }
            }
        }
    }

    public static void drawTile(Graphics _g, int i, int e, int gfxID) {
        _g.drawImage(GfxManager.ms_vImage[gfxID],
                TILE_WIDTH * e,
                iFirstTileY + (TILE_HEIGHT * i),
                0);
    }
}
