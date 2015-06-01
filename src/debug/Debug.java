package debug;

public class Debug {
	public static boolean isDebug = false;

	public static void log(Object obj) {
		if (isDebug) {
			System.out.println(obj.toString());
		}
	}
}
