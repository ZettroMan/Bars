package ru.zettro.bars.model;

import java.time.*;

public class Contract {
    private Long id;
    private LocalDate date;
    private String number;
    private String title;
    private LocalDate created;
    private LocalDate modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return modified;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActual() {
        if (modified == null) {
            return false;
        }
        LocalDateTime startDate = LocalDateTime.of(modified, LocalTime.now());
        LocalDateTime endDate = LocalDateTime.now();
        return Duration.between(startDate, endDate).toDays() <= 60L;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", date=" + date +
                ", number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", isActual=" + isActual() +
                '}';
    }
}

