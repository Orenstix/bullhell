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
import com.game.MusicManager;

/**
 *
 * @author 22b05
 */
public class Jugador extends Entidad implements Interaccion{
    private BalaUpdater bala;
    private boolean shoot;
    private boolean soundPower;
    private boolean soundBomb;
    private long shootMillis;
    private long ghostTime;
    private Random rand;
    private double normalSpeed;
    private int bullCount;
    private boolean slow;
    private int power;
    private int pickedPoints;
    private int bombs;
    private int bombTimeout;
    public Jugador(MusicManager sound, Point2D.Double pos, boolean side, int refImg, double speed, BalaUpdater bala){
        super(sound, 1, pos, side, 0, speed, 1, 0);
        super.bound(25);
        normalSpeed = speed;
        this.bala = bala;
        shootMillis = System.currentTimeMillis();
        slow = false;
        bombs = 3;
        rand = new Random();
        ghostTime = 0;
        bombTimeout = 0;
    }

    public Jugador(MusicManager sound, Point2D.Double pos, boolean side, int refImg, double speed, BalaUpdater bala, double speedX, double speedY) {
        super(sound, 1, pos, side, 0, speed, 1, 0);
        super.bound(25);
        normalSpeed = speed;
        this.bala = bala;
        shootMillis = System.currentTimeMillis();
        slow = false;
        bombs = 3;
        rand = new Random();
        super.setSpeedX(speedX);
        super.setSpeedY(speedY);
        ghostTime = 100;
        bombTimeout = 0;
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
        if(ghostTime <= 0){
            super.damage(bala.checkDamage(super.getSide(), super.getPos(), super.getSize()));
        } else {
            ghostTime--;
        }
        if(super.getHealth() < 0){
            bala.createExplosion(super.getPos(), 0, 0);
            bala.flush(!super.getSide());
        }
        if(bombTimeout > 0){
            bombTimeout--;
        }
    }
    private void pickStuff(){
        int tempPower = power;
        int tempPoints = pickedPoints;
        int tempBombs = bombs;
        pickedPoints += bala.getPoints(super.getPos(), super.getSize());
        power += bala.getPower(super.getPos(), super.getSize());
        bombs += bala.getBombas(super.getPos(), super.getSize());
        if(power > tempPower){
            super.getMusicManager().playPowerUp();
        }
        if(pickedPoints > tempPoints){
            super.getMusicManager().playGetPoint();
        }
        if(bombs > tempBombs){
            super.getMusicManager().playBomb();
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
    public int getBombs(){
        return bombs;
    }
    public void bomb() {
        if(bombTimeout == 0){
            if(bombs > 0){
                for(int x = 0; x < 50; x++){
                    bala.inicializarBalaHoming(super.getPos(), 2,
                    Math.toRadians(rand.nextInt(360)), 15, 5,
                    0.1, super.getSide(), 420, (rand.nextInt(5) - 3) * 50, (rand.nextInt(5) - 3) * 50, false);
                }
                super.getMusicManager().playBomb();
                bala.causeBomb();
                ghostTime = 15;
                bombTimeout = 100;
                bombs--;
            }
        }
    }
}
