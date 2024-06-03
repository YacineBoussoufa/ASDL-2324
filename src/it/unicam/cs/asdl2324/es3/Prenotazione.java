package it.unicam.cs.asdl2324.es3;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Luca Tesei
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

    private final String aula;

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
    public Prenotazione(String aula, TimeSlot timeSlot, String docente,
            String motivo) {
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
    public String getAula() {
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
     * @param docente
     *                    the docente to set
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }

    /**
     * @param motivo
     *                   the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /*
     * Due prenotazioni sono uguali se hanno la stessa aula e lo stesso time
     * slot. Il docente e il motivo possono cambiare senza influire
     * sull'uguaglianza.
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
        
        if(this.aula == tmp.aula && this.timeSlot.equals(tmp.timeSlot)) {
        	//Se due prenotazioni hanno la stessa aulae lo stesso timeslot ritorno vero
            return true;
        }
        return false;	//Altrimenti ritorno falso
    }

    /*
     * L'hashcode di una prenotazione si calcola a partire dai due campi usati
     * per equals.
     */
    @Override
    public int hashCode() {
        int hash = 37;
        hash = 97 * hash + aula.hashCode();
        hash = 97 * hash + timeSlot.hashCode();
        return hash;
    }

    /*
     * Una prenotazione precede un altra in base all'ordine dei time slot. Se
     * due prenotazioni hanno lo stesso time slot allora una precede l'altra in
     * base all'ordine tra le aule.
     */
    @Override
    public int compareTo(Prenotazione o) {
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
