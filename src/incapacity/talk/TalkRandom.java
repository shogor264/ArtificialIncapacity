package incapacity.talk;

import incapacity.Data;

import java.io.IOException;
import java.util.List;

public class TalkRandom extends Talk {

	public TalkRandom(String talkFilePath, String talkName) throws IOException {
		super(talkFilePath, talkName);
	}

	@Override
	protected Data createData(List<String> list) {
		Data data = new Data();
		int i = 0;
		for (String line : list) {
			String key = String.valueOf(i);
			String value = line;
			data.put(key, value);
			i++;
		}
		return data;
	}

	@Override
	public boolean isTalk(String input) {
		Data data = getData();
		int count = data.size();
		int randomCount = (int) (Math.random() * count);
		setTalkContent(data.get(String.valueOf(randomCount)));
		return true;
	}
}
