import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import utils.ExcelHelper
import utils.Employee
import utils.EmployeeReader
import utils.JsonHelper

import java.text.SimpleDateFormat

import groovy.json.JsonOutput
import java.nio.file.Files
import java.nio.file.Paths

// Read through excel file to get employees data and exchange rate

ExcelHelper excel = new ExcelHelper("Data.xlsx", "exchange rate")

BigDecimal usd = new BigDecimal(excel.getCellValue(1, 0).toString())
BigDecimal vnd = new BigDecimal(excel.getCellValue(1, 1).toString())
BigDecimal exchangeRate = vnd.divide(usd)

List<Employee> employees = EmployeeReader.readEmployeesFromExcel("Data.xlsx", "employee")

// Q1: Salary of Bradley

println("SALARY OF BRADLEY")
Employee bradley = employees.find {emp -> emp.name.contains("Bradley")}
BigDecimal salaryVnd = bradley.salaryInDollar.multiply(exchangeRate)

println("Name: ${bradley.name}")
println("USD: ${bradley.salaryInDollar}")
println("VND: ${salaryVnd}")

// Q2: People with Salary > $400

println("EMPLOYEE WITH SALARY > 400")
List<Employee> richEmployees  = employees.findAll {emp -> emp.salaryInDollar > 400}
richEmployees.each { emp -> println("${emp.name} - ${emp.salaryInDollar}") }

// Q3: First 3 people with office in Tokyo

println("FIRST 3 EMPLOYEE WITH OFFICE IN TOKYO")
List<Employee> tokyoEmployees = employees.findAll {emp -> emp.office == "Tokyo"}
for (int i = 0; i < 3; i++) {
	Employee tokyoEmployee = tokyoEmployees.getAt(i)
	println("${tokyoEmployee.name} - ${tokyoEmployee.office}")
}

// Q4: People <= 40 years old

println("EMPLOYEE <= 40 YEARS OLD")
List<Employee> youngEmployees = employees.findAll {emp -> emp.age <= 40}
youngEmployees.each { emp -> println("${emp.name} - ${emp.age}") }

// Q5: People with the number 3 in their age

println("EMPLOYEE WITH THE NUMBER 3 IN THEIR AGE")
List<Employee> threeEmployees = employees.findAll {emp -> emp.age != null && emp.age.toString().contains("3")}
threeEmployees.each { emp -> println("${emp.name} - ${emp.age}") }

// Q6: People with start date from 1/1/2011 onwards

println("EMPLOYEE WITH START DATE FROM 1/1/2011 ONWARDS")
Date cutoffDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2011")
List<Employee> cutoffEmployees = employees.findAll {emp -> emp.startDate >= cutoffDate}
cutoffEmployees.each { emp -> println("${emp.name} - ${new SimpleDateFormat("dd/MM/yyyy").format(emp.startDate)}") }

// Q7: People with position as Accountant or Software Engineer and salary < 5 million VND

println("EMPLOYEE WITH POSITION AS ACCOUNTANT OR SOFTWARE ENIGNEEER AND SALARY < 5 MILLION VND")
List<Employee> filteredEmployees = employees.findAll {emp -> (emp.position == "Accountant" || emp.position == "Software Engineer") && emp.salaryInDollar.multiply(exchangeRate) < 5000000}
filteredEmployees.each { emp -> println("${emp.name} - ${emp.position} - ${emp.salaryInDollar.multiply(exchangeRate)} VND") }


// Q8: Write all data to JSON file

println("WRITE TO JSON FILE")
JsonHelper.exportEmployees(
    employees,
    "Output/employees.json"
)

JsonHelper.exportUsdVnd(
    "Output/exchangeRate.json",
	usd,
	vnd
)
