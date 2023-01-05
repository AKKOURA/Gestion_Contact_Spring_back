	package com.lip6.daos;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
	public boolean modifyContact(long id, String firstname, String lastname, String email) {
		boolean success = false;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			System.out.printf("zzzzzzzzzzz",id);
			System.out.println(id);
			Statement stmt = con.createStatement();
			String sqlFirstName = "UPDATE contact SET firstName = " + "'" + firstname + "'" + " WHERE idContact = " + id;
			String sqlLastName = "UPDATE contact SET lastName = " + "'" + lastname + "'" + " WHERE idContact = " + id;
			String sqlEmail = "UPDATE contact SET email = " + "'" + email + "'" + " WHERE idContact = " + id;

			if (firstname != "")
				stmt.executeUpdate(sqlFirstName);
			if (lastname != "")
				stmt.executeUpdate(sqlLastName);
			if (email != "")
				stmt.executeUpdate(sqlEmail);

			success = true;
			stmt.close();
			con.close();

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
				Contact previousContact = em.find(Contact.class, contact.getIdContact());
				
				previousContact.setFirstName(contact.getFirstName());
				previousContact.setLastName(contact.getLastName());
				previousContact.setEmail(contact.getEmail());
				previousContact.setAddress(contact.getAddress());
				previousContact.setPhones(contact.getPhones());
				previousContact.setContactGroups(contact.getContactGroups());

				em.getTransaction().commit();
				return true;
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
	public ArrayList<ContactGroup> getGroupes() {
		ArrayList<ContactGroup>  groupes = new ArrayList<ContactGroup>();

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
	public Address getAdressByIdContact(Long idContact) {
		Address adr = new Address();

		ResultSet rec = null;
		Connection con = null;
		try {
			
		Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT ad.address FROM address ad join contact as c on ad.idAddress=c.idContact where c.idContact=" + "'" + idContact + "'");
			while (rec.next()) {
			    adr = new Address();
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
	


}
