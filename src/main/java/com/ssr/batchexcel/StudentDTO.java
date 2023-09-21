package com.ssr.batchexcel;

import lombok.Data;
import java.util.List;

@Data
public class StudentDTO {

    //First level
    private String emailAddress;
    private String name;
    private String purchasedPackage;

    // 2nd level
    private String username;
    private int age;
    private String gender;
    private String grade;

}
