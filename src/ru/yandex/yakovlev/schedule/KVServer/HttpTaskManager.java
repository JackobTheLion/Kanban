package ru.yandex.yakovlev.schedule.KVServer;

import com.google.gson.Gson;
import ru.yandex.yakovlev.schedule.manager.FileBackedTasksManager;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpTaskManager extends FileBackedTasksManager {

    KVClient kvClient;
    Gson gson = new Gson();

    public HttpTaskManager(String path) throws URISyntaxException, IOException, InterruptedException {
        super(path);
        kvClient = new KVClient();
    }

    public void save() {
        
    }
}
