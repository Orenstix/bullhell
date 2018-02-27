/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game;

import com.game.Enemy.Interaccion;
import java.awt.Point;
import java.awt.geom.Point2D;
import com.game.FastAtan2;
import com.game.FastTrig;

/**
 *
 * @author 22b05
 */
public class Bala {
    private volatile boolean life;
    private boolean expiring;
    private volatile boolean isHoming;
    private Interaccion target;
    private int timeLeft;
    private volatile int refImg;
    private Point2D.Double pos;
    private double speed;
    private double direction;
    private double turnRate;
    private int size;
    private int damage;
    private volatile boolean side;
    private long millis;
    private double compensatedTurnRate;
    private double nuAngle;
    private float cacheX;
    private float cacheY;
    private int refreshRate;
    private double compensatedSpeed;
    private boolean spin;
    private double currSpin;
    Bala(int refreshRate){
        pos = new Point2D.Double(0,0);
        life = false;
        this.refreshRate = refreshRate;
    }

    public void inicializarBalaTonta(Point2D pos, int refImg,
            double speed, double direction, int size, boolean side, int damage, double offsetX, double offsetY, boolean spin){
        life = true;
        this.pos.setLocation(pos.getX() + offsetX, pos.getY() + offsetY);
        this.refImg = refImg;
        this.spin = spin;
        this.direction = direction;
        this.speed = speed;
        this.size = size;
        turnRate = 0;
        this.side = side;
        this.damage = damage;
        isHoming = false;
        timeLeft = 1000;
        currSpin = (int)(Math.toRadians(direction));
        millis = System.currentTimeMillis();
    }
    public void inicializarBalaHoming(Point2D pos, int refImg,
            double direction, double speed, int size, Interaccion target,
            double turnRate, boolean side, int damage, double offsetX, double offsetY, boolean spin){
        life = true;
        this.pos.setLocation(pos.getX() + offsetX, pos.getY() + offsetY);
        this.refImg = refImg;
        this.direction = direction;
        this.speed = speed;
        this.size = size;
        this.target = target;
        isHoming = true;
        this.turnRate = turnRate;
        this.spin = spin;
        this.side = side;
        this.damage = damage;
        timeLeft = 1000;
        currSpin = (int)(Math.toRadians(direction));
        millis = System.currentTimeMillis();
    }
    public void inicializarBalaExpirante(Point2D pos, int refImg,
            double direction, double speed, int size, boolean side, int timeLeft, int damage, double offsetX, double offsetY, boolean spin){
        life = true;
        this.refImg = refImg;
        this.direction = direction;
        this.speed = speed;
        this.size = size;
        this.timeLeft = timeLeft;
        expiring = true;
        target = null;
        this.side = side;
        this.damage = damage;
        this.spin = spin;
        millis = System.currentTimeMillis();
        currSpin = (int)(Math.toRadians(direction));
        
    }
    public void update(){
        compensatedSpeed =  (System.currentTimeMillis() - millis) * speed / refreshRate;
        if(spin){
            currSpin += 1;
            if(currSpin == 360){
                currSpin = 0;
            }
        }
        if(isHoming){
            if(target != null && target.life()){
                moveHoming(System.currentTimeMillis() - millis);
            } else {
                target = null;
            }
        }
        pos.setLocation(pos.getX() + FastTrig.cos((int)Math.toDegrees(direction)) * compensatedSpeed, pos.getY() + FastTrig.sin((int)Math.toDegrees(direction)) * compensatedSpeed);
        if(expiring){
            timeLeft--;
        }
        if((timeLeft == 0 && expiring) || 
                (pos.getX() > 640) || 
                (pos.getY() > 680) || 
                (pos.getX() < -40) ||
                (pos.getY() < -40)){
            kill();
        }
        millis = System.currentTimeMillis();
    }
    public boolean isHoming(){
        return isHoming;
    }
    public boolean hasTarget(){
        return target != null;
    }

    private void moveHoming(long delay) {
        cacheY = (float)(target.getPos().getY() - pos.getY());
        cacheX = (float)(target.getPos().getX() - pos.getX());
        nuAngle = FastAtan2.atan2(cacheY, cacheX);
        compensatedTurnRate = turnRate;
        if(direction > 3.14 / 2 && nuAngle < -3.14 / 2 && nuAngle < 3.14 / 2){
            direction += turnRate; 
        } else if(direction < -3.14 / 2 && nuAngle > 3.14 / 2){
            direction -= turnRate;
        } else {
            if(nuAngle > direction){
                direction += turnRate;
            } else {
                direction -= turnRate;
            }
        }
        if(direction > 3.14){
            direction -= 3.14 * 2;
        }
        if(direction < -3.14){
            direction += 3.14 * 2;
        }
    }
    public Point2D.Double getPos(){
        return pos;
    }
    public int damage(boolean hit, Point2D personPos, int enemySize){
        if(life){
            if(hit != side){
                if(pos.distance(personPos) < size + enemySize){
                    kill();
                    isHoming = false;
                    return damage;
                }
            }
        }
        return 0;
    }
    public boolean active(){
        return life;
    }
    public void assignTarget(Interaccion target){
        this.target = target;
    }
    public boolean getSide(){
        return side;
    }
    public int getSkin(){
            return refImg;
    }
    public int getDir(){
        if(!spin){
            return (int)(-Math.toDegrees(direction) + 270) % 360;
        } else {
            return (int)currSpin % 360;
        }
    }
    public void kill() {
            life = false;
    }
}
