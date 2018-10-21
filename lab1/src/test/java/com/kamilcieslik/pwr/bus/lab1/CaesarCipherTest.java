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
public class CaesarCipherTest {
    @Mock
    private TextFileReaderWriter textFileReaderWriter;
    private CaesarCipher caesarCipher;

    @Before
    public void before() {
        caesarCipher = new CaesarCipher(textFileReaderWriter);
    }

    @Test
    public void encryptTextTest() {
        // given
        String fileContent = "abcDEFG123.$.321GFEDcbayZ";
        Integer key = 5;
        when(textFileReaderWriter.readFromFile(any())).thenReturn(fileContent);

        // when
        caesarCipher.encryptFile("any", "any", key);

        // then
        String expectedEncryptedContent = "ﾙﾘﾗﾶﾵﾴﾳ123.$.321ﾳﾴﾵﾶﾗﾘﾙﾛﾺ";
        verify(textFileReaderWriter, times(1)).writeToFile("any", expectedEncryptedContent);
    }

    @Test
    public void decryptTextTest() {
        // given
        String fileContent = "ﾙﾘﾗﾶﾵﾴﾳ123.$.321ﾳﾴﾵﾶﾗﾘﾙﾛﾺ";
        Integer key = 5;
        when(textFileReaderWriter.readFromFile(any())).thenReturn(fileContent);

        // when
        caesarCipher.decryptFile("any", "any", key);

        // then
        String expectedDecryptedContent = "abcDEFG123.$.321GFEDcbayZ";
        verify(textFileReaderWriter, times(1)).writeToFile("any", expectedDecryptedContent);
    }
}