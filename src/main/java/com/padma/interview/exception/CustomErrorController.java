package com.padma.interview.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        errorDetails.put("timestamp", System.currentTimeMillis());
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            errorDetails.put("status", statusCode);
            errorDetails.put("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        }
        
        if (message != null && !message.toString().isEmpty()) {
            errorDetails.put("message", message);
        } else if (exception != null) {
            errorDetails.put("message", exception.toString());
        } else {
            errorDetails.put("message", "Unknown error occurred");
        }
        
        return ResponseEntity.status(status != null ? 
                Integer.valueOf(status.toString()) : HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(errorDetails);
    }
} 