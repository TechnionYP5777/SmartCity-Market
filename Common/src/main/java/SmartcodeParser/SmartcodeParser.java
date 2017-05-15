package SmartcodeParser;

import java.time.LocalDate;
import java.util.HashMap;

import BasicCommonClasses.SmartCode;

/**
 * SmartcodeParser - This class parse the Smartcode which is been sent from the scanner.
 * 
 * @author Noam Yefet
 * @since  2017-01-02
 */
public class SmartcodeParser implements Codex {

	static final String GS1_PREFIX = "]C1";
	static final String EXPIRATION_DATE_IDENTIFIER = "17";
	static final String BARCODE_IDENTIFIER = "21";

	private static final Integer EXPIRATION_DATE_LEN = 6;
	private static final Integer BARCODE_MAX_LEN = 13;

	private static final String FNC1_CHARACTER = "FNC 1";
	private static final String START_CODE_C_CHARACTER = "Start Code C";
	private static final String CODE_B_CHARACTER = "Code B";
	private static final String STOP_CHARACTER = "Stop";

	private static HashMap<String, Code128Char> codeCMap;

	/*****************************
	 * 
	 * decode part
	 * 
	 *****************************/

	@Override
	public SmartCode decode(String ¢) {
		return formCode(¢);
	}

	/**
	 * parse GS1 (EAN128) code to Smartcode
	 * 
	 * @param scannedCode
	 * @return
	 */
	private static SmartCode codeToSmartcode(String scannedCode) {
		boolean flgDateFilled = false, flgSerialFilled = false;
		SmartCode $ = new SmartCode(0, null);

		// move to the smartcode itself
		scannedCode = scannedCode.substring(GS1_PREFIX.length());

		while (scannedCode.length() != 0) {
			// CASE: expiration date field
			if (scannedCode.startsWith(EXPIRATION_DATE_IDENTIFIER)) {
				// eats the field code
				scannedCode = scannedCode.substring(EXPIRATION_DATE_IDENTIFIER.length());
				// check if the expiration date already inserted or the field is
				// too small or not all digits
				if (flgDateFilled || scannedCode.length() < 6
						|| !scannedCode.matches("[0-9]{" + EXPIRATION_DATE_LEN + "}" + ".*"))
					return null;

				// start parsing expiration date
				flgDateFilled = true;
				int year = Integer.parseInt(scannedCode.substring(0, 2)),
						month = Integer.parseInt(scannedCode.substring(2, 4)),
						day = Integer.parseInt(scannedCode.substring(4, 6));
				if (month > 12 || month < 1 || day > 31 || day < 1)
					return null;

				$.setExpirationDate(LocalDate.of(2000 + year, month, day));

				// eats the expiration date code
				scannedCode = scannedCode.substring(EXPIRATION_DATE_LEN);

				continue;
			}
			if (scannedCode.startsWith(BARCODE_IDENTIFIER)) {
				// eats the field code
				scannedCode = scannedCode.substring(BARCODE_IDENTIFIER.length());
				// check if the expiration date already inserted or the field is
				// too small or not all digits
				if (flgSerialFilled)
					return null;

				// start parsing barcode (serial code)
				flgSerialFilled = true;

				// search for the end of barcode
				int i;
				for (i = 0; i < scannedCode.length(); ++i)
					if (scannedCode.charAt(i) < '0' || scannedCode.charAt(i) > '9')
						break;

				if (i > BARCODE_MAX_LEN)
					return null;

				// if we reached the end - fix the offset
				$.setBarcode(Long.parseLong(
						(i == scannedCode.length()) ? scannedCode.substring(0) : scannedCode.substring(0, i - 1)));

				// eats the barcode
				scannedCode = scannedCode.substring(i);
				continue;
			}

			// goto next char
			scannedCode = scannedCode.substring(1);
		}

		return $;
	}

	/**
	 * Parse code that came from scanner to Smartcode or barcode
	 * 
	 * @param scannedCode
	 *            - The scanned code
	 * @return If the code is Smartcode - return parsing of that code If the
	 *         code is barcode - return Smarcode with the scanned barcode and
	 *         null as expiration date On error - return null.
	 */
	public static SmartCode formCode(String scannedCode) {

		return scannedCode.startsWith(GS1_PREFIX) ? codeToSmartcode(scannedCode)
				: !scannedCode.matches("\\d*") || scannedCode.length() > 13 ? null
						: new SmartCode(Long.parseLong(scannedCode), null);
	}

	/*****************************
	 * 
	 * encode part
	 * 
	 *****************************/

	@Override
	public String encode(SmartCode ¢) {
		return smartcodeToCode(¢);
	}

