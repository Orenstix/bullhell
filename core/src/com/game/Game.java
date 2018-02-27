package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends ApplicationAdapter implements InputProcessor, ApplicationListener {
    SpriteBatch batch;
    ShapeRenderer shape;
    BitmapFont fpsFont;
    StringBuffer pointString;
    StringBuffer itemString;
    private NinePatch healthBar;
    private Texture[] bulletSkin;
    private TextureAtlas planeAtlas;
    private Array<Array<Sprite>> planeSprite;
    private Texture[] special;
    private TextureAtlas explosion;
    private Array<Sprite> explosionSprite;
    private TextureAtlas drop;
    private Array<Sprite> dropSprite;
    private Entity entity;
    private BalaUpdater balas;
    private OrthographicCamera cam;
    @Override
    public void create () {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 600, 640);
        pointString = new StringBuffer(30);
        itemString = new StringBuffer(15);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = com.badlogic.gdx.graphics.Color.BLACK;
        fpsFont = generator.generateFont(parameter);
        balas = new BalaUpdater();
        entity = new Entity(balas);
        balas.getEnemy(entity);
        try {
            createBullets();
            createPlanes();
            createSpecial();
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        generator.dispose();
        Gdx.input.setInputProcessor(this);
    }
    private void createSpecial() throws IOException{
        explosion = new TextureAtlas(Gdx.files.internal("Sprites/Explosion/explosion.pack"));
        explosionSprite = explosion.createSprites("explo");
        healthBar = new NinePatch(new Texture("Sprites/Special/healthbar.png"), 0 , 0 ,0,1);
        special = new Texture[100];
        special[0] = new Texture("Sprites/Special/Focus.png");
        drop = new TextureAtlas(Gdx.files.internal("Sprites/Special/drops.pack"));
        dropSprite = drop.createSprites("drop");
    }
    private void createPlanes() throws IOException{       
        planeAtlas = new TextureAtlas(Gdx.files.internal("Sprites/Vehicle/Vehicle.pack"));
        planeSprite = new Array<Array<Sprite>>();
        planeSprite.add(planeAtlas.createSprites("f14Fighter"));
        planeSprite.add(planeAtlas.createSprites("FirstBoss"));
        planeSprite.add(planeAtlas.createSprites("Dummy"));

    }
    private void createBullets() throws IOException {
        bulletSkin = new Texture[100];
        bulletSkin[0] = new Texture("Sprites/Bullets/pellet.png");
        bulletSkin[1] = new Texture("Sprites/Bullets/Shell.png");
        bulletSkin[2] = new Texture("Sprites/Bullets/OldMissile.png");
        bulletSkin[3] = new Texture("Sprites/Bullets/Drop.png");
        bulletSkin[4] = new Texture("Sprites/Bullets/Nothing.png");
    }

    @Override
    public void render () {
        super.render();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        entity.run();
        balas.run();
        batch.begin();
        for(int x = 0; x < entity.size(); x++){
            batch.draw(planeSprite.get(entity.get(x).getSprite()).get(entity.get(x).getStatus()),
                    (float)entity.get(x).getPos().getX() - planeSprite.get(entity.get(x).getSprite()).get(entity.get(x).getStatus()).getWidth() / 2,
                    640 - (float)entity.get(x).getPos().getY() - planeSprite.get(entity.get(x).getSprite()).get(entity.get(x).getStatus()).getHeight() / 2);
        }
        for(Drop currDrop: balas.getDrop()){
            if(!currDrop.isDead()){
                batch.draw(dropSprite.get(currDrop.getType() - 1),
                        (float)currDrop.getPos().getX() - dropSprite.get(currDrop.getType() - 1).getWidth(),
                        640 - (float)currDrop.getPos().getY() - dropSprite.get(currDrop.getType() - 1).getHeight());
            }
        }
        for(Explosion currExp:balas.getExplosion()){
            if(!currExp.isDead()){
                batch.draw(explosionSprite.get(currExp.getState()),
                        (float)currExp.getPos().getX() - 50, 640 - (float)currExp.getPos().getY() - 50);
            }
        }
        for(int x = 0; x < balas.length(); x++){
            batch.draw(bulletSkin[balas.skin(x)]
                    , (float)balas.location(x).getX() - bulletSkin[balas.skin(x)].getWidth() / 2
                    , 640 - (float)balas.location(x).getY() - bulletSkin[balas.skin(x)].getHeight() / 2
                    , bulletSkin[balas.skin(x)].getWidth() / 2
                    , bulletSkin[balas.skin(x)].getHeight() / 2
                    , bulletSkin[balas.skin(x)].getWidth()
                    , bulletSkin[balas.skin(x)].getHeight()
                    , 1
                    , 1
                    , balas.angle(x)
                    , 0
                    , 0
                    , bulletSkin[balas.skin(x)].getWidth()
                    , bulletSkin[balas.skin(x)].getHeight()
                    , false
                    , false);
        }
        
        if(entity.getPlayer().isSlow()){
            batch.draw(special[0],
                    (float)entity.getPlayer().getPos().getX() - 5,
                    640 - (float)entity.getPlayer().getPos().getY() - 5);
        }
        if(entity.isBossAlive()){
            //healthBar.setMiddleWidth(entity.healthBar());
            //batch.draw(healthBar.getTexture(), 10, 640 - 5);
            healthBar.draw(batch, 10, 640 - 10, entity.healthBar(), 10);
        }
        pointString.delete(0, pointString.length());
        pointString.append(entity.getScore());
        pointString.append(" points");
        
        itemString.delete(0, itemString.length());
        itemString.append("L: ");
        itemString.append(entity.getLives());
        itemString.append(" B: ");
        itemString.append(entity.getPlayer().getBombs());
        
        fpsFont.draw(batch, pointString.toString(), 14, 630);
        fpsFont.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps", 540, 17); //FPS Counter
        fpsFont.draw(batch, itemString, 16, 16);
        batch.end();
        if(balas.flashing()){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shape.begin(ShapeType.Filled);
            shape.setColor(255, 255, 255, balas.bombAlpha());
            shape.rect(0, 0, 600, 640);
            shape.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void dispose () {
            batch.dispose();
            explosion.dispose();
    }
    @Override
    public boolean keyDown(int keyCode){
        if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
            entity.getPlayer().limitSpeed();
        } else {
            entity.getPlayer().regularSpeed();
        }
        if(keyCode == Keys.LEFT){
            entity.getPlayer().setSpeedX(-1);
        }
        if(keyCode == Keys.UP){
            entity.getPlayer().setSpeedY(-1);
        }
        if(keyCode == Keys.RIGHT){
            entity.getPlayer().setSpeedX(1);
        }
        if(keyCode == Keys.DOWN){
            entity.getPlayer().setSpeedY(1);
        }
        if(keyCode == Keys.Z){
            entity.getPlayer().shoot();
        }
        if(keyCode == Keys.X){
            entity.getPlayer().bomb();
        }
        return true;
    }
    @Override
    public boolean keyUp(int keyCode) {
        if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
                entity.getPlayer().limitSpeed();
        } else {
            entity.getPlayer().regularSpeed();
        }
        if(keyCode == Keys.LEFT){
            entity.getPlayer().setSpeedX(1);
        }
        if(keyCode == Keys.UP){
            entity.getPlayer().setSpeedY(1);
        }
        if(keyCode == Keys.RIGHT){
            entity.getPlayer().setSpeedX(-1);
        }
        if(keyCode == Keys.DOWN){
            entity.getPlayer().setSpeedY(-1);
        }
        if(keyCode == Keys.Z){
            entity.getPlayer().stopShoot();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;    
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;    
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }
}
