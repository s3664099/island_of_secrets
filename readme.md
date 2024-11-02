# Island of Secrets

This is an attempt to translate the game Island of Secrets to a modern computer. The game appeared as a basic
listing in a book of the same name and the idea was that you would type the game in and play it. The
problem with adventure games is that if you type it in then you can fairly easily work out the solution
to the game, so the authors made it as difficult as possible for somebody to be able to read the code.
While this meant that you would have a game that was playable, there was also issues with errors creeping
in, and it also made it difficult for users to learn some of the tricks of the basic language, namely
because there aren't any notes telling you what the functions did. Also, the readability is notoriously
bad.

[Anyway, you can find a copy of the book here.](https://archive.org/details/island-of-secrets_202303)
(It looks like they have all been moved to the Internet Archive).

[You can find the basic version of the game here.](https://github.com/s3664099/basic_scripts/tree/master/Island%20of%20Secrets)
(The various different basic versions are here).

## Notes on the Basic Code

*Reading the Data*

This is different to what I have seen in other games, and no doubt this is due to memory restrictions in some
of the computers. Instead of reading all of the data into the memory, it will only read one at a time (which will
be slower as it will need to do it for every item as well). 

Line 2780 repositions the pointer at the begining of the data and will read through all of the locations, discarding each one.
Line 4110 reads through all of the objects and discards them

With regards to coding the location of the objects, this is store in the string H$, which are symbols, but the ascii characters
represent a location. The next line, Q$ is a flag that is set on each of the objects, which are used throughout the game.

## Executing the Game

To compile the game you need to do the following:

*javac \*.java*

Once it has compiled then you can run the game as follows:

*java Start*

## Development Notes ##
*v01*

Okay, it works, but there are some issues with it. However I won't be fulling testing it as yet since
I am looking to upgrade it future and make the code readable. Also, I want to move it away from the
command line so that it can be played in a window environment, and that the game runs smoother.

Also, I need to go over the code again and make further notes, and also tidy up the notes that I have
added to the notes file.

2 November 2024
Finished the initialisation classes. Moved the locations and objects into separate methods which are contained
in the game method (and are stored in set arrays). The commands are once again split into nouns and verbs, however
I have decided to use the whole words as opposed to the first three letters. I note that this method was originally
for making the code less complicated on older computers, but it can cause confusion (as well as being lazy). These
are also stored in arrays, and since they do not change they have been marked as final.

