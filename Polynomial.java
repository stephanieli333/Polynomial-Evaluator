// Stephanie Li
// 260742072

import java.math.BigInteger;

public class Polynomial 
{
	private SLinkedList<Term> polynomial;
	public int size()
	{	
		return polynomial.size();
	}
	private Polynomial(SLinkedList<Term> p)
	{
		polynomial = p;
	}
	
	public Polynomial()
	{
		polynomial = new SLinkedList<Term>();
	}
	
	// Returns a deep copy of the object.
	public Polynomial deepClone()
	{	
		return new Polynomial(polynomial.deepClone());
	}
	
	/* 
	 * TODO: Add new term to the polynomial. Also ensure the polynomial is
	 * in decreasing order of exponent.
	 */
	public void addTerm(Term t)
	{	
		/**** ADD CODE HERE ****/
		
		// Hint: Notice that the function SLinkedList.get(index) method is O(n), 
		// so if this method were to call the get(index) 
		// method n times then the method would be O(n^2).
		// Instead, use a Java enhanced for loop to iterate through 
		// the terms of an SLinkedList.
		/*
		for (Term currentTerm: polynomial)
		{
			// The for loop iterates over each term in the polynomial!!
			// Example: System.out.println(currentTerm.getExponent()) should print the exponents of each term in the polynomial when it is not empty.  
		}
		*/
		if(t.getCoefficient().compareTo(new BigInteger("0"))==0) {
			return;
		}
		if(polynomial.size() == 0) {
			polynomial.addFirst(t);
			return;
		}
		int i = 0;
		// if the exponent of t is greater than the exponent of the previous term and 
		// less than the exponent of the currentTerm, then t should be inserted between them
		for(Term currentTerm: polynomial) {
			if(t.getExponent() > currentTerm.getExponent()) {
				polynomial.add(i, t);
				return;
			}
			// if the exponent of t == the exponent of currentTerm, then add their 
			// coefficients and replace currentTerm with the sum of t and currentTerm
			else if(t.getExponent() == currentTerm.getExponent()) {
				BigInteger coefficient = t.getCoefficient().add(currentTerm.getCoefficient());
				if(coefficient.compareTo(new BigInteger("0")) != 0) {
					Term add = new Term(t.getExponent(), coefficient);
					polynomial.set(i, add);
				}
				else {
					polynomial.remove(i);
				}
				return;
			}
			i++;
		}
		// if t has not yet been added to the polynomial, then its exponent is greater
		// than the degree of the current polynomial
		// so add t to the end of the polynomial
		polynomial.add(polynomial.size(), t);
	}
	
	public Term getTerm(int index)
	{
		return polynomial.get(index);
	}
	
	//TODO: Add two polynomial without modifying either
	public static Polynomial add(Polynomial p1, Polynomial p2)
	{
		/**** ADD CODE HERE ****/
		/*Polynomial sum = new Polynomial(p1.polynomial);
		for(Term currentTerm:p2.polynomial) {
			sum.addTerm(currentTerm);
		}
		*/
		Polynomial sum = new  Polynomial();
		SLinkedList<Term> p1p = p1.polynomial.deepClone();
		SLinkedList<Term> p2p = p2.polynomial.deepClone();
		while(p1p.size()>0 && p2p.size()>0) {
			if (p1p.get(0).getExponent()>p2p.get(0).getExponent()) {
				sum.polynomial.add(sum.size(), p1p.removeFirst());
			}
			else if (p1p.get(0).getExponent()<p2p.get(0).getExponent()) {
				sum.polynomial.add(sum.size(), p2p.removeFirst());
			}
			else {
				int nexp = p1p.get(0).getExponent();
				BigInteger ncoeff = p1p.get(0).getCoefficient().add(p2p.get(0).getCoefficient());
				if(ncoeff.compareTo(new BigInteger("0")) != 0) {
					Term nterm = new Term(nexp, ncoeff);
					sum.polynomial.add(sum.size(), nterm);
				}
				p1p.removeFirst();
				p2p.removeFirst();
			}
		}
		while(p1p.size()>0 || p2p.size()>0) {
			if(p1p.size()>0) {
				sum.polynomial.add(sum.size(), p1p.removeFirst());
			}
			if(p2p.size()>0) {
				sum.polynomial.add(sum.size(), p2p.removeFirst());
			}
		}
		return sum;
	}
	
