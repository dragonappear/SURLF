package me.dragonappear.domain.main.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionError {

    NOT_VALID_ARGUMENT(400_000, "NOT_VALID_ARGUMENT"),
    NOT_EXIST_SHORT_ID(400_010, "NOT_EXIST_SHORT_ID"),
    INVALID_URL_FORMAT(400_020, "INVALID_URL_FORMAT"),
    NOT_FOUND_RESOURCE(404_000, "NOT_FOUND_RESOURCE"),
    NOT_SUPPORTED_METHOD(405_000, "NOT_SUPPORTED_METHOD"),
    INTERNAL_SERVER_ERROR(500_000, "INTERNAL_SERVER_ERROR"),
    EXHAUSTED_SHORT_LINK(500_001, "EXHAUSTED_SHORT_LINK");

    private final int errorCode;
    private final String errorMsg;
}
