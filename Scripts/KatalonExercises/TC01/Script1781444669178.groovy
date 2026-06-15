import pages.HomePage
import pages.LoginPage
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

def homePage = new HomePage()
def loginPage = new LoginPage()

try {
	WebUI.openBrowser('')
	WebUI.navigateToUrl('https://automationexercise.com')
	homePage.verifyPageLoadSuccessfully()
	homePage.navigateToLoginPage()
	loginPage.login("newuser@gmail.com", "wrongpassword123", true)
} finally {
	WebUI.closeBrowser()
}