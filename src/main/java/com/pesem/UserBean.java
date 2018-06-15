package com.pesem;

import org.jboss.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    private String userName;

    private String passWord;

    FacesMessage message;

    private final static Logger Log = Logger.getLogger(UserBean.class);

    private FacesContext context;

    private boolean loggedIn = false;

    public UserBean() {

    }

    /**
     * method used to login
     *
     * @return
     */
    public String login() {
        try {
            if (userName.isEmpty() || passWord.isEmpty()) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Invalid username or password");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "index";
            } else {
                if (userName.equals("admin") && passWord.equals("admin123")) {
                    loggedIn = true;
                    context = FacesContext.getCurrentInstance();
                    context.getExternalContext().getSessionMap().put("user", userName);
                    Log.info("user " + userName + " logged in at " + LocalDateTime.now());
                    return "payment?faces-redirect=true";
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Invalid username or password");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "index";
                }
            }
        } catch (Exception ex) {
            Log.error(" Enable to log in | " + ex.toString());
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Enable to login try again later");
            return "index?faces-redirect=true";
        }
    }

    /**
     * method used to check if user is authenticated if not redirect to login
     */
    public void checkIfuserIsAuthenticated() {
        try {
            context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            if (session.getAttribute("user") == null) {
                String viewId = context.getViewRoot().getViewId();
                if (viewId.equals("/index.xhtml")) {
                    context.getExternalContext().redirect("index.xhtml");
                    context.responseComplete();
                } else if (!viewId.equals("/index.xhtml")) {
                    context.getExternalContext().redirect("index.xhtml");
                    context.responseComplete();
                }
            }
        } // end of try // end of try
        catch (Exception ex) {
            Log.error("Cannot check if user is authenticated", ex.getCause());
        }
    }

    /**
     * method used to logout
     */
    public void logout() {
        try {
            context = FacesContext.getCurrentInstance();
            HttpSession currentSession = (HttpSession) context.getExternalContext().getSession(true);
            String viewId = context.getViewRoot().getViewId();
            if (!viewId.equals("/index.xhtml")) {
                context.getExternalContext().redirect("index.xhtml");
                context.responseComplete();
            }
            currentSession.invalidate();
            loggedIn = false;
        } catch (IOException ex) {
            Log.error("The error ", ex);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
