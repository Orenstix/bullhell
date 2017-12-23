/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game;

import com.badlogic.gdx.utils.Pool.Poolable;
import java.awt.geom.Point2D;

/**
 *
 * @author Tobias
 */
public class Drop implements Poolable{
    public static final int bomb = 4;
    public static final int power = 1;
    public static final int bigPower = 2;
    public static final int points = 3;
    public static final int cancelled = 0;
    Point2D.Double pos;
    double speed;
    int type;
    Drop(){
        pos = new Point2D.Double(0,0);
        type = 0;
    }
    public void init(Point2D.Double pos, int type){
        this.pos.setLocation(pos);
        this.type = type;
        speed = -0.3;
    }
    public void init(double x, double y, int type){
        this.pos.setLocation(x, y);
        this.type = type;
        speed = -1;
    }
    public void update(){
        this.pos.setLocation(this.pos.getX(), this.pos.getY() + 3 * speed);
        if(speed < 1){
            speed += 0.02;
        }
    }
    public boolean isDead(){
        return pos.getX() < -20 || pos.getX() > 620
                || pos.getY() < -20 || pos.getY() > 660
                || type == cancelled;
    }
    public int getType(){
        return type;
    }
    @Override
    public void reset() {
        type = 4;
    }
    public boolean collision(Point2D.Double posShip, int size){
        if(posShip.distance(pos) < this.getSize() + size){
            return true;
        } else {
            return false;
        }
    }
    public int getSize(){
        switch(type){
            case bomb:{
                return 60 + 20;
            }
            case points:{
                return 6 + 20;
            }
            case power:{
                return 18 + 20;
            }
            case bigPower:{
                return 27 + 20;
            }
        }
        return 0;
    }

    public Point2D.Double getPos() {
        return pos;
    }

    public void kill() {
        type = 0;
    }
    
}
