
package com.pages;

import com.microsoft.playwright.*;

public class LoginPage {
    private Page page;
    
    // Locators
    private String usernameId = "input[name='username']";
    private String passwordId = "input[name='password']";
    private String loginBtn = "button[type='submit']";

    public LoginPage(Page page) {
        this.page = page;
    }

    public void setName(String user) {
        page.locator(usernameId).fill(user);
    }

    public void setPassword(String passw) {
        page.locator(passwordId).fill(passw);
    }

    public void loginButton() {
        page.locator(loginBtn).click();
    }

    public void loginCre(String username, String password) {
        setName(username);
        setPassword(password);
        loginButton();
    }
}
