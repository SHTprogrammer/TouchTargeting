package touch.target.draw;

public class DrawFactory {
	public static Draw createDraw(String currentDraw) {
		if (currentDraw.equals("a"))
			return new DrawAImpl();
		else if (currentDraw.equals("win"))
			return new DrawWinImpl();
		else
			return null;
	}
}
