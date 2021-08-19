package org.ubs.dis.framework.selenium;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.Optional;
import org.ubs.dis.framework.utilities.LoggerHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SeleniumHelper {
    //HashMap<String,String> dicConfig;
    private Logger log = LoggerHelper.getLogger(SeleniumHelper.class);
    private WebDriver driver;
    protected String ErrDescription = "";
    //private String driverType=TestBase.dicConfig.get("SeleniumVariant").toLowerCase();
    //protected Browser browser;
    public static HashMap<String, String> dicProjectVar;
    public static ExtentReports extent;
    public static ExtentTest test;

    ITestContext testContext;

    //Constructor
    public SeleniumHelper(WebDriver driver){
        this.driver  = driver;
    }

    public void setdriver1(@Optional String browserName, ITestContext testContext){
        HashMap<String, String> time= new HashMap<String, String>();
        WebDriverFactory base= new WebDriverFactory();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();

        time.put("StartdateTime",dateFormat.format(date));
        base.launchBrowser(browserName);
        TestBase.dicTestData=base.getDicTestData();
        //browser= new Browser(testContext.getCurrentXmlTest().getName());
        driver=base.getDriver();
        TestBase.dicResult.put(testContext.getCurrentXmlTest().getName(), time);
    }


    public void Quit(ITestContext testContext){
        try{
            driver.quit();
        }catch (Exception e){
            log.error("Error while closing driver");
        }
    }

    /**
     * switchToFrame by Frame Name
     * @param frameName
     * @return boolean
     * @author Varsha Singh
     */
    public boolean switchToFrame(String frameName){
        boolean flag = true;
        try{
            driver.switchTo().frame(frameName);
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
            log.error(String.format("Not able to switch to frame name %s and getting error as %s",frameName, e.getMessage()));
        }
        return flag;
    }

    /**
     * switchToFrame by frame index
     * @param index
     * @return boolean
     * @author Varsha Singh
     */
    public boolean switchToFrame(int index){
        boolean flag = true;
        try{
            driver.switchTo().frame(index);
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
            log.error(String.format("Not able to switch to frame index %s and getting error as %s",index, e.getMessage()));
        }
        return flag;
    }

    /**
     * switchToFrame by frame element
     * @param element
     * @return boolean
     * @author Varsha Singh
     */
    public boolean switchToFrame(WebElement element){
        boolean flag = true;
        try{
            driver.switchTo().frame(element);
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
            log.error(String.format("Not able to switch to frame element %s and getting error as %s",element.toString(), e.getMessage()));
        }
        return flag;
    }

    /***
     * This function get current window name as a string.
     * @return String
     * @author Varsha Singh
     */
    public String GetCurrentWindowHandle(){
        try{
            return driver.getWindowHandle();
        }
        catch (Exception e){
            ErrDescription = e.getMessage();
            log.error(String.format("Not able to get Window handle and getting error as %s",e.getMessage()));
        }
        return "Error";
    }

    /**
     * This function to used to switch to default content.
     * @return True/False
     * @author Varsha Singh
     */
    public boolean SwitchToDefaultContent()
    {
        boolean flag = true;
        try{
            driver.switchTo().defaultContent();
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
            log.error(String.format("Not able to switch to default web window and getting error as \n%s",e.getMessage()));
        }
        return flag;
    }

    /***
     * This function selects the browser window on which passed object is found
     * @param strObjectXpath -: Object XPath
     * @return True/False
     * @author Varsha Singh
     */
    public boolean SelectWindowByObject(String strObjectXpath){
        boolean flag ;
        boolean isObjectFound = false;
        try{
            for(String strWindowHandle : driver.getWindowHandles()){
                driver.switchTo().window(strWindowHandle);
                if (driver.findElements(By.xpath(strObjectXpath)).size() > 0){
                    isObjectFound = true;
                    flag = true;
                    break;
                }
                else
                    continue;
            }

            if (!isObjectFound)
                flag = false;
                log.error(String.format("No object found with xpath %s in any of the opened windows. ",strObjectXpath));
                throw new Exception("No object found with xpath " + strObjectXpath + " in any of the opened windows.");
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
            log.error(String.format("Exception found with xpath %s in any of the opened windows. error is \n%s ",strObjectXpath,e.getMessage()));
        }
        return flag;
    }

    /***
     * This function selects the browser window on which passed object is found and then close that
     * @param strObjectXpath -: Object XPath
     * @return True/False
     * @author Varsha Singh
     */
    public boolean CloseWindowByObject(String strObjectXpath){
        boolean flag = false;
        boolean isObjectFound = false;
        List<WebElement> eleNewWindow = null;
        try{
            for(String strWindowHandle : driver.getWindowHandles()){
                driver.switchTo().window(strWindowHandle);
                eleNewWindow = driver.findElements(By.xpath(strObjectXpath));
                if (eleNewWindow.size() > 0){
                    driver.close();
                    isObjectFound = true;
                    break;
                }
                else
                    continue;
            }

            if (!isObjectFound)
                log.error(String.format("No object found with xpath %s in any of the opened windows. so not able to close",strObjectXpath));
                throw new Exception("No object found with xpath " + strObjectXpath + " in any of the opened windows.");
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
            log.error(String.format("No object found with xpath %s and getting error as \n%s",strObjectXpath,e.getMessage()));
        }
        return flag;
    }

    //All Java Script related functions

    /**
     * This function scroll element to visible porting.
     * @return True/False
     * @author Varsha Singh
     */
    public boolean ScrollToElement(WebElement element){
        boolean flag = false;
        try{

                try{
                    ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
                }
                catch(Exception e){
                    log.error(String.format(e.getMessage()));
                    flag = false;
                    ErrDescription = e.getMessage();
                }
                return flag = true;
        }
        catch(Exception e){
            log.error(String.format(e.getMessage()));
            ErrDescription = e.getMessage();
            return false;
        }
    }

}
