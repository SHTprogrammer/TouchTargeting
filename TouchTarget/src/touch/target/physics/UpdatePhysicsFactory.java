package touch.target.physics;

public class UpdatePhysicsFactory {
	UpdatePhysicsFactory() {
	};

	public static UpdatePhysics createUpdatePhysics(String type) {
		if (type.equals("a"))
			return new UpdatePhysicsAImpl();
		if (type.equals("b"))
			return new UpdatePhysicsBImpl();
		if (type.equals("c"))
			return new UpdatePhysicsCImpl();
		if (type.equals("win"))
			return new UpdatePhysicsWinImpl();
		else
			return null;
	}
}
