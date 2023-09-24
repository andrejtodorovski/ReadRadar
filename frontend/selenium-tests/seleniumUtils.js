const { By, until } = require('selenium-webdriver');

async function login(driver, username, password) {
    await driver.findElement(By.name('username')).sendKeys(username);
    await driver.findElement(By.name('password')).sendKeys(password);
    await driver.findElement(By.css('button[type="submit"]')).click();
}

async function register(driver, username, password, email, profilePicture, role) {
    await driver.findElement(By.name('username')).sendKeys(username);
    await driver.findElement(By.name('password')).sendKeys(password);
    await driver.findElement(By.name('email')).sendKeys(email);
    await driver.findElement(By.name('profilePicture')).sendKeys(profilePicture);
    let roleDropdown = await driver.findElement(By.name('role'));
    let optionToSelect = await roleDropdown.findElement(By.xpath(`./option[text()="${role}"]`));
    await optionToSelect.click();
    await driver.sleep(1000);
    await driver.findElement(By.css('button[type="submit"]')).click();
}

async function elementExists(driver, locator) {
    try {
        await driver.findElement(locator);
        return true;
    } catch (error) {
        return false;
    }
}

module.exports = { login, elementExists, register };
