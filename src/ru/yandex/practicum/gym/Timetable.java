package ru.yandex.practicum.gym;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    private HashMap<Coach, Integer> coachesCounter = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (!timetable.containsKey(trainingSession.getDayOfWeek())) {
            timetable.put(trainingSession.getDayOfWeek(), new TreeMap<>());
        }

        TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = timetable.get(trainingSession.getDayOfWeek());

        if (!dayTimetable.containsKey(trainingSession.getTimeOfDay())) {
            dayTimetable.put(trainingSession.getTimeOfDay(), new ArrayList<>());
        }

        List<TrainingSession> sessionsAtTime = dayTimetable.get(trainingSession.getTimeOfDay());

        if (!sessionsAtTime.contains(trainingSession)) {
            sessionsAtTime.add(trainingSession);

            Coach coach = trainingSession.getCoach();
            coachesCounter.put(coach, coachesCounter.getOrDefault(coach, 0) + 1);
        }
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> trainingsForDay = timetable.get(dayOfWeek);
        if (trainingsForDay == null) {
            return new TreeMap<>();
        }
        return trainingsForDay;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = timetable.get(dayOfWeek);

        if (dayTimetable != null) {
            List<TrainingSession> sessionsAtTime = dayTimetable.get(timeOfDay);
            if (sessionsAtTime != null) {
                return sessionsAtTime;
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> getTimetable() {
        return timetable;
    }

    public HashMap<Coach, Integer> getCountByCoaches() {
        //Возвращаем таблицу тренеров с количеством тренировок
        return coachesCounter;
    }

    public List<Map.Entry<Coach, Integer>> getCountByCoachesSorted() {
        return coachesCounter.entrySet().stream()
                .sorted(Map.Entry.<Coach, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    public List<String> formatCountByCoaches() {
        List<Map.Entry<Coach, Integer>> sortedCoaches = getCountByCoachesSorted();
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
