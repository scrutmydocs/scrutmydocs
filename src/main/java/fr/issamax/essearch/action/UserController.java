package fr.issamax.essearch.action;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.issamax.essearch.data.User;

@Component("userController")
@Scope("session")
public class UserController implements Serializable{

	@Autowired
	protected Client esClient;

	@Deprecated
	protected String username;
	
	@Deprecated
	protected String password;
	
	protected User user = new User();
	
	protected boolean logged;
	
	public void login(ActionEvent actionEvent) {
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg = null;  
          // TODO REMOVE ME
        if(username != null  && username.equals("admin") && password != null  && password.equals("admin")) {  
        	logged = true;  
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);  
        } else {  
        	logged = false;  
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");  
        }  
          
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        context.addCallbackParam("logged", logged);  
	}

	public void logout() {
    	logged = false;
    	password = "";

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout successful", username);  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
	}

	/**
	 * Create or update the user
	 */
	public void push() {
		// Persist the user
		try {
			esClient
					.prepareIndex("users","user",user.getLogin())
					.setSource(
						jsonBuilder()
							.startObject()
								.field("login", user.getLogin())
								.field("password", Base64
									.encodeBytes(user.getPassword().getBytes()))
								.field("email", user.getEmail())
							.endObject())
					.execute().actionGet();
			FacesMessage msg = new FacesMessage("Succesful", user.getLogin() + " is created/updated.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	@Deprecated
	public String getUsername() {
		return username;
	}
	
	@Deprecated
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Deprecated
	public String getPassword() {
		return password;
	}

	@Deprecated
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
}
