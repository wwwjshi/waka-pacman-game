# waka-pacman-game
This is a pacman-like game developed using gradle, java with processing library

## Design description
The design procedure is to first to read and obtain all data required for setting up. Then parse the data and generate game components. After each component is generated, define the characteristics of the components and their interaction between components. Finally, in the running of the game application draw the game components began the interactions from user to and between game components.

## Classes description
### App
The main class for running the game. Activates the game, start-up game window draws the game components and accepts player actions from the keyboard.

### ConfigReader
The class used to read and exact data from the configuration file. 

### GameParser
The class that take the data exacted by ConfigReader then parsing the data for each relevant game components and hence allows the generation of components. Additionally, the GameParser class also has attributes to keep track of game status and methods for transferring the status to game objects. The attribute of GameParser also contains the collections of non-player objects (Ghost, Wall, Fruit, SuperFruit), and based on the logic flow handles the overall interaction between all objects.

### Waka
The class that creates the character which the player controls. Contains sets of attributes to keep track of status, movements, and locations in the game map. Also contains methods for interaction between the other game components including detection of the collision of objects and the triggering of status in GameParser. Some of the attributes are in protected to allow other class objects to easily obtain relevant data and also allows ease of testing.

### Ghost
The class that creates the non-player character that is movable in the game map. The moving behaviour depends on the information from GameParser such as the type it is, and mode it is in.  

### FixedComponents
The parent class for creating all fixed objects in the map including Wall and Fruit objects. Contains attributes and getter methods for the object’s location. Collections of subclass objects are stored in GameParser allowing Waka and/or Ghost to check for collision.  

### Wall
The class for the game objects that restricts the movement of Ghost and Waka. Where the Ghost and Waka would check for collision of all Wall objects stored in GameParser.

### Fruit
The class for the game objects that player must collect to win the game. Where Waka would check for collision of all Fruit objects stored in GameParser and GameParser would remove it from the collection if collided. 

### SuperFruit
The class that inherits Fruit class since it can be regarded to be a type for fruit. Similarly, a collection of all SuperFruit object is stored in GameParser and GameParser would remove it and triggers the frighten mode of all ghost objects when collided with Waka.


