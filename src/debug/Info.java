package debug;

public class Info {
	public static boolean isInfo = false;

	public static void log(Object obj) {
		if (isInfo) {
			System.out.println(obj.toString());
		}
	}
	public static void logNoLn(Object obj) {
		if (isInfo) {
			System.out.print(obj.toString());
		}
	}
}
