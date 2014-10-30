package touch.target.draw;

/**
 * 
 * The factory that creates our draw methods, we could be in the running state or the end
 * 
 * @author Rick
 *
 */
public class DrawFactory {
	public static Draw createDraw(String currentDraw) {
		if (currentDraw.equals("running"))
			return new DrawRunningImpl();
		else if (currentDraw.equals("end"))
			return new DrawWinImpl();
		else
			return null;
	}
}
