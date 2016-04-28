package graphics;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class CanvasRenderer extends JComponent {
	private static final long serialVersionUID = -4746951092603091692L;

	private PixelImage canvas;
	
	public CanvasRenderer(PixelImage canvas) {
		this.canvas = canvas;
		this.setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));
		this.setFocusable(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		synchronized(canvas) {
			g.drawImage(canvas.asBufferedImage(), 0, 0, null);
		}
		
	}
	
}
