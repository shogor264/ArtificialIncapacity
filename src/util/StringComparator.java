package util;

public class StringComparator implements Comparable<StringComparator> {
	private String target = "";

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public StringComparator(String target) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.target = target;
	}

	@Override
	public int compareTo(StringComparator arg0) {
		// TODO 自動生成されたメソッド・スタブ
		//idの文字列長でソート。文字列数がが小さい順に並べる。
		String p = arg0.toString();
		return target.length() - p.length();
	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return target;
	}
}
