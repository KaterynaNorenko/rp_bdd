package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DriverManager {
    private static final String CHROME_DRIVER_PATH_MAC_OS = "src/test/resources/chromedriver";
    private static final String CHROME_DRIVER_PATH_WINDOWS = "src/test/resources/chromedriver.exe";
    private static final int IMPLICIT_WAIT_TIMEOUT = 10;
    private static final int PAGE_LOAD_TIMEOUT = 60;
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void setupDriver() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("os.name").contains("Windows")
                                                      ? CHROME_DRIVER_PATH_WINDOWS
                                                      : CHROME_DRIVER_PATH_MAC_OS);
        WebDriver driver = new ChromeDriver(Capabilities.getChromeOptions());
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, SECONDS);
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, SECONDS);
        driverThreadLocal.set(driver);
    }

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            setupDriver();
        }
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        Optional.ofNullable(getDriver()).ifPresent(WebDriver::quit);
        driverThreadLocal.remove();
    }
}
