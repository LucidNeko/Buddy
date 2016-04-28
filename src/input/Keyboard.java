package input;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * The Keyboard is a class with static methods to determine which keys are pressed.
 * @author Hamish Rae-Hodgson.
 */
public class Keyboard extends KeyAdapter {
	/** Singleton instance of the Keyboard */
	private static final Keyboard instance = new Keyboard();
	
	/** Set of all currently pressed keys */
	private static final Set<Integer> pressedKeys = new HashSet<Integer>();
	
	/** Set used for one time keypresses */
	private static final Set<Integer> pressedKeysOnce = new HashSet<Integer>();
	
	/**
	 * Private constructor to guarantee singleton.
	 */
	private Keyboard() { }
	
	/**
	 * Register the Keyboard with the component to listen for keypresses.
	 * @param component The component to listen to.
	 */
	public static void register(Component component) {
		component.addKeyListener(instance);
	}
	
	/**
	 * Unregisters the Keyboard from the specified component.
	 * @param component The component to stop listening to.
	 */
	public static void unregister(Component component) {
		component.removeKeyListener(instance);
	}
	
	/**
	 * Returns true if the specified key is pressed.
	 * @param keycode The key to check for. KeyEvent.VK_{?}
	 * @return True if the key is down, otherwise false.
	 */
	public static boolean isKeyDown(int keycode) {
		return pressedKeys.contains(keycode);
	}
	
	/**
	 * Returns true only once between a given key being pressed and it being released.
	 * @param keycode The key to check for. KeyEvent.VK_{?}
	 * @return True if the key is pressed AND a call to this method has not yet happened for this keypress.
	 */
	public static boolean isKeyDownOnce(int keycode) {
		return pressedKeysOnce.remove(keycode);
	}

	////////
	// Due to the way the OS handles keypresses. Holding down a key actually results in
	// keyPressed then keyReleased being called repeatedly throughout the duration of the key press..
	// This is why we must have the conditional on pressedKeys containing the keycode.
	////////
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		if(!pressedKeys.contains(keycode)) {
			pressedKeys.add(keycode);
			pressedKeysOnce.add(keycode);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keycode = e.getKeyCode();
		if(pressedKeys.contains(keycode)) {
			pressedKeys.remove(keycode);
			pressedKeysOnce.remove(keycode);
		}
	}

}
