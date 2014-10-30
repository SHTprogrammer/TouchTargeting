package touch.target.draw;

import touch.target.surface.Targeting;
import android.graphics.Canvas;

/**
 * The only thing we need our draw class to do for sure is draw to the canvas.
 * 
 * @author Rick
 *
 */
public interface Draw {
	public void draw(Canvas canvas, Targeting targeting);
}