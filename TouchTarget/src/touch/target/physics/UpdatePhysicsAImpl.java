package touch.target.physics;

import touch.target.surface.Targeting;

public class UpdatePhysicsAImpl implements UpdatePhysics {

	@Override
	public void updatePhysics(Targeting targeting) {
		// called if application and thread are running
		if (targeting.isTouched && !targeting.isTouchUpdated) {
			targeting.isTouchUpdated = true;

			if (targeting.homingRect.rect.left > (targeting.targetRect.rect.left - targeting.STROKE_WIDTH)
					&& (targeting.homingRect.rect.left < targeting.targetRect.rect.left
							+ targeting.STROKE_WIDTH)) {
				targeting.isTouchedOnTime = true;
				targeting.isRoundOver = true;
			} else {
				targeting.isTouchedOnTime = false;
				targeting.isRoundOver = true;
			}
		}

		targeting.homingRect.rect.left++;
		targeting.homingRect.rect.top++;
		targeting.homingRect.rect.right--;
		targeting.homingRect.rect.bottom--;

		if (targeting.homingRect.rect.left >= targeting.homingRect.rect.right
				|| targeting.homingRect.rect.top >= targeting.homingRect.rect.bottom) {
			targeting.isTouchedOnTime = false;
			targeting.isTouched = false;
			targeting.isTouchUpdated = false;
			targeting.isRoundOver = false;

			targeting.homingRect.rect.top = 0;
			targeting.homingRect.rect.left = 0;
			
			//keep it visible so minus one (stroke_width)
			targeting.homingRect.rect.right = targeting.screenWidth - 1;
			targeting.homingRect.rect.bottom = targeting.screenHeight;
		}
	}

	@Override
	public boolean getIsResetable() {
		// TODO Auto-generated method stub
		return false;
	}
}