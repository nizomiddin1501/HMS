package revolusion.developers.hms.service;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface FileDownloadService {


    // create CSV file
    void generateCSV(HttpServletResponse response) throws IOException;

    // create Excel file
    void generateExcel(HttpServletResponse response) throws IOException;

    // // create PDF file
    public void generatePDF(HttpServletResponse response) throws IOException, DocumentException;





}
