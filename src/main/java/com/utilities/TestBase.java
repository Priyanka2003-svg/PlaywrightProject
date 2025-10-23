package com.utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.testng.annotations.*;

public class TestBase {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    // Configuration
    protected String baseUrl = "https://opensource-demo.orangehrmlive.com/";
    protected String username = "Admin";
    protected String password = "admin123";

    @BeforeClass
    public void setUp() {
        // Initialize Playwright
        playwright = Playwright.create();
        
        // Launch browser (you can change to firefox() or webkit())
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(false)
            .setSlowMo(500)); // Slow down for visualization

        // Create browser context
        context = browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(1920, 1080));

        // Create new page
        page = context.newPage();
        
        // Set longer default timeout for all operations
        page.setDefaultTimeout(30000);

        // Navigate to base URL and wait for page to be fully loaded
        page.navigate(baseUrl, new Page.NavigateOptions()
            .setWaitUntil(WaitUntilState.NETWORKIDLE));
        
        // Additional wait for page to stabilize
        page.waitForTimeout(3000);

        System.out.println("Browser launched and navigated to: " + baseUrl);
    }

    @AfterClass
    public void tearDown() {
        // Close browser and cleanup
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        System.out.println("Browser closed and resources cleaned up");
    }

    // Helper method to take screenshot on failure
    protected void takeScreenshot(String testName) {
        if (page != null) {
            try {
                // Create screenshots directory if it doesn't exist
                java.nio.file.Files.createDirectories(
                    java.nio.file.Paths.get("screenshots"));
                
                page.screenshot(new Page.ScreenshotOptions()
                    .setPath(java.nio.file.Paths.get("screenshots/" + testName + ".png"))
                    .setFullPage(true));
                System.out.println("Screenshot saved: " + testName + ".png");
            } catch (Exception e) {
                System.out.println("Failed to take screenshot: " + e.getMessage());
            }
        }
    }
}