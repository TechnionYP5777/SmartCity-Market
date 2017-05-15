package PicturesHandlerTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Test;

import PicturesHandler.PictureManager;

public class PictureManagerTest {
	
	@Test
	public void testCurrentDateAndcheckIfMostUpdate() {
		try {
			LocalDate currentDate = PictureManager.getCurrentDate(), newDate = LocalDate.now();
			assert PictureManager.checkIfMostUpdate(currentDate);
			
			PictureManager.updateDate(newDate);
			assert PictureManager.checkIfMostUpdate(newDate);
			
			/* returning original date */
			PictureManager.updateDate(currentDate);
		} catch (IOException e) {
			fail();
		}
	}
}
