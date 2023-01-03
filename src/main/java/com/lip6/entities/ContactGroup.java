package com.lip6.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.stereotype.Component;


@Entity
public class ContactGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idContactGroup;
	private String label;

	@ManyToMany(mappedBy="contactGroups")
	private Set <Contact> contacts=new HashSet<Contact>();
	

	public ContactGroup(String label) {
		super();
		this.label = label;
	}


	public ContactGroup() {
		super();
	}


	public ContactGroup(String label, Set<Contact> contacts) {
		super();
		this.label = label;
		this.contacts = contacts;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
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
