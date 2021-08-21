package org.ubs.dis.framework.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class ExtentListener implements ITestListener {
    private Logger log = Logger.getLogger(ExtentListener.class);
    public static ExtentReports extent;
    public static ExtentTest test;

    @Override
    public void onTestStart(ITestResult iTestResult) {
        //test.log(Status.INFO,iTestResult.getName()+" Started ...");
        Reporter.log(iTestResult.getName()+" Started ...");
        log.info(String.format("OnTestStart - %s Started",iTestResult.getName()));
    }
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        //test.log(Status.INFO,iTestResult.getName()+" Passed ...");
        Reporter.log(iTestResult.getMethod().getMethodName()+" Test Passed");
        log.info(String.format("onTestSuccess - %s Passed",iTestResult.getMethod().getMethodName()));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        //test.log(Status.FAIL,iTestResult.getThrowable());
        Reporter.log(iTestResult.getMethod().getMethodName()+" Test Failed"+iTestResult.getThrowable());
        log.info(String.format("onTestFailure - Test %s Failed and error is as - %s",iTestResult.getMethod().getMethodName(),iTestResult.getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        //test.log(Status.SKIP,iTestResult.getThrowable());
        Reporter.log(iTestResult.getMethod().getMethodName()+" Test Skipped"+iTestResult.getThrowable());
        log.info(String.format("onTestSkipped - %s Skipped",iTestResult.getMethod().getMethodName()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        /**
        extent = ExtentManager.getExtentInstance(TestBase.dicProjectVar.get("ReportPath")+"AutomationReport.html");
        test = extent.createTest(iTestContext.getName());
        test = extent.createTest(iTestContext.getCurrentXmlTest().getName());
         */
        Reporter.log(iTestContext.getCurrentXmlTest().getName()+" On Start");
        log.info(String.format("onStart - %s OnStart",iTestContext.getCurrentXmlTest().getName()));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        //extent.flush();
        Reporter.log(iTestContext.getCurrentXmlTest().getName()+" Test Finished..");
        log.info(String.format("onFinish - %s Finished",iTestContext.getCurrentXmlTest().getName()));
    }
}
