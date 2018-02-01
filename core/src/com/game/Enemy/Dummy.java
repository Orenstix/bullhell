/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.game.Enemy;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;
import com.game.BalaUpdater;
import com.game.Drop;
import com.game.MusicManager;

/**
 *
 * @author 22b05
 */
public class Dummy extends Entidad implements Interaccion{
    private BalaUpdater bala;
    private long shootMillis;
    private Random rand;
    private double normalSpeed;
    private int bullCount;
    Interaccion player;
    public Dummy(MusicManager sound, int health, Point2D.Double pos, boolean side, int refImg, double speed, BalaUpdater bala,
            Interaccion player){
        super(sound, health, pos, side, refImg, speed, 20, 100);
        normalSpeed = speed;
        this.bala = bala;
        shootMillis = System.currentTimeMillis();
        rand = new Random();
        super.setSpeedY(1);
        bullCount = 0;
        this.player = player;
    }
    @Override
    public void update(){
        super.update();
        super.damage(bala.checkDamage(super.getSide(), super.getPos(), super.getSize()));
        if(System.currentTimeMillis() > shootMillis + 60){
            bullCount++;
            if(bullCount == 20) {
                for(int x = 25; x < 65 + 90; x += 20){
                    bala.inicializarBalaTonta(super.getPos(), 0, 2, Math.toRadians(x) , 5, super.getSide(), 10
                    , rand.nextInt(20) - 10 , rand.nextInt(20) - 10, true);
                }
                bullCount = 0;
            }
            shootMillis = System.currentTimeMillis();
        }
        if(!super.life()){
            bala.createExplosion(super.getPos(), super.getSpeed(), Math.toRadians(90));
            bala.createDrop(super.getPos().getX(), super.getPos().getY(), Drop.bigPower);
            super.getMusicManager().playExplosion();
        }
        if((super.getPos().getX() > 600) || 
                (super.getPos().getY() > 640) || 
                (super.getPos().getX() < 0) ||
                (super.getPos().getY() < 0)){
            super.kill();
        }
        if(super.getPos().getY() > 150 && super.getPos().getY() < 210){
            super.setSpeed(.07);
        } else if (super.getPos().getY() > 210){
            super.setSpeed(3);
        }
    }


    @Override
    public int getSize() {
        return super.getSize();
    }
    @Override
    public boolean life() {
        return super.life();
    }
    @Override
    public void setSpeedY(int y){
        super.setSpeedY(y);
    }
    @Override
    public void setSpeedX(int x){
        super.setSpeedX(x);
    }

    @Override
    public void shoot() {
        //Siempre dispara
    }

    @Override
    public void stopShoot() {
        //Siempre dispara
    }
    public int getStatus(){
        return 0;
    }

    @Override
    public void limitSpeed() {
        super.setSpeed(normalSpeed / 2);
    }
    @Override
    public void regularSpeed() {
        super.setSpeed(normalSpeed);
    }
}
