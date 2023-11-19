Betting Processor

Introduction

This Java program processes betting data involving multiple players and matches. 
It reads input from two files, "player_data.txt" and "match_data.txt," and produces results in a file named "result.txt." 
The operations include bets, deposits, and withdrawals, and they are ordered by time, with older events coming first.

Description

Players can perform three types of operations: Bet, Deposit, and Withdraw.
Matches have two sides (A and B) and three possible results: A side won, B side won, or Draw.
Each match has rates for sides A and B.
Players gain or lose coins based on their bets and the match outcome.
One player can place only one bet on one match and one side.
Illegal actions occur if a player attempts to bet or withdraw more coins than they have.
All players start with a balance of 0 coins.
Coin results are integers rounded down.

Assignment Rules
Read input from "player_data.txt" and "match_data.txt" in the resource folder.
Produce results in "result.txt" in the same location.
Coin numbers in transactions are of type int, and account balance values are of type long.
Input Data
Input data is provided in text files.
Each line contains information about one action.
Data is grouped by player UID, ordered by time.

Example "player_data.txt" contents:

163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,DEPOSIT,,4000,
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,BET,abae2255-4255-4304-8589-737cdff61640,500,A
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,WITHDRAW,,200,

Output Data

The "result.txt" file contains three result groups:
Legitimate players with their final balance and betting win rate.
Illegitimate players represented by their first illegal operation.
Coin changes in the casino host balance.

# Legitimate Players
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4321 0.80
4925ac98-833b-454b-9342-13ed3dfd3ccf 8093 null

# Illegitimate Players
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 BET abae2255-4255-4304-8589-737cdff61640 5000 A

# Casino Balance Change
Casino Host Balance Change: 1500

In case of empty lists in the first or second part, an empty line should be written into the output file for the corresponding section.


Tests
The project includes JUnit tests to ensure the correctness of key functionalities. To run the tests, follow these steps:

Test Descriptions
Process Deposit Test:

Verifies that the processDeposit method correctly updates a player's balance after a deposit operation.

Process Bet Test (First):

Checks various scenarios involving betting, including successful and unsuccessful bets.

Process Withdraw Test:

Ensures the processWithdraw method correctly processes a withdrawal operation and updates the player's balance accordingly.

Process Match Test:

Validates that the processMatch method accurately adds a match to the provided map.

Calculate Casino Balance Change Test:

Verifies the calculateCasinoBalanceChange method, ensuring it accurately calculates the net change in the casino's balance based on players' bets.

Note
The tests are designed to cover different aspects of the BettingProcessor functionality and should be run regularly to ensure the code behaves as expected.

