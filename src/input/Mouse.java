package input;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * The Mouse is a class with static methods to determine mouse behaviour.
 * @author Hamish Rae-Hodgson.
 */
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {
	public static final int LEFT_BUTTON = MouseEvent.BUTTON1;
	public static final int MIDDLE_BUTTON = MouseEvent.BUTTON2;
	public static final int RIGHT_BUTTON = MouseEvent.BUTTON3;
	
	public static Cursor CURSOR_INVISIBLE = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TRANSLUCENT), new Point(0,0), "inviscursor");
	public static Cursor CURSOR_CROSSHAIR = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	public static Cursor CURSOR_DEFAULT = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	
	private static float MOUSE_SENSITIVITY = 0.5f;
	
	/** Singleton instance of Mouse */
	private static final Mouse instance = new Mouse();
	
	/** The component that this Mouse is listening to events on */
	private static Component component = null;
	
	/** This Robot is used for resetting the mouse to the center of the screen */
	private static Robot robot;
	static { try { robot = new Robot(); } catch (AWTException e) { e.printStackTrace(); } }
	
	private static final Set<Integer> pressedButtons = new HashSet<Integer>();
	
	private static float x = 0;
	private static float y = 0;
	private static float dx = 0;
	private static float dy = 0;
	private static float dwheel = 0;

	/** If this is true the mouse should be considered as grabbed. */
	private static boolean isGrabbed = false;
	
	/** private singleton constructor */
	private Mouse() { }
	
	/**
	 * Register the Mouse with the component so we can listen to mouse behaviour.
	 * @param component The component to attach the Mouse to.
	 */
	public static void register(Component component) {
		component.addMouseListener(instance);
		component.addMouseWheelListener(instance);
		component.addMouseMotionListener(instance);
		Mouse.component = component;
	}
	
	/** 
	 * Unregisters the Mouse from the specified component.
	 * @param component The component to stop listening to.
	 */
	public static void unregister(Component component) {
		component.removeMouseListener(instance);
		component.removeMouseWheelListener(instance);
		component.removeMouseMotionListener(instance);
		if(Mouse.component == component) {
			Mouse.component = null;
		}
	}
	
	public static void setCursor(Cursor cursor) {
		if(component != null)
			component.setCursor(cursor);
	}
	
	/**
	 * Return true if the specified button is currently pressed.
	 * @param button The button to check for.
	 * @return The state of the button.
	 */
	public static boolean isButtonDown(int button) {
		return pressedButtons.contains(button);
	}
	
	/**
	 * Get the current X position of the Mouse.
	 * @return The x position of the mouse.
	 */
	public static float getX() {
		return x;
	}
	
	/**
	 * Get the current Y position of the Mouse.
	 * @return The y position of the mouse.
	 */
	public static float getY() {
		return y;
	}
	
	/**
	 * Movement on the x axis since last time getDX() was called.
	 * @return The movement on the x axis.
	 */
	public static float getDX() {
		float out = dx;
		dx = 0;
		return out;
	}
	
	/**
	 * Movement on the y axis since last time getDY() was called.
	 * @return The movement on the y axis.
	 */
	public static float getDY() {
		float out = dy;
		dy = 0;
		return out;
	}
	
	/** returns the direction of wheel scroll.
	 * @return 0 when no scroll pending. -1 when down scroll, 1 when up scroll.
	 */
	public static float getDWheel() {
		float out = dwheel;
		dwheel = 0;
		return out;
	}
	
	/**
	 * Check if the mouse is grabbed.
	 * @return Returns true if the mouse is grabbed.
	 */
	public static boolean isGrabbed() {
		return isGrabbed;
	}
	
	/**
	 * Grab the Mouse causing it to remain locked in the center of the frame.
	 * @param grabbed boolean state of grabbed. true to grab, false to not grab.
	 */
	public static void setGrabbed(boolean grabbed) {
		isGrabbed = grabbed;
		if(isGrabbed)
			centerMouseOverComponent();
	}
	
	/** Moves the mouse to the center of the component that this Mouse is listening to */
	public static void centerMouseOverComponent() {
		if(component == null || robot == null) return;
		robot.mouseMove(component.getLocationOnScreen().x + component.getWidth()/2,
						component.getLocationOnScreen().y + component.getHeight()/2);
	}
	
	///////////////
	// Listeners //
	///////////////

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		dwheel = e.getWheelRotation() < 0 ? e.getWheelRotation() : e.getWheelRotation();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		
		// If we were unable to create a Robot dx/dy stay 0 and we don't grab Mouse
		if(robot != null && isGrabbed ) {
			float ndx =  (x - component.getWidth()/2) * MOUSE_SENSITIVITY;
			float ndy =  (component.getHeight()/2 - y) * MOUSE_SENSITIVITY; //swapped because y=1 is up.
			dx = ndx == 0 ? dx : ndx; //robot invoked movement causes ndx to equal 0, so ignore it.
			dy = ndy == 0 ? dy : ndy;
			centerMouseOverComponent();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressedButtons.add(e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressedButtons.remove(e.getButton());
	}
	
	public void mouseDragged(MouseEvent e) { }
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }

}
