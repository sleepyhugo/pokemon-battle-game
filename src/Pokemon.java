import java.util.Arrays;

public class Pokemon {
    private String name;
    private Type type;
    private int maxHp;
    private int currentHp;
    private Move[] moves;

    public Pokemon(String name, Type type, int maxHp, Move[] moves) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public Move[] getMoves() {
        return moves;
    }

    public boolean isFainted() {
        return currentHp <= 0;
    }

    public void takeDamage(int damage) {
        currentHp -= damage;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public void resetHp() {
        this.currentHp = maxHp;
    }

    @Override
    public String toString() {
        return name + " (" + type + ") HP: " + currentHp + "/" + maxHp +
                " Moves: " + Arrays.toString(moves);
    }
}
