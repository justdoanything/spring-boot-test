package yong.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private String message;
    private String status;

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
