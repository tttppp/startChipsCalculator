# startChipsCalculator
Android app to decide how to use chips in a "no stakes" poker game

## Problem

The problem this app aims to solve is that before a game of poker can begin the players need to agree upon values
for the chips. It is desirable to be able to represent a large spread of values with a small number of chips (which
implies the value of the chips should be spread), but it is also desirable that players do not often have to make
change with each other (i.e. the chips should not be spread too much). This app simulates different distributions of
chips in order to heuristically determine a 'best' set of values.

"No Stakes" poker presents a unique challenge in that there is no obvious total that a player's chips should sum to.

## Instructions

Enter the number of players, the number of different colours of chips and the quantity of each colour (in the order
that you want the denominations to be). Wait for the progress bar to turn blue, and then read the results from the
bottom. For example with 7 players, 4 colours (with 100, 60, 40 and 40 of each colour) this will look something like:

```
Tournament ratio: 100
Denominations: [1, 2, 5, 10]
Player Cash: 100 [Start 75, Rebuy 25]
Start chips: [13, 6, 4, 3]
Rebuy chips: [1, 2, 0, 2]
```

The tournament ratio is the ratio of the smallest denomination chip (which will usually be used as the initial small
blind) and a player's total chips. Denominations gives the value that each colour of chip will take. Player cash gives
the amounts in chips that a player has at the start of the game, and what they receive for their re-entry (nb. we allow
exactly one re-entry, and anybody who hasn't had a re-entry receives these chips at a pre-agreed point). Start chips
shows how many of each chip should be given to each player at the start of the game, and rebuy chips shows how many of
each chip should be set aside for each player's re-entry.

## Limitations

The app insists that all players receive exactly the same quantity of each colour, which may not make optimal use of
the available chips, but does avoid arguments and make set-up easier. The app aims for a tournament ratio of 100. The
app will only allow chips to be certain denominations (e.g. it won't suggest giving one of the colours a value of 37).
