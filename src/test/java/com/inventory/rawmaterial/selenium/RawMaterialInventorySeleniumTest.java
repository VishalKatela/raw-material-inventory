package com.inventory.rawmaterial.selenium;

import com.inventory.rawmaterial.model.Material;
import com.inventory.rawmaterial.service.MaterialService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RawMaterialInventorySeleniumTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MaterialService materialService;

    private static WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @RegisterExtension
    final TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            ScreenshotUtil.captureScreenshot(driver, "FAILED_" + context.getDisplayName().replaceAll("[^a-zA-Z0-9_-]", "_"));
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            ScreenshotUtil.captureScreenshot(driver, "PASSED_" + context.getDisplayName().replaceAll("[^a-zA-Z0-9_-]", "_"));
        }
    };

    @BeforeAll
    static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--remote-allow-origins=*"
        );
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/";
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Seed initial material if none exists
        if (materialService.getAllMaterials().isEmpty()) {
            Material baseMaterial = new Material();
            baseMaterial.setMaterialCode("RM-BASE-01");
            baseMaterial.setName("Base Stainless Steel");
            baseMaterial.setCategory("Metals");
            baseMaterial.setUnit("kg");
            baseMaterial.setQuantity(new BigDecimal("100.00"));
            baseMaterial.setMinimumQuantity(new BigDecimal("20.00"));
            materialService.addMaterial(baseMaterial);
        }
    }

    @Test
    @Order(1)
    @DisplayName("TC-01: Add New Material Journey")
    void testAddMaterialJourney() {
        driver.get(baseUrl);

        // Click Add Material button
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Add Material')]")));
        addBtn.click();

        // Fill form fields
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("materialCode"))).sendKeys("RM-SEL-ALUM");
        driver.findElement(By.id("materialName")).sendKeys("Aluminum Sheet");
        driver.findElement(By.id("category")).sendKeys("Metals");
        driver.findElement(By.id("unit")).sendKeys("pcs");
        driver.findElement(By.id("quantity")).sendKeys("50");
        driver.findElement(By.id("minimumQuantity")).sendKeys("20");

        // Submit form
        WebElement saveBtn = driver.findElement(By.cssSelector("#addMaterialForm button[type='submit']"));
        saveBtn.click();

        // Handle JavaScript alert confirmation
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        // Verify table displays new material
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("materialTable"), "RM-SEL-ALUM"));
        String pageSource = driver.getPageSource();
        assertThat(pageSource).contains("RM-SEL-ALUM");
        assertThat(pageSource).contains("Aluminum Sheet");
    }

    @Test
    @Order(2)
    @DisplayName("TC-02: Dashboard Metrics And Status Visibility")
    void testDashboardMetricsAndStatus() {
        driver.get(baseUrl);

        // Verify total materials count is at least 1
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalMaterials")));
        WebElement totalMaterials = driver.findElement(By.id("totalMaterials"));
        wait.until(d -> !totalMaterials.getText().equals("0") && !totalMaterials.getText().trim().isEmpty());

        assertThat(Integer.parseInt(totalMaterials.getText().trim())).isGreaterThanOrEqualTo(1);

        // Filter by keyword
        WebElement searchInput = driver.findElement(By.id("searchKeyword"));
        searchInput.clear();
        searchInput.sendKeys("Steel");
        driver.findElement(By.xpath("//button[contains(text(), 'Apply Filters')]")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("materialTable"), "Steel"));
        assertThat(driver.findElement(By.id("materialTable")).getText()).contains("Steel");
    }

    @Test
    @Order(3)
    @DisplayName("TC-03: Stock In Movement Journey")
    void testStockInTransactionJourney() {
        driver.get(baseUrl);

        // Wait for dropdown to populate
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#transactionMaterial option"), 1));

        Select materialSelect = new Select(driver.findElement(By.id("transactionMaterial")));
        materialSelect.selectByIndex(1);

        Select typeSelect = new Select(driver.findElement(By.id("transactionType")));
        typeSelect.selectByValue("STOCK_IN");

        WebElement qtyInput = driver.findElement(By.id("transactionQuantity"));
        qtyInput.clear();
        qtyInput.sendKeys("25");

        WebElement remarksInput = driver.findElement(By.id("transactionRemarks"));
        remarksInput.clear();
        remarksInput.sendKeys("Received supplier shipment");

        driver.findElement(By.cssSelector("#transactionForm button[type='submit']")).click();

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("transactionMessage")));
        wait.until(ExpectedConditions.textToBePresentInElement(msg, "Stock movement recorded."));

        assertThat(msg.getText()).contains("Stock movement recorded.");
    }

    @Test
    @Order(4)
    @DisplayName("TC-04: Stock Out Movement Journey")
    void testStockOutTransactionJourney() {
        driver.get(baseUrl);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#transactionMaterial option"), 1));

        Select materialSelect = new Select(driver.findElement(By.id("transactionMaterial")));
        materialSelect.selectByIndex(1);

        Select typeSelect = new Select(driver.findElement(By.id("transactionType")));
        typeSelect.selectByValue("STOCK_OUT");

        WebElement qtyInput = driver.findElement(By.id("transactionQuantity"));
        qtyInput.clear();
        qtyInput.sendKeys("10");

        WebElement remarksInput = driver.findElement(By.id("transactionRemarks"));
        remarksInput.clear();
        remarksInput.sendKeys("Production release");

        driver.findElement(By.cssSelector("#transactionForm button[type='submit']")).click();

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("transactionMessage")));
        wait.until(ExpectedConditions.textToBePresentInElement(msg, "Stock movement recorded."));

        assertThat(msg.getText()).contains("Stock movement recorded.");
    }

    @Test
    @Order(5)
    @DisplayName("TC-05: Rejection of Invalid Transaction (Insufficient Stock)")
    void testInvalidTransactionRejected() {
        driver.get(baseUrl);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#transactionMaterial option"), 1));

        Select materialSelect = new Select(driver.findElement(By.id("transactionMaterial")));
        materialSelect.selectByIndex(1);

        Select typeSelect = new Select(driver.findElement(By.id("transactionType")));
        typeSelect.selectByValue("STOCK_OUT");

        // Attempt excessive withdrawal
        WebElement qtyInput = driver.findElement(By.id("transactionQuantity"));
        qtyInput.clear();
        qtyInput.sendKeys("999999");

        WebElement remarksInput = driver.findElement(By.id("transactionRemarks"));
        remarksInput.clear();
        remarksInput.sendKeys("Attempt excessive stock out");

        driver.findElement(By.cssSelector("#transactionForm button[type='submit']")).click();

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("transactionMessage")));
        wait.until(ExpectedConditions.textToBePresentInElement(msg, "Insufficient stock"));

        assertThat(msg.getText()).contains("Insufficient stock");
    }
}
