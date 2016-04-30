package game;

import java.awt.event.KeyEvent;

import input.Keyboard;

public class Controller {
	
	public static final Controller P1 = new Controller(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D);
	public static final Controller P2 = new Controller(KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L);
	
	private int up;
	private int left;
	private int down;
	private int right;
	
	public Controller(int up, int left, int down, int right) {
		this.up = up;
		this.left = left;
		this.down = down;
		this.right = right;
	}
	
	public boolean up() {
		return Keyboard.isKeyDown(up);
	}
	
	public boolean upOnce() {
		return Keyboard.isKeyDownOnce(up);
	}
	
	public boolean left() {
		return Keyboard.isKeyDown(left);
	}
	
	public boolean leftOnce() {
		return Keyboard.isKeyDownOnce(left);
	}
	
	public boolean down() {
		return Keyboard.isKeyDown(down);
	}
	
	public boolean downOnce() {
		return Keyboard.isKeyDownOnce(down);
	}
	
	public boolean right() {
		return Keyboard.isKeyDown(right);
	}
	
	public boolean rightOnce() {
		return Keyboard.isKeyDownOnce(right);
	}
	
	

}
