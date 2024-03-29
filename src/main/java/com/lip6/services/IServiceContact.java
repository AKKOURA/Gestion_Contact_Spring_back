package com.lip6.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lip6.entities.Address;
import com.lip6.entities.Contact;
import com.lip6.entities.ContactGroup;
import com.lip6.entities.PhoneNumber;

public interface IServiceContact {
	
	public ArrayList<Contact>  getContacts();
	public void createContact( String fname, String lname, String email);
	public boolean createContact(Contact contact);
	public boolean createGroupe(ContactGroup contactgroup);
	public boolean editContact(Contact contact);
	public boolean updateContact(Contact contact);
	public boolean deleteContact(int id);
	public boolean deleteGroupe(long id);
	public ArrayList<PhoneNumber> getPhonesByIdContact(Long idContact);
	public ArrayList<ContactGroup> getGroupesByIdContact(Long idContact);
	public ArrayList<PhoneNumber> getPhones();
	public ArrayList<ContactGroup> getGroupes();
	boolean updateGroupe(ContactGroup contactgroup);
	public  boolean addGroupToContact(long idContactGroup , long idContact);
	public boolean deleteGroupFromContact(long idContactGroup,long idContact);
	public ArrayList<ContactGroup> getGroupesForAddContact(long idContact);
	
	public ArrayList<Contact> getContactsForJoinGroup(long idContactGroup);
	public ArrayList<Contact> getContactsByGroup(long idContactGroup);
	
	public boolean addContactToGroup(long idContact,long idContactGroup);
	public boolean deleteContactFromGroup(long idContact,long idContactGroup);
	

}
