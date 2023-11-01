import java.time.LocalDate;
import java.util.*;

public class Revision {
    private Map<LocalDate, List<String>> aftaler;
    public Kalender kalender;
    Scanner scanner = new Scanner(System.in);
    double totalbelobfordag = 0;

    public Revision(Kalender salonKalender) {
        aftaler = new HashMap<>();
        kalender = salonKalender;

    }

    public void tilføjAftale(String aftale, LocalDate dato) {
        LocalDate iDag = LocalDate.now();
        if (dato.isBefore(iDag.minusDays(1))) {
            aftaler.computeIfAbsent(dato, k -> new ArrayList<>()).add(aftale);
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
        totalbelobfordag = 0;
        while (!gyldigDato) {
            System.out.println("Indtast datoen (åååå-mm-dd): ");
            String datoInput = scanner.nextLine();

            try {
                LocalDate valgtDato = LocalDate.parse(datoInput);

                List<Aftale> alleAftaler = kalender.aftaleListe;

                if (alleAftaler != null) {
                    System.out.println("Aftaler på valgt dato (" + valgtDato + "):");
                    boolean fundetAftale = false;
                    for (Aftale aftale : alleAftaler) {
                        if (aftale.dato.isEqual(valgtDato)) {
                            System.out.println(aftale + "\n");
                            totalbelobfordag = totalbelobfordag + aftale.totalBelob;
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
            } catch (Exception DateTimeParseException) {
                System.out.println("Ugyldigt datoformat. Brug formatet åååå-mm-dd.");
            }
        }
    }
    void redigerBetaling (Aftale fundetAftale) {
        while (true) {
            System.out.println("Vil du redigere denne aftale? (Ja/Nej)");
            String jaEllerNej = scanner.next();
            jaEllerNej = spellingControl(jaEllerNej);
            if (jaEllerNej.equalsIgnoreCase("Ja")) {
                System.out.println("Vil du ændre betalingsmetode? (Ja/Nej) ");
                String editPayment = scanner.next();
                if (editPayment.equalsIgnoreCase("Ja")) {
                    while (true) {
                        System.out.println("Vælg betalingsmetode: ");
                        System.out.println("1. Kontant ");
                        System.out.println("2. MobilePay ");
                        System.out.println("3. Kort ");
                        System.out.println("4. Ubetalt ");
                        int op1 = scanner.nextInt();
                        switch (op1) {
                            case 1:
                                fundetAftale.betalingsmetode = "Kontant";
                                break;
                            case 2:
                                fundetAftale.betalingsmetode = "MobilePay";
                                break;
                            case 3:
                                fundetAftale.betalingsmetode = "Kort";
                                break;
                            case 4:
                                fundetAftale.betalingsmetode = "Ubetalt";
                                break;
                            default:
                                System.out.println("Ugyldigt input.");
                        }
                        break;
                    }
                }
                System.out.println("Afbetaling af Kredit? (Ja/Nej)");
                String editPaymentAmount = scanner.next();
                if (editPaymentAmount.equalsIgnoreCase("Ja") && fundetAftale.totalBelob < 0) {
                    while (true) {
                        System.out.println("Indtast afbetalingsbeløb: ");
                        double newPaymentAmount = scanner.nextDouble();
                        double op5 = -fundetAftale.totalBelob;
                        if (newPaymentAmount <= op5) {
                            fundetAftale.totalBelob = fundetAftale.totalBelob + newPaymentAmount;
                            if (fundetAftale.totalBelob == 0) {
                                fundetAftale.totalBelob = fundetAftale.afbetalingAfGaeld * -1;
                                fundetAftale.erBetalt = true;
                            }
                            break;
                        } else
                            System.out.println("Indtast venligst et beløb som maksimalt er lig fuld afbetaling.");
                    }
                }
                if (!fundetAftale.erBetalt) {
                    System.out.println("Er betalingen gennemført? (Ja/Nej)");
                    String paymentStatus = scanner.next();
                    paymentStatus = spellingControl(paymentStatus);
                    if (paymentStatus.equalsIgnoreCase("Ja")) {
                        fundetAftale.erBetalt = true;
                    } else {
                        fundetAftale.erBetalt = false;
                    }
                }
                System.out.println("Aftalen er blevet opdatere: " + fundetAftale + "\n");
                break;
            } else if (jaEllerNej.equalsIgnoreCase("Nej")) {
                System.out.println("Du har valgt ikke at redigere denne aftale.");
                break;
            } else {
                System.out.println("Du skal vælge mellem Ja eller Nej");
            }
        }
    }
    void registrerBetaling(Aftale fundetAftale) {
        int op1;
        int op2;
        double op3;
        System.out.println("\n1. Herreklipning. 200kr. " + "\n" + "2. kvindeklipning. 400 kr." + "\n" + "3. Herreklipning+kredit" + "\n" + "4. Kvindeklipning+kredit");

        op1 = scanner.nextInt();
        while (true) {
            if (op1 == 0)
                break;
            if (op1 == 1) {
                fundetAftale.totalBelob = 200;
                System.out.println("Ekstra omkostninger?" + "\n" + "1. ja. " + "\n" + "2. nej.");
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
                System.out.println("Ekstra omkostninger?" + "\n" + "1. ja." + "\n" + "2. nej.");
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
                fundetAftale.afbetalingAfGaeld = fundetAftale.totalBelob;
                System.out.println("Ekstra omkostninger?" + "\n" + "1. ja." + "\n" + "2. nej.");
                op2 = scanner.nextInt();
                if (op2 == 1) {
                    System.out.println("Total beløb for omkostninger. (XX,XX): ");
                    op3 = scanner.nextDouble();
                    fundetAftale.totalBelob = fundetAftale.totalBelob + -op3;
                    fundetAftale.erBetalt = false;
                    fundetAftale.betalingsmetode = "Kredit";
                    fundetAftale.afbetalingAfGaeld = fundetAftale.totalBelob;
                    System.out.println("\nManglende betaling: " + fundetAftale.totalBelob + "kr.\n");
                    break;
                } else {
                    fundetAftale.betalingsmetode = "Kredit";
                    fundetAftale.afbetalingAfGaeld = fundetAftale.totalBelob;
                    System.out.println("\nManglende betaling: " + fundetAftale.totalBelob + "kr.\n");
                    break;
                }
            } else if (op1 == 4) {
                fundetAftale.totalBelob = -400;
                fundetAftale.afbetalingAfGaeld = fundetAftale.totalBelob;
                System.out.println("Ekstra omkostninger?" + "\n" + "1. ja." + "\n" + "2. nej.");
                op2 = scanner.nextInt();
                if (op2 == 1) {
                    System.out.println("Total beløb for omkostninger. (XX,XX): ");
                    op3 = scanner.nextDouble();
                    fundetAftale.totalBelob = fundetAftale.totalBelob + -op3;
                    fundetAftale.erBetalt = false;
                    fundetAftale.betalingsmetode = "Kredit";
                    fundetAftale.afbetalingAfGaeld = fundetAftale.totalBelob;
                    System.out.println("\nManglende betaling: " + fundetAftale.totalBelob + "kr.\n");
                    break;
                } else {
                    fundetAftale.betalingsmetode = "Kredit";
                    fundetAftale.afbetalingAfGaeld = fundetAftale.totalBelob;
                    System.out.println("\nManglende betaling: " + fundetAftale.totalBelob + "kr.\n");
                    break;
                }
            } else {
                System.out.println("vælge 1, 2, 3 eller 4");
            }
        }
        if (op1 == 1 || op1 == 2) {
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
                } else {
                    System.out.println("Du skal vælge mellem de pågældne tal der står for oven...");
                }
                break;
            }
        }
    }
    public String spellingControl(String str) {
        if (str == null || str.isEmpty()) return str;
        String lowerCaseStr = str.toLowerCase();
        return lowerCaseStr.substring(0, 1).toUpperCase() + lowerCaseStr.substring(1);
    }
}