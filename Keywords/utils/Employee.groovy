package utils

import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.text.ParseException

public class Employee {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")
	
	private String name
	private String position
	private String office
	private Integer age
	private Date startDate
	private BigDecimal salaryInDollar
	
	Employee(String name, String position, String office, Integer age, String startDate, BigDecimal salaryInDollar) {
		this.name = name
		this.position = position
		this.office = office
		this.age = age
		try {
			this.startDate = FORMATTER.parse(startDate)
	    } catch (ParseException e) {
	        throw new IllegalArgumentException(
	            "Invalid startDate '${startDate}'. Expected format: dd/MM/yyyy",
	            e
	        )
	    }
		this.salaryInDollar = salaryInDollar
	}
	
	Employee(String name, String position, String office, Integer age, Date startDate, BigDecimal salaryInDollar) {
		this.name = name
		this.position = position
		this.office = office
		this.age = age
		this.startDate = startDate
		this.salaryInDollar = salaryInDollar
	}
}
