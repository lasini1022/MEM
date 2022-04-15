import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Map.Entry;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;

public class MEM {
    static Scanner in = new Scanner(System.in);
    static File file1 = new File("PendingPayments.txt");
    static File file2 = new File("Balances.txt");
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    static String resetCNum;

    public static void main(String args[]) throws IOException {
        // Creates a manager object and catched IOException
        clearConsole();

        // Reads each member's info from the PendingPayments.txt file.
        // Adds the member's email and the PendingAmount to the treeMap
        ClubManager.fromFile(file1);

        // Reads each member's info from the PendingPayments.txt file.
        // Adds the member's email and the PendingAmount to the treeMap
        ClubManager.fromFile(file2);
        Finances.getData();
        ClubManager manager = null;
        try {
            manager = new ClubManager();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Gives the user the option of registering or logging in
        // Enter 1 to register, enter 2 to log in
        System.out.println("\n*** Welcome to the Recreation Club Membership App ***\n");
        System.out.print("Register(1)\t");
        System.out.print("Login(2)\t");
        System.out.print("Forgot Password?(3)\n");
        System.out.print("\n> ");

        int input = convertInputToInteger(3, 1);

        // If the input is a 1, allow the user to register
        if (input == 1) {
            try {
                clearConsole();
                System.out.println("*** Registration ***\n");
                RegisterationQuestions(manager); // Shows the registration questions
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            clearConsole();
            System.out.print("Registration Complete\n");
            System.out.println("Please run the program again to log in.\n");

        }

        // For logging in.
        if (input == 2) {
            log_in();
        }

        if (input == 3) {
            reset_password();
        }
    }

    public static void reset_password() throws IOException {
        Boolean flag = true;// true means that email is incorrect
        String email;
        AMember n = null;
        do {
            System.out.print("Please enter your email: ");
            email = in.nextLine();
            for (Entry<String, AMember> entry : ClubManager.members.entrySet()) {
                if (entry.getKey().equals(email)) {
                    flag = false;
                    n = entry.getValue();
                }
            }
            if (n == null) {
                System.out.println("Your email is incorrect.");
                flag = true;
            }
        } while (flag);

        ConfirmNumEmail(email, "Ehansa Kuruku");

        System.out.print("\nPlease enter the confirmation number: ");
        String cNum = in.nextLine();
        String newP;
        String newP2;

        if (cNum.equals(resetCNum)) {
            System.out.print("Enter new password: ");
            newP = in.nextLine();
            System.out.print("Enter new password again to confirm: ");
            newP2 = in.nextLine();
            while (!(newP.equals(newP2))) {
                System.out.println("Passwords do not match.");
                System.out.print("Enter new password: ");
                newP = in.nextLine();
                System.out.print("Enter new password again to confirm: ");
                newP2 = in.nextLine();
            }
            n.setPassword(newP);
            ClubManager.toFile("User_Info.txt");
            clearConsole();
            System.out.println("Password Successfully Changed.");
        } else {
            System.out.println("The confirmation number does not match.");
            System.out.print("Restart resetting password process (R)");
            String o = in.nextLine();

            if (o.equalsIgnoreCase("R")) {
                reset_password();
            }
        }

    }

    public static void registerLogin() {
        System.out.println("\n*** Welcome to the Recreation Club Membership App ***\n");
        System.out.print("Register(1)\t");
        System.out.print("Login(2)\n");
    }

    public static void loginExit() {
        System.out.print("Login(1)\t");
        System.out.print("Exit(2)\n");
        System.out.print("\n> ");
    }

    public static void chooseAppFeature(int num) throws IOException {
        if (num == 1) {
            registerLogin();
        } else if (num == 2) {
            loginExit();
        } else if (num == 3) {
            reset_password();
        }

    }

    // Converts user input fot choosing what to do in the app to an integer
    // Max input is the maximum number the user can enter
    public static int convertInputToInteger(int maxInput, int featureNum) throws IOException {
        Boolean checkValidInput = false;
        int input = 0;
        while (checkValidInput == false || (input < 1 || input > maxInput)) {
            try {
                input = Integer.parseInt(in.nextLine());
                checkValidInput = true;
                if (input < 1 || input > maxInput) {
                    clearConsole();
                    chooseAppFeature(featureNum);
                    System.out.println("\nPlease enter a valid number");
                    System.out.print("> ");
                    checkValidInput = false;

                }
            } catch (NumberFormatException e) {
                clearConsole();
                chooseAppFeature(featureNum);
                System.out.println("\nPlease enter a valid number");
                System.out.print("> ");
            }
        }
        return input;

    }

    public static void RegisterationQuestions(ClubManager manager) throws IOException {

        // Asks for the first name of the user
        String firstName = "";
        while (firstName == "" || firstName == null) {
            System.out.print("Enter your first name: ");
            firstName = in.nextLine();
            if (firstName == "" || firstName == null) {
                clearConsole();
                System.out.println("*** Registration ***\n");
                System.out.println("Can't leave first name empty.\n");
            }
        }
        clearConsole();
        System.out.println("*** Registration ***\n");

        // Asks for the last name of the user
        String lastName = "";
        while (lastName == "" || lastName == null) {
            System.out.print("Enter your last name: ");
            lastName = in.nextLine();
            if (lastName == "" || lastName == null) {
                clearConsole();
                System.out.println("*** Registration ***\n");
                System.out.println("Can't leave last name empty.\n");
            }
        }
        clearConsole();
        System.out.println("*** Registration ***\n");

        // Asks for phone number of the user
        String phoneNumber = "";
        while (phoneNumber == "" || phoneNumber == null) {
            System.out.print("Enter your phone number: ");
            phoneNumber = in.nextLine();
            if (phoneNumber == "" || phoneNumber == null) {
                clearConsole();
                System.out.println("*** Registration ***\n");
                System.out.println("Can't leave phone number empty.\n");
            }
        }
        clearConsole();
        System.out.println("*** Registration ***\n");

        // Asks for the email of the user
        // Makes sure that a person cannot register with an email that is already being
        // used
        Boolean emailFound = true;
        String email = "";
        while (emailFound == true || email == "" || email == null) {
            System.out.print("Enter your email: ");
            email = in.nextLine();
            emailFound = manager.checkEmail(email);

            if (emailFound == true) {
                clearConsole();
                System.out.println("*** Registration ***\n");
                System.out
                        .println("This email is already being used by a current member. Please enter another email.\n");
            }

            if (email == "" || email == null) {
                clearConsole();
                System.out.println("*** Registration ***\n");
                System.out.println("Can't leave email empty.\n");
            }
        }
        clearConsole();
        System.out.println("*** Registration ***\n");

        // Asks user for a password.
        // Makes sure password is longer than 8 characters
        String password = "0";

        // Asks user to verify password
        // Makes sure that the password is the same as the password entered before.
        String password2 = "1";
        while (password.equals(password2) == false) {
            while (password.length() < 8) {
                System.out.print("Enter your password: ");
                password = in.nextLine();

                if (password.length() < 8) {
                    clearConsole();
                    System.out.println("*** Registration ***\n");
                    System.out.println("Password needs to be at least 8 characters.");
                }
                System.out.println();
            }

            System.out.print("Re-Enter your password: ");
            password2 = in.nextLine();

            if (password.equals(password2) == false) {
                clearConsole();
                System.out.println("*** Registration ***\n");
                System.out.println("Passwords do not match. Please enter your password again.");
                System.out.println();
                password = "-1";
            }
        }
        clearConsole();
        System.out.println("*** Registration ***\n");

        // Asks for phone number of the user
        String address = "";
        while (address == "" || address == null) {
            System.out.print("Format: Unit#-Building#-Street or House#-Street\nEnter your address: ");
            address = in.nextLine();
            if (address == "" || address == null) {
                clearConsole();
                System.out.println("*** Registration ***\n");
                System.out.println("Can't leave address empty.\n");
            }
        }
        manager.registerMember(firstName, lastName, phoneNumber, email, password, address);
        MemberBalance person = new MemberBalance(email, 0, 0, 0);
        ATreasurer.balance.put(email, person);
        ClubManager.toFile("Balances.txt");
    }

    // Log in Feature
    public static void log_in() throws IOException {
        System.out.print("Email: ");
        String email = in.nextLine();
        System.out.print("Password: ");
        String password = in.nextLine();

        ArrayList<AMember> people = new ArrayList<>();

        for (Map.Entry<String, AMember> entry : ClubManager.members.entrySet()) {
            people.add(entry.getValue());
        }

        Boolean flag = true;
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getEmail().equals(email) && people.get(i).getPassword().equals(password)) {
                AfterLogIn(people.get(i));
                flag = false;
            }
        }
        if (flag) {
            ErrorPrintMessage();
        }

    }

