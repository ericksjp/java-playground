/*
 * Trying to replicate LocalDate class from Java 8
 */

public class MyDate {
    private int year;
    private int month;
    private int day;
    private final int[] daysPerMonth =
            { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };


    /* Constructor */

    public MyDate(){
        this(1970,1,1);
    }
    public MyDate(int year){
        this(year,1,1);
    }
    public MyDate(int year, int month){
        this(year,month,1);
    }
    public MyDate(int year, int month, int day) {
        if (year % 4 == 0) daysPerMonth[1] = 29;

        if (month < 1 || month > 12){
            throw new IllegalArgumentException("month must be 1-12");
        }

        if (year % 4 == 0 && month == 2){
            if (day < 1 || day > daysPerMonth[month - 1]){
                throw new IllegalArgumentException("day must be 1-29");
            }
        } else {
            if (day < 1 || day > daysPerMonth[month - 1]){
                throw new IllegalArgumentException("day must be 1-" + daysPerMonth[month - 1]);
            }
        }

        this.year = year;
        this.month = month;
        this.day = day;
    }

    /* Getters and Setters */

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year == getYear()) return;

        if (year % 4 == 0 && !(year % 100 == 0 && year % 400 != 0))
            daysPerMonth[1] = 29;
        else {
            daysPerMonth[1] = 28;
            if (month == 2 && day == 29) setDay(28);
        }

        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month == getMonth()) return;

        if (month > 12 || month < 1){
            throw new IllegalArgumentException("month must be 1-12");
        }
        if (getDay() > daysPerMonth[month - 1]){
            setDay(daysPerMonth[month - 1]);
        }
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (day == getDay()) return;

        int maxDays = daysPerMonth[getMonth() - 1];
        if (day > maxDays || day < 1){
            throw new IllegalArgumentException("day must be 1-" + maxDays);
        }
        this.day = day;
    }

    /* Arithmetic operations */

    public void plusYears(int year){
        setYear(getYear() + year);
    }
    public void minusYears(int year){
        setYear(getYear() - year);
    }

    public void plusMonths(int months){
        this.plusDays(Math.multiplyExact(months, 30L));
    }
    public void minusMonths (int months){
        this.minusDays(Math.multiplyExact(months, 30L));
    }
    public void plusWeeks(int weeks){
        this.plusDays(Math.multiplyExact(weeks, 7));
    }
    public void minusWeeks(int weeks) {this.minusDays(Math.multiplyExact(weeks, 7));}

    public void plusDays(long days){
        days = days + getDay();
        while ((days) > daysPerMonth[getMonth() - 1]){
            days = days - daysPerMonth[getMonth() - 1];
            if (getMonth() + 1 > 12){
                setMonth(1);
                setYear(getYear() + 1);
            }
            else {
                setMonth(getMonth() + 1);
            }
            setDay(1);
        }
        setDay((int) days);
    }

    public void minusDays(long days){
        if (days < 0) days*=-1;

        while (days >= getDay()){
            if (getMonth() - 1 < 1){
                setMonth(12);
                setYear(getYear() - 1);
            }
            else {
                setMonth(getMonth() - 1);
            }
            days = days - getDay();
            setDay(daysPerMonth[getMonth() - 1]);
        }
        setDay(getDay() - (int)days);
    }

    /* Compare operations */

    public static int compareDate(MyDate date1, MyDate date2){
        if (date1.getYear() > date2.getYear()) return 1;
        if (date1.getYear() < date2.getYear()) return -1;

        if (date1.getMonth() > date2.getMonth()) return 1;
        if (date1.getMonth() < date2.getMonth()) return -1;

        return Integer.compare(date1.getDay(), date2.getDay());
    }

    /* To String */

    @Override
    public String toString(){
        return this.toString(null, null);
    }
    public String toString(String separator){
        return this.toString(separator, null);
    }
    public String toString(String separator, String format){
        if (separator == null) separator = "/";
        if (format == null) format = String.format("dd%smm%syy",separator,separator);
        String[] acepted = {"dd", "mm", "yy"};
        String[] received = format.split(separator);
        if (received.length != 3){
            throw new IllegalArgumentException("format must be: xx,xx,xx");
        }

        StringBuilder formString = new StringBuilder();

        for (int i = 0; i < acepted.length; i++){
            if ( !(received[i].equals(acepted[0]) || received[i].equals(acepted[1]) || received[i].equals(acepted[2])) ){
                throw new IllegalArgumentException("invalid format");
            } else {
                if (received[i].compareTo("dd") == 0){
                    formString.append(String.format("%02d",getDay()));
                }
                else if (received[i].compareTo("mm") == 0){
                    formString.append(String.format("%02d", getMonth()));
                }
                else formString.append(getYear());
                if (i < acepted.length - 1){
                    formString.append(separator);
                }
            }
        }

        return formString.toString();
    }

}
