package com.utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVReporter implements ITestListener {
    private static final String DIRECTORY = "./target/reports";
    private static final String FILE_NAME = DIRECTORY + "/test-report.csv";
    private FileWriter writer;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;

    @Override
    public void onStart(ITestContext context) {
        try {
            File folder = new File(DIRECTORY);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            writer = new FileWriter(FILE_NAME);
            writer.write("TestName,Status,ExecutionTime(ms),Description,Timestamp\n");
            System.out.println("CSV Report initialized: " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
        writeResult(result, "PASSED");
        System.out.println("✓ Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        writeResult(result, "FAILED");
        System.out.println("✗ Test FAILED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            System.out.println("  Error: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
        writeResult(result, "SKIPPED");
        System.out.println("⊘ Test SKIPPED: " + result.getMethod().getMethodName());
    }

    private void writeResult(ITestResult result, String status) {
        try {
            String testName = result.getMethod().getMethodName();
            long duration = result.getEndMillis() - result.getStartMillis();
            String description = result.getMethod().getDescription();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            
            writer.write(testName + "," + 
                        status + "," + 
                        duration + "," + 
                        (description != null ? description.replace(",", ";") : "N/A") + "," + 
                        timestamp + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            if (writer != null) {
                // Write summary at the end
                writer.write("\n=== Test Execution Summary ===\n");
                writer.write("Total Tests," + (passedTests + failedTests + skippedTests) + "\n");
                writer.write("Passed," + passedTests + "\n");
                writer.write("Failed," + failedTests + "\n");
                writer.write("Skipped," + skippedTests + "\n");
                
                writer.close();
                
                System.out.println("\n" + "=".repeat(50));
                System.out.println("CSV REPORT SUMMARY");
                System.out.println("=".repeat(50));
                System.out.println("Total Tests: " + (passedTests + failedTests + skippedTests));
                System.out.println("Passed: " + passedTests);
                System.out.println("Failed: " + failedTests);
                System.out.println("Skipped: " + skippedTests);
                System.out.println("Report Location: " + new File(FILE_NAME).getAbsolutePath());
                System.out.println("=".repeat(50));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}