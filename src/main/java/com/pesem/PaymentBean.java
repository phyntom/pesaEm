package com.pesem;

import com.pesem.com.pesaem.model.Employee;
import com.pesem.com.pesaem.model.Transaction;
import com.pesem.utilities.CsvFileProcessor;
import com.pesem.utilities.LoadConfig;
import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ManagedBean(name = "paymentBean")
@SessionScoped
public class PaymentBean implements Serializable {

    private final static Logger Log = Logger.getLogger(UserBean.class);

    Properties prop;

    private Path filePath;

    private List<Employee> employeeList = new ArrayList<>();

    private List<Transaction> transactionList = new ArrayList<>();

    /**
     * method used to handler file upload from submitted csv file
     * and load read into list of employee using utility class
     * CsvFileProcessor
     *
     * @param event
     */
    public void processFileUpload(FileUploadEvent event) {

        employeeList = new ArrayList<>();
        try (InputStream fileInputStream = event.getFile().getInputstream()) {
            /**
             * we created a new file name by adding the current time milliseconds as prefix and followed
             * by _ and the original filename
             *
             */
            String newFileName = System.currentTimeMillis() + "_" + event.getFile().getFileName();
            String fileLocation = LoadConfig.readFilesFolder() + "" + newFileName;
            OutputStream outputStream =
                    new FileOutputStream(new File(fileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            Log.info("File " + newFileName + " saved to " + fileLocation + " for processing");
            filePath = Paths.get(fileLocation);
//          read the uploaded csv file and load record into list of employee
            employeeList = new CsvFileProcessor().readFileRecord(filePath);
            FacesMessage message = new FacesMessage("File ", event.getFile().getFileName() + " is successfully uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Log.info("List of employees loaded. Number of records " + employeeList.size());
        }

        catch (FileNotFoundException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"File ", event.getFile().getFileName() + " is not uploaded. Invalid upload location");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Log.error("Cannot copy the file. Invalid upload location" + event.getFile().getFileName(), ex);
        }
        catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"File ", event.getFile().getFileName() + " is not uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Log.error("Cannot copy the file " + event.getFile().getFileName(), ex.getCause());
        }
    }

    /**
     * this method takes the list of employees and then process it using CsvFileProcess class
     */
    public String processPayment() {
        if (!employeeList.isEmpty()) {
            transactionList = new CsvFileProcessor().sendEmployeePayment(employeeList);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Processing employee list finished", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "transactions?faces-redirect=true";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot process empty list", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "payment?faces-redirect=true";
        }

    }

    /**
     * method used to reset the page records
     */
    public String resetAll() {
        employeeList = new ArrayList<>();
        transactionList = new ArrayList<>();
        return "payment?faces-redirect=true";
    }

    public String back(){
        return "payment?faces-redirect=true";
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
