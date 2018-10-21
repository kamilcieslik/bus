package com.kamilcieslik.pwr.bus.lab1;

import com.kamilcieslik.pwr.bus.commons.TextFileReaderWriter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
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

        var dictionary = text.chars().mapToObj(character -> (char) character).collect(Collectors.toSet());

        AtomicReference<Integer> idx = new AtomicReference<>(0);
        Map<Character, Integer> characterValues = new HashMap<>();
        dictionary.forEach(character -> {
            characterValues.put(character, idx.get());
            idx.getAndSet(idx.get() + 1);
        });

        int x = dictionary.size();
        int n = BigDecimal.valueOf(Math.log(x) / Math.log(2)).setScale(0, RoundingMode.UP).intValue();
        int k = (8 - (3 + text.length() * n) % 8) % 8;
        log.info("Dictionary: {}, dictionary size: {}, n (bits per char): {}, k: {}", dictionary, x, n, k);

        result.append((char) x);
        dictionary.forEach(result::append);

        textFileReaderWriter.writeToFile(compressedFilename, result.toString());
    }

    Character convertBitsToCharacter(String bits) {
        return (char) Integer.parseInt(bits, 2);
    }
}
