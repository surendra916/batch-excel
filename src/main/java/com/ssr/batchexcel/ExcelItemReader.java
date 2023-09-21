package com.ssr.batchexcel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Iterator;

@Component
@Slf4j
public class ExcelItemReader extends StepExecutionListenerSupport implements ItemStreamReader<StudentDTO>, ResourceAwareItemReaderItemStream<StudentDTO> {


    //@Value(value = "classpath:input/students.xlsx")
    private Resource resource;

    private Iterator<Row> rowIterator;
    private Workbook workbook;

    private int lineCount = 0;

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public StudentDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Inside read method");
        Row studentRow;
        Row studentDetailRow;
        StudentDTO studentDTO = null;
        while(rowIterator.hasNext()){
            //First part
            skipLines(rowIterator,1);
            studentRow = rowIterator.next();// First set of data
            studentDTO = new StudentDTO();
            studentDTO.setName(studentRow.getCell(0).getStringCellValue());
            studentDTO.setEmailAddress(studentRow.getCell(1).getStringCellValue());
            studentDTO.setPurchasedPackage(studentRow.getCell(2).getStringCellValue());

            // 2nd part
            skipLines(rowIterator,2);
            studentDetailRow = rowIterator.next();
            studentDTO.setUsername(studentDetailRow.getCell(0).getStringCellValue());
            studentDTO.setAge((int) studentDetailRow.getCell(1).getNumericCellValue());
            studentDTO.setGender(studentDetailRow.getCell(2).getStringCellValue());
            studentDTO.setGrade(studentDetailRow.getCell(3).getStringCellValue());
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
        lineCount = 0;
        return ExitStatus.COMPLETED;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try{
            FileInputStream fileInputStream = new FileInputStream(resource.getFile());
            workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            rowIterator = sheet.iterator();
        }catch (Exception e){
            throw new ItemStreamException("Error opening the workbook");
        }
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
