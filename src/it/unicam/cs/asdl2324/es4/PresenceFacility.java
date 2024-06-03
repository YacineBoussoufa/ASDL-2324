/**
 * 
 */
package it.unicam.cs.asdl2324.es4;

/**
 * Una Presence Facility è una facility che può essere presente oppure no. Ad
 * esempio la presenza di un proiettore HDMI oppure la presenza dell'aria
 * condizionata.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class PresenceFacility extends Facility {

    /**
     * Costruisce una presence facility.
     * 
     * @param codice
     * @param descrizione
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla.
     */
    public PresenceFacility(String codice, String descrizione) {
        super(codice, descrizione);
    }

    /*
     * Una Presence Facility soddisfa una facility solo se la facility passata è
     * una Presence Facility ed ha lo stesso codice.
     * 
     */
    @Override
    public boolean satisfies(Facility o) {
    	if(o==null) //Se l'oggetto passato è null, lancio un exception
    		throw new NullPointerException("Facility Nulla!");
    	
    	if(o instanceof PresenceFacility) {	//Se o è un istanza di presence facility
            PresenceFacility pf = (PresenceFacility) o;	//Creo un oggetto temporaneo pf, a cui assegno il casting della Facility o
            if (this.getCodice().equals(pf.getCodice()))	//Se i codici corrispondono senza nessun problema, ritorno true
            	return true;
    	}
    	
        return false;	//Altrimenti ritornofalso
    }

}
