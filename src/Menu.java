import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    Kalender startCalender = new Kalender();
    Revision startRevision = new Revision(startCalender);
    Menu() {
    }
    Scanner menutast = new Scanner(System.in);
    int tast;
    void svend() {
        while (true) {

        System.out.println("\nVælg en af følgende valgmuligheder");
        System.out.println("1. Kalender");
        System.out.println("2. Revision");
        System.out.println("3. Søg efter fornavn");
        System.out.println("4. Søg efter aftaleID");
        System.out.println("5. Redigere aftale");
        System.out.println("Tast 9 for at afslutte");

            try {
                tast = menutast.nextInt();
                if (tast == 2) {
                    System.out.print("Indtast adgangskode for Revision: ");
                    String adgangskode = menutast.next();
                    if (adgangskode.equals("hairyharry")) {
                        System.out.println("Adgangskode accepteret. Du har adgang til Revision.\n");
                    } else {
                        System.out.println("Forkert adgangskode. Du har ikke adgang til Revision.\n");
                        continue;
                    }
                }
            switch (tast) {
                case 1:
                    System.out.println("Kalender.\n");
                    startCalender.mainMenu();
                    break;
                case 2:
                    System.out.println("Revision.\n");
                    startRevision.hovedmenu();
                    break;
                case 3:
                    System.out.println("Søg efter fornavn.\n");
                    break;
                case 4:
                    System.out.println("Søg efter aftaleID.\n");
                    break;
                case 5:
                    System.out.println("Redigere aftale.\n");
                    break;
                case 9:
                    System.out.println("Afslut programmet.\n");
                    menutast.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ugyldig valg. Prøv igen.\n");
            }

        }catch (InputMismatchException Ie){
            System.out.println("Du skal bruge et tal for at komme videre, prøv igen!");
            menutast.next();
        }
    }
}
    public static void main(String[] args) {
        Menu karl = new Menu();
        karl.svend();
    }
}
