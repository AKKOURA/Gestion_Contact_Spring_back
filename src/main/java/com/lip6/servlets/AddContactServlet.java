package com.lip6.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lip6.configuration.AppConfiguration;
import com.lip6.entities.Address;
import com.lip6.entities.Contact;
import com.lip6.entities.PhoneNumber;
import com.lip6.services.IServiceContact;

/**
 * Servlet implementation class AddContactServlet
 */
@ContextConfiguration(classes= {AppConfiguration.class})
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
       // ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		ApplicationContext	context = new ClassPathXmlApplicationContext("*/main/java/applicationContext-servlet.xml");
		
		String[] allBeanNames = context.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println(beanName + "******************");
        }

       //IDAOContact dao = (IDAOContact)context.getBean("cdao_set");
      Contact contactForm = (Contact)context.getBean("contactform");
      Address adr = (Address)context.getBean("ContactFormAdresseBean");
      PhoneNumber  phone= (PhoneNumber)context.getBean("ContactFormPhoneNumberBean");
       
       IServiceContact service = (IServiceContact)context.getBean("cservice");//lookup
      
       //création par setter
       service.createContact((Contact)context.getBean("contact1"));
       
       //création par constructeur
       service.createContact((Contact)context.getBean("contact2"));
       
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
		service.createContact(contactForm);
	}

}
