package touch.target.draw;

import touch.target.surface.Targeting;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 
 * The win implementation is simple a yellow screen, what will you do with it?
 * 
 * @author Rick
 *
 */
public class DrawWinImpl implements Draw {

	/**
	 * the draw class must be called if its implementing Draw
	 */
	@Override
	public void draw(Canvas canvas, Targeting targeting) {
		canvas.drawColor(Color.YELLOW);
	}
}