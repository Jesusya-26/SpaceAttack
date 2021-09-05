package ru.jesusya.spaceattack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class SpaceAttack extends ApplicationAdapter {

	public static final int SCR_WIDTH = 540;
	public static final int SCR_HEIGHT = 960;
	long lastEnemySpawnTime;
	long enemySpawnInterval = 2000L;
	long lastShootTime;
	long shootInterval = 1000L;
	int nTrashes = 500;
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	Texture imgShip;
	Texture imgStars;
	Texture imgShoot;
	Texture imgShipEnemy;
	Texture imgTrashEnemy;
	Texture imgTrashShip;
	Sound sndShoot;
	Sound sndExplosion;
	Array<Stars> stars = new Array();
	Ship ship;
	Array<ShipEnemy> shipEnemies = new Array();
	Array<Shoot> shoots = new Array();
	Array<Trash> trashes = new Array();
	Array<Trash> trashesShip = new Array();
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 540.0F, 960.0F);
		this.touch = new Vector3();
		this.loadResources();
		this.stars.add(new Stars(0.0F));
		this.stars.add(new Stars(960.0F));
		this.ship = new Ship();
	}

	@Override
	public void render () {
		this.actions();
		this.camera.update();
		this.batch.setProjectionMatrix(this.camera.combined);
		Gdx.gl.glClearColor(1.0F, 0.0F, 0.0F, 1.0F);
		Gdx.gl.glClear(16384);
		this.batch.begin();

		int i;
		for(i = 0; i < this.stars.size; ++i) {
			this.batch.draw(this.imgStars, ((Stars)this.stars.get(i)).x, ((Stars)this.stars.get(i)).y, (float)((Stars)this.stars.get(i)).width, (float)((Stars)this.stars.get(i)).height);
		}

		for(i = 0; i < this.trashes.size; ++i) {
			this.batch.draw(this.imgTrashEnemy, ((Trash)this.trashes.get(i)).x, ((Trash)this.trashes.get(i)).y, (float)(((Trash)this.trashes.get(i)).width / 2), (float)(((Trash)this.trashes.get(i)).height / 2), (float)((Trash)this.trashes.get(i)).width, (float)((Trash)this.trashes.get(i)).height, 1.0F, 1.0F, ((Trash)this.trashes.get(i)).aRotation, 0, 0, 100, 100, false, false);
		}

		for(i = 0; i < this.trashesShip.size; ++i) {
			this.batch.draw(this.imgTrashShip, ((Trash)this.trashesShip.get(i)).x, ((Trash)this.trashesShip.get(i)).y, (float)(((Trash)this.trashesShip.get(i)).width / 2), (float)(((Trash)this.trashesShip.get(i)).height / 2), (float)((Trash)this.trashesShip.get(i)).width, (float)((Trash)this.trashesShip.get(i)).height, 1.0F, 1.0F, ((Trash)this.trashesShip.get(i)).aRotation, 0, 0, 100, 100, false, false);
		}

		for(i = 0; i < this.shipEnemies.size; ++i) {
			this.batch.draw(this.imgShipEnemy, ((ShipEnemy)this.shipEnemies.get(i)).x, ((ShipEnemy)this.shipEnemies.get(i)).y, (float)((ShipEnemy)this.shipEnemies.get(i)).width, (float)((ShipEnemy)this.shipEnemies.get(i)).height);
		}

		for(i = 0; i < this.shoots.size; ++i) {
			this.batch.draw(this.imgShoot, ((Shoot)this.shoots.get(i)).x, ((Shoot)this.shoots.get(i)).y, (float)((Shoot)this.shoots.get(i)).width, (float)((Shoot)this.shoots.get(i)).height);
		}

		if (this.ship.isAlive) {
			this.batch.draw(this.imgShip, this.ship.x, this.ship.y, (float)this.ship.width, (float)this.ship.height);
		}

		this.batch.end();
	}

	private void actions() {
		int i;
		for(i = 0; i < this.stars.size; ++i) {
			((Stars)this.stars.get(i)).move();
		}

		if (Gdx.input.isTouched()) {
			this.touch.set((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0.0F);
			this.camera.unproject(this.touch);
			Ship var10000 = this.ship;
			var10000.x += (this.touch.x - (this.ship.x + (float)(this.ship.width / 2))) / 20.0F;
		}

		if (this.ship.isAlive && TimeUtils.millis() - this.lastShootTime > this.shootInterval) {
			this.spawnShoot();
		}

		for(i = 0; i < this.shoots.size; ++i) {
			((Shoot)this.shoots.get(i)).move();

			for(int j = 0; j < this.shipEnemies.size; ++j) {
				if (((Shoot)this.shoots.get(i)).overlaps((SpaceObject)this.shipEnemies.get(j))) {
					((Shoot)this.shoots.get(i)).isAlive = false;
					((ShipEnemy)this.shipEnemies.get(j)).isAlive = false;
					this.sndExplosion.play();

					for(int k = 0; k < this.nTrashes; ++k) {
						this.trashes.add(new Trash((SpaceObject)this.shipEnemies.get(j)));
					}
				}
			}

			if (!((Shoot)this.shoots.get(i)).isAlive) {
				this.shoots.removeIndex(i);
			}
		}

		if (TimeUtils.millis() - this.lastEnemySpawnTime > this.enemySpawnInterval) {
			this.spawnEnemy();
		}

		for(i = 0; i < this.shipEnemies.size; ++i) {
			((ShipEnemy)this.shipEnemies.get(i)).move();
			if (((ShipEnemy)this.shipEnemies.get(i)).y < 0.0F && this.ship.isAlive) {
				this.gameOver();
			}

			if (!((ShipEnemy)this.shipEnemies.get(i)).isAlive) {
				this.shipEnemies.removeIndex(i);
			}
		}

		for(i = 0; i < this.trashes.size; ++i) {
			((Trash)this.trashes.get(i)).move();
			if (!((Trash)this.trashes.get(i)).isAlive) {
				this.trashes.removeIndex(i);
			}
		}

		for(i = 0; i < this.trashesShip.size; ++i) {
			((Trash)this.trashesShip.get(i)).move();
			if (!((Trash)this.trashesShip.get(i)).isAlive) {
				this.trashesShip.removeIndex(i);
			}
		}

	}

	void spawnShoot() {
		this.shoots.add(new Shoot(this.ship));
		this.lastShootTime = TimeUtils.millis();
		this.sndShoot.play();
	}

	void spawnEnemy() {
		this.shipEnemies.add(new ShipEnemy());
		this.lastEnemySpawnTime = TimeUtils.millis();
	}

	void gameOver() {
		this.ship.isAlive = false;
		this.sndExplosion.play();

		for(int k = 0; k < this.nTrashes; ++k) {
			this.trashesShip.add(new Trash(this.ship));
		}

	}

	private void loadResources() {
		this.imgShip = new Texture("ship.png");
		this.imgShipEnemy = new Texture("shipenemy.png");
		this.imgStars = new Texture("stars.png");
		this.imgShoot = new Texture("shoot.png");
		this.imgTrashEnemy = new Texture("trashenemy.png");
		this.imgTrashShip = new Texture("trashship.png");
		this.sndShoot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
		this.sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
	}

	public void dispose() {
		this.batch.dispose();
		this.imgShip.dispose();
		this.imgStars.dispose();
		this.imgShoot.dispose();
		this.imgTrashEnemy.dispose();
		this.imgTrashShip.dispose();
		this.sndShoot.dispose();
		this.sndExplosion.dispose();
	}
}
