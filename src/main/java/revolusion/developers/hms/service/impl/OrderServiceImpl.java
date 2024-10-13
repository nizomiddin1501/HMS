package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.exceptions.OrderException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.repository.OrderRepository;
import revolusion.developers.hms.repository.RoomRepository;
import revolusion.developers.hms.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(ModelMapper modelMapper, OrderRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> getAllOrders(int page, int size) {
        return null;
    }

    @Override
    public Optional<OrderDto> getOrderById(Long orderId) throws ResourceNotFoundException {
        return Optional.empty();
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) throws OrderException {
        return null;
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) throws ResourceNotFoundException {

    }

    // DTO ---> Entity
    private Order dtoToOrder(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    // Entity ---> DTO
    public OrderDto orderToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

}
