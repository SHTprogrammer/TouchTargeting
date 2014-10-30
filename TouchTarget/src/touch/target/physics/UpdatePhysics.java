package touch.target.physics;

import touch.target.surface.Targeting;

public interface UpdatePhysics {
	public void updatePhysics(Targeting targeting);
	public boolean getIsResetable();
}
