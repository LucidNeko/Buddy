package core;

import java.util.concurrent.locks.LockSupport;

import util.Log;

/**
 * The GameLoop class provides abstract methods for you to implement and <br>
 * then calls these methods at regular intervals
 * @author Hamish Rae-Hodgson
 *
 */
public abstract class GameLoop extends Thread {
	private boolean running = true;
	private boolean runningSlow = false;
	
	//standard time
	//nanos
	private long timeGameStart;
	private long timeLastFrame;
	//seconds
	private float timeElapsed;
	private float deltaTime;
	
	//fixed time
	private float fixedDeltaTime;;
	private float fixedTime; //elapsed time in seconds
	
	private long syncRate;
	
	/**
	 * Creates a new GameLoop with the given parameters
	 * @param fps Desired frames per second.
	 * @param fixedUPS fixedUpdate() calls per second.
	 */
	public GameLoop(int fps, int fixedUPS) {
		syncRate = (long) ((1D/fps)*1000000000); //fps in nanoseconds.
		fixedDeltaTime = 1f/fixedUPS;// fixed updates per seconds in seconds.
	}
	
	/**
	 * Returns true if the game is running slow.
	 * @return True if no time is spare after an update loop.
	 */
	public boolean isRunningSlow() {
		return runningSlow;
	}
	
	@Override
	public void run() {
		setupTime();
		while(running) {
			updateTime();
			while(fixedTime < timeElapsed) {
				fixedTick(fixedDeltaTime);
				fixedTime += fixedDeltaTime;
			}
			tick(deltaTime);
			render();
			sleepforabit(syncRate-(System.nanoTime()-timeLastFrame)); //syncRate - (time taken)
		}
	}
	
	/**
	 * Sets up all the time fields.
	 */
	private void setupTime() {
		timeGameStart = System.nanoTime();
		timeLastFrame = timeGameStart;
		timeElapsed = 0;
		deltaTime = 0;
		fixedTime = 0;
	}
	
	/**
	 * Updates/Advances all the time fields.
	 */
	private void updateTime() {
		long now = System.nanoTime();
		timeElapsed = (now-timeGameStart)*0.000000001f;
		deltaTime = (now-timeLastFrame)*0.000000001f;
		timeLastFrame = now;
	}
	
	/**
	 * Called approximately every 1/fps seconds.
	 * @param delta The time since the last call to tick() in seconds.
	 */
	protected abstract void tick(float delta);
	
	/**
	 * Called exactly fixedUPS times a second. delta is always exactly 1/fixedUPS<br>
	 * delta critical work should go here - such as physics.
	 * @param delta The step to advance by.
	 */
	protected abstract void fixedTick(float delta);
	
	/**
	 * You should do all your rendering from here.
	 */
	protected abstract void render();
	
	/** 
	 * If nanos is greater than 0 sleeps the thread for the length of nanos.
	 * @param nanos Desired sleep duration in nanoseconds.
	 */
	private void sleepforabit(long nanos) {
		if(nanos > 0)  {
			runningSlow = false;			
			LockSupport.parkNanos(nanos);
		} else {
			runningSlow = true;
		}
	}
	
}
