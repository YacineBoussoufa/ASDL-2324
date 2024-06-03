/**
 * 
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.HashSet;

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei (template) 
 * 		   Yacine Boussoufa yacine.boussoufa@studenti.unicam.it (implementazione)
 *
 * 
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return this.nodesIndex.size();	//Restituisco il numero di nodi in questo grafo.
    }

    @Override
    public int edgeCount() {
    	int count=0;	//Dichiarazione ed inizializzazione del contatore

        for(int i=0;i<matrix.size();i++) {	//Ciclo nella matrice
            ArrayList<GraphEdge<L>> row=this.matrix.get(i);

            /* Ciclo da j=i in quanto essendo un arco non orientato,
             * contare dirrettamente "duplica" il risultato in quanto
             * viene contato sia l'arco da i a j che da j a i.
             */
            for(int j=i;j<row.size();j++)	//Ciclo negli archi
                if(row.get(j)!=null)	//Se l'elemento non è nullo 
                    count++; //incremento il contatore
        }
        
        return count;	//Ritorno il contatore
    }

    @Override
    public void clear() {
    	//Cancello i precedenti valori richiamando i costruttori
    	this.matrix=new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex=new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public boolean isDirected() {
    	//Il Grafo non può essere orientato .-.
        return false;	//Quindi ritorno false e basta
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if(node==null)	//Se l'elemento è nullo lancio un eccezione
        	throw new NullPointerException("Nodo nullo!");
        else if(this.nodesIndex.containsKey(node))	//Se il nodo è presente
                return false;	//Ritorno falso
        
        this.nodesIndex.put(node,nodeCount());	//Aggiungo il nodo alla mappa
 
        ArrayList<GraphEdge<L>> tmp=new ArrayList<>();
        for(int i=0;i<nodeCount()-1;i++) //Aggiungo una nuova riga alla matrice
        	tmp.add(null);	//Inizialmente tutta vuota
        this.matrix.add(tmp);	//Aggiungo
        
        
        for(int i=0; i<nodeCount();i++)	//Ciclo le righe
            this.matrix.get(i).add(null); //Inserisco la nuova colonna a ogni riga

        return true;	//Ritorno true
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
    	if(label==null)	//Se il valore è nullo lancio un eccezione
    		throw new NullPointerException("Label nullo!");
    	
    	//Creo un nuovo nodo con il label passato (richiamo il metodo precedente)
    	return this.addNode(new GraphNode<L>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
    	if(node==null)
    		throw new NullPointerException("Nodo Nullo!");
    	else if(!this.nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Node non presente!");
        
        Set<GraphNode<L>> tmp=nodesIndex.keySet(); //Set d'appoggio

        int removeValue=nodesIndex.get(node);  //Memorizzio value dopo la rimozione del nodo dal nodesIndex

        for(int i=0;i<nodeCount();i++)	//Ciclo nel grafo
            this.matrix.get(i).remove(removeValue);	//Rimuovo il nodo
        
        this.matrix.remove(removeValue);	//rimuobo il nodo

        this.nodesIndex.remove(node);	//Rimuovo il noto

        for(GraphNode<L> element:tmp)	//Ciclo i grafi
            if(this.nodesIndex.get(element)>removeValue) {	//Scorro all'indietro i valori
                nodesIndex.put(element,removeValue);
                removeValue++;
            }
            	
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
    	if(label==null)	//Se il label è nullo lancio un eccezione
    		throw new NullPointerException("Label nullo!");
    	
        removeNode(new GraphNode<>(label));	//Richiamo il metodo precedente con un label
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
    	if((i<0)||(i>this.matrix.size()-1))	//Se il nodo è nullo lancio un eccezione
    			throw new IndexOutOfBoundsException("Indice non valido!");
        
        this.removeNode(this.getNode(i));	//Rimuovo l'elemento di indice i
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
    	if(node==null)	//Se il nodo è nullo lancio un eccezione
            throw new NullPointerException("Nodo Nullo!");
    	
    	for(GraphNode<L> tmp:this.nodesIndex.keySet())	//Ciclo gli elementi
            if(tmp.equals(node))	//Se un nomo corrisponde
                return tmp;	//Ritorno il valore
    	
    	return null;	//Altrimenti ritorno null
    }

    @Override
    public GraphNode<L> getNode(L label) {
    	if(label==null)	//Se il nodo è nullo lancio un eccezione
            throw new NullPointerException("Nodo Nullo!");
    	
        return this.getNode(new GraphNode<L>(label));	//Effettuo l'operazione del metodo precendete con il label
    }

    @Override
    public GraphNode<L> getNode(int i) {
    	if((i<0)||(i>this.matrix.size()-1))	//Se inseirsco un valore non valido lancio un eccezione
            throw new IndexOutOfBoundsException("Indice non valido!");
    	
    	for(GraphNode<L> key:this.nodesIndex.keySet())	//Ciclo la lista dei nodi
            if(this.nodesIndex.get(key)==i)	//Se l'indice del nodo equivale ad i
                return this.getNode(key);	//Ritorno il valore del nodo
            
    	return null;	//Se il nodo non esiste ritorno null    	
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
    	if(node==null)
    		throw new NullPointerException("Nodo nullo!");
    	
    	if(this.nodesIndex.containsKey(node))	//Se il nodo è presente
                return this.nodesIndex.get(node);	//Ritorno l'indice
    	
    	throw new IllegalArgumentException("Nodo non presente!");	//Se non è presente lancio un eccezione
    }

    @Override
    public int getNodeIndexOf(L label) {
    	if(label==null)	//Se il nodo è nullo lancio un eccezione
            throw new NullPointerException("Label Nullo!");
    	else if(getNode(label)==null)
            throw new IllegalArgumentException("Nodo non presente!");
    	
    	return nodesIndex.get(getNode(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
    	if(edge==null)	//Se l'edge è nullo lancio un eccezione
    		throw new NullPointerException("Edge nullo!");
    	else if((getNode(edge.getNode1())==null)||(getNode(edge.getNode2())==null))	//Se uno dei due nodi è nullo
    		throw new IllegalArgumentException("Uno dei due nodi è mancante");	//Lancio un eccezione
    	else if((edge.isDirected() && !this.isDirected())||(!edge.isDirected() && this.isDirected()))
    		throw new IllegalArgumentException("Arco non ammesso!");	//Se l'arco è orientato lancio un eccezione
    	        
        int i1=this.nodesIndex.get(edge.getNode1());	//Salvo l'indice del primo edge
        int i2=this.nodesIndex.get(edge.getNode2());	//salvo l'indice del secondo edge
        
        if((this.matrix.get(i1).get(i2)!= null)&&(this.matrix.get(i2).get(i1)!=null))	//Se entrambi i nodi non osno null
            if((this.matrix.get(i1).get(i2).equals(edge))&&(this.matrix.get(i2).get(i1).equals(edge))) //E se corrispondono
                return false;	//Ritorno false e non aggiungo
        
        //Aggiungo l'arco alla matrice di adiacenza
        this.matrix.get(i1).remove(i2);
        this.matrix.get(i1).add(i2,edge);
        //Aggiungo l'altro arco
        this.matrix.get(i2).remove(i1);
        this.matrix.get(i2).add(i1,edge);
        
        return true;	//Ritorno true
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
    	if((node1==null)||(node2==null))	//Se i nodi sono nulli lancio un eccezione
    		throw new NullPointerException("Uno dei due nodi è nullo!");
    	//Richiamo il metodo precedente
    	return addEdge(new GraphEdge<>(node1,node2,false));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
            double weight) {
    	if((node1==null)||(node2==null)||(weight<0))	//Se i nodi o il peso sono nulli lancio un eccezione
    		throw new NullPointerException("Uno dei due nodi è nullo!");
    	//Ritorno l'aggiunta
    	return addEdge(new GraphEdge<>(node1,node2,false,weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
    	if((label1==null)||(label2==null))	//Se i nodi sono nulli lancio un eccezione
    		throw new NullPointerException("Uno dei due label è nullo!");
    	//Ritorno l'aggiunta
    	 return addEdge((new GraphEdge<>(new GraphNode<>(label1),new GraphNode<>(label2), false)));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
    	if((label1==null)||(label2==null)||(weight<0))	//Se i nodi o il peso sono nulli lancio un eccezione
    		throw new NullPointerException("Uno dei due label è nullo!");
    	//Ritorno l'aggiuntas
    	return addEdge((new GraphEdge<>(new GraphNode<>(label1),new GraphNode<>(label2),false,weight)));
    }

    @Override
    public boolean addEdge(int i, int j) {
    	if(((i<0)||(i>this.matrix.size()-1)||(j<0)||(j>this.matrix.size()-1)))	//Se l'indice non è valido lancio un eccezione
               throw new IndexOutOfBoundsException ("Indice non valido!");
    	else if((!this.nodesIndex.containsValue(i))|| (!this.nodesIndex.containsValue(j)))	//Se i nodi non esistono lancio un eccezione
    			throw new IndexOutOfBoundsException ("Nodi non presenti!");
    	
    	return addEdge(getNode(i), getNode(j));	//Richiamo i metodi precedenti
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
    	if (((i<0)||(i>this.matrix.size()-1)||(j<0)||(j>this.matrix.size()-1)||(weight<0)))	//Se l'indice non è valido lancio un eccezione
            throw new IndexOutOfBoundsException ("Indice non valido!");
    	else if((!this.nodesIndex.containsValue(i))|| (!this.nodesIndex.containsValue(j)))	//Se i nodi non esistono lancio un eccezione
 			throw new IndexOutOfBoundsException ("Nodi non presenti!");
    	
    	return addWeightedEdge(getNode(i),getNode(j),weight);	//Richiamo i metodi precedenti
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
    	if(edge==null)	//Se l'edge è nullo lancio un eccezione
    		throw new NullPointerException("Edge nullo!");
    	else if((getNode(edge.getNode1())==null)||(getNode(edge.getNode2())==null))	//Se uno dei due nodi è nullo
    		throw new IllegalArgumentException("Uno dei due nodi è mancante");	//Lancio un eccezione
    	
    	int edge1 = nodesIndex.get(edge.getNode1());	//Assegnazione dell'indice ad una variabile temporanea
        int edge2 = nodesIndex.get(edge.getNode2());	//Assegnazione dell'indice ad una variabile temporanea
        matrix.get(edge1).set(edge2, null);		//Rimozione
        matrix.get(edge2).set(edge1, null);		//Rimozione
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
    	if((node1==null)||(node2==null))	//Se uno dei due nodi è nullo
    		throw new NullPointerException("Uno dei due nodi è mancante");
    	else if(this.getEdge(node1,node2)==null)	//Se l'edge fra i due nodi non esiste lancio un eccezione
    		throw new IllegalArgumentException("Edge mancante!");
    	
    	this.removeEdge(new GraphEdge<L>(node1,node2,false));	//Richiamo Metodo precedente
    }

    @Override
    public void removeEdge(L label1, L label2) {
    	if((label1==null)||(label2==null))	//Se i label sono nulli lancio un eccezione
    		throw new NullPointerException("Uno dei due label è nullo!");
    	else if(getEdge(label1,label2)==null)	//Se l'edge fra i due nodi non esiste lancio un eccezione
             throw new IllegalArgumentException("Edge mancante!");
    	
    	this.removeEdge(new GraphNode<>(label1),new GraphNode<>(label2));	//Richiamo metodi precedenti
    }

    @Override
    public void removeEdge(int i, int j) {
    	 if((i<0)||(i>this.matrix.size()-1)||(j<0)||(j>this.matrix.size()-1))	//Se l'edge ha un indice mancante
    		 throw new IndexOutOfBoundsException("Indice non valido!");	//Lancio un eccezzione
    	
         this.matrix.get(i).set(j, null);	//Rimuobo l'edge
    	 this.matrix.get(j).set(i, null);	//Rimuovo l'edge
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
    	if(edge==null)	//Se l'edge è nullo lancio un eccezione
            throw new NullPointerException("Edge nullo!");
    	else if ((!nodesIndex.containsKey(edge.getNode1()))||(!nodesIndex.containsKey(edge.getNode2())))
             throw new IllegalArgumentException("Arco mancante!");
        
    	/* NON FUNGE .-.
        for(ArrayList<GraphEdge<L>> tmp:this.matrix)//Ciclo il Grafo
            for(GraphEdge<L> edge1:tmp)	//Per ogni edge
                if(edge.equals(edge1))	//se trovo l'edge
                    return edge;	//Lo ritorno
        */

        //Se l'edge è presente lo ritorno
        if(edge.equals(matrix.get(nodesIndex.get(edge.getNode1())).get(nodesIndex.get(edge.getNode2()))))
            return matrix.get(nodesIndex.get(edge.getNode1())).get(nodesIndex.get(edge.getNode2()));
        
        return null;	//Altrimenti ritorno null
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
    	if((node1==null)||(node2==null))	//Se i nodi sono nulli lancio un eccezione
    		throw new NullPointerException("Uno dei due nodi è nullo!");
    	else if((!nodesIndex.containsKey(node1)) || (!nodesIndex.containsKey(node2)))
    		throw new IllegalArgumentException("Il nodo non appartiene al grafo.");

    	return getEdge(new GraphEdge<L>(node1,node2,false));     	//Richiamo metodi precedenti
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
    	if((label1==null)||(label2==null))	//Se i nodi sono nulli lancio un eccezione
    		throw new NullPointerException("Uno dei due label è nullo!");
    	//Richiamo metodi precedenti  	
    	return getEdge(new GraphEdge<>(new GraphNode<>(label1),new GraphNode<>(label2),false));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
    	if((i<0)||(i>this.matrix.size()-1)||(j<0)||(j>this.matrix.size()-1))	//Se l'edge ha un indice mancante
    		throw new IndexOutOfBoundsException("Indice non valido!");	//Lancio un eccezzione
    	
    	return this.matrix.get(i).get(j);	//Richiamo metodi precedenti
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {  
        if(node==null)	//Se il nodp è nullo lancio un eccezione
            throw new NullPointerException("Nodo nullo!");
        else if(getNode(node)==null)	//Se il nodo non è presente lancio un eccezione
            throw new IllegalArgumentException("Node non presente!");
        
        Set<GraphNode<L>> tmp=new HashSet<>();	//Set temporaneo

        for(GraphNode<L> mapNode:getNodes())	//Ciclo nei nodis
            if(getEdge(node,mapNode)!=null)	//Aggiungo gli Edge adiacenti non nulli
                tmp.add(mapNode);
        
        return tmp;	//Ritorno il set
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
    	if (label==null)	//Se il label è nullo lancio un eccezione
            throw new NullPointerException("Label nullo!");
        
    	return getAdjacentNodesOf(new GraphNode<>(label));	//Ritorno il metodo precedente
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
    	if((i<0)||(i>this.matrix.size()-1))	//Se l'indice non è valido lancio un eccezione
    		throw new IndexOutOfBoundsException("Indice non valido!");
    	
    	return getAdjacentNodesOf(this.getNode(i));	//Richiamo metodo precedente
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
    	if(node==null)	//Se il nodo è nullo lancio un eccezione
            throw new NullPointerException("Node nullo!");
    	else if(getNode(node)==null)	//SE il nodo non è presente lancio un eccezione
            throw new IllegalArgumentException("Nodo non presente!");
    	
        Set<GraphEdge<L>> tmp=new HashSet<>();	//Set di appoggio

        for(GraphEdge<L> element:this.matrix.get(nodesIndex.get(node)))	//Ciclo i nodi
            if(element!=null)	//Se l'elemento non è nullo
                tmp.add(element);	//Lo aggiungo al set di appoggio
        
        return tmp;	//Ritorno il set di appoggio
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
    	if(label==null)	//Se il label è nullo lancio un eccezione
    		throw new NullPointerException("Lable nullo!");
    	
    	return getEdgesOf(new GraphNode<>(label));	//Ritorno il metodo precedente
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
    	if(((i<0)||(i>this.matrix.size()-1)))	//Lancio un eccezione sel'indice non è valido
            throw new IndexOutOfBoundsException("Indice non valido!");
        if(getNode(i)==null)	//Se il nodo è nullo lancio un eccezione
            throw new IndexOutOfBoundsException("Nodo non preente!");
        
        Set<GraphEdge<L>> tmp=new HashSet<>();	//Set di appoggio
        
        for(GraphNode<L> mapNode:getAdjacentNodesOf(i))	//Scorro i nodi aadiacenti
            tmp.add(getEdge(getNode(i), mapNode));	//Aggiungo l'edge
        
        return tmp;	//Ritorno il set di appoggio
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
    	Set<GraphEdge<L>> tmp=new HashSet<>();	//Set di appoggio
    	
        for (ArrayList<GraphEdge<L>> list:this.matrix)	//Ciclo
            for (GraphEdge<L> edge:list)	//Per ogni edge
                if (edge!=null)	//Se l'edge non è nullo
                   tmp.add(edge);	//Lo aggiungo al set  di appoggio
        
        return tmp;	//Ritorno il set di appoggio
    }
}