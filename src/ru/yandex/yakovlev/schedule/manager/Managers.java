package ru.yandex.yakovlev.schedule.manager;

import ru.yandex.yakovlev.schedule.http.HttpTaskManager;
import ru.yandex.yakovlev.schedule.http.KVServer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() throws URISyntaxException, IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:", KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static ScheduleManager getDefaultScheduleManager() {
        return new ScheduleManager(LocalDateTime.of(2023, 2, 15, 12,0));
    }

}
