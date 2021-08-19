package TestScripts;

import PageObjects.LoginPage;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.ubs.dis.framework.selenium.TestBase;
import org.ubs.dis.framework.utilities.LoggerHelper;

/**
 //#########################################################################
 //# Test Name: LogInTest
 //# Test Case Id: 1
 //# Description: Objective of this test is to verify login to application
 //# -----------------------------------------------------------------------
 //# Objective: Test Valid credential login
 //#            Test invalid credential login messages
 //# -----------------------------------------------------------------------
 //# Created By: Varsha Singh
 //# Created Dated: 17-Aug-2021
 //# -----------------------------------------------------------------------
 //# History:
 //#########################################################################
 */

public class LogInTest extends TestBase {
    private Logger log = LoggerHelper.getLogger(LogInTest.class);
    boolean blnStep = false;
    //Test Methode for valid credentials
    @Test(description = "Login with valid credentials")
    public void logInWithValid(){
        // Step# 2: Test Case Objective
        reportStep("","<b> Test Description: </b><br> Verify user is able to login with valid credentials</br> ");
        // Step# 3: Launch application
        blnStep = launchApplication(dicConfig.get("strApplicationURL"));
        if(blnStep){
            reportStep("PASS","strApplicationURL <b>"+dicConfig.get("strApplicationURL")+"</b> launched successfully");
        }else{
            reportStep("FAIL","strApplicationURL <b>"+dicConfig.get("strApplicationURL")+"</b> fail to launch");
        }

        // Step# : Create Login Page Objects
        LoginPage loginPage = new LoginPage(driver);

        // Step# 4: Click on Sign In link on right Top
        blnStep = loginPage.clicOnSignIn();
        if(blnStep){
            reportStep("PASS","<b>SignIn</b> link clicked");
        }else{
            reportStep("FAIL","<b>SignIn</b> link not clicked and error is as - ");
        }

        // Step# 5: Enter User Id, Password and click on Sign In button
        blnStep = loginPage.SignInWithValidCred(dicConfig.get("userid"),dicConfig.get("password"));
        if(blnStep){
            reportStep("PASS","User id: <b>"+dicConfig.get("userid")+"</b> successfully logged in");
        }else{
            reportStep("FAIL","<b>SignIn</b> link not clicked and error is as - ");
        }

        // Step# 6: Click on Logout button
        blnStep = loginPage.logOutApp();
        if(blnStep){
            reportStep("PASS","User id: <b>"+dicConfig.get("userid")+"</b> successfully logout");
        }else{
            reportStep("FAIL","Not able to logout and error is as - ");
        }

    }

    //Test Methode for invalid credentials
    @Test(description = "Login with invalid credentials")
    public void logInWithInValid(){
        reportStep("","<b> Test Description: </b><br> Verify user is not able to login with invalid credentials ");
        launchApplication(dicConfig.get("strApplicationURL"));
        reportStep("PASS","strApplicationURL <b>"+dicConfig.get("strApplicationURL")+"</b> launched successfully");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clicOnSignIn();
        reportStep("PASS","<b>SignIn</b> link clicked");
        loginPage.SignInWithValidCred(dicConfig.get("userid"),dicConfig.get("password"));
        reportStep("PASS",String.format("User id: <b> %s </b> and password: <b> %s </b> enter and clicked on Sign In button",dicConfig.get("userid"),dicConfig.get("password")));
        loginPage.logOutApp();
        reportStep("PASS","<b>Log Out</b> link clicked successfully");
    }

}
