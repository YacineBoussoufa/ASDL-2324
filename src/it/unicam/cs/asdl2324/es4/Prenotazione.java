package it.unicam.cs.asdl2324.es4;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

    private final Aula aula;

    private final TimeSlot timeSlot;

    private String docente;

    private String motivo;

    /**
     * Costruisce una prenotazione.
     * 
     * @param aula
     *                     l'aula a cui la prenotazione si riferisce
     * @param timeSlot
     *                     il time slot della prenotazione
     * @param docente
     *                     il nome del docente che ha prenotato l'aula
     * @param motivo
     *                     il motivo della prenotazione
     * @throws NullPointerException
     *                                  se uno qualsiasi degli oggetti passati è
     *                                  null
     */
    public Prenotazione(Aula aula, TimeSlot timeSlot, String docente,
            String motivo) {
        // Riutilizzo il codice della ES 3
    	//Se un oggetto passato è nullo lancio un exception
    	if((aula==null)||(timeSlot==null)||(docente==null)||(motivo==null))
    		throw new NullPointerException("Uno degli oggetti passati è nullo");
    	
        this.aula = aula;
        this.timeSlot = timeSlot;
        this.docente = docente;
        this.motivo = motivo;
    }

    /**
     * @return the aula
     */
    public Aula getAula() {
        return aula;
    }

    /**
     * @return the timeSlot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * @return the docente
     */
    public String getDocente() {
        return docente;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param docente the docente to set
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public int hashCode() {
    	// Riutilizzo il codice della ES 3
    	int hash = 37;
        hash = 97 * hash + aula.hashCode();
        hash = 97 * hash + timeSlot.hashCode();
        return hash;
    }

    /*
     * L'uguaglianza è data solo da stessa aula e stesso time slot. Non sono
     * ammesse prenotazioni diverse con stessa aula e stesso time slot.
     */
    @Override
    public boolean equals(Object obj) {
    	if(obj == null)	//Se l'oggetto passato è nullo ritorno falso
    		return false;
        if(obj == this)	//Se l'oggetto passato è identito a quello presente ritorno true
        	return true;
        if(!(obj instanceof Prenotazione))	//Se l'oggetto passato non è di tipo Prenotazione ritorno falso
        	return false;
        
        Prenotazione tmp = (Prenotazione) obj;	//Dichiarazione oggetto temporaneo di tipo Prenotazione per il controllo 
        /*
        if(this.aula == tmp.aula && this.timeSlot.equals(tmp.timeSlot)) {
        	//Se due prenotazioni hanno la stessa aulae lo stesso timeslot ritorno vero
            return true;
        }
        return false;	//Altrimenti ritorno falso
        */
        
        //Cambio da == a equals in quanto si tratta di un oggetto: aula.
        
        if( (this.aula.equals(tmp.aula) ) && ( this.timeSlot.equals(tmp.timeSlot) ) )
            return true;
        
        return false;
    }

    /*
     * Una prenotazione precede un altra in base all'ordine dei time slot. Se
     * due prenotazioni hanno lo stesso time slot allora una precede l'altra in
     * base all'ordine tra le aule.
     */
    @Override
    public int compareTo(Prenotazione o) {
        // Riutilizzo il codice della ES 3
    	if(this.timeSlot.equals(o.getTimeSlot())) {
            return(this.aula.compareTo(o.aula));
        } else {
            return(this.timeSlot.compareTo(o.timeSlot));
        }
    }

    @Override
    public String toString() {
        return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot
                + ", docente=" + docente + ", motivo=" + motivo + "]";
    }

}
