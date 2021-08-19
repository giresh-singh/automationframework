package org.ubs.dis.framework.read;

import org.apache.log4j.Logger;
import org.ubs.dis.framework.utilities.LoggerHelper;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class ReadProperty {
    Logger log;
    public ReadProperty()
    {
        log = LoggerHelper.getLogger(org.ubs.dis.framework.read.ReadProperty.class);
    }

    public HashMap profile(String ProFile)
    {
        HashMap dicProjectVar = new HashMap();
        Properties prop = new Properties();
        try
        {
            FileInputStream fis = new FileInputStream(ProFile);
            prop.load(fis);
            Set keys = prop.keySet();
            Object k;
            for(Iterator iterator = keys.iterator(); iterator.hasNext(); dicProjectVar.put((String)k, prop.getProperty((String)k)))
                k = iterator.next();

            fis.close();
            log.info("Project variable read successfully.");
        }
        catch(Exception ex)
        {
            log.error(String.format("Error while reading property file %s.\n Error is -:%s.", new Object[] {
                    ProFile, ex.getMessage()
            }));
        }
        return dicProjectVar;
    }
}
