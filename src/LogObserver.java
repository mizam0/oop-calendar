import java.time.LocalDateTime;

/** Второй наблюдатель — просто пишет в лог, что напоминание сработало. */
public class LogObserver implements ReminderObserver {
    @Override
    public void onReminder(Reminder r) {
        System.out.println("[LOG " + LocalDateTime.now().withNano(0) + "] сработало #" + r.getId() + " \"" + r.getTitle() + "\"");
    }
}
