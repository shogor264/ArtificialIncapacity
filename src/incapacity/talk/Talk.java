package incapacity.talk;

import incapacity.Data;
import incapacity.InputHistory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import debug.Info;

public abstract class Talk {
	private String talkFilePath = "";
	private String talkName = "";
	private String talkContent = "";

	private Data data = null;

	public Talk(String talkFilePath, String talkName) throws IOException {
		this.talkFilePath = talkFilePath;
		this.talkName = talkName;
		data = readData();
	}

	public void print(String str) {
		String printStr = talkName + ">" + str;
		InputHistory.writeHistory(printStr);
		Info.log(printStr);

	}

	public final Data readData() throws IOException {
		if (talkFilePath == null) {
			return null;
		}
		Path path = Paths.get(talkFilePath);
		List<String> list = null;
		if (Files.exists(path)) {
			list = Files.readAllLines(path);
		}

		return createData(list);
		//		System.out.println(data);
	}

	/**
	 * データ（マップ）オブジェクトの生成
	 * <pre>
	 * ファイルのデータ格納形式を基にマップ情報を生成します。
	 * ※このメソッドは、継承先クラスからのみの利用を想定しています。
	 * </pre>
	 * @param list 元データ行のリスト
	 */
	protected abstract Data createData(List<String> list);

	/**
	 * 会話ロジック
	 * <pre>
	 * 会話するかの判定および、会話文字列の設定をしてください。
	 * </pre>
	 * @param input 入力文字
	 * @return 返答したかどうか
	 */
	public abstract boolean isTalk(String input) throws InterruptedException;

	public void talk() {
		print(talkContent);
	}

	public String getTalkName() {
		return talkName;
	}

	public String getTalkFilePath() {
		return talkFilePath;
	}

	public Data getData() {
		return data;
	}
	public String getTalkContent() {
		return talkContent;
	}

	public void setTalkContent(String talkContent) {
		this.talkContent = talkContent;
	}

	//
	//	public static void printPattern(String str) {
	//		System.out.println("pattern>" + str);
	//	}
	//
	//	public static void printRandom(String str) {
	//		System.out.println("random>" + str);
	//	}
	//
	//	public static void printSame(String str) {
	//		System.out.println("same>" + str);
	//	}
}
