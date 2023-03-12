package com.alcorross;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckMessageTest {
    private static final CheckMessage checkMessage = new CheckMessage();

    @Test
    void checkCharacterShouldReturnTrue() {
        assertTrue(checkMessage.checkCharacter("Л"),"The method must return true");
    }

    @Test
    void checkCharacterShouldReturnFalse() {
        assertFalse(checkMessage.checkCharacter("Fg"),"The method must return false");
    }
}
