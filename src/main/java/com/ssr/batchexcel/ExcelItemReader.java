package com.ssr.batchexcel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class ExcelItemReader extends StepExecutionListenerSupport implements ItemStreamReader<StudentDTO>, ResourceAwareItemReaderItemStream<StudentDTO> {


    @Autowired
    private FileService fileService;

    private Resource resource;

    private Iterator<Row> rowIterator;
    private Workbook workbook;

    private List<Sheet> sheets;

    private int fileCount = 0;

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public StudentDTO read() {
        log.info("Inside read method");
        if(fileCount < sheets.size()){
            rowIterator  = sheets.get(fileCount).iterator();
            fileCount++;
            return constructStudentDTO(rowIterator);
        }else {
            return null;
        }
    }

    public StudentDTO constructStudentDTO(Iterator<Row> rowIterator){
        Row studentRow;
        Row studentDetailRow;
        StudentDTO studentDTO;
        StudentDetail studentDetail;
        List<StudentDetail> studentDetailsList = null;
        int cellNum = 0;
        if(rowIterator.hasNext()){
            //First part
            skipLines(rowIterator,1);
            studentRow = rowIterator.next();// First set of data
            cellNum = studentRow.getFirstCellNum();
            studentDTO = new StudentDTO();
            studentDTO.setName(studentRow.getCell(cellNum++).getStringCellValue());
            studentDTO.setEmailAddress(studentRow.getCell(cellNum++).getStringCellValue());
            studentDTO.setPurchasedPackage(studentRow.getCell(cellNum).getStringCellValue());

            // 2nd part
            skipLines(rowIterator,2);
            studentDetailsList = new ArrayList<>();
            while (rowIterator.hasNext()){
                studentDetailRow = rowIterator.next();
                cellNum = studentDetailRow.getFirstCellNum();
                studentDetail = new StudentDetail();
                studentDetail.setUsername(studentDetailRow.getCell(cellNum++).getStringCellValue());
                studentDetail.setAge((int) studentDetailRow.getCell(cellNum++).getNumericCellValue());
                studentDetail.setGender(studentDetailRow.getCell(cellNum++).getStringCellValue());
                studentDetail.setGrade(studentDetailRow.getCell(cellNum).getStringCellValue());

                studentDetailsList.add(studentDetail);
            }
            studentDTO.setStudentDetails(studentDetailsList);
            return studentDTO;
        }
        return null;
    }

    public void skipLines(Iterator<Row> rowIterator,int num){
        for(int i = 1;  i <= num;  i++){
            if(rowIterator.hasNext())
            {
                rowIterator.next();
            }
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fileCount = 0;
        System.out.println("inside after step");
        return ExitStatus.COMPLETED;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        // sheets at line 37 should be assigned
        try {
            sheets = fileService.readExcelFilesFromClasspath("students1.xlsx", "students2.xlsx", "students3.xlsx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        try{
//            FileInputStream fileInputStream = new FileInputStream(resource.getFile());
//            workbook = new XSSFWorkbook(fileInputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//            rowIterator = sheet.iterator();
//        }catch (Exception e){
//            throw new ItemStreamException("Error opening the workbook");
//        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
        try{
            if(workbook != null){
                workbook.close();
            }
        }catch (Exception exception){
            throw new ItemStreamException("Error closing the workbook");
        }
    }
}
