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
    String strCreateEmail = "Auto"+random.nextInt(0x186a0)+"@gmail.com";
    String strPassword = "Vaanya@03";

    /**
     //############################################################
     //# Test Data:  TestData can be added in dicTestData.put(k,v)
     //############################################################
     */
    //Test Methode for valid credentials
    @Test(description = "Login with valid credentials")
    public void RegisterWithValid(ITestContext testContext) {
        // Step# 2: Test Case Objective
        strDescription = "<b> Test Description: </b><br> Verify user is able to login with valid credentials</br> ";
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

        blnStep = regiUserPage.enterEmailCreateAcc(strCreateEmail);
        if (blnStep) {
            reportStep(testContext, "PASS", "<b>SignIn</b> link clicked");
        } else {
            reportStep(testContext, "FAIL", regiUserPage.ErrDescription);
        }
    }
}
