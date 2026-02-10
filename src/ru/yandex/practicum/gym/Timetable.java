package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (!timetable.containsKey(trainingSession.getDayOfWeek())) {
            timetable.put(trainingSession.getDayOfWeek(), new TreeMap<>());
        }

        TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = timetable.get(trainingSession.getDayOfWeek());

        if (!dayTimetable.containsKey(trainingSession.getTimeOfDay())) {
            dayTimetable.put(trainingSession.getTimeOfDay(), new ArrayList<>());
        }

        List<TrainingSession> sessionsAtTime = dayTimetable.get(trainingSession.getTimeOfDay());

        if (sessionsAtTime.contains(trainingSession)) {
            System.out.println("Занятие уже есть в расписании");
        } else {
            sessionsAtTime.add(trainingSession);
        }
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.containsKey(dayOfWeek)) {
            return timetable.get(dayOfWeek);
        } else {
            System.out.println("Занятий в " + dayOfWeek + " нет");
            return null;
        }
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = timetable.get(dayOfWeek);

        if (dayTimetable != null) {
            List<TrainingSession> sessionsAtTime = dayTimetable.get(timeOfDay);
            if (sessionsAtTime != null) {
                return sessionsAtTime;
            } else {
                System.out.println("Занятий в " + timeOfDay + " нет");
                return null;
            }
        } else {
            System.out.println("Занятий в " + dayOfWeek + " нет");
            return null;
        }
    }

    public HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> getTimetable() {
        return timetable;
    }

    public HashMap<Coach, Integer> getCountByCoaches() {
        //Создаём таблицу тренеров с количеством тренировок
        HashMap<Coach, Integer> coachesCount = new HashMap<>();
        //Перебираем дни
        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> entry : timetable.entrySet()) {
            TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = entry.getValue();
            //перебираем время
            for (Map.Entry<TimeOfDay, List<TrainingSession>> timeEntry : sessionsForDay.entrySet()) {
                List<TrainingSession> sessions = timeEntry.getValue();
                //перебираем занятия
                for (TrainingSession session : sessions) {
                    //считаем уникальных тренеров и их тренировки
                    //добавляем нового тренера на этом занятии, либо добавляем 1 в значение к уже существующему
                    coachesCount.merge(session.getCoach(), 1, Integer::sum);
                }
            }
        }
        return coachesCount;
    }

    public List<Map.Entry<Coach, Integer>> getCountByCoachesSorted() {
        HashMap<Coach, Integer> coachesCount = getCountByCoaches();
        return coachesCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((a, b) -> b.compareTo(a)))
                .toList();
    }

    public List<String> formatCountByCoaches(List<Map.Entry<Coach, Integer>> sortedCoaches) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : sortedCoaches) {
            String coachName = entry.getKey().getFullName();
            int trainingCount = entry.getValue();
            result.add(coachName + " - " + trainingCount + " тренировок");
        }
        return result;
    }

    public void printTimeTable() {
        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> entry : timetable.entrySet()) {
            DayOfWeek day = entry.getKey();
            TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = entry.getValue();

            System.out.println("День недели: " + day);

            for (Map.Entry<TimeOfDay, List<TrainingSession>> timeEntry : sessionsForDay.entrySet()) {
                TimeOfDay time = timeEntry.getKey();
                List<TrainingSession> sessions = timeEntry.getValue();

                System.out.println("  Время: " + time);

                for (TrainingSession session : sessions) {
                    System.out.println("    Группа: " + session.getGroup().getTitle());
                    System.out.println("    Длительность минут: " + session.getGroup().getDuration());
                    System.out.println("    Тренер: " + session.getCoach().getFullName() + "\n");
                }
            }
        }
    }

}
