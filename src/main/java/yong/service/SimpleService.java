package yong.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yong.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class SimpleService {

    public void method() {
        System.out.println("method called");
        throw new BusinessException();
    }
}