	/**
	 * return the code class of digit (0-9) according to Code B (or A) mapping
	 * 
	 * @param digitChar
	 * @return
	 */
	private static Code128Char getDigitCharCodeInCodeB(String digitChar) {
		// this number is the index of digit 0 character in Code B mapping
		final int DIGIT_0_INDEX = 16;

		int digit = Integer.parseInt(digitChar);
		assert (digit >= 0 && digit <= 9);

		return getCode128CharByIndex(DIGIT_0_INDEX + digit);
	}

	/**
	 * return the code class character according to the character index (in Code
	 * 128)
	 * 
	 * @param index
	 * @return
	 */
	private static Code128Char getCode128CharByIndex(int index) {

		// search for the given index
		for (Code128Char $ : codeCMap.values())
			if ($.index == index)
				return $;

		return null;
	}

	private static String intCharToStringChar(Integer integerChar) {
		assert (integerChar != null && integerChar >= 0);
		return integerChar > 9 ? integerChar + "" : "0" + integerChar;
	}

	public static String smartcodeToCode(SmartCode c) {

		StringBuilder result = new StringBuilder();
		Code128Char tempChar = null;
		int checksum = 0, positionMultiplier = 1;
		// insert start code c and fnc1 identifiers
		tempChar = codeCMap.get(START_CODE_C_CHARACTER);
		checksum = tempChar.index;
		result.append(tempChar.binaryCode);

		tempChar = codeCMap.get(FNC1_CHARACTER);
		checksum += positionMultiplier * tempChar.index; // multiplier = 1
		result.append(tempChar.binaryCode);

		/*
		 * inserting expiration date field
		 */
		// insert field code
		++positionMultiplier;
		tempChar = codeCMap.get(EXPIRATION_DATE_IDENTIFIER);
		checksum += positionMultiplier * tempChar.index;
		result.append(tempChar.binaryCode);

		// insert expiration date itself
		LocalDate date = c.getExpirationDate();

		++positionMultiplier;
		tempChar = codeCMap.get(intCharToStringChar(date.getYear() % 100));
		checksum += positionMultiplier * tempChar.index;
		result.append(tempChar.binaryCode);
		++positionMultiplier;

		tempChar = codeCMap.get(intCharToStringChar(date.getMonthValue()));
		checksum += positionMultiplier * tempChar.index;
		result.append(tempChar.binaryCode);

		++positionMultiplier;
		tempChar = codeCMap.get(intCharToStringChar(date.getDayOfMonth()));
		checksum += positionMultiplier * tempChar.index;
		result.append(tempChar.binaryCode);

		/*
		 * inserting barcode field
		 */
		// insert field code
		++positionMultiplier;
		tempChar = codeCMap.get(BARCODE_IDENTIFIER);
		checksum += positionMultiplier * tempChar.index;
		result.append(tempChar.binaryCode);

		// insert barcode itself
		String barcode = Long.toString(c.getBarcode());

		// convert barcode digits in pairs
		while (barcode.length() >= 2) {
			++positionMultiplier;
			tempChar = codeCMap.get(barcode.substring(0, 2));
			checksum += positionMultiplier * tempChar.index;
			result.append(tempChar.binaryCode);
			barcode = barcode.substring(2);
		}

		// if one digit left - move to Code B and print the relevant code
		if (barcode.length() == 1) {
			++positionMultiplier;
			tempChar = codeCMap.get(CODE_B_CHARACTER);
			checksum += positionMultiplier * tempChar.index;
			result.append(tempChar.binaryCode);

			++positionMultiplier;
			tempChar = getDigitCharCodeInCodeB(barcode);
			checksum += positionMultiplier * tempChar.index;
			result.append(tempChar.binaryCode);
		}

		// add checksum to code
		result.append(getCode128CharByIndex(checksum % 103).binaryCode);

		// add stop code
		result.append(codeCMap.get(STOP_CHARACTER).binaryCode);

		return result + "";
	}

	private static class Code128Char {
		/**
		 * the index of character in code C 128
		 */
		int index;
		/**
		 * the numbers that the code will parse to
		 */
		@SuppressWarnings("unused")
		String character;
		/**
		 * binary code representation as strikes of black (1) and white (0),
		 * like: 11110010010
		 */
		String binaryCode;
		/**
		 * binary code representation as width of black rectangles (the one in
		 * the even position) and width of white rectangles (the one in the odd
		 * position), like: 421211
		 */
		@SuppressWarnings("unused")
		String widthCode;

		public Code128Char(int index, String digits, String binaryCode, String widthCode) {
			this.index = index;
			this.character = digits;
			this.binaryCode = binaryCode;
			this.widthCode = widthCode;
		}

	}

