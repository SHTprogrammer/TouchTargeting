package touch.target.physics;

import touch.target.surface.Targeting;

public class UpdatePhysicsRunningImpl implements UpdatePhysics {

	/**
	 * Used to tell the targeting class that the current set of actions can be
	 * reset is over
	 */
	boolean isResetable;

	@Override
	public void updatePhysics(Targeting targeting) {
		// handle user touch
		if (targeting.isTouched && !targeting.isTouchUpdated) {
			targeting.isTouchUpdated = true;
			targeting.isTouched = false;

			// check for touch on time
			if (targeting.homingRect.rect.left > (targeting.targetRect.rect.left - targeting.STROKE_WIDTH)
					&& (targeting.homingRect.rect.left < targeting.targetRect.rect.left
							+ targeting.STROKE_WIDTH)) {
				targeting.isTouchedOnTime = true;
				targeting.isTouchTimingSet = true;

				// reduce the home target for next round
				targeting.targetRect.rect.left += targeting.startTargetPixel;
				targeting.targetRect.rect.top += targeting.startTargetPixel;
				targeting.targetRect.rect.right -= targeting.startTargetPixel;
				targeting.targetRect.rect.bottom -= targeting.startTargetPixel;

				// register the current round to be over
				if (targeting.targetRect.rect.left > targeting.screenWidth / 2) {
					isResetable = true;
				}
			} else {
				// count as missed and end round
				targeting.isTouchedOnTime = false;
				targeting.isTouchTimingSet = true;
				targeting.isRoundOver = true;

				// expand the target to show it was off time
				targeting.targetRect.rect.left -= targeting.startTargetPixel;
				targeting.targetRect.rect.top -= targeting.startTargetPixel;
				targeting.targetRect.rect.right += targeting.startTargetPixel;
				targeting.targetRect.rect.bottom += targeting.startTargetPixel;

				// don't let it set it back further than the first visible step
				if (targeting.targetRect.rect.left <= 0) {
					targeting.targetRect.rect.left = targeting.startTargetPixel;
					targeting.targetRect.rect.top = targeting.startTargetPixel;
					targeting.targetRect.rect.right = targeting.screenWidth - 1
							- targeting.startTargetPixel;
					targeting.targetRect.rect.bottom = targeting.screenHeight
							- targeting.startTargetPixel;
				}
			}
		}

		// reduce square size of homing target
		targeting.homingRect.rect.left++;
		targeting.homingRect.rect.top++;
		targeting.homingRect.rect.right--;
		targeting.homingRect.rect.bottom--;

		// when the left meets the right or the top meets the bottom means the
		// round is over and user touch will be accepted
		if (targeting.homingRect.rect.left >= targeting.homingRect.rect.right
				|| targeting.homingRect.rect.top >= targeting.homingRect.rect.bottom) {
			targeting.isTouched = false;
			targeting.isTouchedOnTime = false;
			targeting.isTouchTimingSet = false;
			targeting.isTouchUpdated = false;
			targeting.isRoundOver = false;
			targeting.homingRect.rect.left = 0;
			targeting.homingRect.rect.right = targeting.screenWidth - 1;
			targeting.homingRect.rect.top = 0;
			targeting.homingRect.rect.bottom = targeting.screenHeight;
		}
	}

	@Override
	public boolean getIsResetable() {
		// TODO Auto-generated method stub
		return isResetable;
	}
}