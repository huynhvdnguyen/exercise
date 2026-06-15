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
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import org.openqa.selenium.WebElement

public class ProductsPage extends CommonPage {
	static void searchProduct(String name) {
		WebUI.setText(
			findTestObject('Page_Products/txt_SearchBox'),
			name
		)
		
		WebUI.click(
			findTestObject('Page_Products/btn_SubmitSearch')
		)
		
		List<WebElement> products = WebUiCommonHelper.findWebElements(
            findTestObject('Page_Products/txt_ProductName'),
            10
        )
		
		assert products.size() == 1 :
		"Expected 1 product but found ${products.size()}"

		String actualProductName = products[0].getText().trim()
		
		assert actualProductName.equalsIgnoreCase(name) :
		"Expected '${name}' but found '${actualProductName}'"
	}
}
