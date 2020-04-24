# Link-Analysis-Algorithm
The goal of this program is to implement Kleinberg’s Hyperlink Induced Topic Search(HITS) Algorithm that will help to rate webpages. 

Hyperlink Induced Topic Search (HITS) Algorithm is a Link Analysis Algorithm that rates webpages, developed by Jon Kleinberg. This algorithm is used to the web link-structures to discover and rank the webpages relevant for a particular search.
HITS Algorithm uses hubs and authorities to define a recursive relationship between webpages.

This is a resuasable plug and play program that would help to implement link analysis algorithm in any project with minimal tweaks.

# Steps for Compilation and execution of Hits program:

javac hits.java	//Compilation of Hits program

java hits number_of_iteration initial_value samplegraph.txt	//Execution of Hits program
(For Ex:- java hits 0 -1 samplegraph.txt)
