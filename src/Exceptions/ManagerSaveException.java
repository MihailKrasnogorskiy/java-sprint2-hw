package Exceptions;

// неотслеживаемое исключение записываемого менеджера
public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final String message) {
        super(message);
    }
}
