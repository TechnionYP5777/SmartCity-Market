package BasicCommonClassesTest;

import static org.junit.Assert.fail;

import java.awt.BufferCapabilities;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.joda.time.LocalDate;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.CartProduct;

/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class CartProductTest {
	@Test public void CartProductTestMethod() throws MalformedURLException, IOException {
		Manufacturer man = new Manufacturer(11,"Osem");
		BufferedImage im = ImageIO.read(
				new URL("http://barnraisersllc.com/wp-content/uploads/2016/11/21.png"));
		CatalogProduct catp = new CatalogProduct(11, "Bamba", null, man, "", 12, im, null);
		LocalDate ld = new LocalDate(2016,12,11);
		CartProduct crp = new CartProduct(catp, ld, 0);
		if (!catp.equals(crp.getCatalogProduct()) || !ld.equals(crp.getExpirationDate()) ||
				crp.getAmount() != 0)
			fail();
		catp.setBarcode(12);
		crp.setCatalogProduct(catp);
		ld.plusDays(1);
		crp.setExpirationDate(ld);
		crp.setAmount(2);
		if (!catp.equals(crp.getCatalogProduct()) || !ld.equals(crp.getExpirationDate()) ||
				crp.getAmount() != 2)
			fail();
		CartProduct crp2 = new CartProduct(catp, ld, 2);
		if (!crp2.equals(crp))
			fail();
		crp2.setAmount(1);
		if (!crp2.equals(crp))
			fail();
		crp2.setExpirationDate(ld.plusDays(1));
		if (crp2.equals(crp))
			fail();
	}
}

