package vectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bazyli
 */
public class MyVector {
    
    public double x;
    public double y;
    
    public MyVector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void add(MyVector v2) {
        this.x += v2.x;
        this.y += v2.y;
    }
    
    public void subtract(MyVector v2) {
        this.x -= v2.x;
        this.y -= v2.y;
    }
    
    public static MyVector subtract(MyVector v1, MyVector v2) {
        return new MyVector(v2.x - v1.x, v2.y - v1.y);
    }
    
    public void multiply(double multiplier){
        this.x *= multiplier;
        this.y *= multiplier;
    }
    
    public void divide(double multiplier){
        this.x /= multiplier;
        this.y /= multiplier;
    }
    
    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    
    public MyVector normalize() {
        return new MyVector(x / magnitude(), y / magnitude());
    }
    
    public void normalizeThis() {
        x /= magnitude();
        y /= magnitude();
    }
    
    public void limit(double limit) {
        if(magnitude() > limit) {
             MyVector v = normalize();
             this.x = v.x * limit;
             this.y = v.y * limit;
        }
    }
    
    
    
    
}
