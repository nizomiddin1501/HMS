package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.exceptions.OrderException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.payload.OrderUpdateRequest;
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.service.OrderService;

import java.util.Optional;

/**
 * REST controller for managing orders, offering endpoints for
 * creating, updating, retrieving, and deleting order records.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    /**
     * Retrieve a paginated list of orders.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of orders per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated OrderDto list
     */
    @Operation(summary = "Get all Orders with Pagination", description = "Retrieve a paginated list of all orders.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<OrderDto>>> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<OrderDto> orderDtos = orderService.getAllOrders(page, size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of orders.",
                true,
                orderDtos), HttpStatus.OK);
    }


    /**
     * Retrieve an order by their unique ID using the provided OrderDto.
     *
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the OrderDto and
     * an HTTP status of OK
     */
    @Operation(summary = "Get Order by ID", description = "Retrieve a order by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the order.")
    @ApiResponse(responseCode = "404", description = "Order not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<OrderDto>> getOrderById(@PathVariable Long id) {
        OrderDto orderDto = orderService.getOrderById(id)
                .orElseThrow(() -> new OrderException("Order not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the order.",
                true,
                orderDto), HttpStatus.OK);
    }


    /**
     * Creates a new order.
     *
     * @param orderDto the DTO containing the order information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved order data
     */
    @Operation(summary = "Create a new Order", description = "Create a new order record.")
    @ApiResponse(responseCode = "201", description = "Order created successfully.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto savedOrder = orderService.createOrder(orderDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Order created successfully",
                true,
                savedOrder), HttpStatus.CREATED);
    }


    /**
     * Update the details of an existing order using the provided OrderDto.
     *
     * @param id                 the ID of the order to be updated
     * @param orderUpdateRequest the DTO containing updated order details
     * @return a ResponseEntity containing a CustomApiResponse with the updated OrderDto
     */
    @Operation(summary = "Update order", description = "Update the details of an existing order.")
    @ApiResponse(responseCode = "200", description = "Order updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<OrderDto>> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderDto updatedOrder = orderService.updateOrder(id, orderUpdateRequest);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Order updated successfully",
                true,
                updatedOrder), HttpStatus.OK);
    }


    /**
     * Delete an order by their ID.
     *
     * @param id the ID of the order to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete Order", description = "Delete a order by its ID.")
    @ApiResponse(responseCode = "204", description = "Order deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Order not found.")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Order deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
    }
}

