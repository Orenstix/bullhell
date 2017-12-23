/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game;

import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author 22b05
 */
public class Tileset {
    private List<Integer> colores;
    private Random rand;
    private int level;
    Tileset(){
        colores = new ArrayList();
        rand = new Random();
        level = 0;
        colores.add(new Color(204,120,16).getRGB());
        colores.add(new Color(167,72,8).getRGB());
        colores.add(new Color(167,72,8).getRGB());
        colores.add(new Color(135,13,13).getRGB());
        colores.add(new Color(105,26,26).getRGB());
        colores.add(new Color(88,22,22).getRGB());
    }
    public void advance(){
        level++;
        colores.clear();
    }
    public int getLength(){
        return colores.size();
    }
    public int getRandColor(){
        return getColor(rand.nextInt(getLength()));
    }
    public int getColor(int index){
        if(index < getLength()){
            return colores.get(index);
        } else {
            return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)).getRGB();
        }
    }
}
