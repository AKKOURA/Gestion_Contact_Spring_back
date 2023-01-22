	package com.lip6.daos;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lip6.entities.Address;
import com.lip6.entities.Contact;
import com.lip6.entities.ContactGroup;
import com.lip6.entities.Messages;
import com.lip6.entities.PhoneNumber;
import com.lip6.util.JpaUtil;

@Repository
public class DAOContact implements IDAOContact {

	   //création par setter
		@Autowired 
		private Contact contact1;
		
		//création par constructeur
		@Autowired 
		private Contact contact2;
		
	//réation de contact par constructeur ou setter
	@Override
	public boolean addContact(Contact contact) {
		//1: obtenir une connexion et un EntityManager, en passant par la classe JpaUtil
		EntityManager em=JpaUtil.getEmf().createEntityManager();
		// 3 : Ouverture transaction
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		// 4 : Persistance Objet/Relationnel : création d'un enregistrement en base, 
		//lorsque les assocations seront présentes alors on persist que le contact)
		if(em.find(Contact.class, contact1.getIdContact()) == null && em.find(Contact.class, contact2.getIdContact()) == null) {
			em.persist(contact1);
			em.persist(contact2);
		}
	     
		em.persist(contact);
		contact.getPhones().forEach(p -> p.setContact(contact));
	   
		// 5 : Fermeture transaction
		tx.commit();
		// 6 : Fermeture de l'EntityManager et de unité de travail JPA
		em.close();

		return true;
	}


	/**
	 * Rajoute un contact dans la base de donnees.
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @return renvoit le nouveau contact
	 */
	//création de contact via le formulaire
	@Transactional
	@Override
	public  boolean addContact(String firstname, String lastname, String email) {

		//1: obtenir une connexion et un EntityManager, en passant par la classe JpaUtil
		EntityManager em=JpaUtil.getEmf().createEntityManager();
		// 2 : Instanciation Objet métier
		Contact contact = new Contact(firstname , lastname,email);
		PhoneNumber phoneNumber1 = new PhoneNumber("0664252545"); 
		PhoneNumber phoneNumber2 = new PhoneNumber("0664252545");
	    ContactGroup contactGroup1 = new ContactGroup();
	    ContactGroup contactGroup2 = new ContactGroup();
		Address address = new Address("Rue de la Cerisaie");
		
		contact.setAddress(address);
		address.setContact(contact);

		phoneNumber1.setContact(contact);
		phoneNumber2.setContact(contact);
			
		contact.getPhones().add(phoneNumber1);
		contact.getPhones().add(phoneNumber2);
		
		contact.getContactGroups().add(contactGroup1);
		contactGroup2.getContacts().add(contact);
		
		//PhoneNumber PhoneNumber3 = em.find(PhoneNumber.class, 2);
		//TESTER UN MERGE 
		
		// 3 : Ouverture transaction
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		// 4 : Persistance Objet/Relationnel : création d'un enregistrement en base, 
		//lorsque les assocations seront présentes alors on persist que le contact
		em.persist(contact);
		//em.persist(phoneNumber);
		//em.persist(contactGroup);
		//em.persist(address);
		// L’entité étant en mode managée, pas besoin de refaire un persist
		// 5 : Fermeture transaction
		tx.commit();
		// 6 : Fermeture de l'EntityManager et de unité de travail JPA
		em.close();

		return true;
	}

