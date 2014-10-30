package touch.target;

import touch.target.surface.DrawingSurface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * 
 * The starting class as declared in the Android manifest file. Handle the
 * overall Activity calls from onCreate to onDestroy.
 * 
 * @author Rick
 *
 */
public class MainActivity extends ActionBarActivity {

	/**
	 * Will be used to hold the pixels to be drawn on.
	 */
	public DrawingSurface drawingSurface;

	/**
	 * The on create method allow you start creating the view objects and
	 * referencing them.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get reference to our surface
		drawingSurface = (DrawingSurface) findViewById(R.id.drawing_surface);
		drawingSurface.setTextView((TextView) findViewById(R.id.pause_message));
	}

	/**
	 * Declare the menu you want to use here from the menu folder.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * The method that will be used to handle the options selected from the menu
	 * you created.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.restart:
			drawingSurface.menuRestart();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// start the application out by modifying the view and don't accept user
		// input until the animation ends
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.intro);
		anim.reset();
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// if the animation isn't finished we shouldn't be drawing on it
				drawingSurface.introFinished = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// this animation is longest and touch should be ready when this
				// completes
				drawingSurface.introFinished = true;
			}
		});

		Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.warp_out);
		anim2.reset();

		drawingSurface.clearAnimation();
		drawingSurface.startAnimation(anim);

		drawingSurface.messageTextView.clearAnimation();
		drawingSurface.messageTextView.startAnimation(anim2);
		
		drawingSurface.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		drawingSurface.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		drawingSurface.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		drawingSurface.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		drawingSurface.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		drawingSurface.onDestroy();
	}
}