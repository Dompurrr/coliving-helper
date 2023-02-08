package org.dompurrr.utils;

import org.springframework.stereotype.Component;

@Component
public class ErrorTemplates {
    public static final String WRONG_INPUT = "Неподдерживаемая комманда!\n" +
            "Отправьте /help для получения списка всех комманд.";
    public static final String BAD_ROOM_NAME = "Неверное название комнаты.\n" +
            "Название должно быть длинной от 2 до 19 символов и может " +
            "содержать буквы кириллицы, латиницы, цифры и точку.";
    public static final String BAD_NAME = "Недопустимое имя!\n" +
            "Разрешены символы кириллицы и латиницы, а так же точка.\n" +
            "Минимальная длина - 2 символа, максимальная - 20";
    public static final String BAD_NUMBER = "Недопустимое число!\n" +
            "Разрешенно использовать только арабские цифры.";
    public static final String UNDEFINED_PROBLEM = "Внутренняя ошибка.\n" +
            "Повторите попытку.";
    public static final String NO_ROOM = "У вас отсутствует какая-либо комната.\n" +
            "Вы можете создать новую командой /room";
    public static final String RESIDENT_NOT_FOUND = "Пользователь не найден.\n" +
            "Попробуйте повторить ввод.";
    public static final String RESIDENT_NOT_IN_ROOM = "Пользователь находится не в вашей комнате.";
    public static final String MAX_RESIDENTS_NUM = "Достигнут лимит участников, 19 человек.";
    public static final String HAS_ROOM = "У вас уже имеется комната.";
    public static final String BAD_DATA = "Введенные данные имеют неверный формат, попробуйте еще раз.";
    public static final String ROOM_NOT_FOUND = "Комната с данным id не найдена или токен не верный.";
    public static final String ROOM_IS_FULL = "Комната заполнена, попросите участника добавить нового сожителя.";
    public static final String BAD_PURCHASE_NAME = "Неверно задана покупка.\n" +
            "Входная строка должна быть из 2 частей разделенных пробелом\n" +
            "Первая - название покупки в виде строки размером от 2 до 20 элементов из латинских и кириллических символов\n" +
            "Вторая - сумма покупки, максимальное значени 999999.";
    public static final String BAD_PURCHASE_LIST = "Неверный ввод списка участников покупки.\n" +
            "Необходимо перечислить числа через запятую с пробелом.\n" +
            "Пример:\n" +
            "1, 4, 6";
    public static final String BAD_PURCHASE_LIST_ID = "В списке один из id не верный или не проживает в вашей комнате.";
}
