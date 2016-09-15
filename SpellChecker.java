/*
Program that takes in a dictionary of words and a file of words that the user would like spell checked,
then prints the values of misspelled words along with suggestions of correctly-spelled words that the user may have meant.
Utilizes HashSets, regular expressions, and all kinds of string manipulation.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class SpellChecker
{
	public static final void main(String[] args)
	{
		// handle possible errors using a try/catch
		try
		{
			// create Files using the command line arguments of the dictionary and the words to spell check
			File dictionary = new File(args[0]);
			File wordsToCheck = new File(args[1]);

			// create BufferedReader to read in the dictionary
			BufferedReader reader = new BufferedReader(new FileReader(dictionary));

			// declare String to be used for the current word
			String currWord;

			// create HashSet for the dictionary
			HashSet<String> dictionarySet = new HashSet<String>();

			// loop through each line in the file
			while ((currWord = reader.readLine()) != null)
			{
				// see if the current line is white space, in which case, continue
				if (currWord.equals("\\s+"))
				{
					continue;
				}

				// add the (lower cased) word to the set
				dictionarySet.add(currWord.toLowerCase());
			}

			// create BufferedReader to read in the text to spell check
			BufferedReader checkReader = new BufferedReader(new FileReader(wordsToCheck));

			// create variable to hold the line from the input file
			String lineToCheck;

			// create counter to keep track of current line
			int currentLine = 0;

			while ((lineToCheck = checkReader.readLine()) != null)
			{
				// see if the current line is white space, in which case, continue
				if (lineToCheck.matches("\\n") || lineToCheck.matches("\\t") || lineToCheck.matches(" "))
				{
					continue;
				}
				
				// increment current line since we've entered a new line
				currentLine++;

				// create array to hold the words from the line passed in
				String[] wordArray;
				
				// make the line lower case and add individual words to the array by splitting at white space
				wordArray = lineToCheck.toLowerCase().split("\\s+");

				// loop through each word in the array and do a contains check against the dictionary set
				for (int i = 0; i < wordArray.length; i++)
				{
					// use regular expressions to remove all punctuation from the start and the end of the word
					String wordToCheck = wordArray[i].replaceAll("[^A-z0-9]+$", "").replaceAll("^[^A-z0-9]+", "");
					
					// if the word isn't in the dictionary, it's misspelled
					if ((dictionarySet.contains(wordToCheck)) == false)
					{
						//print out that it's misspelled, then see if there are
						//suggestions to be made
						System.out.println("The word " + wordToCheck + " on line " + currentLine + " is misspelled.");

						//store current word as StringBuilder
						StringBuilder currentWord = new StringBuilder(wordToCheck);

						// create linked list to hold the suggested words
						LinkedList<String> suggWords = new LinkedList<String>();

						// HELPFUL SUGGESTIONS
						// loop through and look for helpful suggestions
						
						// REMOVING LETTERS
						for(int b = 0; b < currentWord.length(); b++)
						{
							StringBuilder removedWord = new StringBuilder(currentWord);

							// remove letter at each index and check if it is now in the dictionary
							removedWord = removedWord.deleteCharAt(b);
							if(dictionarySet.contains(removedWord.toString()))
							{
								// if the word is in the dictionary and isn't already stored as a suggested word,
								// add it to the array of suggested words
								if((suggWords.contains((removedWord).toString())) != true)
								{
									suggWords.add(removedWord.toString());
								}
							}
						}
						
						// SWAPPING LETTERS
						for(int c = 0; c < currentWord.length() - 1; c++)
						{
							// create char array of the current word
							char[] chars = (currentWord.toString().toCharArray());
							
							// store the first and second letters
							char firstLetter = chars[c];
							char secondLetter = chars[c+1];
							
							// perform the swap of the letters
							chars[c] = secondLetter;
							chars[c+1] = firstLetter;
							
							// create a string of the char array
							String swapWord = new String(chars);
							
							// compare it against the dictionary; if it exists, add it to the array of suggested words
							if(dictionarySet.contains(swapWord))
							{
								suggWords.add(swapWord);
							}
						}
						
						// print the suggested words
						for (int k = 0; k < suggWords.size(); k++)
						{
							System.out.println("Instead of " + currentWord + ", consider trying: " + suggWords.get(k));
						}
					}

				}
			}
			// close the buffered readers
			reader.close();
			checkReader.close();
		} catch (FileNotFoundException e) {
			// if the file isn't found, tell the user to run the program again
			// and to include the file name as a command line argument
			System.out.println("Run program with a command line argument " + "of your file name.");
		} catch (IOException e) {
			// if there's an IO exception, tell the user
			System.out.println("There is an IO exception; try agagin.");
		} catch (ArrayIndexOutOfBoundsException e) {
			// if the array index is out of bounds, it likely means that the
			// user did not supply a file for the program, so prompt them
			// to do so
			System.out.println("Run the program with a command line argument " + "of your file name.");
		}
	}
}
