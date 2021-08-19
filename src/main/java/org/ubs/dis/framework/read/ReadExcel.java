package org.ubs.dis.framework.read;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.ubs.dis.framework.selenium.TestBase;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadExcel {
    int RunModeIndex;
    Logger log;

    enum SheetToExclude{

        Runs("Runs"), Execution("Execution");
        String value;
        SheetToExclude(String s) {
            this.value = s;
        }

        public String getValue(){
            return value;
        }
    }

    public ReadExcel()
    {
        log = Logger.getLogger(ReadExcel.class);
    }

    public HashMap exlDictionary(String filename, String sheetname, int... KeyValueColumnIndex)
    {
        HashMap dic = new HashMap();
        try
        {
            InputStream fis = new FileInputStream(filename);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheet(sheetname);
            if(KeyValueColumnIndex.length == 2)
            {
                String key = "";
                String value = "";
                for(int i = 0; i < sheet.getPhysicalNumberOfRows(); i++)
                {
                    HSSFRow row = sheet.getRow(i);
                    key = getData(KeyValueColumnIndex[0], row);
                    value = getData(KeyValueColumnIndex[1], row);
                    if(!key.equals("") && key != null)
                        dic.put(key, value);
                }

            }
            fis.close();
            log.info(String.format("Dictionary created successfully of %s.", new Object[] {
                    filename
            }));
        }
        catch(Exception ex)
        {
            log.error(String.format("Error while creating dictionary of %s.\n Error is -: %s ", new Object[] {
                    filename, ex.getMessage()
            }));
        }
        return dic;
    }

    private String getData(int Column, HSSFRow row)
    {
        String ExcelData = "";
        try
        {
            HSSFCell cell = row.getCell(Column - 1);

            switch(cell.getCellType())//.getCellType()
            {

                  case STRING: // '\001'
                    ExcelData = cell.getStringCellValue().toString();
                    break;

                case NUMERIC: // '\0'
                    if(cell.getNumericCellValue() % 1.0D == 0.0D)
                        ExcelData = (new StringBuilder(String.valueOf((int)cell.getNumericCellValue()))).toString();
                    else
                        ExcelData = (new StringBuilder(String.valueOf(cell.getNumericCellValue()))).toString();
                    break;

                case BOOLEAN: // '\004'
                    if(cell.getBooleanCellValue())
                        ExcelData = "true";
                    else
                        ExcelData = "false";
                    break;
                default:
                    break;
            }
        }
        catch(Exception exception) { }
        return ExcelData;
    }

    public HashMap exlReadMatserTestData(String TestCaseDetail)
    {
        System.out.println();
        HashMap dicTestData = new HashMap();
        String TestCase = "";
        TestCaseDetail = TestCaseDetail.replace("TestScript.", "");
        TestCaseDetail = TestCaseDetail.substring(TestCaseDetail.indexOf(".") + 1);
        String TestName[] = TestCaseDetail.split("\\.");
        ArrayList KeyName = new ArrayList();
        ArrayList Value = new ArrayList();
        String Test[] = TestName[1].split("_");
        String filename = (new StringBuilder(String.valueOf((String) TestBase.dicProjectVar.get("testdata")))).append((String)TestBase.dicConfig.get("MasterTestDataPath")).toString();
        log.info(filename);
        int RowCount = 0;
        int ExpectedRowIndex = 0;
        try
        {
            if(TestName.length == 2)
            {
                InputStream fis = null;
                try
                {
                    fis = new FileInputStream(filename);
                }
                catch(Exception ex)
                {
                    filename = (String)TestBase.dicConfig.get("MasterTestDataPath");
                    fis = new FileInputStream((String)TestBase.dicConfig.get("MasterTestDataPath"));
                }
                HSSFWorkbook workbook = new HSSFWorkbook(fis);
                HSSFSheet sheet = workbook.getSheet(TestName[0]);
                HSSFRow row = sheet.getRow(0);
                KeyName = exlCreateHeader(row, false);
                for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++)
                {
                    row = sheet.getRow(i);
                    TestCase = (new StringBuilder(String.valueOf(getData(2, row).replaceAll("_", "")))).append("_").append(getData(1, row)).toString();
                    if(TestCase.toLowerCase().equals((new StringBuilder(String.valueOf(Test[0].toLowerCase()))).append("_").append(Test[1].toLowerCase()).toString()))
                    {
                        if(ExpectedRowIndex == 0)
                            ExpectedRowIndex = i;
                        RowCount++;
                    }
                }

                if(RowCount >= Integer.parseInt(Test[2]))
                {
                    row = sheet.getRow(ExpectedRowIndex + (Integer.parseInt(Test[2]) - 1));
                    TestCase = (new StringBuilder(String.valueOf(getData(2, row).replaceAll("_", "")))).append("_").append(getData(1, row)).toString();
                    if(TestCase.toLowerCase().equals((new StringBuilder(String.valueOf(Test[0].toLowerCase()))).append("_").append(Test[1].toLowerCase()).toString()))
                    {
                        for(int j = 1; j <= row.getLastCellNum(); j++)
                            Value.add(getData(j, row));

                    }
                    for(int i = 0; i < KeyName.size(); i++)
                        dicTestData.put((String)KeyName.get(i), (String)Value.get(i));

                    log.info(String.format("Data against Testcase  \"%s\" is readed successfully.", new Object[] {
                            TestCaseDetail
                    }));
                } else
                {
                    System.out.println("Number of row Error");
                }
            } else
            {
                log.error(String.format("Issue in the testcase name \"%s\".", new Object[] {
                        TestCase
                }));
            }
        }
        catch(Exception ex)
        {
            log.error(String.format("Error while reading test suite \"%s\".\n Error is -: %s.", new Object[] {
                    filename, ex.getMessage()
            }));
        }
        return dicTestData;
    }

    private ArrayList exlCreateHeader(HSSFRow row, boolean isTestSuite)
    {
        boolean flag = false;
        ArrayList KeyName = new ArrayList();
        for(int i = 1; i <= row.getLastCellNum(); i++)
        {
            String key = getData(i, row);
            if(key != "" && key != null)
            {
                if(isTestSuite)
                    if(((String)TestBase.dicConfig.get("JiraIntegration")).equalsIgnoreCase("yes"))
                    {
                        if(key.equalsIgnoreCase("TestCaseId") && !flag)
                        {
                            RunModeIndex = i;
                            flag = true;
                        }
                    } else
                    if(key.toLowerCase().equals("run") && !flag)
                    {
                        RunModeIndex = i;
                        flag = true;
                    }
                KeyName.add(getData(i, row));
            }
        }

        return KeyName;
    }

    public HashMap<String,String>exlReadRowDetails(String fileName,String WorkSheetName,String ENV){
        HashMap<String,String> output = new HashMap<String,String>();
        String RunTimeValue = "";
        int foundRow = 0;
        try{
            InputStream fis = null;
            fis = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheet(WorkSheetName);
            HSSFRow row = sheet.getRow(0);
            for(int i = 1;  i < sheet.getPhysicalNumberOfRows(); i++){
                row = sheet.getRow(i);
                RunTimeValue = String.valueOf(getData(1,row));
                if(RunTimeValue.toLowerCase().equals(ENV.toLowerCase()))
                {
                    foundRow = i;
                    break;
                }
            }
            if(foundRow > 0)
            {
                HSSFRow HeaderRow = sheet.getRow(0);
                row = sheet.getRow(foundRow);
                for(int j = 1; j <= row.getLastCellNum(); j++)
                {
                    output.put(getData(j, HeaderRow), getData(j,row));
                }
            }
            workbook.close();
        }catch(Exception ex){
            log.error(ex.getMessage());
            return null;
        }
        return output;
    }
/**
    public static void main(String[] args){
        ReadExcel obj = new ReadExcel();
        HashMap<String,String>dicDBDetails = obj.exlReadRowDetails("D:\\automationframework\\src\\main\\resources\\config\\Config.xlsx","DBDetails","QA01");
    }
 */
}
