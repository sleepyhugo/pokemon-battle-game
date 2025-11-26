import java.util.Random;
import java.util.Scanner;

public class BattleGame {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Mini Pokémon Battle Game ===");
        System.out.println("Choose your starter:");

        Pokemon[] starters = createStarters();

        for (int i = 0; i < starters.length; i++) {
            System.out.println((i + 1) + ". " + starters[i].getName() + " (" + starters[i].getType() + ")");
        }

        System.out.print("Enter 1, 2, or 3: ");
        int choice = scanner.nextInt();
        while (choice < 1 || choice > starters.length) {
            System.out.print("Invalid choice. Try again: ");
            choice = scanner.nextInt();
        }

        Pokemon player = starters[choice - 1];
        Pokemon cpu = starters[RANDOM.nextInt(starters.length)];

        // avoid mirror match just for fun
        if (cpu.getName().equals(player.getName())) {
            cpu = starters[(choice) % starters.length];
        }

        player.resetHp();
        cpu.resetHp();

        System.out.println("\nYou chose: " + player.getName());
        System.out.println("CPU chose: " + cpu.getName());
        System.out.println("\nBattle start!\n");

        // Battle loop
        while (!player.isFainted() && !cpu.isFainted()) {
            showStatus(player, cpu);

            // player turn
            System.out.println("\nYour turn! Choose a move:");
            Move[] playerMoves = player.getMoves();
            for (int i = 0; i < playerMoves.length; i++) {
                System.out.println((i + 1) + ". " + playerMoves[i]);
            }

            System.out.print("Move: ");
            int moveIndex = scanner.nextInt() - 1;
            while (moveIndex < 0 || moveIndex >= playerMoves.length) {
                System.out.print("Invalid move. Try again: ");
                moveIndex = scanner.nextInt() - 1;
            }

            Move playerMove = playerMoves[moveIndex];
            int damageToCpu = calculateDamage(playerMove, player, cpu);
            System.out.println("\nYou used " + playerMove.getName() + "!");
            System.out.println("It dealt " + damageToCpu + " damage.");
            cpu.takeDamage(damageToCpu);

            if (cpu.isFainted()) {
                System.out.println("\n" + cpu.getName() + " fainted!");
                System.out.println("You win! 🎉");
                break;
            }

            // cpu turn
            Move[] cpuMoves = cpu.getMoves();
            Move cpuMove = cpuMoves[RANDOM.nextInt(cpuMoves.length)];
            int damageToPlayer = calculateDamage(cpuMove, cpu, player);
            System.out.println("\nCPU's turn!");
            System.out.println(cpu.getName() + " used " + cpuMove.getName() + "!");
            System.out.println("It dealt " + damageToPlayer + " damage.");
            player.takeDamage(damageToPlayer);

            if (player.isFainted()) {
                System.out.println("\n" + player.getName() + " fainted!");
                System.out.println("You lost...");
                break;
            }
        }

        System.out.println("\n=== Battle Over ===");
        scanner.close();
    }

    private static Pokemon[] createStarters() {
        Move[] fireMoves = {
                new Move("Flame Burst", Type.FIRE, 25),
                new Move("Ember", Type.FIRE, 18),
                new Move("Scratch", Type.FIRE, 15),
                new Move("Quick Attack", Type.NORMAL, 15) // we'll treat NORMAL as neutral
        };

        Move[] waterMoves = {
                new Move("Water Gun", Type.WATER, 22),
                new Move("Bubble", Type.WATER, 18),
                new Move("Tackle", Type.WATER, 15),
                new Move("Splash Hit", Type.WATER, 20)
        };

        Move[] grassMoves = {
                new Move("Vine Whip", Type.GRASS, 22),
                new Move("Leaf Blade", Type.GRASS, 25),
                new Move("Tackle", Type.GRASS, 15),
                new Move("Razor Leaf", Type.GRASS, 20)
        };

        Pokemon fireMon = new Pokemon("Flareon", Type.FIRE, 100, fireMoves);
        Pokemon waterMon = new Pokemon("Aquarion", Type.WATER, 105, waterMoves);
        Pokemon grassMon = new Pokemon("Leafia", Type.GRASS, 95, grassMoves);

        return new Pokemon[]{fireMon, waterMon, grassMon};
    }

    private static void showStatus(Pokemon player, Pokemon cpu) {
        System.out.println("--------------------------------------");
        System.out.println("Your " + player.getName() + " HP: " +
                player.getCurrentHp() + "/" + player.getMaxHp());
        System.out.println("CPU " + cpu.getName() + " HP: " +
                cpu.getCurrentHp() + "/" + cpu.getMaxHp());
        System.out.println("--------------------------------------");
    }

    private static int calculateDamage(Move move, Pokemon attacker, Pokemon defender) {
        double effectiveness = getEffectiveness(move.getType(), defender.getType());
        int basePower = move.getPower();

        // small random factor so damage varies a bit
        double randomFactor = 0.85 + (RANDOM.nextDouble() * 0.15);

        int damage = (int) Math.round(basePower * effectiveness * randomFactor);
        if (damage < 1) damage = 1; // always at least 1 dmg

        if (effectiveness > 1.0) {
            System.out.println("It's super effective!");
        } else if (effectiveness < 1.0) {
            System.out.println("It's not very effective...");
        }

        return damage;
    }

    private static double getEffectiveness(Type attackType, Type defenderType) {
        // Fire/Water/Grass triangle
        if (attackType == Type.FIRE && defenderType == Type.GRASS) return 2.0;
        if (attackType == Type.GRASS && defenderType == Type.WATER) return 2.0;
        if (attackType == Type.WATER && defenderType == Type.FIRE) return 2.0;

        if (attackType == Type.GRASS && defenderType == Type.FIRE) return 0.5;
        if (attackType == Type.WATER && defenderType == Type.GRASS) return 0.5;
        if (attackType == Type.FIRE && defenderType == Type.WATER) return 0.5;

        // neutral
        return 1.0;
    }
}