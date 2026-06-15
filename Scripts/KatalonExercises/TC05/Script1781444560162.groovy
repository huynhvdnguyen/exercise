import pages.HomePage
import pages.CartPage
import pages.LoginPage
import pages.PaymentPage
import models.PaymentInfo
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

def homePage = new HomePage()
def cartPage = new CartPage()
def loginPage = new LoginPage()
def paymentPage = new PaymentPage()

try {
	WebUI.openBrowser('')
	WebUI.navigateToUrl('https://automationexercise.com')
	homePage.verifyPageLoadSuccessfully()
	homePage.chooseItem("Blue Top")
	homePage.navigateToCartPage()
	cartPage.proceedToCheckout(false)
	loginPage.signupNewCustomer(
		"John Doe",
	    "john${System.currentTimeMillis()}@mail.com",
	    "John",
	    "Doe",
	    "123 Main Street",
		"Canada",
	    "Toronto",
	    "Ontario",
	    "M5V1A1",
	    "1234567890"
	)
	homePage.verifyLoggedInSuccessfully()
	homePage.navigateToCartPage()
	cartPage.proceedToCheckout()
	cartPage.verifyDeliveryAndBillingAddress()
	cartPage.verifyItems()
	cartPage.addCommentAndPlaceOrder()
	paymentPage.completePaymentInfo(
		new PaymentInfo(
		    nameOnCard : "John Doe",
		    cardNumber : "4111111111111111",
		    cvc        : "123",
		    expiryMonth: "12",
		    expiryYear : "2030"
		)
	)
	paymentPage.downloadInvoiceAndVerify(System.getProperty("user.home") + File.separator + "Downloads", 10)
	homePage.deleteAccount()
} finally {
	WebUI.closeBrowser()
}