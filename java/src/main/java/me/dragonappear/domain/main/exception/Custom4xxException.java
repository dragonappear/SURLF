package me.dragonappear.domain.main.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Custom4xxException extends RuntimeException {
    private CustomExceptionError error;
}