	/**
	 * Suppresion d'un contact a partir de son identifiant
	 * 
	 * @param id
	 * @return vrai si la suppression a bien ete effectuee
	 */
	@Override
	public boolean deleteContact(int id) {
		boolean success = false;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			String request1 = "DELETE FROM phonenumber WHERE id_contact = " + id;
			stmt.executeUpdate(request1);
			String request2 = "DELETE FROM ctc_grp WHERE ctc_id = " + id;
			stmt.executeUpdate(request2);
			String request3 = "DELETE FROM contact WHERE idContact = " + id;
			 stmt.executeUpdate(request3);
			 success = true;
			stmt.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Recuperation d'un contact a partir de son identifiant
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Contact getContact(long id) {
		ResultSet rec = null;
		Contact contact = null;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contact WHERE idContact = " + id);

			if (rec.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
				    contact = new Contact();
					contact.setIdContact(Long.parseLong(rec.getString("idContact")));
					contact.setFirstName(rec.getString("firstName"));
					contact.setLastName(rec.getString("lastName"));
					contact.setEmail(rec.getString("email"));
				} while (rec.next());
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contact;
	}

	/**
	 * Methode qui modifie les coordonees d'un contact
	 * 
	 * @param id
	 * @param firstname
	 * @param alstname
	 * @param email
	 * @return
	 */
	@Override
	public boolean modifyContact(Contact contact) {
		boolean success = false;
		Connection con = null;
		ArrayList<String> idContactGroups = new ArrayList<String>() ;

		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));

			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();
			Statement stmt5 = con.createStatement();
			String sqlFirstName = "UPDATE contact SET firstName = " + "'" + contact.getFirstName() + "'" + " WHERE idContact = " + contact.getIdContact();
			String sqlLastName = "UPDATE contact SET lastName = " + "'" + contact.getLastName() + "'" + " WHERE idContact = " +  contact.getIdContact();
			String sqlEmail = "UPDATE contact SET email = " + "'" + contact.getEmail() + "'" + " WHERE idContact = " +  contact.getIdContact();
			String sqlAdr = "Update address Set address =" + "'" + contact.getAddress().getAddress() + "'" + " WHERE idAddress = " +  contact.getAddress().getIdAddress();
			//suppression de tout les phones
			String sqlDeleteAllPhones = "DELETE FROM phonenumber WHERE id_contact = "  +contact.getIdContact();
			stmt.executeUpdate(sqlDeleteAllPhones);
			
			//suppression de tout les contactGroups
			String sqlDeleteAllGroupsAsso = "DELETE FROM ctc_grp WHERE ctc_id = " + contact.getIdContact();
			stmt.executeUpdate(sqlDeleteAllGroupsAsso);
			
