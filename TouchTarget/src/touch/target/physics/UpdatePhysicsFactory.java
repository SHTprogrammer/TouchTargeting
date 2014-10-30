package touch.target.physics;

public class UpdatePhysicsFactory {

	UpdatePhysicsFactory() {
	};

	public static UpdatePhysics createUpdatePhysics(String type) {
		if (type.equals("running"))
			return new UpdatePhysicsRunningImpl();
		if (type.equals("win"))
			return new UpdatePhysicsWinImpl();
		else
			return null;
	}
}