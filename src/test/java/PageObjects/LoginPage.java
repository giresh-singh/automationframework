package PageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.ubs.dis.framework.selenium.SeleniumHelper;
import org.ubs.dis.framework.selenium.TestBase;
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
    SeleniumHelper selenium;
    public String ErrDescription="";
    int intElementWait = Integer.parseInt(TestBase.dicProjectVar.get("elementWait"));
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

    //Create Account text
    @FindBy(xpath = "//h1[contains(text(),'Create an account')]")
    WebElement lblCreateAnAccount;

    @FindBy(id = "email_create")
    WebElement txtEmailCreate;

    @FindBy(id = "SubmitCreate")
    WebElement btnCreateAccount;


    //Create Page Constructor which initialize driver amd elements of this page
    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        waitHelper = new WaitHelper(driver);
        waitHelper.waitForElement(lnkSignin, intElementWait);
        selenium = new SeleniumHelper(driver);
        log.info(String.format("LoginPage initiated with all its elements"));
    }

    public boolean SignInWithValidCred(String strUserName,String strPassword){
        boolean blnFlag = false;
        try{
            blnFlag = selenium.Type(txtEmailAdd,strUserName);
            if(blnFlag){
                blnFlag = selenium.Type(txtPassword,strPassword);
                if(blnFlag){
                    blnFlag = selenium.Click(btnSignIn);
                    waitHelper.waitForElement(lblMyAccount,intElementWait);
                    if(blnFlag){
                        String myAcc = selenium.GetText(lblMyAccount);
                        if(myAcc.equalsIgnoreCase("My account")){
                            log.info(String.format("User ID: %s, Pwd: %s, entered and clicked on SignIn button",strUserName,strPassword));
                            return blnFlag=true;
                        }else{
                            ErrDescription=String.format("Observed Text <b>%s</b> does not match with expected text %s",myAcc,"My account");
                            log.error(String.format("Observed Text <b>%s</b> does not match with expected text %s",myAcc,"My account"));
                            return blnFlag;
                        }
                    }else{
                        ErrDescription=String.format("Not able to click on <b>%s</b> button","Sign In");
                        log.error(String.format("Not able to click on <b>%s</b> button","Sign In"));
                        return blnFlag;
                    }
                }else{
                    ErrDescription= selenium.ErrDescription+String.format("Not able to enter Password <b>%s</b>",strPassword);
                    log.error(String.format("Not able to enter Password <b>%s</b>",strPassword));
                    return blnFlag;
                }
            }else{
                ErrDescription = selenium.ErrDescription+String.format("Not able to type <b>%s</b> in user id field",strUserName);
                log.error(String.format("Not able to type <b>%s</b> in user id field",strUserName));
                return blnFlag;
            }
        }catch(Exception ex){
            log.error(String.format("Not able SignIn getting error %s",ex.getMessage()));
            ErrDescription = String.format("Not able SignIn getting error %s",ex.getMessage());
            return  blnFlag;
        }
    }

    public boolean logOutApp(){
        boolean blnFlag = false;
        try{
            //Click on Sign Out link
            blnFlag = selenium.Click(lnklogOut);
            waitHelper.waitForElement(lblAuthentication, intElementWait);
            if(blnFlag){
                String Authentication = selenium.GetText(lblAuthentication);
                log.info(String.format("Text found : %s",Authentication));
                if(Authentication.equalsIgnoreCase("Authentication")) {
                    log.info(String.format("User Logged out successfully"));
                }
                else{
                    ErrDescription=String.format("Observed Text <b>%s</b> does not match with expected text %s",Authentication,"Authentication");
                    log.error(String.format("Observed Text: %s does not match with expected text: %s",Authentication,"AUTHENTICATION"));
                }
            }else{
                ErrDescription = selenium.ErrDescription+String.format("Not able to click on <b>%s</b> button","Logout");
                log.error(String.format("Not able to click on %s button","Logout"));
            }
            return blnFlag;
        }catch(Exception ex){
            log.error(String.format("Not able Logout from application getting error %s",ex.getMessage()));
            ErrDescription = String.format("Not able Logout from application getting error %s",ex.getMessage());
            return  blnFlag;
        }
    }

    public boolean enterEmailCreateAcc(String createEmailId){
        try{
            selenium.ScrollToElement(txtEmailCreate);
            //Enter Valid Email id
            txtEmailCreate.sendKeys(createEmailId);
            String val = txtEmailCreate.getText();
            if(txtEmailCreate.isDisplayed()){
                btnCreateAccount.click();
                WebElement element = waitHelper.waitForElement(lblCreateAnAccount,intElementWait);
                if(element.isDisplayed()){
                    return true;
                }else{
                    ErrDescription = String.format("Not able to navigate to <b>%s</b>","CREATE AN ACCOUNT");
                    return false;
                }
            }else{
                ErrDescription = String.format("Not able to enter email <b>%s</b>",createEmailId);
                return false;
            }
        }catch(Exception ex){
            ErrDescription = String.format(ex.getMessage());
            log.error(ex.getMessage());
            return false;
        }
    }

    public boolean ClickOnCreateAccBtn(){
        boolean blnFlag = false;
        try{
            blnFlag = selenium.Click(btnCreateAccount);
            if(blnFlag){
                log.info(String.format("Clicked on Create An Account button"));
            }else{
                log.error(String.format("Not able to click on Create An Account button getting error as: %s ",selenium.ErrDescription));
                ErrDescription = String.format("Not able to click on Create An Account button getting error as: %s ",selenium.ErrDescription);
            }
            return blnFlag;
        }catch(Exception ex){
            ErrDescription = String.format(ex.getMessage());
            log.error(ex.getMessage());
            return blnFlag;
        }
    }
}
