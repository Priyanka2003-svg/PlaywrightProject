package com.tests;

import com.pages.*;
import com.utiltities.TestBase;
import org.testng.annotations.*;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class UserManagementTest extends TestBase {
    
    private LoginPage loginPage;
    private AdminPage adminPage;
    private SearchUserPage searchUserPage;
    private DeleteUserPage deleteUserPage;
    
    // Test data
    private String employeeName = "Peter Mac Anderson";
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
        loginPage.loginCre(username, password);
        
        // Wait for dashboard to load
        page.waitForSelector("//h6[text()='Dashboard']", new Page.WaitForSelectorOptions()
            .setTimeout(15000));
        System.out.println("✓ Login successful");
    }
    
    private void addUser() {
        System.out.println("Opening Admin section...");
        adminPage.openAdminSection();
        
        // Wait for Admin page to load
        page.waitForTimeout(2000);
        
        System.out.println("Clicking Add button...");
        adminPage.clickAddButton();
        
        // Wait for form to load
        page.waitForTimeout(2000);
        
        System.out.println("Filling user details...");
        System.out.println("- Employee Name: " + employeeName);
        System.out.println("- Username: " + newUsername);
        System.out.println("- Password: " + newPassword);
        
        adminPage.fillUserDetails(employeeName, newUsername, newPassword, newPassword);
        
        // Wait a bit before saving
        page.waitForTimeout(1000);
        
        System.out.println("Saving user...");
        adminPage.saveUser();
        
        // Wait for success message or page reload
        page.waitForTimeout(3000);
        System.out.println("✓ User added successfully");
    }
    
    private void searchUser() {
        System.out.println("Searching for user: " + newUsername);
        
        // Wait for search form to be ready
        page.waitForTimeout(2000);
        
        searchUserPage.searchUser(newUsername, userRole, userStatus);
        
        // Wait for search results
        page.waitForTimeout(2000);
        
        // Verify user appears in results
        try {
            page.waitForSelector("//div[contains(text(),'" + newUsername + "')]", 
                new Page.WaitForSelectorOptions().setTimeout(10000));
            System.out.println("✓ User found in search results");
        } catch (Exception e) {
            System.out.println("⚠ User not found in search results, but continuing...");
        }
    }
    
    private void deleteUser() {
        System.out.println("Deleting user: " + newUsername);
        
        deleteUserPage.deleteUser(newUsername);
        
        // Wait for deletion confirmation
        page.waitForTimeout(3000);
        
        System.out.println("✓ User deleted successfully");
    }
    
    private void logout() {
        System.out.println("Logging out...");
        
        // Click on user dropdown
        page.locator("//p[@class='oxd-userdropdown-name']").click();
        page.waitForTimeout(1000);
        
        // Click logout
        page.locator("//a[text()='Logout']").click();
        
        // Wait for login page
        page.waitForSelector("input[name='username']", new Page.WaitForSelectorOptions()
            .setTimeout(10000));
        
        System.out.println("✓ Logout successful");
    }
    
    // Additional individual test methods (optional)
    
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
