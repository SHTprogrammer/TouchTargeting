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
	// store the screen size for scaling objects
	public int screenWidth, screenHeight;
	public float STROKE_WIDTH = 10;

	public boolean isTouchedOnTime;
	public boolean isTouched;
	public boolean isRoundOver;
	public boolean isTouchUpdated;
	public boolean isTouchTimingSet;

	public Paint targetHomingPaint = new Paint();
	public Paint targetObjectPaint = new Paint();

	// tell the factory to create version c of UpdatePhysics
	String currentPhysics = "running";
	UpdatePhysics updatePhysics = UpdatePhysicsFactory
			.createUpdatePhysics(currentPhysics);

	// tell the factory to create version a of Draw
	String currentDraw = "running";
	Draw draw = DrawFactory.createDraw(currentDraw);

	// the starting pixel is the scaled point from the origin the homing target
	// starts at
	public int startTargetPixel;

	// target class is the homing target and homing object
	public class Target {
		// Rect will be drawn to the screen
		public Rect rect;

		// constructor creates a new target
		public Target() {
			rect = new Rect();
		}
	}

	// the moving
	public Target homingRect = new Target();
	public Target targetRect = new Target();

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
				isTouchUpdated = false;
				return false;
			}
		}
		return true;
	}

	/**
	 * update the current objects, reset after screen cycle has ended
	 */
	public void updatePhysics() {
		updatePhysics.updatePhysics(this);

		// the moving homing rectangle has minimized completely
		if (updatePhysics.getIsToBeReset()) {
			currentPhysics = "win";
			updatePhysics = UpdatePhysicsFactory
					.createUpdatePhysics(currentPhysics);
			currentDraw = "win";
			draw = DrawFactory.createDraw(currentDraw);
		}
	}

	/**
	 * The homing target just inside the bounds of the screen and the target
	 * bounds are initialized based on a scaled starting point.
	 */
	private void initTargets() {
		homingRect.rect.top = 0;
		homingRect.rect.left = 0;
		homingRect.rect.right = screenWidth - 1;
		homingRect.rect.bottom = screenHeight - 1;

		targetRect.rect.top = startTargetPixel;
		targetRect.rect.left = startTargetPixel;
		targetRect.rect.right = screenWidth - startTargetPixel - 1;
		targetRect.rect.bottom = screenHeight - startTargetPixel - 1;
	}

	/**
	 * The user may have pressed the menu restart button this resets the factory
	 * pattern created classes for physics and draw methods.
	 */
	public void menuRestart() {
		currentPhysics = "running";
		updatePhysics = UpdatePhysicsFactory
				.createUpdatePhysics(currentPhysics);
		currentDraw = "running";
		draw = DrawFactory.createDraw(currentDraw);

		initTargets();

		isTouched = false;
		isTouchedOnTime = false;
		isTouchTimingSet = false;
		isTouchUpdated = false;
		isRoundOver = false;
	}
}