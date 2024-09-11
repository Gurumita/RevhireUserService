package com.revhire.userservice.utilities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private String messages;
    private T data;
    private int status;
}