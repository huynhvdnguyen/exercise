package pages

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import models.PaymentInfo

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

public class PaymentPage extends CommonPage {
	
	public void verifyPageLoadSuccessfully() {
		WebUI.verifyElementPresent(findTestObject("Page_Payment/lbl_PaymentTitle"), 20)
	}
	
	public void completePaymentInfo(PaymentInfo paymentInfo) {
		
		WebUI.delay(3)
		this.removeAds()
		WebUI.delay(3)
		
		assert paymentInfo != null : "Payment information cannot be null"
	
		WebUI.setText(
			findTestObject('Page_Payment/txt_NameOnCard'),
			paymentInfo.nameOnCard
		)
	
		WebUI.setText(
			findTestObject('Page_Payment/txt_CardNumber'),
			paymentInfo.cardNumber
		)
	
		WebUI.setText(
			findTestObject('Page_Payment/txt_Cvc'),
			paymentInfo.cvc
		)
	
		WebUI.setText(
			findTestObject('Page_Payment/txt_ExpiryMonth'),
			paymentInfo.expiryMonth
		)
	
		WebUI.setText(
			findTestObject('Page_Payment/txt_ExpiryYear'),
			paymentInfo.expiryYear
		)
		
		WebUI.click(findTestObject('Page_Payment/btn_PayAndConfirmOrder'))
		
//		WebUI.verifyElementPresent(findTestObject("Page_Payment/lbl_SuccessPayOrder"), 10)
	
		println("Payment information entered successfully.")
	}
	
	public File waitForNewFile(File downloadDir, Set<String> existingFiles, int timeoutSeconds) {
	
		long endTime = System.currentTimeMillis() + timeoutSeconds * 1000
	
		while (System.currentTimeMillis() < endTime) {
	
			File newFile = downloadDir.listFiles()?.find { file ->
	
				!existingFiles.contains(file.name) &&
				!file.name.endsWith(".crdownload") &&
				!file.name.endsWith(".tmp")
			}
	
			if (newFile != null) {
				return newFile
			}
	
			WebUI.delay(1)
		}
	
		return null
	}
	
	void downloadInvoiceAndVerify(String downloadFolder, int timeoutSeconds = 15) {
		
		File downloadDir = new File(downloadFolder)
		println(downloadDir)
	
		assert downloadDir.exists() :
			"Download directory does not exist: ${downloadFolder}"
	
		// Capture existing files before download
		Set<String> existingFiles = downloadDir.listFiles()*.name as Set
	
		WebUI.click(findTestObject('Page_Payment/btn_DownloadInvoice'))
	
		File downloadedFile = waitForNewFile(
			downloadDir,
			existingFiles,
			timeoutSeconds
		)
	
		assert downloadedFile != null :
			"Invoice file was not downloaded within ${timeoutSeconds} seconds."
	
		assert downloadedFile.length() > 0 :
			"Downloaded invoice file is empty."
	
		println("Invoice downloaded successfully: ${downloadedFile.name}")
		
		WebUI.click(findTestObject('Page_Payment/btn_Continue'))
	}
}	
