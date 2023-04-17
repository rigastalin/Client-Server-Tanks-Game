# Client-Server-Tanks-Game
Simple Client-Server Tanks Game

![GIF animation](https://github.com/rigastalin/Client-Server-Tanks-Game/blob/074d8518bd5ed33b7a0df3ac5bd5d94ed9dc1e63/tanks.gif)

JavaFX is a framework that enables developers to create high-quality desktop applications. Although JavaFX is not widely used in corporate development, it is commonly employed in various private solutions. Additionally, JavaFX is a useful tool for exploring the mechanisms of the Java programming language.

My task is to develop a client/server game, where the Socket server will enable two JavaFX clients to play a tank game together.

Thus, the game should enable two users to "drive" their tank and decrease the enemy's HP by shooting.

**Game mechanics** :
1. A tank is only able to move horizontally by pressing left and right arrow keys. Holding down the respective key results in a continuous movement in the respective direction.
2. A tank cannot move beyond the field edge.
3. A single space key press results in one shot. Makin a series of shots by holding down the key is impossible.
4. Hitting the target deducts 5 HPs from the enemy.
5. In the beginning, both players have 100 HPs.
6. The player is always at the bottom of the screen, whereas the enemy is at the top.
7. Tanks can only move if both players are connected to the server.

**Additional requirements**:
- Interface shall allow to connect to a specific server.
- When either player wins, a modal box shall appear with the statistics of shots, hits, and misses.
- These stats shall be stored in DBMS on the server.
- JavaFX client shall have an executable jar archive that can be launched like a normal application (by clicking on the file).
- README.md file shall be prepared with a set of instructions for application assembly and startup.

===
**How to play**:
1. Set up Postgres Server
2. Start the server from folder ServerTanks
3. Start the game from folder ClientTanks
