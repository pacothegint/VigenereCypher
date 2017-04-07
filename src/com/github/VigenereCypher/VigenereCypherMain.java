package com.github.VigenereCypher;
/**
 * Andrew Harbor
 * CYEN301 -002
 * Problem #2: ViegenereCipher
 *
 * 2017-03-29
 *
 * Takes the text from an input file and either deciphers or ciphers the text based on an inputed key.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VigenereCypherMain {

	/*This is used to find the index(number representation) and the letter*/
	private String alphabet = "abcdefghijklmnopqrstuvwxyz";

	/**
	 * The main method used to instantiate the class
	 * 
	 * @param args
	 *            An array of strings used as arguments separated by spaces.
	 *            IE(-e, -d, key)
	 */
	public static void main(String[] args) {
		String cipherMode = "";
		String cipherKey = "";

		if (args.length != 2) {
			
			/* Incorrect arguments were given */
			System.out.print("Incorrect arguemnts given! Please follow this format: ");
			System.out.println("Java ViegenerCypher -e thisismykey");
			System.out.print("If you want a sentence as your key use: ");
			System.out.println("Java ViegenerCypher -e \"This is my key\"");
			System.exit(0);
		} else {
			
			/* At least one argument was given */
			if (args[0].equalsIgnoreCase("-e") || args[0].equalsIgnoreCase("-d")) {
				
				/* The argument was a known value */
				cipherMode = args[0];
				cipherKey = args[1].replaceAll(" ", "").toLowerCase();
			} else 
			{
				
				/* Unknown cipher mode argument */
				System.out.println("Unkown Command!");
				System.out.println("Use -e for encryption or -d for decryption");
				System.exit(0);
			}
			VigenereCypher vc = new VigenereCypher(cipherMode, cipherKey);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param mode
	 *            The cipher mode the user wants
	 * @param key
	 *            The key the user wants to cipher with
	 */
	private VigenereCypherMain(String mode, String key) {
		String Filename = "stdin.txt";
		File output = new File("stdout.txt");
		writeToFile(Filename, key, mode, output);
	}

	/**
	 * Prints the ciphered string returned by the vigenereCiphering() method.
	 * 
	 * @param fileName
	 *            The name of the file to read.
	 * @param key
	 *            They key that will be used to cipher.
	 * @param mode
	 *            Does the user want to cipher or decipher
	 * @param output
	 *            The file that will either be created or written to.
	 * @see #vigenereCiphering(String, String,String)
	 */
	private void writeToFile(String fileName, String key, String mode, File output) {
		String currentLineFromFile;
		String fullTextFromFile = "";

		try (BufferedReader br = new BufferedReader(new FileReader(fileName));
				BufferedWriter bw = new BufferedWriter(new FileWriter(output))) {
			/*
			 * Read until the end of the file line by line then add it to the
			 * fullText
			 */
			while ((currentLineFromFile = br.readLine()) != null) {
				fullTextFromFile += currentLineFromFile + "\r\n";
			}

			/* Write the return value of vigenereCiphering() */
			bw.write(vigenereCiphering(fullTextFromFile, key, mode));

		} catch (IOException e) /* Unable to read/write the files */
		{
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Takes the input text, key, and mode (as strings) and return either
	 * ciphered or deciphered text based on mode
	 * 
	 * @param text
	 *            The text that needs to be ciphered
	 * @param key
	 *            The key that will be used in the ciphering
	 * @param mode
	 *            Does the user want to cipher or decipher
	 * @return The ciphered or deciphered text
	 */
	private String vigenereCiphering(String text, String key, String mode) {

		String finalText = "";

		/* The character that is being ciphered*/
		Character currentCharacter;

		/* The indexValue of the key */
		int currentKeyValue = 0;

		/* The current character of the key */
		Character currentKeyCharacter;

		/*
		 * Takes the input text and grabs the first Character of the string.
		 * Either changes the character based on case and the key or simply
		 * returns it.
		 */
		for (int i = 0; i < text.length(); i++) {
			currentCharacter = text.substring(i, text.length()).charAt(0);

			/* If they key value is over the length. Reset it. */
			if (currentKeyValue > key.length() - 1) {
				currentKeyValue = 0;
			}
			currentKeyCharacter = key.charAt(currentKeyValue);

			/* Is the character a Letter? */
			if (Character.isLetter(currentCharacter)) {
				if (Character.isUpperCase(currentCharacter)) {
					currentCharacter = Character.toLowerCase(currentCharacter);
					currentCharacter = getCharacter(currentCharacter, currentKeyCharacter, mode);
					currentCharacter = Character.toUpperCase(currentCharacter);
					currentKeyValue++; /* Increment key value by 1 */
				} else {
					currentCharacter = getCharacter(currentCharacter, currentKeyCharacter, mode);
					currentKeyValue++; /* Increment key value by 1 */
				}
				finalText += currentCharacter;
			} else {
				finalText += currentCharacter;

				/*
				 * Do not increment key value by 1 because the character is not
				 * a letter.
				 */
			}
		}
		return finalText;
	}

	/**
	 * Takes the characters given and performs the VigenereCipher on them I.E. (
	 * (charOfOrigionalText + charOfKey) % 26)
	 * 
	 * @param inputTextCharacter
	 *            The character from the input text
	 * @param keyCharacter
	 *            The character from the keyCharacter
	 * @param mode
	 *            Does the user want to cipher or decipher
	 * @return the character at the index of the resulting equation
	 */
	private char getCharacter(Character inputTextCharacter, Character keyCharacter, String mode) {
		
		/*These represent the */
		int inputTextCharIndex = alphabet.indexOf(inputTextCharacter);
		int keyCharIndex = alphabet.indexOf(keyCharacter);
		
		/*Will always be 26 unless I add more letters to alphabet*/
		int alphabetSize = alphabet.length(); 
		int indexOfCharacter = 0;
		if (mode.equalsIgnoreCase("-e")) {
			
			/*(Index of input char + index of key char) % alphabet length (26)*/
			indexOfCharacter = (inputTextCharIndex + keyCharIndex) % alphabetSize; 
		} else if (mode.equalsIgnoreCase("-d")) {
			indexOfCharacter = (inputTextCharIndex - keyCharIndex + alphabetSize) % alphabetSize;
		}
		return alphabet.charAt(indexOfCharacter);
	}
}