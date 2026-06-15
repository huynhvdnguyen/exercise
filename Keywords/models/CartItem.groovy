package models

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

public class CartItem {
	public String name
	public BigDecimal price
	public Integer quantity
	
	CartItem(String name, String price) {
		this.name = name
		this.price = new BigDecimal(price)
		this.quantity = 1
	}
	
	CartItem(String name, BigDecimal price) {
		this.name = name
		this.price = price
		this.quantity = 1
	}
	
	CartItem(String name, BigDecimal price, Integer quantity) {
		this.name = name
		this.price = price
		this.quantity = quantity
	}

	String toString() {
		return "CartItem(name=${name}, price=${price}, quantity=${quantity})"
	}
}
