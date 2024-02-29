package yong.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import yong.model.ResponseVO;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseVO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.append(error.getDefaultMessage()).append("; "));

        return new ResponseEntity<>(ResponseVO.builder()
                .status("fail")
                .message(errors.toString())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseVO> handleMethodArgumentNotValidException(HandlerMethodValidationException ex) {

        StringBuilder errors = new StringBuilder();
        ex.getAllErrors().forEach(error ->
                errors.append(error.getDefaultMessage()).append("; "));

        return new ResponseEntity<>(ResponseVO.builder()
                .status("fail")
                .message(errors.toString())
                .build(), HttpStatus.BAD_REQUEST);
    }
}