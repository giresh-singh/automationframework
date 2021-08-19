package org.ubs.dis.framework.utilities;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer{

    private Logger log = LoggerHelper.getLogger(Retry.class);
    private int retryCount = 0;
    private int maxRetryCount = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if(retryCount < maxRetryCount){
            log.info(String.format("Retrying test name: %s with status: %s for iteration count: %s  times",iTestResult.getName(),iTestResult.getStatus(), (retryCount+1) ));
            retryCount++;
            return true;
        }
        return false;
    }

    public String getResultStatusVal(int intStatus){
        String strResultVal = null;
        if(intStatus == 1){
            strResultVal = "SUCCESS";
        }else if(intStatus == 2){
            strResultVal = "FAILURE";
        }else if(intStatus == 3){
            strResultVal = "SKIP";
        }
        return strResultVal;
    }
}
