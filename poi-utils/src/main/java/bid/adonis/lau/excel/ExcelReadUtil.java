package bid.adonis.lau.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author adonis
 * @Date 2019/12/18 2:31 下午
 * @Description 读取Excel文件
 */
@Slf4j
public class ExcelReadUtil {
    
    public static HashMap<String, ArrayList<ArrayList<String>>> readExcel(File file, int ignoreRow) {
        String xlsxSuffix = ".xlsx";
        String xlsSuffix = ".xls";
        if (file.getName().toLowerCase().endsWith(xlsxSuffix)) {
            return readExcelForXlsx(file, ignoreRow);
        } else if (file.getName().toLowerCase().endsWith(xlsSuffix)) {
            return readExcelForXls(file, ignoreRow);
        }
        return null;
    }
    /**
     * 读取Excel xlsx后缀名文件数据
     *
     * @param file Excel文件对象
     * @param ignoreRow 忽略前几行
     */
    private static HashMap<String, ArrayList<ArrayList<String>>> readExcelForXlsx(File file, int ignoreRow) {
        HashMap<String, ArrayList<ArrayList<String>>> map = new HashMap<>();
        if (!file.exists()) {
            log.error("{}文件不存在", file.getName());
            return null;
        }
        int rowSize = 0;
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            XSSFWorkbook workbook = null;
            try {
                workbook = new XSSFWorkbook(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            XSSFCell cell = null;
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                XSSFSheet sheet = workbook.getSheetAt(sheetIndex);


                ArrayList<ArrayList<String>> lists = new ArrayList<>();
                for (int rowIndex = ignoreRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    XSSFRow row = sheet.getRow(rowIndex);
                    if (null == row) {
                        continue;
                    }

                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }

                    ArrayList<String> list = new ArrayList<>();
                    int col = 0;

                    for (int colIndex = 0; colIndex <= row.getLastCellNum(); colIndex++) {
                        cell = row.getCell(colIndex);
                        String value = "";
                        if (cell != null) {
                            CellType cellType = cell.getCellType();

                            switch (cellType) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        value = String.valueOf(cell.getDateCellValue());
                                    } else {
                                        value = String.valueOf(new DecimalFormat("0").format(cell.getNumericCellValue()));
                                    }
                                    break;
                                case STRING:
                                    value = String.valueOf(cell.getStringCellValue());
                                    break;
                                case FORMULA:
                                    value = String.valueOf(cell.getCellFormula());
                                    break;
                                case BLANK:
                                    value = "";
                                    break;
                                case BOOLEAN:
                                    value = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    value = String.valueOf(cell.getErrorCellValue());
                                    break;
                                default:
                                    value = "";
                            }
                            if (StringUtils.isNotBlank(value)) {
                                list.add(value);
                            } else {
                                col++;
                            }
                        }
                    }
                    if (col == row.getRowNum()) {
                        continue;
                    }
                    if (list.size() > 0) {
                        lists.add(list);
                    }
                }

                map.put("sheet" + sheetIndex, lists);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 读取excel xls后缀名文件
     *
     * @param file Excel文件对象
     * @param ignoreRow 忽略前几行
     */
    private static HashMap<String, ArrayList<ArrayList<String>>> readExcelForXls(File file, int ignoreRow) {
        HashMap<String, ArrayList<ArrayList<String>>> map = new HashMap<>(10);
        if (!file.exists()) {
            log.error("{}文件不存在", file.getName());
            return null;
        }
        int rowSize = 0;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            HSSFWorkbook workbook = new HSSFWorkbook(bufferedInputStream);
            HSSFCell cell = null;
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                ArrayList<ArrayList<String>> lists = new ArrayList<>();
                for (int rowIndex = ignoreRow; rowIndex < sheet.getLastRowNum(); rowIndex++) {
                    HSSFRow row = sheet.getRow(rowIndex);
                    if (null == row) {
                        continue;
                    }

                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }
                    ArrayList<String> list = new ArrayList<>();
                    int col = 0;
                    for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
                        cell = row.getCell(colIndex);
                        String value = "";
                        if (cell != null) {
                            CellType cellType = cell.getCellType();

                            switch (cellType) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        value = String.valueOf(cell.getDateCellValue());
                                    } else {
                                        value = String.valueOf(new DecimalFormat("0").format(cell.getNumericCellValue()));
                                    }
                                    break;
                                case STRING:
                                    value = String.valueOf(cell.getStringCellValue());
                                    break;
                                case FORMULA:
                                    value = String.valueOf(cell.getCellFormula());
                                    break;
                                case BLANK:
                                    value = "";
                                    break;
                                case BOOLEAN:
                                    value = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    value = String.valueOf(cell.getErrorCellValue());
                                    break;
                                default:
                                    value = "";
                            }
                            if (StringUtils.isNotBlank(value)) {
                                list.add(value);
                            } else {
                                col++;
                            }
                        }
                    }
                    if (col == row.getRowNum()) {
                        continue;
                    }
                    if (list.size() > 0) {
                        lists.add(list);
                    }
                }
                map.put("sheet" + sheetIndex, lists);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