			// Ajouter les  nv phones ou groupes s'ils existent 
			if(contact.getPhones().isEmpty() == false) {
			   for (PhoneNumber phone : contact.getPhones()) {
				String insertInto = "INSERT INTO phonenumber (`phoneNumber`,`id_contact`) values ("+ "'" +phone.getPhoneNumber()+ "'," + contact.getIdContact()+")  ";
				stmt3.executeUpdate(insertInto);
			  }
			}
			stmt3.close();
			
				
			if (contact.getFirstName() != "")
				stmt.executeUpdate(sqlFirstName);
			if (contact.getLastName() != "")
				stmt.executeUpdate(sqlLastName);
			if (contact.getEmail() != "")
				stmt.executeUpdate(sqlEmail);
			if (contact.getAddress() != null)
			  stmt.executeUpdate(sqlAdr);
			stmt.close();
			con.close();
			success = true;
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * Renvoit la liste des contacts correspondant au prenom firrstname
	 * 
	 * @param firstname
	 * @return
	 */
	@Override
	public ArrayList<Contact> getContactByFirstName(String firstname) {

		ArrayList<Contact> contacts = new ArrayList<Contact>();

		ResultSet rec = null;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contact WHERE firstName = " + "'" + firstname + "'");

			while (rec.next()) {
				Contact contact = new Contact();
				contact.setIdContact(Long.parseLong(rec.getString("idContact")));
				contact.setFirstName(rec.getString("firstName"));
				contact.setLastName(rec.getString("lastName"));
				contact.setEmail(rec.getString("email"));
				contacts.add(contact);

				contacts.add(contact);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}

	/**
	 * Renvoit la liste des contacts correspondant au nom lastname
	 * 
	 * @param lastname
	 * @return
	 */
	@Override
	public ArrayList<Contact> getContactByLastName(String lastname) {

		ArrayList<Contact> contacts = new ArrayList<Contact>();

		ResultSet rec = null;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contact WHERE lastName = " + "'" + lastname + "'");

			while (rec.next()) {
				Contact contact = new Contact();
				contact.setIdContact(Long.parseLong(rec.getString("idContact")));
				contact.setFirstName(rec.getString("firstName"));
				contact.setLastName(rec.getString("lastName"));
				contact.setEmail(rec.getString("email"));
				contacts.add(contact);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}

	/**
	 * Renvoit la liste des contacts correspondant a l'email email
	 * 
	 * @param email
	 * @return
	 */
	@Override
	public ArrayList<Contact> getContactByEmail(String email) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();

		ResultSet rec = null;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contact WHERE email = " + "'" + email + "'");

			while (rec.next()) {
				Contact contact = new Contact();
				contact.setIdContact(Long.parseLong(rec.getString("idContact")));
				contact.setFirstName(rec.getString("firstName"));
				contact.setLastName(rec.getString("lastName"));
				contact.setEmail(rec.getString("email"));
				contacts.add(contact);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}


	@Override
	public ArrayList<Contact> getContacts() {
		ArrayList<Contact> contacts = new ArrayList<Contact>();

		ResultSet rec = null;
		Connection con = null;
		try {
			EntityManager em=JpaUtil.getEmf().createEntityManager();
			
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contact");
			while (rec.next()) {
				Contact contact = new Contact();
				Address adr = getAdressByIdContact(Long.parseLong(rec.getString("idContact")));
				ArrayList<PhoneNumber> phones = getPhonesByIdContact(Long.parseLong(rec.getString("idContact")));
				contact.setIdContact(Long.parseLong(rec.getString("idContact")));
				contact.setFirstName(rec.getString("firstName"));
				contact.setLastName(rec.getString("lastName"));
				contact.setEmail(rec.getString("email"));
				contact.setAddress(adr);
		
				contacts.add(contact);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
		
	}


	@Override
	public boolean updateContact(Contact contact) {
		//1: obtenir une connexion et un EntityManager, en passant par la classe JpaUtil
				EntityManager em=JpaUtil.getEmf().createEntityManager();
				EntityTransaction tx = em.getTransaction();
				Contact previousContact = em.find(Contact.class, contact.getIdContact());
				previousContact.setFirstName(contact.getFirstName());
				previousContact.setLastName(contact.getLastName());
				previousContact.setEmail(contact.getEmail());
				previousContact.setAddress(contact.getAddress());
				tx.commit();
				em.close();

				return true;
	}
	@Override
	public boolean addGroupsToContact(Set<ContactGroup> contactgGroupes , long idContact) {
		boolean success= false;
		
           if(contactgGroupes.isEmpty() == false) {
        		//Ajouter les nouveaux ContactGroup au contact
   			EntityManager em=JpaUtil.getEmf().createEntityManager();
   			em.getTransaction().begin();
   			Contact previousContact = em.find(Contact.class,idContact);
   			
   		    for(ContactGroup groupe : contactgGroupes) {
   		    	previousContact.getContactGroups().removeIf(g -> g.getLabel().equalsIgnoreCase(groupe.getLabel()));
   		    }
   		    previousContact.getContactGroups().clear();
   			previousContact.setContactGroups(contactgGroupes);
   			//previousContact.getContactGroups().addAll(contactgGroupes);

   			em.getTransaction().commit();
   			
   			em.close();
           }
		
			success = true;

		return success;
	}
	
	@Override
	public boolean addPhonesToContact(Set<PhoneNumber> phones , long idContact) {
		boolean success= false;
		EntityManager em=JpaUtil.getEmf().createEntityManager();

			if(phones.isEmpty() == false) {
				//Ajouter les nouveaux phones au contact
				Contact previousContact = em.find(Contact.class,idContact);
				em.getTransaction().begin();
				previousContact.getPhones().addAll(phones);
				em.getTransaction().commit();
				em.close();
			}
			
			
			success = true;
			

	return success;
		
	}
	@Override
	public ArrayList<PhoneNumber> getPhonesByIdContact(Long idContact) {
		ArrayList<PhoneNumber> phones = new ArrayList<PhoneNumber>();

		ResultSet rec = null;
		Connection con = null;
		try {
			
		Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM phonenumber where id_contact =" + "'" + idContact + "'");
			while (rec.next()) {
				PhoneNumber phone = new PhoneNumber();
				phone.setIdPhoneNumber(Long.parseLong(rec.getString("idPhoneNumber")));
				phone.setPhoneNumber(rec.getString("phoneNumber"));
				phones.add(phone);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return phones;	
	}


	@Override
	public ArrayList<ContactGroup> getGroupesByIdContact(Long idContact) {
		ArrayList<ContactGroup> groupes = new ArrayList<ContactGroup>();

		ResultSet rec = null;
		Connection con = null;
		try {
			
		Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contactgroup g join ctc_grp  cg on cg.GRP_ID = g.idContactGroup and cg.CTC_ID =" + "'" + idContact + "'" );
			
			while (rec.next()) {
				ContactGroup groupe = new ContactGroup();
				groupe.setIdContactGroup(Long.parseLong(rec.getString("idContactGroup")));
				groupe.setLabel(rec.getString("label"));
				groupes.add(groupe);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupes;	
	}
	@Override
	public ArrayList<ContactGroup> getGroupes() {
		ArrayList<ContactGroup> groupes = new ArrayList<ContactGroup>();

		ResultSet rec = null;
		Connection con = null;
		try {
			
		Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contactgroup" );
			
			while (rec.next()) {
				ContactGroup groupe = new ContactGroup();
				groupe.setIdContactGroup(Long.parseLong(rec.getString("idContactGroup")));
				groupe.setLabel(rec.getString("label"));
				groupes.add(groupe);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupes;	
	}



	@Override
	public ArrayList<PhoneNumber> getPhones() {
		ArrayList<PhoneNumber> phones = new ArrayList<PhoneNumber>();

		ResultSet rec = null;
		Connection con = null;
		try {
			
		Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM phonenumber where id_contact is null");
			while (rec.next()) {
				PhoneNumber phone = new PhoneNumber();
				phone.setIdPhoneNumber(Long.parseLong(rec.getString("idPhoneNumber")));
				phone.setPhoneNumber(rec.getString("phoneNumber"));
				phones.add(phone);
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return phones;	
	}


	@Override
	public Address getAdressByIdContact(Long idContact) {
		Address adr = new Address();

		ResultSet rec = null;
		Connection con = null;
		try {
			
		Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM address ad join contact as c on ad.idAddress=c.id_address where c.idContact=" + "'" + idContact + "'");
			while (rec.next()) {
			    adr = new Address();
			    adr.setIdAddress(Long.parseLong(rec.getString("idAddress")));
				adr.setAddress(rec.getString("address"));
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return adr;
	}


	@Override
	public boolean addGroupe(ContactGroup contactgroup) {
		//1: obtenir une connexion et un EntityManager, en passant par la classe JpaUtil
				EntityManager em=JpaUtil.getEmf().createEntityManager();
				// 3 : Ouverture transaction
				EntityTransaction tx = em.getTransaction();
				tx.begin();
				// 4 : Persistance Objet/Relationnel : création d'un enregistrement en base, 
				em.persist(contactgroup);
				// 5 : Fermeture transaction}
				tx.commit();
				// 6 : Fermeture de l'EntityManager et de unité de travail JPA
				em.close();
				
				return false;

				
			}


	@Override
	public boolean deleteGroupe(long id) {
			boolean success = false;
			Connection con = null;
			try {
				Class.forName(Messages.getString("driver"));
				con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),Messages.getString("password"));
				Statement stmt = con.createStatement();
				String request1 = "select grp_id from ctc_grp WHERE grp_id= " + id;
				String request = "DELETE FROM contactgroup WHERE idContactGroup= " + id;
				String request2 = "DELETE FROM ctc_grp WHERE grp_id= " + id;
				
				if(request1.isEmpty()) {
				stmt.executeUpdate(request);
				 success = true;
				}
				else {
					stmt.executeUpdate(request2);
					stmt.executeUpdate(request);
					success = true;}
				
				stmt.close();
				con.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return success;

	}


	@Override
	public boolean updateGroupe(ContactGroup contactgroup) {
		boolean success = false;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			String sqlLabel = "UPDATE contactgroup SET label = " + "'" + contactgroup.getLabel() + "'" + " WHERE idContactGroup = " + contactgroup.getIdContactGroup();
				
			if (contactgroup.getLabel() != "")
				stmt.executeUpdate(sqlLabel);
			stmt.close();
			con.close();
			success = true;
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return success;

	}
	
	
	//liste des groupe 
	

	


}
