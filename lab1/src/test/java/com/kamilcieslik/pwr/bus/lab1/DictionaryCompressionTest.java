package com.kamilcieslik.pwr.bus.lab1;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kamilcieslik.pwr.bus.commons.TextFileReaderWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryCompressionTest {
    @Mock
    private TextFileReaderWriter textFileReaderWriter;
    private DictionaryCompression dictionaryCompression;

    @Before
    public void before() {
        dictionaryCompression = new DictionaryCompression(textFileReaderWriter);
    }

    @Test
    public void convertBitsToCharacterTest() {
        // given
        String bits = "00110001";

        // when
        char result = dictionaryCompression.convertBitsToCharacter(bits);

        // then
        assertEquals((char) 49, result);
    }

    @Test
    public void assignToDictionaryElementsBinaryValuesTest() {
        // given
        Integer bitsPerChar = 10;
        var dictionary = new ImmutableList.Builder<Character>().add('A').add('B').add('D').add('C').build();

        // when
        var result = dictionaryCompression.assignToDictionaryElementsBinaryValues(dictionary, bitsPerChar);

        // then
        assertEquals(new ImmutableMap.Builder<Character, String>()
                .put('A', "0000000000")
                .put('B', "0000000001")
                .put('C', "0000000011")
                .put('D', "0000000010")
                .build(), result);
    }

    @Test
    public void compressTextTest() {
        // given
        String fileContent = "AACCCAACBADBBCD";
        when(textFileReaderWriter.readFromFile(any())).thenReturn(fileContent);

        // when
        dictionaryCompression.compressFile("any", "any");

        // then
        String expectedCompressedContent
                = String.valueOf(Character.toChars(4)) + "ACBD"
                + String.valueOf(Character.toChars(224)) + String.valueOf(Character.toChars(168))
                + String.valueOf(Character.toChars(49)) + String.valueOf(Character.toChars(211))
                + String.valueOf(Character.toChars(255));
        verify(textFileReaderWriter, times(1)).writeToFile("any", expectedCompressedContent);
    }
}