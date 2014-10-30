package touch.target.physics;

import touch.target.surface.Targeting;

/**
 * 
 * UpdatePhysics handles the state of objects and if the application can be
 * reset.
 * 
 * @author Rick
 *
 */
public interface UpdatePhysics {
	public void updatePhysics(Targeting targeting);

	public boolean getIsToBeReset();
}