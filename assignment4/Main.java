package patterns;// Observer pattern
import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String message);
}

class Client implements Observer {
    private String name;

    public Client(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("Уведомление для " + name + ": " + message);
    }
}

abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}

class Order extends Subject {
    private String status;

    public Order(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers("Статус заказа обновлен на: " + this.status);
    }
}

// State pattern
interface OrderState {
    void handle(OrderContext context);
}

class CreatedState implements OrderState {
    @Override
    public void handle(OrderContext context) {
        System.out.println("Заказ создан.");
        context.setState(new ConfirmedState());
    }
}

class ConfirmedState implements OrderState {
    @Override
    public void handle(OrderContext context) {
        System.out.println("Заказ подтвержден.");
        context.setState(new OnTheWayState());
    }
}

class OnTheWayState implements OrderState {
    @Override
    public void handle(OrderContext context) {
        System.out.println("Такси в пути.");
        context.setState(new FinishedState());
    }
}

class FinishedState implements OrderState {
    @Override
    public void handle(OrderContext context) {
        System.out.println("Заказ завершен.");
    }
}

class OrderContext {
    private OrderState state;

    public OrderContext() {
        state = new CreatedState();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void proceed() {
        state.handle(this);
    }
}

// Strategy pattern
interface PricingStrategy {
    double calculateCost(TripData data);
}

class DistanceBasedPricing implements PricingStrategy {
    @Override
    public double calculateCost(TripData data) {
        return data.getDistance() * 10;
    }
}

class TimeBasedPricing implements PricingStrategy {
    @Override
    public double calculateCost(TripData data) {
        return data.getTime() * 5;
    }
}

class FixedPricing implements PricingStrategy {
    @Override
    public double calculateCost(TripData data) {
        return 50;
    }
}

class TripCostCalculator {
    private PricingStrategy strategy;

    public TripCostCalculator(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateCost(TripData data) {
        return strategy.calculateCost(data);
    }
}

class TripData {
    private double distance;
    private double time;

    public TripData(double distance, double time) {
        this.distance = distance;
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }
}

// Template Method pattern
abstract class OrderProcess {
    public final void processOrder() {
        checkAvailability();
        calculateCost();
        confirmOrder();
    }

    protected void checkAvailability() {
        System.out.println("Проверка доступности...");
    }

    protected abstract void calculateCost();

    protected void confirmOrder() {
        System.out.println("Заказ подтвержден.");
    }
}

class TaxiOrderProcess extends OrderProcess {
    @Override
    protected void calculateCost() {
        System.out.println("Расчет стоимости...");
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        // Observer pattern demo
        Client client1 = new Client("Алексей");
        Client client2 = new Client("Мария");
        Order order = new Order("создан");
        order.attach(client1);
        order.attach(client2);
        order.setStatus("подтвержден");
        order.setStatus("в пути");

        // State pattern demo
        System.out.println("\nДемонстрация паттерна State:");
        OrderContext orderContext = new OrderContext();
        orderContext.proceed();
        orderContext.proceed();
        orderContext.proceed();

        // Strategy pattern demo
        System.out.println("\nДемонстрация паттерна Strategy:");
        TripData tripData = new TripData(15, 30);
        TripCostCalculator calculator = new TripCostCalculator(new DistanceBasedPricing());
        System.out.println("Стоимость по расстоянию: " + calculator.calculateCost(tripData));

        calculator.setStrategy(new TimeBasedPricing());
        System.out.println("Стоимость по времени: " + calculator.calculateCost(tripData));

        calculator.setStrategy(new FixedPricing());
        System.out.println("Фиксированная стоимость: " + calculator.calculateCost(tripData));

        // Template Method pattern demo
        System.out.println("\nДемонстрация паттерна Template Method:");
        OrderProcess process = new TaxiOrderProcess();
        process.processOrder();
    }
}
