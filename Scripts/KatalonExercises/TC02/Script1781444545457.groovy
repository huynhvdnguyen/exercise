import pages.HomePage
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

def homePage = new HomePage()

try {
	WebUI.openBrowser('')
	WebUI.navigateToUrl('https://automationexercise.com')
	homePage.verifyPageLoadSuccessfully()
	homePage.navigateToTestCasesPage()
} finally {
	WebUI.closeBrowser()
}