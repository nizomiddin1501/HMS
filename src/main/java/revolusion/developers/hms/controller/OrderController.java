package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.payload.OrderUpdateRequest;
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.service.OrderService;
import java.util.Optional;

/**
 * Controller for handling requests related to Order operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting order information.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructor for OrderController.
     *
     * @param orderService the service to manage order records
     * @Autowired automatically injects the OrderService bean
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    /**
     * Retrieve a paginated list of all orders.
     *
     * This method fetches a paginated list of order records and returns them as a list of OrderDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of orders per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of OrderDto representing all orders
     */
    @Operation(summary = "Get all Orders with Pagination", description = "Retrieve a paginated list of all orders.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<OrderDto>>> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<OrderDto> orderDtos = orderService.getAllOrders(page,size);
        CustomApiResponse<Page<OrderDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of orders.",
                true,
                orderDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }






    /**
     * Retrieve an order by their unique ID using the provided OrderDto.
     *
     * This method retrieves an order's details based on their ID and returns
     * a CustomApiResponse containing the corresponding OrderDto if found.
     * If the order does not exist, it returns a CustomApiResponse with a
     * message indicating that the order was not found and a 404 Not Found status.
     *
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the OrderDto and
     *         an HTTP status of OK, or a NOT FOUND status if the order does not exist.
     */
    @Operation(summary = "Get Order by ID", description = "Retrieve a order by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the order.")
    @ApiResponse(responseCode = "404", description = "Order not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<OrderDto>> getOrderById(@PathVariable Long id) {
        Optional<OrderDto> orderDto = orderService.getOrderById(id);
        if (orderDto.isPresent()){
            CustomApiResponse<OrderDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the order.",
                    true,
                    orderDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<OrderDto> response = new CustomApiResponse<>(
                    "Order not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new order.
     *
     * This method validates the incoming order data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param orderDto the DTO containing the order information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved order data
     */
    @Operation(summary = "Create a new Order", description = "Create a new order record.")
    @ApiResponse(responseCode = "201", description = "Order created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto){
        OrderDto savedOrder = orderService.createOrder(orderDto);
        CustomApiResponse<OrderDto> response = new CustomApiResponse<>(
                "Order created successfully",
                true,
                savedOrder
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing order using the provided OrderUpdateRequest.
     *
     * This method accepts the order's ID and a DTO containing updated order and payment details.
     * It updates the order record if it exists and returns the updated OrderDto object.
     *
     * @param id the ID of the order to be updated
     * @param orderUpdateRequest the DTO containing updated order and payment details
     * @return a ResponseEntity containing a CustomApiResponse with the updated OrderDto,
     *         or a NOT FOUND response if the order does not exist
     */
    @Operation(summary = "Update order", description = "Update the details of an existing order.")
    @ApiResponse(responseCode = "200", description = "Order updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<OrderDto>> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderUpdateRequest orderUpdateRequest) {
        Optional<OrderDto> orderDtoOptional = orderService.getOrderById(id);
        if (orderDtoOptional.isPresent()) {
            OrderDto updatedOrder = orderService.updateOrder(id, orderUpdateRequest);
            CustomApiResponse<OrderDto> response = new CustomApiResponse<>(
                    "Order updated successfully",
                    true,
                    updatedOrder
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<OrderDto> response = new CustomApiResponse<>(
                    "Order not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Delete an order by their ID.
     *
     * This method deletes the order record based on the given ID if it exists.
     *
     * @param id the ID of the order to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the order does not exist
     */
    @Operation(summary = "Delete Order", description = "Delete a order by its ID.")
    @ApiResponse(responseCode = "204", description = "Order deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Order not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        Optional<OrderDto> orderDto = orderService.getOrderById(id);
        if (orderDto.isPresent()) {
            orderService.deleteOrder(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Order deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Order not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }



}
