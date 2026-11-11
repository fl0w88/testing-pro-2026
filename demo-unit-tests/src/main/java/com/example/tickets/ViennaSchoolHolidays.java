/*
 * Copyright (c) 2026 Nagarro GmbH. Author: Florin Balint.
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Demo code for training purposes. Distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for details.
 */

package com.example.tickets;

import de.focus_shift.jollyday.core.Holiday;
import de.focus_shift.jollyday.core.HolidayCalendar;
import de.focus_shift.jollyday.core.HolidayManager;
import de.focus_shift.jollyday.core.ManagerParameters;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * School holidays (Schulferien) and school-free days for Vienna (Wien).
 * <p>
 * The dates are derived from the rules of the Austrian Schulzeitgesetz as they apply to Vienna,
 * so they work for any school year, not just a hard-coded list:
 * <ul>
 *     <li>Herbstferien: 27 October - 31 October</li>
 *     <li>Allerseelen: 2 November (school-free)</li>
 *     <li>Leopolditag: 15 November (school-free in Vienna)</li>
 *     <li>Weihnachtsferien: 24 December - 6 January (from 23 December if that is a Monday)</li>
 *     <li>Semesterferien (Vienna and Lower Austria): first Monday in February until the following Sunday</li>
 *     <li>Osterferien: Saturday before Palm Sunday - Easter Monday</li>
 *     <li>Pfingstferien: Saturday before Pentecost - Whit Monday</li>
 *     <li>Sommerferien (Vienna, Lower Austria, Burgenland): Saturday between 28 June and 4 July -
 *         Sunday before the first Monday in September</li>
 * </ul>
 * Easter Monday and Whit Monday are taken from the Austrian holiday calendar provided by Jollyday.
 * School-autonomous days (schulautonome Tage) differ per school and are not included.
 */
public class ViennaSchoolHolidays {

    /**
     * A named, inclusive date range.
     */
    public record Period(String name, LocalDate start, LocalDate end) {
        public boolean contains(LocalDate date) {
            return !date.isBefore(start) && !date.isAfter(end);
        }
    }

    private final HolidayManager austrianHolidays;

    public ViennaSchoolHolidays() {
        this(HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.AUSTRIA)));
    }

    public ViennaSchoolHolidays(HolidayManager austrianHolidays) {
        this.austrianHolidays = austrianHolidays;
    }

    /**
     * @return true if the given date is within a Vienna school holiday or a Vienna school-free day.
     */
    public boolean isSchoolHoliday(LocalDate date) {
        // A date may belong to the school year that started last autumn or (for summer) the one ending now.
        return getSchoolHolidays(date.getYear() - 1).stream().anyMatch(p -> p.contains(date))
                || getSchoolHolidays(date.getYear()).stream().anyMatch(p -> p.contains(date));
    }

    /**
     * Returns all school holidays of the school year that starts in autumn of {@code startYear},
     * e.g. {@code 2026} returns the holidays of school year 2026/27 (including the summer holidays 2027).
     */
    public List<Period> getSchoolHolidays(int startYear) {
        int endYear = startYear + 1;
        List<Period> periods = new ArrayList<>();

        periods.add(new Period("Herbstferien",
                LocalDate.of(startYear, Month.OCTOBER, 27), LocalDate.of(startYear, Month.OCTOBER, 31)));
        LocalDate allSouls = LocalDate.of(startYear, Month.NOVEMBER, 2);
        periods.add(new Period("Allerseelen", allSouls, allSouls));
        LocalDate leopold = LocalDate.of(startYear, Month.NOVEMBER, 15);
        periods.add(new Period("Leopolditag", leopold, leopold));

        LocalDate dec23 = LocalDate.of(startYear, Month.DECEMBER, 23);
        LocalDate christmasStart = dec23.getDayOfWeek() == DayOfWeek.MONDAY ? dec23 : dec23.plusDays(1);
        periods.add(new Period("Weihnachtsferien", christmasStart, LocalDate.of(endYear, Month.JANUARY, 6)));

        LocalDate semesterStart = LocalDate.of(endYear, Month.FEBRUARY, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        periods.add(new Period("Semesterferien", semesterStart, semesterStart.plusDays(6)));

        LocalDate easterMonday = findHoliday(endYear, "christian.EASTER_MONDAY");
        // Easter Monday - 1 = Easter Sunday, - 7 = Palm Sunday, - 1 = Saturday before Palm Sunday
        periods.add(new Period("Osterferien", easterMonday.minusDays(9), easterMonday));

        LocalDate whitMonday = findHoliday(endYear, "christian.WHIT_MONDAY");
        periods.add(new Period("Pfingstferien", whitMonday.minusDays(2), whitMonday));

        LocalDate summerStart = LocalDate.of(endYear, Month.JUNE, 28)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        LocalDate nextSchoolYearStart = LocalDate.of(endYear, Month.SEPTEMBER, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        periods.add(new Period("Sommerferien", summerStart, nextSchoolYearStart.minusDays(1)));

        return periods;
    }

    private LocalDate findHoliday(int year, String propertiesKey) {
        return austrianHolidays.getHolidays(Year.of(year)).stream()
                .filter(h -> propertiesKey.equals(h.getPropertiesKey()))
                .map(Holiday::getDate)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(propertiesKey + " not found for year " + year));
    }
}
