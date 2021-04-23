package io.zipcoder.tc_spring_poll_application.exception;

//import io.zipcoder.tc_spring_poll_application.dtos.error.ErrorDetail;
//import io.zipcoder.tc_spring_poll_application.dtos.error.ValidationError;
//import org.springframework.context.MessageSource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@ControllerAdvice
//public class RestExceptionHandler extends ResponseEntityExceptionHandler {
//    @Inject
//    private MessageSource messageSource;
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public @ResponseBody ErrorDetail handleValidationError(MethodArgumentNotValidException
//                                                                         manve, HttpServletRequest request){
//        ErrorDetail errorDetail = new ErrorDetail();
//        errorDetail.setTimeStamp(new Date().getTime());
//        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
//        String requestPath = (String) request.getAttribute("javax.servlet.error.request_uri");
//        if(requestPath == null){
//            requestPath = request.getRequestURI();
//        }
//        errorDetail.setTitle("Validation Failed");
//        errorDetail.setDetail("Input validation failed");
//        errorDetail.setDeveloperMessage(manve.getClass().getName());
//
//        //Create ValidationError instances
//        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
//        for(FieldError fe : fieldErrors){
//            List<ValidationError> validationErrorList = errorDetail.getErrors().
//                    get(fe.getField());
//            if(validationErrorList == null){
//                validationErrorList = new ArrayList<ValidationError>();
//                errorDetail.getErrors().put(fe.getField(),
//                        validationErrorList);
//            }
//            ValidationError validationError = new ValidationError();
//            validationError.setCode(fe.getCode());
//            validationError.setMessage(fe.getDefaultMessage());
//            validationErrorList.add(validationError);
//        }
//        return errorDetail;
//    }
//
//
//}
import io.zipcoder.tc_spring_poll_application.dtos.error.ErrorDetail;
import io.zipcoder.tc_spring_poll_application.dtos.error.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe, HttpServletRequest request) {

        ErrorDetail ed = new ErrorDetail();
        ed.setTimeStamp(new Date().getTime());
        ed.setDetail(rnfe.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        rnfe.printStackTrace(pw);
        ed.setDeveloperMessage(sw.toString());

        ed.setTitle("Resource Not Found Exception");
        ed.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(ed, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest request){
        ErrorDetail ed = new ErrorDetail();
        ed.setTimeStamp(new Date().getTime());
        ed.setTitle("Resource Validation Failed");
        ed.setStatus(HttpStatus.BAD_REQUEST.value());
        ed.setDetail(manve.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        manve.printStackTrace(pw);
        ed.setDeveloperMessage(sw.toString());


        List<FieldError> fieldErrors =  manve.getBindingResult().getFieldErrors();
        for(FieldError fe : fieldErrors) {

            List<ValidationError> validationErrorList = ed.getErrors().get(fe.getField());
            if(validationErrorList == null) {
                validationErrorList = new ArrayList<>();
                ed.getErrors().put(fe.getField(), validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(messageSource.getMessage(fe, null));
            validationErrorList.add(validationError);
        }
        return new ResponseEntity<ErrorDetail>(ed, HttpStatus.BAD_REQUEST);
    }



}
