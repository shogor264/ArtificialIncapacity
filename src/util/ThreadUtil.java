package util;

import java.util.HashMap;
import java.util.Map;

public class ThreadUtil {
	private static final Map<String, Long> beforeExeMiliTimeMap = new HashMap<>();
	/**
	 * Wikipediaのアクセス過多はNGなので、waitを設ける
	 */
	private static final long INTERVAL_MILI_TIME = (long) (0.5 * 1000);

	/**
	 * アクセス過多にならないようwaitを設ける
	 * @name 識別名（クラス名とか）
	 */
	public static void waitExe(String name) {
		long nowTime = System.currentTimeMillis();
		Long beforeExeMiliTime = beforeExeMiliTimeMap.get(name);
		if (beforeExeMiliTime == null) {
			beforeExeMiliTime = 0L;
		}
		if (beforeExeMiliTime + INTERVAL_MILI_TIME > nowTime) {
			try {
				//				System.out.println("wait開始:" + name);
				Thread.sleep(beforeExeMiliTime + INTERVAL_MILI_TIME - nowTime);
				//				System.out.println("wait終了:" + name);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		beforeExeMiliTime = System.currentTimeMillis();
		beforeExeMiliTimeMap.put(name, beforeExeMiliTime);
	}

	/**
	 * 割り込み処理を確認し、割り込まれてたら例外を投げて中断します。
	 * @param className
	 * @throws InterruptedException
	 */
	public static void checkInterrupt(String className) throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException(className + ":処理が中断されました");
		}
	}
}
