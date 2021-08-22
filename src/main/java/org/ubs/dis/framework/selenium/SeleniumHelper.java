package org.ubs.dis.framework.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.ubs.dis.framework.utilities.LoggerHelper;

import java.util.List;

public class SeleniumHelper {
    private final WebDriver driver;
    private Logger log = LoggerHelper.getLogger(SeleniumHelper.class);
    public String ErrDescription = "";
    public WaitHelper waitHelper;
    public SeleniumHelper(WebDriver driver){
        this.driver = driver;
        waitHelper = new WaitHelper(driver);
    }


    //All Methods working on elements

    /**
     * @Description This function scroll element to visible porting.
     * @return True/False
     * @author Varsha Singh
     */
    public boolean ScrollToElement(WebElement element){
        try{
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
            return true;
        }
        catch(Exception e){
            log.error(String.format(e.getMessage()));
            ErrDescription = e.getMessage();
            return false;
        }

    }

    /***
     * This function perform click operation on 'Element'
     * @return true / false
     * @author Varsha Singh
     */
    public boolean Click(WebElement element) {
        boolean flag = true;
        try{
            element.click();
        }
        catch (Exception e){
            log.error(String.format(e.getMessage()));
            flag = false;
            ErrDescription = e.getMessage();
        }
        return flag;
    }

