package SmartcodeParserTest;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import BasicCommonClasses.SmartCode;
import SmartcodeParser.SmartcodeParser;

public class SmartcodeParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBarcode() {
		SmartCode barcode = SmartcodeParser.formCode("7290000688077");
		
		assertNotNull(barcode);
		assertNull(barcode.getExpirationDate());
		assertEquals(7290000688077L, barcode.getBarcode());
		
	}
	
	@Test
	public void testSmartode() {
		SmartCode barcode = SmartcodeParser.formCode("]C117010203217290000688077");
		
		assertNotNull(barcode);
		assertEquals(LocalDate.of(2001, 02, 03), barcode.getExpirationDate());
		assertEquals(7290000688077L, barcode.getBarcode());
		
	}

}
