package com.pages;

import com.microsoft.playwright.*;

public class AdminPage {
    private Page page;
    
    // Locators
    private String adminTab = "//span[text()='Admin']";
    private String addButton = "//button[normalize-space()='Add']";
    private String employeeNameInput = "//input[@placeholder='Type for hints...']";
    private String usernameInput = "//label[text()='Username']/parent::div/following-sibling::div/input";
    private String passwordInput = "//label[text()='Password']/parent::div/following-sibling::div/input";
    private String confirmPasswordInput = "//label[text()='Confirm Password']/parent::div/following-sibling::div/input";
    private String saveButton = "//button[@type='submit']";

    public AdminPage(Page page) {
        this.page = page;
    }

    public void openAdminSection() {
        page.locator(adminTab).click();
    }

    public void clickAddButton() {
        page.locator(addButton).click();
    }

    public void fillUserDetails(String empName, String username, String password, String confirmPassword) {
        page.locator(employeeNameInput).waitFor(new Locator.WaitForOptions().setTimeout(15000));
        
        // Select User Role - using index-based approach
        page.locator("//label[text()='User Role']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]").click();
        page.waitForTimeout(1000);
        page.locator("//div[@role='listbox']//span[text()='Admin']").click();
        page.waitForTimeout(500);
        
        // Fill Employee Name
        page.locator(employeeNameInput).fill(empName);
        page.waitForTimeout(2000); // Wait for autocomplete dropdown
        
        try {
            // Click on the suggested employee from dropdown
            page.locator("//div[@role='listbox']//span[contains(text(),'" + empName + "')]").first().click();
            page.waitForTimeout(1000);
        } catch (Exception e) {
            System.out.println("Employee name '" + empName + "' not found or not clickable.");
        }
        
        // Fill Username
        page.locator(usernameInput).fill(username);
        page.waitForTimeout(500);
        
        // Select Status
        page.locator("//label[text()='Status']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]").click();
        page.waitForTimeout(1000);
        page.locator("//div[@role='listbox']//span[text()='Enabled']").click();
        page.waitForTimeout(500);
        
        // Fill Passwords
        page.locator(passwordInput).fill(password);
        page.waitForTimeout(500);
        page.locator(confirmPasswordInput).fill(confirmPassword);
        page.waitForTimeout(500);
    }

    public void saveUser() {
        page.locator(saveButton).click();
    }
}