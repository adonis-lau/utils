package bid.adonis.lau.test;

import bid.adonis.lau.excel.ExcelReadUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author adonis
 * @Date 2019/12/18 2:43 下午
 * @Description
 */
public class ExcelReaderTest {
    @Test
    public void testExcelRead(){
        String excelFilePath = "/Users/adonis/Downloads/20191208-下午茶/all.xlsx";
//        String excelFilePath = "/Users/adonis/Downloads/20191208-下午茶/杭州下午茶9月和10月短信及核销汇总.xlsx";
        HashMap<String, ArrayList<ArrayList<String>>> excelReadMap = ExcelReadUtil.readExcel(new File(excelFilePath), 1);
        if(excelReadMap != null){
            excelReadMap.entrySet().stream().forEach(entry -> {
                entry.getValue().stream().forEach(col -> {
                    col.stream().forEach(System.out::println);
                });
            });

/*
            excelReadMap.size();
            excelReadMap.entrySet().stream().forEach(entry -> {
                System.out.println(entry.getValue().size());
            });
*/
        }
    }

}
