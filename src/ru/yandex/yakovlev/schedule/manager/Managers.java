package ru.yandex.yakovlev.schedule.manager;

import java.io.File;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager("resources/backup.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static ScheduleManager getDefaultScheduleManager() {
        return new ScheduleManager(LocalDateTime.of(2023, 2, 15, 12,0));
    }
}
