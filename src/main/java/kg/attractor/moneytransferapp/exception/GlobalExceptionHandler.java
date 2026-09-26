package kg.attractor.moneytransferapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    private ModelAndView page(String key, HttpStatus status) {
        ModelAndView result = new ModelAndView("error");
        result.addObject("errorKey", key);
        result.setStatus(status);
        return result;
    }
    
    @ExceptionHandler(BusinessException.class)
    public ModelAndView business(BusinessException ex) {
        log.warn("Operation rejected: {}", ex.getMessage());
        return page(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ModelAndView invalid(Exception ex) {
        log.warn("Invalid request: {}", ex.getClass().getSimpleName());
        return page("error.input", HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView conflict(DataIntegrityViolationException ex) {
        log.warn("Conflicting database operation", ex);
        return page("error.conflict", HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView denied(AccessDeniedException ex) {
        return page("error.forbidden", HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView missing(NoResourceFoundException ex) {
        return page("error.notFound", HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView unexpected(Exception ex) {
        log.error("Request failed", ex);
        return page("error.unexpected", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
