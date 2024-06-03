package it.unicam.cs.asdl2324.mp1;

import java.util.Iterator;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ConcurrentModificationException;

/**
 * La classe MyMultiset implementa un {@link Multiset} utilizzando una mappa {@link  HashMap}
 * Ogni elemento del multiset è rappresentato da una chiave nella mappa. 
 * Il valore associato a ciascuna chiave è il numero di occorrenze dell'elemento nella mappa.
 * L'accesso a un elemento del multiset richiede tempo costante, grazie alle HashMap.
 * 
 * @author Luca Tesei (template)
 * 		   Yacine Boussoufa yacine.boussoufa@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
public class MyMultiset<E> implements Multiset<E> {

	/**
     * Misurazione della dimensione del multiset
     */
	private int dimensione;
	
	/**
     * Mappa degli elementi del multiset
     */
	private HashMap<E, Integer> mappaElementi;
	
	/**
     * Numero di modifiche effettuate sul multiset
     */
	private int modifiche;	//Dichiarazione variabile che contiene il numero di modifiche
	
	
    /**
     * La classe IteratorMultiSet implementa l'interfaccia {@link Iterator} e 
     * permette di restituire un Iteratore per la mappa del MultiSet.
     * Per scorrere gli elementi del multiset, si usa il metodo {@code hasNext()} per verificare
     * se ci sono ancora elementi da scorrere, e il metodo {@code next()} per ottenere il prossimo elemento.
     * 
     * Se il multiset viene modificato durante l'iterazione, l'iteratore potrebbe restituire risultati imprevisti
     * per questo l'iterator è fail-fast: se il multinsieme viene modificato strutturalmente
     * in qualsiasi momento dopo la creazione dell'iteratore, l'iteratore lancia una
     * {@code ConcurrentModificationException} alla chiamata successiva del metodo {@code next()}.
     * 
     * 
     * @author Yacine Boussoufa yacine.boussoufa@studenti.unicam.it (implementazione) 
     *
     */
	private class IteratorMultiSet implements Iterator<E> {

        /**
         * Iteratore sui singoli elementi del multiset
         */
        private final Iterator<E> elementiIterator=elementSet().iterator();
        
        /**
         * Il numero di modifiche a cui è stato sottoposto il set fin'ora
         */
        private final int modificheAttuali=modifiche;

        /**
         * L'elemento di cui si stanno restituendo le occorrenze
         */
        
        private E elementiAttuali;
        /**
         * Contatore delle occorrenze ancora da restituire dell'elemento
         */
        private int contatoreElementiAttuali;

        @Override
        public boolean hasNext() {
            return elementiIterator.hasNext() || contatoreElementiAttuali>0;	//Ritorno true o false se c'è un successivo
        }

        @Override
        public E next() {
            if(modificheAttuali!=modifiche)	//Il multiset è stato modificato durante l'iterazione
                throw new ConcurrentModificationException("Multiset changed during iteration");
            else if(contatoreElementiAttuali==0) {	//Tutte le occorrenze dell'elemento attuale sono finite
            	elementiAttuali=elementiIterator.next();
            	contatoreElementiAttuali=mappaElementi.get(elementiAttuali);
            }
            
            contatoreElementiAttuali--;	//Diminuisco il contatore degli elementi attuali
            return elementiAttuali;	//Ritorno il valore degli elementi attuali
        }
    }
	

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
    	this.dimensione=0;	//Inizializzazione della dimensione a 0
    	this.mappaElementi=new HashMap<>();	//Creazione di un MultiSet vuoto
    	this.modifiche=0;	//Inizializzazione = modifiche 0
    }

    @Override
    public int size() {
        return this.dimensione;	//Ritorno del valore della dimensione del multiset
    }

    @Override
    public int count(Object element) {
    	if(element==null)	//Se l'elemento è nullo lancio un eccezione
    		throw new NullPointerException("Elemento nullo!");
    	
    	//Ritorno il numero di occorrenze se l'elemento non è presente, invece di ritornare
    	//il valore di default null, ritorna invece 0
    	return mappaElementi.get(element)==null?0:mappaElementi.get(element);
    	
    }

    @Override
    public int add(E element, int occurrences) {
    	if(element==null)	//Se l'elemento è nullo lancio un eccezione
    		throw new NullPointerException("Elemento nullo!");
    	
    	//Salvataggio valore delle precedenti occorrenze, richiamando il metodo Count (evitando di richiamarlo ogni volta)
    	int precOccorrenze=count(element);
    	
    	//Se le occorrenze sono minori di 0 o se superano il valore massimo, lancio un eccezione
    	if((occurrences<0)||(precOccorrenze+occurrences>Integer.MAX_VALUE))
    		throw new IllegalArgumentException("Numero di occorrenze non valido!");
    	else if(occurrences==0)	//Se le occorrenze è 0, ritorno il numero di occorrenze precendete (evitando di chiamare setCount inutilmente)
    		return precOccorrenze;
    	
    	this.modifiche++;  //Incremento le modifiche
    	//Ritorno il valore ritornato dalla SetCount (in quanto anch'essa ritorna il valore precendente)
    	return this.setCount(element, precOccorrenze+occurrences);	
    }

    @Override
    public void add(E element) {
        this.add(element, 1);	//Richiamo l'add codificata precedentemente con un unica occorrenze
    }

    @SuppressWarnings("unchecked")
	@Override
    public int remove(Object element, int occurrences) {
        if(occurrences<0)	//Se le occorrenze passate sono minori di 0 lancio un eccezione
        	throw new IllegalArgumentException("Valore occorrenze negativo!");
        if(element==null)	//Se l'elemento passato è nullo lancio un eccezione
        	throw new NullPointerException("Elemento nullo!");
        
        if(!(this.contains(element)))	//Se l'elemento non è presente nella mappa degli elementi
        	return 0;	//Ritorno 0, in quanto nessun valore viene cancellato
        
        int precOccorrenze=count(element);	//Valore delle precedenti occorrenze (si potrebbe usare questo per il confronto invece di contains)
        
        if(occurrences==0)	//Non viene eliminato nessun elemento
        	return precOccorrenze;
        
        if(occurrences>=precOccorrenze) {	//Se il numero di occorrenze da rimuovere è maggiore del numero di occorenze presenti
        	this.mappaElementi.remove(element);	//Rimuovo tutte le occorrenze dalla mappa
        	this.dimensione=this.dimensione-precOccorrenze;	//Diminiusisco dalla dimensione, il numero di occorrenze cancellate
        	this.modifiche++;  //Incremento le modifiche
        } else {	//Altrimenti rimuovo una solo occorrenza
            this.setCount((E) element, precOccorrenze-occurrences);
            this.dimensione=this.dimensione-precOccorrenze-occurrences;	//Nuovo valore della dimensione
            this.modifiche++;  //Incremento le modifiche
        }
        
        return precOccorrenze;	//Ritorno il valore della precedente occorrenza
    }

    @Override
    public boolean remove(Object element) {
    	int precOccorrenze = remove(element,1); //Rimuovo un singolo elemento
        
    	return precOccorrenze<=0?false:true;	//Ritorno falso se il numero delle precedenti occorrenze è 0
    }

    @Override
    public int setCount(E element, int count) {
    	if(element==null)	//Se l'elemento passato è nullo lancio un eccezione
    		throw new NullPointerException("Elemento nullo!");
    	if(count<0)
    		throw new IllegalArgumentException("Il numero di occorenze è negativo!");

    	//Se element non esiste la put ritorna 0, se esiste viene ritornato il numero di occorrenze precendeti (come da Q&A)
    	Integer precOccorrenze=mappaElementi.put(element, count);
        precOccorrenze=(precOccorrenze==null)?0:precOccorrenze;
        
        this.dimensione=this.dimensione+count-precOccorrenze; //Nuova dimensione
        
        if (precOccorrenze!=count)	//Il multiset cambia solo se il nuovo count è diverso
            this.modifiche++;  //Incremento le modifiche 
        
        return precOccorrenze;	//Ritorno il valore delle occorrenze precedenti
    }

    @Override
    public Set<E> elementSet() {
    	return new HashSet<>(this.mappaElementi.keySet());
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorMultiSet();	//Ritorno un'istanza dell'iteratore
    }

    @Override
    public boolean contains(Object element) {
    	if(element==null)	//Se l'elemento è nullo lancio un eccezione
    		throw new NullPointerException("Elemento nullo!");	
    	
    	return this.mappaElementi.containsKey(element);	//Ritorno se nell'HashMap è contenuto l'elemento
    }

    @Override
    public void clear() {
    	this.mappaElementi.clear();	//Pulizia degli elementi
    	this.dimensione=0;	//Dopo la pulizia la dimensione è 0
    	this.modifiche++;  //Incremento le modifiche
    }

    @Override
    public boolean isEmpty() {
        return this.dimensione==0;	//Ritorno il valore della dimensione, se è 0, è vuoto, altrimenti no
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
    	if(obj==null)	//Se l'oggetto passato è nullo ritorno falso
    		return false;
        if(obj==this)	//Se l'oggetto passato è identito a quello presente ritorno true
        	return true;
        if(!(obj instanceof MyMultiset))	//Se l'oggetto passato non è di tipo MyMultiSet ritorno falso
        	return false;
    	
        MyMultiset<?> tmp = (MyMultiset<?>) obj;	//Dichiarazione oggetto temporaneo di tipo MyMultiset per il controllo
        Iterator<E> thisIterator = this.iterator();	//Creo l'iteratore di questo multiset
        Iterator<?> tmpIterator = tmp.iterator();	//Creo l'iteratore del multiset temporaneo
        
        while(thisIterator.hasNext()) {	//Ciclo finchè c'è un elemento nel multiset
        	 if(!tmpIterator.hasNext())	//Se i due iteratori hanno un numero diverso di dimensioni sono diversi
        		 return false;			//Quindi ritorno falso
        	 else if(!thisIterator.next().equals(tmpIterator.next()))	//Se invece l'iteratore temporaneo ha più elementi
        		 return false;	//Sono diversi e ritorno false
        }
        
        return !(tmpIterator.hasNext());	//Se sono identici ritorno true
        
        /*
        //Se le dimensioni sono diversi o se uno dei due è vuoto mentre l'altro non lo è: sono diversi
        if((this.size()!=tmp.size())||(this.isEmpty() && !tmp.isEmpty())||(!this.isEmpty() && tmp.isEmpty())) 
        	return false;
        else if(this.isEmpty() && tmp.isEmpty())
        	return true;	//Se sono entrambi vuoti sono uguali
        
        
        //Altrimenti ritorno true o false se i due elementi corrispondono o no
        return this.mappaElementi.equals(tmp.mappaElementi);
        */
                
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	int hash=37;
    	for (Iterator<E> iterator=iterator(); iterator.hasNext();) {
            hash=hash+96*iterator().next().hashCode();
        }
        return hash;
    }

}
