package com.lip6.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lip6.daos.IDAOContact;
import com.lip6.entities.Address;
import com.lip6.entities.Contact;
import com.lip6.entities.ContactGroup;
import com.lip6.entities.PhoneNumber;

@Service
public class ServiceContact implements IServiceContact{
	//d�claration d'injection de d�pendance par annotation
	@Autowired 
	private IDAOContact cdao;
	
	@Override
	public ArrayList<Contact>  getContacts() {
	   return cdao.getContacts();
	}
	
	@Override
	public void createContact( String fname, String lname, String email) {

		boolean ok=cdao.addContact(fname, lname, email);
		if (ok)
			System.out.println("Contact ajout�!");
		else
			System.out.println("Contact non ajout�!");		
	}

	@Override
	public boolean createContact(Contact contact) {
		//Injection de dep de contact , aucun new !!
		boolean ok=cdao.addContact(contact);
		if (ok)
			System.out.println("Contact ajout�!");
		else
			System.out.println("Contact non ajout�!");
		
		return ok;
		
	}

	@Override
	public boolean deleteContact(int id) {
		return cdao.deleteContact(id);
	}

	@Override
	public boolean updateContact(Contact contact) {
		return cdao.modifyContact(contact);
	}

	
	@Override
	public boolean editContact(Contact contact) {
		// TODO Auto-generated method stub
		return cdao.updateContact(contact);
	}

	@Override
	public ArrayList<PhoneNumber> getPhonesByIdContact(Long idContact) {
		return cdao.getPhonesByIdContact(idContact);
	}

	@Override
	public ArrayList<ContactGroup> getGroupesByIdContact(Long idContact) {
		return cdao.getGroupesByIdContact(idContact);
	}

	@Override
	public ArrayList<PhoneNumber> getPhones() {
		return cdao.getPhones();
	}

	@Override
	public ArrayList<ContactGroup> getGroupes() {
		return cdao.getGroupes();
	}

	@Override
	public boolean addGroupToContact(long idContactGroup, long idContact) {
		return cdao.addGroupToContact(idContactGroup,  idContact);
	}

	@Override
	public boolean deleteGroupFromContact(long idContactGroup, long idContact) {
		return cdao.deleteGroupFromContact(idContactGroup, idContact);
	}

	@Override
	public boolean createGroupe(ContactGroup contactgroup) {
		boolean ok=cdao.addGroupe(contactgroup);
		if (ok)
			System.out.println("Groupe ajout�!");
		else
			System.out.println("Groupe non ajout�!");
		
		return ok;
	}
	@Override
	public boolean deleteGroupe(long id) {
		return cdao.deleteGroupe(id);
	}
	@Override
	public boolean updateGroupe(ContactGroup contactgroup) {
		return cdao.updateGroupe(contactgroup);
	}


	public ArrayList<ContactGroup> getGroupesForAddContact(long idContact) {
		return cdao.getGroupesForAddContact(idContact);
	}



}
