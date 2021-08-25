package PageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.ubs.dis.framework.selenium.SeleniumHelper;
import org.ubs.dis.framework.selenium.WaitHelper;
import org.ubs.dis.framework.utilities.LoggerHelper;

public class MyAccountPage {
    /**
     //#########################################################################
     //# Page Name: MyAccountPage
     //# Description: Page after login
     //# -----------------------------------------------------------------------
     //# Objective: This class will store all elements on this page and will
     //#            initialise all objects
     //# -----------------------------------------------------------------------
     //# Created By: Varsha Singh
     //# Created Dated: 20-Aug-2021
     //# -----------------------------------------------------------------------
     //# History:
     //#########################################################################
     */

    //Create Instance variable
    private WebDriver driver;
    private final Logger log = LoggerHelper.getLogger(MyAccountPage.class);
    WaitHelper waitHelper;
    SeleniumHelper selenium;
    public String ErrDescription="";

    /**
     //###################################################################
     //# All Elements (Object Repository)
     //# #################################################################
     */
    @FindBy(xpath = "//*[contains(text(),'Welcome to your account. Here you can manage all of your personal information and orders')]")
    WebElement msgAccWelcome;

    @FindBy(xpath = "//*[contains(text(),'Order history and details')]")
    WebElement lnkOrderHistoryDetails;


    //Create Page Constructor which initialize driver amd elements of this page
    public MyAccountPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        waitHelper = new WaitHelper(driver);
        waitHelper.waitForElement(lnkOrderHistoryDetails,30);
        selenium = new SeleniumHelper(driver);
        log.info(String.format("My Account Page initiated with all its elements"));
    }


}
