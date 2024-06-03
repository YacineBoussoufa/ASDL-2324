/**
 * 
 */
package it.unicam.cs.asdl2324.es10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
@SuppressWarnings("unchecked")
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzion di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    };

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente
         * 
         */
    	if(o==null)	//Controlla se l'elemento è nullo e lancio un eccezione
    		throw new NullPointerException("L'elemento è nullo!");
        //Calcola il bucket in cui cercare l'oggetto dato
        int bucket=this.phf.hash(o.hashCode(),this.getCurrentCapacity());
        
        if(table[bucket]==null)	//Controlla se c'è una lista di collisioni
        	return false;
        
		Node<E> list=(Node<E>) this.table[bucket];	//Ottiene il nodo del bucket ottenuto
        
        
        do{	//Se il nodo contiene null, ritorna false
            
            if(o.equals(list.item))	//Controlla l'elemento corrente
            	return true;	//Se lo trova ritorno true
            
            list = list.next; //altrimenti vado avanti nella lista di collisioni
        }while(list!=null);
        
        return false;	//Non ha trovato l'elemento, ritorna false
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui inserire
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve inserire l'elemento o
         * nella lista concatenata lì presente. Se vuota, si crea la lista
         * concatenata e si inserisce l'elemento, che sarà l'unico.
         * 
         */
        // ATTENZIONE, si inserisca prima il nuovo elemento e poi si controlli
        // se bisogna fare resize(), cioè se this.size >
        // this.getCurrentThreshold()
    	if(e==null)
    		throw new NullPointerException("Elemento nullo!");
    	
        
        if(this.contains(e))	//Controlla se la tabella contiene già l'elemento
        	return false;
        
        int bucket=this.phf.hash(e.hashCode(), this.getCurrentCapacity()); //Ottiene il bucket dell'elemento dato
        
        Node<E> newNode=new Node<E>(e, null);	//Crea nuovo nodo con l'elemento dato
        
		Node<E> n = (Node<E>) this.table[bucket];	//Ottiene il nodo del bucket ottenuto
        
        if(n==null)	//Se il nodo è null, ci inserisce il nuovo nodo creando la lista
            this.table[bucket] = newNode;
        else { //Altrimenti scorre la lista a cui punta il bucket
            while(n!=null) 
                n=n.next;
            
            n = newNode;	//Imposta ultimo nodo al nuovo nodo
        }
        
        //Incrementa dimensione tabella e Contatore modifiche
        this.size++;
        this.modCount++;
        
        //Se ha superato la capacità della tabella, effettua il resize
        if(this.size>this.getCurrentThreshold())
            this.resize();
        
        return true;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
    	
        int newCapacity = this.getCurrentCapacity()*2; //Calcola la capacità per la nuova tabella
       
        Object[] newTable = new Object[newCapacity];  //Crea la nuova tabella
        
        Itr it = (CollisionListResizableHashTable<E>.Itr) this.iterator(); //Itera sulla tabella attuale
        while(it.hasNext()){
            E elem=it.next();	//Calcola il bucket dell'elemento con la nuova capacità
            
            int bucket = this.phf.hash(elem.hashCode(), newCapacity);  //Inserisce l'elemento nella nuova tabella
            this.insertElementInTable(newTable, bucket, elem);
        }
        
        this.table = newTable; //Sostituisce la tabella attuale con la nuova tabella
    }
    
    public boolean insertElementInTable(Object[] table, int bucket, E e) {
        if(table[bucket]==null) {  //Se la posizione è vuota creo una nuova lista
            table[bucket]=new Node<E>(e, null);
            return true;
        }else{
            Node<E> list = (Node<E>) table[bucket];
            do{
                if(e.equals(list.item))	//Controlla l'elemento corrente
                	return false;
                
                list = list.next; //Altrimenti va avanti nella lista di collisioni
            }while(list!=null);
            Node<E> head=(Node<E>) table[bucket];
            table[bucket]=new Node<E>(e, head);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, **non** comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.
    	
        if(o==null)	// Controlla se l'elemento dato è nullo
        	throw new NullPointerException("Elemento nullo!");
        
        if(!this.contains(o))	//Controlla se la tabella non contiene l'elemento
        	return false;
        
        int bucket = this.phf.hash(o.hashCode(), this.getCurrentCapacity()); //Ottiene il bucket dell'elemento dato
        
        Node<E> n=(Node<E>) this.table[bucket]; // Ottiene il nodo dalla posizione della tabella
        
        if(n != null) { //Se il nodo è diverso da null, allora punta a una lista
            while(n.next!=null) { //Scorre la lista cercando l'elemento dato
                if(n.next.item.equals(o)) //Se l'elemento successivo è quello dato, lo elimina
                    n.next = n.next.next;
                else
                    n = n.next; //Altrimenti, passa all'elemento successivo della lista
            }
        }
        //Decrementa dimensione tabella e Incrementa numero modifiche
        this.size--;
        this.modCount++;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // utilizzare un iteratore della collection e chiamare il metodo
        // contains
    	for(Object o : c) {
            if(!this.contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // utilizzare un iteratore della collection e chiamare il metodo add
    	for(E e:c)	//Scorre gli oggetti della collezione, aggiungendoli alla tabella
            this.add(e);
        
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // utilizzare un iteratore della collection e chiamare il metodo remove
    	
        if(c.contains(null))
        	throw new NullPointerException("Collezione nulla!");
        
        for(Object o:c) //corre gli oggetti della collezione rimuovendoli dalla tabella
            this.remove(o);
        
        return true;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {

    	private Iterator<E> flattenIterator = null;

        private int numeroModificheAtteso;

        private Itr() {
        	this.numeroModificheAtteso = modCount;
            ArrayList<E> flatten = new ArrayList<>();

            for(Object cell: table) {
                if(cell != null) {
                    Node<E> node = (Node<E>) cell;
                    do {
                        flatten.add(node.item);
                        node = node.next;
                    } while(node != null);
                }
            }

            this.flattenIterator = flatten.iterator();
        }

        @Override
        public boolean hasNext() {
        	return flattenIterator.hasNext();
        }

        @Override
        public E next() {
        	if(this.numeroModificheAtteso!=modCount) 
                throw new ConcurrentModificationException("Tabella modificata durante l'iterazione");
            
            if (!this.hasNext())
            	throw new NoSuchElementException("Non c'è un nodo successivo");
            
            return flattenIterator.next();
        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
