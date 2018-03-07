PRGOGRAM: VCS
STUDENT NAME: Anthony Rojas 011819338
CONTACT INFO: anthony.rojas@student.csulb.edu
CLASS NUMBER AND SECTION: CECS 343 Sec 07
PROJECT PART: VCS Part 3
INTRO:
In this part, I figure out how to merge files in a different branch. The program will automatically merge them together. Additionally, this program will also be able to check in and check out, create folders/directories, create files, and delete the files/folders.

EXTERNAL REQUIREMENTS:
This program requires the computer running it to have the latest version of Java installed. Additionally, a computer must have a Java IDE installed as well.

BUILD, INSTALLATION, AND SETUP:
First, create a new Java project in the Java IDE of your choice. Name it what you want. Find the src folder of the Java project you just created and add in the Repo.java and CreateRepo.java files. In the IDE, right click on the folder and select "Refresh", this should now make it so that Repo.java and CreateRepo.java are included in the project src folder. Next, save and complile the code within the IDE. Once all this is complete, you may run it by selecting "run program" within your IDE. The program will accept commands through the JVM Console.

USAGE:
This program will be run through the JVM Console of the IDE of your choice.
THe prossible commands you may enter for this VCS are:
ls - local files and folders/subfolders 
mkdir - create a directory within the current folder
cd - change current folder/directory 
rm - remove a folder or file
mkf - create a file
mv - move a folder or file
co - check out a file
ci - check in a file
mrg - merge files
help - display possible commands
exit - exit the program

This program will create a repository within the C:\\ folder of your computer. Simply enter the name of your repo to create it.

EXTRA FEATURES:
This program will open the Notepad program when creating a new file, so that you may format the contents of the file to your liking.

BUGS:
When removing a folder, you must make sure it is empty before removing it.