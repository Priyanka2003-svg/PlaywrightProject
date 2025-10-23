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
        // Wait for element to be visible
        page.locator(usernameId).waitFor(new Locator.WaitForOptions()
            .setTimeout(10000));
        page.locator(usernameId).clear();
        page.locator(usernameId).fill(user);
    }

    public void setPassword(String passw) {
        // Wait for element to be visible
        page.locator(passwordId).waitFor(new Locator.WaitForOptions()
            .setTimeout(10000));
        page.locator(passwordId).clear();
        page.locator(passwordId).fill(passw);
    }

    public void loginButton() {
        page.locator(loginBtn).click();
        // Wait for navigation to start
        page.waitForTimeout(1000);
    }

    public void loginCre(String username, String password) {
        setName(username);
        page.waitForTimeout(500);
        setPassword(password);
        page.waitForTimeout(500);
        loginButton();
    }
}