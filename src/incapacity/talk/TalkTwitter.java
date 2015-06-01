package incapacity.talk;

import incapacity.Data;

import java.io.IOException;
import java.util.List;

import util.StringUtil;
import util.TweetSearchAPIUtil;

public class TalkTwitter extends Talk {
	public TalkTwitter(String talkFilePath, String talkName) throws IOException {
		super(talkFilePath, talkName);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	protected Data createData(List<String> list) {
		return null;
	}

	@Override
	public boolean isTalk(String input) throws InterruptedException {
		String tweet = TweetSearchAPIUtil.getMaxFriendsReply(input);
		if (StringUtil.isNullOrEmpty(tweet)) {
			return false;
		}
		setTalkContent(tweet + "\r\n");
		return true;
	}

}
