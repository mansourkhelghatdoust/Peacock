package usyd.distributed.scheduling.dataconversion.Tasks;

import usyd.distributed.scheduling.dataconversion.Function;
import usyd.distributed.scheduling.dataconversion.Utility;

public class TaskEventPolish implements Function<String, String> {

	private char delimiter;

	public TaskEventPolish(char delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public String apply(String input) {

		String[] atrArr = split(input, delimiter);

		int[] indexes = new int[] { 2, 3, 5, 9, 10, 0 };
		if ((Utility.isNullOrEmpty(atrArr[0]) || atrArr[0].equals("0")) || Utility.isNullOrEmpty(atrArr[3])
				|| (!atrArr[5].equals("1") && !atrArr[5].equals("4")) || Utility.isNullOrEmpty(atrArr[2])
				|| Utility.isNullOrEmpty(atrArr[9]) || Utility.isNullOrEmpty(atrArr[10]))
			return null;

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < indexes.length; i++) {
			buffer.append(atrArr[indexes[i]]);
			buffer.append(" ");
		}

		return buffer.toString();

	}

	private String[] split(String input, char delimiter) {

		if (input == null)
			return null;
		int delimiterIdx = 0;

		String[] retArr = new String[12];
		char[] charArr = input.toCharArray();

		StringBuffer temp = new StringBuffer();

		for (int i = 0; i < charArr.length && delimiterIdx < 12; i++) {
			if (charArr[i] != ',') {
				temp.append(charArr[i]);
			} else {
				retArr[delimiterIdx] = temp.toString();
				delimiterIdx++;
				temp.delete(0, temp.length());
			}
		}

		return retArr;
	}
}
