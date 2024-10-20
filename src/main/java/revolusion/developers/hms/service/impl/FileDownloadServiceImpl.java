package revolusion.developers.hms.service.impl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.mapper.HotelMapper;
import revolusion.developers.hms.mapper.OrderMapper;
import revolusion.developers.hms.payload.HotelDto;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.repository.HotelRepository;
import revolusion.developers.hms.repository.OrderRepository;
import revolusion.developers.hms.service.FileDownloadService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileDownloadServiceImpl implements FileDownloadService {

    private final HotelRepository hotelRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final HotelMapper hotelMapper;



    @Override
    public void generateCSV(Long hotelId, HttpServletResponse response) throws IOException {
        HotelDto hotel = hotelMapper.hotelToDto(hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found")));

        List<OrderDto> orders = orderRepository.findByHotelId(hotelId).stream()
                .map(orderMapper::orderToDto)
                .collect(Collectors.toList());

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"hotel_data.csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Hotel Name, Address, Star Rating, Description, Balance, Account Number");

        writer.println(hotel.getName() + "," + hotel.getAddress() + "," + hotel.getStarRating() + "," +
                hotel.getDescription() + "," + hotel.getBalance() + "," + hotel.getAccountNumber());

        writer.println("\nOrder ID, Order Date, Total Amount, Check-In Date, Check-Out Date, Order Status, User ID");

        for (OrderDto order : orders) {
            writer.println(order.getId() + "," + order.getOrderDate() + "," + order.getTotalAmount() + "," +
                    order.getCheckInDate() + "," + order.getCheckOutDate() + "," + order.getOrderStatus() + "," +
                    order.getDeadline() + "," +
                    (order.getUserDto() != null ? order.getUserDto().getId() : ""));
        }

        writer.flush();
        writer.close();
    }

    @Override
    public void generateExcel(Long hotelId, HttpServletResponse response) throws IOException {
        HotelDto hotel = hotelMapper.hotelToDto(hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found")));

        List<OrderDto> orders = orderRepository.findByHotelId(hotelId).stream()
                .map(orderMapper::orderToDto)
                .collect(Collectors.toList());

        Workbook workbook = new XSSFWorkbook();
        Sheet hotelSheet = workbook.createSheet("Hotel");
        Sheet orderSheet = workbook.createSheet("Orders");

        Row hotelHeader = hotelSheet.createRow(0);
        hotelHeader.createCell(0).setCellValue("Name");
        hotelHeader.createCell(1).setCellValue("Address");
        hotelHeader.createCell(2).setCellValue("Star Rating");
        hotelHeader.createCell(3).setCellValue("Description");
        hotelHeader.createCell(4).setCellValue("Balance");
        hotelHeader.createCell(5).setCellValue("Account Number");

        Row row = hotelSheet.createRow(1);
        row.createCell(0).setCellValue(hotel.getName());
        row.createCell(1).setCellValue(hotel.getAddress());
        row.createCell(2).setCellValue(hotel.getStarRating());
        row.createCell(3).setCellValue(hotel.getDescription());
        row.createCell(4).setCellValue(hotel.getBalance());
        row.createCell(5).setCellValue(hotel.getAccountNumber());

        Row orderHeader = orderSheet.createRow(0);
        orderHeader.createCell(0).setCellValue("Order ID");
        orderHeader.createCell(1).setCellValue("Order Date");
        orderHeader.createCell(2).setCellValue("Total Amount");
        orderHeader.createCell(3).setCellValue("Check-In Date");
        orderHeader.createCell(4).setCellValue("Check-Out Date");
        orderHeader.createCell(5).setCellValue("Order Status");
        orderHeader.createCell(6).setCellValue("Order deadline");
        orderHeader.createCell(7).setCellValue("User ID");

        int orderRowNum = 1;
        for (OrderDto order : orders) {
            Row rowOrder = orderSheet.createRow(orderRowNum++);
            rowOrder.createCell(0).setCellValue(order.getId());
            rowOrder.createCell(1).setCellValue(order.getOrderDate().toString());
            rowOrder.createCell(2).setCellValue(order.getTotalAmount());
            rowOrder.createCell(3).setCellValue(order.getCheckInDate().toString());
            rowOrder.createCell(4).setCellValue(order.getCheckOutDate().toString());
            rowOrder.createCell(5).setCellValue(order.getOrderStatus().toString());
            rowOrder.createCell(6).setCellValue(order.getDeadline().toString());
            rowOrder.createCell(7).setCellValue(order.getUserDto() != null ? String.valueOf(order.getUserDto().getId()) : "");
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"hotel_data.xlsx\"");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @Override
    public void generatePDF(Long hotelId, HttpServletResponse response) throws IOException, DocumentException {
        HotelDto hotel = hotelMapper.hotelToDto(hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found")));

        List<OrderDto> orders = orderRepository.findByHotelId(hotelId).stream()
                .map(orderMapper::orderToDto)
                .collect(Collectors.toList());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"hotel_data.pdf\"");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Hotel Data"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Name: " + hotel.getName()));
        document.add(new Paragraph("Address: " + hotel.getAddress()));
        document.add(new Paragraph("Star Rating: " + hotel.getStarRating()));
        document.add(new Paragraph("Description: " + hotel.getDescription()));
        document.add(new Paragraph("Balance: " + hotel.getBalance()));
        document.add(new Paragraph("Account Number: " + hotel.getAccountNumber()));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Order Data"));
        document.add(new Paragraph(" "));

        for (OrderDto order : orders) {
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));
            document.add(new Paragraph("Total Amount: " + order.getTotalAmount()));
            document.add(new Paragraph("Check-In Date: " + order.getCheckInDate()));
            document.add(new Paragraph("Check-Out Date: " + order.getCheckOutDate()));
            document.add(new Paragraph("Order Status: " + order.getOrderStatus()));
            document.add(new Paragraph("Order Deadline: " + order.getDeadline()));
            document.add(new Paragraph("User ID: " + (order.getUserDto() != null ? order.getUserDto().getId() : "")));
            document.add(new Paragraph(" "));
        }
        document.close();
    }
}
