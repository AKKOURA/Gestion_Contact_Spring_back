package com.lip6.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lip6.entities.Contact;
import com.lip6.entities.ContactGroup;
import com.lip6.entities.PhoneNumber;
import com.lip6.services.ServiceContact;

//Il inclut les annotations @Controller et @ResponseBody
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {
	
	@Autowired
	private ServiceContact cservice;
	
	@ResponseBody
	@GetMapping(value="/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Contact>  getAllContacts() {
		return cservice.getContacts();
	}


	@GetMapping(value="/delete/{id}")
	public boolean deleteContactById(@PathVariable int id){
		return cservice.deleteContact(id);
	}

	@PostMapping(value="/create")
	public boolean createProduct(@RequestBody Contact contact){
		 return cservice.createContact(contact);
	}

	@PostMapping(value="/update")
	public boolean updateContact(@RequestBody  Contact contact){
		return cservice.updateContact(contact);
	}
    
	@PostMapping(value="/edit")
	public boolean editContact (@RequestBody  Contact contact){
		return cservice.updateContact(contact);
	}
	
	@GetMapping(value="{idContact}/phones", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<PhoneNumber> getPhonesByIdContact(@PathVariable int idContact) {
		return cservice.getPhonesByIdContact(idContact);
	}

	@GetMapping(value="{idContact}/groupes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ContactGroup> getGroupesByIdContact( @PathVariable int idContact) {
		return cservice.getGroupesByIdContact(idContact);
	}
	
	@GetMapping(value="phones", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<PhoneNumber> getPhones() {
		return cservice.getPhones();
	}

	@GetMapping(value="groupes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ContactGroup> getGroupes() {
		return cservice.getGroupes();
	}
}
