package com.worldelite.job;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class UtilsTests {

    @Test
    public void gen_pass(){
        String password = "WorldElite#21";
        String salt = RandomStringUtils.randomAlphanumeric(40);
        String enPassw = DigestUtils.sha256Hex(password + salt);
        System.out.println(String.format("enPass: %s, salt: %s", enPassw, salt));
    }

}
