/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Tobias
 */
public class MusicManager {
    private float volume;
    private int bgm;
    private Array<Music> backgroundMusic;
    private Sound explosion;
    private Sound powerUp;
    public MusicManager(){
        bgm = 0;
        backgroundMusic = new Array<Music>();
        volume = 1.0f;
        backgroundMusic.add(Gdx.audio.newMusic(Gdx.files.internal("Sounds/8bitNeoClassic.ogg")));
        //Insert all other sounds
        for(Music music: backgroundMusic){
            music.setLooping(true);
        }
        powerUp = Gdx.audio.newSound(Gdx.files.internal("Sounds/PowerUp.wav"));
    }
    public void changeVolume(float newVolume){
        volume = newVolume;
        backgroundMusic.get(bgm).setVolume(volume);
    }
    public void playMusic(int bgm, float pos){
        if(bgm != this.bgm || !backgroundMusic.get(this.bgm).isPlaying()){
            if(backgroundMusic.get(this.bgm).isPlaying()){
                backgroundMusic.get(this.bgm).stop();
            }
            this.bgm = bgm;
            backgroundMusic.get(bgm).setVolume(volume);
            backgroundMusic.get(bgm).play();
        }
    }
    public void playPowerUp(){
        powerUp.play(volume);
    }
    public void playExplosion(){
        //explosion.play(volume);
    }
    public void stopMusic(){
        if(backgroundMusic.get(bgm).isPlaying()){
            backgroundMusic.get(bgm).stop();
        }
        
    }
}
