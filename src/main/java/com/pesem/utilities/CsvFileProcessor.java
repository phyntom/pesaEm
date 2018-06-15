package com.pesem.utilities;

import com.pesem.com.pesaem.model.Employee;
import com.pesem.com.pesaem.model.Transaction;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvFileProcessor {

    private List<Employee> employeeList;

    private final static Logger Log = Logger.getLogger(CsvFileProcessor.class);

    /**
     * method used to read csv file and load it into list of employee
     *
     * @param path
     * @return list of employee
     */
    public List<Employee> readFileRecord(Path path) {
        Pattern pattern = Pattern.compile(",");
        employeeList = new ArrayList<>();
        Log.info("Starting to read file with name " + path.getFileName().toString());
        if (Files.exists(path)) {
            try {
                Stream<String> in = Files.lines(path);
                employeeList = in.skip(1)
                        .map(line -> {
                            String[] row = pattern.split(line);
                            if (row.length == 3) {
                                return new Employee(row[0], row[1], new BigInteger(row[2]));
                            } else {
                                return new Employee();
                            }
                        })
                        .collect(Collectors.toList());
                // removing duplicate msisdn
                employeeList.parallelStream().distinct().collect(Collectors.toList());
            } catch (IOException ex) {
                Log.error(" Enable to read file with name " + path.getFileName() + " | " + ex.toString());
            } catch (Exception ex) {
                Log.error(" Enable to read file with name " + path.getFileName() + " | " + ex.toString());
            }
        }
        return employeeList;
    }

    /**
     * this method is used to send employee payment by submitting the list of employee containing their details
     * it used multi threading using new Java 8 Executor service and process all requests through different thread
     * the response are later sent using future
     *
     * @param employeeList
     * @return
     */
    public List<Transaction> sendEmployeePayment(List<Employee> employeeList) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            /**
             * we preferred to start with small amount of threads but this number can be increased
             * or configured in the configuration file
             *
             */
            MpesaService mpesaService= new MpesaService();
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            List<Callable<Transaction>> sendTasks = new ArrayList<>();
            for (Employee emp : employeeList) {
                Callable<Transaction> task = () -> {
                    return mpesaService.processRequest(emp);
                };
                sendTasks.add(task);
            }
            List<Future<Transaction>> futureResponses = executorService.invokeAll(sendTasks);
            for (Future response : futureResponses) {
                transactions.add((Transaction) response.get());
            }
            Log.info("Finished to process "+transactions.size()+" transactions");
        } catch (InterruptedException | ExecutionException ex) {
            Log.error(" Enable to process transaction  | " + ex.toString());
        }
        return transactions;
    }

}
