package com.lip6.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


@Entity

public class ContactGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idContactGroup;
	//private String contact;

	@ManyToMany(mappedBy="contactGroups")
	private Set <Contact> contacts=new HashSet<Contact>();
	
	public ContactGroup() {
		super();
	}

	public ContactGroup(Set<Contact> contacts) {
		super();
		this.contacts = contacts;
	}

	public long getIdContactGroup() {
		return idContactGroup;
	}

	public void setIdContactGroup(long idContactGroup) {
		this.idContactGroup = idContactGroup;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	
}
