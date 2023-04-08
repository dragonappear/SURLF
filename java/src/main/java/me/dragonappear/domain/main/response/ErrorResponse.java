package me.dragonappear.domain.main.response;

import lombok.Data;

/**
 * Error Response
 * <p>
 * Since: 1.0.0
 * <p>
 * Author: dragonappear
 * <p>
 * {
 * "errorCode": 400,
 * "errorMsg": "Bad Request",
 * }
 */

@Data
public class ErrorResponse {
    private int errorCode;
    private String errorMsg;
}
