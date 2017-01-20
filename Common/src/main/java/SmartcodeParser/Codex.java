package SmartcodeParser;

import BasicCommonClasses.SmartCode;

public interface Codex {
	String encode(SmartCode c);

	SmartCode decode(String s);
}
