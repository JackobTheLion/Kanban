package manager.scheduleManager;

import manager.ScheduleManager;
import manager.exceptions.ScheduleBookingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yakovlev.schedule.tasks.Status;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleManagerTest {
    ScheduleManager scheduleManager;
    LocalDateTime scheduleStart = LocalDateTime.of(2023,3, 15, 12, 0);
    List<ScheduleManager.TimeSlot> emptySchedule;

    @BeforeEach
    public void beforeEach() {
        scheduleManager = new ScheduleManager(scheduleStart);
        emptySchedule = scheduleManager.getSchedule();
    }

    @Test
    public void bookScheduleTest() {
        Task task = new Task("задача 1","описание 1", Status.NEW);
        task.setStartTime(LocalDateTime.of(2023,3,15,12, 15));
        task.setDuration(60); //4 слота по 15 минут
        scheduleManager.bookSchedule(task);

        List<ScheduleManager.TimeSlot> savedSchedule = scheduleManager.getSchedule();

        for (long i = 1; i < 6; i++) { //1 слот пустой, 4 на таск
            emptySchedule.get((int) i).isAvailable = false;
        }

        for (int i = 1; i < 6; i++) { //1 слот пустой, 4 на таск
            ScheduleManager.TimeSlot savedSlot = savedSchedule.get(i);

            assertFalse(savedSlot.isAvailable,"Слот " + i + " не забронирован");
        }

        assertTrue(savedSchedule.get(0).isAvailable, "Слот 0 забронирован");

        for (int i = 6; i < savedSchedule.size(); i++) {
            ScheduleManager.TimeSlot savedSlot = savedSchedule.get(i);

            assertTrue(savedSlot.isAvailable,"Слот " + i + " забронирован");
        }
    }

    @Test
    public void bookScheduleWithNullTest() {
        Task task = null;
        final ScheduleBookingException exception = assertThrows(
                ScheduleBookingException.class,
                () -> scheduleManager.bookSchedule(task)
        );
        assertEquals("Can't book null task", exception.getMessage());
    }

    @Test
    public void bookScheduleWithNoDurationOrStartTimeTest() {
        Task noDurationTask = new Task("задача 1","описание 1", Status.NEW);
        noDurationTask.setStartTime(LocalDateTime.of(2023,3,15,12, 15));
        assertFalse(scheduleManager.bookSchedule(noDurationTask),"Задача без duration забронирована");

        Task noStartTimeTask = new Task("задача 2","описание 2", Status.NEW);
        noStartTimeTask.setDuration(60);
        assertFalse(scheduleManager.bookSchedule(noStartTimeTask),"Задача без StartTime забронирована");
    }

    @Test
    public void bookScheduleWithStartTimeBeforeScheduleStartTest() {
        Task task = new Task("задача 1","описание 1", Status.NEW);
        task.setStartTime(LocalDateTime.of(1970,3,15,12, 15));
        task.setDuration(60); //4 слота по 15 минут
        final ScheduleBookingException exception = assertThrows(
                ScheduleBookingException.class,
                () -> scheduleManager.bookSchedule(task)
        );
        assertEquals("Can't book ID " + task.getId() + " at " + task.getStartTime() + "with duration "
                + task.getDuration() + ". StartTime is before scheduleStartTime", exception.getMessage());
    }

    @Test
    public void bookScheduleWhenSlotIsBookedTest() {
        Task task = new Task("задача 1","описание 1", Status.NEW);
        task.setStartTime(LocalDateTime.of(2023,3,15,12, 15));
        task.setDuration(60); //4 слота по 15 минут
        scheduleManager.bookSchedule(task);

        Task task2 = new Task("задача 2","описание 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2023,3,15,12, 45));
        task2.setDuration(60); //4 слота по 15 минут

        final ScheduleBookingException exception = assertThrows(
                ScheduleBookingException.class,
                () -> scheduleManager.bookSchedule(task2)
        );
        assertEquals("Can't book ID " + task2.getId() + " at " + task2.getStartTime() + "with duration "
                + task2.getDuration() + ". Slot is busy", exception.getMessage());
    }

    @Test
    public void updateBookingIfTaskIsNull() {
        Task oldTaskNull = null;
        Task updatedTaskNull = null;

        Task oldTaskNotNull = new Task("задача 1","описание 1", Status.NEW);
        oldTaskNotNull.setStartTime(LocalDateTime.of(2023,3,15,12, 15));
        oldTaskNotNull.setDuration(60); //4 слота по 15 минут

        Task updatedTaskNotNull = new Task("задача 1","описание 1", Status.NEW);
        updatedTaskNotNull.setStartTime(LocalDateTime.of(2023,3,15,12, 15));
        updatedTaskNotNull.setDuration(60); //4 слота по 15 минут

        final ScheduleBookingException exception1 = assertThrows(
                ScheduleBookingException.class,
                () -> scheduleManager.updateBooking(oldTaskNull, updatedTaskNotNull)
        );
        assertEquals("OldTask is null", exception1.getMessage());

        final ScheduleBookingException exception2 = assertThrows(
                ScheduleBookingException.class,
                () -> scheduleManager.updateBooking(oldTaskNotNull, updatedTaskNull)
        );
        assertEquals("UpdatedTask is null", exception2.getMessage());
    }

    @Test
    public void updateBookingTest() {
        Task oldTask = new Task("задача 1","описание 1", Status.NEW);
        oldTask.setStartTime(LocalDateTime.of(2023,3,15,12, 15)); //первый слот 1
        oldTask.setDuration(60); //4 слота по 15 минут

        Task updatedTask = new Task("задача 1","описание 1", Status.NEW);
        updatedTask.setStartTime(LocalDateTime.of(2023,3,15,12, 45));//первый слот 3
        updatedTask.setDuration(60); //4 слота по 15 минут

        scheduleManager.bookSchedule(oldTask);
        scheduleManager.updateBooking(oldTask, updatedTask);

        List<ScheduleManager.TimeSlot> savedSchedule = scheduleManager.getSchedule();

        for (int i = 0; i < 3; i++) {
            assertTrue(savedSchedule.get(i).isAvailable, "Слот " + i + " забронирован");
        }

        for (int i = 3; i < 7; i++) {
            assertFalse(savedSchedule.get(i).isAvailable, "Слот " + i + " не забронирован");
        }

        for (int i = 7; i < savedSchedule.size(); i++) {
            assertTrue(savedSchedule.get(i).isAvailable, "Слот " + i + " забронирован");
        }
    }

    @Test
    public void unbookScheduleNullTest() {
        Task task = null;
        final ScheduleBookingException exception = assertThrows(
                ScheduleBookingException.class,
                () -> scheduleManager.unBookSchedule(task)
        );
        assertEquals("Can't unBook null task", exception.getMessage());
    }

    @Test
    public void unbookScheduleTest() {
        Task task = new Task("задача 1","описание 1", Status.NEW);
        task.setStartTime(LocalDateTime.of(2023,3,15,12, 15)); //первый слот 1
        task.setDuration(60); //4 слота по 15 минут

        scheduleManager.bookSchedule(task);
        scheduleManager.unBookSchedule(task);

        List<ScheduleManager.TimeSlot> savedSchedule = scheduleManager.getSchedule();

        for (int i = 0; i < savedSchedule.size(); i++) {
            assertTrue(savedSchedule.get(i).isAvailable, "Слот " + i + " забронирован");
        }
    }

    @Test
    public void unbookAllTest() {
        Task task = new Task("задача 1","описание 1", Status.NEW);
        task.setStartTime(LocalDateTime.of(2023,3,15,12, 15)); //первый слот 1
        task.setDuration(60); //4 слота по 15 минут

        scheduleManager.bookSchedule(task);
        scheduleManager.unbookAll();

        List<ScheduleManager.TimeSlot> savedSchedule = scheduleManager.getSchedule();

        for (int i = 0; i < savedSchedule.size(); i++) {
            assertTrue(savedSchedule.get(i).isAvailable, "Слот " + i + " забронирован");
        }
    }

    @Test
    public void checkAvailabilityNullTest() {
        Task task = null;
        final ScheduleBookingException exception = assertThrows(
                ScheduleBookingException.class,
                () -> scheduleManager.checkAvailability(task)
        );
        assertEquals("Can't check null task", exception.getMessage());
    }

    @Test
    public void checkAvailabilityTest() {
        Task task = new Task("задача 1","описание 1", Status.NEW);
        task.setStartTime(LocalDateTime.of(2023,3,15,12, 15)); //первый слот 1
        task.setDuration(60); //4 слота по 15 минут

        assertTrue(scheduleManager.checkAvailability(task), "Расписание забронировано");

        scheduleManager.bookSchedule(task);

        Task task2 = new Task("задача 1","описание 1", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2023,3,15,12, 45)); //первый слот 1
        task2.setDuration(60); //4 слота по 15 минут

        assertFalse(scheduleManager.checkAvailability(task2), "Расписание не забронировано");
    }
}
