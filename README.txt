# Password Generator

### Overview
It's about time to update your password again, but you feel unsafe to just add one more character to your existing one. 
But... coming up with a satisfying new password is also quite a brain exercise..... 

This is what drove me to write a password generator. You can customize the words 
that the generator will use so it's easier for you to memorize and it's also more 
secured.

### How to use
* The program will load _*.txt_ files from the _setupFiles_ folder. If you haven't modified the _setupFiles_ folder, 
by default the program will generate passwords based on the content of the _basic.txt_, which contains the lyrics of 
a song.
* If you want to generate passwords with your own phrases, be sure to add or replace the txt files in the _setupFiles_ 
folder. Then when running the program, click the _Reload txt Files_ button to update the database.

###### What should I put in the txt files?
Some might say "This does make it easy to generate a password, but I still have to memorize it". That depends on the data 
provided in the setupFiles. You can put in words that relate to each other and contents that you are familiar with, 
such as lyrics of a song, words of your favorite books, characters in your favorite books, your favorite movie titles... 
There are so much more possibilities.
 
**_Note:_** 
* The _.jar_ file should run on its own if you already have Java 8 installed.
* After have you executed the _.jar_ file for the first time, _mydb_ folder and _derby.log_ file will be 
generated. __DO NOT EDIT THESE FILES!__ They are the database files that keep your txt contents locally so it will load 
faster the next time on start up. 
* This Password Generator currently support alphanumeric characters. The program will take all the non-alphanumeric 
characters out when parsing the files. 
* Currently only support _.txt_ files for password database customization.

### Features
* Customizable database which the passwords are generated from
* Customizable password length

More features are coming soon... maybe
