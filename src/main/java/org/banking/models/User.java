package org.banking.models;

import jakarta.servlet.http.HttpServletRequest;

import java.security.SecureRandom;

public class User {

    private final String full_name;
    private final String email;
    private final String mobile;
    private final String account_type;
    private final String date_of_birth;
    private final String address;
    private final String id_proof;

    private int account_number;
    private final String password;


    public User(HttpServletRequest request) throws InterruptedException {
        full_name = request.getParameter("full_name");
        email = request.getParameter("email");
        mobile = request.getParameter("mobile");
        account_type = request.getParameter("account_type");
        date_of_birth = request.getParameter("date_of_birth");
        address = request.getParameter("address");
        id_proof = request.getParameter("id_proof");

        password = generateSting(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "abcdefghijklmnopqrstuvwxyz" +
                        "0123456789!@#$", 8);
    }

    public static String generateSting(String chars, int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    public String get_full_name() {return full_name;}
    public String get_email() {return email;}
    public String get_mobile() {return mobile;}
    public String get_account_type() {return account_type;}
    public String get_date_of_birth() {return date_of_birth;}
    public String get_address() {return address;}
    public String get_id_proof() {return id_proof;}

    public int get_account_number() {return account_number;}
    public String get_password() {return password;}

    public void change_account_number() {account_number = Integer.parseInt(generateSting("1234567890", 5));}

}
