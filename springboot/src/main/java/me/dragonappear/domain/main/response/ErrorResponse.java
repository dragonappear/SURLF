package me.dragonappear.domain.main.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int errorCode;
    private String errorMsg;
}
