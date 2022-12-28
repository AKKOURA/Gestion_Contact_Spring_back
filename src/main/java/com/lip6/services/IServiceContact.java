package com.lip6.services;

import java.util.ArrayList;
import java.util.List;

import com.lip6.entities.Contact;

public interface IServiceContact {
	
	public ArrayList<Contact>  getContacts();
	public void createContact( String fname, String lname, String email);
	public boolean createContact(Contact contact);
	public boolean editContact(Contact contact);
	public boolean updateContact(Contact contact);
	public boolean deleteContact(int id);


}
