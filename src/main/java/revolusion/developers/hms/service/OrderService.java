package revolusion.developers.hms.service;

import revolusion.developers.hms.exceptions.OrderException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {


    // get all orders using pagination
    List<OrderDto> getAllOrders(int page, int size);

    // get order by ID
    Optional<OrderDto> getOrderById(Long orderId) throws ResourceNotFoundException;

    // create a new order
    OrderDto createOrder(OrderDto orderDto) throws OrderException;

    // update an existing order
    OrderDto updateOrder(Long orderId, OrderDto orderDto) throws ResourceNotFoundException;

    // delete a order
    void deleteOrder(Long orderId) throws ResourceNotFoundException;









}