	/**
	 * initialize the map for the first time
	 */
	static {
		codeCMap = new HashMap<>();
		codeCMap.put("00", new Code128Char(0, "00", "11011001100", "212222"));
		codeCMap.put("01", new Code128Char(1, "01", "11001101100", "222122"));
		codeCMap.put("02", new Code128Char(2, "02", "11001100110", "222221"));
		codeCMap.put("03", new Code128Char(3, "03", "10010011000", "121223"));
		codeCMap.put("04", new Code128Char(4, "04", "10010001100", "121322"));
		codeCMap.put("05", new Code128Char(5, "05", "10001001100", "131222"));
		codeCMap.put("06", new Code128Char(6, "06", "10011001000", "122213"));
		codeCMap.put("07", new Code128Char(7, "07", "10011000100", "122312"));
		codeCMap.put("08", new Code128Char(8, "08", "10001100100", "132212"));
		codeCMap.put("09", new Code128Char(9, "09", "11001001000", "221213"));
		codeCMap.put("10", new Code128Char(10, "10", "11001000100", "221312"));
		codeCMap.put("11", new Code128Char(11, "11", "11000100100", "231212"));
		codeCMap.put("12", new Code128Char(12, "12", "10110011100", "112232"));
		codeCMap.put("13", new Code128Char(13, "13", "10011011100", "122132"));
		codeCMap.put("14", new Code128Char(14, "14", "10011001110", "122231"));
		codeCMap.put("15", new Code128Char(15, "15", "10111001100", "113222"));
		codeCMap.put("16", new Code128Char(16, "16", "10011101100", "123122"));
		codeCMap.put("17", new Code128Char(17, "17", "10011100110", "123221"));
		codeCMap.put("18", new Code128Char(18, "18", "11001110010", "223211"));
		codeCMap.put("19", new Code128Char(19, "19", "11001011100", "221132"));
		codeCMap.put("20", new Code128Char(20, "20", "11001001110", "221231"));
		codeCMap.put("21", new Code128Char(21, "21", "11011100100", "213212"));
		codeCMap.put("22", new Code128Char(22, "22", "11001110100", "223112"));
		codeCMap.put("23", new Code128Char(23, "23", "11101101110", "312131"));
		codeCMap.put("24", new Code128Char(24, "24", "11101001100", "311222"));
		codeCMap.put("25", new Code128Char(25, "25", "11100101100", "321122"));
		codeCMap.put("26", new Code128Char(26, "26", "11100100110", "321221"));
		codeCMap.put("27", new Code128Char(27, "27", "11101100100", "312212"));
		codeCMap.put("28", new Code128Char(28, "28", "11100110100", "322112"));
		codeCMap.put("29", new Code128Char(29, "29", "11100110010", "322211"));
		codeCMap.put("30", new Code128Char(30, "30", "11011011000", "212123"));
		codeCMap.put("31", new Code128Char(31, "31", "11011000110", "212321"));
		codeCMap.put("32", new Code128Char(32, "32", "11000110110", "232121"));
		codeCMap.put("33", new Code128Char(33, "33", "10100011000", "111323"));
		codeCMap.put("34", new Code128Char(34, "34", "10001011000", "131123"));
		codeCMap.put("35", new Code128Char(35, "35", "10001000110", "131321"));
		codeCMap.put("36", new Code128Char(36, "36", "10110001000", "112313"));
		codeCMap.put("37", new Code128Char(37, "37", "10001101000", "132113"));
		codeCMap.put("38", new Code128Char(38, "38", "10001100010", "132311"));
		codeCMap.put("39", new Code128Char(39, "39", "11010001000", "211313"));
		codeCMap.put("40", new Code128Char(40, "40", "11000101000", "231113"));
		codeCMap.put("41", new Code128Char(41, "41", "11000100010", "231311"));
		codeCMap.put("42", new Code128Char(42, "42", "10110111000", "112133"));
		codeCMap.put("43", new Code128Char(43, "43", "10110001110", "112331"));
		codeCMap.put("44", new Code128Char(44, "44", "10001101110", "132131"));
		codeCMap.put("45", new Code128Char(45, "45", "10111011000", "113123"));
		codeCMap.put("46", new Code128Char(46, "46", "10111000110", "113321"));
		codeCMap.put("47", new Code128Char(47, "47", "10001110110", "133121"));
		codeCMap.put("48", new Code128Char(48, "48", "11101110110", "313121"));
		codeCMap.put("49", new Code128Char(49, "49", "11010001110", "211331"));
		codeCMap.put("50", new Code128Char(50, "50", "11000101110", "231131"));
		codeCMap.put("51", new Code128Char(51, "51", "11011101000", "213113"));
		codeCMap.put("52", new Code128Char(52, "52", "11011100010", "213311"));
		codeCMap.put("53", new Code128Char(53, "53", "11011101110", "213131"));
		codeCMap.put("54", new Code128Char(54, "54", "11101011000", "311123"));
		codeCMap.put("55", new Code128Char(55, "55", "11101000110", "311321"));
		codeCMap.put("56", new Code128Char(56, "56", "11100010110", "331121"));
		codeCMap.put("57", new Code128Char(57, "57", "11101101000", "312113"));
		codeCMap.put("58", new Code128Char(58, "58", "11101100010", "312311"));
		codeCMap.put("59", new Code128Char(59, "59", "11100011010", "332111"));
		codeCMap.put("60", new Code128Char(60, "60", "11101111010", "314111"));
		codeCMap.put("61", new Code128Char(61, "61", "11001000010", "221411"));
		codeCMap.put("62", new Code128Char(62, "62", "11110001010", "431111"));
		codeCMap.put("63", new Code128Char(63, "63", "10100110000", "111224"));
		codeCMap.put("64", new Code128Char(64, "64", "10100001100", "111422"));
		codeCMap.put("65", new Code128Char(65, "65", "10010110000", "121124"));
		codeCMap.put("66", new Code128Char(66, "66", "10010000110", "121421"));
		codeCMap.put("67", new Code128Char(67, "67", "10000101100", "141122"));
		codeCMap.put("68", new Code128Char(68, "68", "10000100110", "141221"));
		codeCMap.put("69", new Code128Char(69, "69", "10110010000", "112214"));
		codeCMap.put("70", new Code128Char(70, "70", "10110000100", "112412"));
		codeCMap.put("71", new Code128Char(71, "71", "10011010000", "122114"));
		codeCMap.put("72", new Code128Char(72, "72", "10011000010", "122411"));
		codeCMap.put("73", new Code128Char(73, "73", "10000110100", "142112"));
		codeCMap.put("74", new Code128Char(74, "74", "10000110010", "142211"));
		codeCMap.put("75", new Code128Char(75, "75", "11000010010", "241211"));
		codeCMap.put("76", new Code128Char(76, "76", "11001010000", "221114"));
		codeCMap.put("77", new Code128Char(77, "77", "11110111010", "413111"));
		codeCMap.put("78", new Code128Char(78, "78", "11000010100", "241112"));
		codeCMap.put("79", new Code128Char(79, "79", "10001111010", "134111"));
		codeCMap.put("80", new Code128Char(80, "80", "10100111100", "111242"));
		codeCMap.put("81", new Code128Char(81, "81", "10010111100", "121142"));
		codeCMap.put("82", new Code128Char(82, "82", "10010011110", "121241"));
		codeCMap.put("83", new Code128Char(83, "83", "10111100100", "114212"));
		codeCMap.put("84", new Code128Char(84, "84", "10011110100", "124112"));
		codeCMap.put("85", new Code128Char(85, "85", "10011110010", "124211"));
		codeCMap.put("86", new Code128Char(86, "86", "11110100100", "411212"));
		codeCMap.put("87", new Code128Char(87, "87", "11110010100", "421112"));
		codeCMap.put("88", new Code128Char(88, "88", "11110010010", "421211"));
		codeCMap.put("89", new Code128Char(89, "89", "11011011110", "212141"));
		codeCMap.put("90", new Code128Char(90, "90", "11011110110", "214121"));
		codeCMap.put("91", new Code128Char(91, "91", "11110110110", "412121"));
		codeCMap.put("92", new Code128Char(92, "92", "10101111000", "111143"));
		codeCMap.put("93", new Code128Char(93, "93", "10100011110", "111341"));
		codeCMap.put("94", new Code128Char(94, "94", "10001011110", "131141"));
		codeCMap.put("95", new Code128Char(95, "95", "10111101000", "114113"));
		codeCMap.put("96", new Code128Char(96, "96", "10111100010", "114311"));
		codeCMap.put("97", new Code128Char(97, "97", "11110101000", "411113"));
		codeCMap.put("98", new Code128Char(98, "98", "11110100010", "411311"));
		codeCMap.put("99", new Code128Char(99, "99", "10111011110", "113141"));
		codeCMap.put("Code B", new Code128Char(100, "Code B", "10111101110", "114131"));
		codeCMap.put("Code A", new Code128Char(101, "Code A", "11101011110", "311141"));
		codeCMap.put("FNC 1", new Code128Char(102, "FNC 1", "11110101110", "411131"));
		codeCMap.put("Start Code A", new Code128Char(103, "Start Code A", "11010000100", "211412"));
		codeCMap.put("Start Code B", new Code128Char(104, "Start Code B", "11010010000", "211214"));
		codeCMap.put("Start Code C", new Code128Char(105, "Start Code C", "11010011100", "211232"));
		codeCMap.put("Stop", new Code128Char(106, "Stop", "1100011101011", "2331112"));

	}

}
