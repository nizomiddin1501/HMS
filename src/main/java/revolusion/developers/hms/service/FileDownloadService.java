package revolusion.developers.hms.service;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface FileDownloadService {


    void generateCSV(HttpServletResponse response) throws IOException;

    void generateExcel(HttpServletResponse response) throws IOException;

    void generatePDF(HttpServletResponse response) throws IOException, DocumentException;





}
