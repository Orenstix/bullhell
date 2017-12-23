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
public class DropPool extends Pool<Drop>{
    public DropPool(){
        super();
    }
    public DropPool(int init, int max){
	super(init,max);
    }
    protected Drop newObject() {
        return new Drop();
    }
}
