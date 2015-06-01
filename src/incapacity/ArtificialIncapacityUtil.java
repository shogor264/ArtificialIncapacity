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
import java.util.List;

public class ArtificialIncapacityUtil {
	private static final Main main;

	public static enum Name {
		BYE, SAME, PATTERN, TWEET, WIKI, GOOGLE, SUGGEST, RANDOM
	}

	static {
		// TODO 自動生成されたコンストラクター・スタブ
		try {
			main = new Main();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw new Error(e);
		}
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
		for (Name name : nameAry) {
			if (Name.BYE.equals(name)) {
				// ※上から優先で判定する
				talkList.add(new TalkBye("resources/ByeTalk.txt", Name.BYE.toString()));
			} else if (Name.SAME.equals(name)) {
				// 同じことばかり言ってた時
				talkList.add(new TalkSame("resources/SameTalk.txt", Name.SAME.toString()));
			} else if (Name.PATTERN.equals(name)) {
				// 定型パターン
				talkList.add(new TalkPattern("resources/PatternTalk.txt", Name.PATTERN.toString()));
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
				talkList.add(new TalkRandom("resources/RandomTalk.txt", Name.RANDOM.toString()));
			} else {
				throw new IllegalArgumentException(name + "：対応するTalk処理がありません。");
			}
		}
		main.setTalkList(talkList);
	}

	public static String getTalkString(String input) {
		InputHistory.getInputlist().add(input);
		InputHistory.writeHistory("user>" + input);
		return main.execThread(input);
	}
}
