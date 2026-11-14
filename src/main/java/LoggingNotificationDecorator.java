public class LoggingNotificationDecorator
        implements NotificationService {

    private final NotificationService delegate;

    public LoggingNotificationDecorator(
            NotificationService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void notify(String message) {
        System.out.println("[LOG] " + message);
        delegate.notify(message);
    }
}
