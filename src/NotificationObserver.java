/** Наблюдатель, который отправляет уведомление выбранным у напоминания способом (стратегией). */
public class NotificationObserver implements ReminderObserver {
    @Override
    public void onReminder(Reminder r) {
        r.getStrategy().remind(r);
    }
}
