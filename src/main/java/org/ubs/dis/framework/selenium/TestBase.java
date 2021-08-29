package org.ubs.dis.framework.selenium;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.ubs.dis.framework.read.ReadProperty;
import org.ubs.dis.framework.utilities.ExtentManager;
import org.ubs.dis.framework.utilities.FileUtil;
import org.ubs.dis.framework.utilities.LoggerHelper;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestBase {
    public static HashMap<String, String> dicConfig,dicEnvironment,dicDBDetails;
    public static HashMap<String, String> dicProjectVar;
    public static HashMap<String, String> dicCommValues,dicReport;
    public static HashMap<String, HashMap<String,String>> dicResult= new HashMap<String, HashMap<String,String>>();
    public static HashMap<String, String> dicTestData;
    public static ExtentReports extent;
    public static ExtentTest test;
    public static ITestContext testContext;
    public static ITestResult result;
    public Logger log = LoggerHelper.getLogger(TestBase.class);
    public WebDriver driver;
    public String ErrDescription = "";

    /**
     * @Description: Initialise Suite level objects and methods
     * @Example: Read Framework properties, configs,clean and move old results and xml files e.t.c
     * @Author: Varsha Singh
     * @Date:
     */
    @BeforeSuite
    public void initializeSuiteLevel(ITestContext testContext){
        log.info(String.format("......................................................"));
        log.info(String.format("................Before Suite ........................."));
        log.info(String.format(".............Test Suite -> %s Started ..................",testContext.getSuite().getName()));
        ReadProperty prop = new ReadProperty();
        log.info("Read Framework Properties......");
        dicProjectVar = prop.profile("src/main/resources/config/framework-config.properties");
        log.info(String.format("Project Property Value is: %s",dicProjectVar));
        log.info("Read Config Properties......");
        dicConfig = prop.profile("src/main/resources/config/Config.properties");
        log.info(String.format("Project Config Value is: %s",dicConfig));
        FileUtil obj = new FileUtil();
        obj.InitializeProject();
    }
    /**
     * @Description: Initialise Test level objects and methods
     * @Example: Launch and Setup browser, create Extent Report Object, get test data e.t.c
     * @Author: Varsha Singh
     * @Date:
     */
    @BeforeTest
    public void InitializeTestLevel(ITestContext testContext){
        log.info(String.format("......................................................"));
        log.info(String.format(".................Before Test ........................."));
        log.info(String.format("......................................................"));
        //Setup Browser
        setupBrowser(dicConfig.get("BrowserType"));
        log.info(String.format("BrowserType: %s launched successfully",dicConfig.get("BrowserType")));
        extent = ExtentManager.getExtentInstance(dicProjectVar.get("ReportPath")+"AutomationReport.html");

        //ReadExcel read = new ReadExcel(); ----- TBD
        //dicTestData = read.exlReadMatserTestData(testContext.getCurrentXmlTest().getName()); --- TBD
    }
    /**
     * @Description: Initialise Test case level needs
     * @Example: test methode related data etc
     * @Author: Varsha Singh
     * @Date:
     */
    @BeforeClass
    public void getExtentTestInstance(){
        log.info(String.format("......................................................"));
        log.info(String.format("................Before Class ........................."));
        log.info(String.format("......................................................"));
        // TBD
    }
    /**
     * @Description: Initialise Test methode level objects and data
     * @Example: test methode related data and report preparations etc
     * @Author: Varsha Singh
     * @Date:
     */
    @BeforeMethod
    public void InitializeTestMethod(Method method, ITestContext testContext){
        log.info(String.format("......................................................"));
        log.info(String.format("...............Before Method ........................."));
        test = extent.createTest(method.getName());
        log.info(String.format("Execution of Test Case: %s -> Test Method: %s Started......",testContext.getCurrentXmlTest().getName(),method.getName()));
        log.info(String.format("......................................................"));
        //reportStep(testContext,"INFO","Test Case: <b>"+testContext.getName()+"</b> Started");
    }
    /**
     * @Description: Prepare report, clear test data etc
     * @Example: test methode related data and report preparations etc
     * @Author: Varsha Singh
     * @Date:
     */
    @AfterMethod
    public void PrepareTestResult(ITestResult result,ITestContext testContext){
        log.info(String.format("......................................................"));
        log.info(String.format("................After Method ........................."));
        log.info(String.format("......................................................"));

        try {
            extent.flush();
            log.info(String.format("Report is ready now for Test Case: %s -> Test Method: %s"
                    ,testContext.getCurrentXmlTest().getName(),result.getMethod().getMethodName()));
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
    /**
     * @Description: Initialise Test methode level objects and data
     * @Example: test methode related data and report preparations etc
     * @Author: Varsha Singh
     * @Date:
     */
    @AfterTest
    public void TearDown(ITestContext testContext){
        log.info(String.format("......................................................"));
        log.info(String.format("...................After Test........................."));
        log.info(String.format("......................................................"));
        log.info(String.format(".........Execution Completed for Test Case %s ......",testContext.getCurrentXmlTest().getName()));
        driver.quit();
        log.info(String.format(".......End of test -> driver closed......."));
    }

    @AfterSuite
    public void Clean(){
        log.info(String.format("......................................................"));
        log.info(String.format("...................After Suite........................."));
        log.info(String.format("......................................................"));
        dicProjectVar.clear();
        log.info(String.format("dicProjectVar cleared ......"));
        dicConfig.clear();
        log.info(String.format("dicConfig cleared ......"));
    }

    public String CaptureScreen(String fileName, WebDriver driver){
        Random random = new Random();
        String TCName = fileName;
        FileUtil futil = new FileUtil();
        String ScreenShotFolder = (String)dicProjectVar.get("Screenshot")+TCName+"/";
        futil.createDirectory(ScreenShotFolder,false);
        log.info(String.format("screenshot Folder %s",ScreenShotFolder));
        int randCode = random.nextInt(0x186a0);
        if(driver == null){
            log.info("driver is null ....");
            return null;
        }
        if(fileName == ""){
            fileName = "blank";
            log.info(String.format("File Name:is blank"));
        }
        File destFile = null;
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("HH_mm_ss_SSSSSS");
        File screFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        log.info(String.format("Source File Name: %s ",screFile.toPath().toString()));
        try{
            destFile = new File((String)dicProjectVar.get("Screenshot")+TCName+"/"+fileName+"_"+formater.format(calender.getTime())+"_"+randCode+".png");
            Files.copy(screFile.toPath(),destFile.toPath());
            log.info(String.format("Source Screenshot file: %s  copied to Destination file : %s",screFile.toPath().toString(),destFile.toPath()));
            Reporter.log("<a href='"+destFile.getAbsolutePath()+"'><img src='"+destFile.getAbsolutePath()+"' height='100' width='100'/></a>");
            log.info(String.format("Link for screenshot file: %s  copied to result file",screFile.toPath().toString()));
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return destFile.getAbsolutePath().toString();
    }

    public WebDriver getBrowserObject(String browserName){
        try{
            log.info(String.format("Getting Browser Object: %s process started ...",browserName));
            browserName = browserName.toLowerCase();
            switch(browserName){
                case "chrome":
                    log.info(String.format("%s : Browser object creation started ....",browserName));
                    WebDriverFactory chrome = WebDriverFactory.class.newInstance();
                    ChromeOptions chromeoptions = chrome.getChromeOptions();
                    try{
                        driver = chrome.getChromeDriver(chromeoptions);
                        log.info(String.format("Driver with detail launched successfully:  %s",driver.toString()));
                        return driver;
                    }catch(Exception ex){
                        log.error(String.format("Driver options not set and getting error %s",ex.getMessage()));
                        ErrDescription = String.format("Driver options not set and getting error %s",ex.getMessage());
                        return null;
                    }
                case "firefox":
                    log.info(String.format("%s : Browser object creation started ....",browserName));
                    WebDriverFactory firefox = WebDriverFactory.class.newInstance();
                    FirefoxOptions firefoxoptions = firefox.getFirefoxOptions();
                    try{
                        driver = firefox.getFirefoxDriver(firefoxoptions);
                        log.info(String.format("Driver with detail launched successfully:  %s",driver.toString()));
                        return driver;
                    }catch (Exception ex){
                        log.error(String.format("Driver options not set and getting error %s",ex.getMessage()));
                        ErrDescription = String.format("Driver options not set and getting error %s",ex.getMessage());
                        return null;
                    }
                case "ie":
                case "internetexplorer":
                    log.info(String.format("%s : Browser object creation started ....",browserName));
                    WebDriverFactory ieDriver = WebDriverFactory.class.newInstance();
                    InternetExplorerOptions ieOptions = ieDriver.getIEOptions();
                    try{
                        driver =  ieDriver.getIEDriver(ieOptions);
                        log.info(String.format("Driver with detail launched successfully:  %s",driver.toString()));
                        return driver;
                    }catch (Exception ex){
                        log.error(String.format("Driver options not set and getting error %s",ex.getMessage()));
                        ErrDescription = String.format("Driver options not set and getting error %s",ex.getMessage());
                        return null;
                    }
                default:
                    throw new IllegalStateException("Unexpected value: " + browserName);
            }
        }catch (Exception ex){
            log.info(String.format("Browser Name %s is not a valid should be chrome, firefox,ie,internetexplorer",browserName));
            log.error(ex.getMessage());
            ErrDescription = ex.getMessage();
            return null;
        }
    }

    public void setupBrowser(String browserName){
        try{
            driver = getBrowserObject(browserName);
            log.info(String.format("Initialize Driver %s",driver.hashCode()));
            WaitHelper wait = new WaitHelper(driver);
            wait.setImplicitWait(Integer.parseInt(dicProjectVar.get("implicitWait")), TimeUnit.SECONDS);
            wait.pageLoadTime(Integer.parseInt(dicProjectVar.get("pageLoadWait")));
            driver.manage().window().maximize();
            log.info(String.format("Driver %s maximized",browserName));
        }catch(Exception ex){
            ErrDescription = ex.getMessage()+ErrDescription;
        }
    }

    public void TakeScreenShot(WebDriver driver,String TCName){
        log.info(String.format("Take Screenshot on navigation ..."));
        log.info(String.format("Capture Scree for Test Case - %s",TCName));
        String screen = CaptureScreen(TCName,driver);
        log.info(String.format("Screenshot saved on path %s",screen));
        try{
            test.addScreenCaptureFromPath(screen);
            log.info(String.format("Screenshot taken successfully"));
        }catch(Exception ex){
            log.error(String.format(ex.getMessage()));
        }
    }

    /***
     * @Description: This function is used to launch an application
     * @param strUrl -: optional string -: Url esle pick url from CommonValue->strApplicationURL
     * @return true/false
     * @author Varsha Singh
     * @Since 17/Aug/2021
     */
    public boolean launchApplication(String... strUrl){
        try{
            if(strUrl.length!=0){
                driver.get(strUrl[0]);
                log.info(String.format("Application URL: %s launched successfully",strUrl[0]));
                return true;
            }else{
                driver.get(TestBase.dicConfig.get("strApplicationURL"));
                log.info(String.format("Application URL (Config) : %s launched successfully",strUrl[0]));
                ErrDescription = String.format("Application URL (Config) : %s launched successfully",strUrl[0]);
                return false;
            }

        }catch(Exception ex){
            log.error(String.format("Not able launch URL %s getting below error \n%s",strUrl,ex.getMessage()));
            ErrDescription = String.format("Not able launch URL %s getting below error \n%s",strUrl,ex.getMessage());
            return false;
        }
    }

    /***
     * @Description: This function is used to report steps and capture screen
     * @param strStatus,strDetail Status - PASS/FAIL/INFO, Detail- Pass/fail detail
     * @return none
     * @author Varsha Singh
     * @Since 17/Aug/2021
     */

    public void reportStep(ITestContext testContext,String strStatus,String strDetail) {
        String TCName = testContext.getCurrentXmlTest().getName();
        switch (strStatus.toLowerCase()) {
            case "pass":
                test.log(Status.PASS, strDetail);
                TakeScreenShot(driver,TCName);
                break;
            case "fail":
                //test.fail(strDetail);
                test.log(Status.FAIL,strDetail);
                TakeScreenShot(driver,TCName);
                //test.fail(result.getThrowable());
                break;
            default:
                test.info(strDetail);
        }
    }

}
