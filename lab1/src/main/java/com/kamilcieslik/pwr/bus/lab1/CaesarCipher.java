package com.kamilcieslik.pwr.bus.lab1;

import com.kamilcieslik.pwr.bus.commons.TextFileReaderWriter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Value
@RequiredArgsConstructor
public class CaesarCipher {
    private TextFileReaderWriter textFileReaderWriter;

    public void encryptFile(String filename, String encryptedFilename, Integer key) {
        String text = textFileReaderWriter.readFromFile(filename);
        var result = new StringBuffer();

        text.chars().mapToObj(c -> (char) c).forEach(character -> {
            Optional<Character> newCharacter = Optional.empty();

            if (Character.isUpperCase(character)) {
                newCharacter = Optional.of((char) ((character + key - 65) % 26 + 65));
            } else if (Character.isLowerCase(character)) {
                newCharacter = Optional.of((char) ((character + key - 97) % 26 + 97));
            }

            newCharacter.ifPresentOrElse(
                    encryptedCharacter -> {
                        var encryptedCharacterAfterInversion = (char) ~encryptedCharacter;
                        result.append(encryptedCharacterAfterInversion);
                        log.info("Encryption of a letter from '{}' to '{}' and after inversion to '{}'", character,
                                encryptedCharacter, encryptedCharacterAfterInversion);
                    },
                    () -> {
                        result.append(character);
                        log.info("The letter '{}' does not required conversion", character);
                    });
        });

        textFileReaderWriter.writeToFile(encryptedFilename, result.toString());
    }

    public void decryptFile(String filename, String decryptedFilename, Integer key) {
        String text = textFileReaderWriter.readFromFile(filename);
        var result = new StringBuffer();

        text.chars().mapToObj(c -> (char) c).forEach(character -> {
            Optional<Character> newCharacter = Optional.empty();

            char encryptedCharacterAfterNegativeInversion = (char) ~character;
            if (Character.isUpperCase(encryptedCharacterAfterNegativeInversion)) {
                newCharacter = Optional.of((char) ((encryptedCharacterAfterNegativeInversion - key - 65) % 26 + 65));
            } else if (Character.isLowerCase(encryptedCharacterAfterNegativeInversion)) {
                newCharacter = Optional.of((char) ((encryptedCharacterAfterNegativeInversion - key - 97) % 26 + 97));
            }

            newCharacter.ifPresentOrElse(
                    decryptedCharacter -> {
                        result.append(decryptedCharacter);
                        log.info("Conversion letter '{}' after negative inversion to '{}' and after decryption to '{}'",
                                character, encryptedCharacterAfterNegativeInversion, decryptedCharacter);
                    },
                    () -> {
                        result.append(character);
                        log.info("The letter '{}' does not required conversion", character);
                    });
        });

        textFileReaderWriter.writeToFile(decryptedFilename, result.toString());
    }
}
