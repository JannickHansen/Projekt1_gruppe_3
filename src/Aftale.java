import java.time.LocalDate;

public class Aftale {
    LocalDate dato;
    String tidspunkt;
    String kundenavn;
    protected static int aftaleIDcounter;
    protected int aftaleID;
    boolean erBetalt = false;
    String betalingsstatus;
    String betalingsmetode = "";
    double totalBelob;

    Aftale(LocalDate dato, String tidspunkt, String kundenavn) {
        aftaleIDcounter++;
        this.dato = dato;
        this.tidspunkt = tidspunkt;
        this.kundenavn = kundenavn;
        aftaleID = aftaleIDcounter;
    }
    public String toString() {
        if (erBetalt) {
            betalingsstatus = "Gennemført";
        } else
            betalingsstatus = "Ikke gennemført";
        return "\nDato: "+dato+"\nTidspunkt: "+tidspunkt+"\nKundenavn: "+kundenavn+"\nAftale nr: "+aftaleID+"\nBetalingsmetode: "+betalingsmetode+"\nBetalingsstatus: "+betalingsstatus+"\nTotal Beløb: "+totalBelob;
    }
}