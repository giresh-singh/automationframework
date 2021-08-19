package org.ubs.dis.framework.utilities;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.log4j.Logger;
import org.ubs.dis.framework.selenium.TestBase;

public class ExtentManager {
    Logger log = LoggerHelper.getLogger(ExtentManager.class);

    private static ExtentReports extent;

    public static ExtentReports getExtentInstance(String reportPath){
        if(extent == null){
            return createInstance(reportPath);//TestBase.dicProjectVar.get("ReportPath")+"AutomationReport.html"
        }else{
            return extent;
        }
    }

    public  static ExtentReports createInstance(String fileName){
        ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(fileName);
        extentHtmlReporter.config().setChartVisibilityOnOpen(true);
        extentHtmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        extentHtmlReporter.config().setEncoding("UFT-8");
        extentHtmlReporter.config().setTheme(Theme.STANDARD);
        extentHtmlReporter.config().setReportName(TestBase.dicProjectVar.get("reportName"));
        extentHtmlReporter.config().setDocumentTitle(fileName);
        //extentHtmlReporter.config().setCSS(".r-img { width: 30%; }");

        extent = new ExtentReports();
        //extent.setAnalysisStrategy(AnalysisStrategy.TEST);
        extent.attachReporter(extentHtmlReporter);
        //extent.setReportUsesManualConfiguration(true);
        extent.setSystemInfo("OS Name",System.getProperty("os.name"));
        return extent;
    }
}
