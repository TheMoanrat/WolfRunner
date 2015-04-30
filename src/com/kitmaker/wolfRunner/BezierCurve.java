/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kitmaker.wolfRunner;


/**
 *
 * @author Stan
 */
public class BezierCurve {

    public static final int prototipeScreenX = 240;
    public static final int prototipeScreenY = 320;
    public Vector2 start = new Vector2();
    public Vector2 cont_p1 = new Vector2();
    public Vector2 cont_p2 = new Vector2();
    public Vector2 end = new Vector2();
    /**
     * Creates a bezier curve 
     * @param startX x of first spot
     * @param startY y of first spot
     * @param cp1x x of second spot
     * @param cp1y y of second spot
     * @param cp2x x of third spot
     * @param cp2y y of third spot
     * @param epx x of last spot
     * @param epy y of last spot
     */
    public BezierCurve(int startX, int startY, int cp1x, int cp1y, int cp2x, int cp2y, int epx, int epy) {
        this.start.set(startX * ((float) Define.BASE_SIZEX / prototipeScreenX), startY * ((float) Define.BASE_SIZEY / prototipeScreenY));
        this.cont_p1.set((cp1x - startX) * ((float) Define.BASE_SIZEX / prototipeScreenX), (cp1y - startY) * ((float) Define.BASE_SIZEY / prototipeScreenY));
        this.cont_p2.set((cp2x - startX) * ((float) Define.BASE_SIZEX / prototipeScreenX), (cp2y - startY) * ((float) Define.BASE_SIZEY / prototipeScreenY));
        this.end.set((epx - startX) * ((float) Define.BASE_SIZEX / prototipeScreenX), (epy - startY) * ((float) Define.BASE_SIZEY / prototipeScreenY));
    }

    private float bezierAt(int a, int b, int c, int d, float t) {
        float remainingTime = 1f - t;
        return (int) ((remainingTime * remainingTime * remainingTime * a) + (remainingTime * remainingTime * b * 3 * t) + (t * t * 3 * remainingTime * c) + (t * t * t * d));
    }

    public void getPos(float posCalc, final Vector2 Position) {
        int xa = 0;
        int xb = (int) cont_p1.x;
        int xc = (int) cont_p2.x;
        int xd = (int) end.x;
        int ya = 0;
        int yb = (int) cont_p1.y;
        int yc = (int) cont_p2.y;
        int yd = (int) end.y;
        int x = (int) bezierAt(xa, xb, xc, xd, posCalc);
        int y = (int) bezierAt(ya, yb, yc, yd, posCalc);
        Position.set(start.x + x, start.y + y);
    }
}
