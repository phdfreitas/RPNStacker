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
        boolean error = false;

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String value = bufferedReader.readLine();

            while(value != null) {
                // [Scanning manual] Pra não usar o "loop for" é possível usar expressões regulares, mas não é esse o objetivo da task2
                for (int i = 0; i < value.length(); i++) {
                    if (value.charAt(i) != '0' && value.charAt(i) != '1' && value.charAt(i) != '2' && value.charAt(i) != '3'
                            && value.charAt(i) != '4' && value.charAt(i) != '5' && value.charAt(i) != '6' && value.charAt(i) != '7'
                            && value.charAt(i) != '8' && value.charAt(i) != '9' && value.charAt(i) != '+' && value.charAt(i) != '-'
                            && value.charAt(i) != '*' && value.charAt(i) != '/') {

                        error = true;
                        break;
                    }
                }

                if (error) {  // Pra não fazer dessa maneira, eu poderia adicionar um try/catch, mas pra essa entrega deve ser suficiente essa maneira
                    scanning.clear(); // Limpa qualquer entrada anterior que tenha sido escaneada corretamente
                    stack.clear(); // Limpa qualquer entrada anterior válida da pilha

                    System.out.println("Error: Unexpected character: " + value);
                    break;
                } else {
                    if (!value.equals("+") && !value.equals("-") && !value.equals("*") && !value.equals("/")) {
                        stack.push(Double.parseDouble(value));
                        scanning.add(new Token(TokenType.NUM, value));
                    }
                    else {
                        double v2 = stack.pop();
                        double v1 = stack.pop();

                        if (value.equals("+")) {
                            scanning.add(new Token(TokenType.PLUS, value));
                            double newValue = v1 + v2;
                            stack.push(newValue);
                        }
                        if (value.equals("-")) {
                            scanning.add(new Token(TokenType.MINUS, value));
                            double newValue = v1 - v2;
                            stack.push(newValue);
                        }
                        if (value.equals("*")) {
                            scanning.add(new Token(TokenType.STAR, value));
                            double newValue = v1 * v2;
                            stack.push(newValue);
                        }
                        if (value.equals("/")) {
                            scanning.add(new Token(TokenType.SLASH, value));
                            double newValue = v1 / v2;
                            stack.push(newValue);
                        }
                    }
                }
                value = bufferedReader.readLine();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        if(scanning.size() > 0){
            for (Token token : scanning) {
                System.out.println(token);
            }
            System.out.printf("\nSaída: %.2f\n", stack.pop());
        }
    }
}
