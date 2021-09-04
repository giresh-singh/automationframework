package org.ubs.dis.framework.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.ElementScrollBehavior;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.ubs.dis.framework.read.ReadExcel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WebDriverFactory {
    private Logger log = Logger.getLogger(WebDriverFactory.class);
    private WebDriver driver;
    int implicitWait = Integer.parseInt(TestBase.dicProjectVar.get("implicitWait"));
    public WebDriver getDriver() {
        return driver;
    }

    @Parameters({"Browser"})
    public void LaunchBrowser(@Optional String browser, ITestContext testContext){
        String key;
        key = testContext.getCurrentXmlTest().getName();
        launchBrowser(browser);
    }

    /**
     * This function is used to initialize the browser,android app or ios app
     * @param browser - Name of the browser fetch from testng.xml. In case of android/ios setting are fetched from config.xml
     * @author Varsha Singh
     */
    @SuppressWarnings("unchecked")
    public void launchBrowser(String browser) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

            try{
                browser = browser.toLowerCase();
                if (browser.equalsIgnoreCase("chrome")) {
                    log.info(String.format("%s : Browser object creation started ....",browser));
                    System.setProperty("webdriver.chrome.driver",TestBase.dicProjectVar.get("Driver")+"chromedriver.exe");
                    Map<String, Object> preferences  = new HashMap<String, Object>();
                    preferences .put("profile.default_content_settings.popups", 0);
                    preferences .put("download.prompt_for_download", "false");
                    preferences .put("download.default_directory", new File(TestBase.dicProjectVar.get("FileDownloadPath")).getAbsolutePath());
                    preferences.put("profile.default_content_setting_values.notifications", 2);
                    preferences.put("credentials_enable_service", false);
                    preferences.put("profile.password_manager_enabled", false);

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--disable-web-security");
                    options.addArguments("--no-proxy-server");
                    options.setExperimentalOption("prefs", preferences);

                    //capabilities = DesiredCapabilities.chrome();
                    capabilities.setBrowserName("chrome");
                    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    //capabilities.setCapability("chrome.prefs", prefs);
                    driver = new ChromeDriver(capabilities);
                    log.info(String.format("%s : Browser object Created successfully ....",browser));
                } else if (browser.equalsIgnoreCase("firefox")) {
                    log.info(String.format("%s : Browser object creation started ....",browser));
                    System.setProperty("webdriver.firefox.marionette", TestBase.dicProjectVar.get("Driver") + "geckodriver.exe");
                    System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
                    capabilities.setBrowserName("firefox");
                    capabilities.setCapability("marionette", true);

                    FirefoxProfile profile = new FirefoxProfile();
                    profile.setPreference("browser.download.dir",new File(TestBase.dicProjectVar.get("FileDownloadPath")).getAbsolutePath());
                    profile.setPreference("browser.download.folderList", 2);
                    profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
                    profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, application/xls, application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
                    profile.setPreference( "browser.download.manager.showWhenStarting", false );
                    profile.setPreference("browser.download.manager.focusWhenStarting", false);
                    profile.setPreference("browser.download.useDownloadDir", true);
                    profile.setPreference("browser.helperApps.alwaysAsk.force", false);
                    profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
                    profile.setPreference("browser.download.manager.closeWhenDone", true);
                    profile.setPreference("browser.download.manager.showAlertOnComplete", false);
                    profile.setPreference("browser.download.manager.useWindow", false);
                    profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
                    profile.setPreference( "pdfjs.disabled", true );
                    capabilities.setCapability(FirefoxDriver.PROFILE, profile);
                    driver = new FirefoxDriver(capabilities);
                    log.info(String.format("%s : Browser object Created successfully ....",browser));

                    //driver.manage().window().maximize();

                } else if (browser.equalsIgnoreCase("ie")) {
                    browser = "internet explorer";
                    log.info(String.format("%s : Browser object creation started ....",browser));
                    System.setProperty("webdriver.ie.driver",TestBase.dicProjectVar.get("Driver")+"IEDriverServer.exe");
                    //capabilities=DesiredCapabilities.internetExplorer();
                    capabilities.setBrowserName("internetExplorer");
                    capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
                    capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
                    driver = new InternetExplorerDriver();
                    log.info(String.format("%s : Browser object Created successfully ....",browser));
                }
                if(driver!=null){
                    driver.manage().window().maximize();
                    driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
                }
            }catch(Exception ex){
                log.error(String.format("Error while launching webdriver.\n %s",ex.getMessage()));
            }

    }

    public void getTestData(String testDetails,String browser) {
        try {
            testDetails = testDetails.substring(0, testDetails.lastIndexOf("_"));
            TestBase.dicTestData = new HashMap<String, String>();
            ReadExcel excel = new ReadExcel();
            testDetails = testDetails.replace("_" + browser, "");
            TestBase.dicTestData = excel.exlReadMatserTestData(testDetails);
        } catch (Exception ex) {
            log.equals(String.format("Error while reading Test data of %s./n%s", testDetails, ex.getMessage()));
        }
    }

    public HashMap<String, String> getDicTestData() {
        return TestBase.dicTestData;
    }

    public ChromeOptions getChromeOptions(){
        log.info(String.format("Setting -> ChromeOptions  Started..."));
        //preferences
        Map<String, Object> preferences  = new HashMap<String, Object>();
        preferences .put("profile.default_content_settings.popups", 0);
        preferences .put("download.prompt_for_download", "false");
        preferences .put("download.default_directory", new File(TestBase.dicProjectVar.get("FileDownloadPath")).getAbsolutePath());
        preferences.put("profile.default_content_setting_values.notifications", 2);
        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);
        //Chrome Options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security");
        options.addArguments("--no-proxy-server");
        options.addArguments("--test-type");
        options.addArguments("--disable-popup-blocking");
        options.setExperimentalOption("prefs", preferences);
        //Desired Capabilities
        DesiredCapabilities chromeCapability = new DesiredCapabilities();
        chromeCapability.setJavascriptEnabled(true);
        chromeCapability.setBrowserName("chrome");
        chromeCapability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        chromeCapability.setCapability(ChromeOptions.CAPABILITY, options);
        options.setCapability(ChromeOptions.CAPABILITY,options);
        if(System.getProperty("os.name").contains("Linux")){
            options.addArguments("--headless","window-size=1024,768","--no-sandbox");
        }
        log.info(String.format("Setting -> ChromeOptions  Completed..."));
        return options;
    }

    public WebDriver getChromeDriver(ChromeOptions cap){
        if(System.getProperty("os.name").contains("Mac")){
            System.setProperty("webdriver.chrome.driver",TestBase.dicProjectVar.get("Driver")+"chromedriver");
            log.info(String.format("Chrome Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new ChromeDriver(cap);
        }else if(System.getProperty("os.name").contains("Window")){
            System.setProperty("webdriver.chrome.driver",TestBase.dicProjectVar.get("Driver")+"chromedriver.exe");
            log.info(String.format("Chrome Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new ChromeDriver(cap);
        }else if(System.getProperty("os.name").contains("Linux")){
            System.setProperty("webdriver.chrome.driver","user/bin/chrome");
            log.info(String.format("Chrome Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new ChromeDriver(cap);
        }
        log.info(String.format("Not able to initiate any driver for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
        return null;
    }

    public InternetExplorerOptions getIEOptions(){
        log.info(String.format("Setting -> InternetExplorerOptions  Started..."));
        //Desired Capabilities
        DesiredCapabilities ieCapability = new DesiredCapabilities();
        ieCapability.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, ElementScrollBehavior.BOTTOM);
        //ieCapability.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);
        ieCapability.setJavascriptEnabled(true);
        ieCapability.setBrowserName("internet explorer");
        ieCapability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        ieCapability.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        ieCapability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
        //Options
        InternetExplorerOptions ieOptions = new InternetExplorerOptions(ieCapability);
        log.info(String.format("Setting -> InternetExplorerOptions  Completed..."));
        return ieOptions;
    }

    public WebDriver getIEDriver(InternetExplorerOptions cap){
        if(System.getProperty("os.name").contains("Mac")){
            System.setProperty("webdriver.ie.driver",TestBase.dicProjectVar.get("Driver")+"IEDriverServer");
            log.info(String.format("InternetExplorer Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new InternetExplorerDriver(cap);
        }else if(System.getProperty("os.name").contains("Window")){
            System.setProperty("webdriver.ie.driver",TestBase.dicProjectVar.get("Driver")+"IEDriverServer.exe");
            log.info(String.format("InternetExplorer Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new InternetExplorerDriver(cap);
        }else if(System.getProperty("os.name").contains("Linux")){
            System.setProperty("webdriver.ie.driver","user/bin/internetexplorer");
            log.info(String.format("InternetExplorer Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new InternetExplorerDriver(cap);
        }
        log.info(String.format("Not able to initiate any driver for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
        return null;
    }

    public FirefoxOptions getFirefoxOptions(){
        //Firefox profile
        log.info(String.format("Setting -> FirefoxOptions  Started..."));
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(true);
        profile.setPreference("browser.download.dir",new File(TestBase.dicProjectVar.get("FileDownloadPath")).getAbsolutePath());
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, application/xls, application/x-msexcel,application/excel,application/x-excel,application/vnd.ms-excel,application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
        profile.setPreference( "browser.download.manager.showWhenStarting", false );
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.download.useDownloadDir", true);
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.closeWhenDone", true);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
        profile.setPreference( "pdfjs.disabled", true );
        //Desired Capabilities
        DesiredCapabilities firefoxCapability = new DesiredCapabilities();
        firefoxCapability.setJavascriptEnabled(true);
        firefoxCapability.setBrowserName("chrome");
        firefoxCapability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        firefoxCapability.setCapability(FirefoxDriver.PROFILE, profile);
        firefoxCapability.setCapability("marionette",true);
        FirefoxOptions options = new FirefoxOptions(firefoxCapability);
        if(System.getProperty("os.name").contains("Linux")){
            options.addArguments("--headless","window-size=1024,768","--no-sandbox");
        }
        log.info(String.format("Setting -> FirefoxOptions  Completed..."));
        return options;
    }
    public WebDriver getFirefoxDriver(FirefoxOptions cap){
        if(System.getProperty("os.name").contains("Mac")){
            System.setProperty("webdriver.firefox.marionette",TestBase.dicProjectVar.get("Driver")+"geckodriver");
            log.info(String.format("Firfox Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new FirefoxDriver(cap);
        }else if(System.getProperty("os.name").contains("Window")){
            System.setProperty("webdriver.firefox.marionette",TestBase.dicProjectVar.get("Driver")+"geckodriver.exe");
            log.info(String.format("Firfox Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new FirefoxDriver(cap);
        }else if(System.getProperty("os.name").contains("Linux")){
            System.setProperty("webdriver.firefox.marionette","user/bin/firefox");
            log.info(String.format("Firfox Driver is initialized for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
            return new FirefoxDriver(cap);
        }
        log.info(String.format("Not able to initiate any driver for %s O.S",System.getProperty("os.name").toUpperCase().toString()));
        return null;
    }
}
