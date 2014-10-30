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

/**
 * 
 * The targeting class is used to create our two target objects the homing
 * target and target objects.
 * 
 * @author Rick
 *
 */
public class Targeting {
	/**
	 * store the screen size for scaling objects
	 */
	public int screenWidth, screenHeight;

	/**
	 * Allows for developer to change the width of the target.
	 */
	public float STROKE_WIDTH = 10;

	/**
	 * Flags for the logic.
	 */
	public boolean isTouchedOnTime;
	public boolean isTouched;
	public boolean isRoundOver;
	public boolean isTouchUpdated;
	public boolean isTouchTimingSet;

	/**
	 * Our target paint objects.
	 */
	public Paint targetHomingPaint = new Paint();
	public Paint targetObjectPaint = new Paint();

	/**
	 * Used to contain the current physics class object.
	 */
	String currentPhysics = "running";

	/**
	 * Our update physics object is used to create the physics at the time that
	 * we need them.
	 */
	UpdatePhysics updatePhysics = UpdatePhysicsFactory
			.createUpdatePhysics(currentPhysics);

	/**
	 * Contains the title to the current Draw class.
	 */
	String currentDraw = "running";

	/**
	 * Our draw object is used to create the draw class at the time that we need
	 * them.
	 */
	Draw draw = DrawFactory.createDraw(currentDraw);

	/**
	 * The starting pixel is the scaled point from the origin to the target
	 * object.
	 */
	public int startTargetPixel;

	/**
	 * 
	 * Class that holds the rect object that contains the bounds of the target.
	 * 
	 * @author Rick
	 *
	 */
	public class MyRect {
		/**
		 * will be used to determine the draw pixels of the object.
		 */
		public Rect rect;

		/**
		 * Create a new Rect when you create a new Target.
		 */
		public MyRect() {
			rect = new Rect();
		}
	}

	/**
	 * The homing object that will pass over the target.
	 */
	public MyRect homingRect = new MyRect();
	public MyRect targetRect = new MyRect();

	/**
	 * When creating a new Targeting class we crete the Paint objects we will
	 * need.
	 */
	public Targeting() {
		targetHomingPaint.setColor(Color.WHITE);
		targetHomingPaint.setStyle(Style.STROKE);
		targetHomingPaint.setStrokeWidth(1);

		targetObjectPaint.setColor(Color.BLACK);
		targetObjectPaint.setStyle(Style.STROKE);
		targetObjectPaint.setStrokeWidth(STROKE_WIDTH);
	}

	/**
	 * 
	 * Calling draw will hand the work off to the Draw class that is implemented
	 * at that time.
	 * 
	 * @param canvas
	 *            our drawing surface's canvas.
	 */
	public void draw(Canvas canvas) {
		draw.draw(canvas, this);
	}

	/**
	 * 
	 * On start of the application we can set the screen height and widtht.
	 * 
	 * @param holder
	 *            our surface holder object.
	 * @param height
	 *            height of our screen in pixels.
	 * @param width
	 *            width of our screen in pixels.
	 */
	public void surfaceChanged(SurfaceHolder holder, int height, int width) {
		// called when the surface is created for the thread
		screenHeight = height;
		screenWidth = width;

		// by scaling the startTargetPixel from the screen width we can make
		// sure that
		// the start target pixel is in a similar location on different devices.
		startTargetPixel = screenWidth / 20;

		// place targets
		initMyRects();
	}

	/**
	 * If the application is in a state to accept touch events, handle them
	 * here. Returning false means until the user presses down the handling is
	 * over, returning true means the touch events will keep being fed to here.
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
	 * Update the current objects, reset after screen cycle has ended.
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
	private void initMyRects() {
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
		// reset our factory methods
		currentPhysics = "running";
		updatePhysics = UpdatePhysicsFactory
				.createUpdatePhysics(currentPhysics);
		currentDraw = "running";
		draw = DrawFactory.createDraw(currentDraw);

		// reset our MyRect objects
		initMyRects();

		//reset our flags for the targeting class
		isTouched = false;
		isTouchedOnTime = false;
		isTouchTimingSet = false;
		isTouchUpdated = false;
		isRoundOver = false;
	}
}