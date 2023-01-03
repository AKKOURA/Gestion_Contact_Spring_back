package com.lip6.daos;

import java.util.ArrayList;
import java.util.List;

import com.lip6.entities.Contact;
import com.lip6.entities.ContactGroup;
import com.lip6.entities.PhoneNumber;

public interface IDAOContact {

	
	public  boolean addContact( Contact contact);
	public  boolean updateContact(Contact contact);
	
	public  boolean addContact( String firstname, String lastname, String email);
	
	public boolean deleteContact(int id);
	
	public Contact getContact(long id);
	
	public boolean modifyContact(long id, String firstname, String lastname, String email);
	
	public ArrayList<Contact> getContactByFirstName(String firstname);
	
	public ArrayList<Contact> getContactByLastName(String lastname);
	
	public ArrayList<Contact> getContactByEmail(String email);

	public ArrayList<Contact> getContacts();
	
	public ArrayList<PhoneNumber> getPhonesByIdContact(int idContact);
	public ArrayList<ContactGroup> getGroupesByIdContact(int idContact);
	
	public ArrayList<PhoneNumber> getPhones();
	public ArrayList<ContactGroup> getGroupes();
	
}
