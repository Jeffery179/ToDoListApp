# To Do List App

  A simple intutiive Java application that allows for the user to create "to do" items.  
These items can be marked as completed, edited, or deleted. If marked complete, the finished items will move to a 
completed list. The completed list allows the user the option of moving the item back to the uncompleted list or deleting it.

  Items on both list persist through sessions with the use of MongoDB. MongoDB saves our tasks through sessions and the information is 
parsed in Java. For this to work, the project must contain the mongodb-driver-3.9.0.jar driver given by Mongo themselves. 
Additional information about that can be found here: https://mongodb.github.io/mongo-java-driver/

  The view of our application is quite simple and was created with JavaFX and SceneBuilder. I highly recommend using JavaFx over Swing,
I found it much easier to use and understand. Additionally, SceneBuilder, found here https://gluonhq.com/products/scene-builder/ sped 
the GUI building up several fold. 


