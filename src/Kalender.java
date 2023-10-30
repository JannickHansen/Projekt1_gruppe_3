import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;

class Kalender {
    Scanner tastatur = new Scanner(System.in);
    List<Aftale> aftaleListe = new ArrayList<>();
    String op2;
    int op4;
    String kundenavn;
    List<LocalDate> ferieDage = new ArrayList<>();
    public static final String resetColour = "\u001B[0m";
    public static final String getGreen = "\u001B[32m";
    public static final String getRed = "\u001B[31m";
    public static final String getYellow = "\u001B[33m";


    void mainMenu() {
        List<LocalDate> selectedWeek;

        while (true) {
            System.out.println("\nUgedag: \t  Mandag\t  Tirsdag\t  Onsdag\t  Torsdag\t  Fredag");

            for (int weekOffset = 0; weekOffset < 4; weekOffset++) {
                List<LocalDate> weekDates = getWeek(weekOffset);

                for (int i = 0; i < 5; i++) {
                    LocalDate date = weekDates.get(i);

                    boolean isDayFullyBooked = isDayFullyBooked(date);
                    String dayColor = isDayFullyBooked ? getRed : getGreen;
                    if(erDatoLukket(date))
                        dayColor = getYellow;

                    if (i == 0) {
                        System.out.print("Uge nr: "+getWeekNumber(weekOffset)+"\t");
                    }

                    System.out.print(dayColor + date + resetColour + "\t");
                }
                System.out.println();
            }

            System.out.println("Tryk 0 for at returnere til hovedmenu.");
            System.out.println("Tryk 97 for at søge efter bestemt fornavn.");
            System.out.println("Tryk 98 for at søge efter bestemt aftaleID.");
            System.out.println("Tryk 99 for at se alle aftaler.");
            System.out.println("Tryk 100 for at melde lukkedag.");
            System.out.print("Skriv uge nr. for at oprette aftale:");

            int op1 = tryCatchInput();

            if (op1 == 0) {
                break;
            } else if (op1 == 97) {
                System.out.print("Indtast kundenavn:");
                op2 = tastatur.next();
                for (Aftale aftale : findSpecificAftaleByName(aftaleListe, op2)) {
                    System.out.println(aftale + "\n");
                }
            } else if (op1 == 98) {
                System.out.print("Indtast aftaleID:");
                op1 = tastatur.nextInt();
                System.out.println(findSpecificAftaleByAftaleID(aftaleListe, op1) + "\n");
                fjernOrdre(findSpecificAftaleByAftaleID(aftaleListe, op1));
            } else if (op1 == 99) {
                aftaleListe.sort(Comparator.comparing(aftale -> aftale.dato));
                System.out.println("Alle aftaler efter dato:");
                for (Aftale aftale : aftaleListe) {
                    System.out.println(aftale + "\n");
                }
            }else if (op1 == 100) {
                System.out.println("Indtast lukkedatoen (åååå-mm-dd): ");
                System.out.println("Tryk 0 for at gå tilbage ");
                boolean gyldigDato = false;
                char ferie=' ';
                while (!gyldigDato) {
                    String datoInput = tastatur.nextLine();
                    try {
                        if (datoInput.equals("0"))
                            break;
                        LocalDate valgtDato = LocalDate.parse(datoInput);
                        if (ferieDage.contains(valgtDato)){
                            System.out.println("Dato er allerede lukket ");
                            System.out.println("Vælg ny dato (åååå-mm-dd)");
                            System.out.println("Eller tryk 0 for at gå tilbage ");
                            continue;
                        }
                        if (isFerieEqualsAftale(valgtDato)) {
                            System.out.println("denne dag har allerede aftaler booket");
                            System.out.println("vil du stadig lukke dato? y/n");
                            while (true) {
                                ferie = tastatur.next().charAt(0);
                                tastatur.nextLine();
                                if (ferie == 'y') {
                                    Iterator<Aftale> iterator = aftaleListe.iterator();
                                    while (iterator.hasNext()) {
                                        Aftale aftale = iterator.next();
                                        if (aftale.dato.isEqual(valgtDato)) {
                                            System.out.println("Denne aftaler er nu fjernet: \n"+aftale);
                                            iterator.remove();
                                        }
                                    }
                                    break;
                                } else if (ferie == 'n') {
                                    break;
                                } else {
                                    System.out.println("Du kan enten taste y eller n");
                                }
                            }
                        }
                        if (ferie=='y' || !isFerieEqualsAftale(valgtDato)){
                            ferieDage.add(valgtDato);
                            System.out.println(valgtDato + " er nu lukket");
                            break;
                        }

                    } catch (Exception e) {
                        System.out.println("Ugyldigt datoformat. Brug formatet åååå-mm-dd.");
                    }
                }
            } else if (op1 > 53) {
                System.out.println("\nDer er højest 53 uger på et år. Vælg venligst en gyldig uge.\n");
            } else {
                selectedWeek = selectedWeek(op1, getWeekNumber(0));
                selectedWeekPrint(op1, getWeekNumber(0));
                System.out.println("\nTryk 0 for at gå tilbage");

                System.out.print(isDayFullyBooked(selectedWeek.get(0)) ? getRed : getGreen);
                System.out.print(resetColour);

                System.out.print("Skriv ugedagen hvor aftalen skal oprettes:");
                op2 = " ";
                op2 = tastatur.next();
                op2 = spellingControl(op2);
                if (isValidWeekday(op2)) {
                    opretAftale(selectedWeek);
                }
            }
        }
    }
    private boolean isFerieEqualsAftale(LocalDate date) {
        boolean ferieLukket = false;
        for (Aftale aftale : aftaleListe) {
            if (aftale.dato.isEqual(date)) {
                ferieLukket = true;
            }
        }
        return ferieLukket;
    }

