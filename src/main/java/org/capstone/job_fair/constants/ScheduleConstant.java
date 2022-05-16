package org.capstone.job_fair.constants;

public final class ScheduleConstant {
    private ScheduleConstant() {
    }

    public static final String BEGIN_TIME = "946688400000"; //Saturday, January 1, 2000 1:00:00 AM
    public static final String END_TIME = "4102448400000"; //Friday, January 1, 2100 1:00:00 AM
    public static final long BUFFER_CHANGE_INTERVIEW_SCHEDULE = 60 * 60 * 1000L;

    public static final String WAITING_ROOM_PREFIX = "w";
    public static final String INTERVIEW_ROOM_PREFIX = "i";

}
