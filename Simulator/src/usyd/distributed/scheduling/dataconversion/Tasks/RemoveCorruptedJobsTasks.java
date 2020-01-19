package usyd.distributed.scheduling.dataconversion.Tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import usyd.distributed.scheduling.dataconversion.Function;
import usyd.distributed.scheduling.dataconversion.Utility;

public class RemoveCorruptedJobsTasks implements Function<BufferedReader, List<String>> {

	HashMap<String, String> map = new HashMap<String, String>();
	String fileName = null;

	public RemoveCorruptedJobsTasks(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<String> apply(BufferedReader br) {

		String currentLine = null;
		while (true) {
			try {
				currentLine = br.readLine();
				if (currentLine == null)
					break;
				String[] str = currentLine.split(" ");
				if (str.length == 6)
					updateMap(getKey(str), str[5], currentLine);
			} catch (IOException e) {
				Utility.handleIOException(br, e);
			}
		}
		HashSet<String> tasks = calculateDurationsRemoveUnfinishedTasks();
		if (tasks == null)
			return null;
		else
			return new ArrayList<String>(tasks);
	}

	private HashSet<String> calculateDurationsRemoveUnfinishedTasks() {

		HashSet<String> set = new HashSet<String>();
		String[] strs = null;
		try {
			for (String str : map.keySet()) {
				strs = str.split("-");
				if (strs.length == 3) {
					strs[2] = strs[2].equals("1") ? "4" : "1";
					String str2 = String.join("-", strs);
					if (map.containsKey(str2)) {
						set.add(String.join(" ", strs[0], strs[1], getDuration(str, str2)));
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return set.isEmpty() ? null : set;
	}

	private String getDuration(String str1, String str2) {

		BigInteger num1 = new BigInteger(map.get(str1).split(" ")[5]);
		BigInteger num2 = new BigInteger(map.get(str2).split(" ")[5]);

		if (num1.compareTo(num2) == 1) {
			return num1.subtract(num2).toString();
		} else {
			return num2.subtract(num1).toString();
		}
	}

	private void updateMap(String key, String str, String currentLine) {
		
		if (map.containsKey(key)) {
			BigInteger bStr = new BigInteger(str);
			BigInteger bLine = new BigInteger(map.get(key).split(" ")[5]);
			if (bStr.compareTo(bLine) == 1)
				map.put(key, currentLine);
		} else
			map.put(key, currentLine);
	
	}

	 private String getKey(String[] str) {
		return String.join("-", str[0], str[1], str[2]);
	 }

}
