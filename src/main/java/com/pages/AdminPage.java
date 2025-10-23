package com.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

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
        page.locator(employeeNameInput).waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(15000));

        // Select User Role - using index-based approach
        page.locator("//label[text()='User Role']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]").click();
        page.waitForTimeout(1000);
        page.locator("//div[@role='listbox']//span[text()='Admin']").click();
        page.waitForTimeout(500);

        // Fill Employee Name with better handling
        System.out.println("Typing employee name: " + empName);
        page.locator(employeeNameInput).fill(empName);
        page.waitForTimeout(3000); // Increased wait for autocomplete

        // Try to select from dropdown with better error handling
        try {
            // Wait for dropdown to appear
            page.locator("//div[@role='listbox']").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
            
            // Click on the first matching employee from dropdown
            String dropdownOption = "//div[@role='listbox']//span[contains(text(),'" + empName + "')]";
            
            if (page.locator(dropdownOption).count() > 0) {
                page.locator(dropdownOption).first().click();
                System.out.println("✓ Employee '" + empName + "' selected from dropdown");
                page.waitForTimeout(1000);
            } else {
                // Try selecting first option if exact match not found
                page.locator("//div[@role='listbox']//span").first().click();
                System.out.println("✓ First employee option selected");
                page.waitForTimeout(1000);
            }
        } catch (Exception e) {
            System.out.println("⚠ Autocomplete dropdown not found. Trying to continue...");
            // Clear and type a simpler name
            page.locator(employeeNameInput).clear();
            page.locator(employeeNameInput).fill("a");
            page.waitForTimeout(2000);
            try {
                page.locator("//div[@role='listbox']//span").first().click();
                System.out.println("✓ Selected first available employee");
            } catch (Exception ex) {
                System.out.println("✗ Could not select employee: " + ex.getMessage());
            }
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