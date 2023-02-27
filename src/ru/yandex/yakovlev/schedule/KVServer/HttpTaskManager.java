package ru.yandex.yakovlev.schedule.KVServer;

import ru.yandex.yakovlev.schedule.manager.FileBackedTasksManager;

import java.io.File;

public class HttpTaskManager extends FileBackedTasksManager {

    public HttpTaskManager(String path) {
        super(path);
    }
}
