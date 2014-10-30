package touch.target.draw;

import touch.target.surface.Targeting;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Implementation of the draw method, draws the background and the objects.
 * 
 * @author Rick
 *
 */
public class DrawRunningImpl implements Draw {
	@Override
	public void draw(Canvas canvas, Targeting targeting) {
		// draw based on targeting state
		if (!targeting.isTouchTimingSet)
			canvas.drawColor(Color.BLUE);
		else if (targeting.isTouchedOnTime)
			canvas.drawColor(Color.GREEN);
		else
			canvas.drawColor(Color.RED);

		// draw on the canvas the two rectangle objects.
		canvas.drawRect(targeting.targetRect.rect, targeting.targetObjectPaint);
		canvas.drawRect(targeting.homingRect.rect, targeting.targetHomingPaint);
	}
}