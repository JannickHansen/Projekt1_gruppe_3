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
                System.out.println("3. Søg efter fornavn");
                System.out.println("4. Søg efter aftaleID");
                System.out.println("5. Print alle aftaler");
                System.out.println("6. Fjern aftale");
                System.out.println("Tast 9 for at afslutte");

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
                                startRevision.hovedmenu();
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
                            op1 = menutast.nextInt();
                            System.out.println(startCalender.findSpecificAftaleByAftaleID(startCalender.aftaleListe, op1) + "\n");
                            startCalender.fjernOrdre(startCalender.findSpecificAftaleByAftaleID(startCalender.aftaleListe, op1));
                            break;
                        case 9:
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
        public static void main(String[] args) {
            Menu front = new Menu();
            front.mainMenu();
        }
    }
