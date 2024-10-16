package revolusion.developers.hms.service.impl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.User;
import revolusion.developers.hms.entity.status.OrderStatus;
import revolusion.developers.hms.exceptions.OrderException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.payload.OrderUpdateRequest;
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.repository.OrderRepository;
import revolusion.developers.hms.repository.PaymentRepository;
import revolusion.developers.hms.repository.RoomRepository;
import revolusion.developers.hms.service.OrderService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final RoomRepository roomRepository;

    private final PaymentRepository paymentRepository;


    @Override
    public Page<OrderDto> getAllOrders(int page, int size) {
        Page<Order> ordersPage = orderRepository.findAll(PageRequest.of(page, size));
        return ordersPage.map(this::orderToDto);
    }

    @Override
    public Optional<OrderDto> getOrderById(Long orderId) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", orderId));
        OrderDto orderDto = orderToDto(order);
        return Optional.ofNullable(orderDto);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) throws OrderException {

        Order order = dtoToOrder(orderDto);

        if (order.getUser() == null || order.getUser().getId() == null) {
            throw new OrderException("User is required for the order.");
        }

        if (order.getRoom() == null || order.getRoom().getId() == null) {
            throw new OrderException("Room is required for the order.");
        }

        Room room = roomRepository.findById(order.getRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Id", order.getRoom().getId()));

        List<Order> existingOrders = orderRepository.findByRoomId(room.getId());
        for (Order existingOrder : existingOrders) {
            if ((orderDto.getCheckInDate().isBefore(existingOrder.getCheckOutDate()) ||
                    orderDto.getCheckInDate().isEqual(existingOrder.getCheckOutDate())) &&
                    (orderDto.getCheckOutDate().isAfter(existingOrder.getCheckInDate()) ||
                            orderDto.getCheckOutDate().isEqual(existingOrder.getCheckInDate()))) {
                throw new OrderException("The room is already booked for the selected dates.");
            }
        }

        order.setOrderStatus(OrderStatus.PENDING);

        order.setRoom(room);
        order.setOrderDate(LocalDate.now());

        long numberOfNights = ChronoUnit.DAYS.between(orderDto.getCheckInDate(), orderDto.getCheckOutDate());
        double roomPrice = room.getPrice();
        double totalAmount = numberOfNights * roomPrice;
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return orderToDto(savedOrder);
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderUpdateRequest orderUpdateRequest) throws ResourceNotFoundException {
         Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", " Id ", orderId));
        OrderDto orderDto = orderUpdateRequest.getOrderDto();
        existingOrder.setOrderDate(orderDto.getOrderDate());

        if (orderDto.getTotalAmount() != null) {
            existingOrder.setTotalAmount(orderDto.getTotalAmount());
        }

        PaymentDto paymentDto = orderUpdateRequest.getPaymentDto();
        switch (paymentDto.getPaymentStatus()) {
            case PAID:
                existingOrder.setOrderStatus(OrderStatus.CONFIRMED);
                break;
            case FAILED:
                existingOrder.setOrderStatus(OrderStatus.CANCELLED);
                break;
            case PENDING:
                existingOrder.setOrderStatus(OrderStatus.PENDING);
                break;
            default:
                existingOrder.setOrderStatus(OrderStatus.PENDING);
                break;
        }

        Room room = roomRepository.findById(orderDto.getRoomDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Id", orderDto.getRoomDto().getId()));
         existingOrder.setRoom(room);

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderToDto(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", " Id ", orderId));
        orderRepository.delete(order);
    }


//    private Order dtoToOrder(OrderDto orderDto) {
//        return modelMapper.map(orderDto, Order.class);
//    }

    private Order dtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setCheckInDate(orderDto.getCheckInDate());
        order.setCheckOutDate(orderDto.getCheckOutDate());

        if (orderDto.getUserDto() != null) {
            User user = new User();
            user.setId(orderDto.getUserDto().getId());
            user.setName(orderDto.getUserDto().getName());
            order.setUser(user);
        }
        if (orderDto.getRoomDto() != null) {
            Room room = new Room();
            room.setId(orderDto.getRoomDto().getId());
            order.setRoom(room);
        }
        return order;
    }
    public OrderDto orderToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

}
