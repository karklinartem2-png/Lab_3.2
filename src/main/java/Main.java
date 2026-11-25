import exception.InvalidEmailException;
import exception.InvalidVanException;
import exception.VanServiceNotFoundException;
import model.*;
import service.ConsoleNotificationService;
import service.NotificationService;
import logistics.*;

public class Main {
    public static void main(String[] args) throws InvalidEmailException {
        NotificationService notificationService = new ConsoleNotificationService();
        VanLogistics VanLogistics = new VanLogistics(notificationService);

        // --- Подготовка успешных данных ---
        Loader loader1 = new Loader("Иван", 1, 100);
        Loader loader2 = new Loader("Константин", 2, 200);
        Loader loader3 = new Loader("Глеб", 3, 300);

        // Успешный заказ 1
        VanService sub1 = new PersonalOrder("Константин Витальевич", 399.0);
        sub1.addLoader(loader1);
        sub1.addLoader(loader2);

        // Успешный заказ 2
        VanService sub2 = new CorporateOrder("ООО Тмыв", 2000.0, 0.15); // 15% скидка
        sub2.addLoader(loader3);
        sub2.addLoader(loader1);

        // Регистрация в базе
        VanLogistics.registerVanService("const@mail.ru", sub1);
        VanLogistics.registerVanService("corporate@tmiv.com", sub2);

        System.out.println("=== Запуск демонстрации системы ===");

        // Сценарий 1: Успех
        System.out.println("\n--- Сценарий 1: Успешная оплата ---");
        try {
            VanLogistics.processPaymentByEmail("const@mail.ru");
            VanLogistics.processPaymentByEmail("corporate@tmiv.com");
        } catch (VanServiceNotFoundException e) {
            System.out.println("Этот текст на экран не выведется 67676767676767667678: " + e.getMessage());
        }

        // Сценарий 2: Проверяемое исключение (VanServiceNotFoundException)
        System.out.println("\n--- Сценарий 2: Попытка оплаты несуществующего Email ---");
        try {
            VanLogistics.processPaymentByEmail("GrigoryLeps@mail.com");
        } catch (VanServiceNotFoundException e) {
            System.err.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
        }
        System.out.println("-> Доказательство: Программа продолжила работу после обработки ошибки.");

        // Сценарий 3: Непроверяемое исключение (InvalidVanServiceException)
        System.out.println("\n--- Сценарий 3: Создание некорректного заказа (отрицательная цена) ---");
        try {
            VanService badOrd = new PersonalOrder("Кирил", -100.0);
        } catch (InvalidVanException e) {
            System.err.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
        }

        System.out.println("\n--- Сценарий 3.2: Создание некорректного заказа (пустое имя) ---");
        try {
            VanService badSub2 = new PersonalOrder("   ", 500.0);
        } catch (InvalidVanException e) {
            System.err.println("[ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ]: " + e.getMessage());
        }

        System.out.println("\n=== Демонстрация успешно завершена! Система стабильна. ===");
    }
}
