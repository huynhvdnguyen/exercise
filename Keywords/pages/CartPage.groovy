package pages

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import models.*

public class CartPage extends CommonPage {
	
	static String getTextByHeader(String header, int rowIndex) {
		String text = DriverFactory.getWebDriver().findElement(By.xpath("//tr[@id][${rowIndex + 1}]//td[count(//thead//td[text()='${header}']/preceding-sibling::td) + 1]")).getText().trim()
		return text
	}
	
	static List<CartItem> getCartItems() {
		WebUI.delay(3)
		
		List<CartItem> items = []
		
		List<WebElement> rows = DriverFactory.getWebDriver().findElements(By.cssSelector("#cart_info_table tbody tr[id], #cart_info tbody tr[id]"))
		
		for (int i = 0; i < rows.size(); i++) {
			String name = this.getTextByHeader("Description", i)
			String price = this.getTextByHeader("Price", i)
			String quantity = this.getTextByHeader("Quantity", i)
			
			items.add(
				new CartItem(
					name.split('\n')[0].trim(),
					new BigDecimal(price.replace("Rs.", "").trim()),
					Integer.parseInt(quantity)
				)
			)
		}
		
		return items
	}
	
	public void verifyItems() {

	    List<CartItem> expectedItems = cart.getAllItems()
	
	    List<CartItem> actualItems = getCartItems()
		
		println(actualItems)
	
	    List<String> failures = []
	
		println("ExpectedItems: ${expectedItems}")
		println("ActualItems: ${actualItems}")
	
	    expectedItems.each { expectedItem ->
	
	        CartItem actualItem = actualItems.find { item ->
	            item.name == expectedItem.name
	        }
	
	        if (actualItem == null) {
	
	            failures.add(
	                "Missing item: ${expectedItem.name}"
	            )
	
	            return
	        }
	
	        if (actualItem.price.compareTo(expectedItem.price) != 0) {
	
	            failures.add(
	                "Price mismatch for '${expectedItem.name}'. " +
	                "Expected=${expectedItem.price}, Actual=${actualItem.price}"
	            )
	        }
	
	        if (actualItem.quantity != expectedItem.quantity) {
	
	            failures.add(
	                "Quantity mismatch for '${expectedItem.name}'. " +
	                "Expected=${expectedItem.quantity}, Actual=${actualItem.quantity}"
	            )
	        }
	    }
	
	    actualItems.each { actualItem ->
	
	        if (!expectedItems.any { item -> 
	            item.name == actualItem.name
	        }) {
	
	            failures.add(
	                "Unexpected item found: ${actualItem.name}"
	            )
	        }
	    }
	
	    if (!failures.isEmpty()) {
	
	        throw new AssertionError(
	            """
					Cart verification failed:
					
					${failures.join('\n')}
				"""
	        )
	    }
	
	    println("Cart items verified successfully.")
	}
	
	public void proceedToCheckout(Boolean isLogin = true) {
		WebUI.click(findTestObject('Page_Cart/btn_ProceedToCheckout'))
		
		if (!isLogin) {
			def btnRegisterLogin = findTestObject('Page_Cart/btn_RegisterLogin')
			
			WebUI.waitForElementVisible(btnRegisterLogin, 10)
	
			WebUI.click(btnRegisterLogin)
			
			WebUI.waitForElementVisible(findTestObject('Page_Login/lbl_LoginToYourAccount'), 10)
		}
	}
	
