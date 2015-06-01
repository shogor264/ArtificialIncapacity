package incapacity.talk;

import incapacity.Data;
import incapacity.InputHistory;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

public class TalkSame extends Talk {

	public TalkSame(String talkFilePath, String talkName) throws IOException {
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
	public boolean isTalk(String pInput) {
		List<String> inputList = InputHistory.getInputlist();
		Data data = getData();
		int size = inputList.size();
		int i = 0;
		if (size < 3) {
			// 開始して3回は除外
			return false;
		}
		for (String input : inputList) {
			if (i++ < size - 3) {
				continue;
			}
			if (!input.equals(pInput)) {
				break;
			}

			int dataSize = data.size();
			int randomCount = (int) (Math.random() * dataSize);
			int j = 0;
			for (Entry<String, String> entry : data.entrySet()) {
				if (j == randomCount) {
					String value = entry.getValue();
					setTalkContent(value);
					return true;
				}
				j++;
			}

			return true;
		}
		return false;
	}
}
