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

    /**
     * @Description: Initialise Suite level objects and methods
     * @Example: Read Framework properties, configs,clean and move old results and xml files e.t.c
     * @Author: Varsha Singh
     * @Date:
     */
    @BeforeSuite
    public void initializeSuiteLevel(){
        ReadProperty prop = new ReadProperty();
        dicProjectVar = prop.profile("src/main/resources/config/framework-config.properties");
        dicConfig = prop.profile("src/main/resources/config/Config.properties");
        log.info(String.format("Project Property Value is: %s",dicProjectVar));
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
        test = extent.createTest(method.getName());
        reportStep(testContext,"INFO","Test Case: <b>"+testContext.getName()+"</b> Started");
    }

    /**
     * @Description: Prepare report, clear test data etc
     * @Example: test methode related data and report preparations etc
     * @Author: Varsha Singh
     * @Date:
     */
    @AfterMethod
    public void PrepareTestResult(ITestResult result,ITestContext testContext){
        try {
            extent.flush();
            log.info(String.format("Report is ready now"));
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
    public void TearDown(){
        driver.quit();
        log.info(String.format("End of test driver closed"));
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
            browserName = browserName.toLowerCase();
            switch(browserName){
                case "chrome":
                    WebDriverFactory chrome = WebDriverFactory.class.newInstance();
                    ChromeOptions chromeoptions = chrome.getChromeOptions();
                    driver = chrome.getChromeDriver(chromeoptions);
                    log.info(String.format("Driver with detail launched successfully:  %s",driver.toString()));
                    return driver;
                    
                case "firefox":
                    WebDriverFactory firefox = WebDriverFactory.class.newInstance();
                    FirefoxOptions firefoxoptions = firefox.getFirefoxOptions();
                    driver = firefox.getFirefoxDriver(firefoxoptions);
                    log.info(String.format("Driver with detail launched successfully:  %s",driver.toString()));
                    return driver;
                case "ie":
                case "internetexplorer":
                    WebDriverFactory ieDriver = WebDriverFactory.class.newInstance();
                    InternetExplorerOptions ieOptions = ieDriver.getIEOptions();
                    driver =  ieDriver.getIEDriver(ieOptions);
                    log.info(String.format("Driver with detail launched successfully:  %s",driver.toString()));
                    return driver;
                default:
                    throw new IllegalStateException("Unexpected value: " + browserName);
            }

        }catch (Exception ex){
            log.info(String.format("Browser Name %s is not a valid should be chrome, firefox,ie,internetexplorer",browserName));
            return null;
        }
    }

    public void setupBrowser(String browserName){
        driver = getBrowserObject(browserName);
        log.info(String.format("Initialize Driver %s",driver.hashCode()));
        WaitHelper wait = new WaitHelper(driver);
        wait.setImplicitWait(Integer.parseInt(dicProjectVar.get("implicitWait")), TimeUnit.SECONDS);
        wait.pageLoadTime(Integer.parseInt(dicProjectVar.get("pageLoadWait")));
        driver.manage().window().maximize();
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
            }else{
                driver.get(TestBase.dicConfig.get("strApplicationURL"));
                log.info(String.format("Application URL (Config) : %s launched successfully",strUrl[0]));
            }

        }catch(Exception ex){
            log.error(String.format("Not able launch URL %s getting below error \n%s",strUrl,ex.getMessage()));
            return false;
        }
        return true;
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
                test.fail(strDetail);
                test.fail(result.getThrowable());
                TakeScreenShot(driver,TCName);
                break;
            default:
                test.info(strDetail);
        }
    }
}
