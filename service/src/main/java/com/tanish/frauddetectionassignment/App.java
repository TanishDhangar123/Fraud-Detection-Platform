package com.tanish.frauddetectionassignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanish.frauddetectionassignment.model.FlaggedTransaction;
import com.tanish.frauddetectionassignment.model.Transaction;
import com.tanish.frauddetectionassignment.rules.FraudRules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            File inputFile = new File("service/src/main/resources/transactions.json");


            Transaction[] transactions = mapper.readValue(inputFile, Transaction[].class);

            List<FlaggedTransaction> flaggedTransactions = new ArrayList<>();

            System.out.println(" Fraud Detection Results ");

            for (Transaction transaction : transactions) {

                List<String> reasons = FraudRules.checkTransaction(transaction);

                if (!reasons.isEmpty()) {
                    FlaggedTransaction flagged = new FlaggedTransaction(transaction, reasons);
                    flaggedTransactions.add(flagged);
                    System.out.println(" Flagged transactions : " + flagged);
                }

                else {
                    System.out.println("Normal transactions : " + transaction);
                }

            }

            if (!flaggedTransactions.isEmpty()) {
                File outputFile = new File("service/src/main/resources/flaggedTransactions.json");
                mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, flaggedTransactions);
                System.out.println("\n Flagged transactions saved to: " + outputFile.getAbsolutePath());
            }

            else {
                System.out.println("\n No suspicious transactions found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
