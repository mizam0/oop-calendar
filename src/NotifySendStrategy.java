import java.io.IOException;

/** Напоминание через системное уведомление линух (notify-send). */
public class NotifySendStrategy implements ReminderStrategy {
    @Override
    public void remind(Reminder r) {
        try {
            new ProcessBuilder("notify-send", r.getTitle(), r.getMessage()).start();
        } catch (IOException e) {
            // notify-send не установлен — запасной вариант
            System.out.println("\n[notify-send недоступен] " + r.getTitle() + " — " + r.getMessage());
        }
    }
}
