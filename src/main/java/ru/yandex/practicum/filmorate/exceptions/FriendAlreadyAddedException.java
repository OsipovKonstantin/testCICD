package ru.yandex.practicum.filmorate.exceptions;

public class FriendAlreadyAddedException extends RuntimeException {
    public FriendAlreadyAddedException(String message) {
        super(message);
    }
}
