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
        // Wait for username input to be visible and fill
        Locator usernameInput = page.locator(usernameSearchInput);
        usernameInput.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(60000));
        usernameInput.fill(username);

        // Select User Role
        Locator roleDropdown = page.locator("//div[contains(@class,'oxd-table-filter')]//label[text()='User Role']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]");
        roleDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        roleDropdown.click();
        page.locator("//div[@role='listbox']//span[text()='" + role + "']").click();

        // Select Status
        Locator statusDropdown = page.locator("//div[contains(@class,'oxd-table-filter')]//label[text()='Status']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]");
        statusDropdown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        statusDropdown.click();
        page.locator("//div[@role='listbox']//span[text()='" + status + "']").click();

        // Click Search
        Locator searchBtn = page.locator(searchButton);
        searchBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        searchBtn.click();

        // Optional: wait until results table is visible
        page.locator("//div[@class='oxd-table-body']").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
    }
}
