package at.ac.univie.vis2017.util;

import java.util.Random;

/** 
 Generate pseudo-random floating point values, with an 
 approximately Gaussian (normal) distribution.

 Many physical measurements have an approximately Gaussian 
 distribution; this provides a way of simulating such values. 
 source: http://www.javapractices.com/topic/TopicAction.do?Id=62
*/
public final class RandomGaussian {
    
  private Random fRandom = new Random();
  
  public double getGaussian(double aMean, double aVariance){
    return aMean + fRandom.nextGaussian() * aVariance;
  }

  private static void log(Object aMsg){
    System.out.println(String.valueOf(aMsg));
  }
} 
