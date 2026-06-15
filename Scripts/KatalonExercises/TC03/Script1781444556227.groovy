import pages.HomePage
import pages.ProductsPage
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

def homePage = new HomePage()
def productsPage = new ProductsPage()

try {
	WebUI.openBrowser('')
	WebUI.navigateToUrl('https://automationexercise.com')
	homePage.verifyPageLoadSuccessfully()
	homePage.navigateToProductsPage()
	productsPage.searchProduct("Blue Top")
} catch (e) {
	WebUI.closeBrowser()
}