    void opretAftale(List<LocalDate> selectedWeek) {
        op4 = convertWeekdayToInt.getOrDefault(op2, 0);
        LocalDate selectedDate = getDate(selectedWeek, op4);

        if(erDatoLukket(selectedDate)){
            System.out.println("Valgt dato er ferielukket");

            return;
        }

        Set<String> reservedTimeSlots = new HashSet<>();

        for (Aftale aftale : aftaleListe) {
            if (aftale.dato.isEqual(selectedDate)) {
                reservedTimeSlots.add(aftale.tidspunkt);
            }
        }

        System.out.println("0. For 0 for at returnere til kalenderen");
        for (int i = 1; i <= 8; i++) {
            String timeSlot = convertIntTimetoStringTime.get(i);
            if (reservedTimeSlots.contains(timeSlot)) {
                System.out.println(getRed + i + ". " + timeSlot + resetColour);
            } else {
                System.out.println(getGreen + i + ". " + timeSlot + resetColour);
            }
        }
        System.out.print("Vælg venligst tidspunkt:");
        while (true) {
            int op3 = tryCatchInput();
            if (op3 > 0 && op3 < 9) {
                String selectedTimeSlot = convertIntTimetoStringTime.getOrDefault(op3, "");

                if (reservedTimeSlots.contains(selectedTimeSlot)) {
                    System.out.println("Dette tidspunkt er allerede booket. Vælg venligst et andet tidspunkt.");
                } else {
                    System.out.print("Skriv kundens fornavn:");
                    kundenavn = tastatur.next();
                    Aftale nyAftale = new Aftale(getDate(selectedWeek, op4), selectedTimeSlot, kundenavn);
                    aftaleListe.add(nyAftale);
                    System.out.println("\nAftale oprettet:");
                    System.out.println(nyAftale + "\n");
                    break;
                }
            }
            else {
                System.out.println("Indtastet tidspunkt er ugyldigt. Vælg venligst et gyldigt tidspunkt.");
            }
        }
    }
    public LocalDate getDate(List<LocalDate> importList, int weekday) {
        if (weekday >= 1 && weekday <= importList.size()) {
            int indexToExtract = weekday - 1;
            return importList.get(indexToExtract);
        } else {
            System.out.println("Ugyldig Dato!\n");
            return LocalDate.now();
        }
    }
    public Aftale findSpecificAftaleByAftaleID(List<Aftale> aftaleListe, int aftaleID) {
        for (Aftale aftale : aftaleListe) {
            if (aftale.aftaleID == aftaleID) {
                return aftale;
            }
        }
        System.out.println("Aftalen kunne ikke findes.\n");
        return null;
    }
    public void fjernOrdre(Aftale fundetAftale){

        System.out.println("Skriv 0 for gå tilbage til menu");
        System.out.println("Skriv 1 for at fjerne "+fundetAftale.kundenavn+"'s aftale");
        int op1 = tastatur.nextInt();

        if(op1==1){
            aftaleListe.remove(fundetAftale);
        } else {
            System.out.println("Aftale ikke slettet");
        }
    }
    public List<Aftale> findSpecificAftaleByName(List<Aftale> aftaleListe, String name) {
        List<Aftale> matchingAftale = new ArrayList<>();
        for (Aftale aftale : aftaleListe) {
            if (aftale.kundenavn.equals(name)) {
                matchingAftale.add(aftale);
            }
        }
        if (matchingAftale.isEmpty()) {
            System.out.println("Aftalen kunne ikke findes.\n");
        }
        return matchingAftale;
    }
    List<LocalDate> selectedWeekPrint(int wantedWeek, int currentWeek) {
        int n = Math.abs(wantedWeek - currentWeek);
        List<LocalDate> weekDates = getWeek(n);
        System.out.println("Ugedag:   Mandag\t  Tirsdag\t  Onsdag\t  Torsdag\t  Fredag");
        System.out.print("Uge nr: ");
        System.out.print(getGreen + weekDates.get(0) + resetColour + "\t");

        for (int i = 1; i < 5; i++) {
            LocalDate date = weekDates.get(i);
            boolean isDayFullyBooked = isDayFullyBooked(date);
            String dayColor = isDayFullyBooked ? getRed : getGreen;
            if(erDatoLukket(date))
                dayColor = getYellow;
            System.out.print(dayColor + date + resetColour + "\t");
        }
        return weekDates;
    }
    List<LocalDate> selectedWeek(int wantedWeek, int currentWeek) {
        int i = Math.abs(wantedWeek - currentWeek);
        return getWeek(i);
    }