    public static void ErrorPrintMessage() throws IOException {
        System.out.println(
                "You have entered one or more of the following pieces of information incorrectly: username and/or password.");
        System.out.println("Please try again");
        log_in();
    }

    /*
     * Alpha-Numeric_String as a randomly generated confirmation code
     */
    public static String AN_String(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /*
     * Confirmation email with confirmation number
     */

    public static void ConfirmNumEmail(String personEmail, String fullname) throws IOException {
        String username = "group66club@gmail.com";
        String password = "april2022";
        resetCNum = AN_String(8);// confirmation number
        String recipient = personEmail;

        // For a single person, get rid of from here
        // ClubManager manager = null;
        // try {
        // manager = new ClubManager();
        // } catch (IOException e) {
        // System.out.println(e.getMessage());

        String subj = "Confirmation Number";
        String body = "Hello,\n\n"
                + "Confirmation Number: " + resetCNum + "\n\n"
                + "If in the app, you were asked to enter a confirmation number, please enter the above confirmation number.";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "imap.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subj);
            message.setText(body + "\n\n" + fullname);
            Transport.send(message);
            System.out.println("Confirmation Number Sent");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void returnOrExit(AMember member) throws IOException {
        System.out.print("Return to Main Screen(1)\t");
        System.out.print("Exit(2)\n");
        System.out.print("\n> ");

        int input = 0;
        int maxInput = 2;
        while ((input < 1 || input > maxInput) && in.hasNextInt()) {

            input = Integer.parseInt(in.nextLine());
        }
        if (input == 1) {
            clearConsole();
            AfterLogIn(member);
        }
        // For exiting the annoucement feature.
        else if (input == 2) {
            System.out.println("\nHave a nice day!\n");
            System.exit(0);
        }
    }

    // After log in options for staff and players
    public static void AfterLogIn(AMember member) throws IOException {
        clearConsole();

        // Gives the user the option of entering S to send announcement, F for finance
        // feature,
        // P for practice schedule/scheduling, and E to exit
        System.out.println("\n*** Welcome to the Recreation Club Membership App ***\n");
        if (member.getRole().equals("Coach")) {
            System.out.print("Send Annoucement (S)\t");
            System.out.print("Attendance (A)\t");
            System.out.print("Edit List of Members (M)\t");
        } else if (member.getRole().equals("Treasurer")) {
            System.out.print("Pending Payments List (L)\t");
            System.out.print("Attendance (A)\t");
            System.out.print("Change Coach (CC)");
            System.out.print("Check Coach for Current Month (CM)");
        }
        System.out.print("Finances (F)\t");
        System.out.print("Practice Schedule (P)\t");
        System.out.print("Change Password (C))\t");
        System.out.print("Exit (E)\n");
        System.out.print("\n> ");

        String option = in.nextLine();

        // If the input is a 1, go to annoucemnets
        if (option.equalsIgnoreCase("S")) {
            try {
                clearConsole();
                System.out.println("*** Send Announcement ***\n");
                // method for sending email through java code
                sendAnnouncements(member.email, member.password, member.firstName + member.lastName);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            clearConsole();
            System.out.println("Announcement Successfully Sent\n");

            // Allows the user to choose if they want to return to the main screen or exit
            // after senting a annoucement
            returnOrExit(member);
        } else if (option.equalsIgnoreCase("L")) {
            clearConsole();
            ClubManager.fromFile(new File("PendingPayments.txt"));
            ClubManager.fromFile(new File("Balances.txt"));

            System.out.println("Showing list of pending payments\n");

            try {
                ATreasurer.Choose();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
            returnOrExit(member);

        } else if (option.equalsIgnoreCase("F")) {
            // insert finance code method here
            if (member.getRole().equals("Treasurer")) {
                System.out.print("\nDisplay Debts (D)\t");
                System.out.print("Display Payables (P)\n\n");
                System.out.print(">");
                option = in.nextLine();
                if (option.equalsIgnoreCase("D")) {
                    try {
                        clearConsole();
                        Finances.displayDebt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (option.equalsIgnoreCase("P")) {
                    clearConsole();
                    Finances.displayPayables();
                }
                returnOrExit(member);
            } else if (!(member.getRole().equals("Treasurer") || member.getRole().equals("Coach"))) {
                System.out.print("Top up account balance(1)\t");
                System.out.print("Return to Main Screen(2)\t");
                System.out.print("Exit(3)\n");
                System.out.print("\n> ");

                int input = convertInputToInteger(3, 1);

                // If the input is a 1, return to the main screen
                if (input == 1) {
                    clearConsole();
                    System.out.println("Here are some steps to top up your account balance:");
                    System.out
                            .println("\n1. Go to this link: https://paypal.me/memgroup66?country.x=CA&locale.x=en_US");
                    System.out.println("2. Click on the SEND option in PayPal.");
                    System.out.println("3. Log in to PayPal. Sign up if you don't have an account.");
                    System.out.println(
                            "4. Pay the amount you want to ($10/practice class, and you can only pay for 12 classes in a row).");
                    System.out.println("5. Enter the amount you have paid through PayPal below.");

                    String amount = "";

                    while (amount == "" || amount == null) {
                        System.out.print("\n\nEnter the amount you paid: $");
                        amount = in.nextLine();

                        if (amount == "" || amount == null || !amount.matches("[0-9]+")) {
                            clearConsole();
                            System.out.println("*** Payment ***\n");
                            System.out.println("Invalid amount.\n");
                            amount = "";
                        }
                    }

                    System.out.print("\n\nThank you for your payment! ");
                    System.out.println("Funds will be ready to use in 4-24 hours.");

                    // try {
                    // PaypalConfirmationemail("group66club@gmail.com", "AppleBee", "AmandaScott");
                    // } catch (IOException e) {
                    // System.out.println(e.getMessage());
                    // }

                    // MemberBalance balance = new MemberBalance(member.getEmail(), amount, "0",
                    // "0");

                    ATreasurer.payments.put(member.getEmail(), Integer.parseInt(amount));
                    ClubManager.toFile("PendingPayments.txt");

                    // for debugging reasons, the line below should show the amount after it has
                    // been updated to the map
                    System.out.println("The amount is: " + ATreasurer.payments.get(member.getEmail()));

                    System.out.print("Check your balance(1)\t");
                    System.out.print("Return to Main Screen(2)\t");
                    System.out.print("Exit(3)\n");
                    System.out.print("\n> ");

                    int anotherInput = convertInputToInteger(3, 1);

                    if (anotherInput == 1) {
                        clearConsole();

                        MemberBalance bal = null;
                        for (Entry<String, MemberBalance> en : ATreasurer.balance.entrySet()) {
                            if (en.getKey().equals(member.getEmail())) {
                                bal = en.getValue();
                            }
                        }

                        System.out.println("Current Balance: $" + bal.getBalance());
                        // System.out.println("Account balance: $" + balance);
                    } else if (anotherInput == 2) {
                        clearConsole();
                        AfterLogIn(member);
                    }
                    // For exiting the annoucement feature.
                    else if (anotherInput == 3) {
                        clearConsole();
                        System.out.println("\nHave a nice day!\n");
                        System.exit(0);
                    }

                } else if (input == 2) {
                    clearConsole();
                    AfterLogIn(member);
                }
                // For exiting the annoucement feature.
                else if (input == 3) {
                    clearConsole();
                    System.out.println("\nHave a nice day!\n");
                    System.exit(0);
                }
            }
            returnOrExit(member);
        } else if (option.equalsIgnoreCase("P")) {
            // insert make a practice schedule/scheduling method here
            clearConsole();
            returnOrExit(member);
        } else if (option.equalsIgnoreCase("A")) {
            // insert make a attendance method here
            clearConsole();

            returnOrExit(member);
        } else if (option.equalsIgnoreCase("C")) {
            // method for resetting password here
            clearConsole();
            reset_password();
        } else if (option.equalsIgnoreCase("M")) {
            // methods for coach to add or remove members
            clearConsole();
            System.out.print("Add New Member (A)\t");
            System.out.print("Remove Members (R)\n");

            String input = in.nextLine();

            if (input.equalsIgnoreCase("A")) {/////coach can add new participant and create temporary password for them
                System.out.println("Please enter the participant's information on behalf of them.");
                System.out.println("Please enter a temporary password for them \nand inform the participant to change their password.");
                RegisterationQuestions(new ClubManager());
                System.out.println("Participant Successfully Registrated.");
                returnOrExit(member);

            } else if (input.equalsIgnoreCase("R")) {/////coach can remove participants from the app
                System.out
                        .println("Please enter the emails of the participants you would like to remove individually.");
                System.out.println("Type Q when you are done.");
                List<String> peopleToBeRemoved = new ArrayList<>();
                String email = "";

                while (!(email.equalsIgnoreCase("Q")) && in.hasNextLine()) {
                    Boolean flag = true;// true means that email is incorrect
                    AMember n = null;
                    do {
                        System.out.print("> ");
                        email = in.nextLine();
                        for (Entry<String, AMember> entry : ClubManager.members.entrySet()) {
                            if (entry.getKey().equals(email)) {
                                flag = false;
                                n = entry.getValue();
                                peopleToBeRemoved.add(email);
                            }
                        }
                        if (n == null) {
                            System.out.println("The email is not in the database. Please enter another email.");
                            flag = true;
                        }
                    } while (flag);

                }
                if(email.equalsIgnoreCase("Q")){
                    clearConsole();
                    removeParticipant(peopleToBeRemoved);
                }
                returnOrExit(member);
            }
        } else if (option.equalsIgnoreCase("CC")) {
            // method for changing coach
            clearConsole();
            changeCoach(member);
            returnOrExit(member);
        } else if (option.equalsIgnoreCase("CM")) {
            // code for checking who the coach for the month is
            clearConsole();
            
            ACoach coach = new ACoach();
            String month = new DateFormatSymbols().getMonths()[Calendar.getInstance().get(Calendar.MONTH)];
            System.out.println("Coach " + coach.getFirstName() + " will be coaching on Fridays for the current month, " + month + ".");

            returnOrExit(member);
        } else if (option.equalsIgnoreCase("E")) {
            clearConsole();
            System.out.println("\nHave a nice day\n");
            System.exit(0);
        }
    }

    public static void removeParticipant(List<String> peopleToBeRemoved) throws IOException {
        System.out.println("The following people were removed from the app: ");
        for(String email: peopleToBeRemoved){
            ClubManager.members.remove(email);
            System.out.println(email);
        }
        ClubManager.toFile("User_Info.txt");
    }

    public static void changeCoach(AMember member) throws IOException {
        System.out.println("Are you sure you want to change the coach? (Y or N)");
        String c = in.nextLine();
        if(c.equalsIgnoreCase("Y")){
            ClubManager.members.remove(ACoach.email);
            RegisterationQuestions(new ClubManager());

        } else if(c.equalsIgnoreCase("N")){
            returnOrExit(member);
        }


    }

    /*
     * NOTE: Can only send through Gmail. Can send to any address.
     * 
     * 1. Go onto the sender's Gmail.
     * 2. Go to settings icon > See All Settings > Forwarding and POP/IMAP
     * 3. Enable IMAP access
     * 
     * 1. Go onto myaccount.google.com
     * 2. Scroll down to less secure app access
     * 3. Enable less secure app access
     */
    public static void sendAnnouncements(String coachEmail, String coachPassword, String fullname) throws IOException {
        final String username = coachEmail;
        final String password = coachPassword;

        // For a single person, get rid of from here
        ClubManager manager = null;
        try {
            manager = new ClubManager();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // to here

        System.out.println("Enter the subject line: ");
        String subj = in.nextLine();
        System.out.println("Enter the body of the email (Press S to Send): ");
        String body = "";
        String next;
        while (in.hasNextLine() && !(next = in.nextLine()).equals("S")) {
            body += next;
            body += "\n";
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "imap.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(manager.emailsToString()) // For 1 person, just enter the email string ex:
                                                                    // "kffjk322@gmail.com"
            );
            message.setSubject("** ANNOUNCEMENT **: " + subj);
            message.setText("Hello! \n\n"
                    + body + "\n\n" + fullname);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Clears the console
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}