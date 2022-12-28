package com.lip6.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lip6.entities.Address;
import com.lip6.entities.Contact;
import com.lip6.entities.PhoneNumber;
import com.lip6.services.IServiceContact;

/**
 * Servlet implementation class AddContactServlet
 */
@WebServlet("/AddContactServlet")
public class AddContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddContactServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		ClassPathXmlApplicationContext	context = new ClassPathXmlApplicationContext("*/main/java/applicationContext-servlet.xml");
		String[] allBeanNames = context.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println(beanName + "CONTXT 1******************");
        }
		 ((ClassPathXmlApplicationContext) context).close();
		
		ApplicationContext context2 = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		String[] allBeanNames2 = context2.getBeanDefinitionNames();
        for(String beanName : allBeanNames2) {
            System.out.println(beanName + "CONTEXT 2******************");
        }
	

       //IDAOContact dao = (IDAOContact)context.getBean("cdao_set");
     // Contact contactForm = (Contact)context2.getBean("Contact", Contact.class);
      //Address adr = (Address)context2.getBean("Address", Address.class);
      //PhoneNumber  phone= (PhoneNumber)context2.getBean("PhoneNumber",PhoneNumber.class);
       
      /* IServiceContact service = (IServiceContact)context2.getBean("cservice");//lookup
      
       //création par setter
       service.createContact((Contact)context2.getBean("contact1"));
       
       //création par constructeur
       service.createContact((Contact)context2.getBean("contact2"));
       ((ClassPathXmlApplicationContext) context2).close();
       
       
       
       //création via le formulaire  
		String fname=request.getParameter("fname");
		String lname =request.getParameter("lname");
		String email=request.getParameter("email"); 
		String address=request.getParameter("address"); 
		String tel=request.getParameter("tel"); 
		
		 contactForm.setFirstName(fname);
		 contactForm.setLastName(lname);
		 contactForm.setEmail(email);
		 adr.setAddress(address);
		 phone.setPhoneNumber(tel);
		
		
		//création via le forrmulaire 
		service.createContact(contactForm);*/
		//((ClassPathXmlApplicationContext) context2).close();
	}

}
