package com.mage.util;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;

public class test {
	public static void main(String args[]) {
		String username = "JXSDipW7TlX0ZeGnGZi+pJE+jp1Wb8SGvOEOkdy1z1wCNqGTm+bBWHeaF244dbih+DKTKJrudvkb2w0UlnS6CrX4BMJIjUcrR51RU2IyGg2Edtz6jPtGWcsYzE7vGEk4/krjmtirjTGx6FwuBFU1Ic6PsqYfmcs/1TBgTZWObaw=";
	    PrivateKey privateKey = null;
	    System.out.println(username);
		try {
			privateKey =
	                Ende.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL0Y4V5HSA3ZiDynKX0bfVdr9HKD6zEqlWvgMzqG/bXBJr5jMep3Z8Rkc+xeX6TwGgQyg7G5maIYDhwtJXm1axVoXxY5gAAv8vQJeC9UofgElzN+TweldC43dUcwKWCnHzimY2gkdSo3aQ78cB1y/vGfVjuSgBrG5AWIKq/eg1SlAgMBAAECgYEAk/1W04wBziUCxEUm6SyMadCpHL41YOMs0aJDNXjUMlyZz8KeHBua8E6VktVBETp/ge1ut7bDj+I3mMGUZK4gwOXfyYjRuZa01f3Z89X5aLDRkjOwIm0PmTgEiAEuAIQaYfj/c6Iru+TwCjq5QZyqjLY7ASJ0muzai/0tAOo30QECQQDf6ETVqHZ7WClnoE3JT2ZmQ8CUK/yDzPVnL1Q+HSn8StaUKuZavHGbU5KgrC9dnGQudNEOGPHQGT2tEcSxM2+FAkEA2DNW6YpmxlTUvbHRytMemXkjNVpdnEma5y8osUWDq+IeWnJXmBJu4D6T5K1gwyign5HjmNksNgYK1Fquv0MKoQJAMy0ARqE5a1msJP4zqTZXnjoQEw22ql03Hb1okMXTqdFlF/pyKfz2Ll08nzKbpNaw4xlaCtHSuxB500vDXAj4jQJAclErKo+o6kPevXLxyDo7mtEHweVHTCVLR+SSsrFb/x2wCQkeseVFRUMxdiAK4wZvcBB29NIYY3Rsc36DmdQ8IQJAPSpFpwru315UT4/QCCrq8b/wVA6BbKeptG+ySmhC6cpZVvk2kqtcQekJliGRqUOP0M9EeTofCALJ/Fvm7oD9Mg==");
	        username =
	            new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(username)), "utf-8");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		System.out.println(username);
	}
}
