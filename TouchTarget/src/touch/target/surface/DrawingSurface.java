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

public class DrawingSurface extends SurfaceView implements
		SurfaceHolder.Callback {

	public TargetThread targetThread;
	Targeting targeting = new Targeting();
	Context context;
	public TextView messageTextView;
	Handler myHandler;
	public boolean introFinished;
	boolean recreateThread;

	class IncomingHandlerCallback implements Handler.Callback {
		@Override
		public boolean handleMessage(Message m) {
			// handle message code
			messageTextView.setVisibility(m.getData().getInt("show"));
			messageTextView.setText(m.getData().getString("message"));
			return true;
		}
	}

	public DrawingSurface(Context con, AttributeSet attrs) {
		super(con, attrs);
		context = con;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		myHandler = new Handler(new IncomingHandlerCallback());
		targetThread = new TargetThread(getHolder(), con, myHandler, this);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		targeting.draw(canvas);
	}

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

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		targeting.surfaceChanged(holder, height, width);
	}

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

	public void updatePhysics() {
		targeting.updatePhysics();
	}

	@Override
	public boolean performClick() {
		super.performClick();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (targetThread.mSurfaceHolder) {
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

	public void setTextView(TextView textView) {
		messageTextView = textView;
	}

	public void menuRestart() {
		// restart based on menu select by user
		targeting.menuRestart();
	}

	public void onStart() {
	}

	public void onRestart() {

	}

	public void onResume() {
		if (targetThread.getState() == Thread.State.TERMINATED) {
			recreateThread = true;
		}
	}

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