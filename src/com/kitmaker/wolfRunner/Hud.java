/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kitmaker.wolfRunner;

import com.kitmaker.manager.FntManager;
import javak.microedition.lcdui.Graphics;

/**
 *
 * @author Tomeu
 */
public class Hud {

    public static void Run() {
    }

    public static void Draw(Graphics g) {
        drawLifes(g);
        drawPoints(g);
        drawLevel(g);
    }

    public static void drawLifes(Graphics g) {
        FntManager.DrawFont(g, FntManager.FONT_INGAME, WolfPack.wolfLives + "", Define.BASE_SIZEX24, Define.BASE_SIZEY24, 0, 2);
    }

    public static void drawPoints(Graphics g) {
        FntManager.DrawFont(g, FntManager.FONT_INGAME, ModeGame.points + "", Define.BASE_SIZEX - Define.BASE_SIZEX24, Define.BASE_SIZEY24, Graphics.RIGHT, 6);
    }

    public static void drawLevel(Graphics g) {
        FntManager.DrawFont(g, FntManager.FONT_INGAME, ModeGame.level + "", Define.BASE_SIZEX2, Define.BASE_SIZEY24, Graphics.HCENTER, 2);
    }
}
