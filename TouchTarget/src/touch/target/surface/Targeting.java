package touch.target.surface;

import touch.target.draw.Draw;
import touch.target.draw.DrawFactory;
import touch.target.physics.UpdatePhysics;
import touch.target.physics.UpdatePhysicsFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class Targeting {

	public int screenWidth, screenHeight;
	public Paint targetHomingPaint = new Paint();
	public Paint targetObjectPaint = new Paint();
	public float STROKE_WIDTH = 10;
	public boolean isTouchedOnTime;
	public boolean isTouched;
	public boolean isRoundOver;
	public boolean touchUpdated;
	public boolean isTouchTimingSet;

	String currentPhysics = "c";
	UpdatePhysics updatePhysics = UpdatePhysicsFactory
			.createUpdatePhysics(currentPhysics);

	String currentDraw = "a";
	Draw draw = DrawFactory.createDraw(currentDraw);

	public int startTargetPixel;

	// current target class is the homing target and home target
	public class Target {
		public Rect rect;

		public Target() {
			rect = new Rect();
		}
	}

	public Target homingTarget = new Target();
	public Target homeTarget = new Target();

	public Targeting() {
		targetHomingPaint.setColor(Color.WHITE);
		targetHomingPaint.setStyle(Style.STROKE);
		targetHomingPaint.setStrokeWidth(1);

		targetObjectPaint.setColor(Color.BLACK);
		targetObjectPaint.setStyle(Style.STROKE);
		targetObjectPaint.setStrokeWidth(STROKE_WIDTH);
	}

	public void draw(Canvas canvas) {
		draw.draw(canvas, this);
	}

	public void surfaceChanged(SurfaceHolder holder, int height, int width) {
		// called when the surface is created for the thread
		screenHeight = height;
		screenWidth = width;

		// set the initial target location and increment amount
		startTargetPixel = screenWidth / 20;

		// place targets
		initTargets();
	}

	/**
	 * Perform click needs to be implemented
	 */
	public boolean onTouch(MotionEvent event) {
		// touch tracks the life cycle of the touch event
		if (!isRoundOver && !isTouched) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// touch event has happened
				isTouched = true;
				touchUpdated = false;
				return false;
			}
		}
		return true;
	}

	public void updatePhysics() {
		updatePhysics.updatePhysics(this);

		if (updatePhysics.getIsResetable()) {
			currentPhysics = "win";
			updatePhysics = UpdatePhysicsFactory
					.createUpdatePhysics(currentPhysics);
			currentDraw = "win";
			draw = DrawFactory.createDraw(currentDraw);
		}
	}

private void initTargets() {
	homingTarget.rect.top = 0;
	homingTarget.rect.left = 0;
	homingTarget.rect.right = screenWidth - 1;
	homingTarget.rect.bottom = screenHeight - 1;

	homeTarget.rect.top = startTargetPixel;
	homeTarget.rect.left = startTargetPixel;
	homeTarget.rect.right = screenWidth - startTargetPixel - 1;
	homeTarget.rect.bottom = screenHeight - startTargetPixel - 1;
}

	public void menuRestart() {
		currentPhysics = "c";
		updatePhysics = UpdatePhysicsFactory
				.createUpdatePhysics(currentPhysics);
		currentDraw = "a";
		draw = DrawFactory.createDraw(currentDraw);

		initTargets();

		isTouched = false;
		isTouchedOnTime = false;
		isTouchTimingSet = false;
		touchUpdated = false;
		isRoundOver = false;
	}
}