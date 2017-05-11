package SmartcodeParser;

import BasicCommonClasses.SmartCode;

/**
 * Codex - interface which represents encode and decode of Smartcode
 * 
 * @author Noam Yefet
 * @since  2017-01-02
 *
 */
public interface Codex {
	String encode(SmartCode c);

	SmartCode decode(String s);
}
