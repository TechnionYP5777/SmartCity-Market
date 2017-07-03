package CustomerContracts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Sale;
import BasicCommonClasses.SmartCode;
import CustomerContracts.ACustomerExceptions.AmountBiggerThanAvailable;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.GroceryListIsEmpty;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductCatalogDoesNotExist;
import CustomerContracts.ACustomerExceptions.ProductNotInCart;
import CustomerContracts.ACustomerExceptions.ProductPackageDoesNotExist;
import CustomerContracts.ACustomerExceptions.UsernameAlreadyExists;
import SMExceptions.CommonExceptions.CriticalError;

/**
 * ICustomer - This interface is the contract for Customer Type.
 *
 * @author Lior Ben Ami
 * @since 2017-01-04
 */
public interface ICustomer {
	/**
	 * getId - returns the customer's id
	 * @return int
	 */
	int getId();

	/**
	 * getCartProductCache - returns the current CartProduct Cache.
	 * @return HashMap<SmartCode, CartProduct> 
	 */
	HashMap<Long, CartProduct> getCartProductCache();
	
	/**
	 * login - the customer login to the server and gets it's own id;
	 * @param updateProductPictures - when set true, UpdateProductPictures flow is activated. By default it should be false.
	 * @throws CriticalError 
	 * @throws AuthenticationError
	 */
	void login(String username, String password, boolean updateProductPictures) throws CriticalError, AuthenticationError;
	
	/**
	 * logout - the customer logout from  the server. To use in the end of the shopping.
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 */
	void logout() throws CustomerNotConnected, CriticalError;
	
	/**
	 * resume - saves the data of the customer from the server (to use in case of collapse)
	 * @param int _id
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 */
	void resume(int _id) throws CriticalError, CustomerNotConnected;
	
	/**
	 * addPtoductToCart - Adds product with amount to the customer
	 * 
	 * @param SmartCode c
	 * @param CatalogProduct catalogProduct
	 * @param int amount
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws ProductPackageDoesNotExist 
	 * @throws AmountBiggerThanAvailable 
	 * @throws InvalidParameter 
	 */
	void addProductToCart(SmartCode c, CatalogProduct p, int amount) throws CriticalError, CustomerNotConnected, AmountBiggerThanAvailable, ProductPackageDoesNotExist, InvalidParameter;
	
	/**
	 * returnProductToShelf - removes product with amount from the customer
	 * 
	 * @param SmartCode c 
	 * @param  int amount
	 * @throws ProductNotInCart
	 * @throws AmountBiggerThanAvailable 
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws ProductPackageDoesNotExist 
	 */
	void returnProductToShelf(SmartCode c, int amount) throws CriticalError, CustomerNotConnected, AmountBiggerThanAvailable, ProductPackageDoesNotExist;
	
	/**
	 * getTotalSum - returns the total sum of the shopping
	 * @return Double
	 */
	Double getTotalSum(); 
	
	/**
	 * getCartProductsNum - returns the number of the CarProducts in the customer
	 * @return Integer
	 */
	Integer getCartProductsNum();
	
	/**
	 * checkOutGroceryList - returns the final total sum of the shopping and initialize grocery list, including the sales in its calculation
	 * @param Map<Sale, Boolean> specialSaleTaken
	 * @return Double
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws GroceryListIsEmpty 
	 */
	Double checkOutGroceryList(Map<Sale, Boolean> specialSaleTaken) throws CriticalError, CustomerNotConnected, GroceryListIsEmpty;

	/**
	 * getCartProduct - returns the CartProduct in the cart by its SmartCode
	 *  
	 * @param SmartCode
	 * @return CartProduct
	 */
	CartProduct getCartProduct(SmartCode c);

	/**
	 * viewCatalogProduct - returns product from the catalog by SmartCode.
	 * 
	 * @param SmartCode
	 * @return CatalogProduct
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws GroceryListIsEmpty 
	 */
	CatalogProduct viewCatalogProduct(SmartCode c) throws CriticalError, CustomerNotConnected, ProductCatalogDoesNotExist;
	
	/**
	 * removeAllItemsOfCartProduct - remove all items of CartProduct from the cart.
	 * 
	 * @param SmartCode
	 * @throws CriticalError 
	 * @throws ProductNotInCart 
	 */
	void removeAllItemsOfCartProduct(SmartCode c) throws ProductNotInCart, CriticalError;
	
	/**
	 * registerNewCustomer - register new customer to the system
	 * 
	 * @param CustomerProfile
	 * @throws CriticalError 
	 * @throws ProductNotInCart 
	 */
	void registerNewCustomer(CustomerProfile p) throws CriticalError, InvalidParameter, UsernameAlreadyExists;
	
	/**
	 * getAllIngredients - returns all existing ingredients  in system
	 * 
	 * @return List<Ingredient>
	 * @throws CriticalError 
	 * @throws ProductNotInCart 
	 */
	List<Ingredient> getAllIngredients() throws CriticalError;
	
	/**
	 * isFreeUsername - returns true if the chosen user name isn't in use
	 * @param username
	 * @return Boolean
	 * @throws CriticalError
	 */
	Boolean isFreeUsername(String username) throws CriticalError;
	
	/**
	 * getSaleForProduct - returns sale for catalog product (by its barcode)
	 * @param Long barcode
	 * @return Sale
	 * @throws CriticalError
	 * @throws CustomerNotConnected
	 * @throws InvalidParameter
	 * @throws ProductCatalogDoesNotExist
	 */
	Sale getSaleForProduct(Long barcode) throws CriticalError, CustomerNotConnected, InvalidParameter, ProductCatalogDoesNotExist;

	/**
	 * getMarketCatalog - return the products catlaog
	 * @return List<CatalogProduct>
	 * @throws CriticalError
	 */
	List<CatalogProduct> getMarketCatalog() throws CriticalError;
}