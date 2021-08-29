package TestScripts;

import PageObjects.CommonLib;
import PageObjects.HomePage;
import PageObjects.LoginPage;
import PageObjects.RegisterNewUserPage;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.ubs.dis.framework.selenium.TestBase;
import org.ubs.dis.framework.utilities.LoggerHelper;

import java.util.Random;


/**
 //#########################################################################
 //# Test Name: RegisterNewUser
 //# Test Case Id: 2
 //# Description: Objective of this test is to verify Registration to application
 //# -----------------------------------------------------------------------
 //# Objective: Test Valid email id and password
 //#            All Validation messages
 //# -----------------------------------------------------------------------
 //# Created By: Varsha Singh
 //# Created Dated: 20-Aug-2021
 //# -----------------------------------------------------------------------
 //# History:
 //#########################################################################
 */
public class RegisterNewUserTest extends TestBase {
    private Logger log = LoggerHelper.getLogger(RegisterNewUserTest.class);
    boolean blnStep = false;
    public static String strDescription = "";
    Random random = new Random();

    /**
     //######################################################################################################
     //# Test Data:  TestData can be added in dicTestData.put(k,v)
     //#######################################################################################################
     */
    CommonLib common = new CommonLib();
    String strPreFix = "Mr.";
    String strCreateEmail = "Auto"+random.nextInt(0x186a0)+"@gmail.com";
    String fName = common.generateRandString(5);//"Auto";
    String lName = common.generateRandString(5);//"Test";
    String strPassword = "Vaanya@03";
    String strDays = "1",strMonth = "7",strYears = "1975";

    String personalInfo = String.format("First Name: <b>%s</b><br>Last Name: <b>%s</b></br><br>Password: <b>%s</b></br> entered successfully",fName,lName,strPassword);
    String addressInfo =  String.format("First Name: <b>%s</b><br>Last Name: <b>%s</b></br>" +
            "<br>Company: <b>%s</b></br><br>Address1: <b>%s</b></br><br>City: <b>%s</b></br> " +
            "entered successfully",fName,lName,"Coforge","Punta Gorda, FL 33955, United States","Charlotte");
    String strZip = "33948";
    /** #################################################################################################### */


    //Test Methode for valid credentials
    @Test(description = "Login with valid credentials")
    public void RegisterWithValid(ITestContext testContext) {
        // Step# 1: Test Case Objective
        strDescription = "<b> Test Description: </b><br> Verify user is able to Register with valid details</br> ";
        reportStep(testContext, "INFO", strDescription);

        // Step# 2: Launch application
        blnStep = launchApplication(dicConfig.get("strApplicationURL"));
        if (blnStep) {
            reportStep(testContext, "PASS", "strApplicationURL <b>" + dicConfig.get("strApplicationURL") + "</b> launched successfully");
        } else {
            reportStep(testContext, "FAIL", ErrDescription);
        }

        //Step# 3: Create Home Page Objects
        HomePage homePage = new HomePage(driver);
        reportStep(testContext,"INFO","Home Page object is created");

        //Step# 4: Verify Home Page is launched successfully
        blnStep = homePage.verifyHomePage();
        if(blnStep){
            reportStep(testContext,"PASS","Home page loaded successfully");
        }else{
            reportStep(testContext,"FAIL",homePage.ErrDescription);
            return;
        }
        // Step# 5: Click on Sign In link on right Top
        blnStep = homePage.clicOnSignIn();
        if(blnStep){
            reportStep(testContext,"PASS","<b>SignIn</b> link clicked");
        }else{
            reportStep(testContext,"FAIL",homePage.ErrDescription);
            return;
        }

        // Step# 6: Create Login Page Object
        LoginPage loginPage = new LoginPage(driver);
        reportStep(testContext,"INFO","Login Page object is created");

        // Step# 7: Enter Email Add to Register and click on Create An Account button
        blnStep = loginPage.enterEmailCreateAcc(strCreateEmail);
        if (blnStep) {
            reportStep(testContext, "PASS", "Email <b> "+strCreateEmail+"</b> Entered clicked on Register");
        } else {
            reportStep(testContext, "FAIL", loginPage.ErrDescription);
        }

        // Step# 8: Create Login Page Objects
        RegisterNewUserPage regiUserPage = new RegisterNewUserPage(driver);
        reportStep(testContext,"INFO","Registration Page object is created");

        // Step# 9: Select Prefix
        blnStep = regiUserPage.selectPrefix(strPreFix);
        if (blnStep) {
            reportStep(testContext, "PASS", "Prefix <b> "+strPreFix+"</b> Selected");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 10: Enter Personal Information
        personalInfo = String.format("First Name: <b>%s</b><br>Last Name: <b>%s</b></br><br>Password: <b>%s</b></br> entered successfully",fName,lName,strPassword);
        blnStep = regiUserPage.fillPersonalInfo(fName,lName,strPassword);
        if (blnStep) {
            reportStep(testContext, "PASS",personalInfo);
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 11: Enter Personal Information DOB
        blnStep = regiUserPage.selectDOB(strDays,strMonth,strYears);
        if (blnStep) {
            reportStep(testContext, "PASS",String.format("Date Of Birth <b>%s-%s-%s</b> entered",strDays,strMonth,strYears));
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 12: Enter Personal Information Address
        blnStep = regiUserPage.fillYourAddress(fName,lName,"Coforge","Punta Gorda","Charlotte");
        if (blnStep) {
            reportStep(testContext, "PASS",addressInfo);
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 13: Enter Personal Information Address
        blnStep = regiUserPage.selectState("Florida");
        if (blnStep) {
            reportStep(testContext, "PASS",String.format("State <b>%s</b> selected","Florida"));
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        //Step 14 Select Country United States
        blnStep = regiUserPage.selectCountry("United States");
        if (blnStep) {
            reportStep(testContext, "PASS",String.format("Country <b>%s</b> selected","United States"));
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 15: Enter Personal Information Zip code
        blnStep = regiUserPage.enterZipCode(strZip);
        if (blnStep) {
            reportStep(testContext, "PASS","Zip Code: <b>"+strZip+"</b> entered successfully");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 16: Enter Personal Information Phone numbers
        blnStep = regiUserPage.fillMobileDetails("9412351035","9412351035","Punta Gorda, FL 33955, United States");
        if (blnStep) {
            reportStep(testContext, "PASS","Zip Code: <b>"+strZip+"</b> entered successfully");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 17: Click on Register button
        blnStep = regiUserPage.clickRegister();
        if (blnStep) {
            reportStep(testContext, "PASS","Register button clicked successfully");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 18: Click on logout
        blnStep = loginPage.logOutApp();
        if (blnStep) {
            reportStep(testContext, "PASS","Logout clicked successfully");
        } else {
            reportStep(testContext, "FAIL", loginPage.ErrDescription);
        }
    }
}
