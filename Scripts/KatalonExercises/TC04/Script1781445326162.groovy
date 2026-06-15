import pages.HomePage
import pages.CartPage
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

def homePage = new HomePage()
def cartPage = new CartPage()

try {
	WebUI.openBrowser('')
	WebUI.navigateToUrl('https://automationexercise.com')
	homePage.verifyPageLoadSuccessfully()
	homePage.scrollDownToRecommendedItems()
	homePage.selectFirstRecommendedItem()
	cartPage.verifyItems()
} finally {
	WebUI.closeBrowser()
}