    /***
     * This function perform click operation on o'Element using Java script.'
     * @return true / false
     * @author Varsha Singh
     */
    public void JSClick(WebElement element) {
        try{
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
        catch (Exception e){
            log.error(String.format(e.getMessage()));
            ErrDescription = e.getMessage();
        }
    }

    /***
     * This function types value into input box .
     * @param strKeyword -: String to be entered.
     * @return true / false
     * @author Varsha Singh
     */
    public boolean Type(WebElement element, String strKeyword){
        boolean flag = false;
        try {
                element.clear();
                element.sendKeys(strKeyword);
                flag = true;
            }catch(Exception ex){
                try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].value=''", element);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + strKeyword + "'", element);
                        return flag = true;
                }catch(Exception e){
                        log.error(String.format(e.getMessage()));
                        flag = false;
                        ErrDescription = e.getMessage();

                }

            }
        return flag;
    }

    /***
     * This function types value into input box using JS.
     * @param strKeyword -: String to be entered.
     * @return true / false
     * @author Varsha Singh
     */
    public boolean JsType(WebElement element,String strKeyword){

        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value=''", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + strKeyword + "'", element);
            return true;
        }
        catch (Exception e){
            log.error(String.format(e.getMessage()));
            ErrDescription = e.getMessage();
            return  false;
        }
    }

    /***
     * This Function is used to check whether element exist or not in a web page.
     * @author Varsha Singh
     * @return True/False
     */
    public boolean Exist(String xpath){
        boolean flag = false;
        try
        {
            if (driver.findElements(By.xpath(xpath)).size() > 0)
            {
                if (driver.findElement(By.xpath(xpath)).isDisplayed()) {
                    log.error(String.format("Element with xpath - %s is visible on PAGE ", xpath));
                    flag = true;
                }
                else {
                    log.error(String.format("Element with xpath - %s is not visible on PAGE ",xpath));
                    throw new Exception("Element with xpath - " + xpath + " is not visible on PAGE ");
                }
            }
            else
                ErrDescription = "Element with xpath '" + xpath + "' is not found on PAGE ";
        }
        catch (Exception e)
        {
            flag = false;
            ErrDescription = e.getMessage();
        }
        return flag;
    }

    /***
     * This function perform mouse hover operation on object passed to 'Element.
     * @author Varsha Singh
     * @return True/False
     */
    public boolean MouseHover(WebElement element){
        boolean blnFlag = true;
        try{
            Actions builder = new Actions(driver);
            builder.moveToElement(element).build().perform();
        }
        catch (Exception e){
            blnFlag = false;
            ErrDescription = e.getMessage();
        }
        return blnFlag;
    }
    /***
     * This function is used to get selected option in a dropdown.
     * @return List<WebElement>
     */
    public List<WebElement> GetSelectedOptions(WebElement element){
        return new org.openqa.selenium.support.ui.Select(element).getAllSelectedOptions();
    }

    /***
     * This function selects the value based on certain condition for 'Element(string strObjectName)'
     * @param strListValue -: Value to be selected
     * @param selectType -: Different Select Type (visibletext,byvalue,byindex,partialtext)
     * @param element Element of select box
     * @return True/false
     */
    public boolean Select(WebElement element,String strListValue,String selectType){
        boolean flag = false;
        try{
            if (element == null){
                return false;
            }
            try
            {
                switch (selectType.toLowerCase()) {
                    case "visibletext":
                        new org.openqa.selenium.support.ui.Select(element).selectByVisibleText(strListValue);
                        return true;
                    case "byvalue":
                        new org.openqa.selenium.support.ui.Select(element).selectByValue(strListValue);
                        return true;
                    case "byindex":
                        new org.openqa.selenium.support.ui.Select(element).selectByIndex(Integer.parseInt(strListValue));
                        return true;
                    case "partialtext":
                        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
                        List<WebElement> lstOptions = select.getOptions();
                        String strvalue="";
                        for(WebElement option:lstOptions){
                            if(option.getText().contains(strListValue)) {
                                strvalue = option.getAttribute("value");
                                break;
                            }
                        }
                        if(!strvalue.equals("")){
                            select.selectByValue(strvalue);
                            return true;
                        }else{
                            ErrDescription = String.format("Option <b>%s</b> is not valid option",strvalue);
                            log.error(String.format("Option %s is not valid option",strvalue));
                            return false;
                        }
                }

            }
            catch (Exception e)
            {
                element.click();
                element.findElement(By.xpath("//*[text()='" + strListValue + "']"));
            }

            flag = true;
        }
        catch (Exception e)
        {
            flag = false;
            ErrDescription = e.getMessage();
            log.error(String.format(e.getMessage()));
        }
        return flag;
    }

    /***
     * This function verifies the visibility of an object passed to 'Element
     * @author Varsha Singh
     * @return True/False
     */
    public boolean isVisible(WebElement element){
        try{
            return element.isDisplayed();
        }
        catch (Exception e){
            ErrDescription = e.getMessage();
            log.error(String.format(e.getMessage()));
        }
        return false;
    }

    /***
     * This function verifies for selected radio or list object passed to 'Element
     * @author Varsha Singh
     * @return True/False
     */
    public boolean isSelected(WebElement element){
        try{
            return element.isSelected();
        }
        catch (Exception e){
            ErrDescription = e.getMessage();
            log.error(String.format(e.getMessage()));
        }
        return false;
    }

    /***
     * This function gets the text over an object passed to 'Element(string strObjectName)'
     * @author Giresh Singh
     * @Since 18/April/2016
     * @return String captured text
     */
    public String GetText(WebElement element) {
        try{
            return element.getText();
        }
        catch (Exception e) {
            ErrDescription = e.getMessage();
            log.error(String.format(e.getMessage()));
        }
        return "ErrorValue";
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

    /***
     * This funstion is used get name of the browser.
     * @return String -BrowserName
     * @author Varsha Singh
     */
    public String BrowserName(){
        String strBrowser="";
        try{
            strBrowser=driver.toString();
            if(strBrowser.contains("chrome")){
                strBrowser="chrome";
            }else if(strBrowser.contains("internet explorer")){
                strBrowser="ie";
            }else if(strBrowser.contains("firefox")){
                strBrowser="firefox";
            }
        }catch(Exception ex){
            strBrowser="default";
        }
        return strBrowser;
    }

    /**
     * This function performs Browser back operation.
     * @return True/False
     * @author Varsha Singh
     */
    public boolean Back(){
        boolean flag = false;
        try{
            driver.navigate().back();
            //Need to implement Wait for Page Load in Wait Helper class
            flag = true;
        }
        catch (Exception e){
            log.error("Error while using browser back function.");
            flag = false;
            ErrDescription= e.getMessage();
        }
        return flag;
    }

    /**
     * This function performs Browser refresh operation.
     * @return True/False
     * @author Varsha Singh
     */
    public boolean Refresh(){
        boolean flag = false;
        try{
            driver.navigate().refresh();
            //Need to implement WaitForPageLoad()
            flag = true;
        }
        catch (Exception e)
        {
            flag = false;
            ErrDescription = e.getMessage();
        }
        return flag;
    }

    /**
     * This function performs Browser maximize operation.
     * @return True/False
     * @author Varsha Singh
     */
    public boolean Maximize() {
        boolean flag = false;
        try{
            driver.manage().window().maximize();
            flag = true;
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
        }
        return flag;
    }

    /**
     *  This function gets the page URL
     * @return True/False
     * @author Varsha Singh
     */
    public String GetURL(){
        String strURL = "";
        try{
            strURL = driver.getCurrentUrl();
        }
        catch (Exception e){
            strURL = "";
            ErrDescription= e.getMessage();
        }
        return strURL;
    }

    /***
     * This function compares the actual page title with the passed string
     * @param strWindowTitle -: Page Title
     * @return -: true / false
     * @author Varsha Singh
     */
    public boolean VerifyTitle(String strWindowTitle){
        boolean flag = false;
        try{
            String strBrowserTitle = "";
            strBrowserTitle = driver.getTitle();
            flag=strBrowserTitle.contains(strWindowTitle);
        }
        catch (Exception e){
            flag = false;
            ErrDescription = e.getMessage();
        }
        return flag;
    }

    /***
     *
     * @param action
     * @return
     */
    public String PerformActionOnAlert(String action){
        String alertMessage = "" ;
        try{
            WebDriverWait wait = new WebDriverWait(driver,5);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alertMessage = alert.getText();
            switch(action.toLowerCase()){
                case "ok":
                case  "accept":{
                    alert.accept();
                    break;
                }
                case  "cancel":
                case"dismiss":{
                    alert.dismiss();
                    break;
                }
            }

        }catch(Exception ex){
            alertMessage ="Exception";
            log.error(ex.getMessage());
        }

        return alertMessage;
    }


}
