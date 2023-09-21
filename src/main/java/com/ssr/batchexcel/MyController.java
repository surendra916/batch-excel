package com.ssr.batchexcel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

@RestController
public class MyController {

    private final FileService fileService;

    public MyController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/read-excel-files")
    public String readExcelFiles() {
        try {
            List<Sheet> sheets = fileService.readExcelFilesFromClasspath("students1.xlsx","students2.xlsx","students3.xlsx");
            System.out.println(sheets.size());
            Iterator<Row> iterator = sheets.get(0).iterator();
            Row row = iterator.next();

            String s1 = row.getCell(0).getStringCellValue();
            String s2 = row.getCell(1).getStringCellValue();
            String s3 = row.getCell(2).getStringCellValue();
            System.out.println("cell content is "+ s1 +" = " + s2 +" = "+ s3);
            return s1 + " ,"+s2+ " ,"+s3;

        } catch (Exception e) {
            return "error occured";
        }
    }
}

