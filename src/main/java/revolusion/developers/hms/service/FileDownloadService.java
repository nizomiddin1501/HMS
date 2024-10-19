package revolusion.developers.hms.service;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface FileDownloadService {


    void generateCSV(Long hotelId, HttpServletResponse response) throws IOException;

    void generateExcel(Long hotelId, HttpServletResponse response) throws IOException;

    void generatePDF(Long hotelId, HttpServletResponse response) throws IOException, DocumentException;





}
