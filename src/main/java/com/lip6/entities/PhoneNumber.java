package com.lip6.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.stereotype.Component;


@Entity
public class PhoneNumber {

	private String phoneNumber;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idPhoneNumber;

	@ManyToOne
	@JoinColumn(name="id_contact")
	private Contact contact;

	public PhoneNumber(String phoneNumber, Contact contact) {
		super();
		this.phoneNumber = phoneNumber;
		this.contact = contact;
	}

	public PhoneNumber() {
		super();
	}

	public PhoneNumber(String phoneNumber2) {
		this.phoneNumber = phoneNumber2;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public PhoneNumber(Contact contact) {
		super();
		this.contact = contact;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getIdPhoneNumber() {
		return idPhoneNumber;
	}

	public void setIdPhoneNumber(long idPhoneNumber) {
		this.idPhoneNumber = idPhoneNumber;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	


	
}
