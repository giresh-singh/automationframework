package org.ubs.dis.framework.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.ubs.dis.framework.utilities.LoggerHelper;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class WaitHelper {
    private WebDriver driver;
    //Initialize logger
    private Logger log = LoggerHelper.getLogger(WaitHelper.class);
    //initialize webdriver
    public WaitHelper(WebDriver driver){
        this.driver = driver;
    }

    public void setImplicitWait(long TimeOut, TimeUnit unit){
        log.info("Implicit wait is set with Timeout "+TimeOut+" "+unit);
        driver.manage().timeouts().implicitlyWait(TimeOut,unit);
    }

    /**
     * getWait : use to get wait object with polling of 500 milli seconds
     * @param timeOutInSeconds
     * @return WebDriverWait object
     */
    private WebDriverWait getWait(int timeOutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.pollingEvery(Duration.ofMillis(500));
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(ElementNotVisibleException.class);
        wait.ignoring(NoSuchFrameException.class);
        return wait;
    }

    /**
     * waitForElementVisible : Wait element to be visible
     * @param element
     * @param timeOutInSeconds
     */
    public void waitForElementVisible(WebElement element, int timeOutInSeconds ){
        log.info(String.format("waiting for element %s with polling time 500 milliseconds and time out %s seconds",element.toString(),timeOutInSeconds));
        WebDriverWait wait = getWait(timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOf(element));
        log.info(String.format("Element %s is visible now",element.toString()));
    }

    /**
     * waitForElementClickable: wait for element to be clickable
     * @param element
     * @param timeOutInSeconds
     */
    public void waitForElementClickable(WebElement element, int timeOutInSeconds ){
        log.info(String.format("waiting for element %s to be clickable",element.toString()));
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        log.info(String.format("Element %s is clickable now",element.toString()));
    }

    /**
     * waitForFrameAvailableAndSwitchToIt : wait for Frame to be available and switch to it, frame locator element
     * @param FrameElement
     * @param timeOutInSeconds
     */
    public void waitForFrameAvailableAndSwitchToIt(WebElement FrameElement, int timeOutInSeconds ){
        log.info(String.format("waiting for Frame to be available and switch to it, frame locator element is: %s ",FrameElement.toString()));
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(FrameElement));
        log.info(String.format("frame (Locator element %s ) is available and also able to switch to it",FrameElement.toString()));
    }

    public Wait<WebDriver> getFluentWait(int timeOutInSeconds){
            Wait<WebDriver> fWait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeOutInSeconds))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
        return fWait;
    }

    public WebElement waitForElement(WebElement element, int timeOutInSeconds){
        Wait<WebDriver> fWait = getFluentWait(timeOutInSeconds);
        fWait.until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    public void pageLoadTime(int timeOutInSeconds){
        log.info(String.format("Waiting for page to be loaded max time %s seconds",timeOutInSeconds));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeOutInSeconds));
        log.info(String.format("Page is Loaded within specified time %s seconds",timeOutInSeconds));
    }

}
