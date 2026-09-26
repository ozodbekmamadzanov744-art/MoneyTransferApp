package kg.attractor.moneytransferapp.exception;

public class BusinessException extends RuntimeException {
    
    public BusinessException(String key) {
        super(key);
    }
}
