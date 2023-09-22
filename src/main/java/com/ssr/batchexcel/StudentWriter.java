package com.ssr.batchexcel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class StudentWriter implements ItemWriter<StudentDTO> {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void write(List<? extends StudentDTO> items) throws Exception {
        if(items instanceof List){
            System.out.println("Inside if "+ items);
            System.out.println("Size is  "+ items.size());
//            Student student = items.get(0);
//            Student savedStudent = studentRepository.save(student);
//            if(savedStudent != null){
//                log.info("Saved with ID : {}",savedStudent.getId());
//            }
        }else {
            System.out.println("Inside else "+ items);
        }
    }
}