package it.unicam.cs.asdl2324.es8;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Un oggetto di questa classe rappresenta un binary search tree, cioè un albero
 * binario di ricerca, realizzato tramite nodi ricorsivi (rappresentati da
 * oggetti della classe interna RecBST). Le API pubbliche chiamano i
 * corrispondenti metodi ricorsivi sul nodo RecBST che attualmente è la radice.
 * Questa classe non accetta elementi null e non accetta elementi duplicati.
 * 
 * Il binary search tree rappresentato da un oggetto di questa classe può essere
 * anche vuoto, mentre un oggetto della classe RecBST non può rappresentare un
 * albero vuoto: in quel caso l'albero vuoto è rappresentato dal fatto che il
 * puntatore all'oggetto RecBST è null, cioè dal fatto che l'oggetto non esiste.
 * 
 * La complessità delle operazioni di ricerca, inserimento e cancellazione nel
 * caso pessimo sono O(h) dove h è l'altezza dell'albero. Questa classe non
 * esegue un autobilanciamento dell'altezza, quindi nei casi degeneri la
 * complessità delle operazioni può diventare O(n) dove n è il numero degli
 * elementi presenti.
 * 
 * @param E
 *              il tipo delle etichette dei nodi in questo Binary Search Tree.
 *              La classe {@code E} deve avere un ordinamento naturale definito
 *              tra gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class BinarySearchTree<E extends Comparable<E>> {

    /*
     * Puntatore all'attuale nodo radice dell'albero, se null allora l'albero è
     * vuoto
     */
    private RecBST root;

    /*
     * Numero di nodi attualmente presenti in questo albero
     */
    private int size;

    /**
     * Crea un albero binario di ricerca vuoto.
     */
    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Costruisce un albero contenente solo un nodo radice/foglia.
     * 
     * @param label
     *                  etichetta del nodo radice/foglia
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public BinarySearchTree(E label) {
        if (label == null)
            throw new NullPointerException("Etichetta della radice null");
        this.root = new RecBST(label);
        this.size = 1;
    }

    /**
     * Determina se questo albero è vuoto.
     * 
     * @return true se questo albero è vuoto, false altrimenti
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Determina il numero di nodi in questo albero.
     * 
     * @return il numero di nodi in questo albero
     */
    public int size() {
        return this.size;
    }

    /**
     * Cancella tutti i nodi di questo albero, che quindi diventa vuoto.
     */
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Restituisce l'altezza di questo albero. L'altezza è definita come la
     * massima lunghezza di un percorso dal nodo radice a un nodo foglia in
     * questo albero. L'altezza dell'albero vuoto è -1, l'altezza dell'albero
     * con una radice/foglia è 0, e così via.
     * 
     * @return l'altezza di questo albero oppure -1 se questo albero è vuoto.
     */
    public int getHeight() {
        if (this.isEmpty())
            return -1;
        return this.root.computeHeight();
    }

    /**
     * Aggiunge ad una lista data la lista delle etichette dei nodi dell'albero
     * nell'ordine naturale.
     * 
     * @param l
     *              una lista (può essere anche vuota) su cui inserire le
     *              etichette in ordine.
     * @throws NullPointerException
     *                                  se la lista passata è null
     */
    public void addOrderedLabelsTo(List<E> l) {
        if (l == null)
            throw new NullPointerException(
                    "Lista null su cui aggiungere le etichette");
        if (this.isEmpty())
            return;
        this.root.addLabelsInOrder(l);
    }

    /**
     * Restituisce la lista ordinata delle etichette dei nodi di questo albero
     * secondo l'ordinamento naturale della classe {@code E}.
     * 
     * @return la lista ordinata delle etichette dei nodi di questo albero
     *         secondo l'ordinamento naturale della classe {@code E}
     */
    public List<E> getOrderedLabels() {
        if (this.isEmpty())
            return new ArrayList<E>();
        return this.root.inOrderVisit();
    }

    /**
     * Cerca un certo nodo in questo albero che ha una etichetta data.
     * 
     * @param label
     *                  l'etichetta da cercare
     * @return true se l'etichetta è presente, false altrimenti
     * 
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public boolean contains(E label) {
        if (label == null)
            throw new NullPointerException("Etichetta da cercare null");
        if (this.isEmpty())
            return false;
        RecBST n = this.root.search(label);
        if (n == null)
            return false;
        else
            return true;
    }

    /**
     * Restituisce l'etichetta più piccola, in base all'ordinamento naturale
     * della classe {@code E}, presente nell'albero.
     * 
     * @return l'etichetta minima presente nell'albero oppure null se l'albero è
     *         vuoto
     */
    public E getMin() {
        if (this.isEmpty())
            return null;
        return this.root.getMinNode().getLabel();
    }

    /**
     * Restituisce l'etichetta più grande, in base all'ordinamento naturale
     * della classe {@code E}, presente nell'albero.
     * 
     * @return l'etichetta massima presente nell'albero oppure null se l'albero
     *         è vuoto
     */
    public E getMax() {
        if (this.isEmpty())
            return null;
        return this.root.getMaxNode().getLabel();
    }

    /**
     * Restituisce l'etichetta successiva a una etichetta data secondo l'ordine
     * canonico della classe E.
     * 
     * @param label
     *                  l'etichetta di cui trovare il successore
     * 
     * @return l'etichetta successore di {@code label} in questo albero, oppure
     *         null se {@code label} non ha un successore
     * @throws IllegalArgumentException
     *                                      se l'etichetta {@code label} non è
     *                                      presente in questo albero
     * @throws NullPointerException
     *                                      se l'etichetta passata è null
     */
    public E getSuccessor(E label) {
        if (label == null)
            throw new NullPointerException(
                    "Etichetta di cui cercare il successore null");
        if (this.isEmpty())
            throw new IllegalArgumentException(
                    "Tentativo di cercare il successore di una etichetta non esistente");
        RecBST n = this.root.search(label);
        if (n == null)
            throw new IllegalArgumentException(
                    "Tentativo di cercare il successore di una etichetta non esistente");
        RecBST succ = n.getSuccessorNode();
        if (succ == null)
            return null;
        else
            return succ.getLabel();
    }

    /**
     * Restituisce l'etichetta precedente a una etichetta data secondo l'ordine
     * canonico della classe E.
     * 
     * @param label
     *                  l'etichetta di cui trovare il predecessore
     * 
     * @return l'etichetta predecessore di {@code label} in questo albero,
     *         oppure null se {@code label} non ha un predecessore
     * @throws IllegalArgumentException
     *                                      se l'etichetta {@code label} non è
     *                                      presente in questo albero
     * @throws NullPointerException
     *                                      se l'etichetta passata è null
     */
    public E getPredecessor(E label) {
        if (label == null)
            throw new NullPointerException(
                    "Etichetta di cui cercare il predecessore null");
        if (this.isEmpty())
            throw new IllegalArgumentException(
                    "Tentativo di cercare il predecessore di una etichetta non esistente");
        RecBST n = this.root.search(label);
        if (n == null)
            throw new IllegalArgumentException(
                    "Tentativo di cercare il predecessore di una etichetta non esistente");
        RecBST pred = n.getPredecessorNode();
        if (pred == null)
            return null;
        else
            return pred.getLabel();
    }

    /**
     * Aggiunge un nodo a questo albero con una etichetta specificata.
     * 
     * @param label
     *                  etichetta da inserire
     * 
     * @return true se il nodo è stato effettivamente inserito, false se
     *         l'etichetta era già presente
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public boolean add(E label) {
        if (label == null)
            throw new NullPointerException("Etichetta da aggiungere null");
        if (this.isEmpty()) {
            // aggiunge la radice
            this.root = new RecBST(label);
            this.size = 1;
            return true;
        } // chiama il corrispondente metodo sulla radice
        else {
            boolean result = this.root.insert(label);
            if (result)
                this.size++;
            return result;
        }
    }

    /**
     * Rimuove da questo albero il nodo contenente una certa etichetta.
     * 
     * @param label
     *                  l'etichetta del nodo da rimuovere
     * @return true se il nodo è stato rimosso, false se l'etichetta non era
     *         presente
     * @throws NullPointerException
     *                                  se l'etichetta passata è null
     */
    public boolean remove(E label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere etichetta null");
        if (this.isEmpty())
            // etichetta sicuramente non presente in un albero vuoto
            return false;
        RecBST n = this.root.search(label);
        if (n == null)
            // etichetta non presente
            return false;
        n.deleteSelfLabel();
        this.size--;
        return true;
    }

    /**
     * Just for JUnit testing purposes.
     * 
     * @return the RecBST node corresponding to the root of this binary search
     *         tree.
     */
    protected RecBST getRoot() {
        return this.root;
    }

    /*
     * Classe interna che implementa tutti i metodi ricorsivamente e in cui ogni
     * nodo è un (sotto-)albero. Lo specificatore è protected solamente per
     * permettere i test JUnit.
     */
    protected class RecBST {
        /*
         * Etichetta associata al nodo
         */
        private E label;

        /*
         * Sottoalbero sinistro, se non presente vale null
         */
        private RecBST left;

        /*
         * Sottoalbero destro, se non presente vale null
         */
        private RecBST right;

        /*
         * Genitore di questo (sotto-)albero, può essere null quando questo nodo
         * è la radice dell'albero rappresentato dall'oggetto della classe
         * principale.
         */
        private RecBST parent;

        /*
         * Costruisce un (sotto-)albero che contiene solo la radice/foglia.
         * 
         * @param label etichetta da associare al nodo
         */
        protected RecBST(E label) {
            this.label = label;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        /*
         * Costruisce un (sotto-albero) a partire da un nodo, due sotto-alberi e
         * un nodo genitore.
         * 
         * @param label etichetta da associare al nodo
         * 
         * @param aLeft sotto-albero sinistro, può essere null
         * 
         * @param aRight sotto-albero destro, può essere null
         * 
         * @param aParent nodo genitore, può essere null
         */
        protected RecBST(E label, RecBST aLeft, RecBST aRight, RecBST aParent) {
            this.label = label;
            this.left = aLeft;
            this.right = aRight;
            this.parent = aParent;
        }

        /**
         * @return the label
         */
        protected E getLabel() {
            return this.label;
        }

        /**
         * @param label
         *                  the label to set
         */
        protected void setLabel(E label) {
            this.label = label;
        }

        /**
         * @return the left
         */
        protected RecBST getLeft() {
            return left;
        }

        /**
         * @param left
         *                 the left to set
         */
        protected void setLeft(RecBST left) {
            this.left = left;
        }

        /**
         * @return the right
         */
        protected RecBST getRight() {
            return right;
        }

        /**
         * @param right
         *                  the right to set
         */
        protected void setRight(RecBST right) {
            this.right = right;
        }

        /**
         * @return the parent
         */
        protected RecBST getParent() {
            return parent;
        }

        /**
         * @param parent
         *                   the parent to set
         */
        protected void setParent(RecBST parent) {
            this.parent = parent;
        }

        /*
         * Restituisce l'altezza di questo nodo.
         * 
         * @return la lunghezza del massimo cammino da questo nodo a una foglia.
         */ 
        protected int computeHeight() {
        	if((this.left==null) && (this.right==null))	//Non ci sono nodi figli: radice
                return 0;
        	else if(this.left==null)	//C'è almeno un nodo figlio a destra
        		return 1+this.right.computeHeight();
        	else if(this.right==null)	//C'è almeno un nodo figlio a sinistra
        		return 1+this.left.computeHeight();
        	else	//Se ci sono sia figli a destra che sinistra calcolo il nodo massimo
        		return 1+Math.max(this.right.computeHeight(),this.left.computeHeight());
        	
        }

        /*
         * Aggiunge un nodo a questo (sotto-)albero con una etichetta
         * specificata.
         * 
         * @param label etichetta da inserire
         * 
         * @return true se il nodo è stato effettivamente inserito, false se
         * l'etichetta era già presente.
         */
        protected boolean insert(E label) {        
            int x = this.label.compareTo(label);	//Confronto con l'etichetta attuale
         
            if(x==0)	// L'elemento è già presente nel nodo
                return false;
            else if(x<0) {	//L'elemento va inserito nel sotto-albero destro
            	if (this.right==null) {	 //Inserisco l'elemento come sottoalbero destro
                    this.right=new RecBST(label);
                    this.right.setParent(this);
                    return true;
                } else
                    return this.right.insert(label);
            }
            else if(x>0) {
            	if (this.left==null) {	 //Inserisco l'elemento come sottoalbero destro
                    this.left=new RecBST(label);
                    this.left.setParent(this);
                    return true;
                } else
                    return this.left.insert(label);
            }
            
            return false;
        }

        /*
         * Cerca un nodo con una certa etichetta in questo albero.
         * 
         * @param label l'etichetta da cercare
         * 
         * @return il puntatore al nodo che contiene l'etichetta cercata, oppure
         * null se l'etichetta non è presente
         */
        protected RecBST search(E label) {
        	
        	if((this.left==null) && (this.right==null))	//Se non ci sono nodi sono nella radice
                if(this.label.equals(label))	//Se l'etichetta corrisponde ritorno il puntatore
                    return this;
                else	//Altrimenti ritorno null perchè non presente
                    return null;
        	
        	int x = this.label.compareTo(label);	//Confronto con l'etichetta attuale
        	
        	if(x==0)	// L'elemento è già presente nel nodo
                return this;
            else if(x<0) {	//L'elemento va cercato nel sotto-albero destro
            	if (this.right==null) 	 //Verifico a Destra
                    return this;
                else
                    return null;
        	}
            else if(x>0) {	//L'elemento va cercato nel sotto-albero sinistro
            	if (this.left==null) //Verifico a Sinistra
            		return this;
        		else
                    return null;
        	}
        	
            return null;
        }

        /*
         * Aggiunge ad una lista data le etichette dei nodi di questo
         * (sotto-)albero nell'ordine naturale. Per far questo esegue una visita
         * in-order di questo (sotto-)albero.
         * 
         * @param l una lista (può essere anche vuota) su cui inserire le
         * etichette in ordine
         */
        protected void addLabelsInOrder(List<E> l) {

        	if(this.left!=null) {	//Esploro prima il sottoalbero sinistro
        	    this.left.addLabelsInOrder(l);
        	}
        	
        	l.add(this.label);	//Poi quello corrente
        	
        	if(this.right != null) {	//Infine quello destro
        	    this.right.addLabelsInOrder(l);
        	}
        }

        /*
         * Restituisce la lista ordinata delle etichette dei nodi di questo
         * (sotto-)albero secondo l'ordinamento naturale della classe {@code E}.
         * Per ottenere il risultato fa una visita in-order.
         * 
         * @return la lista ordinata delle etichette dei nodi di questo
         * (sotto-)albero secondo l'ordinamento naturale della classe {@code E}
         */
        protected List<E> inOrderVisit() {
        	List<E> l = null;	//Dichiaro una lista temporanea vuota
            
            if(this.left!=null)	//Se ci sono elementi a sinistra 
                l = this.left.inOrderVisit();	//Ordino ricorsivamente la parte sinistra
            else //Se non ci sono elementi
                l = new ArrayList<E>();	//Creo la lista
            
            l.add(this.label);	//Aggiungo il valore attuale
            
            if(this.right!=null)	//Se la lista contiene un elemento a destra
                l.addAll(this.right.inOrderVisit());	//Aggiungo e Ordino ricorsivamente la parte Destra
            
            return l;	//Ritorno la lista
        }

        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta più piccola
         * presente in questo (sotto-)albero.
         * 
         * @return il nodo più a sinistra che non ha un sotto-albero sinistro in
         * questo (sotto-)albero
         */
        protected RecBST getMinNode() {
        	if(this.left==null)	//Il nodo con l'etichetta più piccola è il nodo più a sinistra dell'albero
                return this;
            else	//Se il nodo corrente ha un figlio sinistro, 
                return this.left.getMinNode(); //allora il nodo con l'etichetta più piccola è nel sotto-albero sinistro del nodo corrente
        }

        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta più grande
         * presente in questo (sotto-)albero.
         * 
         * @return il nodo più a destra che non ha un sottoalbero destro in
         * questo (sotto-)albero
         */
        protected RecBST getMaxNode() {
            if(this.right==null) //Il nodo con l'etichetta più grande è il nodo più a destra dell'albero
                return this;
            else	//Se il nodo corrente ha un figlio destro,
                return this.right.getMaxNode();	//allora il nodo con l'etichetta più grande è nel sotto-albero destro del nodo corrente
        }


        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta successiva
         * all'etichetta di questo nodo secondo l'ordine canonico della classe
         * E.
         * 
         * @return il puntatore al nodo successore oppure null se questo nodo
         * non ha successore
         */
        protected RecBST getSuccessorNode() {
        	
        	if (this.right!=null)	//Se non c'è un nodo figlio a destra
                return this.right.getMinNode();	//Ritorno l'elemento minimo a destra
        	
        	Set<E> padre = new HashSet<E>();	//Il nodo viene considerato padre di se stesso
        	padre.add(this.label);
        	RecBST p = this;
        	
            while (p.parent!=null) {	//Ciclo finchè non c'è un nodo nullo (fine albero)
                if ((p.parent.left!=null) && (padre.contains(p.parent.left.label)))	//Se c'è un nodo a sinistra, e il nodo è il padre
                	return p.parent;	//Il padre è il successivo
                padre.add(p.parent.label);	//Altrimenti continuo la ricerca assegnando al padre il valore del nodo
                p = p.parent;
            }
        	return null;	//Altrimenti non c'è un successivo
        }

        /*
         * Restituisce il puntatore al nodo che contiene l'etichetta precedente
         * all'etichetta di questo nodo secondo l'ordine canonico della classe
         * E.
         * 
         * @return il puntatore al nodo predecessore oppure null se questo nodo
         * non ha predecessore
         */
        protected RecBST getPredecessorNode() {
            
        	if(this.left!=null)	//Se non c'è un nodo figlio a sinistra
                return this.left.getMaxNode();	//Ritorno l'elemente massino a sinistra
        	
        	Set<E> padre = new HashSet<E>();	//Il nodo viene considerato padre di se stesso
        	padre.add(this.label);
        	RecBST p = this;
        	
            while (p.parent!=null) {	//Ciclo finchè non c'è un nodo nullo	(fine albero)
                if ((p.parent.right!=null) && (padre.contains(p.parent.right.label))) //Se c'è un nodo a destra, e il nodo è il padre
                	return p.parent;	//Il padre è il precedente
                padre.add(p.parent.label);	//Altrimenti continuo la ricerca assegnando al padre il valore del nodo
                p = p.parent;
            }
        	
            return null;	//Altrimenti non c'è un precendete
        }

        /*
         * Cancella l'etichetta di questo nodo dall'albero. Potrebbe non
         * eliminare proprio questo nodo, ma un'altro nodo, copiando l'etichetta
         * di quel nodo cancellato (scollegandolo dal parent) in questo nodo.
         * Cfr. slides di teoria.
         */
        protected void deleteSelfLabel() {
        	// TODO implementare
        	  if (this.left == null && this.right == null) {	//Se questo nodo è una foglia, lo si elimina
        	    if (this.parent != null) {	//Se questo nodo ha un genitore
        	      if (this.parent.left == this) {	//Se questo nodo è il figlio sinistro del suo genitore
        	        this.parent.left = null;	//Se lo è, imposta il figlio sinistro del genitore a null
        	      } else { //Altrimenti, imposta il figlio destro del genitore a null
        	        this.parent.right = null;
        	      }
        	    }
        	  }
        	  else if (this.left != null) { //Se questo nodo ha un figlio sinistro
        	    this.label = this.left.label;	//Aggiorna l'etichetta di questo nodo con l'etichetta del figlio sinistro
        	    this.left = this.left.left;	//Rimuove il figlio sinistro dall'albero
        	  }
        	  else if (this.right != null) {	//Se questo nodo ha un figlio destro
        		  this.label = this.right.label;	// Aggiorna l'etichetta di questo nodo con l'etichetta del figlio destro   
        		  this.right = this.right.right;	// Rimuove il figlio destro dall'albero
        	  }
        }
    }
}
