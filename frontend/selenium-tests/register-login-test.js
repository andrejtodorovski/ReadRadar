const { Builder, By } = require('selenium-webdriver');
const { login, elementExists, register } = require('./seleniumUtils');  // Adjust path if necessary

(async function() {
    let driver = await new Builder().forBrowser('chrome').build();

    try {
        await driver.get('http://localhost:4200');

        await driver.findElement(By.id('register')).click();

        await driver.sleep(1000)

        await register(driver, 'iviv2', 'iviv2', 'test@email.com', 'path/to/image.jpg', 'User');
        
        await driver.sleep(1000)

        await login(driver, 'iviv2', 'iviv2');
        
        await driver.sleep(1000)

        const isLoginDisplayed = await elementExists(driver, By.id('login'));
        const isRegisterDisplayed = await elementExists(driver, By.id('register'));
        const isProfileDisplayed = await elementExists(driver, By.id('profile'));
        const isLogoutDisplayed = await elementExists(driver, By.id('logout'));


        if (isProfileDisplayed && isLogoutDisplayed && !isLoginDisplayed && !isRegisterDisplayed) {
            console.log('User has successfully logged in!');
        } else {
            console.error('User has not successfully logged in!');
        }
    } finally {
        await driver.quit();
    }
})();
