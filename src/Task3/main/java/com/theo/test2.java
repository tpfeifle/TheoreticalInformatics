package Task3.main.java.com.theo;

import java.util.HashSet;
import java.util.Set;

public class test2 {
	
	public static void main(String[] args) {
		State s0 = new State("s0");
		State s1 = new State("s1");
		State s2 = new State("s2");
		State s3 = new State("s3f");
		State s4 = new State("s4");
		State s5 = new State("s5f");
		
		Set<State> states = new HashSet<>();
		states.add(s0);
		states.add(s1);
		states.add(s2);
		states.add(s3);
		states.add(s4);
		states.add(s5);
		
//		Transition t0  = new Transition(s0, s1, Transition.EPSILON);
//		Transition t1  = new Transition(s1, s5f, Transition.EPSILON);
//		Transition t2 =  new Transition(s1, s2, 'a');
//		Transition t3 =  new Transition(s2, s5f, Transition.EPSILON);
//		Transition t4 =  new Transition(s2, s5f, 'b');
//		Transition t5 =  new Transition(s5f, s4, Transition.EPSILON);
//		Transition t6 =  new Transition(s3f, s5f, 'd');
//		Transition t7 =  new Transition(s2, s4, Transition.EPSILON);
//		Transition t8 =  new Transition(s1, s4, Transition.EPSILON);
//		Transition t9 =  new Transition(s4, s1, 'c');
//		Transition t10 = new Transition(s1, s3f, Transition.EPSILON);
//		Transition t11 = new Transition(s3f, s0, 'd');
//		Transition t12 = new Transition(s4, s3f, 'c');
//		Transition t13 = new Transition(s3f, s4, 'c');
		
		Transition t0  = new Transition(s0, s1, 'a');
		Transition t1  = new Transition(s0, s0, 'a');
		Transition t01  = new Transition(s0, s0, 'b');
		Transition t2 =  new Transition(s1, s2, 'a');
		Transition t02 =  new Transition(s1, s2, 'b');
		Transition t3 =  new Transition(s2, s3, 'a');
		Transition t4 =  new Transition(s3, s3, 'b');
		Transition t04 =  new Transition(s3, s3, 'a');
		Transition t5 =  new Transition(s5, s3, 'b');
		Transition t6 =  new Transition(s4, s5, 'a');
		Transition t06 =  new Transition(s4, s5, 'b');
		Transition t7 =  new Transition(s0, s4, 'b');
		
		
		
		Set<Transition> trans = new HashSet<>();
		trans.add(t0);
		trans.add(t1);
		trans.add(t01);
		trans.add(t02);

		trans.add(t2);
		trans.add(t3);
		trans.add(t4);
		trans.add(t04);

		trans.add(t5);
		trans.add(t6);
		trans.add(t06);
		trans.add(t7);

		Set<Character> alpha = new HashSet<>();
		alpha.add('a');
		alpha.add('b');
		
		State start = s0;
		
		Set<State> finale = new HashSet<>();
		finale.add(s3);
		
		NFA n = new NFA(states, trans, alpha, start, finale);
		System.out.println(n.toString());
		
		DFA d = Powerset.powersetConstruction(n);
		System.out.println(d);
	
		
//		Set<State> finale2 = new HashSet<>();
//		Set<State> states2 = new HashSet<>();
//		states2.add(s0);
//		finale2.add(s0);
//		Set<Transition> trans2 = new HashSet<>();
//		
//		EpsilonNFA n2 = new EpsilonNFA(states2, trans2, alpha, s0, finale2);
//		System.out.println();
//		System.out.println(n2);
//		System.out.println(AcceptNFA.accept(n2, "b"));
		
	}

}
