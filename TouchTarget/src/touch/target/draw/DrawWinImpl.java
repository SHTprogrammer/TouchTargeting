package touch.target.draw;

import touch.target.surface.Targeting;
import android.graphics.Canvas;
import android.graphics.Color;

public class DrawWinImpl implements Draw {

	@Override
	public void draw(Canvas canvas, Targeting targeting) {
		canvas.drawColor(Color.YELLOW);
	}
}