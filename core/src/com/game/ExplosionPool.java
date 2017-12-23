/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game;

import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author Tobias
 */
public class ExplosionPool extends Pool<Explosion>{
    
    public ExplosionPool(){
        super();
    }
    public ExplosionPool(int init, int max){
	super(init,max);
    }
    protected Explosion newObject() {
        return new Explosion();
    }
    
}
