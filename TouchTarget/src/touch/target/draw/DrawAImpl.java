package touch.target.draw;

import touch.target.surface.Targeting;
import android.graphics.Canvas;
import android.graphics.Color;

public class DrawAImpl implements Draw {
	@Override
	public void draw(Canvas canvas, Targeting targeting) {
		// TODO Auto-generated method stub
		if (!targeting.isTouchTimingSet)
			canvas.drawColor(Color.BLUE);
		else if (targeting.isTouchedOnTime)
			canvas.drawColor(Color.GREEN);
		else
			canvas.drawColor(Color.RED);

		canvas.drawRect(targeting.targetRect.rect, targeting.targetObjectPaint);
		canvas.drawRect(targeting.homingRect.rect,
				targeting.targetHomingPaint);
	}
}