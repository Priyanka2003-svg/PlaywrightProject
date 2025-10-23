package com.pages;

import com.microsoft.playwright.*;

public class EditUserPage {
    private Page page;
    
    // Locators
    private String usernameSearchInput = "//div[contains(@class,'oxd-table-filter')]//label[text()='Username']/parent::div/following-sibling::div/input";
    private String searchButton = "//button[@type='submit']";
    private String editIcon = "//i[@class='oxd-icon bi-pencil-fill']";
    private String saveButton = "//button[@type='submit']";

    public EditUserPage(Page page) {
        this.page = page;
    }

    public void editUserStatus(String username, String newStatus) {
        try {
            // Search for the user first
            page.locator(usernameSearchInput).waitFor(new Locator.WaitForOptions().setTimeout(15000));
            page.locator(usernameSearchInput).clear();
            page.locator(usernameSearchInput).fill(username);
            page.waitForTimeout(1000);
            page.locator(searchButton).click();
            page.waitForTimeout(2000);
            
            // Wait for edit icon and click
            page.locator(editIcon).first().click();
            page.waitForTimeout(2000);
            
            // Change status
            page.locator("//label[text()='Status']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]").click();
            page.waitForTimeout(1000);
            page.locator("//div[@role='listbox']//span[text()='" + newStatus + "']").click();
            page.waitForTimeout(1000);
            
            // Save changes
            page.locator(saveButton).click();
            page.waitForTimeout(2000);
        } catch (Exception e) {
            System.out.println("Edit operation failed: " + e.getMessage());
        }
    }
}
