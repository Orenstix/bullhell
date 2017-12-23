package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.Game;

public class DesktopLauncher {
   public static void main (String[] arg) {
      LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
      config.title = "Orengame";
      config.width = 600;
      config.resizable = false;
      config.vSyncEnabled = true;
      config.samples = 0;
      config.height = 640;
      new LwjglApplication(new Game(), config);
   }
}