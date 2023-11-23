package org.timequeue.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PBKDF2Test {
    final private static String PRINTABLE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    private static String generatePassword(int length, String characterSet) {
        final SecureRandom random = new SecureRandom();
        return IntStream
                .range(0, length)
                .map(i -> random.nextInt(characterSet.length()))
                .mapToObj(randomIndex -> String.valueOf(characterSet.charAt(randomIndex)))
                .collect(Collectors.joining());
    }

    @Test
    void generateStorngPasswordHash() throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String plainPassword = generatePassword(ThreadLocalRandom.current().nextInt(3, 20), PRINTABLE_CHARS);
        final String hashedPassword = PBKDF2.generateStorngPasswordHash(plainPassword);
        Assertions.assertTrue(PBKDF2.validatePassword(plainPassword, hashedPassword));
    }
}