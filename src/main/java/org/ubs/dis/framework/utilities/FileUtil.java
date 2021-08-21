package org.ubs.dis.framework.utilities;

import org.apache.log4j.Logger;
import org.ubs.dis.framework.selenium.TestBase;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    Logger log;
    //HashMap<String,String>dicConfig;
    HashMap<String,String>dicTestSuite;
    //HashMap<String,String>dicProjectVar = TestBase.dicProjectVar;
    HashMap<String,String>dicReport;

    public FileUtil(){
        log = LoggerHelper.getLogger(org.ubs.dis.framework.utilities.FileUtil.class);
    }
    public String readTemplate(String file)
    {
        BufferedReader br;
        String strData;
        br = null;
        strData = "";
        try
        {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            for(String line = br.readLine(); line != null; line = br.readLine())
            {
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            strData = sb.toString();
            //break MISSING_BLOCK_LABEL_188;
        }
        catch(Exception e)
        {
            log.error(String.format("Error while reading template of file %s.\n Error is %s", new Object[] {
                    file, e.getMessage()
            }));
        }
        try
        {
            br.close();
        }
        catch(IOException e)
        {
            log.error(String.format("Error while closing template file %s.\n Error is %s", new Object[] {
                    file, e.getMessage()
            }));
        }
        //break MISSING_BLOCK_LABEL_225;
        Exception e ;
        // exception;
        try
        {
            br.close();
        }
        catch(IOException ex)
        {
            log.error(String.format("Error while closing template file %s.\n Error is %s", new Object[] {
                    file, ex.getMessage()
            }));
        }
        return strData;
    }


    public void delete(File file)
    {
        try
        {
            if(file.isDirectory())
            {
                if(file.list().length == 0)
                {
                    file.delete();
                } else
                {
                    String files[] = file.list();
                    String as[];
                    int j = (as = files).length;
                    for(int i = 0; i < j; i++)
                    {
                        String temp = as[i];
                        File fileDelete = new File(file, temp);
                        delete(fileDelete);
                    }

                    if(file.list().length == 0)
                        file.delete();
                }
            } else
            {
                file.delete();
            }
        }
        catch(Exception ex)
        {
            if(!((String) TestBase.dicConfig.get("SystemTempFolder")).contains("/AppData/Local/Temp/"))
                log.error((new StringBuilder("Error while deleting folder at ")).append(file).toString());
        }
    }


    public void deleteSpecificFile(String Filename, final String strPattern, String filetoExclude[])
    {
        try
        {
            File file = new File(Filename);
            boolean flag = false;
            File foundFiles[] = file.listFiles(new FilenameFilter() {

                                                   public boolean accept(File dir, String name)
                                                   {
                                                       return name.startsWith(strPattern);
                                                   }

                                                   final FileUtil this$0;
                                                   //private final String val$strPattern;
                                                   private String s;


                                                   {
                                                       this$0 = FileUtil.this;
                                                       //strPattern = s;
                                                       //super();
                                                   }
                                               }
            );
            File afile[];
            int j = (afile = foundFiles).length;
            for(int i = 0; i < j; i++)
            {
                File foundFile = afile[i];
                String as[];
                int l = (as = filetoExclude).length;
                for(int k = 0; k < l; k++)
                {
                    String fi = as[k];
                    if(foundFile.toString().contains(fi))
                        flag = true;
                }

                if(!flag)
                    delete(foundFile);
                flag = false;
            }

        }
        catch(Exception ex)
        {
            log.error(String.format("Error while deletespecific file whose pattern is %s in folder %s and file to be exculde is %s.\nError is %s", new Object[] {
                    strPattern, Filename, filetoExclude, ex.getMessage()
            }));
        }
    }

    public void move(String srcpath, String destpath, boolean isCopy, String... strFileTobeExcluded)
    {
        String strPattern;
        strPattern = ".*?Retry[0-9]*$";
        boolean flag = false;
        boolean isRetry = false;
        Path sourceDir;
        Path destinationDir;
        sourceDir = Paths.get(srcpath, new String[0]);
        destinationDir = Paths.get(destpath, new String[0]);
        Exception exception1;
        exception1 = null;
        Object obj = null;
        DirectoryStream directoryStream = null;
        try {
            directoryStream = Files.newDirectoryStream(sourceDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(Iterator iterator = directoryStream.iterator(); iterator.hasNext();)
        {
            Path path = (Path)iterator.next();
            flag = false;
            for(int i = 0; i < strFileTobeExcluded.length; i++)
            {
                if(!path.toString().equals((new StringBuilder(String.valueOf((String)TestBase.dicProjectVar.get("ReportPath")))).append(strFileTobeExcluded[i]).toString().replace("/", "\\")))
                    continue;
                flag = true;
                break;
            }

            if(!flag && path.equals(destinationDir))
                flag = true;
            else
            if(!flag && dicTestSuite != null)
            {
                Pattern p = Pattern.compile(strPattern);
                Matcher m = p.matcher(path.toString());
                isRetry = m.find();
                if(isRetry)
                    flag = true;
            }
            Path d2 = destinationDir.resolve(path.getFileName());
            if(isCopy)
                try {
                    Files.copy(path, d2, new CopyOption[0]);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            else
            if(!flag)
                try {
                    Files.move(path, d2, new CopyOption[0]);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

        if(directoryStream != null)
            try {
                directoryStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        //break MISSING_BLOCK_LABEL_352;
        //exception1;
       /**
        if(directoryStream != null)
            try {
                directoryStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        try {
            throw exception1;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
    }

    public void movefailedFile()
    {
        boolean isretry = false;
        FileUtil futil = new FileUtil();
        futil.createDirectory((String)TestBase.dicProjectVar.get("RetryFailedXML"), true);
        List strSheet = new ArrayList();
        String destpath = (String)TestBase.dicProjectVar.get("RetryFailedXML");
        String srcpath = ((String)TestBase.dicProjectVar.get("tempreportdir")).replace("html/", "");
        for(Iterator iterator = dicReport.keySet().iterator(); iterator.hasNext();)
        {
            String key = (String)iterator.next();
            if(key.contains("(failed"))
                isretry = true;
            key = key.substring(0, key.indexOf("."));
            if(!strSheet.contains(key))
                strSheet.add(key);
        }

        try
        {
            for(Iterator iterator1 = strSheet.iterator(); iterator1.hasNext();)
            {
                String failedFile = (String)iterator1.next();
                File file;
                if(isretry)
                    file = new File((new StringBuilder(String.valueOf(srcpath))).append("Failed suite [").append(failedFile).append("]").append("//testng-failed.xml").toString());
                else
                    file = new File((new StringBuilder(String.valueOf(srcpath))).append(failedFile).append("//testng-failed.xml").toString());
                if(file.exists())
                    file.renameTo(new File((new StringBuilder(String.valueOf(destpath))).append(failedFile).append(file.getName()).toString()));
            }

        }
        catch(Exception e)
        {
            log.error("Error while moving a file");
        }
    }

    public void movefail(String src, String desc)
    {
        try
        {
            File file = new File(src);
            if(file.exists())
                file.renameTo(new File(desc));
        }
        catch(Exception e)
        {
            log.error("Error while moving a file");
        }
    }

    public void createFile(String fileName, String strContent)
    {
        BufferedWriter output = null;
        try
        {
            File file = new File(fileName);
            output = new BufferedWriter(new FileWriter(file));
            output.write(strContent);
            //break MISSING_BLOCK_LABEL_165;
        }
        catch(Exception ex)
        {
            log.error(String.format("Error while writing data to %s.\nError is -: %s .", new Object[] {
                    fileName, ex.getMessage()
            }));
        }
        if(output != null)
            try
            {
                output.close();
            }
            catch(IOException e)
            {
                log.error(String.format("Error while closing %s.\nError is -: %s .", new Object[] {
                        fileName, e.getMessage()
                }));
            }
        //break MISSING_BLOCK_LABEL_209;
        Exception exception;
        //exception;
        if(output != null)
            try
            {
                output.close();
            }
            catch(IOException e)
            {
                log.error(String.format("Error while closing %s.\nError is -: %s .", new Object[] {
                        fileName, e.getMessage()
                }));
            }
        try {
            //throw exception;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if(output != null)
            try
            {
                output.close();
            }
            catch(IOException e)
            {
                log.error(String.format("Error while closing %s.\nError is -: %s .", new Object[] {
                        fileName, e.getMessage()
                }));
            }
    }

    public boolean createDirectory(String dirName, boolean isDelExistDir)
    {
        boolean flag = false;
        try
        {
            File file = new File(dirName);
            if(!file.exists())
                flag = file.mkdirs();
            else
            if(isDelExistDir)
            {
                delete(file);
                flag = file.mkdirs();
            } else
            {
                flag = true;
            }
        }
        catch(Exception ex)
        {
            log.error((new StringBuilder("Error while creating Directory ")).append(dirName).append(".\n Error is ").append(ex.getMessage()).toString());
            flag = false;
        }
        return flag;
    }

    public boolean checkfileExist(String filename)
    {
        boolean flag = false;
        try
        {
            File file = new File(filename);
            flag = file.exists();
        }
        catch(Exception ex)
        {
            log.error((new StringBuilder("Error while searching file ")).append(filename).append(".\n Error is ").append(ex.getMessage()).toString());
            flag = false;
        }
        return flag;
    }

    public boolean InitializeProject()
    {
        boolean flag;
        try{
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_mm_yyyy_HH_mm_ss_SSSSSS");
            //Create Report Folder
            flag = createDirectory(TestBase.dicProjectVar.get("ReportPath"),false);
            if(flag){
                //Create LastRun Folder
                createDirectory(TestBase.dicProjectVar.get("ReportPath")+"LastRun",false);
                //Create Debuglog folder
                flag = createDirectory(TestBase.dicProjectVar.get("logs"),false);
                //Create Screenshot folder
                flag = createDirectory(TestBase.dicProjectVar.get("Screenshot"),false);
                //Move Previous execution report into LastRun folder
                if(checkfileExist(TestBase.dicProjectVar.get("ReportPath")+"AutomationReport.html")) {
                    String destFolder = TestBase.dicProjectVar.get("ReportPath") + "LastRun/Report" + dateFormat.format(date);
                    createDirectory(destFolder, false);
                    move(TestBase.dicProjectVar.get("ReportPath"), destFolder, false, "LastRun");
                }
                    //Now create ScreenShot folder
                    createDirectory(TestBase.dicProjectVar.get("Screenshot"),false);
                    //Create TestNG Suit XML Folder
                   //createDirectory(TestBase.dicProjectVar.get("TestNG"),true);
                    //Delete System Temp Folder and Debuglog
                    if(System.getProperty("os.name").contains("Window")){
                        delete(new File(TestBase.dicProjectVar.get("SystemTempFolder")));
                    }
            }
            if(!flag){
                log.error(String.format("Error while creating Report Folder"));
            }
        }catch(Exception ex){
            log.error(String.format("Error while creating Report Folder"));
            log.error(String.format(ex.getMessage()));
            flag = false;
        }
        return flag;
    }

}
