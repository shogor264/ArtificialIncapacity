package incapacity;

import incapacity.ArtificialIncapacityUtil.Name;
import incapacity.talk.Talk;
import incapacity.talk.TalkBye;
import incapacity.talk.TalkThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import debug.Info;

public class Main {
	private static final Scanner SCANNER = new Scanner(System.in);
	private static final long WAIT_MILITIME = 100L;
	private static final long TIMEOUT_TIME = 30L;
	private List<Talk> talkList = new ArrayList<>();

	public List<Talk> getTalkList() {
		return talkList;
	}

	public void setTalkList(List<Talk> talkList) {
		this.talkList = talkList;
	}

	public static void main(String[] args) throws IOException {
		Info.isInfo = true;
		ArtificialIncapacityUtil.createTalkList(new Name[] { Name.BYE, Name.SAME, Name.PATTERN, Name.TWEET, Name.WIKI,
				Name.GOOGLE, Name.SUGGEST, Name.RANDOM });
		new Main().exec();
		System.exit(0);
	}

	public Main() throws IOException {
		// 初期処理
	}

	private void exec() {
		//			InputHistory.readHistory();
		Info.log("会話をしよう。（終わる時は「" + TalkBye.END + "」と言ってね）");
		while (true) {

			String input = input();
			if (input == null) {
				continue;
			}

			execThread(input);

		}

	}

	protected String execThread(String input) {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
		for (Talk talk : talkList) {
			Future<Boolean> future = exec.submit(new TalkThread(talk, input));
			list.add(future);
		}

		//Future#getメソッドで結果を取得できる。実行中の場合は終了まで待つ。
		long timeoutTime = System.currentTimeMillis() + TIMEOUT_TIME * 1000;
		boolean[] isDoneArray = new boolean[talkList.size()];
		for (int i = 0; i < isDoneArray.length; i++) {
			isDoneArray[i] = false;
		}
		try {
			while (true) {
				int cnt = 0;
				for (Future<Boolean> future : list) {
					if (!isDoneArray[cnt]) {
						// 表示したら飛ばす
						//						System.out.println("完了:" + talkList.get(cnt));
						if (future.isDone()) {
							isDoneArray[cnt] = true;
						} else {
							//						System.out.println("未完了:" + talkList.get(cnt));
						}
					}
					cnt++;
				}
				Thread.sleep(WAIT_MILITIME);
				boolean isComplete = true;
				cnt = 0;
				for (boolean isDone : isDoneArray) {
					// 終わっていないなら、そのまま
					if (!isDone) {
						isComplete = false;
						break;
					}
					// 全部終わったか、終わっていないものの、前のパターンが話すと決まっているなら、一通り中断し完了とする。
					if (list.get(cnt).get()) {
						for (Future<Boolean> future : list) {
							if (!future.isDone()) {
								future.cancel(true);
							}
						}
						break;
					}
					cnt++;
				}
				if (isComplete) {
					cnt = 0;
					for (Future<Boolean> future : list) {
						boolean isTalk = future.get();
						if (isTalk) {
							Talk talk = talkList.get(cnt);
							talk.talk();
							if (TalkBye.END.equals(input)) {
								// ファイル出力し、終了
								InputHistory.flush();
								break;
							}
							return talk.getTalkName() + ">>" + talk.getTalkContent();
						}
						cnt++;
					}
				}
				if (System.currentTimeMillis() > timeoutTime) {
					Info.log("タイムアウトしました");
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return "";
	}

	private String input() {
		Info.logNoLn(">>");
		String input = SCANNER.nextLine();
		if (input.length() == 0) {
			return null;
		}
		InputHistory.getInputlist().add(input);
		InputHistory.writeHistory("user>" + input);
		return input;
	}

}
