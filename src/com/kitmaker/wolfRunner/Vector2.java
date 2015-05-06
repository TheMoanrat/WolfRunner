/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kitmaker.wolfRunner;

/**
 *
 * @author Stan
 */
public class Vector2 {

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float length() {
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public void normalize() {
        float length = length();
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }

    public void multiply(float number) {
        x *= number;
        y *= number;
    }
}
