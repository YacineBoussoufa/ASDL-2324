package it.unicam.cs.asdl2324.mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template)
 * 		   Yacine Boussoufa yacine.boussoufa@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
    	this.currentElements = new HashMap<E, Node<E>>();	//Costruzione HashMap
    }

    @Override
    public boolean isPresent(E e) {
        if(e==null)	//Se l'elemento è nullo lancio un eccezione
            throw new NullPointerException("Elemento nullo!");

        return this.currentElements.containsKey(e);	//Altrimenti ritorno true o false se è contenuto
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        if(e==null)	//Se l'elemento è nullo lancio un eccezione
            throw new NullPointerException("Elemento nullo!");
        else if(this.currentElements.get(e)!=null)	//Se l'elemento esiste lancio un eccezione
            throw new IllegalArgumentException("Elemento già presente!");

        this.currentElements.put(e,new Node<>(e));	//Aggiungo il nodo di rango zero
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        if(e==null) //Se l'elemento è nullo lancio un eccezione
            throw new NullPointerException("Elemento nullo!");
        else if(!isPresent(e))	//Se non è presente ritorno null
            return null;
        
        if (!this.currentElements.get(e).equals(this.currentElements.get(e).parent)) //Se il parent non è se stesso chiamo ricorsivamente
        	this.currentElements.get(e).parent=currentElements.get(findSet(this.currentElements.get(e).parent.item));	//Trovo il rappresentante

        return this.currentElements.get(e).parent.item;	//Ritorno il rappresentante
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        if((e1==null)||(e2==null))	//Se gli elementi sono nulli lancio un eccezioe
            throw new NullPointerException("Uno dei due nodi è nullo!");
        else if((!isPresent(e1))||(!isPresent(e2)))	//Se uno degli elementi non è presente lancio un eccezione
            throw new IllegalArgumentException("Uno dei due nodi non è presente!");
        
        Node<E> rep1 = this.currentElements.get(findSet(e1));	//Nodo 1
        Node<E> rep2 = this.currentElements.get(findSet(e2));	//Nodo 2
        
        if(rep1==rep2)	//Se i due nodi rapps. sono identici
        	return;		//Non faccio nulla
        else if(rep1.rank>rep2.rank)	//Se il rango del primo è maggiore
            rep2.parent=rep1;	//Il primo diventa parent del secondo
        else{
            rep1.parent=rep2;	//Altrimenti l'opposto
            if(rep1.rank==rep2.rank)	//Se il rango è identico
                rep2.rank++;	//Lo incremento
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        Set<E> tmp=new HashSet<>();	//Creo un hashset di appoggio
        
        for(E element:this.currentElements.keySet())	//Scorro gli elementi
            if(findSet(element).equals(element))	//Se l'elemento è presente
                tmp.add(element);					//Lo aggiungo all'hahset
        
        return tmp;		//Ritorno l'hashset di appoggio
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
    	if(e==null)	//Se gli elementi sono nulli lancio un eccezioe
            throw new NullPointerException("Nodo nullo!");
        else if(!isPresent(e))	//Se l' elemento non è presente lancio un eccezione
            throw new IllegalArgumentException("L'elemento non è presente!");
    	
        //Set di ritorno
        Set<E> tmp=new HashSet<>();	//Creo un hashset di appoggio

        for(E element:currentElements.keySet())	//Scorro gli elementi
            if(findSet(e).equals(findSet(element)))	//Se l'elemento è presente
                tmp.add(element);	//Lo aggiungo all'hahset

        return tmp;	//Ritorno l'hashset di appoggio
    }

    @Override
    public void clear() {
    	this.currentElements=new HashMap<E, Node<E>>();	//Reinizializzo per cancellare tutto
    }

}
