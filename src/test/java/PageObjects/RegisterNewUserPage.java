package PageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.ubs.dis.framework.selenium.WaitHelper;
import org.ubs.dis.framework.utilities.LoggerHelper;

public class RegisterNewUserPage {
    /**
     //#########################################################################
     //# Page Name: RegisterNewUserPage
     //# Description: New user can register (Sign UP) and then login with new cred
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
    private Logger log = LoggerHelper.getLogger(RegisterNewUserPage.class);
    WaitHelper waitHelper;
    public String ErrDescription="";
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

    @FindBy(xpath = "//h1[contains(text(),'Create an account')]")
    WebElement lblCreateAnAccount;

    @FindBy(id = "email_create")
    WebElement txtEmailCreate;

    @FindBy(id = "SubmitCreate")
    WebElement btnCreateAccount;

    @FindBy(id = "id_gender1")
    WebElement radioPrefixMr;

    @FindBy(id = "id_gender2")
    WebElement radioPrefixMrs;

    @FindBy(id = "customer_firstname")
    WebElement txtCustomerFName;

    @FindBy(id = "customer_lastname")
    WebElement txtCustomerLName;

    @FindBy(id = "passwd")
    WebElement txtSetPassword;

    @FindBy(id = "days")
    WebElement dropDOBDate;

    @FindBy(id = "months")
    WebElement dropDOBMonth;

    @FindBy(id = "years")
    WebElement dropDOBYear;

    @FindBy(id = "firstname")
    WebElement txtFName;

    @FindBy(id = "lastname")
    WebElement txtLName;

    @FindBy(id = "company")
    WebElement txtCompany;

    @FindBy(id = "address1")
    WebElement txtAddressLine1;

    @FindBy(id = "address2")
    WebElement txtAddressLine2;

    @FindBy(id = "city")
    WebElement txtCity;

    @FindBy(id = "id_state")
    WebElement dropState;

    @FindBy(id = "id_country")
    WebElement dropCountry;

    @FindBy(id = "postcode")
    WebElement txtZipCode;

    @FindBy(id = "phone")
    WebElement txtHomePhone;

    @FindBy(id = "phone_mobile")
    WebElement txtMobilePhone;

    @FindBy(id = "alias")
    WebElement txtAliasAddress;

    @FindBy(id = "submitAccount")
    WebElement btnRegister;



    //Create Page Constructor which initialize driver amd elements of this page
    public RegisterNewUserPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        waitHelper = new WaitHelper(driver);
        waitHelper.waitForElement(lnkSignin,30);
        log.info(String.format("RegisterPage initiated with all its elements"));
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

    public boolean enterEmailCreateAcc(String createEmailId){
        try{
            //Enter Valid Email id
            txtEmailCreate.sendKeys(createEmailId);
            String val = txtEmailCreate.getText();
            if(txtEmailCreate.isDisplayed()){
                btnCreateAccount.click();
                WebElement element = waitHelper.waitForElement(lblCreateAnAccount,30);
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

}
