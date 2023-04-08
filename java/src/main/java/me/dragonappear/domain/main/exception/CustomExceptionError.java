package me.dragonappear.domain.main.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionError {

    NOT_VALID_ARGUMENT(4_000, "NOT_VALID_ARGUMENT"),
    NOT_EXIST_SHORT_ID(4_001, "NOT_EXIST_SHORT_ID"),
    INVALID_URL_FORMAT(4_002, "INVALID_URL_FORMAT"),
    INTERNAL_SERVER_ERROR(5_000, "INTERNAL_SERVER_ERROR"),
    EXHAUSTED_SHORT_LINK(5_001, "EXHAUSTED_SHORT_LINK");

    private final int errorCode;
    private final String errorMsg;
}
