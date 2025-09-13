package com.ecommerce.tests;

import com.ecommerce.pages.LoginPage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://example-ecommerce.com/login");
        loginPage = new LoginPage(driver);
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() throws IOException {
        FileInputStream file = new FileInputStream(new File("testdata/LoginData.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        Object[][] data = new Object[rows-1][2];

        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            data[i-1][0] = row.getCell(0).getStringCellValue();
            data[i-1][1] = row.getCell(1).getStringCellValue();
        }
        workbook.close();
        file.close();
        return data;
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        
        String currentUrl = driver.getCurrentUrl();
        boolean loggedIn = !currentUrl.contains("login");
        Assert.assertTrue(loggedIn, "Login failed for: " + username);
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
