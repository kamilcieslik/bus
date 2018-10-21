package com.kamilcieslik.pwr.bus.lab1;

import com.kamilcieslik.pwr.bus.commons.TextFileReaderWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
                + String.valueOf(Character.toChars(69)) + String.valueOf(Character.toChars(211))
                + String.valueOf(Character.toChars(255));
        verify(textFileReaderWriter, times(1)).writeToFile("any", expectedCompressedContent);
    }
}
