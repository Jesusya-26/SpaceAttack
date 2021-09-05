package ru.jesusya.spaceattack.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.jesusya.spaceattack.SpaceAttack;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpaceAttack(), config);
		config.width = SpaceAttack.SCR_WIDTH;
		config.height = SpaceAttack.SCR_HEIGHT;
	}
}
