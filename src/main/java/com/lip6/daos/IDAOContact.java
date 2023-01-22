package com.lip6.daos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lip6.entities.Address;
import com.lip6.entities.Contact;
import com.lip6.entities.ContactGroup;
import com.lip6.entities.PhoneNumber;

public interface IDAOContact {

	
	public  boolean addContact( Contact contact);
	public  boolean updateContact(Contact contact);
	
	public  boolean addContact( String firstname, String lastname, String email);
	
	public boolean deleteContact(int id);
	
	public Contact getContact(long id);
	
	public boolean modifyContact(Contact contact);
	
	public ArrayList<Contact> getContactByFirstName(String firstname);
	
	public ArrayList<Contact> getContactByLastName(String lastname);
	
	public ArrayList<Contact> getContactByEmail(String email);

	public ArrayList<Contact> getContacts();
	
	public ArrayList<PhoneNumber> getPhonesByIdContact(Long idContact);
	public ArrayList<ContactGroup> getGroupesByIdContact(Long idContact);
	
	public ArrayList<PhoneNumber> getPhones();
	public ArrayList<ContactGroup> getGroupes();
	
	public boolean addGroupe(ContactGroup contactgroup);
	
	public Address getAdressByIdContact(Long idContact);
    public  boolean addGroupsToContact(Set<ContactGroup> contactgGroupes , long idContact);
	public boolean addPhonesToContact(Set<PhoneNumber> phones , long idContact);
	public boolean deleteGroupe(long id);
	public boolean updateGroupe(ContactGroup contactgroup);
	
}
