import x10.lang.*;
/**
 * N-queens problem
 *
 *
 * @author kemal, vj 12/2004
 */




/**
 * Load balancer class.
 */
class loadBalancer {
	/**
	 * find an appropriate place to ship a task
	 */
	public static place choosePlace() {
		// not yet implemented 
		return here;
	}
}

/**
 * Helper class. Generic boxed<int> does not seem to work. 
 */
class BoxedInt {
	public int val;
	BoxedInt(int x) {
		val=x;
	}
}


public value class QueensList {
   // Operations and data structures for a list of queen coordinates

   // Since QueensList is an immutable value class, 
   // its instances do not have a fixed place.
   // The instances are passed from place to place
   // freely, allowing activities operating on the instances
   // to be load-balanced dynamically.

   /**
    * nullary constructor: create an empty value array
    */
   public QueensList() {
       q= new int value[(0:-1)->here];
   }

   /**
    * Constructor creates oldList with newItem added at end
    */
   public QueensList(final int value [.] oldList, final int newItem) {
       final int n= length(oldList);
       q= new int value[(0:n)->here] 
          (point [j]) {return j<n? oldList[j]: newItem;};
   }

   // Number of solutions
   const BoxedInt nSolutions=new BoxedInt(0);
   
   // Length of one side of chess board
   const int N=5;

   // Expected number of solutions for each N
   const int[] expectedSolutions =
     { 0, 1, 0, 0, 2, 10, 4, 40, 92, 352, 724, 2680, 14200,
                   73712, 365596, 2279184, 14772512 };

   // Let n=q.length, then
   // q[0],q[1],...,q[n-1] are the columns in which queens were placed
   // for rows 0,1,...,n-1, respectively.
   // I.e., (0,q[0]) (1.q[1]) ... (n-1,q[n-1]) are the coordinates
   // of the queens that were already placed.
   private int value[.] q;

   /**
    * Return true iff one of the already placed queens in coordinates 
    * (0,q[0]) .... (n-1,q[n-1]) "attacks" a new queen at (n,j) 
    */
   public boolean attacks(int j) {

   //Exists(row k:0..n-1)(sameColumn(j,q[k])||sameDiagonal(<n,j>,<k,q[k]>))

       int n= length(q); // the next row to place a queen
       for(point [k]: 0:(n-1)) {
         if (j==q[k] ||                       // same column
             Math.abs(n-k)==Math.abs(j-q[k])) // same diagonal 
             return true; 
       }
       return false;
   }


  /** 
   * Method Q solves the N-queens problem with 
   * a brute-force tree search.
   *
   * If all N queens were already placed, atomically increment the
   * number of solutions, and return.
   *
   * Otherwise: given a list of queen positions placed in rows 
   * 0,...,n-1, 
   * for each column in row n (in parallel)
   * if a new queen can be placed in this column,
   * place the queen there, and spawn a new activity (in an 
   * appropriately chosen place) to recursively continue from 
   * this chess board configuration, with the next row n+1. 
   * After spawning all such activities, return.
   *
   */
  
   public void Q() {
       if (length(q)==N) {async(nSolutions.location) atomic nSolutions.val++; return;}
           
       foreach (point p[k]: 0:(N-1)) {//try all columns of next row
	       if (!attacks(k)) {
               async(loadBalancer.choosePlace())
                     {(new QueensList(q,k)).Q();};
           }
       }
   }
   /**
    * utility function length() for 
    * one dimensional value arrays.
    */
	public static int length(int value [.] q) {
	    if (q.region.size() == 0) return 0;
	    else return q.region.high()-q.region.low()+1;
	}
   /**
    * run the n-queens problem
    */
   public boolean run() {
       finish{this.Q();}
       System.out.println("Number of solutions for "+N+" queens: "+nSolutions.val);
       return nSolutions.val==expectedSolutions[N];
   }
   /**
    * main method
    */
   public static void main(String args[]) {
		boolean b= (new QueensList()).run();
		System.out.println("++++++ "+(b?"Test succeeded.":"Test failed."));
		System.exit(b?0:1);
   }

}

