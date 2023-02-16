package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    private static final TaskType type = TaskType.TASK;
    protected LocalDateTime startTime;
    protected Duration duration = Duration.ofMinutes(0); //продолжительность в минутах
    protected LocalDateTime endTime;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        if (status == Status.NEW || status == Status.IN_PROGRESS || status == Status.DONE) {
            this.status = status;
        } else {
            System.out.println("Ошибка присвоения статуса. Неверный status");
        }
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(int minutes) {
        this.duration = Duration.ofMinutes(minutes);
        setEndTime();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        setEndTime();
    }

    protected void setEndTime() {
        if (duration != null && startTime != null) endTime = startTime.plusMinutes(duration.toMinutes());
    }

    protected LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start time='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                "}" + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration)
                && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration, endTime);
    }
}
