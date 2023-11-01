import java.time.LocalDate;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    Kalender startCalender = new Kalender();
    Revision startRevision = new Revision(startCalender);
    Menu() {
    }
    Scanner menutast = new Scanner(System.in);
    int tast;

    int op1;
    String op2;
    void mainMenu() {
        while (true) {
            System.out.println("\nVælg en af følgende valgmuligheder");
            System.out.println("1. Kalender");
            System.out.println("2. Revision");
            System.out.println("3. Søg efter Fornavn");
            System.out.println("4. Søg efter AftaleID");
            System.out.println("5. Print alle aftaler");
            System.out.println("6. Fjern aftale");
            System.out.println("7. Meld lukkedag");
            System.out.println("0. For at afslutte");

            try {
                tast = menutast.nextInt();
                switch (tast) {
                    case 1:
                        System.out.println("Kalender. \n");
                        startCalender.mainMenu();
                        break;
                    case 2:
                        System.out.println("Indtast adgangskode for Revision: ");
                        String adgangskode = menutast.next();
                        if (adgangskode.equals("hairyharry")) {
                            System.out.println("Adgangskode accepteret. Du har adgang til Revision.\n");
                            hovedmenu();
                            continue;
                        } else {
                            System.out.println("Forkert adgangskode. Du har ikke adgang til Revision.\n");
                            continue;
                        }
                    case 3:
                        System.out.println("Søg efter fornavn. \n");
                        op2 = menutast.next();
                        for (Aftale aftale : startCalender.findSpecificAftaleByName(startCalender.aftaleListe, op2)) {
                            System.out.println("\nDato: "+aftale.dato+"\nTidspunkt: "+aftale.tidspunkt+"\nKundenavn: "+aftale.kundenavn+"\nAftale nr: "+aftale.aftaleID+"\n");
                        }
                        break;

                    case 4:
                        System.out.println("Søg efter aftaleID. \n");
                        op1 = menutast.nextInt();
                        System.out.println(startCalender.findSpecificAftaleByAftaleID(startCalender.aftaleListe, op1) + "\n");
                        startCalender.findSpecificAftaleByAftaleID(startCalender.aftaleListe, op1);
                        break;
                    case 5:
                        System.out.println("Print alle aftaler.\n");
                        startCalender.aftaleListe.sort(Comparator.comparing(aftale -> aftale.dato));
                        System.out.println("Alle aftaler efter dato:");
                        for (Aftale aftale : startCalender.aftaleListe) {
                            System.out.println("\nDato: "+aftale.dato+"\nTidspunkt: "+aftale.tidspunkt+"\nKundenavn: "+aftale.kundenavn+"\nAftale nr: "+aftale.aftaleID+"\n");
                        }
                        break;
                    case 6:
                        System.out.println("Fjern aftale");
                        System.out.print("Indtast AftaleID: ");
                        System.out.println("Tast 0 for at gå tilbage");
                        op1 = menutast.nextInt();
                        if (op1==0){
                            break;
                        }
                        else
                        System.out.println(startCalender.findSpecificAftaleByAftaleID(startCalender.aftaleListe, op1) + "\n");
                        startCalender.fjernOrdre(startCalender.findSpecificAftaleByAftaleID(startCalender.aftaleListe, op1));
                        break;
                    case 7:
                        startCalender.meldLukkedag();
                        break;
                    case 0:
                        System.out.println("Afslut programmet.\n");
                        menutast.close();
                        return; // Afslut metoden og programmet
                    default:
                        System.out.println("Ugyldig valg. Prøv igen.\n");
                }
            } catch (InputMismatchException Ie) {
                System.out.println("Du skal bruge et tal for at komme videre, prøv igen!");
                menutast.next();
            }
        }
    }
    void hovedmenu() {
        while (true) {
            int op4 = 9;
            System.out.println("0. For afslut");
            System.out.println("1. For at søge på dato");
            System.out.println("2. For at søge på aftaleID");
            System.out.println("3. For at søge efter navn");
            System.out.println("4. For at registrere betaling");
            System.out.println("5. For at redigere betaling");
            op4 = menutast.nextInt();
            try {
                if (op4 == 0) break;
                switch (op4) {
                    case 1:
                        startRevision.vaelgDatoOgVisAftaler();
                        System.out.println("\nTotalbeløb for dag: "+startRevision.totalbelobfordag+"\n");
                        break;
                    case 2:
                        System.out.println("Indtast aftale-ID: ");
                        int søgeAftaleID = menutast.nextInt();
                        startRevision.findSpecificAftaleByAftaleID(startRevision.kalender.aftaleListe, søgeAftaleID);
                        break;
                    case 3:
                        System.out.println("Indtast navn: ");
                        String søgEfterNavn = menutast.next();
                        startRevision.findSpecificAftaleByName(startRevision.kalender.aftaleListe, søgEfterNavn);
                        break;
                    case 4:
                        System.out.println("Indtast aftale-ID: ");
                        søgeAftaleID = menutast.nextInt();
                        startRevision.registrerBetaling(startRevision.findSpecificAftaleByAftaleID(startRevision.kalender.aftaleListe, søgeAftaleID));
                        break;
                    case 5:
                        System.out.println("Indtast aftale-ID: ");
                        søgeAftaleID = menutast.nextInt();
                        startRevision.redigerBetaling(startRevision.findSpecificAftaleByAftaleID(startRevision.kalender.aftaleListe, søgeAftaleID));
                        break;
                    default:
                }
            } catch (InputMismatchException Ie) {
                System.out.println("Du skal vælge en gyldig valg mulighed!");
                menutast.next();
            }
        }
    }
    public static void main(String[] args) {
        Menu front = new Menu();
        front.mainMenu();
    }
}