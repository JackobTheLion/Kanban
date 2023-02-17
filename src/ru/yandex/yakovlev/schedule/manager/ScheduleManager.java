package manager;

import manager.exceptions.ScheduleBookingException;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class ScheduleManager {

    protected final static int SLOT_DURATION = 15; //в минутах
    private final static int MAX_PLANNING_YEARS = 1;
    private final static int DAYS_IN_YEAR = 365;
    private final static int MINUTES_IN_DAY = 24 * 60; //24 часа * 60 минут
    private final static int NUMBER_OF_SLOTS = (MINUTES_IN_DAY / SLOT_DURATION) * DAYS_IN_YEAR * MAX_PLANNING_YEARS;
    private final ArrayList<TimeSlot> schedule = new ArrayList<>(NUMBER_OF_SLOTS);
    private final LocalDateTime scheduleStart;

    public ScheduleManager(LocalDateTime scheduleStart) {
        this.scheduleStart = scheduleStart;
        schedule.add(new TimeSlot(scheduleStart));

        for (int i = 1; i <= NUMBER_OF_SLOTS; i++) {
            TimeSlot previousSlot = schedule.get(i-1);
            schedule.add(new TimeSlot(previousSlot.endTime));
        }
    }

    public ArrayList<TimeSlot> getSchedule() {
        return schedule;
    }

    public boolean bookSchedule(Task task) throws ScheduleBookingException {
        if (task == null) {
            throw new ScheduleBookingException("Can't book null task");
        }

        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();

        if (startTime == null || duration.toMinutes() == 0) {
            return false;
        }
        if (startTime.isBefore(scheduleStart)) {
            throw new ScheduleBookingException("Can't book ID " + task.getId() + " at " + startTime + "with duration "
                    + duration + ". StartTime is before scheduleStartTime");
        }

        if (!checkAvailability(task)) {
            throw new ScheduleBookingException("Can't book ID " + task.getId() + " at " + startTime + "with duration "
                    + duration + ". Slot is busy");
        }

        Duration difference = Duration.between(scheduleStart, startTime);
        long firstSlotNumber = difference.toMinutes() / SLOT_DURATION;
        long numberOfSlots = duration.toMinutes() / SLOT_DURATION;

        if ((duration.toMinutes() % SLOT_DURATION) > 0) {
            numberOfSlots++;
        }

        long lastSlotNumber = firstSlotNumber + numberOfSlots;

        for (long i = firstSlotNumber; i < lastSlotNumber; i++) {
            schedule.get((int) i).isAvailable = false;
        }
        return true;
    }

    public void updateBooking(Task oldTask, Task updatedTask) {
        if (oldTask == null) {
            throw new ScheduleBookingException("OldTask is null");
        }

        if (updatedTask == null) {
            throw new ScheduleBookingException("UpdatedTask is null");
        }

        unBookSchedule(oldTask);
        if (!bookSchedule(updatedTask)) bookSchedule(oldTask);
    }

    public void unBookSchedule(Task task) {
        if (task == null) {
            throw new ScheduleBookingException("Can't unBook null task");
        }

        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();

        if (startTime == null || duration == null) {
            return;
        }

        Duration difference = Duration.between(scheduleStart, startTime);
        long firstSlotNumber = difference.toMinutes() / SLOT_DURATION;
        long numberOfSlots = duration.toMinutes() / SLOT_DURATION;

        if ((duration.toMinutes() % SLOT_DURATION) > 0) {
            numberOfSlots++;
        }

        long lastSlotNumber = firstSlotNumber + numberOfSlots;

        for (long i = firstSlotNumber; i < lastSlotNumber; i++) {
            schedule.get((int) i).isAvailable = true;
        }
    }

    public boolean checkAvailability(Task task) {
        if (task == null) {
            throw new ScheduleBookingException("Can't check null task");
        }

        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();

        Duration difference = Duration.between(scheduleStart, startTime);
        long firstSlotNumber = difference.toMinutes() / SLOT_DURATION;
        long numberOfSlots = duration.toMinutes() / SLOT_DURATION;
        long lastSlotNumber = firstSlotNumber + numberOfSlots;

        for (long i = firstSlotNumber; i < lastSlotNumber; i++) {
            if (!schedule.get((int) i).isAvailable) {
                return false;
            }
        }
        return true;
    }

    public void unbookAll() {
        for (TimeSlot timeSlot : schedule) {
            timeSlot.isAvailable = true;
        }
    }

    public static class TimeSlot {
        LocalDateTime startTime;
        LocalDateTime endTime;
        public boolean isAvailable = true;

        TimeSlot(LocalDateTime startTime) {
            this.startTime = startTime;
            endTime = startTime.plusMinutes(ScheduleManager.SLOT_DURATION);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeSlot timeSlot = (TimeSlot) o;
            return isAvailable == timeSlot.isAvailable
                    && startTime.equals(timeSlot.startTime)
                    && endTime.equals(timeSlot.endTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startTime, endTime, isAvailable);
        }

        @Override
        public String toString() {
            return "TimeSlot{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", isAvailable=" + isAvailable +
                    '}';
        }
    }
}


