package rpn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Stacker {

    public static void main(String[] args) {

        String filePath = "src/files/Calc1.stk";
        Stack<Double> stack = new Stack<Double>();

        ArrayList<Token> scanning = new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String value = bufferedReader.readLine();
            while (value != null) {
                if(value.matches("[0-9]*") || value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")){
                    if(!value.equals("+") && !value.equals("-") && !value.equals("*") && !value.equals("/")){
                        stack.push(Double.parseDouble(value));
                        scanning.add(new Token(TokenType.NUM, value));
                    }
                    else{
                        double v2 = stack.pop();
                        double v1 = stack.pop();

                        if(value.equals("+")){
                            scanning.add(new Token(TokenType.PLUS, value));
                            double newValue = v1 + v2;
                            stack.push(newValue);
                        }
                        if(value.equals("-")){
                            scanning.add(new Token(TokenType.MINUS, value));
                            double newValue = v1 - v2;
                            stack.push(newValue);
                        }
                        if(value.equals("*")){
                            scanning.add(new Token(TokenType.STAR, value));
                            double newValue = v1 * v2;
                            stack.push(newValue);
                        }
                        if(value.equals("/")){
                            scanning.add(new Token(TokenType.SLASH, value));
                            double newValue = v1 / v2;
                            stack.push(newValue);
                        }
                    }
                    value = bufferedReader.readLine();
                }
                else{
                    System.out.println("Error: Unexpected character: " + value);
                    scanning.clear(); // Impede de listar qualquer token já escaneado
                    stack.clear(); // limpa qualquer valor que esteja na pilha
                    break;
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < scanning.size(); i++) {
            System.out.println(scanning.get(i));
        }
        if(stack.size() > 0) System.out.printf("%.2f\n", stack.pop());
    }
}
