/**
 * 
 */
package it.unicam.cs.asdl2324.es3;

// TODO completare gli import se necessario

import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     * 
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
    	if((start==null) || (stop==null))	//Se i due elementi sono nulli, lancio un Exception 
    		throw new NullPointerException("Uno dei due istanti è nullo");
    	if(start.compareTo(stop)>=0)	//Se l'istante did start è maggiore o uguale a stop, lancio un Exception
    		throw new IllegalArgumentException("Lo start non può essere uguale o successivo dello stop");
    	this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
    	if(obj==null) //Se l'oggetto passato è nullo, ritorno falso
    		return false; 
    	
    	if(!(obj instanceof TimeSlot))	//Se l'oggetto passato non è di tipo TimeSlot ritorno falso
    		return false;
    	
    	if(this==obj) //Se i due oggetti sono completamente uguali, ritorno vero
    		return true;
    	
    	TimeSlot tmp = (TimeSlot) obj;	//Dichiarazione oggetto temporaneo di tipo TimeSlot per il controllo
    	//Se l'intervallo di inizio e fine corrispondono ritorno vero
    	if(this.start.compareTo(tmp.start) == 0 && this.stop.compareTo(tmp.stop) == 0) {
            return true;
        }
    	
        return false;	//Altrimenti ritorno falso
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
    	int hash = 37;
        hash = 97 * hash + start.hashCode();
        hash = 97 * hash + stop.hashCode();
        return hash;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
    	// Se iniziano nello stesso momento, controlla se finiscono anche nello stesso momento
    	
    	if(o==null)
    		throw new NullPointerException("Time slot nullo"); 
    	
        if(this.start.compareTo(o.start) != 0) {	//NOn iniziano nello stesso momento
            return(this.start.compareTo(o.start));
        } else {	//Iniziano nello stesso momento
            return(this.stop.compareTo(o.stop));
        }
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     * 
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
    	if(o==null)	//Se il TimeSlot è nullo lancio un eccezione
    		throw new NullPointerException("Il time slot passato è nullo!");
    	
    	//Variabili per il controllo degli inizii e delle terminazioni
    	int start1 = this.start.compareTo(o.start);
        int start2 = this.start.compareTo(o.stop);
        int stop1 = this.stop.compareTo(o.start);
        int stop2 = this.stop.compareTo(o.stop);
        long tempo=0;
    	
        //Se inizia prima di quello precedente, e termina dopo che quello precedente sia iniziato
        if((start1<=0)&&(stop1>=0)&&(stop2<=0)) {
        	tempo=this.stop.getTimeInMillis()-o.start.getTimeInMillis();
        }
        else if((start1<=0)&&(stop2>=0)) {
        	//Se inizia prima di quello precedente e termina dopo che quello precedente sia finito
        	tempo=o.stop.getTimeInMillis()-o.start.getTimeInMillis();
        }
        else if((start1>=0) && (start2<=0) && (stop2>=0)) {
        	//Se inizia dopo quello precedente e termina dopo che quello precedente sia finito 
        	tempo = o.stop.getTimeInMillis()-this.start.getTimeInMillis();
        }
        else if((start1>=0)&&(stop2<=0)) {
        	//Se inizia prima di quello precendente e termina dopo che quello precendete sia finitiro
        	tempo = this.stop.getTimeInMillis()-this.start.getTimeInMillis();
        }
        
        if (tempo==0) //Se non c'è nessuna soprapposizione allora ritorno -1
            return -1;
        
        long minuti = tempo/60000; //Trasformo i millisecondi in minuti
        
        if(minuti>Integer.MAX_VALUE)	//Se i minuti superano il valore massimo lancio l'Expection
        	throw new IllegalArgumentException("I minuti di sovrapposizione superano Integer.MAX_VALUE");
        
        return (int) minuti; //Ritorno i minuti, trasformandoli da long a int per evitare il mismatch
    }
    
    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     * 
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
    	
    	int minuti = this.getMinutesOfOverlappingWith(o); //Determino il tempo di overlap
    	
    	if (minuti==-1) // Se non c'è sovrapposizione ritorno falso
            return false;
    	else if (minuti <= MINUTES_OF_TOLERANCE_FOR_OVERLAPPING) //Se non viene superata la tolleranza
            return false;	//Ritorno falso
    	else
    		return true;	//Altrimenti segnalo l'overlap
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     * 
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     * 
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     * 
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
    	String result = "[";
        result += start.get(GregorianCalendar.DAY_OF_MONTH) + "/" + (start.get(GregorianCalendar.MONTH)+1) + "/" +
                start.get(GregorianCalendar.YEAR) + " " + start.get(GregorianCalendar.HOUR_OF_DAY) + "." +
                start.get(GregorianCalendar.MINUTE);
        result += " - " + stop.get(GregorianCalendar.DAY_OF_MONTH) + "/" +  (stop.get(GregorianCalendar.MONTH)+1) + "/" +
                stop.get(GregorianCalendar.YEAR) + " " + stop.get(GregorianCalendar.HOUR_OF_DAY) + "." +
                stop.get(GregorianCalendar.MINUTE) + "]";
        return result;
    }

}
