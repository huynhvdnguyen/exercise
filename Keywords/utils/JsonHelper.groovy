package utils

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

import internal.GlobalVariable

import groovy.json.JsonOutput
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat

public class JsonHelper {
	/**
	 * Export any list of objects (e.g., Employee list) to JSON file
	 */
	static void exportToJson(List data, String filePath) {

		String json = JsonOutput.prettyPrint(
			JsonOutput.toJson(data)
		)

		writeFile(filePath, json)
	}
	
	/**
	 * Export Employee list with formatted date
	 */
	static void exportEmployees(List employees, String filePath) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")

		def exportData = employees.collect { emp ->
			return [
				name          : emp.name,
				position      : emp.position,
				office        : emp.office,
				age           : emp.age,
				startDate     : emp.startDate ? sdf.format(emp.startDate) : null,
				salaryInDollar: emp.salaryInDollar
			]
		}

		String json = JsonOutput.prettyPrint(
			JsonOutput.toJson(exportData)
		)

		writeFile(filePath, json)
	}
	
	static void exportUsdVnd(String filePath, BigDecimal usd, BigDecimal vnd) {
		
		def data = [
			USD: usd,
			VND: vnd
		]
	
		String json = JsonOutput.prettyPrint(
			JsonOutput.toJson(data)
		)
	
		writeFile(filePath, json)
	}

	/**
	 * Internal file writer
	 */
	private static void writeFile(String filePath, String content) {

		def path = Paths.get(filePath)

		Files.createDirectories(path.parent)

		Files.write(path, content.getBytes("UTF-8"))

		println "JSON exported to: ${filePath}"
	}
}
