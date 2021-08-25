package PageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.ubs.dis.framework.selenium.SeleniumHelper;
import org.ubs.dis.framework.selenium.WaitHelper;
import org.ubs.dis.framework.utilities.LoggerHelper;

public class HomePage {
    /**
     //#########################################################################
     //# Page Name: HomePage
     //# Description: This is first page after launching url
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
    private final Logger log = LoggerHelper.getLogger(HomePage.class);
    WaitHelper waitHelper;
    SeleniumHelper selenium;
    public String ErrDescription="";

    /**
     //###################################################################
     //# All Elements (Object Repository)
     //# #################################################################
     */

    @FindBy(xpath = "//*[@id='block_top_menu']/ul/li[1]/a")
    WebElement navMenuWomen;
    @FindBy(xpath = "//*[@id='block_top_menu']/ul/li[1]/a")
    WebElement navMenuDress;
    @FindBy(xpath = "//*[@id='block_top_menu']/ul/li[1]/a")
    WebElement navMenuTshirts;


    //Create Page Constructor which initialize driver amd elements of this page
    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        waitHelper = new WaitHelper(driver);
        waitHelper.waitForElement(navMenuWomen,30);
        selenium = new SeleniumHelper(driver);
        log.info(String.format("HomePage initiated with all its elements"));
    }
}
