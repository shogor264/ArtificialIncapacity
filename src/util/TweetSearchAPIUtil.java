package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import debug.Debug;

/**
 * @author
 */
public class TweetSearchAPIUtil {
	private static final String CLASS_NAME = Thread.currentThread().getStackTrace()[1].getClassName();
	private static final Twitter TWITTER;;
	private static final String QUERY_OPTION = "exclude:retweets";

	static {

		// 環境変数からユーザ情報を取得
		Map<String, String> propertyKeyMap = new HashMap<>();
		propertyKeyMap.put(ProxyUtil.ENV_HOST, "twitter4j.http.proxyHost");
		propertyKeyMap.put(ProxyUtil.ENV_PORT, "twitter4j.http.proxyPort");
		propertyKeyMap.put(ProxyUtil.ENV_USER, "twitter4j.http.proxyUser");
		propertyKeyMap.put(ProxyUtil.ENV_PASSWORD, "twitter4j.http.proxyPassword");
		propertyKeyMap.put("OAUTH_CONSUMER_KEY", "twitter4j.oauth.consumerKey");
		propertyKeyMap.put("OAUTH_CONSUMER_SECRET", "twitter4j.oauth.consumerSecret");
		propertyKeyMap.put("OAUTH_ACCESS_TOKEN", "twitter4j.oauth.accessToken");
		propertyKeyMap.put("OAUTH_ACCESS_TOKEN_SECRET", "twitter4j.oauth.accessTokenSecret");
//System.
//		List<Map<String, String>> envMapList = Arrays.asList((Map<String,String>)System.getenv("USER"), (Map<String,String>)System.getenv());
		Map<String, String> envMap = System.getenv();
		Debug.log("envMap:" + envMap);
		for (Entry<String, String> entry : propertyKeyMap.entrySet()) {
			String propKey = entry.getValue();
			String envKey = entry.getKey();
			Debug.log("propKey:" + propKey);
			Debug.log("envKey:" + envKey);
			// VMパラメータに設定されている場合、そのまま使う
			if (!StringUtil.isNullOrEmpty(System.getProperty(propKey))) {
				continue;
			}
			// VMパラメータになく、環境変数に設定されている場合、
			Debug.log("envMap.get(envKey):" + envMap.get(envKey));
			if (envMap.containsKey(envKey)) {
				System.setProperty(propKey, envMap.get(envKey));
				continue;
			}
			// VMパラメータにも環境変数にもない。
			Debug.log("VMパラメータ：" + propKey + " および 環境変数：" + envKey + " 未指定です。");
		}

		System.setProperty("twitter4j.debug", "false");
		TWITTER = TwitterFactory.getSingleton();
	}

	public static void main(String[] args) throws InterruptedException {
		//		for (String str : getTweetList("電車遅延してる")) {
		//			System.out.println(str);
		//		}
		String input = "マクドナルド";
		List<Status> statusList = getMaxFriendsStatus(input);
		for (Status status : statusList) {
			System.out.println(status.getCreatedAt() + "\r\n" + status.getUser().getName() + "\r\n" + status.getText());
			System.out.println();
			Status replyStatus = getMostRecentStatus(status);
			System.out.println(replyStatus.getCreatedAt() + "\r\n" + replyStatus.getUser().getName() + "\r\n"
					+ replyStatus.getText());
		}
		//		System.out.println(getMaxFollowersReply(input));
	}

	/**
	 * いい感じのツイートを返す
	 * <pre>
	 * ・該当するツイートを検索し、フォロワー数が最大のツイート情報を取得
	 * ・そのツイートのユーザの直近のリプライを取得し、ツイートを返却
	 * ・リプライがなかった場合、最初に検索したツイートを返却
	 * </pre>
	 * @param key
	 * @return
	 * @throws InterruptedException
	 */
	public static String getMaxFriendsReply(String pKey) throws InterruptedException {
		String[] keyAry = { "\"" + pKey + "\"", pKey };
		Status replyStatus = null;
		List<Status> parentStatusList = new ArrayList<>();
		for (String key : keyAry) {
			Debug.log("key:" + key);
			List<Status> statusList = getMaxFriendsStatus(key);
			if (statusList.isEmpty()) {
				continue;
			}
			boolean isBreak = false;
			for (Status status : statusList) {
				Debug.log("status:" + status.getText());
				parentStatusList.add(status);
				replyStatus = getMostRecentStatus(status);
				if (replyStatus == null) {
					continue;
				} else {
					isBreak = true;
					break;
				}
			}
			if (isBreak) {
				break;
			}
		}
		if (replyStatus == null) {
			return "";
		}
		String text = replyStatus.getText();
		String resultStr = StringUtil.removeReplyStr(text);
		return resultStr;
	}

	private static Query createDefaultQuery() {
		Query query = new Query();
		// 1 度のリクエストで取得する Tweet の数（100が最大）
		query.setCount(100);
		query.setLang("ja");
		query.resultType(Query.MIXED);
		return query;
	}

