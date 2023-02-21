package ru.yandex.yakovlev.schedule.manager.manager.taskManager.inMemory;

import ru.yandex.yakovlev.schedule.manager.InMemoryTaskManager;
import ru.yandex.yakovlev.schedule.manager.manager.taskManager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }
}
