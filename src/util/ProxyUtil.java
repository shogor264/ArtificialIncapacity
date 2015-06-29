package util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Map;

public class ProxyUtil {
	private static boolean isSet = false;

	public static final String ENV_HOST = "HTTP_PROXY_HOST";
	public static final String ENV_PORT = "HTTP_PROXY_PORT";
	public static final String ENV_USER = "HTTP_PROXY_USER";
	public static final String ENV_PASSWORD = "HTTP_PROXY_PASSWORD";

	public static void setProxy() {
		if (!isSet) {
			isSet = true;
			final Map<String, String> envMap = System.getenv();
			if (!envMap.containsKey(ENV_HOST)) {
				// 環境変数未設定ならプロキシなし
				return;
			}

			System.setProperty("proxySet", "true");
			System.setProperty("proxyHost", envMap.get(ENV_HOST));
			System.setProperty("proxyPort", envMap.get(ENV_PORT));

			Authenticator.setDefault(new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(envMap.get(ENV_USER), envMap.get(ENV_PASSWORD).toCharArray());
				}
			});
		}
	}
}
