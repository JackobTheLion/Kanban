package manager.exceptions;

public class ScheduleBookingException extends RuntimeException{

    public ScheduleBookingException() {
    }
    public ScheduleBookingException(String message) {
        super(message);
    }
    public ScheduleBookingException(String message, Throwable cause) {
        super(message, cause);
    }
    public ScheduleBookingException(Throwable cause) {
        super(cause);
    }
}
