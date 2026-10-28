/** Наблюдатель: получает событие, когда подошло время напоминания. */
public interface ReminderObserver {
    void onReminder(Reminder reminder);
}
