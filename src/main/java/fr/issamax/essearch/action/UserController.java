package fr.issamax.essearch.action;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("userController")
@Scope("session")
public class UserController implements Serializable{

	protected String username;
	
	protected String password;
	
	protected boolean logged;
	
	public void login(ActionEvent actionEvent) {
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg = null;  
          
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

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	
	
}
