package com.kamilcieslik.pwr.bus.lab1;

import com.kamilcieslik.pwr.bus.commons.TextFileReaderWriter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Value
@RequiredArgsConstructor
public class DictionaryCompression {
    private TextFileReaderWriter textFileReaderWriter;

    public void compressFile(String filename, String compressedFilename) {
        String text = textFileReaderWriter.readFromFile(filename);
        var result = new StringBuffer();

        var dictionary = text.chars().mapToObj(character -> (char) character).distinct().collect(Collectors.toList());
        int x = dictionary.size();
        int n = BigDecimal.valueOf(Math.log(x) / Math.log(2)).setScale(0, RoundingMode.UP).intValue();
        int k = (8 - (3 + text.length() * n) % 8) % 8;

        var dictionaryWithBinaryValues = assignToDictionaryElementsBinaryValues(dictionary, n);
        log.info("Dictionary with binary values: {}, dictionary size: {}, n: {}, k: {}", dictionaryWithBinaryValues, x, n, k);

        result.append((char) x);
        dictionary.forEach(result::append);

        var bits = new StringBuffer(Integer.toBinaryString(k));
        var otherBits = new StringBuffer(StringUtils.EMPTY);
        text.chars().mapToObj(c -> (char) c).forEach(character -> {
            if (otherBits.length() > 0) {
                bits.append(otherBits);
                otherBits.delete(0, otherBits.length());
            }

            bits.append(dictionaryWithBinaryValues.get(character));

            if (bits.length() > 8) {
                otherBits.append(bits.substring(8, bits.length()));
            }

            if (bits.length() >= 8) {
                result.append(convertBitsToCharacter(bits.substring(0, 8)));
                bits.delete(0, bits.length());
            }
        });

        if (otherBits.length() > 0) {
            log.info("Last bits '{}'", otherBits.toString());
            result.append(convertBitsToCharacter(StringUtils.rightPad(otherBits.toString(), 8, "1")));
        }

        textFileReaderWriter.writeToFile(compressedFilename, result.toString());
    }

    Map<Character, String> assignToDictionaryElementsBinaryValues(List<Character> dictionary, Integer bitsPerChar) {
        AtomicReference<Integer> idx = new AtomicReference<>(0);
        Map<Character, String> characterBinaryValues = new HashMap<>();
        dictionary.forEach(character -> {
            characterBinaryValues.put(character, StringUtils.leftPad(Integer.toBinaryString(idx.get()), bitsPerChar, "0"));
            idx.getAndSet(idx.get() + 1);
        });
        return characterBinaryValues;
    }

    Character convertBitsToCharacter(String bits) {
        Character character = (char) Integer.parseInt(bits, 2);
        log.info("Conversion bits '{}' to '{}' character", bits, character);
        return character;
    }
}