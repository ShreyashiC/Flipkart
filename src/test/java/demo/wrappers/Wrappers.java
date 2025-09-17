package demo.wrappers;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wrappers {
    WebDriver driver;
    WebDriverWait wait;

    // ---------------- Constructor ----------------
    public Wrappers(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ---------------- Wrapper Methods ----------------

    // Open URL
    public void openUrl(String url) {
        driver.get(url);
    }

    // Click
    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // Type
    public void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    // Press Enter
    public void pressEnter(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.sendKeys(Keys.ENTER);
    }

    // Get Text
    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    // Get Elements
    public List<WebElement> getElements(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    // Get Elements' Text List
    public List<String> getElementsText(By locator) {
        List<WebElement> elements = getElements(locator);
        return elements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    // Check if element is present
    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Close Browser
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
