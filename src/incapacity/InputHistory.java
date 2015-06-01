package incapacity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class InputHistory {
	private static final Path path = Paths.get("resources/TalkHistory.txt");
	private static final List<String> inputList = new ArrayList<>();
	private static final List<String> writeStockList = new ArrayList<>();

	public static List<String> getInputlist() {
		return inputList;
	}

	public static void writeHistory(String input)  {
		writeStockList.add(input);
	}
	public static void flush() throws IOException{
		Files.write(path, writeStockList, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
		inputList.clear();
	}
//
//	public static void readHistory() throws IOException {
//		inputList.clear();
//		inputList.addAll(Files.readAllLines(path));
//	}

}
