package com.ssr.batchexcel;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<Sheet> readExcelFilesFromClasspath(String... fileNames) throws Exception {
        List<Sheet> sheets = new ArrayList<>();
        for (String fileName : fileNames) {
            Resource resource = resourceLoader.getResource("classpath:input/" + fileName);
            try (InputStream inputStream = resource.getInputStream();
                 Workbook workbook = WorkbookFactory.create(inputStream)) {
                 Sheet sheet = workbook.getSheetAt(0);
                 sheets.add(sheet);
            } catch (Exception e) {
                throw new Exception("Error reading Excel file: " + fileName, e);
            }
        }
        return sheets;
    }
}
