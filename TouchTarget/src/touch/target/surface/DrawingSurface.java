package touch.target.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 * 
 * The drawing surface class will be used to hold the thread and handler for
 * message to the UI.
 * 
 * @author Rick
 *
 */
public class DrawingSurface extends SurfaceView implements
		SurfaceHolder.Callback {

	/**
	 * The context object allows access to the resources needed and also contains
	 * information about the application.
	 */
	Context context;

	/**
	 * The thread that runs the cycle of run and update physics during the
	 * applications lifetime.
	 */
	public TargetThread targetThread;

	/**
	 * The targeting class holds most of the application logic and determines
	 * what logic to use.
	 */
	Targeting targeting = new Targeting();

	/**
	 * Sends message to the UI via the thread
	 */
	Handler myHandler;

	/**
	 * Set the message to the UI here.
	 */
	public TextView messageTextView;

	/**
	 * Some logic like handling user touch has to wait until the intro animation
	 * is finished.
	 */
	public boolean introFinished;

	/**
	 * As the application resumes it may need to recreate our thread.
	 */
	boolean recreateThread;

	/**
	 * A class to handle setting the message and them showing the message.
	 * 
	 */
	class IncomingHandlerCallback implements Handler.Callback {
		/**
		 * The Message object can contain more than one value.
		 */
		@Override
		public boolean handleMessage(Message m) {
			// handle message code
			messageTextView.setVisibility(m.getData().getInt("show"));
			messageTextView.setText(m.getData().getString("message"));
			return true;
		}
	}

	/**
	 * 
	 * If we are creating our surface by calling the setContentView in the
	 * MainActivity then you must have a constructor in this class that accepts
	 * two parameters.
	 * 
	 * @param con
	 *            The application Context.
	 * @param attrs
	 *            XML defined attributes can be sent though here.
	 */
	public DrawingSurface(Context con, AttributeSet attrs) {
		super(con, attrs);
		context = con;
		// register the call back interface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// prepare the thread and its message handler (handlers can also execute
		// code if needed)
		myHandler = new Handler(new IncomingHandlerCallback());
		targetThread = new TargetThread(getHolder(), con, myHandler, this);
	}

	/**
	 * Our draw class uses a canvas to draw on and we pass the work to our
	 * targeting class.
	 */
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		targeting.draw(canvas);
	}

	/**
	 * When the surface is created we should have a new thread from our class
	 * constructor but if it was running and terminated then need to recreate
	 * it.
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (targetThread.getState() == Thread.State.TERMINATED) {
			targetThread = new TargetThread(holder, context, myHandler, this);
			targetThread.start();
			targetThread.setRunning(true);
		} else if (targetThread.getState() == Thread.State.NEW) {
			targetThread.start();
			targetThread.setRunning(true);
		}
	}

	/**
	 * Screen dimensions are set at this point and we can record them in the
	 * targeting class.
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		targeting.surfaceChanged(holder, height, width);
	}

	/**
	 * Surface is destroyed and we can let the thread run out its execution
	 * path.
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		targetThread.setRunning(false);
		while (retry) {
			try {
				targetThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Actual physics are encapsulated in the targeting class.
	 */
	public void updatePhysics() {
		targeting.updatePhysics();
	}

	/**
	 * Allow the phone to execute accessibility methods. You should should make
	 * sure the view objects in the UI have a concise but meaningful content
	 * description in the layout XML.
	 */
	@Override
	public boolean performClick() {
		super.performClick();
		return true;
	}

	/**
	 * The touch handler for the surface.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (targetThread.mSurfaceHolder) {
			// if restarting the thread may not be valid, surface created will
			// not be called to do this for us.
			if (recreateThread) {
				recreateThread = false;
				if (targetThread.getState() == Thread.State.TERMINATED) {
					targetThread = new TargetThread(getHolder(), context,
							myHandler, this);
					targetThread.start();
					targetThread.setRunning(true);
				} else if (targetThread.getState() == Thread.State.NEW) {
					targetThread.start();
					targetThread.setRunning(true);
				}
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				performClick();
			}

			/**
			 * If the intro is finished we can perform actions on the touch
			 * events.
			 */
			if (introFinished)
				switch (targetThread.mMode) {
				case TargetThread.STATE_PAUSE:
					targetThread.setState(TargetThread.STATE_RUNNING);
					break;
				case TargetThread.STATE_RUNNING:
					return targeting.onTouch(event);
				}
			return super.onTouchEvent(event);
		}
	}

	/**
	 * The TextView object here will hold the messages to the user.
	 * 
	 * @param textView
	 *            Holds messages from the handler.
	 */
	public void setTextView(TextView textView) {
		messageTextView = textView;
	}

	/**
	 * When the user presses restart in the menu options we handle it in
	 * targeting.
	 */
	public void menuRestart() {
		// restart based on menu select by user
		targeting.menuRestart();
	}

	public void onStart() {
	}

	public void onRestart() {

	}

	/**
	 * We need to set the flag to recreate our thread here, we want the method
	 * to be lightweight so we wait to create it on touch when animation is
	 * needed.
	 */
	public void onResume() {
		if (targetThread.getState() == Thread.State.TERMINATED) {
			recreateThread = true;
		}
	}

	/**
	 * When we pause the thread we will set a flag and send a message to the
	 * user.
	 */
	public void onPause() {
		if (targetThread != null) {
			targetThread.pause();
			targetThread.setRunning(false);
		}
	}

	public void onStop() {

	}

	public void onDestroy() {
	}
}