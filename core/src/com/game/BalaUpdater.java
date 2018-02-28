/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import java.awt.geom.Point2D;
import java.util.Random;
import com.game.Enemy.Interaccion;

/**
 *
 * @author 22b05
 */
public class BalaUpdater{
    private Bala[] balas;
    private int cantActiva;
    private Array<Explosion> explosionActiva;
    private ExplosionPool exploPool;
    private DropPool dropPool;
    private Array<Drop> dropActivo;
    private final int refreshRate = 30;
    private final int maxBalas = 10000;
    private Random rand;
    private Entity ent;
    private Bomb bomb;
    public final int NOTHING = 0;
    BalaUpdater(){
        balas = new Bala[maxBalas];
        for(int x = 0; x < maxBalas; x++){
            balas[x] = new Bala(refreshRate);
        }
        explosionActiva = new Array<Explosion>();
        dropActivo = new Array<Drop>();
        dropPool = new DropPool();
        exploPool = new ExplosionPool();
        this.ent = null;
        cantActiva = 0;
        rand = new Random();
        bomb = new Bomb();
    }
    public void run(){
        int x;
        for(x = 0; x < cantActiva; x++){
            balas[x].update();
            if(!balas[x].active()){
                swap(x--, --cantActiva);
            } else {
                if(balas[x].isHoming() && ent != null){
                    checkTarget(x);
                }
            }
        }
        for(x = 0; x < explosionActiva.size; x++){
            explosionActiva.get(x).update();
            if(explosionActiva.get(x).isDead()){
                exploPool.free(explosionActiva.get(x));
                explosionActiva.removeIndex(x);
            }
        }
        for(x = 0; x < dropActivo.size; x++){
            dropActivo.get(x).update();
            if(dropActivo.get(x).isDead()){
                dropPool.free(dropActivo.get(x));
                dropActivo.removeIndex(x);
            }
        }
        if(bomb.isExploding()){
            bomb.update();
        }
    }
    public void inicializarBalaTonta(Point2D pos, int refImg,
            double speed, double direction, int size, boolean side, int damage, double offsetX, double offsetY, boolean spin){
        if(cantActiva < maxBalas - 1){
            balas[cantActiva].inicializarBalaTonta(pos, refImg, speed, direction, size, side, damage, offsetX, offsetY, spin);
            cantActiva++;
        }
    }
    public void inicializarBalaHoming(Point2D pos, int refImg,
            double direction, double speed, int size,
            double turnRate, boolean side, int damage, double offsetX, double offsetY, boolean spin){
        if(cantActiva < maxBalas - 1){
            balas[cantActiva].inicializarBalaHoming(pos, refImg, direction, speed, size, null, turnRate, side, damage, offsetX, offsetY, spin);
            if(ent != null){
                checkTarget(cantActiva);
            }
            cantActiva++; 
        }
    }
    public void inicializarBalaExpirante(Point2D pos, int refImg, int damage,
            double direction, double speed, int size, boolean side, int timeLeft, double offsetX, double offsetY, boolean spin){
        if(cantActiva < maxBalas - 1){
            balas[cantActiva].inicializarBalaExpirante(pos, refImg, direction, speed, size, side, timeLeft, damage, offsetX, offsetY, spin);
            cantActiva++;
        }
    }
    private void checkTarget(int current){
        if(!balas[current].hasTarget()){
            for(int cont = 0; cont < ent.size(); cont++){
                if(ent.get(cont).getSide() != balas[current].getSide()){
                    balas[current].assignTarget(ent.get(cont));
                }
            }
        }
    }
    private void swap(int dead, int alive){
        Bala temp = balas[dead];
        balas[dead] = balas[alive];
        balas[alive] = temp;
    }
    public int checkDamage(boolean side, Point2D pos, int size){
        int cont;
        int dmg = 0;
        for(cont = 0; cont < cantActiva; cont++){
            dmg += balas[cont].damage(side, pos, size);
        }
        return dmg;
    }
    public Point2D.Double location(int index){
        return balas[index].getPos();
    }
    public int skin(int index){
        return balas[index].getSkin();
    }
    public int length(){
        return cantActiva;
    }
    public boolean alive(int index){
        return balas[index].active();
    }
    public int angle(int index){
        return balas[index].getDir();
    }
    public void flush(boolean side){
        for(int x = 0; x < maxBalas; x++){
            if(balas[x].getSide() == side){
                if(balas[x].active()){
                    createDrop(balas[x].getPos(), 3);
                }
                balas[x].kill();
            }
        }
    }
    public void createExplosion(Point2D pos, double speed, double direction){
        explosionActiva.add(exploPool.obtain());
        explosionActiva.get(explosionActiva.size - 1).init(pos, speed, direction);
    }
    public void createDrop(Point2D.Double pos, int type){
        dropActivo.add(dropPool.obtain());
        dropActivo.get(dropActivo.size - 1).init(pos, type);
    }
    public void createDrop(double x, double y, int type){
        dropActivo.add(dropPool.obtain());
        dropActivo.get(dropActivo.size - 1).init(x, y, type);
    }
    public Array<Explosion> getExplosion(){
        return explosionActiva;
    }
    public Array<Drop> getDrop(){
        return dropActivo;
    }
    public void getEnemy(Entity ent){
        this.ent = ent;
    }
    public int getBombas(Point2D.Double pos, int size){
        int temp = 0;
        for(Drop currDrop: dropActivo){
            if(!currDrop.isDead()){
                if(currDrop.collision(pos, size)){
                    if(currDrop.getType() == Drop.bomb){
                        temp++;
                        currDrop.kill();
                    }
                }
            }
        }
        return temp;
    }
    public int getPower(Point2D.Double pos, int size){
        int temp = 0;
        for(Drop currDrop: dropActivo){
            if(currDrop.collision(pos, size)){
                if(currDrop.getType() == Drop.power){
                    temp++;
                    currDrop.kill();
                }
                if(currDrop.getType() == Drop.bigPower){
                    temp += 3;
                    currDrop.kill();
                }
            }
        }
        return temp;
    }
    public int getPoints(Point2D.Double pos, int size){
        int temp = 0;
        for(Drop currDrop: dropActivo){
            if(currDrop.collision(pos, size)){
                if(currDrop.getType() == Drop.points){
                    temp += 100;
                    currDrop.kill();
                }   
            }
        }
        return temp;
    }
    public void causeBomb(){
        bomb.explode();
    }
    public float bombAlpha(){
        if(bomb.getAlpha() == 1f){
            flush(false);
        }
        return bomb.getAlpha();
    }
    public boolean flashing(){
        return bomb.isExploding();
    }
}
