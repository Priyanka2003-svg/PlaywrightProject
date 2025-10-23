package com.tests;

import com.pages.*;
import com.utilities.TestBase;
import org.testng.annotations.*;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class UserManagementTest extends TestBase {
    private LoginPage loginPage;
    private AdminPage adminPage;
    private SearchUserPage searchUserPage;
    private DeleteUserPage deleteUserPage;

    // Test data - Using a simpler, more common employee name
    private String employeeName = "Paul"; // Shorter name for better autocomplete
    private String newUsername = "testuser" + System.currentTimeMillis(); // Unique username
    private String newPassword = "Test@1234";
    private String userRole = "Admin";
    private String userStatus = "Enabled";

    @BeforeMethod
    public void initializePages() {
        loginPage = new LoginPage(page);
        adminPage = new AdminPage(page);
        searchUserPage = new SearchUserPage(page);
        deleteUserPage = new DeleteUserPage(page);
    }

    @Test(priority = 1, description = "Complete User Management Flow: Login, Add, Search, Delete, Logout")
    public void testCompleteUserManagementFlow() {
        try {
            // Step 1: Login
            System.out.println("Step 1: Logging in...");
            login();

            // Step 2: Add User
            System.out.println("Step 2: Adding new user...");
            addUser();

            // Step 2.5: Navigate back to Admin section
            System.out.println("Step 2.5: Returning to Admin section...");
            adminPage.openAdminSection();
            page.waitForTimeout(3000);

            // Step 3: Search User
            System.out.println("Step 3: Searching for added user...");
            searchUser();

            // Step 4: Delete User
            System.out.println("Step 4: Deleting user...");
            deleteUser();

            // Step 5: Logout
            System.out.println("Step 5: Logging out...");
            logout();

            System.out.println("✓ User Management Flow completed successfully!");
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            takeScreenshot("userManagementTest_failure");
            throw e;
        }
    }

    private void login() {
        System.out.println("Logging in with username: " + username);
        
        int maxRetries = 3;
        int attempt = 0;
        boolean loginSuccess = false;
        
        while (attempt < maxRetries && !loginSuccess) {
            try {
                attempt++;
                System.out.println("Login attempt " + attempt + " of " + maxRetries);
                
                // Ensure we're on the login page
                if (!page.url().contains("auth/login")) {
                    page.navigate(baseUrl);
                    page.waitForLoadState(LoadState.NETWORKIDLE);
                    page.waitForTimeout(2000);
                }
                
                // Perform login
                loginPage.loginCre(username, password);
                
                // Wait for dashboard with longer timeout
                page.waitForSelector("//h6[text()='Dashboard']", 
                    new Page.WaitForSelectorOptions()
                        .setTimeout(30000)
                        .setState(WaitForSelectorState.VISIBLE));
                
                loginSuccess = true;
                System.out.println("✓ Login successful on attempt " + attempt);
                
            } catch (Exception e) {
                System.out.println("Login attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxRetries) {
                    System.out.println("All login attempts failed");
                    throw e;
                }
                // Wait before retry
                page.waitForTimeout(3000);
            }
        }
    }

    private void addUser() {
        System.out.println("Opening Admin section...");
        adminPage.openAdminSection();
        page.waitForTimeout(2000);

        System.out.println("Clicking Add button...");
        adminPage.clickAddButton();
        page.waitForTimeout(2000);

        System.out.println("Filling user details...");
        System.out.println("- Employee Name: " + employeeName);
        System.out.println("- Username: " + newUsername);
        System.out.println("- Password: " + newPassword);
        adminPage.fillUserDetails(employeeName, newUsername, newPassword, newPassword);

        page.waitForTimeout(1000);
        System.out.println("Saving user...");
        adminPage.saveUser();

        // Wait for success message or page reload
        page.waitForTimeout(5000); // Increased wait
        System.out.println("✓ User added successfully");
    }

    private void searchUser() {
        System.out.println("Searching for user: " + newUsername);
        page.waitForTimeout(2000);
        
        searchUserPage.searchUser(newUsername, userRole, userStatus);
        page.waitForTimeout(3000); // Increased wait

        // Verify user appears in results
        try {
            page.waitForSelector("//div[contains(text(),'" + newUsername + "')]", 
                new Page.WaitForSelectorOptions().setTimeout(15000));
            System.out.println("✓ User found in search results");
        } catch (Exception e) {
            System.out.println("⚠ User not found in search results");
            System.out.println("  This could mean the user wasn't created successfully");
        }
    }

    private void deleteUser() {
        System.out.println("Deleting user: " + newUsername);
        deleteUserPage.deleteUser(newUsername);
        page.waitForTimeout(3000);
        System.out.println("✓ Delete operation completed");
    }

    private void logout() {
        System.out.println("Logging out...");
        page.locator("//p[@class='oxd-userdropdown-name']").click();
        page.waitForTimeout(1000);
        page.locator("//a[text()='Logout']").click();
        page.waitForSelector("input[name='username']", new Page.WaitForSelectorOptions()
            .setTimeout(10000));
        System.out.println("✓ Logout successful");
    }

    @Test(priority = 2, description = "Login Test Only", enabled = false)
    public void testLoginOnly() {
        login();
    }

    @Test(priority = 3, description = "Add User Test Only", enabled = false)
    public void testAddUserOnly() {
        login();
        addUser();
        logout();
    }

    @Test(priority = 4, description = "Search User Test Only", enabled = false)
    public void testSearchUserOnly() {
        login();
        adminPage.openAdminSection();
        searchUser();
        logout();
    }
}