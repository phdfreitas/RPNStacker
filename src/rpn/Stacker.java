package rpn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Stacker {

    public static void main(String[] args) {

        String filePath = "src/files/Calc1.stk";
        Stack<Double> stack = new Stack<Double>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String value = bufferedReader.readLine();
            while (value != null) {
                if(!value.equals("+") && !value.equals("-") && !value.equals("*") && !value.equals("/")){
                    stack.push(Double.parseDouble(value));
                }
                else{
                    double v2 = stack.pop();
                    double v1 = stack.pop();

                    if(value.equals("+")){
                        double newValue = v1 + v2;
                        stack.push(newValue);
                    }
                    if(value.equals("-")){
                        double newValue = v1 - v2;
                        stack.push(newValue);
                    }
                    if(value.equals("*")){
                        double newValue = v1 * v2;
                        stack.push(newValue);
                    }
                    if(value.equals("/")){
                        double newValue = v1 / v2;
                        stack.push(newValue);
                    }
                }
                value = bufferedReader.readLine();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.printf("%.2f\n", stack.pop());
    }
}
