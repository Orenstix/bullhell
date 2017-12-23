/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.Enemy;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

/**
 *
 * @author 22b05
 */
public class Entidad{
    private int health;
    private Point2D.Double pos;
    private boolean side;
    private int refImg;
    private double speed;
    private volatile double speedX;
    private volatile double speedY;
    private int size;
    private int maxHealth;
    private volatile boolean life;
    private boolean bounded;
    private int bounds;
    private int point;
    Entidad(int health, Point2D.Double pos, boolean side, int refImg, double speed, int size, int point){
        this.point = point;
        this.health = health;
        this.pos = pos;
        this.side = side;
        this.speed = speed;
        this.maxHealth = health;
        this.speedX = 0;
        this.speedY = 0;
        this.refImg = refImg;
        this.size = size;
        life = true;
        bounded = false;
    }
    public void update(){
        if(health > 0){
            if(!bounded){
                pos.setLocation(pos.getX() + speed * speedX, pos.getY() + speed * speedY);
            } else {
                if(pos.getX() + speedX * speed >= bounds && pos.getX() + speedX * speed <= 600 - bounds){
                    pos.setLocation(pos.getX() + speed * speedX, pos.getY());
                }
                if(pos.getY() + speedY * speed >= bounds * 2 && pos.getY() + speedY * speed <= 640 - bounds){
                    pos.setLocation(pos.getX(), pos.getY() + speed * speedY);
                }
            }
        } else {
            life = false;
        }
    }
    public void damage(int dmg){
        health = health - dmg; 
    }
    public Point2D.Double getPos(){
        return this.pos;
    }
    public void changePos(Point2D.Double newPos){
        this.pos = newPos;
    }
    public boolean getSide(){
        return side;
    }
    public int getSprite(){
        return refImg;
    }
    public void setSpeed(double speed){
        this.speed = speed;
    }

    public void setSpeedY(double speed) {
        if(speedY + speed <= 1 && speedY + speed >= -1){
            speedY += speed;
        }
    }
    public void setSpeedX(double speed) {
        if(speedX + speed <= 1 && speedX + speed >= -1){
            speedX += speed;
        }
    }
    public int getHealth(){
        return health;
    }
    public int getSize(){
        return size;
    }
    public boolean life(){
        return life;
    }

    public void kill() {
        life = false;
    }

    public void stop() {
        speedX = 0;
        speedY = 0;
    }
    public void bound(int bound){
        this.bounds = bound;
        bounded = true;
    }
    public double getSpeed(){
        if(speedX == 0 && speedY == 0){
            return 0;
        }
        return speed;
    }
    public void setPos(double x, double y){
        pos.setLocation(x, y);
    }
    public int maxHealth(){
        return maxHealth;
    }
    public int getPoints(){
        return point;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }
}