	/**
	 * サーチ実行処理
	 * <pre>
	 * 通信のwaitも設けているので、必ずここを通してください
	 * </pre>
	 * @param query
	 * @return
	 * @throws TwitterException
	 * @throws InterruptedException
	 */
	private static QueryResult search(Query query) throws TwitterException, InterruptedException {
		ThreadUtil.checkInterrupt(CLASS_NAME);
		ThreadUtil.waitExe(CLASS_NAME);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		//		System.out.println("twitter検索");
		QueryResult result = TWITTER.search(query);
		ThreadUtil.checkInterrupt(CLASS_NAME);
		return result;
	}

	/**
	 * Tweet情報を一括取得
	 * @param key
	 * @return
	 * @throws InterruptedException
	 */
	public static List<String> getTweetList(String key) throws InterruptedException {
		Query query = createDefaultQuery();

		query.setQuery(key + " " + QUERY_OPTION);
		List<String> resultList = new ArrayList<>();
		try {
			QueryResult result = search(query);
			//			System.out.println("ヒット数 : " + result.getTweets().size());

			int count = 0; // 取得した総件数
			for (Status status : result.getTweets()) {
				// 本文
				String str = status.getText();
				//				Date date = tweet.getCreatedAt();
				resultList.add(++count + "\t" + str);
			}
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return resultList;
	}

	/**
	 * 一番友達の多いツイート、ほとんど同じなツイートの状態を取得
	 * @param key
	 * @return
	 * @throws InterruptedException
	 */
	public static List<Status> getMaxFriendsStatus(String key) throws InterruptedException {
		Query query = createDefaultQuery();

		Calendar cal = Calendar.getInstance();
		//		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date date = cal.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateUntilStr = df.format(date);

		query.setQuery(key + " until:" + dateUntilStr + " " + QUERY_OPTION);
		Debug.log(query.toString());
		List<Status> resultStatusList = new ArrayList<>();
		try {
			int num = 5;
			int[] maxfriendsCountAry = new int[num];
			Status[] maxStatusAry = new Status[num];
			//			for (int i = 0; i < 3; i++) {
			QueryResult result = search(query);
			for (Status status : result.getTweets()) {
				String tweet = status.getText();

				User user = status.getUser();
				int friendsCount = user.getFriendsCount();
				for (int maxfriendsCount : maxfriendsCountAry) {
					// ほとんど同じ文かどうか
					boolean isNear = tweet.length() < key.length() + 10;
					if (isNear || friendsCount > maxfriendsCount) {
						int min = Integer.MAX_VALUE;
						int minno = 0;
						for (int no = 0; no < maxfriendsCountAry.length; no++) {
							if (maxfriendsCountAry[no] < min) {
								min = maxfriendsCountAry[no];
								minno = no;
							}
						}
						maxfriendsCountAry[minno] = friendsCount;
						maxStatusAry[minno] = status;
						if (isNear) {
							// 最大値を設定して、入れ替え対象から除外
							maxfriendsCountAry[minno] = Integer.MAX_VALUE;
						}
						break;
					}
				}
			}
			Map<String, Status> map = new HashMap<>();
			List<StringComparator> resultCompList = new ArrayList<>();
			for (Status status : maxStatusAry) {
				if (status != null) {
					String text = status.getText();
					resultCompList.add(new StringComparator(text));

					map.put(text, status);
				}
			}
			Debug.log("■Comparator前：" + resultCompList);
			Collections.sort(resultCompList);
			Debug.log("■Comparator後：" + resultCompList);
			for (StringComparator resultComp : resultCompList) {
				resultStatusList.add(map.get(resultComp.getTarget()));
			}
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return resultStatusList;
	}

	/**
	 * ツイートの状態から、その時点の直近のリプライを取得
	 * @param key
	 * @param isAmbiguous あいまい検索
	 * @return
	 * @throws InterruptedException
	 */
	public static Status getMostRecentStatus(Status pStatus) throws InterruptedException {
		Query query = createDefaultQuery();
		String screenName = pStatus.getUser().getScreenName();
		Date pDate = pStatus.getCreatedAt();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateSinceStr = df.format(pDate);

		Calendar nextCal = Calendar.getInstance();
		nextCal.setTime(pDate);
		nextCal.add(Calendar.DAY_OF_MONTH, 1);
		Date nextDate = nextCal.getTime();
		String dateUntilStr = df.format(nextDate);

		Status resultStatus = null;
		try {

			query.setQuery("to:" + screenName + " until:" + dateUntilStr + " "
					+ QUERY_OPTION);
			QueryResult result = search(query);
			Status beforeStatus = null;
			for (int i = 0; i < 5; i++) {
				boolean isBreak = false;
				for (Status status : result.getTweets()) {
					Date date = status.getCreatedAt();
					if (pDate.compareTo(date) > 0) {
						isBreak = true;
						resultStatus = beforeStatus;
						break;
					}
					beforeStatus = status;
				}
				if (isBreak) {
					break;
				}
				if (result.hasNext()) {
					query = result.nextQuery();
					result = search(query);
				} else {
					break;
				}
				//				System.out.println(beforeStatus);
			}

		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return resultStatus;
	}
}
