package touch.target.physics;

import touch.target.surface.Targeting;

public class UpdatePhysicsBImpl implements UpdatePhysics {

	@Override
	public void updatePhysics(Targeting targeting) {
		// // if touch and not evaluated
		// if (targeting.isTouched && !targeting.touchUpdated) {
		// targeting.touchUpdated = true;
		// targeting.isTouched = false;
		//
		// // the left corner must be in the buffer of the target pixel to be
		// // considered a hit
		// if (targeting.left > (targeting.targetPixel - targeting.STROKE_WIDTH)
		// && (targeting.left < (targeting.targetPixel +
		// targeting.STROKE_WIDTH))) {
		// targeting.isTouchedOnTime = true;
		// targeting.isTouchTimingSet = true;
		// targeting.targetPixel += targeting.targetIncrement;
		// } else {
		// // count as missed
		// targeting.isTouchedOnTime = false;
		// targeting.isTouchTimingSet = true;
		// targeting.isRoundOver = true;
		// targeting.targetPixel -= targeting.targetIncrement;
		// }
		//
		// if (targeting.targetPixel <= 0) {
		// // complete with on time touches
		// targeting.targetPixel = targeting.targetIncrement;
		// } else if (targeting.targetPixel > targeting.screenWidth / 2) {
		// // completed with off time touches
		// targeting.targetPixel = targeting.targetIncrement;
		// }
		// }
		//
		// // reduce square size
		// targeting.left++;
		// targeting.top++;
		// targeting.right--;
		// targeting.bottom--;
		//
		// // when the left meets the right or the top meets the bottom means
		// the
		// // round is over
		// if (targeting.left >= targeting.right
		// || targeting.top >= targeting.bottom) {
		// targeting.isTouched = false;
		// targeting.isTouchedOnTime = false;
		// targeting.isTouchTimingSet = false;
		// targeting.touchUpdated = false;
		// targeting.isRoundOver = false;
		// targeting.left = 0;
		// targeting.right = targeting.screenWidth;
		// targeting.top = 0;
		// targeting.bottom = targeting.screenHeight;
		// }
	}

	@Override
	public boolean getIsResetable() {
		// TODO Auto-generated method stub
		return false;
	}
}