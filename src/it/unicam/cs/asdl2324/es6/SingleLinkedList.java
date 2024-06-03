package it.unicam.cs.asdl2324.es6;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentMopdificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. E' dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */
    private static class Node<E> {
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     * 
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return SingleLinkedList.this.head != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != SingleLinkedList.this.numeroModifiche) {
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException(
                        "Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = SingleLinkedList.this.head;
                return SingleLinkedList.this.head.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }

    }

    /*
     * Una lista concatenata è uguale a un'altra lista se questa è una lista
     * concatenata e contiene gli stessi elementi nello stesso ordine.
     * 
     * Si noti che si poteva anche ridefinire il metodo equals in modo da
     * accettare qualsiasi oggetto che implementi List<E> senza richiedere che
     * sia un oggetto di questa classe:
     * 
     * obj instanceof List
     * 
     * In quel caso si può fare il cast a List<?>:
     * 
     * List<?> other = (List<?>) obj;
     * 
     * e usando l'iteratore si possono tranquillamente controllare tutti gli
     * elementi (come è stato fatto anche qui):
     * 
     * Iterator<E> thisIterator = this.iterator();
     * 
     * Iterator<?> otherIterator = other.iterator();
     * 
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof SingleLinkedList))
            return false;
        SingleLinkedList<?> other = (SingleLinkedList<?>) obj;
        // Controllo se entrambe liste vuote
        if (head == null) {
            if (other.head != null)
                return false;
            else
                return true;
        }
        // Liste non vuote, scorro gli elementi di entrambe
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della
     * lista.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        // implicitamente, col for-each, uso l'iterator di questa classe
        for (E e : this)
            hashCode = 31 * hashCode + e.hashCode();
        return hashCode;
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
        if(o==null)	//Se l'oggetto è vuoto lancio un eccezione
        	throw new NullPointerException("Oggetto Nullo!");
        Iterator<E> thisIterator = this.iterator();	//Creo una lista con gli stessi elementi
        while(thisIterator.hasNext()) {	//Finchè la lista ha un elemento successione
        	if(o.equals(thisIterator.next()))	//Se l'oggetto attuale è uguale a quello successione
        		return true;	//Ritorno true
        }
        	
        return false;	//Ritorno false altrimenti
    }

    @Override
    public boolean add(E e) {
    	if(e==null) //Se l'elemento è nullo lancio un eccezione
    		throw new NullPointerException("Elemento nullo!");
    	
        Node<E> newNode = new Node<E>(e, null);	//Credo un nuovo nodo, con l'elemento attuale e quello successivo nullo
        if(this.head==null) {	//Se la testa è nulla
            this.head = newNode;	//Assegno alla testa unnuovo nodo
            this.tail = head;	//Assegno alla coda la testa
        } else {	//Altrimenti
            this.tail.next = newNode;	//Assegno alla coda successiva il nuovo nodo
            this.tail = newNode;	//E asseggno alla coda il nuovo nodo
        }
        
        this.size++;	//Incremento la dimensione
        this.numeroModifiche++;	//Incremento il contatore delle modifiche
        return true;	//Ritorno il flag true per la corretta aggiunta
    }

    @Override
    public boolean remove(Object o) {
    	if(o==null) //Se l'oggetto è vuoto lancio un eccezione
    		throw new NullPointerException("Oggetto nullo!");
        if(!this.contains(o)) 	//Se l'elemento non è contenuto ritorno falso
        	return false;
        if(this.size == 1) {	//SE la fimensione è 1
            head = null;	//Assegno alla testa null
            this.size--;	//E diminuisco la dimensione
            return true;	//Ritorno la corretta cancellazione
        } else {	//Altrimenti
            Node<E> currentNode = head;	//Credo un nuovo nodo con la testa
            while (currentNode.next != null) {	//FInchè non si arriva ad un nodo nullo (fine)
                if (currentNode.next.item.equals(o)) {	//USE il nodo successione è uguale al nodo da cancellare
                    currentNode.next = currentNode.next.next;	//Faccio lo shift indietro di tutti i nodi successivi
                    this.size--;	//Diminuisco la dimensione e ritorno true
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;	//ALtrimenti se non avviene nessuna operazione ritorno falso
    }

    @Override
    public void clear() {
    	this.head = null;	//Cancello la testa
        this.tail = null;	//Cancello la coda
        this.numeroModifiche++;	//Aumento il numero di modifiche
        this.size = 0;	//DImensione 0
    }

    @Override
    public E get(int index) {
    	if((index<0) || (index>size-1))	//Se l'indice è minore di 1 o maggiore della dimensione 
    		throw new IndexOutOfBoundsException("");	//Lancio un eccezione
        if(index==0) {	//Se l'indice è uguale a 1
            if(head!=null) 	//Se la testa non è nulla
            	return head.item;	//Ritorno l'elemento dela testa
            else 
            	throw new IndexOutOfBoundsException("");
        }
        if(index==size-1) {	//SE l'indice è nella penultima posizione
            if(tail!=null)	//E se la coda non è nulla
            	return tail.item;	//Ritorno l'elemento della code
            else 	//Altrimenti lancio un eccezione
            	throw new IndexOutOfBoundsException("");
        }
        
        Iterator<E> thisIterator = this.iterator();	//Creo una lista
        int currentIndex = 0;	//COn indice 0
        E item = thisIterator.next();	//E l'elemento
        while(currentIndex!=index) {	//Ciclo finchè l'indice è diverso
        	item = thisIterator.next();	//Assegno all'elemento quello successione
            currentIndex++;	//Incremento l'indice
        }
        return item;	//Ritorno l'elemento
    }

    @Override
    public E set(int index, E element) {
    	if((index>this.size-1) || (index<0))	//SE l'indice è magigore della penultima posizione o minore di 0
    		throw new IndexOutOfBoundsException();	//Lancio un eccezione
        if(element==null) //Se l'elemento è nullo lancio un eccezione
        	throw new NullPointerException("Elemento nullo!");
        
        Node<E> currentNode = head;	//Credo un nodo con la testa
        int i=0;	//Credo un indice
        while (currentNode.next!=null) {	//FInchè il nodo successivo non è nullo
            if(i==index) {	//Se l'indice è uguale all'indec
                E oldItem = currentNode.item;	//Salvo l'item
                currentNode.item = element;	//Assegno al l'item l'elemento
                return oldItem;	//Ritorno l'item salvato
            }
            i++;	//Incremento l'indice
            currentNode = currentNode.next;	
        }
        return null;
    }

    @Override
    public void add(int index, E element) {
    	if(element==null)	//Se l'elemento è nullo lancio un eccezione
    		throw new NullPointerException();
        // Se l'indice è 0, aggiungoin testa
        if(index==0) {
            head = new Node<E>(element, head.next);
            this.size++;
            return;
        }
        // Se l'indice è uguale alla lunghezza della lista, aggiungo in coda
        if(index==this.size) {
            this.add(element);
            return;
        }
        // Altrimenti l'elemento va inserito da qualche parte nel mezzo, quindi controlla validità dell'indice
        if((index<0) || (index>this.size-1))
        	throw new IndexOutOfBoundsException();
        
        // Scorro la lista fino alla coda
        int i = 0;
        Node<E> currentNode = head;
        while (currentNode.next!=null) {
            // Se il prossimo nodo ha l'indice che cerchiamo
            if(i+1==index) {
                // Inserisco il nuovo nodo e shifto il prossimo nodo di una posizione
                Node<E> nodeToShift = currentNode.next;
                currentNode.next = new Node<E>(element, nodeToShift);
                this.size++;
                return;
            }
            i++;
            currentNode = currentNode.next;
        }
        return;
    }

    @Override
    public E remove(int index) {
    	if ((index<0) || (index>=this.size))	//Se l'indice non è valido lancio un eccezione
            throw new IndexOutOfBoundsException();
    	
        Node<E> previous = null;
        Node<E> currentNode = this.head;
        int count = 0;
        
        while (count<index) {	//Contatore
            count++;
            previous = currentNode;
            currentNode = currentNode.next;
        }
        if (previous==null) {
            if (currentNode.next==null) {  // l'elemento in testa è anche in coda, la lista viene svuotata
                this.head = null;
                this.tail = null;
            } else {	// l'elemento in testa ha almeno un elemento successivo
                this.head = currentNode.next;
            }
        } else {	//ELiminando un elemento non in testa
            if (currentNode.next == null) {	//Ultima posizione
                previous.next = null;
                this.tail = previous;
            } else {	//Elemento al centro
                previous.next = currentNode.next;
            }
        }
        
        this.size--;	//Diminuisco la size
        this.numeroModifiche++;	//Icremento il numero di modifiche
        return currentNode.item;	//Ritorno l'elemento
    }

    @Override
    public int indexOf(Object o) {
    	
    	if(!contains(o))	//Se l'elemento non è contenuto ritrno -1
    		return -1;
        int i = 0;	//Indice
        
        Node<E> currentNode = head;	//Testa del nodo attuale
        while (currentNode.next!=null) {	//Finchè non si arriva alla ifne
            if(o.equals(currentNode.item)) {	//Se l'il nodo corrisponde
                return i;	//Ritorno la posizione
            }
            i++;	//Incremento la posizione
            currentNode = currentNode.next;	//Nodo successivo
        }
        return i;	//RItorno l'indice (se non è presente viene ritornato il numero di cicli
    }

    @Override
    public int lastIndexOf(Object o) {
    	if(o==null)
    		throw new NullPointerException();
        if(!contains(o))
        	return -1;
        int i = 0;
        int index = 0;
        Node<E> currentNode = head;
        while (currentNode!=null) {
            if(o.equals(currentNode.item)) index = i;
            i++;
            currentNode = currentNode.next;
        }
        return index;
    }

    @Override
    public Object[] toArray() {
    	Object[] array = new Object[size];
        Iterator<E> thisIterator = this.iterator();
        for (int i=0; i<size; i++) {
            array[i] = thisIterator.next();
        }
        return array;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
