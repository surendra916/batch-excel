package com.ssr.batchexcel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class StudentProcessor implements ItemProcessor<StudentDTO, StudentDTO> {
    @Override
    public StudentDTO process(StudentDTO dto) throws Exception {
        log.info("Inside processor");
//        Student student = Student.builder()
//                .name(dto.getName())
//                .emailAddress(dto.getEmailAddress())
//                .purchasedPackage(dto.getPurchasedPackage())
//                .username(dto.getUsername())
//                .age(dto.getAge())
//                .gender(dto.getGender())
//                .grade(dto.getGrade())
//                .build();
        return dto;
    }
}