	Map getDeliveryAddressInfo() {
		
		String fullName = WebUI.getText(
			findTestObject('Page_Cart/txt_DeliveryAddressFirstLastName')
		).replace('.', '').trim()
	
		List<String> names = fullName.split('\\s+')
	
		String cityStateZip = WebUI.getText(
			findTestObject('Page_Cart/txt_DeliveryAddressCityStatePostcode')
		).trim()
	
		List<String> lines = cityStateZip.split('\\s+')
	
		String city = ''
		String state = ''
		String zipcode = ''
	
		if (lines.size() >= 2) {
			city = lines[0].trim()
			state = lines[1].trim()
			zipcode = lines[2].trim()
		}
	
		return [
			firstName : names[0],
			lastName  : names.size() > 1 ? names[-1] : '',
			address   : WebUI.getText(
							findTestObject('Page_Cart/txt_DeliveryAddress')
						).trim(),
			city      : city,
			state     : state,
			zipcode   : zipcode,
			country   : WebUI.getText(
							findTestObject('Page_Cart/txt_DeliveryAddressCountryName')
						).trim(),
			phoneNumber : WebUI.getText(
							findTestObject('Page_Cart/txt_DeliveryPhone')
						  ).trim()
		]
	}
	
	Map getBillingAddressInfo() {
		
		String fullName = WebUI.getText(
			findTestObject('Page_Cart/txt_BillingAddressFirstLastName')
		).replace('.', '').trim()
	
		List<String> names = fullName.split('\\s+')
	
		String cityStateZip = WebUI.getText(
			findTestObject('Page_Cart/txt_BillingAddressCityStatePostcode')
		).trim()
	
		List<String> lines = cityStateZip.split('\\s+')
	
		String city = ''
		String state = ''
		String zipcode = ''
	
		if (lines.size() >= 2) {
			city = lines[0].trim()
			state = lines[1].trim()
			zipcode = lines[2].trim()
		}
	
		return [
			firstName : names[0],
			lastName  : names.size() > 1 ? names[-1] : '',
			address   : WebUI.getText(
							findTestObject('Page_Cart/txt_BillingAddress')
						).trim(),
			city      : city,
			state     : state,
			zipcode   : zipcode,
			country   : WebUI.getText(
							findTestObject('Page_Cart/txt_BillingAddressCountryName')
						).trim(),
			phoneNumber : WebUI.getText(
							findTestObject('Page_Cart/txt_BillingPhone')
						  ).trim()
		]
	}
	
	void verifyDeliveryAndBillingAddress() {
		
		Map expected = customer.getCustomerInfo()
		Map actualDeliveryInfo = getDeliveryAddressInfo()
		Map acutalBillingInfo = getBillingAddressInfo()
	
		List<String> failures = []
	
		[
			firstName   : 'First Name',
			lastName    : 'Last Name',
			address     : 'Address',
			city        : 'City',
			state       : 'State',
			zipcode     : 'Zip Code',
			country     : 'Country',
			phoneNumber : 'Phone Number'
		].each { key, label ->
	
			String expectedValue = expected[key]?.trim()
			String actualDeliveryValue = actualDeliveryInfo[key]?.trim()
			String actualBillingValue = acutalBillingInfo[key]?.trim()
	
			if (expectedValue != actualDeliveryValue) {
				failures.add(
					"${label} mismatch. Expected='${expectedValue}', Actual='${actualDeliveryValue}'"
				)
			}
			
			if (expectedValue != actualBillingValue) {
				failures.add(
					"${label} mismatch. Expected='${expectedValue}', Actual='${actualBillingValue}'"
				)
			}
		}
	
		if (!failures.isEmpty()) {
			throw new AssertionError("""
				Delivery address verification failed:
				
				${failures.join('\n')}
			""")
		}
	
		println('Delivery address verified successfully.')
	}
	
	public void addCommentAndPlaceOrder() {
		WebUI.setText(findTestObject("Page_Cart/txa_Message"), "OK")
		WebUI.click(findTestObject("Page_Cart/btn_PlaceOrder"))
//		if (WebUI.waitForAlert(5, FailureHandling.OPTIONAL)) {
//			String alertText = WebUI.getAlertText()
//		
//			println("Alert displayed: ${alertText}")
//		
//			WebUI.acceptAlert()
//		}
		WebUI.executeJavaScript("""
		    document.querySelectorAll(
		        '.modal, .popup, .overlay, .adsbygoogle'
		    ).forEach(e => e.remove());
		""", null)
	}
}
