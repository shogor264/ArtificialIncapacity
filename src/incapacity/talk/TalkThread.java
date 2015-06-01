package incapacity.talk;

import java.util.concurrent.Callable;

public class TalkThread implements Callable<Boolean> {
	Talk talk = null;
	String input = "";

	public TalkThread(Talk talk, String input) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.talk = talk;
		this.input = input;
	}

	@Override
	public Boolean call() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return talk.isTalk(input);
	}


}
