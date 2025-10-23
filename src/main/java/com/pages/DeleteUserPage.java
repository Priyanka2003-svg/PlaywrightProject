package com.pages;

import com.microsoft.playwright.*;

public class DeleteUserPage {
    private Page page;
    
    // Locators
    private String usernameSearchInput = "//div[contains(@class,'oxd-table-filter')]//label[text()='Username']/parent::div/following-sibling::div/input";
    private String searchButton = "//button[@type='submit']";
    private String deleteIcon = "//i[@class='oxd-icon bi-trash']";
    private String confirmDeleteButton = "//button[normalize-space()='Yes, Delete']";

    public DeleteUserPage(Page page) {
        this.page = page;
    }

    public void deleteUser(String username) {
        try {
            // Wait for search form
            page.locator(usernameSearchInput).waitFor(new Locator.WaitForOptions().setTimeout(15000));
            page.locator(usernameSearchInput).clear();
            page.locator(usernameSearchInput).fill(username);
            page.waitForTimeout(1000);
            
            // Click search
            page.locator(searchButton).click();
            page.waitForTimeout(2000);
            
            // Wait for results and click delete icon
            page.locator(deleteIcon).first().waitFor(new Locator.WaitForOptions().setTimeout(15000));
            page.locator(deleteIcon).first().click();
            page.waitForTimeout(1000);
            
            // Confirm deletion
            page.locator(confirmDeleteButton).click();
            page.waitForTimeout(2000);
            
            System.out.println("✓ User deletion completed");
        } catch (Exception e) {
            System.out.println("Delete operation failed: " + e.getMessage());
        }
    }
}