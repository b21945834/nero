package tr.com.nero.common;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class NeroResponseBodyExceptionHandler  {

    @ExceptionHandler(value
            = { Exception.class })
    public ResponseEntity<BaseResponse<Void>> handleBaseException(
            Exception ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseEntity.ok(new BaseResponse<Void>(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ex.printStackTrace();
        return new ResponseEntity<>(new BaseResponse<>(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NeroIllegalArgumentException.class)
    public ResponseEntity<BaseResponse<String>> handleIllegalArgument(NeroIllegalArgumentException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new BaseResponse<>(ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }
}