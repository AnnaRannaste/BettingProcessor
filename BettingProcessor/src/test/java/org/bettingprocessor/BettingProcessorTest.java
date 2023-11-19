package org.bettingprocessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;


public class BettingProcessorTest {
    @Test
    public void testProcessDeposit() {
        Player player = new Player("testPlayer");
        BettingProcessor.processDeposit(new String[]{"testPlayer", "DEPOSIT", "", "1000"}, player);
        assertEquals(1000, player.getBalance());
    }


    @Test
    public void testProcessBetFirst() {
        Map<String, Match> matches = new HashMap<>();
        matches.put("match1", new Match("match1", BigDecimal.valueOf(1.5), BigDecimal.valueOf(2.0)));

        Player player = new Player("testPlayer");
        BettingProcessor.processBet(new String[]{"testPlayer", "BET", "match1", "500", "A"}, player, matches, "");

        BettingProcessor.processBet(new String[]{"testPlayer", "BET", "match1", "700", "B"}, player, matches, "");

        long playerBalanceChange = player.getBalanceChangeFromBets();

        int totalBets = player.getTotalBets();

        int numberOfBets = player.getBets().size();


        assertEquals(4800, playerBalanceChange, "Incorrect player balance change");

        assertEquals(2, totalBets, "Incorrect total bets");

        assertEquals(4, numberOfBets, "Incorrect number of bets");
    }


    @Test
    public void testProcessWithdraw() {
        Player player = new Player("testPlayer");
        player.updateBalance(1000);

        BettingProcessor.processWithdraw(new String[]{"testPlayer", "WITHDRAW", "", "500"}, player);

        assertEquals(500, player.getBalance());
    }

    @Test
    public void testProcessMatch() {
        Map<String, Match> matches = new HashMap<>();
        BettingProcessor.processMatch(new String[]{"", "", "match1", "1.5", "2.0", "A"}, matches);

        assertTrue(matches.containsKey("match1"));
    }


    @Test
    public void testCalculateCasinoBalanceChange() {
        Map<String, Player> players = new HashMap<>();
        Player player1 = new Player("player1");
        player1.placeBet(500, true, null);

        Player player2 = new Player("player2");
        player2.placeBet(1000, false, null);
        players.put("player1", player1);
        players.put("player2", player2);

        long casinoBalanceChange = BettingProcessor.calculateCasinoBalanceChange(players);
        assertEquals(1500, casinoBalanceChange, "Incorrect casino balance change");


    }


}

