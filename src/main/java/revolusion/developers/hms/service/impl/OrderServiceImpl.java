package revolusion.developers.hms.service.impl;

import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.entity.Payment;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.exceptions.OrderException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.repository.OrderRepository;
import revolusion.developers.hms.repository.RoomRepository;
import revolusion.developers.hms.service.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;

    private final OrderRepository orderRepository;

    private final RoomRepository roomRepository;

    @Autowired
    public OrderServiceImpl(ModelMapper modelMapper, OrderRepository orderRepository, RoomRepository roomRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<OrderDto> getAllOrders(int page, int size) {
        List<Order> orders = orderRepository.findAll(PageRequest.of(page, size)).getContent();
        return orders.stream()
                .map(this::orderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> getOrderById(Long orderId) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", orderId));

        // Convert Order entity to OrderDto
        OrderDto orderDto = orderToDto(order);
        return Optional.ofNullable(orderDto);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) throws OrderException {
        return null;
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) throws ResourceNotFoundException {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", " Id ", orderId));

        // update order details
        existingOrder.setOrderDate(orderDto.getOrderDate());
        existingOrder.setStatus(orderDto.getStatus());
        existingOrder.setTotalAmount(orderDto.getTotalAmount());

        // get Room by ID
        Room room = roomRepository.findById(orderDto.getRoomDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Id", orderDto.getRoomDto().getId()));

        // Add the updated Room to the order
        existingOrder.setRoom(room);

        // Save updated order
        Order updatedOrder = orderRepository.save(existingOrder);

        // Convert updated order entity to DTO and return
        return orderToDto(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", " Id ", orderId));
        orderRepository.delete(order);
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
