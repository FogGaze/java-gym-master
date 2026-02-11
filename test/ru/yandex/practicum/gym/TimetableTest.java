package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());

        //Проверить, что за вторник вернулась пустая TreeMap
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertEquals(0, tuesdaySessions.size());

        //Вывод в консоль расписания
        timetable.printTimeTable();
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertEquals(0, thursdaySessions.firstEntry().getKey().compareTo(thursdayChildTrainingSession.getTimeOfDay()));
        Assertions.assertEquals(0, thursdaySessions.lastEntry().getKey().compareTo(thursdayAdultTrainingSession.getTimeOfDay()));

        // Проверить, что за вторник вернулась пустая TreeMap
        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertEquals(0, tuesdaySessions.size());

        //Вывод в консоль расписания
        timetable.printTimeTable();
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, mondaySessions.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> noSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertEquals(0, noSessions.size());

        //Вывод в консоль расписания
        timetable.printTimeTable();
    }

    @Test
    void testAddTrainingSessionsDuplicate() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);
        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что в понедельник одно занятие, дубликат не записался
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());

        //Вывод в консоль расписания
        timetable.printTimeTable();
    }

    @Test
    void testAddDifferentTrainingSessionsSameTimeAndDay() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 60);
        Coach coachOne = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachTwo = new Coach("Петров", "Василий", "Сергеевич");
        TimeOfDay timeSession = new TimeOfDay(13, 0);

        TrainingSession trainingSessionChildOne = new TrainingSession(group, coachOne,
                DayOfWeek.MONDAY, timeSession);
        TrainingSession trainingSessionChildTwo = new TrainingSession(group, coachTwo,
                DayOfWeek.MONDAY, timeSession);
        TrainingSession trainingSessionAdult = new TrainingSession(groupAdult, coachOne,
                DayOfWeek.MONDAY, timeSession);

        timetable.addNewTrainingSession(trainingSessionChildOne);
        timetable.addNewTrainingSession(trainingSessionChildTwo);
        timetable.addNewTrainingSession(trainingSessionAdult);

        //Проверить, что в понедельник три занятия, в 13:00
        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> mondaySessionsList = mondaySessions.get(timeSession);
        Assertions.assertEquals(3, mondaySessionsList.size());
        Assertions.assertEquals(0, mondaySessions.firstEntry().getKey().compareTo(timeSession));
        Assertions.assertEquals(0, mondaySessions.lastEntry().getKey().compareTo(timeSession));

        //Вывод в консоль расписания
        timetable.printTimeTable();
    }

    @Test
    void testAddTrainingSessionsFullWeek() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TimeOfDay timeSession = new TimeOfDay(13, 0);

        for (int i = 0; i < DayOfWeek.values().length; i++) {
            TrainingSession trainingSession = new TrainingSession(group, coach, DayOfWeek.values()[i], timeSession);
            timetable.addNewTrainingSession(trainingSession);
        }
        //Проверяем что занятия есть во всех днях недели
        Assertions.assertEquals(7, timetable.getTimetable().size());

        //Вывод в консоль расписания
        timetable.printTimeTable();
    }

    @Test
    void testCalculateCoach() {
        Timetable timetable = new Timetable();

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 60);
        Coach coachOne = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachTwo = new Coach("Петров", "Василий", "Сергеевич");
        Coach coachThree = new Coach("Иванов", "Михаил", "Алексеевич");
        Coach coachFour = new Coach("Иванов", "Андрей", "Алексеевич");
        TimeOfDay timeTen = new TimeOfDay(10, 0);
        TimeOfDay timeTwelve = new TimeOfDay(12, 0);

        TrainingSession trainingSessionChildMonday = new TrainingSession(groupChild, coachOne,
                DayOfWeek.MONDAY, timeTen);
        TrainingSession trainingSessionAdultMonday = new TrainingSession(groupAdult, coachFour,
                DayOfWeek.MONDAY, timeTen);
        TrainingSession trainingSessionChildWednesday = new TrainingSession(groupChild, coachOne,
                DayOfWeek.WEDNESDAY, timeTen);
        TrainingSession trainingSessionAdultWednesday = new TrainingSession(groupAdult, coachThree,
                DayOfWeek.WEDNESDAY, timeTen);
        TrainingSession trainingSessionChildFriday = new TrainingSession(groupChild, coachOne,
                DayOfWeek.FRIDAY, timeTen);
        TrainingSession trainingSessionAdultTuesday = new TrainingSession(groupAdult, coachTwo,
                DayOfWeek.TUESDAY, timeTwelve);
        TrainingSession trainingSessionAdultThursday = new TrainingSession(groupAdult, coachTwo,
                DayOfWeek.THURSDAY, timeTwelve);
        TrainingSession trainingSessionAdultSaturday = new TrainingSession(groupAdult, coachThree,
                DayOfWeek.SATURDAY, timeTwelve);
        TrainingSession trainingSessionChildSaturday = new TrainingSession(groupChild, coachThree,
                DayOfWeek.SATURDAY, timeTen);

        timetable.addNewTrainingSession(trainingSessionChildMonday);
        timetable.addNewTrainingSession(trainingSessionAdultMonday);
        timetable.addNewTrainingSession(trainingSessionChildWednesday);
        timetable.addNewTrainingSession(trainingSessionAdultWednesday);
        timetable.addNewTrainingSession(trainingSessionChildFriday);
        timetable.addNewTrainingSession(trainingSessionAdultTuesday);
        timetable.addNewTrainingSession(trainingSessionAdultThursday);
        timetable.addNewTrainingSession(trainingSessionAdultSaturday);
        timetable.addNewTrainingSession(trainingSessionChildSaturday);
        //Проверяем количество дней когда есть тренировки
        Assertions.assertEquals(6, timetable.getTimetable().size());
        //Проверяем количество тренеров в списке
        Assertions.assertEquals(4, timetable.getCountByCoaches().size());
        //Проверяем сортировку тренеров в списке
        List<Map.Entry<Coach, Integer>> sortedCoaches = timetable.getCountByCoachesSorted();
        for (int i = 0; i < sortedCoaches.size() - 1; i++) {
            Map.Entry<Coach, Integer> current = sortedCoaches.get(i);
            Map.Entry<Coach, Integer> next = sortedCoaches.get(i + 1);
            Assertions.assertTrue(current.getValue() >= next.getValue());
        }
        //Выводим в консоль список тренеров и количество тренировок
        System.out.println(timetable.formatCountByCoaches());

        //Вывод в консоль расписания
        timetable.printTimeTable();
    }

}
