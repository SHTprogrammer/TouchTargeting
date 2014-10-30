package touch.target.physics;

/**
 * 
 * The update physics factory will return to the application the physics for the
 * game to be running or in the win state.
 * 
 * @author Rick
 *
 */
public class UpdatePhysicsFactory {

	public static UpdatePhysics createUpdatePhysics(String type) {
		if (type.equals("running"))
			return new UpdatePhysicsRunningImpl();
		if (type.equals("end"))
			return new UpdatePhysicsEndImpl();
		else
			return null;
	}
}