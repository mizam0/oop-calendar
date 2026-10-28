/** Напоминание просто текстом в консоль. */
public class ConsoleStrategy implements ReminderStrategy {
    @Override
    public void remind(Reminder r) {
        System.out.println("\nНАПОМИНАНИЕ: " + r.getTitle() + " — " + r.getMessage());
    }
}
