package rpn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Stacker {

    public static void main(String[] args) {

        String filePath = "src/files/Calc1.stk";
        Stack<Integer> stack = new Stack<Integer>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String value = bufferedReader.readLine();
            while (value != null) {
                if(!value.equals("+") && !value.equals("-") && !value.equals("*") && !value.equals("/")){
                    stack.push(Integer.parseInt(value));
                }
                else{
                    int v2 = stack.pop();
                    int v1 = stack.pop();

                    if(value.equals("+")){
                        int newValue = v1 + v2;
                        stack.push(newValue);
                    }
                    if(value.equals("-")){
                        int newValue = v1 - v2;
                        stack.push(newValue);
                    }
                    if(value.equals("*")){
                        int newValue = v1 * v2;
                        stack.push(newValue);
                    }
                    if(value.equals("/")){
                        int newValue = v1 / v2;
                        stack.push(newValue);
                    }
                }
                value = bufferedReader.readLine();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println(stack.pop());
    }
}
