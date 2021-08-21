package TestScripts;

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
    private String strDescription = "";
    Random random = new Random();

    /**
     //######################################################################################################
     //# Test Data:  TestData can be added in dicTestData.put(k,v)
     //#######################################################################################################
     */
    String strPreFix = "Mr.";
    String strCreateEmail = "Auto"+random.nextInt(0x186a0)+"@gmail.com";
    String fName = "Auto";
    String lName = "Test";
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
        // Step# 2: Test Case Objective
        strDescription = "<b> Test Description: </b><br> Verify user is able to Register with valid details</br> ";
        reportStep(testContext, "INFO", strDescription);
        // Step# 3: Launch application
        blnStep = launchApplication(dicConfig.get("strApplicationURL"));
        if (blnStep) {
            reportStep(testContext, "PASS", "strApplicationURL <b>" + dicConfig.get("strApplicationURL") + "</b> launched successfully");
        } else {
            reportStep(testContext, "FAIL", "strApplicationURL <b>" + dicConfig.get("strApplicationURL") + "</b> fail to launch");
        }

        // Step# : Create Login Page Objects
        RegisterNewUserPage regiUserPage = new RegisterNewUserPage(driver);

        // Step# 4: Click on Sign In link on right Top
        blnStep = regiUserPage.clicOnSignIn();
        if (blnStep) {
            reportStep(testContext, "PASS", "<b>SignIn</b> link clicked");
        } else {
            reportStep(testContext, "FAIL", "<b>SignIn</b> link not clicked and error is as - ");
        }
        // Step# 5: Enter Email Add to Register
        blnStep = regiUserPage.enterEmailCreateAcc(strCreateEmail);
        if (blnStep) {
            reportStep(testContext, "PASS", "Email <b> "+strCreateEmail+"</b> Entered clicked on Register");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 6: Select Prefix
        blnStep = regiUserPage.selectPrefix(strPreFix);
        if (blnStep) {
            reportStep(testContext, "PASS", "Prefix <b> "+strPreFix+"</b> Selected");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 7: Enter Personal Information
        fName = regiUserPage.generateRandString(5);
        lName = regiUserPage.generateRandString(5);
        personalInfo = String.format("First Name: <b>%s</b><br>Last Name: <b>%s</b></br><br>Password: <b>%s</b></br> entered successfully",fName,lName,strPassword);
        blnStep = regiUserPage.fillPersonalInfo(fName,lName,strPassword);
        if (blnStep) {
            reportStep(testContext, "PASS",personalInfo);
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 8: Enter Personal Information DOB
        blnStep = regiUserPage.selectDOB(strDays,strMonth,strYears);
        if (blnStep) {
            reportStep(testContext, "PASS",String.format("Date Of Birth <b>%s-%s-%s</b> entered",strDays,strMonth,strYears));
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 9: Enter Personal Information Address
        blnStep = regiUserPage.fillYourAddress(fName,lName,"Coforge","Punta Gorda","Charlotte");
        if (blnStep) {
            reportStep(testContext, "PASS",addressInfo);
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 10: Enter Personal Information Address
        //blnStep = regiUserPage.fillYourAddress(fName,lName,"Coforge","Punta Gorda, FL 33955, United States","Charlotte");
        blnStep = regiUserPage.selectState("Florida");
        if (blnStep) {
            reportStep(testContext, "PASS",String.format("State <b>%s</b> selected","Florida"));
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        //Step 11 Select Country United States
        blnStep = regiUserPage.selectCountry("United States");
        if (blnStep) {
            reportStep(testContext, "PASS",String.format("Country <b>%s</b> selected","United States"));
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 11: Enter Personal Information Zip code
        blnStep = regiUserPage.enterZipCode(strZip);
        if (blnStep) {
            reportStep(testContext, "PASS","Zip Code: <b>"+strZip+"</b> entered successfully");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 12: Enter Personal Information Phone numbers
        blnStep = regiUserPage.fillMobileDetails("9412351035","9412351035","Punta Gorda, FL 33955, United States");
        if (blnStep) {
            reportStep(testContext, "PASS","Zip Code: <b>"+strZip+"</b> entered successfully");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }

        // Step# 13: Click on Register button
        blnStep = regiUserPage.clickRegister();
        if (blnStep) {
            reportStep(testContext, "PASS","Register button clicked successfully");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }
        // Step# 14: Click on logout
        blnStep = regiUserPage.logOutApp();
        if (blnStep) {
            reportStep(testContext, "PASS","Logout clicked successfully");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }



    }
}
