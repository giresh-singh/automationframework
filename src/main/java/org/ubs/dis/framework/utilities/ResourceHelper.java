package org.ubs.dis.framework.utilities;

public class ResourceHelper {
    public static String getResourcePath(String path){
        String basePath = System.getProperty("user.dir");
        return basePath+path;
    }

}
