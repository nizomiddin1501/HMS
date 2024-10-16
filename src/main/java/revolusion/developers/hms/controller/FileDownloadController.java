package revolusion.developers.hms.controller;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.service.FileDownloadService;
import java.io.IOException;

/**
 * Controller for handling requests related to file downloads.
 * This controller provides RESTful endpoints to manage file download operations.
 */

@RestController
@RequestMapping("/api/files")
public class FileDownloadController {


    private final FileDownloadService fileDownloadService;

    /**
     * Constructor for FileDownloadController.
     *
     * @param fileDownloadService the service to manage file download operations
     * @Autowired automatically injects the FileDownloadService bean
     */
    @Autowired
    public FileDownloadController(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }




    /**
     * Download a CSV file.
     *
     * This endpoint generates a CSV file and writes it to the response.
     * If successful, it returns a success message; otherwise, it returns an error message.
     *
     * @param response the HttpServletResponse to write the CSV file to
     * @return ResponseEntity containing a CustomApiResponse with the operation status
     * @throws IOException if an input or output exception occurs
     */
    @GetMapping("/download/csv")
    public ResponseEntity<CustomApiResponse<Void>> downloadCSV(HttpServletResponse response) throws IOException {
        try {
            fileDownloadService.generateCSV(response);
            return ResponseEntity.ok(new CustomApiResponse<>("CSV file generated successfully.", true, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomApiResponse<>("Error generating CSV file: " + e.getMessage(), false, null));
        }
    }

    /**
     * Download an Excel file.
     *
     * This endpoint generates an Excel file and writes it to the response.
     * If successful, it returns a success message; otherwise, it returns an error message.
     *
     * @param response the HttpServletResponse to write the Excel file to
     * @return ResponseEntity containing a CustomApiResponse with the operation status
     * @throws IOException if an input or output exception occurs
     */
    @GetMapping("/download/excel")
    public ResponseEntity<CustomApiResponse<Void>> downloadExcel(HttpServletResponse response) throws IOException {
        try {
            fileDownloadService.generateExcel(response);
            return ResponseEntity.ok(new CustomApiResponse<>("Excel file generated successfully.", true, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomApiResponse<>("Error generating Excel file: " + e.getMessage(), false, null));
        }
    }

    /**
     * Download a PDF file.
     *
     * This endpoint generates a PDF file and writes it to the response.
     * If successful, it returns a success message; otherwise, it returns an error message.
     *
     * @param response the HttpServletResponse to write the PDF file to
     * @return ResponseEntity containing a CustomApiResponse with the operation status
     * @throws IOException if an input or output exception occurs
     * @throws DocumentException if an error occurs during PDF generation
     */
    @GetMapping("/download/pdf")
    public ResponseEntity<CustomApiResponse<Void>> downloadPDF(HttpServletResponse response) throws IOException, DocumentException {
        try {
            fileDownloadService.generatePDF(response);
            return ResponseEntity.ok(new CustomApiResponse<>("PDF file generated successfully.", true, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomApiResponse<>("Error generating PDF file: " + e.getMessage(), false, null));
        }
    }
}
