import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Календарь = Субъект (Observable) хранит напоминания и оповещает наблюдателей. */
public class ReminderCalendar {
    private final List<Reminder> reminders = new ArrayList<>();
    private final List<ReminderObserver> observers = new ArrayList<>();

    public synchronized void addObserver(ReminderObserver o) { observers.add(o); }
    public synchronized void removeObserver(ReminderObserver o) { observers.remove(o); }

    public synchronized void addReminder(Reminder r) { reminders.add(r); }

    public synchronized boolean removeReminder(int id) {
        return reminders.removeIf(r -> r.getId() == id);
    }

    public synchronized List<Reminder> getAll() {
        List<Reminder> copy = new ArrayList<>(reminders);
        copy.sort(Comparator.comparing(Reminder::getTime));
        return copy;
    }

    public synchronized boolean hasRemindersOn(LocalDate date) {
        return reminders.stream().anyMatch(r -> r.getTime().toLocalDate().equals(date));
    }

    /** Вызывается по таймеру: находит наступившие напоминания и уведомляет наблюдателей. */
    public synchronized void checkDue(LocalDateTime now) {
        for (Reminder r : reminders) {
            if (!r.isFired() && !r.getTime().isAfter(now)) {
                r.markFired();
                notifyObservers(r);
            }
        }
    }

    private void notifyObservers(Reminder r) {
        for (ReminderObserver o : observers) {
            o.onReminder(r);
        }
    }
}