    public int getWeekNumber(int weeksInFuture) {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        weekNumber = weekNumber + weeksInFuture;
        return weekNumber;
    }
    public static List<LocalDate> getWeek(int week) {
        List<LocalDate> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        startOfWeek = startOfWeek.plusWeeks(week);

        for (int i = 0; i < 5; i++) {
            list.add(startOfWeek);
            startOfWeek = startOfWeek.plusDays(1);
        }
        return list;
    }
    public String spellingControl(String str) {
        if (str == null || str.isEmpty()) return str;
        String lowerCaseStr = str.toLowerCase();
        return lowerCaseStr.substring(0, 1).toUpperCase() + lowerCaseStr.substring(1);
    }
    private boolean isDayFullyBooked(LocalDate date) {
        Set<String> timeSlotsForDate = new HashSet<>();
        for (Aftale aftale : aftaleListe) {
            if (aftale.dato.isEqual(date)) {
                timeSlotsForDate.add(aftale.tidspunkt);
            }
        }
        for (int i = 1; i <= 8; i++) {
            String timeSlot = convertIntTimetoStringTime.get(i);
            if (!timeSlotsForDate.contains(timeSlot)) {
                return false;
            }
        }
        return true;
    }
    boolean isValidWeekday(String day) {
        String[] validWeekdays = {"Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag"};
        return Arrays.asList(validWeekdays).contains(day);
    }
    public int tryCatchInput() {
        int input = 0;
        boolean validInput = false;
        do {
            try {
                input = Integer.parseInt(tastatur.nextLine());
                validInput = true;
            } catch (NumberFormatException ignored) {}
        } while (!validInput);

        return input;
    }

    private static final Map<String, Integer> convertWeekdayToInt = new HashMap<>();

    static {
        convertWeekdayToInt.put("Mandag", 1);
        convertWeekdayToInt.put("Tirsdag", 2);
        convertWeekdayToInt.put("Onsdag", 3);
        convertWeekdayToInt.put("Torsdag", 4);
        convertWeekdayToInt.put("Fredag", 5);
    }
    private static final Map<Integer, String> convertIntTimetoStringTime = new HashMap<>();

    static {
        convertIntTimetoStringTime.put(1,"10:00 - 11:00");
        convertIntTimetoStringTime.put(2,"11:00 - 12:00");
        convertIntTimetoStringTime.put(3,"12:00 - 13:00");
        convertIntTimetoStringTime.put(4,"13:00 - 14:00");
        convertIntTimetoStringTime.put(5,"14:00 - 15:00");
        convertIntTimetoStringTime.put(6,"15:00 - 16:00");
        convertIntTimetoStringTime.put(7,"16:00 - 17:00");
        convertIntTimetoStringTime.put(8,"17:00 - 18:00");
    }

    boolean erDatoLukket(LocalDate lokalDato){
        return ferieDage.contains(lokalDato);
    }
}