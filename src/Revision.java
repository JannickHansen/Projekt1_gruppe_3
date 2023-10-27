import java.time.LocalDate;
import java.util.*;

public class Revision {
    private Map<LocalDate, List<String>> aftaler;
    private Kalender test1;
    Scanner scanner = new Scanner(System.in);

    public Revision(Kalender salonKalender) {
        aftaler = new HashMap<>();
        test1 = salonKalender;

    }

    public void tilføjAftale(String aftale, LocalDate dato) {
        LocalDate iDag = LocalDate.now();
        if (dato.isBefore(iDag.minusDays(1))) {
            aftaler.computeIfAbsent(dato, k -> new ArrayList<>()).add(aftale);
        }
    }

    void hovedmenu() {
        while (true) {
            System.out.println("0. For afslut");
            System.out.println("1. For at søge på dato");
            System.out.println("2. For at søge på aftaleID");
            System.out.println("3. For at søge efter navn");
            int op1 = scanner.nextInt();
            if (op1 == 0) break;
            switch (op1) {
                case 1:
                    vaelgDatoOgVisAftaler();
                    break;
                case 2:
                    System.out.println("Indtast aftale-ID: ");
                    int søgeAftaleID = scanner.nextInt();
                    findSpecificAftaleByAftaleID(test1.aftaleListe, søgeAftaleID);
                    break;
                case 3:
                    System.out.println("Indtast navn: ");
                    String søgEfterNavn = scanner.next();
                    findSpecificAftaleByName(test1.aftaleListe, søgEfterNavn);
                    break;
                case 4:
                    System.out.println("Enes was here");
            }
        }
    }

    public Aftale findSpecificAftaleByAftaleID(List<Aftale> aftaleListe, int aftaleID) {
        List<Aftale> alleAftaler = aftaleListe;
        for (Aftale aftale : aftaleListe) {
            if (aftale.aftaleID == aftaleID) {
                System.out.println("Fundet aftale: \n" + aftale + "\n");
                return aftale;
            }
        }
        System.out.println("Aftalen kunne ikke findes.\n");
        return null;
    }

    public List<Aftale> findSpecificAftaleByName(List<Aftale> aftaleListe, String name) {
        List<Aftale> matchingAftale = new ArrayList<>();
        for (Aftale aftale : aftaleListe) {
            if (aftale.kundenavn.equals(name)) {
                matchingAftale.add(aftale);
                System.out.println("Her er aftalen: \n" + aftale + "\n");
            }
        }
        if (matchingAftale.isEmpty()) {
            System.out.println("Aftalen kunne ikke findes.\n");
        }
        return matchingAftale;
    }

