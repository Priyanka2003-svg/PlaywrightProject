package com.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

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
            page.locator(usernameSearchInput).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15000));
            
            page.locator(usernameSearchInput).clear();
            page.locator(usernameSearchInput).fill(username);
            page.waitForTimeout(1000);

            // Click search
            page.locator(searchButton).click();
            page.waitForTimeout(3000); // Increased wait for results

            // Wait for results table to load
            try {
                page.locator("//div[@class='oxd-table-body']").waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(10000));
            } catch (Exception e) {
                System.out.println("⚠ Results table not found");
            }

            // Check if user exists in results
            if (page.locator("//div[contains(text(),'" + username + "')]").count() > 0) {
                System.out.println("✓ User found in results, proceeding to delete");
                
                // Wait for delete icon and click
                page.locator(deleteIcon).first().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(30000)); // Increased timeout
                
                page.locator(deleteIcon).first().click();
                page.waitForTimeout(1000);

                // Confirm deletion
                page.locator(confirmDeleteButton).waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(10000));
                page.locator(confirmDeleteButton).click();
                page.waitForTimeout(2000);

                System.out.println("✓ User deletion completed");
            } else {
                System.out.println("⚠ User '" + username + "' not found in search results. May have already been deleted or wasn't created successfully.");
            }

        } catch (Exception e) {
            System.out.println("⚠ Delete operation encountered an issue: " + e.getMessage());
            System.out.println("This may be because the user was not created successfully in the first place.");
        }
    }
}