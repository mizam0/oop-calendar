import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reminder {
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static int counter = 0;

    private final int id = ++counter;
    private final String title;
    private final String message;
    private final LocalDateTime time;
    private final ReminderStrategy strategy;
    private boolean fired = false;

    public Reminder(String title, String message, LocalDateTime time, ReminderStrategy strategy) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.strategy = strategy;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public LocalDateTime getTime() { return time; }
    public ReminderStrategy getStrategy() { return strategy; }
    public boolean isFired() { return fired; }
    public void markFired() { fired = true; }

    @Override
    public String toString() {
        return "#" + id + " [" + time.format(FORMAT) + "] " + title + " — " + message
                + (fired ? " (сработало)" : "");
    }
}
