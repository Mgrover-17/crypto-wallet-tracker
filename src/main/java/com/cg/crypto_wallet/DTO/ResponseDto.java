package com.cg.crypto_wallet.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ResponseDto {
    private String message;
    Object data;

    public ResponseDto(String message,Object data){
        this.message = message;
        this.data = data;
    }
}