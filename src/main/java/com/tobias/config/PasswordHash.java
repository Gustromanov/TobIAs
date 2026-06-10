package com.tobias.config;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {

    // Gera o hash da senha
    public static String hashPassword(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    // Verifica se a senha digitada bate com o hash
    public static boolean checkPassword(String senhaDigitada, String hashSalvo) {
        return BCrypt.checkpw(senhaDigitada, hashSalvo);
    }

    public static boolean isBcryptHash(String value) {
        return value != null && value.startsWith("$2");
    }
}

