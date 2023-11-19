package org.bettingprocessor;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class BettingProcessor {
    private static final String PLAYER_DATA_FILE = "player_data.txt";
    private static final String MATCH_DATA_FILE = "match_data.txt";
    private static final String RESULT_FILE = "result.txt";

    public static void main(String[] args) {
        Map<String, Player> players = new HashMap<>();
        Map<String, Match> matches = new HashMap<>();

        readPlayerData(players, matches);

        readMatchData(matches);

        processPlayerActions(players, matches);

        writeResults(players);
    }

    private static void readPlayerData(Map<String, Player> players, Map<String, Match> matches) {
        try (InputStream is = BettingProcessor.class.getClassLoader().getResourceAsStream("player_data.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                processPlayerAction(line, players, matches);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processPlayerAction(String line, Map<String, Player> players, Map<String, Match> matches) {

        String[] data = line.split(",", -1);

        if (data.length < 2) {
            return;
        }

        String playerId = data[0];
        String operation = data[1];


        Player player = players.computeIfAbsent(playerId, Player::new);


        switch (operation) {
            case "DEPOSIT":
                processDeposit(data, player);
                break;

            case "BET":
                processBet(data, player, matches, line);
                break;

            case "WITHDRAW":
                processWithdraw(data, player);
                break;

            case "MATCH":
                processMatch(data, matches);
                break;

            default:
                System.out.println("Unknown operation: " + operation);
                break;
        }
    }

    static void processDeposit(String[] data, Player player) {

        if (data.length > 3 && !data[3].isEmpty()) {
            try {
                long depositAmount = Long.parseLong(data[3]);
                player.updateBalance(depositAmount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid deposit amount: " + data[3]);
            }
        } else {
            System.out.println("Incomplete data for the DEPOSIT operation.");
        }
    }


    static void processBet(String[] data, Player player, Map<String, Match> matches, String line) {
        if (data.length == 5) {
            boolean won = false;
            try {
                String matchId = data[2].trim();
                long betAmount = Long.parseLong(data[3].trim());
                String betSide = data[4].toUpperCase().trim();

                Match match = matches.get(matchId);


                if (match != null) {
                    BigDecimal rate = betSide.equals("A") ? match.getRateA() : match.getRateB();


                    BigDecimal winAmount = rate.multiply(BigDecimal.valueOf(betAmount));


                    won = match.getResult().equals(betSide);


                    BigDecimal balanceChange = won ? winAmount.subtract(BigDecimal.valueOf(betAmount)) : winAmount.negate();


                    long balanceChangeLong = balanceChange.longValue();


                    player.updateBalance(balanceChangeLong);
                    player.placeBet(betAmount, won, match);
                    Bet newBet = new Bet(betAmount, match, won);
                    player.addBet(newBet);

                    System.out.println("Player balance after bet: " + player.getBalance());
                    System.out.println("Bet placed successfully.");
                } else {


                    long balanceChange = -betAmount;


                    player.updateBalance(balanceChange);
                    player.placeBet(betAmount, won, match != null ? match : null);
                    player.addBet(new Bet(betAmount, null, false));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {

                e.printStackTrace();
                player.flagIllegalAction("BET " + line);
            }
        } else {

            System.out.println("Actual line content: " + line);
        }
    }


    static void processWithdraw(String[] data, Player player) {

        if (data.length > 3 && !data[3].isEmpty()) {

            data[3] = data[3].trim();

            try {
                long withdrawAmount = Long.parseLong(data[3]);
                player.processWithdrawal(withdrawAmount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid withdrawal amount: " + data[3]);
            }
        } else {

            System.out.println("Withdrawal amount not specified for player: " + player.getPlayerId());
        }
    }


    static void processMatch(String[] data, Map<String, Match> matches) {

        if (data.length >= 5) {
            try {
                String matchId = data[2].trim();
                BigDecimal rateA = new BigDecimal(data[3].trim());
                BigDecimal rateB = new BigDecimal(data[4].trim());


                String result = "";
                if (data.length > 5) {
                    result = data[5].trim();
                }


                Match match = new Match(matchId, rateA, rateB);
                match.setResult(result);
                matches.put(matchId, match);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {

                System.out.println("Error processing MATCH data: " + Arrays.toString(data));
                e.printStackTrace();
            }
        } else {

            System.out.println("Invalid line format for MATCH. Skipping line: " + Arrays.toString(data));
        }
    }


    private static void readMatchData(Map<String, Match> matches) {
        try (InputStream is = BettingProcessor.class.getClassLoader().getResourceAsStream("match_data.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                processMatchData(line, matches);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processMatchData(String line, Map<String, Match> matches) {
        String[] data = line.split(",", -1);
        if (data.length >= 4) {
            try {
                String matchId = data[0].trim();
                BigDecimal rateA = new BigDecimal(data[1].trim());
                BigDecimal rateB = new BigDecimal(data[2].trim());
                String result = data[3].trim();

                Match match = new Match(matchId, rateA, rateB);
                match.setResult(result);

                matches.put(matchId, match);


            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        } else {

        }
    }


    private static void processPlayerActions(Map<String, Player> players, Map<String, Match> matches) {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player player = entry.getValue();

        }
    }

    private static void writeResults(Map<String, Player> players) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESULT_FILE))) {


            System.out.println("Player Information:");
            players.values().forEach(player -> System.out.println(player.getPlayerId() + " " + player.getBalance() + " " + player.getWinRate().setScale(2)));
            System.out.println("--------------------------");

            writer.println("# Results:");


            writer.println("\n# Legitimate Players:");
            writeLegitimatePlayers(writer, players);

            writer.println();

            writer.println("\n# Illegitimate Players:");

            writeIllegitimatePlayers(writer, players);

            writer.println();

            writer.println("\n# CasinoBalanceChange:");

            writeCasinoBalanceChange(writer, players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeLegitimatePlayers(PrintWriter writer, Map<String, Player> players) {
        List<Player> legitimatePlayers = players.values().stream()
                .filter(Player::isLegitimate)
                .sorted(Comparator.comparing(Player::getPlayerId))
                .collect(Collectors.toList());


        legitimatePlayers.forEach(player -> {

            String winRate = String.format(Locale.getDefault(), "%.2f", player.getWinRate());
            writer.println(
                    player.getPlayerId() + " " + player.getBalance() + " " + winRate
            );
        });
    }


    private static void writeIllegitimatePlayers(PrintWriter writer, Map<String, Player> players) {
        List<Player> illegitimatePlayers = players.values().stream()
                .filter(player -> !player.isLegitimate() && player.getFirstIllegalAction() != null)
                .sorted(Comparator.comparing(Player::getPlayerId))
                .collect(Collectors.toList());


        illegitimatePlayers.forEach(player -> writer.println(
                player.getPlayerId() + " " + player.getFirstIllegalAction()

        ));
    }

    private static void writeCasinoBalanceChange(PrintWriter writer, Map<String, Player> players) {

        boolean hasIllegalAction = players.values().stream().anyMatch(Player::hasIllegalAction);

        if (!hasIllegalAction) {
            long casinoBalanceChange = calculateCasinoBalanceChange(players);
            writer.println("Casino Host Balance Change: " + casinoBalanceChange);
        }
    }


    static int calculateCasinoBalanceChange(Map<String, Player> players) {
        int balanceChange = 0;

        for (Player player : players.values()) {
            if (!player.hasIllegalAction()) {

                int playerBalanceChange = (int) player.getBalanceChangeFromBets();
                System.out.println("Player " + player.getPlayerId() + " Balance Change: " + playerBalanceChange);

                balanceChange += playerBalanceChange;
            }
        }

        System.out.println("Total Casino Balance Change: " + balanceChange);
        return balanceChange;
    }

}
