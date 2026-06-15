package pages

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import models.*

public class HomePage extends CommonPage {
	public void verifyPageLoadSuccessfully() {
		WebUI.verifyElementPresent(
			findTestObject('Page_Home/img_HomePageLogo'),
			10
		)
	}
	
	public void navigateToLoginPage() {
		WebUI.click(findTestObject('Page_Home/btn_SignupLogin'))
		
		WebUI.verifyElementVisible(
			findTestObject('Page_Login/lbl_LoginToYourAccount')
		)
	}
	
	public void navigateToTestCasesPage() {
		WebUI.click(findTestObject('Page_Home/btn_TestCases'))
		
		def testCasesLabel = findTestObject('Page_TestCases/lbl_Title')
		
		WebUI.waitForElementVisible(testCasesLabel, 10)

		WebUI.verifyElementVisible(testCasesLabel)
	}
	
	public void navigateToProductsPage() {
		WebUI.click(findTestObject('Page_Home/btn_Products'))
		
		def allProductsLabel = findTestObject('Page_Products/lbl_AllProducts')
		
		WebUI.waitForElementVisible(allProductsLabel, 10)

		WebUI.verifyElementVisible(allProductsLabel)
	}
	
	public void navigateToCartPage() {
		WebUI.click(findTestObject('Page_Home/btn_Cart'))
		
		def allProductsLabel = findTestObject('Page_Cart/lbl_ShoppingCart')
		
		WebUI.waitForElementVisible(allProductsLabel, 10)

		WebUI.verifyElementVisible(allProductsLabel)
	}
	
	public void chooseItem(String name) {
		// Find the outline element
		WebElement product = DriverFactory.getWebDriver().findElement(
	        By.xpath(
	            "//div[@class='features_items']//div[contains(@class,'productinfo') and .//p[text()='${name}']]"
	        )
	    )
	
		// Get info of the product
	    String productName = product.findElement(By.tagName("p")).getText().trim()
	    String priceText = product.findElement(By.tagName("h2")).getText().trim()
	    BigDecimal price = new BigDecimal(priceText.replace("Rs. ", ""))
		
		// Close the advertisement first if it appears
		this.removeAds()
	
		// Click to add to cart button to add to cart
	    WebElement addToCartButton =
	        product.findElement(
	            By.cssSelector("a.add-to-cart")
	        )
	
	    addToCartButton.click()
		
		// Click continue shopping button to stay on home page
		def allProductsLabel = findTestObject('Page_Home/btn_ContinueShopping')
		
		WebUI.waitForElementVisible(allProductsLabel, 10)

		WebUI.click(allProductsLabel)
		
		WebUI.delay(3)
		
		// Update cart model
		def newItem = new CartItem(productName, price)
		this.cart.addItem(newItem)
		println(newItem)
	}
	
	public void scrollDownToRecommendedItems() {
		def recommendedItemsLabel = findTestObject('Page_Home/lbl_RecommendedItems')
		
		WebUI.scrollToElement(
			recommendedItemsLabel,
			10
		)
		
		WebUI.verifyElementVisible(recommendedItemsLabel)
	}
	
	public String selectFirstRecommendedItem() {
		WebUI.delay(3)
		this.removeAds()
		
		List<WebElement> recommendedItems = DriverFactory.getWebDriver().findElements(
	        By.cssSelector(
	            "#recommended-item-carousel .item.active .single-products"
	        )
	    )
		
		assert !recommendedItems.isEmpty() :
		"No recommended products found"
		 
		WebElement firstItem = recommendedItems[0]
		
		WebElement p = firstItem.findElement(By.tagName("p"))
		
		String productName = firstItem.findElement(By.tagName("p")).getText().trim()
		println("Product Name: ${productName}")
		String priceText = firstItem.findElement(By.tagName("h2")).getText().trim()
		println("Price Text: ${priceText}")
		BigDecimal price = new BigDecimal(priceText.replace("Rs. ", ""))

		WebElement addToCartButton = firstItem.findElement(By.cssSelector("a[class*=add-to-cart]"))
		
		addToCartButton.click()
		
		WebUI.waitForElementVisible(
	        findTestObject('Page_Home/btn_ViewCart'),
	        10
	    )
	
	    WebUI.click(
	        findTestObject('Page_Home/btn_ViewCart')
	    )
	
	    // Update cart model
		def newItem = new CartItem(productName, price)
		this.cart.addItem(newItem)
		println(newItem)
	}
	
	public void verifyLoggedInSuccessfully() {
		this.removeAds()
		
		String username = customer.getCustomerInfo().name
		
		def loggedInLabel = findTestObject('Page_Home/lbl_LoggedInAs')
		
		WebUI.waitForElementVisible(loggedInLabel, 10)
		
		String actualLabel = WebUI.getText(loggedInLabel).trim()

		assert !(actualLabel != "Logged in as ${username}") :
			"'Logged in as' label is not displayed"
	
		println("'Logged in as' label is displayed.")
	}
	
	public void deleteAccount() {
		def btnDeleteAccount = findTestObject('Page_Home/btn_DeleteAccount')
		
		WebUI.waitForElementVisible(btnDeleteAccount, 10)
		WebUI.click(btnDeleteAccount)
		
		WebUI.waitForElementVisible(findTestObject('Page_Home/lbl_AccountDeleted'), 10)
		WebUI.click(findTestObject('Page_Home/btn_Continue'))
	}
}
