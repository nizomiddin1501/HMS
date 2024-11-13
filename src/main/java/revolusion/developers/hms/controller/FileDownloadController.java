package revolusion.developers.hms.controller;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.service.FileDownloadService;
import java.io.IOException;

/**
 * Controller for handling file download operations.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotel/files")
public class FileDownloadController {


    private final FileDownloadService fileDownloadService;

    /**
     * Download a CSV file.
     *
     * @param hotelId the ID of the hotel for the report
     * @param response to write the CSV file to
     * @return ResponseEntity with the operation status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download/csv/{hotelId}")
    public ResponseEntity<CustomApiResponse<Void>> downloadCSV(
            @PathVariable Long hotelId,
            HttpServletResponse response) throws IOException {
        fileDownloadService.generateCSV(hotelId, response);
        return ResponseEntity.ok(new CustomApiResponse<>(
                "CSV file generated successfully.",
                true,
                null));
    }

    /**
     * Download an Excel file.
     *
     * @param hotelId the ID of the hotel for the report
     * @param response to write the Excel file to
     * @return ResponseEntity with the operation status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download/excel/{hotelId}")
    public ResponseEntity<CustomApiResponse<Void>> downloadExcel(
            @PathVariable Long hotelId,
            HttpServletResponse response) throws IOException {
        fileDownloadService.generateExcel(hotelId, response);
        return ResponseEntity.ok(new CustomApiResponse<>(
                "Excel file generated successfully.",
                true,
                null));
    }

    /**
     * Download a PDF file.
     *
     * @param hotelId the ID of the hotel for the report
     * @param response to write the PDF file to
     * @return ResponseEntity with the operation status
     * @throws IOException if an error occurs during PDF generation
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download/pdf/{hotelId}")
    public ResponseEntity<CustomApiResponse<Void>> downloadPDF(
            @PathVariable Long hotelId,
            HttpServletResponse response) throws IOException, DocumentException {
        fileDownloadService.generatePDF(hotelId, response);
        return ResponseEntity.ok(new CustomApiResponse<>(
                "PDF file generated successfully.",
                true,
                null));
    }
}
