package it.unicam.cs.asdl2324.es9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
    	if(list==null)	//Se la lista è vuota lancio un eccezione
    		throw new NullPointerException("Lista vuota!");
    	
    	this.heap=new ArrayList<E>();	//Costruisco un array list
    	for(E element:list)	//Aggiungo ogni elemento alla lista
    		this.insert(element);
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
    	if(el==null)	//se l'elemento è nullo lancio un eccezione
    		throw new NullPointerException("Elemento nullo!");
    	
    	this.heap.add(el);	//Aggiungo alla heap l'elemento
    	int i=this.heap.size()-1;	//Salvataggio posizione
    	
    	while(i>0 && this.heap.get(i).compareTo(this.heap.get(parentIndex(i-1)))>0) {
    		Collections.swap(heap, i, parentIndex(i-1));	//Scambio
    		i=parentIndex(i-1);	//Cambio indice
    	}
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return (2*i);	//Ritorno l'indice sx
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return (2*i+1);	//Ritorno l'indice dx
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        return (i/2);	//Ritorno l'indice del genitore
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        if(heap.size()==0)	//Se l'elemento non ha elementi
        	return null;	//Ritorno null
        else
        	return heap.get(0);	//Altrimenti ritorno il nodo massimo
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
    	if(heap==null || heap.size()==0)	//Se l'heap è nulla o vuota ritorno null
    		return null;
    	        
        E max = heap.get(0);	//Ottiene il primo elemento (il massimo)
        heap.set(0, heap.get(heap.size()-1));	//Sostituisce il primo elemento con l'ultimo
        
        heap.remove(heap.size()-1); //Rimuove l'ultimo elemento
        
        if(heap.size()==0)
        	return max;	//Se l'heap è vuoto, vuol dire che c'era solo un elemento
        
        this.heapify(1);	//Esegue heapify a partire dal primo livello dell'heap
        return max; //Ritorno il valore massimo
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        int leftNodeIndex=this.leftIndex(i)-1;	// Ottiene indici dei sottoalberi figli
        int rightNodeIndex=this.rightIndex(i)-1;
        int maximumValueIndex = 0;
        
        // Confronta nodo attuale con figlio sinistro
        if(leftNodeIndex < heap.size() && heap.get(leftNodeIndex).compareTo(heap.get(i-1))>0) {
            maximumValueIndex=leftNodeIndex;
        } else {
            maximumValueIndex=i-1;
        }
        
        // Confronta massimo attuale con figlio destro
        if(rightNodeIndex<heap.size() && heap.get(rightNodeIndex).compareTo(heap.get(maximumValueIndex))>0)
            maximumValueIndex=rightNodeIndex;
        
        
        // Se il massimo è diverso dal nodo attuale, li scambia
        if(maximumValueIndex!=(i-1)) {
            Collections.swap(heap, i-1, maximumValueIndex);
            // Richiama ricorsivamente heapify sul livello sottostante
            if(i+1<heap.size())
            	heapify(heap.indexOf(heap.get(i+1)));
        }
    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}
