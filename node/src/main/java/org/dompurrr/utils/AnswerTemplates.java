package org.dompurrr.utils;

public class AnswerTemplates {
    public static final String HELP_MESSAGE = "Доступные команды:\n" +
            "\t/help - вывод помощи\n" +
            "\t/register - генерация токена для интеграции ВК\n" +
            "\t/info - выводит информацию о вашем профиле\n" +
            "\t/changeName - смена имени профиля\n" +
            "\t/cancel - отмена выполнения последнего действия\n" +
            "\t/room - создание / редактирование комнаты\n" +
            "\t/addUser - добавление жителя в комнату\n" +
            "\t/removeUser - удаление жителя из комнаты\n" +
            "\t/deleteRoom - удаление комнаты\n" +
            "\t/joinRoom - присоединиться к существующей комнате\n" +
            "\t/inviteToRoom - создать приглашение к комнате";
    public static final String ROOM_CREATION = "Создание комнаты.\n" +
            "Введите название комнаты.\n" +
            "После создания комнаты вы сможете добавить туда участников командой /addUser";
    public static final String WELCOME_MESSAGE = "Добрый день. Для получения списка всех команд введите /help";
    public static final String CANCEL_MESSAGE = "Действие прервано.\n" +
            "Можете ввести другие комманды.";
    public static final String NAME_CHANGE = "Введите новое имя профиля.";
    public static final String ROOM_JOIN = "Для подключения к комнате введите id комнаты и токен через пробел.\n" +
            "В виде:\n" +
            "id токен";
    public static final String SUCCESS_JOIN = "Вы успешно вошли в комнату!";
    public static final String PURCHASE_CREATION = "Добавление покупки.\n" +
            "Введите название покупки и сумму.\n" +
            "В виде:\n" +
            "название сумма";
    public static final String PURCHASE_LIST_SUCCESS = "Участники успешно добавлены к покупке.";

}
