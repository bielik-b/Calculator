package calculator;

import db.DBHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBHandler handler = new DBHandler();
        Scanner scan = new Scanner(System.in);

        String text = scan.nextLine();
        List<Lexeme> lexemes = lexAnalyze(text);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        double result = expr(lexemeBuffer);
        System.out.println(result);

        handler.addResultInDB(text, result);
    }

    public static List<Lexeme> lexAnalyze(String expText) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int position = 0;
        while (position < expText.length()) {
            char c = expText.charAt(position);
            switch (c) {
                case '(':
                    lexemes.add(new Lexeme(SymbolsType.left_bracket, c));
                    position++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(SymbolsType.right_bracket, c));
                    position++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(SymbolsType.plus, c));
                    position++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(SymbolsType.minus, c));
                    position++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(SymbolsType.multiply, c));
                    position++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(SymbolsType.division, c));
                    position++;
                    continue;
                default:
                    if (c <= '9' && c >= 0) {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            position++;
                            if (position >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(position);
                        } while (c <= '9' && c >= '0');
                        lexemes.add(new Lexeme(SymbolsType.number, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character:" + c);
                        }
                        position++;
                    }
            }

        }
        lexemes.add(new Lexeme(SymbolsType.EOF, ""));
        return lexemes;
    }

    public static double expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == SymbolsType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusminus(lexemes);
        }
    }

    public static double plusminus(LexemeBuffer lexemes) {
        double value = multdiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case plus -> value += multdiv(lexemes);
                case minus -> value -= multdiv(lexemes);
                case EOF, right_bracket -> {
                    lexemes.back();
                    return value;
                }
                default -> throw new RuntimeException("Unexpected token: " + lexeme.expression
                        + " at position: " + lexemes.getPos());
            }
        }
    }

    public static double multdiv(LexemeBuffer lexemes) {
        double value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case multiply -> value *= factor(lexemes);
                case division -> value /= factor(lexemes);
                case EOF, right_bracket, plus, minus -> {
                    lexemes.back();
                    return value;
                }
                default -> throw new RuntimeException("Unexpected token: " + lexeme.expression
                        + " at position: " + lexemes.getPos());
            }
        }
    }

    public static double factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case number:
                return Double.parseDouble(lexeme.expression);
            case left_bracket:
                double value = plusminus(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != SymbolsType.right_bracket) {
                    throw new RuntimeException("Unexpected token: " + lexeme.expression
                            + " at position: " + lexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.expression
                        + " at position: " + lexemes.getPos());
        }
    }

}

