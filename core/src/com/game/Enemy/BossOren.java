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
import com.game.FastAtan2;
import com.game.FastTrig;
import com.game.MusicManager;

/**
 *
 * @author Tobias
 */
public class BossOren extends Entidad implements Interaccion{
    private BalaUpdater bala;
    private long shootMillis;
    private Random rand;
    private double normalSpeed;
    private int bullCount;
    Interaccion player;
    private int status;
    private int wait;
    private int moveStep;
    private int maxHealth;
    private int noShoot;
    private Point2D.Double moveTo;
    public BossOren(MusicManager sound, int refImg, double speed, BalaUpdater bala,
            Interaccion player){
        super(sound, 200000, new Point2D.Double(300, 4), false, refImg, speed, 45, 5000);
        normalSpeed = speed;
        this.bala = bala;
        moveTo = new Point2D.Double(super.getPos().getX(), super.getPos().getY());
        shootMillis = System.currentTimeMillis();
        rand = new Random();
        super.setSpeedY(1);
        bullCount = 0;
        this.player = player;
        wait = 0;
        status = 0;
        noShoot = 0;
    }
    @Override
    public void update(){
        super.update();
        super.damage(bala.checkDamage(super.getSide(), super.getPos(), super.getSize()));
        if(!super.life()){
                super.getMusicManager().playExplosion();
                bala.createExplosion(super.getPos(), normalSpeed, (int)Math.toDegrees(FastAtan2.atan2(
                                    (float)(moveTo.getY() - super.getPos().getY()),
                                    (float)(moveTo.getX() - super.getPos().getX())
                            )));
        }
        if(System.currentTimeMillis() > shootMillis + 60){
            if(super.getHealth() > 150000){
                firstMove();
                firstShoot();
            } else if(super.getHealth() > 100000){
                secondShoot();
                secondMove();
            } else if(super.getHealth() > 50000){
                thirdShoot();
                thirdMove();
            } else {
                fourthShoot();
                fourthMove();
            }
            bullCount++;
            if(bullCount == 100){
                bullCount = 0;
            }
            shootMillis = System.currentTimeMillis();
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
    @Override
    public int getStatus(){
        return status;
    }

    @Override
    public void limitSpeed() {
        super.setSpeed(normalSpeed / 2);
    }
    @Override
    public void regularSpeed() {
        super.setSpeed(normalSpeed);
    }
    private void firstMove(){
        if(super.getPos().getY() > 150 && wait == 0){
            super.stop();
            wait = 1;
            super.setSpeed(0.4);
            super.setSpeedY(1);
        } 
        if(wait == 1){
            if(super.getPos().getY() < 80){
                super.stop();
                super.setSpeedY(1);
            }
            if(super.getPos().getY() > 200){
                super.stop();
                super.setSpeedY(-0.3);
            }
        }
    }
    private void secondMove(){
        if(moveStep == 2){
            if(wait == 0){
                if(super.getPos().getY() > 670){
                    super.setPos(rand.nextInt(100) * 6 + 20, -20);
                    wait = 81;
                }
                super.setSpeed(normalSpeed * 8);
                super.setSpeedY(0.1);
            } else if(wait == 120){
                super.setSpeed(normalSpeed * 2);
                moveTo.setLocation(300, 150);
                wait--;
            }else {
                if((wait % 20 == 0 || super.getPos().getY() < 30
                        || super.getPos().getX() < 50
                        || super.getPos().getX() > 600 - 50)
                        && wait < 81){
                    super.setSpeed(normalSpeed);
                    moveTo.setLocation(rand.nextInt(235) + 81, rand.nextInt(150) + 100);
                    super.stop();
                    super.setSpeedX(FastTrig.cos((int)Math.toDegrees(FastAtan2.atan2(
                                    (float)(moveTo.getY() - super.getPos().getY()),
                                    (float)(moveTo.getX() - super.getPos().getX())
                            ))));
                    super.setSpeedY(FastTrig.sin((int)Math.toDegrees(FastAtan2.atan2(
                                    (float)(moveTo.getY() - super.getPos().getY()),
                                    (float)(moveTo.getX() - super.getPos().getX())
                            ))));
                }
                wait--;
                if(wait == 0){
                    super.stop();
                }
            }
        } else {
            super.stop();
            noShoot = 40;
            bala.flush(super.getSide());
            moveStep = 2;
            wait = 40;
        }
    }
    private void thirdMove(){
        if(moveStep == 3){
            if(wait > 20){
                super.setSpeed(normalSpeed * 0.1);
            } else {
                super.setSpeed(normalSpeed * 5);
            }
            if(super.getPos().getX() < 200){
                super.setSpeedX(1);
            }
            if(super.getPos().getX() > 400){
                super.setSpeedX(-1);
            }
            if(super.getPos().getY() < 50){
                super.setSpeedY(1);
            }
            if(super.getPos().getY() > 250){
                super.setSpeedY(-1);
            }
            if(super.getPos().getX() > 250){
                super.setSpeedX(-0.01);
            } else {
                super.setSpeedX(0.01);
            }
            if(super.getPos().getY() > 150){
                super.setSpeedY(-0.01);
            } else {
                super.setSpeedY(0.01);
            }
            if(wait == 0){
                wait = 200;
            }
            wait--;
        } else {
            moveStep = 3;
            super.stop();
            bala.flush(super.getSide());
            super.setSpeed(0.5);
            super.setSpeedX(-1);
            super.setSpeedY(1);
            noShoot = 40;
            wait = 20;
        }
    }
    private void fourthMove(){
        if(moveStep == 4){
            if(wait == 0){
                moveTo.setLocation(player.getPos().getX(), rand.nextInt(100) + 100);
            }
            if(super.getPos().getX() < moveTo.getX()){
                super.setSpeedX(0.1);
            }
            if(super.getPos().getX() > moveTo. getX()){
                super.setSpeedX(-0.1);
            }
            if(super.getPos().getY() < moveTo.getY()){
                super.setSpeedY(0.1);
            }
            if(super.getPos().getY() > moveTo.getY()){
                super.setSpeedY(-0.1);
            }
            if(wait == 0){
                wait = 100;
            }
            wait--;
        } else {
            moveStep = 4;
            noShoot = 40;
            super.stop();
            bala.flush(super.getSide());
            super.setSpeed(normalSpeed * 0.7);
            super.setSpeedX(1);
            super.setSpeedY(1);
            wait = 100;
        }
    }
    private void firstShoot(){
        if(noShoot == 0){
            if(bullCount % 20 == 0){
                for(int x = 150; x > 30; x -= 10){
                    bala.inicializarBalaTonta(super.getPos(), 1, 1, Math.toRadians(x) , 4, super.getSide(), 10
                    , rand.nextInt(40) - 20 , rand.nextInt(40) - 20, false);
                }
            }
        } else {
            noShoot = noShoot--;
        }
    }
    private void secondShoot(){
        if(noShoot == 0){
            if(wait != 0){
                if(bullCount % 3 == 0){
                    for(int x = -5; x < 5; x++){
                        bala.inicializarBalaTonta(super.getPos(), 0, 10, FastAtan2.atan2((float)(player.getPos().getY() - super.getPos().getY() + 30), (float)(player.getPos().getX() - super.getPos().getX())) + Math.toRadians(x * 20) , 5, super.getSide(), 10
                        , 0 , 30, false);
                    }
                }
            } else {
                bala.inicializarBalaTonta(super.getPos(), 1, 2, 0 + 0.3 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 2, -3.14 - 0.3 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 2, 0 - 0.3 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 2, -3.14 + 0.3 , 4, super.getSide(), 10
                        , 0 , 0, true);
                
                bala.inicializarBalaTonta(super.getPos(), 1, 4, 0 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 4, -3.14 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 4, 0 + 0.15 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 4, -3.14 - 0.15 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 4, 0 - 0.15 , 4, super.getSide(), 10
                        , 0 , 0, true);
                bala.inicializarBalaTonta(super.getPos(), 1, 4, -3.14 + 0.15 , 4, super.getSide(), 10
                        , 0 , 0, true);
            }
        } else {
            noShoot--;
        }
    }
    private void thirdShoot(){
        if(noShoot == 0){
            if(bullCount % 9 == 0 || wait < 20 && bullCount % 3 == 0){
                for(int x = 0; x < 360; x += 5){
                    bala.inicializarBalaTonta(super.getPos(), 5, 1, Math.toRadians(x) , 4, super.getSide(), 10
                    , rand.nextInt(40) - 20 , rand.nextInt(40) - 20, true);
                }
            }
        } else {
            noShoot--;
        }
    }
    private void fourthShoot(){
        if(noShoot == 0){
            if(wait % 50 == 0){
                for(double x = 0.5; x < 5; x += 0.5){
                    for(int angle = rand.nextInt(10); angle < 360; angle += 10){
                        bala.inicializarBalaTonta(super.getPos(), 0, x, Math.toRadians(angle) + Math.toRadians(90), 5, super.getSide(), 10, FastTrig.cos(angle) * 100, FastTrig.sin(angle) * 100, true);
                    }
                }
            }
            if(wait % 30 == 0){
                bala.inicializarBalaTonta(super.getPos(), 0, 10, FastAtan2.atan2((float)(player.getPos().getY() -super.getPos().getY()), (float)(player.getPos().getX() - super.getPos().getX())), 5, super.getSide(), 10, 0 ,0 , false);
            }
            if(wait % 30 == 28){
                bala.inicializarBalaTonta(super.getPos(), 0, 10, FastAtan2.atan2((float)(player.getPos().getY() -super.getPos().getY()), (float)(player.getPos().getX() - super.getPos().getX())) - Math.toRadians(3), 5, super.getSide(), 10, 0 ,0 , false);
                bala.inicializarBalaTonta(super.getPos(), 0, 10, FastAtan2.atan2((float)(player.getPos().getY() -super.getPos().getY()), (float)(player.getPos().getX() - super.getPos().getX())) + Math.toRadians(3), 5, super.getSide(), 10, 0 ,0 , false);
            }
            if(wait % 30 == 26){
                bala.inicializarBalaTonta(super.getPos(), 0, 10, FastAtan2.atan2((float)(player.getPos().getY() -super.getPos().getY()), (float)(player.getPos().getX() - super.getPos().getX())) - Math.toRadians(6), 5, super.getSide(), 10, 0 ,0 , false);
                bala.inicializarBalaTonta(super.getPos(), 0, 10, FastAtan2.atan2((float)(player.getPos().getY() -super.getPos().getY()), (float)(player.getPos().getX() - super.getPos().getX())) + Math.toRadians(6), 5, super.getSide(), 10, 0 ,0 , false);
            }
        } else {
            noShoot--;
        }
    }
}
