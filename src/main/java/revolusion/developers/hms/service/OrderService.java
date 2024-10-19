package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.OrderException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.payload.OrderUpdateRequest;
import revolusion.developers.hms.payload.PaymentDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {


    Page<OrderDto> getAllOrders(int page, int size);

    Optional<OrderDto> getOrderById(Long orderId);

    OrderDto createOrder(OrderDto orderDto);

    OrderDto updateOrder(Long orderId, OrderUpdateRequest orderUpdateRequest);

    void deleteOrder(Long orderId);









}
