package revolusion.developers.hms.controller;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileDownloadController {


    private final FileDownloadService fileDownloadService;



    /**
     * Download a CSV file.
     *
     * This endpoint generates a CSV file and writes it to the response.
     * If successful, it returns a success message; otherwise, it returns an error message.
     *
     * @param response the HttpServletResponse to write the CSV file to
     * @param hotelId  the ID of the hotel for which to generate the report
     * @return ResponseEntity containing a CustomApiResponse with the operation status
     * @throws IOException if an input or output exception occurs
     */
    @GetMapping("/download/csv/{hotelId}")
    public ResponseEntity<CustomApiResponse<Void>> downloadCSV(@PathVariable Long hotelId, HttpServletResponse response) throws IOException {
        try {
            fileDownloadService.generateCSV(hotelId, response);
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
     * @param hotelId  the ID of the hotel for which to generate the report
     * @return ResponseEntity containing a CustomApiResponse with the operation status
     * @throws IOException if an input or output exception occurs
     */
    @GetMapping("/download/excel/{hotelId}")
    public ResponseEntity<CustomApiResponse<Void>> downloadExcel(@PathVariable Long hotelId, HttpServletResponse response) throws IOException {
        try {
            fileDownloadService.generateExcel(hotelId, response);
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
     * @param hotelId  the ID of the hotel for which to generate the report
     * @return ResponseEntity containing a CustomApiResponse with the operation status
     * @throws IOException if an input or output exception occurs
     * @throws DocumentException if an error occurs during PDF generation
     */
    @GetMapping("/download/pdf/{hotelId}")
    public ResponseEntity<CustomApiResponse<Void>> downloadPDF(@PathVariable Long hotelId, HttpServletResponse response) throws IOException, DocumentException {
        try {
            fileDownloadService.generatePDF(hotelId, response);
            return ResponseEntity.ok(new CustomApiResponse<>("PDF file generated successfully.", true, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomApiResponse<>("Error generating PDF file: " + e.getMessage(), false, null));
        }
    }
}
