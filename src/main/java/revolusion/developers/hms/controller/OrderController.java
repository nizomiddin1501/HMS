package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.payload.OrderUpdateRequest;
import revolusion.developers.hms.service.OrderService;
import java.util.Optional;

/**
 * Controller for handling requests related to Order operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting order information.
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
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the OrderDto and
     *         an HTTP status of OK
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
     * @param orderDto the DTO containing the order information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved order data
     */
    @Operation(summary = "Create a new Order", description = "Create a new order record.")
    @ApiResponse(responseCode = "201", description = "Order created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto){
        OrderDto savedOrder = orderService.createOrder(orderDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Order created successfully",
                true,
                savedOrder), HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing order using the provided OrderDto.
     *
     * @param id the ID of the order to be updated
     * @param orderUpdateRequest the DTO containing updated order details
     * @return a ResponseEntity containing a CustomApiResponse with the updated OrderDto
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
     * @param id the ID of the order to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete Order", description = "Delete a order by its ID.")
    @ApiResponse(responseCode = "204", description = "Order deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Order not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Order deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
        }
    }

