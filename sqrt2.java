/**
Author: Elaine Martin - MS Analytics
Purpose: Calculate square roots of integer numbers using the Babylonian method
General Design:
  1. Program takes any number of user inputs & checks that they are positive integers.
  2. Program requests a bound and checks that the bound is between 0 and 1.
  3. Program checks whether or not each input is a perfect square.
  4. If not a perfect square, calculate square root and error using the Babylonian method; loop until error is less than the bound.
  5. Program outputs the square roots and number of iterations each square root took to calculate in either list form or separated by spaces.
  6. Program asks the user whether or not they'd like to continue.
*/

import java.util.Scanner;
import java.util.ArrayList;
public class sqrt2 {
  public static void main(String[] args) {
    boolean go = false; //Flag to determine whether or not the user wants to continue
    do {  
      ArrayList<Integer> input = getInput();
      calcSqrt(input);
      go = repeat();
    } while(go);
  }

  /**
  Method takes any number of user inputs either one at a time or separated by spaces & checks that they are positive integers.
  @param none
  @return array list of inputs
  */
  public static ArrayList<Integer> getInput() {
    Scanner in = new Scanner(System.in);
    System.out.print("Please enter as many integers as you'd like either one at a time or separated by spaces, then F when you are finished: ");
    ArrayList<Integer> input = new ArrayList<Integer>(); //Numbers to find the square root of
    int currentSize = 0; //To help with input validation
    while(in.hasNextDouble()) {
        if(in.hasNextInt()) {
          input.add(in.nextInt());
          if (input.get(currentSize) < 0) {
            System.out.println("Warning: Negative integers will not be processed.");
            input.set(currentSize,0);
          }
        }
        else {
          double dummy = in.nextDouble(); //Variable to store the erroneous input
          System.out.println("Warning: Non-integers will not be processed.");
          input.add(0);
        }
        currentSize++;
      }
    return input;
  }

  /**
  Method requests a bound and checks that the bound is a double between 0 and 1.
  @param none
  @return bound
  */
  public static double getBound(){
    Scanner in = new Scanner(System.in);
    double bound;
    System.out.print("Please enter your desired bound on the error: ");
    //Input validation
    if (in.hasNextDouble()) {
      bound = in.nextDouble();
      if (bound <= 0 || bound >= 1) {
        do{
          System.out.print("Please enter a decimal between 0 and 1: ");
          bound = in.nextDouble();
        } while(bound <= 0 || bound >= 1);
      }
    }
    else {
      while(!in.hasNextDouble()) {
        String dummy = in.next(); //Variable to store the erroneous input
        System.out.print("Please enter a number between 0 and 1: ");
      }
      bound = in.nextDouble();
    }
    return bound;
  }

  /**
  Method checks whether or not each input is a perfect square. If not a perfect square, method calculates the square root and error for each input using the Babylonian method and loops until error is less than the bound.
  Method outputs the square roots and number of iterations each square root took to calculate in either list form or separated by spaces.
  @param array list of inputs
  @return none
  */
  public static void calcSqrt(ArrayList<Integer> input){
    Scanner in = new Scanner(System.in);
    double bound = getBound();
    double[] squareRoot = new double[input.size()];
    int[] iterations = new int[input.size()];
    //Check if the input is a perfect square
    for(int i = 0; i < squareRoot.length; i++) {
      boolean perfect = false;
      for (int j=0; j <= input.get(i); j++) {
        if(j*j == input.get(i)) {
          squareRoot[i] = j;
          perfect = true;
        }
      }
      if (!perfect) {
        //Select initial value of squareRoot
        String inputAsString = Integer.toString(input.get(i)); //Convert input to string in order to find number of digits
        int digits = inputAsString.length(); //number of digits input has
        int multiplier = 1; //To get around not being able to use Math.pow()
        for (int zeros=2; zeros < digits; digits=digits-2)
          multiplier = multiplier*10;
        if (digits % 2 == 0)
          squareRoot[i] = 6 * multiplier;
        else
          squareRoot[i] = 2 * multiplier;

        //Calculate square root and error
        double error = 1; //bound should always be < 1
        while (error > bound) {
          squareRoot[i] = 0.5*(squareRoot[i] + (input.get(i)/squareRoot[i]));
          error = (squareRoot[i] / Math.sqrt(input.get(i))) - 1;
          iterations[i]++;
        }
      }
    }
    //Select list or space format
    System.out.print("Please enter L if you'd like your output in list form or S if you'd like it separated by spaces: ");
    String output = in.next();
    //Output square roots & iterations
    System.out.println("The approximate square roots for your numbers were: ");
    if (output.equals("L")||output.equals("l"))
      for (double list:squareRoot)
        System.out.println(list);
    else {
      for (double space:squareRoot)
        System.out.print(space+" ");
      System.out.println();
    }
    System.out.println("It took the following numbers of iterations through the Bablyonian method to get the approximated value within the error bound (zeros denote perfect squares):");
    for (int i:iterations) {
      if (output.equals("L")||output.equals("l"))
        System.out.println(i);
      else
        System.out.print(i+" ");
    }
    System.out.println();
  }

  /**
  Method asks the user whether or not they'd like to continue.
  @param none
  @return boolean of whether or not to continue
  */
  public static boolean repeat(){
    Scanner in = new Scanner(System.in);
    boolean go = false;
    System.out.print("Please enter Y if you'd like to continue or Q to quit: ");
    String check = in.next();
    if (check.equals("y")||check.equals("Y"))
      go = true;
    else
      go = false;
    return go;
  }
}
