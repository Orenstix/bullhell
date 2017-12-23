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
public class Explosion implements Poolable{
    private Point2D.Double pos;
    private int state;
    private double speed;
    private int direction;
    public Explosion(){
        this.pos = new Point2D.Double();
        this.state = 0;
        this.speed = 0;
        this.direction = 0;
        this.pos.setLocation(0,0);
    }
    public void init(Point2D pos, double speed, double direction){
        state = 0;
        this.pos.setLocation(pos);
        this.speed = speed;
        this.direction = (int)Math.toDegrees(direction);
    }
    public Point2D.Double getPos(){
        return pos;
    }
    @Override
    public void reset() {
        state = 0;
    }
    public void update(){
        state++;
        pos.setLocation(pos.getX() + speed * FastTrig.cos(direction), pos.getY() + speed * FastTrig.sin(direction));
    }
    public int getState(){
        return state;
    }
    public boolean isDead(){
        if(state > 72){
            return true;
        }
        return false;
    }
}
