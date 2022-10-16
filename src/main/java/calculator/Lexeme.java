package calculator;

public class Lexeme {
    SymbolsType type;
    String expression;

    public Lexeme(SymbolsType type, String value) {
        this.type = type;
        this.expression = value;
    }

    public Lexeme(SymbolsType type, Character value) {
        this.type = type;
        this.expression = value.toString();
    }

    @Override
    public String toString() {
        return "calculator.Lexeme{" +
                "type=" + type +
                ", value='" + expression + '\'' +
                '}';
    }
}