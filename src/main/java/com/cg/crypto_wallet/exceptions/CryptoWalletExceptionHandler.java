package com.cg.crypto_wallet.exceptions;

import com.cg.crypto_wallet.DTO.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CryptoWalletExceptionHandler {

    private static final String message = "Exception while processing REST Request";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        List<String> errMsg = errorList.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        ResponseDto respDTO = new ResponseDto(message,errMsg);
        return new ResponseEntity<>(respDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CryptoWalletException.class)
    public ResponseEntity<ResponseDto> handleCryptoWalletException(CryptoWalletException exception) {
        ResponseDto respDTO = new ResponseDto("Error: " ,exception.getMessage());
        return new ResponseEntity<>(respDTO, HttpStatus.BAD_REQUEST);
    }
}
