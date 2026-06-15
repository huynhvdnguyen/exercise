package pages

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.security.SecureRandom
import models.*

public class LoginPage extends CommonPage {
	static void login(String email, String password, Boolean isNegative = false) {
		WebUI.setText(
			findTestObject('Page_Login/txt_LoginEmail'),
			email
		)

		WebUI.setText(
			findTestObject('Page_Login/txt_LoginPassword'),
			password
		)

		WebUI.click(
			findTestObject('Page_Login/btn_Login')
		)
		
		if (isNegative) {
			WebUI.verifyElementText(
				findTestObject('Page_Login/lbl_LoginError'),
				'Your email or password is incorrect!'
			)
		}
	}
	
	public void signupNewCustomer(String name, String email,
                              String firstName, String lastName,
                              String address, String country, String state,
                              String city, String zipcode, String mobileNumber) {

	    WebUI.setText(findTestObject('Page_Login/txt_SignupUsername'), name)
	    WebUI.setText(findTestObject('Page_Login/txt_SignupEmail'), email)
	
	    WebUI.click(findTestObject('Page_Login/btn_Signup'))
	
	    WebUI.waitForElementVisible(findTestObject('Page_Login/txt_SignupPassword'), 10)
	
	    String password = generateRandomPassword()
		WebUI.setText(findTestObject('Page_Login/txt_SignupPassword'), password)
	    WebUI.setText(findTestObject('Page_Login/txt_FirstName'), firstName)
	    WebUI.setText(findTestObject('Page_Login/txt_LastName'), lastName)
	    WebUI.setText(findTestObject('Page_Login/txt_Address'), address)
	
	    chooseCountry(country)
	
	    WebUI.setText(findTestObject('Page_Login/txt_State'), state)
	    WebUI.setText(findTestObject('Page_Login/txt_City'), city)
	    WebUI.setText(findTestObject('Page_Login/txt_Zipcode'), zipcode)
	    WebUI.setText(findTestObject('Page_Login/txt_MobileNumber'), mobileNumber)
		
		WebUI.click(findTestObject('Page_Login/btn_CreateAccount'))
		
		WebUI.waitForElementVisible(findTestObject('Page_Login/lbl_AccountCreated'), 10)
		WebUI.waitForElementClickable(findTestObject('Page_Login/btn_Continue'), 10)
		WebUI.click(findTestObject('Page_Login/btn_Continue'))
	
	    // Update customer info to model
	    customer.update([
	        name: name,
	        email: email,
	        firstName: firstName,
	        lastName: lastName,
	        address: address,
	        country: country,
	        state: state,
	        city: city,
	        zipcode: zipcode,
	        phoneNumber: mobileNumber
	    ])
	}
	
	private void chooseCountry(String country) {
		WebUI.click(
			findTestObject('Page_Login/ddl_Country')
		)
		
		WebDriverWait wait = new WebDriverWait(
	        DriverFactory.getWebDriver(),
	        Duration.ofSeconds(10)
	    )
	
	    WebElement option = wait.until(
	        ExpectedConditions.elementToBeClickable(
	            By.xpath("//option[normalize-space()='${country}']")
	        )
	    )
	
	    option.click()
	}
	
	private static String generateRandomPassword(int length = 12) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*"
		SecureRandom random = new SecureRandom()
	
		(1..length)
			.collect { chars[random.nextInt(chars.length())] }
			.join()
	}
}
