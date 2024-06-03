package it.unicam.cs.asdl2324.es5;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                     il nome dell'aula
     * @param location
     *                     la location dell'aula
     * 
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location) {
    	if((nome==null)||(location==null)) //Se uno degli elementi passati è nullo lancio un exception
    		throw new NullPointerException("Informazioni richieste mancanti!");
        
    	this.nome = nome;
        this.location = location;
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                       il nome dell'aula
     * @param location
     *                       la location dell'aula
     * @param facilities
     *                       l'insieme delle facilities dell'aula
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
    	if((nome==null)||(location==null)||(facilities==null))
    		throw new NullPointerException("Informazioni richieste mancanti!");
    	
    	this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        return this.nome.hashCode();
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
    	if(obj == null)	//Se l'oggetto passato è nullo ritorno falso
    		return false;
        if(obj == this)	//Se l'oggetto passato è identito a quello presente ritorno true
        	return true;
        if(!(obj instanceof Aula))	//Se l'oggetto passato non è di tipo Aula ritorno falso
        	return false;
    	
        Aula tmp = (Aula) obj;	//Dichiarazione oggetto temporaneo di tipo Aula per il controllo
        
        if(this.nome.equals(tmp.getNome())) 	//Se sono uguali ritorno true
        	return true;

        return false;	//Altrimenti False
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        return this.nome.compareTo(o.getNome());
    }

    /**
     * @return the facilities
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        if(f==null)	//Se la facility passata è vuoto lancio un exception
        	throw new NullPointerException("Facility nulla!");
        
        return this.facilities.add(f);	//Altrimenti ritorno la facility aggiunta
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
     * @param ts
     *               il time slot da controllare
     * 
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     *         specificato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean isFree(TimeSlot ts) {
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se arrivo a una prenotazione che segue il time slot
         * specificato posso concludere che l'aula è libera nel time slot
         * desiderato e posso interrompere la ricerca
         */
    	
    	if(ts==null)	//Se il timeslot è nullo, lancio un exception
    		throw new NullPointerException("Time Slot nullo!");
    	
    	for(Prenotazione p:this.prenotazioni) {	//For each
            if(p.getTimeSlot().getStart().after(ts.getStop())) 
            	break;	// Se si arriva al timeslot attuale e non c'è nessuna sovrapposizione esco dal ciclo
            // Se si sovrappone, l'aula non è libera
            if(ts.overlapsWith(p.getTimeSlot())) //Se c'è un timeslot che si sovrappone ritorno falso
            	return false;
    	}
    	
        return true;	//Se si esce dal for senza nessun ritorno (dopo break) non c'è una sovrapposizione e ritorno true
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
	public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
    	if(requestedFacilities==null)	//Se l'elemento passato è nullo lanciio un exception
    		throw new NullPointerException("Set di facilities nullo!");
    	
        return facilities.containsAll(requestedFacilities);	//Ritorno tre o false se la facility richiesta è contenuta
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     * 
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException
     *                                      se la prenotazione comporta una
     *                                      sovrapposizione con un'altra
     *                                      prenotazione nella stessa aula.
     * @throws NullPointerException
     *                                      se una qualsiasi delle informazioni
     *                                      richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
    	if((ts==null)||(docente==null)||(motivo==null))	//Se gli elementi passati sono nulli lancio un exception
    		throw new NullPointerException("Informazioni richieste nulle!");
    	
    	if(this.isFree(ts)==false)	//Se il ts contiene una sovrapposizione lancio un exception
    		throw new IllegalArgumentException("Prenotazione sovrapposta!");
    	
    	// Crea la prenotazione e la aggiunge
        Prenotazione p = new Prenotazione(this, ts, docente, motivo);
        this.prenotazioni.add(p);
    }

    /**
     * Cancella una prenotazione di questa aula.
     * 
     * @param p
     *              la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     *         presente.
     * @throws NullPointerException
     *                                  se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
    	 if(p==null)
    		 throw new NullPointerException("Prenotazione nulla!");
    	 
         // Rimuove la prenotazione, controllando in automatico se è già presente
         return this.prenotazioni.remove(p);
    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     * 
     * @param timePoint
     *                      un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     *         altrimenti.
     * @throws NullPointerException
     *                                  se il punto nel tempo passato è nullo.
     */
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se ho raggiunto una prenotazione con tempo di inizio
         * maggiore del tempo indicato posso smettere la procedura
         */
    	
    	if(timePoint==null)	//Se il parametro passato è nullo lancio un exception
    		throw new NullPointerException("Punto nel tempo passato nullo!");
    	
    	Set<Prenotazione> prenotazioniDaCancellare = new HashSet<Prenotazione>();
    	
    	for (Prenotazione p: this.prenotazioni) {	//FOr Each
            if(p.getTimeSlot().getStart().after(timePoint))
            	break;	// Se è arrivato a una prentoazione successiva al timePoint, interrompe la ricerca
            else	// Altrimenti aggiunge la prenotazione a quelle da cancellare
            	prenotazioniDaCancellare.add(p);
        }
    	
        if(!prenotazioniDaCancellare.isEmpty()) { 
            this.prenotazioni.removeAll(prenotazioniDaCancellare);
            return true; // Se ci sono delle prenotazioni da cancellare, le rimuove dalle prenotazioni
        }
        return false;
    }
}
