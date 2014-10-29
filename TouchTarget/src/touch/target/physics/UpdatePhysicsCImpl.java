package touch.target.physics;

import touch.target.surface.Targeting;

public class UpdatePhysicsCImpl implements UpdatePhysics {

	/**
	 * Used to tell the targeting class that the current set of actions can be
	 * reset is over
	 */
	boolean isResetable;

	@Override
	public void updatePhysics(Targeting targeting) {
		// handle user touch
		if (targeting.isTouched && !targeting.touchUpdated) {
			targeting.touchUpdated = true;
			targeting.isTouched = false;

			// check for touch on time
			if (targeting.homingTarget.rect.left > (targeting.homeTarget.rect.left - targeting.STROKE_WIDTH)
					&& (targeting.homingTarget.rect.left < targeting.homeTarget.rect.left
							+ targeting.STROKE_WIDTH)) {
				targeting.isTouchedOnTime = true;
				targeting.isTouchTimingSet = true;

				// reduce the home target for next round
				targeting.homeTarget.rect.left += targeting.startTargetPixel;
				targeting.homeTarget.rect.top += targeting.startTargetPixel;
				targeting.homeTarget.rect.right -= targeting.startTargetPixel;
				targeting.homeTarget.rect.bottom -= targeting.startTargetPixel;

				// register the current round to be over
				if (targeting.homeTarget.rect.left > targeting.screenWidth / 2) {
					isResetable = true;
				}
			} else {
				// count as missed and end round
				targeting.isTouchedOnTime = false;
				targeting.isTouchTimingSet = true;
				targeting.isRoundOver = true;

				// expand the target to show it was off time
				targeting.homeTarget.rect.left -= targeting.startTargetPixel;
				targeting.homeTarget.rect.top -= targeting.startTargetPixel;
				targeting.homeTarget.rect.right += targeting.startTargetPixel;
				targeting.homeTarget.rect.bottom += targeting.startTargetPixel;

				// don't let it set it back further than the first visible step
				if (targeting.homeTarget.rect.left <= 0) {
					targeting.homeTarget.rect.left = targeting.startTargetPixel;
					targeting.homeTarget.rect.top = targeting.startTargetPixel;
					targeting.homeTarget.rect.right = targeting.screenWidth - 1
							- targeting.startTargetPixel;
					targeting.homeTarget.rect.bottom = targeting.screenHeight
							- targeting.startTargetPixel;
				}
			}
		}

		// reduce square size of homing target
		targeting.homingTarget.rect.left++;
		targeting.homingTarget.rect.top++;
		targeting.homingTarget.rect.right--;
		targeting.homingTarget.rect.bottom--;

		// when the left meets the right or the top meets the bottom means the
		// round is over and user touch will be accepted
		if (targeting.homingTarget.rect.left >= targeting.homingTarget.rect.right
				|| targeting.homingTarget.rect.top >= targeting.homingTarget.rect.bottom) {
			targeting.isTouched = false;
			targeting.isTouchedOnTime = false;
			targeting.isTouchTimingSet = false;
			targeting.touchUpdated = false;
			targeting.isRoundOver = false;
			targeting.homingTarget.rect.left = 0;
			targeting.homingTarget.rect.right = targeting.screenWidth - 1;
			targeting.homingTarget.rect.top = 0;
			targeting.homingTarget.rect.bottom = targeting.screenHeight;
		}
	}

	@Override
	public boolean getIsResetable() {
		// TODO Auto-generated method stub
		return isResetable;
	}
}