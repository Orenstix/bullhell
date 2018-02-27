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
public class Bomb {
    private float alpha;
    private boolean exploding;
    private boolean gettingBrighter;
    Bomb(){
        exploding = false;
        gettingBrighter = false;
    }
    void explode(){
        alpha = 0.01f;
        exploding = true;
        gettingBrighter = true;
    }
    public float getAlpha(){
        return alpha;
    }
    public void update(){
        if(gettingBrighter){
            alpha = alpha + 0.3f * alpha;
        } else {
            alpha = alpha - 0.02f;
        }
        if(alpha > 1){
            gettingBrighter = false;
            alpha = 1;
        }
        if(!gettingBrighter && alpha < 0){
            exploding = false;
            alpha = 0;
        }
    }
    public boolean isExploding(){
        return exploding;
    }
}
