package ru.yandex.yakovlev.schedule.manager.manager.CSVformater;

import ru.yandex.yakovlev.schedule.manager.CSVTaskFormat;
import ru.yandex.yakovlev.schedule.manager.HistoryManager;
import ru.yandex.yakovlev.schedule.manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import ru.yandex.yakovlev.schedule.tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVTaskFormatTest {
    @Test
    public void taskToStringTest() {
        Task taskWithStartTimeAndDuration = new Task("taskWithStartTimeAndDuration","описание 1", Status.NEW);
        taskWithStartTimeAndDuration.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        taskWithStartTimeAndDuration.setDuration(60);
        taskWithStartTimeAndDuration.setId(1);

        Task taskWithStartTime = new Task("taskWithStartTime","описание 1", Status.NEW);
        taskWithStartTime.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        taskWithStartTime.setId(1);

        Task taskWithDuration = new Task("taskWithDuration","описание 1", Status.NEW);
        taskWithDuration.setDuration(60);
        taskWithDuration.setId(1);

        Task task = new Task("task","описание 1", Status.NEW);
        task.setId(1);

        String stringWithStartTimeAndDuration = CSVTaskFormat.taskToString(taskWithStartTimeAndDuration);
        String stringWithStartTime = CSVTaskFormat.taskToString(taskWithStartTime);
        String stringWithDuration = CSVTaskFormat.taskToString(taskWithDuration);
        String stringTask = CSVTaskFormat.taskToString(task);

        //id,type,name,status,description,epic,start time,duration
        String expectedWithStartTimeAndDuration = "1,TASK,taskWithStartTimeAndDuration," +
                "NEW,описание 1,null,2023-03-15T08:00,60";
        String expectedWithStartTime = "1,TASK,taskWithStartTime,NEW,описание 1,null,2023-03-15T08:00,0";
        String expectedWithDuration = "1,TASK,taskWithDuration,NEW,описание 1,null,null,60";
        String expectedTask = "1,TASK,task,NEW,описание 1,null,null,0";

        assertEquals(expectedWithStartTimeAndDuration, stringWithStartTimeAndDuration,
                "Строки с startTime и duration не равны");
        assertEquals(expectedWithStartTime, stringWithStartTime,
                "Строки с startTime не равны");
        assertEquals(expectedWithDuration, stringWithDuration,
                "Строки с duration не равны");
        assertEquals(expectedTask, stringTask,
                "Строки без startTime и Duration не равны");
    }

    @Test
    public void epicToStringTest() {
        Epic epic = new Epic("epic","описание эпика 1", Status.NEW);
        epic.setId(1);

        Epic epicWithStartTimeAndDuration = new Epic("epicWithStartTimeAndDuration",
                "описание эпика 1", Status.NEW);
        epicWithStartTimeAndDuration.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        epicWithStartTimeAndDuration.setDuration(60);
        epicWithStartTimeAndDuration.setId(1);

        Epic epicWithStartTime = new Epic("epicWithStartTime",
                "описание эпика 1", Status.NEW);
        epicWithStartTime.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        epicWithStartTime.setId(1);

        Epic epicWithAndDuration = new Epic("epicWithDuration",
                "описание эпика 1", Status.NEW);
        epicWithAndDuration.setDuration(60);
        epicWithAndDuration.setId(1);

        String epicString = CSVTaskFormat.taskToString(epic);
        String stringWithStartTimeAndDuration = CSVTaskFormat.taskToString(epicWithStartTimeAndDuration);
        String stringWithStartTime = CSVTaskFormat.taskToString(epicWithStartTime);
        String epicWithDuration = CSVTaskFormat.taskToString(epicWithAndDuration);

        String expectedString = "1,EPIC,epic,NEW,описание эпика 1,null,null,0";
        String expectedStringWithStartTimeAndDuration = "1,EPIC,epicWithStartTimeAndDuration," +
                "NEW,описание эпика 1,null,2023-03-15T08:00,60";
        String expectedStringWithStartTime = "1,EPIC,epicWithStartTime,NEW,описание эпика 1,null,2023-03-15T08:00,0";
        String expectedStringWithDuration = "1,EPIC,epicWithDuration,NEW,описание эпика 1,null,null,60";

        assertEquals(expectedString, epicString, "Строки без startTime и Duration не равны");
        assertEquals(expectedStringWithStartTimeAndDuration, stringWithStartTimeAndDuration,
                "Строки с startTime и Duration не равны");
        assertEquals(expectedStringWithStartTime, stringWithStartTime,
                "Строки c startTime не равны");
        assertEquals(expectedStringWithDuration, epicWithDuration,
                "Строки c Duration не равны");
    }

    @Test
    public void subTaskToStringTest() {
        SubTask subTask = new SubTask("subTask","описание сабтаска",
                1, Status.NEW);
        subTask.setId(2);

        SubTask subTaskWithStartTimeAndDuration = new SubTask("subTaskWithStartTimeAndDuration",
                "описание сабтаска",1, Status.NEW);
        subTaskWithStartTimeAndDuration.setId(2);
        subTaskWithStartTimeAndDuration.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        subTaskWithStartTimeAndDuration.setDuration(60);

        SubTask subTaskWithStartTime = new SubTask("subTaskWithStartTime",
                "описание сабтаска",1, Status.NEW);
        subTaskWithStartTime.setId(2);
        subTaskWithStartTime.setStartTime(LocalDateTime.of(2023,3,15,8, 0));

        SubTask subTaskWithDuration = new SubTask("subTaskWithDuration","описание сабтаска",
                1, Status.NEW);
        subTaskWithDuration.setId(2);
        subTaskWithDuration.setDuration(60);

        String subTaskString = CSVTaskFormat.taskToString(subTask);
        String stringWithStartTimeAndDuration = CSVTaskFormat.taskToString(subTaskWithStartTimeAndDuration);
        String stringWithStartTime = CSVTaskFormat.taskToString(subTaskWithStartTime);
        String stringWithDuration = CSVTaskFormat.taskToString(subTaskWithDuration);

        String expectedSubTask = "2,SUBTASK,subTask,NEW,описание сабтаска,1,null,0";
        String expectedWithStartTimeAndDuration = "2,SUBTASK,subTaskWithStartTimeAndDuration," +
                "NEW,описание сабтаска,1,2023-03-15T08:00,60";
        String expectedWithStartTime = "2,SUBTASK,subTaskWithStartTime,NEW,описание сабтаска,1,2023-03-15T08:00,0";
        String expectedWithDuration = "2,SUBTASK,subTaskWithDuration,NEW,описание сабтаска,1,null,60";

        assertEquals(expectedSubTask, subTaskString, "Строки без startTime и Duration не равны");
        assertEquals(expectedWithStartTimeAndDuration, stringWithStartTimeAndDuration,
                "Строки с startTime и Duration не равны");
        assertEquals(expectedWithStartTime, stringWithStartTime,
                "Строки c startTime не равны");
        assertEquals(expectedWithDuration, stringWithDuration,
                "Строки c Duration не равны");
    }

    @Test
    public void taskFromStringTest() {
        String WithStartTimeAndDuration = "1,TASK,taskWithStartTimeAndDuration," +
                "NEW,описание 1,null,2023-03-15T08:00,60";
        String WithStartTime = "1,TASK,taskWithStartTime,NEW,описание 1,null,2023-03-15T08:00,0";
        String WithDuration = "1,TASK,taskWithDuration,NEW,описание 1,null,null,60";
        String stringTask = "1,TASK,task,NEW,описание 1,null,null,0";

        Task expectedWithStartTimeAndDuration = new Task("taskWithStartTimeAndDuration","описание 1", Status.NEW);
        expectedWithStartTimeAndDuration.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        expectedWithStartTimeAndDuration.setDuration(60);
        expectedWithStartTimeAndDuration.setId(1);

        Task expectedWithStartTime = new Task("taskWithStartTime","описание 1", Status.NEW);
        expectedWithStartTime.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        expectedWithStartTime.setId(1);

        Task expectedWithDuration = new Task("taskWithDuration","описание 1", Status.NEW);
        expectedWithDuration.setDuration(60);
        expectedWithDuration.setId(1);

        Task expectedTask = new Task("task","описание 1", Status.NEW);
        expectedTask.setId(1);

        Task task = CSVTaskFormat.taskFromString(stringTask);
        Task taskWithStartTimeAndDuration = CSVTaskFormat.taskFromString(WithStartTimeAndDuration);
        Task taskWithStartTime = CSVTaskFormat.taskFromString(WithStartTime);
        Task taskWithDuration = CSVTaskFormat.taskFromString(WithDuration);

        assertEquals(expectedTask,task, "Таски без StartTime и Duration не равны");
        assertEquals(expectedWithStartTimeAndDuration,taskWithStartTimeAndDuration,
                "Таски с StartTime и Duration не равны");
        assertEquals(expectedWithStartTime,taskWithStartTime,
                "Таски с StartTime не равны");
        assertEquals(expectedWithDuration,taskWithDuration,
                "Таски с Duration не равны");
    }

    @Test
    public void epicFromStringTest() {
        String epicString = "1,EPIC,epic,NEW,описание эпика 1,null,null,0";
        String StringWithStartTimeAndDuration = "1,EPIC,epicWithStartTimeAndDuration," +
                "NEW,описание эпика 1,null,2023-03-15T08:00,60";
        String StringWithStartTime = "1,EPIC,epicWithStartTime,NEW,описание эпика 1,null,2023-03-15T08:00,0";
        String StringWithDuration = "1,EPIC,epicWithDuration,NEW,описание эпика 1,null,null,60";

        Epic expectedEpic = new Epic("epic","описание эпика 1", Status.NEW);
        expectedEpic.setId(1);

        Epic expectedWithStartTimeAndDuration = new Epic("epicWithStartTimeAndDuration",
                "описание эпика 1", Status.NEW);
        expectedWithStartTimeAndDuration.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        expectedWithStartTimeAndDuration.setDuration(60);
        expectedWithStartTimeAndDuration.setId(1);

        Epic expectedWithStartTime = new Epic("epicWithStartTime",
                "описание эпика 1", Status.NEW);
        expectedWithStartTime.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        expectedWithStartTime.setId(1);

        Epic expectedWithDuration = new Epic("epicWithDuration",
                "описание эпика 1", Status.NEW);
        expectedWithDuration.setDuration(60);
        expectedWithDuration.setId(1);

        Epic epic = (Epic) CSVTaskFormat.taskFromString(epicString);
        Epic epicWithStartTimeAndDuration = (Epic) CSVTaskFormat.taskFromString(StringWithStartTimeAndDuration);
        Epic epicWithStartTime = (Epic) CSVTaskFormat.taskFromString(StringWithStartTime);
        Epic epicWithDuration =  (Epic) CSVTaskFormat.taskFromString(StringWithDuration);

        assertEquals(expectedEpic, epic, "Эпики без StartTime и Duration не равны");
        assertEquals(expectedWithStartTimeAndDuration, epicWithStartTimeAndDuration,
                "Эпики с StartTime и Duration не равны");
        assertEquals(expectedWithStartTime, epicWithStartTime,
                "Эпики с StartTime не равны");
        assertEquals(expectedWithDuration, epicWithDuration,
                "Эпики с Duration не равны");
    }

    @Test
    public void subTaskFromStringTest() {
        String stringSubTask = "2,SUBTASK,subTask,NEW,описание сабтаска,1,null,0";
        String stringWithStartTimeAndDuration = "2,SUBTASK,subTaskWithStartTimeAndDuration," +
                "NEW,описание сабтаска,1,2023-03-15T08:00,60";
        String stringWithStartTime = "2,SUBTASK,subTaskWithStartTime,NEW,описание сабтаска,1,2023-03-15T08:00,0";
        String stringWithDuration = "2,SUBTASK,subTaskWithDuration,NEW,описание сабтаска,1,null,60";

        SubTask expectedSubTask = new SubTask("subTask","описание сабтаска",
                1, Status.NEW);
        expectedSubTask.setId(2);

        SubTask expectedWithStartTimeAndDuration = new SubTask("subTaskWithStartTimeAndDuration",
                "описание сабтаска",1, Status.NEW);
        expectedWithStartTimeAndDuration.setId(2);
        expectedWithStartTimeAndDuration.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        expectedWithStartTimeAndDuration.setDuration(60);

        SubTask expectedWithStartTime = new SubTask("subTaskWithStartTime",
                "описание сабтаска",1, Status.NEW);
        expectedWithStartTime.setId(2);
        expectedWithStartTime.setStartTime(LocalDateTime.of(2023,3,15,8, 0));

        SubTask expectedWithDuration = new SubTask("subTaskWithDuration","описание сабтаска",
                1, Status.NEW);
        expectedWithDuration.setId(2);
        expectedWithDuration.setDuration(60);

        SubTask subtask = (SubTask) CSVTaskFormat.taskFromString(stringSubTask);
        SubTask subTaskWithStartTimeAndDuration = (SubTask) CSVTaskFormat.taskFromString(stringWithStartTimeAndDuration);
        SubTask subTaskWithStartTime = (SubTask) CSVTaskFormat.taskFromString(stringWithStartTime);
        SubTask subTaskWithDuration = (SubTask) CSVTaskFormat.taskFromString(stringWithDuration);

        assertEquals(expectedSubTask, subtask, "Сабтаски без StartTime и Duration не равны");
        assertEquals(expectedWithStartTimeAndDuration, subTaskWithStartTimeAndDuration,
                "Сабтаски с StartTime и Duration не равны");
        assertEquals(expectedWithStartTime, subTaskWithStartTime,
                "Сабтаски с StartTime не равны");
        assertEquals(expectedWithDuration, subTaskWithDuration,
                "Сабтаски с Duration не равны");
    }

    @Test
    public void historyToStringTest() {
        Task task1 = new Task("задача 1","описание 1", Status.NEW);
        task1.setId(1);

        Task task2 = new Task("задача 2","описание 2", Status.NEW);
        task2.setId(2);

        Epic epic1 = new Epic("эпик 1","описание эпика 1", Status.NEW);
        epic1.setId(3);

        SubTask subTask1 = new SubTask("Сабтаск 1","описание сабтаска 1",
                epic1.getId(), Status.NEW);
        subTask1.setId(4);

        SubTask subTask2 = new SubTask("Сабтаск 2","описание сабтаска 2",
                epic1.getId(), Status.NEW);
        subTask2.setId(5);

        String expectedHistory = "2,3,4,5,1";

        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(task1);

        String savedHistory = CSVTaskFormat.historyToString(historyManager);

        assertEquals(expectedHistory, savedHistory, "Истории не равны");
    }

    @Test
    public void historyFromString() {
        List<Integer> expectedHistory = new ArrayList<>();
        expectedHistory.add(2);
        expectedHistory.add(3);
        expectedHistory.add(4);
        expectedHistory.add(5);
        expectedHistory.add(1);

        String historyString = "2,3,4,5,1";
        List<Integer> restoredHistory = CSVTaskFormat.historyFromString(historyString);

        assertEquals(expectedHistory, restoredHistory, "Восстановленная история не равна ожидаемой");
    }

}