	//TODO: multiply this polynomial by a given term.
	private void multiplyTerm(Term t)
	{	
		/**** ADD CODE HERE ****/
		
		SLinkedList<Term> product = new SLinkedList<Term>();
		BigInteger coeff = t.getCoefficient();
		if(coeff.compareTo(new BigInteger("0"))==0) {
			this.polynomial = new SLinkedList<Term>();
			return;
		}
		int exp = t.getExponent();
		BigInteger ncoeff = new BigInteger("0");
		int nexp = 0;
		for(Term currentTerm:this.polynomial) {
			ncoeff = currentTerm.getCoefficient().multiply(coeff);
			nexp = currentTerm.getExponent() + exp;
			product.addLast(new Term(nexp, ncoeff));
		}
		this.polynomial = product;
	}
	
	//TODO: multiply two polynomials
	public static Polynomial multiply(Polynomial p1, Polynomial p2)
	{
		/**** ADD CODE HERE ****/
		Polynomial product = new Polynomial();
		if(p1.size()>=p2.size()) {
			for(Term currentTerm:p1.polynomial) {
				Polynomial temp = p2.deepClone();
				temp.multiplyTerm(currentTerm);
				product = Polynomial.add(product, temp);
			}
		}
		else {
			for(Term currentTerm:p2.polynomial) {
				Polynomial temp = p1.deepClone();
				temp.multiplyTerm(currentTerm);
				product = Polynomial.add(product, temp);
			}
		}
		return product;
	}
	
	//TODO: evaluate this polynomial.
	// Hint:  The time complexity of eval() must be order O(m), 
	// where m is the largest degree of the polynomial. Notice 
	// that the function SLinkedList.get(index) method is O(m), 
	// so if your eval() method were to call the get(index) 
	// method m times then your eval method would be O(m^2).
	// Instead, use a Java enhanced for loop to iterate through 
	// the terms of an SLinkedList.

	public BigInteger eval(BigInteger x)
	{
		/**** ADD CODE HERE ****/		   
        // Evaluate value of polynomial using Horner's method 
		BigInteger eval = new BigInteger("0");
		if(this.polynomial.size()!=0) {
			int power=this.polynomial.get(0).getExponent();
			for(Term currentTerm:this.polynomial) {
				while(currentTerm.getExponent()<power) {
					eval = eval.multiply(x);
					power--;
				}
				eval = eval.multiply(x);
				power--;
				eval = eval.add(currentTerm.getCoefficient());
			}
			while(power>=0) {
				eval = eval.multiply(x);
				power--;
			}
		}
		return eval;
	}
	
	// Checks if this polynomial is same as the polynomial in the argument
	public boolean checkEqual(Polynomial p)
	{	
		if (polynomial == null || p.polynomial == null || size() != p.size())
			return false;
		
		int index = 0;
		for (Term term0 : polynomial)
		{
			Term term1 = p.getTerm(index);
			
			if (term0.getExponent() != term1.getExponent() ||
				term0.getCoefficient().compareTo(term1.getCoefficient()) != 0 || term1 == term0)
					return false;
			
			index++;
		}
		return true;
	}
	
	// This method blindly adds a term to the end of LinkedList polynomial. 
	// Avoid using this method in your implementation as it is only used for testing.
	public void addTermLast(Term t)
	{	
		polynomial.addLast(t);
	}
	
	// This is used for testing multiplyTerm
	public void multiplyTermTest(Term t)
	{
		multiplyTerm(t);
	}
	
	@Override
	public String toString()
	{	
		if (polynomial.size() == 0) return "0";
		return polynomial.toString();
	}
}
