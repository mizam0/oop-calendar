/** Стратегия: КАК именно напомнить пользователю. */
public interface ReminderStrategy {
    void remind(Reminder reminder);
}
