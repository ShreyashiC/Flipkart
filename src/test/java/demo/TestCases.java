package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    Wrappers wrapper;

    //add comments for verification

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        wrapper = new Wrappers(driver);
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
@Test
    public void testCase01() {
         /** Step 1: Navigate to Flipkart */
        wrapper.openUrl("https://www.flipkart.com/");
        System.out.println("Navigated to Flipkart website");    

       
         /** Step 2: Search for Washing Machine */
        wrapper.type(By.name("q"), "Washing Machine");
        wrapper.pressEnter(By.name("q"));
        System.out.println("Typed Washing Machine into search box");

        /** Step 3: Sort results by popularity */
        wrapper.click(By.xpath("//div[contains(text(),'Popularity')]"));

        /** Step 4: Fetch all ratings and count <= 4 star items */
        List<String> ratings = wrapper.getElementsText(By.xpath("//span[contains(@id,'productRating')]"));

        int count = 0;
        for (String r : ratings) {
            try {
                double val = Double.parseDouble(r);
                if (val <= 4.0) {
                    count++;
                }
            } catch (Exception ignored) {
               
            }
        }

         /** Step 6: Print the result */
        System.out.println("Number of items with rating <= 4 stars: " + count);
    }

   @Test
public void testCase02() {
    /** Step 1: Navigate to Flipkart */
    wrapper.openUrl("https://www.flipkart.com/");
    System.out.println("Navigated to Flipkart website");

    try {
        List<WebElement> closeBtns = wrapper.getElements(By.xpath("//button[contains(text(),'✕')]"));
        if (!closeBtns.isEmpty()) {
            closeBtns.get(0).click();
            System.out.println("Closed login popup");
        }
    } catch (Exception e) {
        System.out.println("No login popup found, continuing...");
    }
    /** Step 2: Search for iPhone */
    wrapper.type(By.name("q"), "iPhone");
    wrapper.pressEnter(By.name("q"));
    System.out.println("Typed iPhone into search box");

    /** Step 3: Locate all discount elements */
    List<WebElement> discountElements = wrapper.getElements(By.xpath("//div[@class='UkUFwK']"));

    /** Step 4: Iterate through discounts */
    for (WebElement element : discountElements) {
        String discountText = element.getText().trim().replace("% off", "").trim();

        try {
            int discount = Integer.parseInt(discountText);
            if (discount > 17) {
                
                WebElement titleElement = element.findElement(
                        By.xpath(".//ancestor::div[contains(@class,'yKfJKb')][1]//div[@class='KzDlHZ']")
                );
                String iphoneTitle = titleElement.getText().trim();

                System.out.println("Title: " + iphoneTitle + " | Discount: " + discount + "% off");
            }
        } catch (NumberFormatException ignored) {
            
        }
    }
}

@Test
public void testCase03() {
    // Step 1: Navigate to Flipkart
    wrapper.openUrl("https://www.flipkart.com/");
    System.out.println("Navigated to Flipkart website");

    // Step 2: Close login popup if present
    try {
        List<WebElement> closeBtns = wrapper.getElements(By.xpath("//button[contains(text(),'✕')]"));
        if (!closeBtns.isEmpty()) {
            closeBtns.get(0).click();
            System.out.println("Closed login popup");
        }
    } catch (Exception e) {
        System.out.println("No login popup found, continuing...");
    }

    // Step 3: Search for Coffee Mug
    wrapper.type(By.name("q"), "Coffee Mug");
    wrapper.pressEnter(By.name("q"));
    System.out.println("Typed Coffee Mug into search box");

    // Step 4: Filter 4 stars and above
    try {
        wrapper.click(By.xpath("//div[text()='4★ & above']"));
        System.out.println("Selected 4 stars & above filter");
    } catch (Exception ignored) {}

    // Step 5: Collect all product cards after filter
    // UPDATED LOCATOR based on current Flipkart structure
    List<WebElement> productCards = wrapper.getElements(By.cssSelector("div.slAVV4"));
    System.out.println("Total product cards found: " + productCards.size());

    // Step 6: Store products with review counts using HashSet to avoid duplicates
    List<Map<String, String>> productsList = new ArrayList<>();
    HashSet<String> seenTitles = new HashSet<>();

    for (WebElement card : productCards) {
        try {
            String title = card.findElement(By.cssSelector("a.wjcEIp")).getText();
            if (seenTitles.contains(title)) continue;

            String imageUrl = card.findElement(By.cssSelector("img.DByuf4")).getAttribute("src");

            String reviewsText = card.findElement(By.cssSelector("span.Wphh3N")).getText();
            String numeric = reviewsText.replaceAll("[^0-9]", "");
            int reviews = numeric.isEmpty() ? 0 : Integer.parseInt(numeric);

            Map<String, String> productData = new HashMap<>();
            productData.put("title", title);
            productData.put("imageUrl", imageUrl);
            productData.put("reviews", String.valueOf(reviews));
            productsList.add(productData);
            seenTitles.add(title);
        } catch (Exception ignored) {}
    }

    // Step 7: Sort products by review count descending
    productsList.sort((p1, p2) -> Integer.parseInt(p2.get("reviews")) - Integer.parseInt(p1.get("reviews")));

    // Step 8: Print top 5 products
    System.out.println("Top 5 Coffee Mugs by number of reviews:");
    for (int i = 0; i < Math.min(5, productsList.size()); i++) {
        Map<String, String> product = productsList.get(i);
        System.out.println((i + 1) + ". Title: " + product.get("title") +
                           ", Image URL: " + product.get("imageUrl") +
                           ", Reviews: " + product.get("reviews"));
    }
}



}
