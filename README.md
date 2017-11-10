Author - Paul Baird-Smith    Date - November 9, 2017

Word Unscrambler 

Unscrambler wants to solve the following problem:

Given a target word, we would like to find all the words in the English language
that can be made using only the letters from the target word. For example, if 
our target word is "tree", we would like to print a list of words including 
"tee", "ret", "ere", and others (the list changes depending on which words you
decide to include in the English language). As demonstrated by the example, any
repeated letter in the target word can be repeated in a resulting word up to the
number of times it appears in the target word (for "tree", we are allowed 2 "e"s).

We solve this problem with 2 approaches:

1) We can find all of the permutations of the letters in the target word and
   check for membership in the dictionary. This approach is preferred for shorter
   words, and becomes less desirable after we pass the number of words in the English
   language (currently between 100,000 and 150,000).

2) Check all the words in the dictionary to see if they can be made from the letters
   in the target word. This approach is useful for longer words, with repeat letters. A
   prime example of this is the word "telephone".


Unscrambler takes at least one argument: the target word. After running, it prints a 
list of all the words that can be written using its letters. An example of how to run
Unscrambler is:

           java Unscrambler helicopter

Here, the target word is helicopter. The program expects the target word as the first
argument, and defaults to "a" if no argument is given.

A second argument passed is a mode integer which can be 0, 1, or 2. If mode 0 is used,
the program will run approach (1) above. If mode 1 is used, the program will run
approach (2). If mode 2 is selected, the program will determine which strategy is the
best depending on the target word length and run this solution. The program defaults
to mode 2.

Email ppb366@cs.utexas.edu with any questions