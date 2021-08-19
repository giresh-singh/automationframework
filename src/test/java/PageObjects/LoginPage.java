package PageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.ubs.dis.framework.selenium.WaitHelper;
import org.ubs.dis.framework.utilities.LoggerHelper;

public class LoginPage {
    /**
    //#########################################################################
    //# Page Name: LoginPage
    //# Description: This is first page after launching url
    //# -----------------------------------------------------------------------
    //# Objective: This class will store all elements on this page and will
    //#            initialise all objects
    //# -----------------------------------------------------------------------
    //# Created By: Varsha Singh
    //# Created Dated: 17-Aug-2021
    //# -----------------------------------------------------------------------
    //# History:
    //#########################################################################
    */

    //Create Instance variable
    private WebDriver driver;
    private final Logger log = LoggerHelper.getLogger(LoginPage.class);
    WaitHelper waitHelper;
    /**
    //###################################################################
    //# All Elements (Object Repository)
    //# #################################################################
     */
    @FindBy(xpath = "//a[@class='login']")
    WebElement lnkSignin;

    @FindBy(xpath = "//a[@class='logout']")
    WebElement lnklogOut;

    @FindBy(xpath = "//h1[contains(text(),'Authentication')]")
    WebElement lblAuthentication;

    @FindBy(xpath = "//h1[contains(text(),'My account')]")
    WebElement lblMyAccount;

    @FindBy(xpath = "//li[contains(text(),'Authentication failed.')]")
    WebElement msgAuthfailed;

    @FindBy(id = "email")
    WebElement txtEmailAdd;

    @FindBy(id = "passwd")
    WebElement txtPassword;

    @FindBy(id = "SubmitLogin")
    WebElement btnSignIn;

    @FindBy(xpath = "//a[contains(text(),'Forgot your password')]")
    WebElement lnkForgotYourPwd;

    @FindBy(id = "email_create")
    WebElement txtEmailCreateAcc;

    @FindBy(id = "SubmitCreate")
    WebElement btnCreateAcc;

    //Create Page Constructor which initialize driver amd elements of this page
    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        waitHelper = new WaitHelper(driver);
        waitHelper.waitForElement(lnkSignin,30);
        log.info(String.format("LoginPage initiated with all its elements"));
    }
    public boolean clicOnSignIn() {
        boolean blnFlag = false;
        try {
            //Click on signin link on righ top of page
            lnkSignin.click();
            //wait for Authentication label to appear
            waitHelper.waitForElement(lblAuthentication, 30);
            log.info(String.format("Clicked on Sigin link ... and Authentication text is available"));
            return blnFlag=true;
        } catch (Exception ex) {
            log.error(String.format("Not able to click on SignIn getting error %s", ex.getMessage()));
            return blnFlag;
        }
    }
    public boolean SignInWithValidCred(String strUserName,String strPassword){
        boolean blnFlag = false;
        try{
            //Enter User Name
            txtEmailAdd.sendKeys(strUserName);
            //Enter Password
            txtPassword.sendKeys(strPassword);
            //Click on SignIn button
            btnSignIn.click();
            //wait for element my account
            waitHelper.waitForElement(lblMyAccount,30);
            log.info(String.format("SignIn with user id %s is successful",strUserName));
            return  blnFlag=true;
        }catch(Exception ex){
            log.error(String.format("Not able SignIn getting error %s",ex.getMessage()));
            return  blnFlag;
        }
    }

    public boolean logOutApp(){
        boolean blnFlag = false;
        try{
            //Click on Sign Out link
            lnklogOut.click();
            //Verify logged out
            waitHelper.waitForElement(lblAuthentication, 30);
            log.info(String.format("Logout is successful....."));
            return blnFlag = true;
        }catch(Exception ex){
            log.error(String.format("Not able Logout from application getting error %s",ex.getMessage()));
            return  blnFlag;
        }
    }


}