    public void vaelgDatoOgVisAftaler() {
        boolean gyldigDato = false;
        while (!gyldigDato) {
            System.out.println("Indtast datoen (åååå-mm-dd): ");
            String datoInput = scanner.nextLine();

            try {
                LocalDate valgtDato = LocalDate.parse(datoInput);

                List<Aftale> alleAftaler = test1.aftaleListe;

                if (alleAftaler != null) {
                    System.out.println("Aftaler på valgt dato (" + valgtDato + "):");
                    boolean fundetAftale = false;
                    for (Aftale aftale : alleAftaler) {
                        if (aftale.dato.isEqual(valgtDato)) {
                            System.out.println(aftale + "\n");
                            fundetAftale = true;
                        }
                    }
                    if (!fundetAftale) {
                        System.out.println("Ingen aftaler på valgt dato.");
                    }
                } else {
                    System.out.println("Ingen aftaler på valgt dato.");
                }

                gyldigDato = true;
            } catch (Exception e) {
                System.out.println("Ugyldigt datoformat. Brug formatet ååå-mm-dd.");
            }
        }
    }
    void registrerBetaling(Aftale fundetAftale) {
        int op1;
        int op2;
        double op3;
        System.out.println(fundetAftale);
        System.out.println("1. Herreklipning. 200kr. " + "\n" + "2.kvindeklipning. 400 kr." + "\n" + "3. Herreklipning+kredit" + "\n" + "4. Kvindeklipning+kredit");

        op1 = scanner.nextInt();
        while (true) {
            if (op1 == 1) {
                fundetAftale.totalBelob = 200;
                System.out.println("ekstra omkostninger?" + "\n" + "1. ja. " + "\n" + "2. nej.");
                op2 = scanner.nextInt();
                if (op2 == 1) {
                    System.out.println("Total beløb for omkostninger. (XX,XX): ");
                    op3 = scanner.nextDouble();
                    fundetAftale.totalBelob = fundetAftale.totalBelob + op3;
                }
                System.out.println("Totalbeløb for ordre er: " + fundetAftale.totalBelob);
                break;
            } else if (op1 == 2) {
                fundetAftale.totalBelob = 400;
                System.out.println("ekstra omkostninger?" + "\n" + "1. ja." + "\n" + "2. nej.");
                op2 = scanner.nextInt();
                if (op2 == 1) {
                    System.out.println("Total beløb for omkostninger. (XX,XX): ");
                    op3 = scanner.nextDouble();
                    fundetAftale.totalBelob = fundetAftale.totalBelob + op3;
                }
                System.out.println("Totalbeløb for ordre er: " + fundetAftale.totalBelob);
                break;
            } else if (op1 == 3) {
                fundetAftale.totalBelob = -200;
                System.out.println("ekstra omkostninger?" + "\n" + "1. ja." + "\n" + "2. nej.");
                op2 = scanner.nextInt();
                if (op2 == 1) {
                    System.out.println("Total beløb for omkostninger. (XX,XX): ");
                    op3 = scanner.nextDouble();
                    fundetAftale.totalBelob = fundetAftale.totalBelob + -op3;
                    fundetAftale.erBetalt = false;
                    fundetAftale.betalingsmetode = "Kredit";
                    System.out.println("Skylder " + fundetAftale.totalBelob + "kr.");
                } else if (op1 == 4) {
                    fundetAftale.totalBelob = -400;
                    System.out.println("ekstra omkostninger?" + "\n" + "1. ja." + "\n" + "2. nej.");
                    op2 = scanner.nextInt();
                    if (op2 == 1) {
                        System.out.println("Total beløb for omkostninger. (XX,XX): ");
                        op3 = scanner.nextDouble();
                        fundetAftale.totalBelob = fundetAftale.totalBelob + -op3;
                        fundetAftale.erBetalt = false;
                        fundetAftale.betalingsmetode = "Kredit";
                        System.out.println("Skylder " + fundetAftale.totalBelob + "kr.");
                    } else {
                        System.out.println("vælge 1, 2, 3 eller 4");
                    }
                }
                System.out.println("Gennemførelse af betalling" + "\n" + "hvilken betaling metode ønsker du?");
                System.out.println("Betallings metode." + "\n" + "1. Kontant." + "\n" + "2. MobilePay" + "\n" + "3. Kort" + "\n" + "4. Ubetalt" + "\n" + "5. kredit");
                op1 = scanner.nextInt();
                while (true) {
                    if (op1 == 1) {
                        fundetAftale.erBetalt = true;
                        fundetAftale.betalingsmetode = "Kontant";
                        System.out.println("Betalt via Kontant\nBeløbet er: " + fundetAftale.totalBelob + "kr.");
                        break;
                    } else if (op1 == 2) {
                        fundetAftale.erBetalt = true;
                        fundetAftale.betalingsmetode = "Mobilepay";
                        System.out.println("Betalt via MobilePay\nBeløbet er: " + fundetAftale.totalBelob + "kr.");
                        break;
                    } else if (op1 == 3) {
                        fundetAftale.erBetalt = true;
                        fundetAftale.betalingsmetode = "Kort";
                        System.out.println("Betalt via Kort\nBeløbet er: " + fundetAftale.totalBelob + "kr.");
                        break;
                    } else if (op1 == 4) {
                        fundetAftale.erBetalt = false;
                        fundetAftale.betalingsmetode = "Ubetalt";
                        System.out.println("Betaling ikke gennemført, Skylder: " + fundetAftale.totalBelob + "kr.");
                        break;
                    } else if (op1 == 5) {
                        fundetAftale.erBetalt = false;
                        fundetAftale.betalingsmetode = "Kredit";
                        System.out.println("Skylder " + fundetAftale.totalBelob + "kr.");
                    } else {
                        System.out.println("Du skal vælge mellem de pågældne tal der står for oven...");
                    }

                }

            }
        }
    }

    public static void main(String[] args) {
        Kalender salonKalender = new Kalender();
        Revision revision = new Revision(salonKalender);

        LocalDate iGår = LocalDate.now().minusDays(1);
        revision.tilføjAftale("Aftale fra i går1", iGår);
        revision.tilføjAftale("Aftale fra i går2", iGår);

        revision.vaelgDatoOgVisAftaler();

        /*int søgAftaleID = scanner.nextInt();
        revision.søgAftaleID();*/
    }
}