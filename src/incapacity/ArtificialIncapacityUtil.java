package incapacity;

import incapacity.talk.Talk;
import incapacity.talk.TalkBye;
import incapacity.talk.TalkGoogleSearch;
import incapacity.talk.TalkGoogleSuggest;
import incapacity.talk.TalkPattern;
import incapacity.talk.TalkRandom;
import incapacity.talk.TalkSame;
import incapacity.talk.TalkTwitter;
import incapacity.talk.TalkWiki;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.CommonUtil;
import util.StringUtil;
import debug.Debug;

public class ArtificialIncapacityUtil {
	private static final Main main;

	public static enum Name {
		BYE, SAME, PATTERN, TWEET, WIKI, GOOGLE, SUGGEST, RANDOM
	}

	static {

		// 環境変数からユーザ情報を取得
		Map<String, String> propertyKeyMap = new HashMap<>();
		propertyKeyMap.put("ARTIFICIAL_INCAPACITY_RESOURCES_PATH",CommonUtil.ENV_RESOURCES_PATH);
		//System.
		//			List<Map<String, String>> envMapList = Arrays.asList((Map<String,String>)System.getenv("USER"), (Map<String,String>)System.getenv());
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

		} // TODO 自動生成されたコンストラクター・スタブ
		main = new Main();
	}

	/**
	 * 会話ルールの生成
	 * <pre>
	 * どのルールから順に評価するかを指定する。
	 * 未指定の場合、デフォルトのルールを指定する。
	 * </pre>
	 * @param nameAry
	 * @throws IOException
	 */
	public static void createTalkList(Name... nameAry) throws IOException {
		if (nameAry == null || nameAry.length == 0) {
			nameAry = new Name[] { Name.BYE, Name.SAME, Name.PATTERN, Name.TWEET, Name.WIKI, Name.GOOGLE, Name.SUGGEST,
					Name.RANDOM };
		}
		List<Talk> talkList = main.getTalkList();
		talkList.clear();
		String parentPath = System.getProperty(CommonUtil.ENV_RESOURCES_PATH);
		for (Name name : nameAry) {
			if (Name.BYE.equals(name)) {
				// ※上から優先で判定する
				talkList.add(new TalkBye(parentPath + "/ByeTalk.txt", Name.BYE.toString()));
			} else if (Name.SAME.equals(name)) {
				// 同じことばかり言ってた時
				talkList.add(new TalkSame(parentPath + "/SameTalk.txt", Name.SAME.toString()));
			} else if (Name.PATTERN.equals(name)) {
				// 定型パターン
				talkList.add(new TalkPattern(parentPath + "/PatternTalk.txt", Name.PATTERN.toString()));
			} else if (Name.TWEET.equals(name)) {
				// ツイッター
				talkList.add(new TalkTwitter(null, Name.TWEET.toString()));
			} else if (Name.WIKI.equals(name)) {
				// Wikipedia検索
				talkList.add(new TalkWiki(null, Name.WIKI.toString()));
			} else if (Name.GOOGLE.equals(name)) {
				// Google検索
				talkList.add(new TalkGoogleSearch(null, Name.GOOGLE.toString()));
			} else if (Name.SUGGEST.equals(name)) {
				// Google予測変換
				talkList.add(new TalkGoogleSuggest(null, Name.SUGGEST.toString()));
			} else if (Name.RANDOM.equals(name)) {
				// ランダム
				talkList.add(new TalkRandom(parentPath + "/RandomTalk.txt", Name.RANDOM.toString()));
			} else {
				throw new IllegalArgumentException(name + "：対応するTalk処理がありません。");
			}
		}
		main.setTalkList(talkList);
	}

	protected static List<Talk> getTalkList() {
		return main.getTalkList();
	}

	public static String getTalkString(String input) {
		InputHistory.getInputlist().add(input);
		InputHistory.writeHistory("user>" + input);
		return main.execThread(input);
	}
}
