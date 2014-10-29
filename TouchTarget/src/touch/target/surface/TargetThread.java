package touch.target.surface;

import touch.target.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;

public class TargetThread extends Thread {

	Context context;
	private DrawingSurface drawingSurface;
	SurfaceHolder mSurfaceHolder;
	public boolean mRun = false;
	public int mMode;
	public final static int STATE_PAUSE = 0;
	public final static int STATE_RUNNING = 1;
	public Handler mHandler;

	public TargetThread(SurfaceHolder surfaceHolder, Context c,
			Handler handler, DrawingSurface drawSurface) {
		mSurfaceHolder = surfaceHolder;
		mHandler = handler;
		context = c;
		mMode = STATE_PAUSE;
		drawingSurface = drawSurface;
	}

	@Override
	public void run() {
		while (mRun) {
			Canvas c = null;
			try {
				c = mSurfaceHolder.lockCanvas(null);
				if (c != null) {
					synchronized (mSurfaceHolder) {
						if (mMode == STATE_RUNNING)
							drawingSurface.updatePhysics();

						drawingSurface.draw(c);
					}
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}

	public void setRunning(boolean b) {
		synchronized (mSurfaceHolder) {
			mRun = b;
		}
	}

	public void setState(int mode) {
		synchronized (mSurfaceHolder) {
			setState(mode, null);
		}
	}

	public void setState(int mode, CharSequence message) {
		synchronized (mSurfaceHolder) {
			mMode = mode;
			Message msg;
			Bundle bundle;
			switch (mMode) {
			case STATE_RUNNING:
				msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("message", "");
				b.putInt("show", View.INVISIBLE);
				msg.setData(b);
				mHandler.sendMessage(msg);
				break;
			case STATE_PAUSE:
				Resources res = context.getResources();
				CharSequence str = "";
				str = res.getText(R.string.message_text);

				if (message != null) {
					str = message + "\n" + str;
				}

				msg = mHandler.obtainMessage();
				bundle = new Bundle();
				bundle.putString("message", str.toString());
				bundle.putInt("show", View.VISIBLE);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				break;
			}
		}
	}

	public void pause() {
		synchronized (mSurfaceHolder) {
			setState(STATE_PAUSE);
		}
	}
}