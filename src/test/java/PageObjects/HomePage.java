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
    //Sigin link Top Right
    @FindBy(xpath = "//a[@class='login']")
    WebElement lnkSignin;
    //Authentication text
    @FindBy(xpath = "//h1[contains(text(),'Authentication')]")
    WebElement lblAuthentication;
    //Contact Us link on Right Top
    @FindBy(id="contact-link")
    WebElement lnkContactUs;
    //Search text field
    @FindBy(id="search_query_top")
    WebElement txtSearch;
    //Search Button
    @FindBy(xpath = "//form[@id='searchbox']/button")
    WebElement btnSearch;

    @FindBy(xpath = "//*[@id='block_top_menu']/ul/li[1]/a")
    WebElement navMenuWomen;
    @FindBy(xpath = "//*[@id='block_top_menu']/ul/li[1]/a")
    WebElement navMenuDress;
    @FindBy(xpath = "//*[@id='block_top_menu']/ul/li[1]/a")
    WebElement navMenuTshirts;

    //Tab labels POPULAR and BEST SELLERS
    @FindBy(xpath = "//a[contains(text(),'Popular')]")
    WebElement tabLblPopular;
    @FindBy(xpath = "//a[contains(text(),'Best Sellers')]")
    WebElement tabLblBestSellers;


    //Create Page Constructor which initialize driver amd elements of this page
    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        waitHelper = new WaitHelper(driver);
        waitHelper.waitForElement(navMenuWomen,30);
        selenium = new SeleniumHelper(driver);
        log.info(String.format("HomePage initiated with all its elements"));
    }

    public boolean verifyHomePage(){
        boolean blnFlag = false;
        try{
            blnFlag = selenium.Exist("//a[@class='login']");
            return blnFlag;
        }catch (Exception ex){
            log.error(String.format("Getting Exception as %s",ex.getMessage()));
            ErrDescription = String.format("Getting Exception as %s",ex.getMessage());
            return blnFlag = false;
        }
    }

    public boolean clicOnSignIn() {
        boolean blnFlag = false;
        try {
            //Click on signin link on righ top of page and wait
            blnFlag = selenium.Click(lnkSignin);
            if(blnFlag){
                waitHelper.waitForElement(lblAuthentication, 30);
                String Authentication = selenium.GetText(lblAuthentication);
                log.info(Authentication);
                if(Authentication.equalsIgnoreCase("Authentication")) {
                    return true;
                }
                else{
                    ErrDescription=String.format("Observed Text <b>%s</b> does not match with expected text %s",Authentication,"Authentication");
                    return false;
                }
            }else{
                ErrDescription=selenium.ErrDescription;//String.format("Not able to click on SignIn link");
                return false;
            }
        } catch (Exception ex) {
            log.error(String.format("Not able to click on SignIn getting error %s", ex.getMessage()));
            ErrDescription = String.format("Not able to click on SignIn getting error %s", ex.getMessage());
            return blnFlag;
        }
    }
}
