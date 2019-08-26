package Lesson2.Messages;

public enum MessageType {
    END, // Запрос на отключение или ответ на отключение
    AUTH, // Запрос авторизации
    AUTH_OK, // Ответ об успешной авторизации
    AUTH_ERROR, // Ответ об ошибке авторизации (сообщение в поле message)
    PRIVATE_MESSAGE, // Приватное сообщение
    BROADCAST_MESSAGE, // Сообщение всем
    ERROR_MESSAGE, // Ошибка при разборе сообщения
    INFO_MESSAGE, // Информационные сообщения
    ADD_BLOCK
}
