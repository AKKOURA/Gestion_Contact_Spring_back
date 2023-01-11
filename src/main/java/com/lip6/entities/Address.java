package com.lip6.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.stereotype.Component;

@Entity
public class Address {

	private String address;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idAddress;
	
	public long getIdAddress() {
		return idAddress;
	}

	public void setIdAddress(long idAddress) {
		this.idAddress = idAddress;
	}

	public Address(String address, Contact contact) {
		super();
		this.address = address;
		this.contact = contact;
	}

	public Address() {
		super();
	}

	@OneToOne(mappedBy="address")
	private Contact contact;

	public Address(Contact contact) {
		super();
		this.contact = contact;
	}

	public Address(String address) {
		this.address = address;
	}

	public Contact getContact() {
		return contact;
	}
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	
}
