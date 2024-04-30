package com.loong;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class KeyTest {

    @Test
    public void parseDERFromPEM() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM00WbC52QTBZczrcKVYi/FzI/FTpxfY8/ngaGjbNXE3ZdEOvzyNTJtsefceDAQsXsTV7/lgNvP5vYTUjT5HuqVfDxZW9Clw8Z4Ia9vo30es/3luMlD9G+2wQSyqEEBPEq+RXmVc4q9ECk/KHTkinAdeqF+yf/Uu8btG2HHqoeBXAgMBAAECgYAJO2w3l7zsSlkupX8NaVTueXwp/NX9LC0Z1l9MLGSUoeXqSGwRKNhD+9bm58qpm2duGqPvx8ThKCC/K6LpJOgAOP2kEzq6tKy3pODleKM6WMzPDGuWnEHxTfnALPPGNtvsFumi7CPZVw18uWCD3kobRPcYaVrCCLUodMAhBF7wZQJBAPen4aCzs1y42K0n8MzGFdkmjU+SqrU+iAm2jnmm4anFHBFq0m4TkXnTZF9Dm7drAIzEQYVcj+OIeVdn9u6w3LsCQQDUHk/Ez9AfpVEBGGOpYwoliPiD+oILj6QVf1xyJD5V7yjk/qiHKUjc8Ke049a9SR15hCUVSliI0oRoKRaHbX8VAkEAwG9m1uRLfsTzOLlMVxGkcHOQYkBo/SR1uHZuBwExYCWVRcNnd7kk1TD5UTzLhp5Scw8cim/+LqMhBlX2fkpXeQJAdUQYp16X2ZbkNG3OUwuMxXl2okfwcwHDHZteeT+uMoXYHtjmRGq/fF2S6X737YcpQ0+ZzK5gSavQRWFJsi8HjQJAemAAdzN1N1HVZ65UwKN/vrtX4AcXy9X5CATeCgZfVIc0gc0z7B4YSdZYMtYzeADd963qZvTtnzRW/aQvQtTg6Q==";
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPrivateKey priKey = (RSAPrivateKey) factory.generatePrivate(spec);
    }

    @Test
    public void parseDERFromPEM2() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String privatekey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDYPducgwi8cvAc4Uw2AMEJzb4j3gRO+dGmb69NoJ8SFWruaFSND1sV6/Gs8ssPaG9+GUMmD2DMhzRxeW5TS1ycPxL2L7B7jBGys1j+0z7VUjCnY3+2M42qVs53RqyHqH48Gc1I+yCuDYTIq+OnLRuRidUBLj1qcfxziLZPSqjkHOcDCxmglTKghh9QbQYIdqGVnR5whqFXXc3TrGGIOvQM/y/ke2IDE45xMkYfvNRwpor4jYbAvx0XlXkmjwP1zC8g7uWg8pdQA5fTQOfq5GTWm9VJQgDZsFsXypTURcvhobxKn+zMtZ1TboR6ooRpjXgLQ5CpgQbRYFeYQQWrvzgNAgMBAAECggEAHuWXv2EpmmOB016NmRm6e0AebtmGHBKt1e4XSiwtDlRpFu9pRU5wYU15FpEKWoU9iPLrGUERP6gB4kTLcrNN5DVO7EYcHUsCLFpb2O0uCSTZNtGMqefr0e7dSUWb2TZF8yhsoIThQn1D/7gvqu9TBRSAjs6gj9kBjTB3fBTa1NzZBMG/RJwP7UreGhhkDHEj464ywFjGszkKJrCUqJCXpdGJaV0J76OSk5lQVmNCCyWhaBZF0z2SSsIeRwckPyGULJfkcSdM5DqzHEDup7b1sTSn+zW+Lv6O9kvQKuxjT6a81ezG/k0Kg0U/XK/geUani10sUq3gM1V7oLsxa+8CgQKBgQD25hrc6x7o2OznlTYvhGPFtzFZ/Dcz+r46jhTa2L+aeHPrEU/UMx2bQoNAs6S2RxQ540SsFaMn1sk9mSdBtmFP0qnYIvcARM2CHhyu6qEU7AiF2AKvq6OU9sPdMFZNtK3D5szo1JmGdzUr2ZwiCKf0nkRop3+VKyKZeIzMHSRZJQKBgQDgNnOuX6L/7ZPgOJODKibAnfQtCZe8pOoCKkmu5ZQO7yq6vVdsqMU59Sp8FJ8M0Yy5eF50RVD0CovnUVSkOOpmML34k6Rg0QusUWiZbzbOp3oEUbS56/qtSDV0M/DaeFoYMbITFdp0Pap05ih4p8hLDYKcCZmyWr1/ZyUhMdwyyQKBgBq11h4kSZnDLg33tH77gZe4M9yaTpaO/9lx2Yu7ey8I7PF3U/2O8hsD3RM43A7kLan9jkZXqOj8cZT6ZqbTRj/OeJVrYvFigCEdhNjfhUGwL1PbhJ7goLfS4mmhexS5ATt6Upy1s046w0PVLPD5osHyBVCVHlZS7lFQG1tRo8cJAoGANxWJpCyA2kmfC/Y6X34qmSX1SF9T9Re7Gh8TjTGAB49AuzIrWVm5YrzV3Dggv0B4R1TIN96TvfEdYA4+Mj59b+/HrFtxhgu1x3cUsKV/HJROyOLd+NN9+baE03CzWqk40TrcWg4DZo2D5EZraQEnlzDRhs5hAU2DwtovaBFc3OkCgYEAx9JNKihuzCizby/ywDlU9yswkw1XdyAKNSl+obAqNvSqcRgK2LuYmhLrXbmqIEtzDBgiWMcfAvsjSzTVgLhM3zV2QLnMaKbKgIpy0f4MVrLigB74PJqlJER4gav3u7v936SXohiz9yEubj/+5EGGFLoDp0dfrERqLg6R9FhpIFQ=";
        byte[] keyBytes = Base64.getDecoder().decode(privatekey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        RSAPrivateKey priKey = (RSAPrivateKey) factory.generatePrivate(spec);
    }

    public static void main(String[] args) {
        //加密（参数1：待加密的密码，参数2：调用获取盐值的方法）
        String hashpw = BCrypt.hashpw("123456", BCrypt.gensalt("$2a"+"123456"));
        System.out.println(hashpw);

        //判断密码是否相同
        boolean checkpw1 = BCrypt.checkpw("123456", "$2a$10$yVodUhUu2o6BYlF3TBplQO1BEwS7uolG7ofIipc6mIJGdtFbjIWhO");
        System.out.println(checkpw1);//true

        boolean checkpw2 = BCrypt.checkpw("123456", hashpw);
        System.out.println(checkpw2);//false
    }

}