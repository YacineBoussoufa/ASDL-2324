package it.unicam.cs.asdl2324.es4;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Aula implements Comparable<Aula> {

    /*
     * numero iniziale delle posizioni dell'array facilities. Se viene richiesto
     * di inserire una facility e l'array è pieno questo viene raddoppiato. La
     * costante è protected solo per consentirne l'accesso ai test JUnit
     */
    protected static final int INIT_NUM_FACILITIES = 5;

    /*
     * numero iniziale delle posizioni dell'array prenotazioni. Se viene
     * richiesto di inserire una prenotazione e l'array è pieno questo viene
     * raddoppiato. La costante è protected solo per consentirne l'accesso ai
     * test JUnit.
     */
    protected static final int INIT_NUM_PRENOTAZIONI = 100;

    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    /*
     * Insieme delle facilities di quest'aula. L'array viene creato all'inizio
     * della dimensione specificata nella costante INIT_NUM_FACILITIES. Il
     * metodo addFacility(Facility) raddoppia l'array qualora non ci sia più
     * spazio per inserire la facility.
     */
    private Facility[] facilities;

    // numero corrente di facilities inserite
    private int numFacilities;

    /*
     * Insieme delle prenotazioni per quest'aula. L'array viene creato
     * all'inizio della dimensione specificata nella costante
     * INIT_NUM_PRENOTAZIONI. Il metodo addPrenotazione(TimeSlot, String,
     * String) raddoppia l'array qualora non ci sia più spazio per inserire la
     * prenotazione.
     */
    private Prenotazione[] prenotazioni;

    // numero corrente di prenotazioni inserite
    private int numPrenotazioni;

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
    	
    	if((nome==null) || (location==null))	//Se gli elementi passati sono nulli lancio un'Expeption
    		throw new NullPointerException("Nome o Location vuota!");
    	
    	this.nome = nome;
        this.location = location;
        this.facilities = new Facility[INIT_NUM_FACILITIES];
        this.numFacilities=0;
        this.prenotazioni = new Prenotazione[INIT_NUM_PRENOTAZIONI];
        this.numPrenotazioni = 0;

    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
    	int hash = 37;
        hash = 97 * hash + this.nome.hashCode();
        return hash;
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
    	return this.nome.compareTo(o.getNome()); //Ritorno true o false a seconda del compare
    }

    /**
     * @return the facilities
     */
    public Facility[] getFacilities() {
        return this.facilities;
    }

    /**
     * @return il numero corrente di facilities
     */
    public int getNumeroFacilities() {
        return this.numFacilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return the prenotazioni
     */
    public Prenotazione[] getPrenotazioni() {
        return this.prenotazioni;
    }

    /**
     * @return il numero corrente di prenotazioni
     */
    public int getNumeroPrenotazioni() {
        return this.numPrenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula. Controlla se la facility è già
     * presente, nel qual caso non la inserisce.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        /*
         * Nota: attenzione! Per controllare se una facility è già presente
         * bisogna usare il metodo equals della classe Facility.
         * 
         * Nota: attenzione bis! Si noti che per le sottoclassi di Facility non
         * è richiesto di ridefinire ulteriormente il metodo equals...
         */
    	
    	if(f==null)	//Se il valore è nullo lancio un eccezione
    		throw new NullPointerException("Facility nulla!");
    	
    	for (int i=0;i<this.numFacilities;i++) //Ciclo tutte le facility già presenti
            if (this.facilities[i].equals(f))	//Se è presente: ritorno falso
                return false;
    	
    	if (this.numFacilities==this.facilities.length) {	//Se non c'è ancora una posizione libera
            Facility[] newArray = new Facility[facilities.length*2]; // Crea un nuovo array lungo il doppio dell'attuale
            for (int i=0; i<numFacilities; i++) // Sostituisce l'array attuale con il nuovo
            	newArray[i] = facilities[i];
            this.facilities = newArray;	//Scambio gli array
    	}
    	this.facilities[this.numFacilities] = f;	//Inserisco la facility
        this.numFacilities++;	//Incremento il contatore delle facility
    	
        return true;	//Ritorno true
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
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
    	
    	if (ts==null)	//Se il timeslot è nullo lancio un Exception
            throw new NullPointerException("Il TimeSlot è nullo!");
    	
    	for (int i=0; i<numPrenotazioni; i++) {	//Ciclo per verificare se l'aulaè libera
            if(ts.overlapsWith(prenotazioni[i].getTimeSlot())) //Se c'è sovrappossizione ritorno falso
            	return false;
        }
    	
        return true;	//Altrimenti ritorno vero
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare, sono da considerare solo le
     *                                posizioni diverse da null
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Facility[] requestedFacilities) {
        // TODO implementare
    	
    	if (requestedFacilities == null)
            throw new NullPointerException("Le facilities richieste non possono essere nulle!");

    	/*
    	 *	Codice di test prima dell'implementazione di Facility.satisfies
    	 */
    	/* Per ogni facility richiesta
        for (int i = 0; i < requestedFacilities.length; i++) {
            // Controlla se la facility non è null e se è stata inserita
            if(requestedFacilities[i] != null && !isIn(requestedFacilities[i])) {
                return false;
            }
        }
        return true;*/  	
    	
    	for (int i = 0; i < requestedFacilities.length; i++) { //Ciclo ogni facility
            if (requestedFacilities[i]!=null) {	//Se la facility è null, la salto e vado avanti
            	boolean soddisfa=false;	//Flag 
	                        
	            for(int j=0;j<this.numFacilities;j++) {	//Scorro le facility dell'aula e verifico che è soddisfatta
	            	if (this.facilities[j].satisfies(requestedFacilities[i]))
	            		soddisfa=true;
	            }
	            
	            if (!soddisfa)	//Se non è soddisfatta almeno una volta ritorno il flag (falso)
	                return soddisfa;
            }
    	}
    	
        return true;	//Se non avviene nessun ritorno, vuol dire che è soddisfatta
    	
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
    	if( (ts==null) || (docente==null) || (motivo==null))	//Se uno degli elementi passati è nullo, lancio un exception
            throw new NullPointerException("Uno dei parametri è nullo");
    	
    	if(!this.isFree(ts))	//Se il timeslot di sovrappone lancio un exception
            throw new IllegalArgumentException("Il TimeSlot si sovrannone con un'altra prenotazione");
        
        // Controlla se l'array è pieno, se lo è lo raddoppio la dimenzione
        if(numPrenotazioni>=prenotazioni.length) {	//se il numero di prenotazioni è maggiore (impossibile) o uguale al limite massimo
        	
            Facility[] newArray = new Facility[facilities.length*2]; // Crea un nuovo array lungo il doppio dell'attuale
            for (int i=0; i<numFacilities; i++) // Sostituisce i dati delll'array attuale in quello nuovo
            	newArray[i] = facilities[i];
            this.facilities = newArray;	//Scambio gli array
        }
        this.prenotazioni[this.numPrenotazioni] = new Prenotazione(this, ts, docente, motivo);	//Inserisco il nuovo elemento
        this.numPrenotazioni++;	//Incremento il contatore del numero di prenotazioni
    	
    }

    // TODO inserire eventuali metodi privati per questioni di organizzazione
    /*private boolean isIn(Facility f) {
        for (int i = 0; i < numFacilities; i++	) {
            if(facilities[i].getCodice().equals(f.getCodice())) return true;
        }
        return false;
    }*/
}
