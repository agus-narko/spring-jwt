package com.narko.project.model;

import java.util.Scanner;

abstract class Animal {
    protected boolean isMammal;
    protected boolean isCarnivorous;
    
    public Animal(boolean isMammal, boolean isCarnivorous) {
        this.isMammal = isMammal;
        this.isCarnivorous = isCarnivorous;
    }
    
    public boolean getIsMammal() {
        return this.isMammal;
    }
    
    public boolean getIsCarnivorous() {
        return this.isCarnivorous;
    }
    
    abstract public String getGreeting();
    
    public void printAnimal(String name) {
        System.out.println( 
        	"A " + name + " says '" + this.getGreeting() 
            + "', is " + (this.getIsCarnivorous() ? "" : "not ") 
        	+ "carnivorous, and is " + (this.getIsMammal() ? "" : "not ") + "a mammal."
        );
    }
}

// Expand the code above line 1 - 30
// Write your code here. Do not use a 'public' access modifier in your class declaration. use default access modifier


/**
*   Cat class
**/

class  Cat extends Animal{
   
	protected String greeting = "meow";
  
    

    public Cat() {
		super(true, true);
	}
    public void setGreeting(String greeting){
        this.greeting = greeting;
    }
     public String getGreeting(){
        return this.greeting;
    }

}
    

/**
*   Cow class
**/

/**
*   Duck class
**/

public class ApiEntity2 {
    
    public static void main(String[] args) {
        Animal cat = new Cat();
        cat.printAnimal("cat");
        	
//        Animal cow = new Cow();
//        cow.printAnimal("cow");
//
//        Animal duck = new Duck();
//        duck.printAnimal("duck");
    }
}