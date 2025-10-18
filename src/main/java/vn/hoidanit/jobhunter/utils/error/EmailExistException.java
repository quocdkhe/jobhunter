package vn.hoidanit.jobhunter.utils.error;

public class EmailExistException extends Exception {
    public EmailExistException() {
        super("Email đã tồn tại");
    }
}
