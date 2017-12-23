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

/**
 *
 * @author 22b05
 */
public class Jugador extends Entidad implements Interaccion{
    private BalaUpdater bala;
    private boolean shoot;
    private long shootMillis;
    private Random rand;
    private double normalSpeed;
    private int bullCount;
    private boolean slow;
    private int power;
    private int pickedPoints;
    private int bombs;
    public Jugador(Point2D.Double pos, boolean side, int refImg, double speed, BalaUpdater bala){
        super(1, pos, side, 0, speed, 1, 0);
        super.bound(25);
        normalSpeed = speed;
        this.bala = bala;
        shootMillis = System.currentTimeMillis();
        slow = false;
        bombs = 3;
        rand = new Random();
    }

    public Jugador(Point2D.Double pos, boolean side, int refImg, double speed, BalaUpdater bala, double speedX, double speedY) {
        super(1, pos, side, 0, speed, 1, 0);
        super.bound(25);
        normalSpeed = speed;
        this.bala = bala;
        shootMillis = System.currentTimeMillis();
        slow = false;
        bombs = 3;
        rand = new Random();
        super.setSpeedX(speedX);
        super.setSpeedY(speedY);
    }
    @Override
    public void update(){
        super.update();
        pickStuff();
        if(shoot){
            if(bullCount % 12 == 0){
                bala.inicializarBalaHoming(super.getPos(), 2,
                    -3.14 / 2 - ((rand.nextInt(11) - 5) * Math.toRadians(15)), 20, 5,
                    0.1, super.getSide(), 50, (rand.nextInt(3) - 1) * 10, 0, false);
                
                bala.inicializarBalaHoming(super.getPos(), 2,
                    -3.14 / 2 - ((rand.nextInt(11) - 5) * Math.toRadians(15)), 20, 5,
                    0.1, super.getSide(), 50, (rand.nextInt(3) - 1) * 10, 0, false);
            }
            if(bullCount % 3 == 0){
                bala.inicializarBalaTonta(super.getPos(), 3, 15, -3.14 / 2, 4, super.getSide(), 20, -10 + rand.nextInt(7), -rand.nextInt(10) - 10, false);
                bala.inicializarBalaTonta(super.getPos(), 3, 15, -3.14 / 2, 4, super.getSide(), 20, 10 - rand.nextInt(7), -rand.nextInt(10) - 10, false);
            }
            bullCount++;
            if(bullCount == 100){
                bullCount = 0;
            }
            shootMillis = System.currentTimeMillis();
        }
        if(!shoot){
            bullCount = 0;
        }
        if(slow){
            super.setSpeed(normalSpeed * 0.5);
        } else {
            super.setSpeed(normalSpeed);
        }
        super.damage(bala.checkDamage(super.getSide(), super.getPos(), super.getSize()));
        if(super.getHealth() < 0){
            bala.createExplosion(super.getPos(), 0, 0);
        }
        
    }
    private void pickStuff(){
        pickedPoints += bala.getPoints(super.getPos(), super.getSize());
        power += power + bala.getPower(super.getPos(), super.getSize());
        bombs += bombs + bala.getBombas(super.getPos(), super.getSize());
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
        shoot = true;
    }
    @Override
    public void stopShoot(){
        shoot = false;
    }
    @Override
    public int getStatus(){
        return 0;
    }
    @Override
    public void limitSpeed() {
        slow = true;
    }
    @Override
    public void regularSpeed() {
        slow = false;
    }
    public boolean isSlow(){
        return slow;
    }
    public int pickedUpPoints(){
        int temp = pickedPoints;
        pickedPoints = 0;
        return temp;
    }
}
