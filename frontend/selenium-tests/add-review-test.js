const { Builder, By, until } = require('selenium-webdriver');
const { login } = require('./seleniumUtils');

(async function() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://localhost:4200');

        await driver.findElement(By.id('login')).click();

        await login(driver, 'iviv2', 'iviv2');

        await driver.wait(
            until.elementLocated(By.id('navbarDropdown')),
            10000
        );
        
        await driver.findElement(By.id('navbarDropdown')).click();

        await driver.findElement(By.id('view-all')).click();

        await driver.wait(
            until.elementLocated(By.className('card')),
            10000 
        );
        await driver.findElement(By.className('card')).click();
    
        await driver.wait(
            until.elementLocated(By.name('rating')),
            10000
        );

        await driver.findElement(By.name('rating')).sendKeys(5);
        await driver.findElement(By.name('comment')).sendKeys("perfect");
        await driver.sleep(4000)
        await driver.findElement(By.className('btn-primary')).click();

        let notification = await driver.wait(
            until.elementLocated(By.css('.toast-message')),
            10000 
        );
        let notificationText = await notification.getText();

        if (notificationText.includes("You have already reviewed this book!")) {
            console.log("Toastr warning notification appeared correctly.");
        }
        if (notificationText.includes("Review added successfully!")) {
            console.log("Toastr success notification appeared correctly.");
        }
    } finally {
        await driver.quit();
    }
})();
