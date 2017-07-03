package api.suggestor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import api.contracts.IGroceryList;
import api.contracts.IProduct;
import api.contracts.ISale;
import api.contracts.IStorePackage;
import api.preferences.InputPreferences;
import api.preferences.SalesPreferences;
import api.types.StoreData;
import api.types.sales.ProductSale;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.saleproperty.ASaleProperty;
import ml.decider.Decider;
import ml.deducer.Deducer;
import ml.extractor.Extractor;

/**
 * 
 * This class is the main class of the module. from here - the server will do all the operations its need
 * 
 * @author noam
 *
 */
public class Suggestor {
	
	protected static Logger log = Logger.getLogger(Suggestor.class.getName());
	
	private volatile static StoreData storeData = new StoreData();
	
	private volatile static InputPreferences inPref = new InputPreferences();
	private volatile static SalesPreferences salePref = new SalesPreferences(0.5);

	public static void updateHistory(List<? extends IGroceryList> history) {
		storeData = new StoreData(history, storeData.getStock(), storeData.getCatalog());
	}

	public static void updateStock(List<? extends IStorePackage> stock) {
		storeData = new StoreData(storeData.getHistory(), stock, storeData.getCatalog());
	}
	
	public static void updateCatalog(List<? extends IProduct> catalog) {
		storeData = new StoreData(storeData.getHistory(), storeData.getStock(), catalog);
	}


	/**
	 * try to generate offer for customer
	 * 
	 * @param currentGrocery
	 *            the current grocery lift of the costumer
	 * @param purchasedProduct
	 *            the product that the customer bought
	 * @return sale if succeeded, null otherwise (note: the sale must be one of the
	 *            types under {@link api.types.sales})
	 */
	public static ISale suggestSale(IGroceryList currentGrocery, IProduct purchasedProduct) {
		StoreData currentData = storeData;
		
		try{
			Set<ABasicProperty> initialProperties = Extractor.extractProperties(inPref, currentData, currentGrocery, purchasedProduct);
			
			Set<AProperty> properties = Deducer.deduceProperties(salePref, initialProperties);
			
			Set<ASaleProperty> salesProperties = properties.stream()
					.filter(p -> p instanceof ASaleProperty)
					.map(p -> (ASaleProperty)p )
					.collect(Collectors.toSet());
		
		
			ASaleProperty result = Decider.decideBestSale(salePref, salesProperties);
			
			if (result == null || result.getOffer().getProduct().getBarcode() != purchasedProduct.getBarcode())
				return null;
			
			String explain = Explainer.explainSale(result, properties);
			ISale saleResult = result.getOffer();
			
			log.info("I chose the sale: " + saleResult.getProduct().getName() + " with amount of: " + saleResult.getTotalAmount() +
					" in total price of: " + saleResult.getTotalPrice() +
					"\n And this is why: " + explain);
		
			return result.getOffer();
			
		} catch (Exception e){
			log.fatal(e.getMessage());
			log.debug(e.getStackTrace());
			
			return null;
		}
	}

	/**
	 * examine sale suggested by customer
	 * 
	 * @param currentGrocery
	 *            the current grocery lift of the costumer
	 * @param purchasedProduct
	 *            the Sale that that the customer suggested (must be one of the
	 *            types under {@link api.types.sales})
	 * @return same sale if agreed, another suggest or null if not.
	 */
	public static ISale examineOffer(IGroceryList currentGrocery, ISale offer) {
		StoreData currentData = storeData;
		
		try {
			
			Set<ABasicProperty> initialProperties = Extractor.extractProperties(inPref, currentData, currentGrocery, null);
			
			Set<AProperty> properties = Deducer.deduceProperties(salePref, initialProperties);
			
			Set<ASaleProperty> salesProperties = properties.stream()
					.filter(p -> p instanceof ASaleProperty)
					.map(p -> (ASaleProperty)p )
					.collect(Collectors.toSet());
			
			ASaleProperty result = Decider.decideSaleSimilar(salePref, salesProperties, offer);
			
			if (result == null)
				return null;
			
			if (isSystemMoreProfitable(result.getOffer(), offer))
				return offer;
			
			String explain = Explainer.explainSale(result, properties);

			ISale saleResult = result.getOffer();
			log.info("I agreee to similar sale: " + saleResult.getProduct().getName() + " with amount of: " + saleResult.getTotalAmount() +
					" in total price of: " + saleResult.getTotalPrice() +
					"\n And this is why: " + explain);
			
			return result.getOffer();
		
		} catch (Exception e){
			log.fatal(e.getMessage());
			log.debug(e.getStackTrace());
			
			return null;
		}
		
	}
	
	private static boolean isSystemMoreProfitable(ProductSale systemSale, ISale userOffer){
		double userDiscount = 1 - (userOffer.getTotalPrice() / 
				(userOffer.getTotalAmount() * userOffer.getProduct().getPrice()));
		
		return systemSale.getdiscount() > userDiscount;
	}

}
