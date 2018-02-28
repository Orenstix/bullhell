/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game;

/**
 *
 * @author Tobias
 */
public class FastTrig {
    private static final double[] COS;
    private static final double[] SIN;
    static{
        COS = new double[360];
        SIN = new double[360];
        for(int x = 0; x < 360; x++){
            COS[x] = Math.cos(Math.toRadians(x));
            SIN[x] = Math.sin(Math.toRadians(x));
        }
    }
    static public double cos(int x){
        while(x < 0){
            x = (x + 360) % 360;
        }
        return COS[(x + 360 * 10) % 360];
    }
    static public double sin(int x){
        while(x < 0){
            x = (x + 360) % 360;
        }
        return SIN[(x + 360 * 10) % 360];
    }
}
