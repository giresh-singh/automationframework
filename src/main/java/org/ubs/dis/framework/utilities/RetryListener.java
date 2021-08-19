package org.ubs.dis.framework.utilities;

import org.apache.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {
    Logger log = LoggerHelper.getLogger(RetryListener.class);
    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {

        IRetryAnalyzer retry = iTestAnnotation.getRetryAnalyzer();
        if(retry == null){
            iTestAnnotation.setRetryAnalyzer(Retry.class);
        }

    }
}
