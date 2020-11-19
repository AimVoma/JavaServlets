/**
 * 
 */
package com.wtc;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author aimilios
 *
 */
public class DbObjects {}


class Transaction {

	@JsonProperty private String name;
	@JsonProperty private String sname;
	@JsonProperty private String street;
	@JsonProperty private String email;
	@JsonProperty private String product;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT")
	private Date date;
	
	private boolean visible=true;
	
	public boolean getVisibility() {
		return visible;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}


class Order{
	@JsonProperty("user") private String user;
	@JsonProperty("product") private String product;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT")
	private Date date;
	
	public String getUser() {
		return user;
	}
	public String getProduct() {
		return product;
	}
	public Date getDate() {
		return date;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
	
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}


class Client {
	 @JsonProperty("firstName") private String firstName;
	 @JsonProperty("lastName") private String lastName;
	 @JsonProperty("email") private String email;
	 @JsonProperty("street") private String street;
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	 public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getStreet() {
		return street;
	}
	
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}