package meetween.backend.member.exception;

public class InvalidMemberException extends RuntimeException {
    public InvalidMemberException() {
        super("잘못된 회원의 정보입니다.");
    }

    public InvalidMemberException(final String message) {
        super(message);
    }
}
