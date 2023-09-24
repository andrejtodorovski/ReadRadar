const { Builder, By, until } = require('selenium-webdriver');
const { login, elementExists, register } = require('./seleniumUtils');  // Adjust path if necessary

(async function() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://localhost:4200');

        await driver.findElement(By.id('login')).click();

        await login(driver, 'admin', 'admin');
        
        await driver.wait(
            until.elementLocated(By.id('add')),
            10000
        );

        await driver.findElement(By.id('add')).click();

        await driver.wait(
            until.elementLocated(By.name('title')),
            10000
        );
        await driver.findElement(By.name('title')).sendKeys("Test");
        await driver.findElement(By.name('isbn')).sendKeys("Test");
        await driver.findElement(By.name('author')).sendKeys("Test");
        await driver.findElement(By.name('coverImage')).sendKeys("Test");
        await driver.findElement(By.name('description')).sendKeys("Test");
        await driver.sleep(5000)
        const selectElement = await driver.findElement(By.id('categories'));

        await selectElement.findElement(By.className(`categories`)).click();
        
    
        await driver.sleep(5000)
        await driver.findElement(By.className('btn-primary')).click();

        let notification = await driver.wait(
            until.elementLocated(By.css('.toast-message')),
            10000 
        );
        let notificationText = await notification.getText();

        await driver.sleep(5000)

        if (notificationText.includes("Book added successfully!")) {
            console.log("Toastr success notification appeared correctly");
        }
        if (notificationText.includes("exists.")) {
            console.log("Toastr error notification appeared correctly.");
        }
    } finally {
        await driver.quit();
    }
})();
