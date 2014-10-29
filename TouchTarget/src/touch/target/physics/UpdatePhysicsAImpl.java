package touch.target.physics;

import touch.target.surface.Targeting;

public class UpdatePhysicsAImpl implements UpdatePhysics {

	@Override
	public void updatePhysics(Targeting targeting) {
		// called if application and thread are running
		if (targeting.isTouched && !targeting.touchUpdated) {
			targeting.touchUpdated = true;

			if (targeting.homingTarget.rect.left > (targeting.homeTarget.rect.left - targeting.STROKE_WIDTH)
					&& (targeting.homingTarget.rect.left < targeting.homeTarget.rect.left
							+ targeting.STROKE_WIDTH)) {
				targeting.isTouchedOnTime = true;
				targeting.isRoundOver = true;
			} else {
				targeting.isTouchedOnTime = false;
				targeting.isRoundOver = true;
			}
		}

		targeting.homingTarget.rect.left++;
		targeting.homingTarget.rect.top++;
		targeting.homingTarget.rect.right--;
		targeting.homingTarget.rect.bottom--;

		if (targeting.homingTarget.rect.left >= targeting.homingTarget.rect.right
				|| targeting.homingTarget.rect.top >= targeting.homingTarget.rect.bottom) {
			targeting.isTouchedOnTime = false;
			targeting.isTouched = false;
			targeting.touchUpdated = false;
			targeting.isRoundOver = false;

			targeting.homingTarget.rect.top = 0;
			targeting.homingTarget.rect.left = 0;
			
			//keep it visible so minus one (stroke_width)
			targeting.homingTarget.rect.right = targeting.screenWidth - 1;
			targeting.homingTarget.rect.bottom = targeting.screenHeight;
		}
	}

	@Override
	public boolean getIsResetable() {
		// TODO Auto-generated method stub
		return false;
	}
}