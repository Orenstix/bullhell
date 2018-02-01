/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.game;

import com.badlogic.gdx.utils.Array;
import java.awt.geom.Point2D;
import java.util.Random;
import com.game.Enemy.BossOren;
import com.game.Enemy.Dummy;
import com.game.Enemy.Interaccion;
import com.game.Enemy.Jugador;

/**
 *
 * @author 22b05
 */
public class Entity{
    private Array<Interaccion> people;
    private Jugador player;
    private long levelMillis;
    private BalaUpdater balas;
    private int level;
    private int score;
    private Random rand;
    private int killCount;
    private int spawnCount;
    private Interaccion currBoss;
    private MusicManager musicManager;
    private int lives;
    private boolean kill;
    Entity(BalaUpdater balas){
        people = new Array<Interaccion>();
        musicManager = new MusicManager();
        this.balas = balas;
        lives = 22222222;
        level = 0;
        levelMillis = System.currentTimeMillis();
        player = new Jugador(musicManager, new Point2D.Double(300, 500), true, 0, 3, balas);
        people.add(player);
        rand = new Random();
        score = 0;
    }
    public void run(){
        int counter;
        for(counter = 0; counter < people.size; counter++){
            if(people.get(counter).life()){
                people.get(counter).update();
            } else {
                score += people.get(counter).getPoints();
                people.removeIndex(counter);
                if(!player.life()){
                    if(lives == 0){
                        kill = true;
                    } else {
                        player = new Jugador(musicManager, new Point2D.Double(300, 500), true, 0, 3, balas, player.getSpeedX(), player.getSpeedY());
                        people.add(player);
                        lives--;
                    }
                }
                killCount++;
            }
            score += player.getPoints();
            levelCheck();
        }
    }
    public void kill(){
        kill = true;
    }
    public Jugador getPlayer(){
        return player;
    }
    public int size(){
        return people.size;
    }
    public Interaccion get(int x){
        try{
            return people.get(x);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }
    public void getBalas(BalaUpdater balas){
        this.balas = balas;
    }

    private void levelCheck() {
        if(level == 0){
            testLevel();
        }
    }
    private void testLevel(){
        musicManager.playMusic(0, 0);
        if(System.currentTimeMillis() > levelMillis + 400 && people.size <= 5 && spawnCount < 15){
            people.add(new Dummy(musicManager, 1000, new Point2D.Double(rand.nextInt(500) + 50, 1), false, 2, 3, balas, people.get(0)));
            spawnCount++;
            levelMillis = System.currentTimeMillis();
        }
        if(killCount >= 13 && currBoss == null){
            currBoss = new BossOren(musicManager, 1, 3, balas, people.get(0));
            people.add(currBoss);
        }
        if(currBoss != null && !currBoss.life()){
            currBoss = null;
            level++;
        }
    }
    public int healthBar(){
        if(currBoss != null){
            return (int)(currBoss.getHealth() * 580 / currBoss.maxHealth()); 
        } else {
            return 0;
        }
    }
    public boolean isBossAlive(){
        return currBoss != null;
    }
    public int getScore(){
        return score;
    }
    public boolean playing(){
        return !kill;
    }
}
