import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        ReminderCalendar calendar = new ReminderCalendar();
        calendar.addObserver(new NotificationObserver());
        calendar.addObserver(new LogObserver());

        // Раз в секунду проверяем, не пора ли что-то напомнить
        ScheduledExecutorService timer =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
        timer.scheduleAtFixedRate(
            () -> calendar.checkDue(LocalDateTime.now()),
            1,
            1,
            TimeUnit.SECONDS
        );

        Scanner in = new Scanner(System.in);
        YearMonth shown = YearMonth.now();

        while (true) {
            printMonth(calendar, shown);
            System.out.println(
                "\n1 - добавить | 2 - список | 3 - удалить | n/p - след./пред. месяц | 0 - выход"
            );
            System.out.print("> ");
            if (!in.hasNextLine()) break;
            String cmd = in.nextLine().trim();

            switch (cmd) {
                case "1" -> addReminder(in, calendar);
                case "2" -> {
                    if (calendar.getAll().isEmpty()) System.out.println(
                        "Напоминаний нет."
                    );
                    calendar.getAll().forEach(System.out::println);
                }
                case "3" -> {
                    System.out.print("ID для удаления: ");
                    try {
                        int id = Integer.parseInt(in.nextLine().trim());
                        System.out.println(
                            calendar.removeReminder(id)
                                ? "Удалено."
                                : "Не найдено."
                        );
                    } catch (NumberFormatException e) {
                        System.out.println("Нужно число.");
                    }
                }
                case "n" -> shown = shown.plusMonths(1);
                case "p" -> shown = shown.minusMonths(1);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private static void addReminder(Scanner in, ReminderCalendar calendar) {
        System.out.print("Заголовок: ");
        String title = in.nextLine().trim();
        if (title.isEmpty()) title = "Без названия";
        System.out.print("Текст: ");
        String message = in.nextLine().trim();

        LocalDateTime time = null;
        while (time == null) {
            System.out.print(
                "Когда? (yyyy-MM-dd HH:mm, либо +N минут, либо +Ns секунд): "
            );
            time = parseTime(in.nextLine().trim());
            if (time == null) System.out.println(
                "Не понял формат, попробуй ещё раз."
            );
        }

        System.out.print("Способ: 1 - консоль, 2 - notify-send: ");
        ReminderStrategy strategy = in.nextLine().trim().equals("2")
            ? new NotifySendStrategy()
            : new ConsoleStrategy();

        Reminder r = new Reminder(title, message, time, strategy);
        calendar.addReminder(r);
        System.out.println("Добавлено: " + r);
    }

    private static LocalDateTime parseTime(String s) {
        try {
            if (s.startsWith("+")) {
                if (s.endsWith("s")) {
                    return LocalDateTime.now().plusSeconds(
                        Long.parseLong(s.substring(1, s.length() - 1))
                    );
                }
                return LocalDateTime.now().plusMinutes(
                    Long.parseLong(s.substring(1))
                );
            }
            return LocalDateTime.parse(s.replace(' ', 'T'));
        } catch (Exception e) {
            return null;
        }
    }

    private static void printMonth(ReminderCalendar calendar, YearMonth ym) {
        System.out.println("\n" + ym.getMonth() + " " + ym.getYear());
        System.out.println(" Пн  Вт  Ср  Чт  Пт  Сб  Вс");
        int offset = ym.atDay(1).getDayOfWeek().getValue() - 1;
        StringBuilder sb = new StringBuilder("    ".repeat(offset));
        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate date = ym.atDay(d);
            sb.append(
                String.format(
                    "%2d%s ",
                    d,
                    calendar.hasRemindersOn(date) ? "*" : " "
                )
            );
            if (date.getDayOfWeek().getValue() == 7) sb.append("\n");
        }
        System.out.println(sb);
        System.out.println("(* — в этот день есть напоминания)");
    }
}
