# Braaaiiiins: An FSM-Driven Zombie Survival Game

Welcome to Braaaiiiins! This is a zombie survival game where you play as Mac the Scot (Macalester's [mascot](res/mac-the-scot-image.jpg)) trying to survive a zombie horde as long as possible. Be warned, these zombies are smart! They use a finite-state machine to determine their behavior. While playing, look at the ways they transition between wandering, chasing you, being on alert, or fighting you!

### How to Play
To play, run the View.java file, and then choose the number of zombies (we recommend around 15 for your first round). Movement uses either WASD or the arrow keys. If zombies touch you, you'll lose health as they attack you. Once your health reaches zero, you lose!

### Known Problems
The calculation for collision bounding boxes is pretty basic, so sometimes the zombies start dealing damage to the player sooner than the visuals suggest. This is especially true while the player is moving diagonally.

### Next Steps

1. Add different game modes
    - Endless (no setting zombie number, they continually enter from offscreen)
    - Horde (current spawn style)
2. Add score/high score
3. Vary zombie types?
4. Add different win condition (killing zombies? finding a cure?)

### Dev Notes

This originated as a group project I did for my COMP128: Data Structures class with another student, Hadley Wilkins. Details about the original class project can be found [here](https://docs.google.com/document/d/1rHyrvtjqpmBZaMnEqKZIDqbY-pYjA7C2B-raVB2e3vU/edit?usp=sharing).