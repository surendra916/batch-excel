package com.ssr.batchexcel;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ExcelFileToDatabaseJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Value(value = "classpath:input/students.xlsx")
    Resource resource;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<StudentDTO, Student>chunk(1)
                .reader(excelStudentReader())
                .processor(getStudentProcessor())
                .writer(getStudentWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Student> getStudentWriter(){
        return new StudentWriter();
    }

    @Bean
    @StepScope
    public ItemProcessor<StudentDTO, Student> getStudentProcessor(){
        return new StudentProcessor();
    }

    @Bean
    public ItemReader<StudentDTO> excelStudentReader() {
        ExcelItemReader excelItemReader = new ExcelItemReader();
        excelItemReader.setResource(resource);
        return excelItemReader;
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importuserjob")
                .start(step1())
                .build();
    }


}
