# csci2020u_Assignment_2
Assignment 2 of Crawford Heidinger and Alessandro Prataviera


Overview
---------------------------------------------------
Our project is a file sharer that allows a client to connect to a server  
and then either upload or download files from the server to client or client to server.  
  
It is a Java FX application so when you run it you will have a UI created to go about this task.


Improvements
-----------------------------------------------------------------------------------------------
The Basic Improvements we made to the project itself were all visual. A menu bar was added instead of multiple buttons since it  
would clutter the UI too much so we put them all into one place. Some of these new button were an exit button to exit out of the  
UI and a refresh button so you can update the UI to see the files being added to server or onto your machine.

How to Run
-----------------------------------------------------------------------------------------------
In order to run our program you will need to download a java that is greater than java 8 and less than java 13.
You must also get a java that can compile javafx code as well since the regular java versions won't do this.
There is a website that you can download a combined version of java and javafx called azul, and I would
recommend you go there to download it. The recommended IDE that we use is Intellij as it is meant for the
java coding language, and it is easy to set up, but Intellij is not needed. Once all the setup is complete, and you
have everything downloaded, you can clone our repository in your terminal which is done with a simple git
clone, and the repository website link. This will import all the code onto your machine for use. Then you
can open our code in the IDE and run our program however there is a certain order that it must be done in. Firstly, out server class must be run so that it can look   
for connections and then you can run the Main class so then the client will connect to the server, and you will be able to share you files!

Steps to use the spam detector:
1. Compile the code in an IDE and run in the specific order
2. Then click on a file to be uploaded/downloaded
3. Inside the Menu Bar labelled "Options" and perform the task you wish to do.




Resources
-----------------------------------------------------------------------------------------------
https://www.azul.com/downloads/zulu-community/?package=jdk

https://www.jetbrains.com/idea/