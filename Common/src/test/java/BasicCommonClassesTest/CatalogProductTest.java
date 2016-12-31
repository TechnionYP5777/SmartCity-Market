package BasicCommonClassesTest;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;

import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Manufacturer;;


/**@author Lior Ben Ami
 * @since 2016-12-11 */
public class CatalogProductTest {
	@Test public void CatalogProductTestMethod() {
		Manufacturer man = new Manufacturer(11,"Osem");
		String bambaIm = "https://www.osem.co.il/tm-content/uploads/2015/01/Bamba_classic_80g3.png3-308x308.png";
		//just an example of use:
		try {
			ImageIO.read(new URL(bambaIm));
		} catch (IOException e) {
			
		}
		
		CatalogProduct cp = new CatalogProduct(11, "Bamba", null, man, "", 12, bambaIm, null);
		if (cp.getBarcode() != 11 || !"Bamba".equals(cp.getName()) || cp.getIngredients() != null ||
				!cp.getManufacturer().equals(man) || !"".equals(cp.getDescription()) || 
				cp.getPrice() != 12 || cp.getLocations() != null)
			fail();
		cp.setBarcode(111);
		cp.setName("Bisli");
		Ingredient gluten = new Ingredient(5, "gluten");
		cp.addIngredient(gluten);
		String description = "A popular snack with different flavours";
		cp.setDescription(description);
		Location lo = new Location(1,1,PlaceInMarket.WAREHOUSE);
		cp.addLocation(lo);
		cp.setPrice(7.35);
		if (cp.getBarcode() != 111 || !"Bisli".equals(cp.getName()) || cp.getIngredients().size() != 1 ||
				!cp.getIngredients().contains(gluten) || !cp.getManufacturer().equals(man) || 
				!description.equals(cp.getDescription()) || cp.getPrice() != 7.35 || 
				cp.getLocations().size() != 1 || !cp.getLocations().contains(lo))
			fail();
		CatalogProduct cp2 = new CatalogProduct(111, "Bamba", null, man, "", 12, bambaIm, null);
		if (!cp2.equals(cp))
			fail();
		cp2.setBarcode(11);
		if (cp2.equals(cp))
			fail();
	}
}

