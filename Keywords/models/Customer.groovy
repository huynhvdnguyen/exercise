package models

@Singleton
public class Customer {
	
	private String name
	private String email
	private String lastName
	private String firstName
	private String country
	private String address
	private String state
	private String city
	private String zipcode
	private String phoneNumber
	
	void update(Map data) {
		this.name = data.name
		this.email = data.email
		this.firstName = data.firstName
		this.lastName = data.lastName
		this.address = data.address
		this.country = data.country
		this.state = data.state
		this.city = data.city
		this.zipcode = data.zipcode
		this.phoneNumber = data.phoneNumber
	}
	
	Map getCustomerInfo() {
		[
			name       : name,
			email      : email,
			firstName  : firstName,
			lastName   : lastName,
			address    : address,
			country    : country,
			state      : state,
			city       : city,
			zipcode    : zipcode,
			phoneNumber: phoneNumber
		]
	}
}
