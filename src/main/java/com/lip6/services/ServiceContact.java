package com.lip6.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lip6.daos.IDAOContact;
import com.lip6.entities.Contact;

@Service
public class ServiceContact implements IServiceContact{
	//déclaration d'injection de dépendance par annotation
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
			System.out.println("Contact ajouté!");
		else
			System.out.println("Contact non ajouté!");		
	}

	@Override
	public boolean createContact(Contact contact) {
		//Injection de dep de contact , aucun new !!
		boolean ok=cdao.addContact(contact);
		if (ok)
			System.out.println("Contact ajouté!");
		else
			System.out.println("Contact non ajouté!");
		
		return ok;
		
	}

	@Override
	public boolean deleteContact(int id) {
		return cdao.deleteContact(id);
	}

	@Override
	public boolean updateContact(Contact contact) {
		return cdao.modifyContact(contact.getIdContact(), contact.getFirstName(), contact.getLastName(), contact.getEmail());
	}

	

}
