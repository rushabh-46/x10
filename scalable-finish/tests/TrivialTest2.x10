package x10.lang;
import x10.util.*;
public class TrivialTest2 {
  
 
  public def f1():int{
	  return 2;
  }
  public def f2():int{
	  return 2;
  }
  public def f5():int{
	  return 2;
  }
  public def f4():int{
	  return 2;
  }
  public def f6():int{
	  return 2;
  }
  public def f3(x:Rail[int],y:Rail[int]):Rail[int]{
	  return x;
  }
  public def foo():void {
	  
	  f3([1,2,3],[1,2]);
	  finish{at(here){}}
	  async{
		  at(here){f1();}
		  async{
			  at(here){f2();}
		  }
	  }
	  finish{
		 at(here){f4();}
		 finish{
			 at(here){f5();}
		 }
	  }
	  at(here){
		  at(here){f6();}
	  }
	
	
  }

  
  
}


