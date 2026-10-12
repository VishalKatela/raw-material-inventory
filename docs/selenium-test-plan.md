# Selenium Test Design and Local Execution

## Critical user journeys
1. Add a new material to the inventory.
2. View the material dashboard with accurate status indicators.
3. Record a stock-in transaction.
4. Record a stock-out transaction without exceeding available stock.
5. Verify the system rejects invalid transaction data.

## Test cases
### TC-01: add material
- Navigate to home page
- Click Add Material
- Fill in required fields
- Submit form
- Assert success message appears

### TC-02: view inventory status
- Load dashboard
- Assert total materials count updates
- Assert low-stock cards reflect current state

### TC-03: stock in transaction
- Submit a STOCK_IN transaction for an existing material
- Assert quantity increases and transaction is listed

### TC-04: stock out transaction
- Submit a STOCK_OUT transaction with valid quantity
- Assert quantity decreases and transaction record appears

### TC-05: invalid transaction rejected
- Submit invalid transaction type or quantity
- Assert a validation error is shown

## Selenium execution strategy
- Uses Selenium WebDriver 4.27.0 with headless Chrome (`--headless=new`).
- Automated driver management via Selenium Manager.
- Implemented in `src/test/java/com/inventory/rawmaterial/selenium/RawMaterialInventorySeleniumTest.java`.
- Failure and execution screenshots captured via `src/test/java/com/inventory/rawmaterial/selenium/ScreenshotUtil.java`.
- Screenshots stored in `target/selenium-reports/screenshots/`.
- Surefire XML reports stored in `target/surefire-reports/`.

## Local execution command
```bash
./mvnw test -Dtest=RawMaterialInventorySeleniumTest
```

## Execution results
- Total test journeys executed: 5
- Failures: 0
- Errors: 0
- Build: SUCCESS
- Screenshots generated: 5 passed step captures and error diagnostics under `target/selenium-reports/screenshots/`.
