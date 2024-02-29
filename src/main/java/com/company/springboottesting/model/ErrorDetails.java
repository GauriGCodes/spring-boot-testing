package com.company.springboottesting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ErrorDetails {
    private Date timeStamp;
    private String message;
    private String details;
}
