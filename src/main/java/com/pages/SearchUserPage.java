package com.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class SearchUserPage {
    private Page page;

    // Locators
    private String usernameSearchInput = "//div[contains(@class,'oxd-table-filter')]//label[text()='Username']/parent::div/following-sibling::div/input";
    private String searchButton = "//button[@type='submit']";

    public SearchUserPage(Page page) {
        this.page = page;
    }

    public void searchUser(String username, String role, String status) {
        // Wait for the search form to be visible and ready
        page.locator(usernameSearchInput).waitFor(
            new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(30000)
        );
        
        // Fill username
        page.locator(usernameSearchInput).clear();
        page.locator(usernameSearchInput).fill(username);
        page.waitForTimeout(1000);

        // Select User Role
        page.locator("//div[contains(@class,'oxd-table-filter')]//label[text()='User Role']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]").click();
        page.waitForTimeout(1000);
        page.locator("//div[@role='listbox']//span[text()='" + role + "']").click();
        page.waitForTimeout(500);

        // Select Status
        page.locator("//div[contains(@class,'oxd-table-filter')]//label[text()='Status']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]").click();
        page.waitForTimeout(1000);
        page.locator("//div[@role='listbox']//span[text()='" + status + "']").click();
        page.waitForTimeout(500);

        // Click search
        page.locator(searchButton).click();
        page.waitForTimeout(2000);
    }
}