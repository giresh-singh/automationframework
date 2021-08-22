package PageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.ubs.dis.framework.selenium.WaitHelper;
import org.ubs.dis.framework.utilities.LoggerHelper;

import java.util.List;
import java.util.Random;

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
        log.info(String.format("RegisterNewUserPage initiated with all its elements"));
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
            ErrDescription = String.format("Not able to click on SignIn getting error %s", ex.getMessage());
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

    public boolean selectPrefix(String val){
        String prefix = val.toLowerCase();
        switch(prefix){
            case "mr.":
                radioPrefixMr.click();
                return true;
            case "mrs.":
                radioPrefixMrs.click();
                return true;
            default:
                ErrDescription = String.format("Option <b>%s</b> is not correct option",val);
        }
        return false;
    }

    public boolean fillPersonalInfo(String fName,String lName, String password){
        boolean blnFlag = false;
        try{
            txtCustomerFName.sendKeys(fName);
            Threadwait();
            txtCustomerLName.sendKeys(lName);
            Threadwait();
            txtSetPassword.sendKeys(password);
            Threadwait();
            return blnFlag=true;
        }catch(Exception ex){
            ErrDescription = ex.getMessage();
            log.error(ex.getMessage());
            return blnFlag;
        }
    }

    public boolean selectDOB(String strDays,String strMonth,String strYears){
        boolean blnFlag = false;
        blnFlag = Select(dropDOBDate,strDays,"byvalue");
        if(!blnFlag){
            ErrDescription = String.format("Not able to select Date %s from DOB drop down",strDays);
            return blnFlag=false;
        }
        blnFlag = Select(dropDOBMonth,strMonth,"byvalue");
        if(!blnFlag){
            ErrDescription = String.format("Not able to select Month %s from DOB drop down",strMonth);
            return blnFlag=false;
        }
        blnFlag = Select(dropDOBYear,strYears,"byvalue");
        if(!blnFlag){
            ErrDescription = String.format("Not able to select Year %s from DOB drop down",strYears);
            return blnFlag=false;
        }
        return true;
    }

    public boolean fillYourAddress(String fName,String lName, String company,String address1,String strCity){
        boolean blnFlag = false;
        try{
            txtFName.clear();
            txtFName.sendKeys(fName);
            Threadwait();
            txtLName.clear();
            txtLName.sendKeys(lName);
            Threadwait();
            txtCompany.sendKeys(company);
            Threadwait();
            txtAddressLine1.sendKeys(address1);
            Threadwait();
            txtAddressLine2.sendKeys(address1);
            Threadwait();
            txtCity.sendKeys(strCity);
            Threadwait();
            return blnFlag=true;
        }catch(Exception ex){
            ErrDescription = ex.getMessage();
            log.error(ex.getMessage());
            return blnFlag;
        }
    }

    public boolean selectState(String strStateName){
        return Select(dropState,strStateName,"visibletext");
    }

    public boolean enterZipCode(String strZip){
        try {
            txtZipCode.sendKeys(strZip);
            Threadwait();
            return true;
        }catch (Exception ex){
            ErrDescription = ex.getMessage();
            return  false;
        }
    }

    public boolean selectCountry(String strCountryName){

        return Select(dropCountry,strCountryName,"visibletext");
    }

    public boolean fillMobileDetails(String strHomePhone, String strMobile,String strAliasAdd){
        try {
            txtHomePhone.sendKeys(strHomePhone);
            Threadwait();
            txtMobilePhone.sendKeys(strMobile);
            Threadwait();
            txtAliasAddress.click();
            Threadwait();
            txtAliasAddress.clear();
            Threadwait();
            txtAliasAddress.sendKeys(strAliasAdd);
            Threadwait();
            return true;
        }catch(Exception ex){
            ErrDescription = ex.getMessage();
            return false;
        }
    }

    public boolean clickRegister(){
        try {
            btnRegister.click();
            Threadwait(10);
            return true;
        }catch(Exception ex){
            ErrDescription = ex.getMessage();
            return false;
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
            ErrDescription = ex.getMessage();
            return  blnFlag;
        }
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
        }
        return flag;
    }

    /***
     * This function perform wait operation.
     * @param WaitInSecond Optional Timeout (default - 1 Sec)
     * @return True/False
     * @author Varsh Singh
     * @Since 18/Aug/2021
     */
    public boolean Threadwait(int... WaitInSecond){
        boolean flag=false;
        int Timeout=1000;
        if(WaitInSecond.length!=0){
            Timeout=Timeout*WaitInSecond[0];
        }
        try{
            Thread.sleep(Timeout);
        }catch(Exception ex){
            flag=false;
        }
        return flag;
    }

    public String generateRandString(int targetStringLength){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

}
