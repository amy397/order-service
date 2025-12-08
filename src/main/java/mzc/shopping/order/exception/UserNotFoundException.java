package mzc.shopping.order.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("회원을 찾을 수 없습니다. ID: " + id);
    }
}