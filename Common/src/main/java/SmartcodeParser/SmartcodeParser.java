package SmartcodeParser;

import java.time.LocalDate;

import BasicCommonClasses.SmartCode;

/**
 * 
 * @author Noam Yefet
 */
public class SmartcodeParser {

	private static String GS1_PREFIX = "]C1";
	private static String EXPIRATION_DATE_IDENTIFIER = "17";
	private static String BARCODE_IDENTIFIER = "21";
	private static Integer EXPIRATION_DATE_LEN = 6;
	private static Integer BARCODE_MAX_LEN = 13;

	private static SmartCode codeToSmartcode(String scannedCode) {
		boolean flgDateFilled = false;
		boolean flgSerialFilled = false;

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
				int year = Integer.parseInt(scannedCode.substring(0, 2));
				int month = Integer.parseInt(scannedCode.substring(2, 4));
				int day = Integer.parseInt(scannedCode.substring(4, 6));

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
}
