package revolusion.developers.hms.service.impl;

import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.entity.Payment;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;

    private final OrderRepository orderRepository;

    private final RoomRepository roomRepository;

    private final PaymentRepository paymentRepository;

    @Autowired
    public OrderServiceImpl(
            ModelMapper modelMapper,
            OrderRepository orderRepository,
            RoomRepository roomRepository,
            PaymentRepository paymentRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.roomRepository = roomRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Page<OrderDto> getAllOrders(int page, int size) {
        Page<Order> ordersPage = orderRepository.findAll(PageRequest.of(page, size));
        return ordersPage.map(this::orderToDto);
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
        // 1. Convert OrderDto to Order entity
        Order order = dtoToOrder(orderDto);

        // 2. Validate User existence
        if (order.getUser() == null || order.getUser().getId() == null) {
            throw new OrderException("User is required for the order.");
        }

        // 3. Validate Room existence
        if (order.getRoom() == null || order.getRoom().getId() == null) {
            throw new OrderException("Room is required for the order.");
        }

        // 4. Get Room by ID
        Room room = roomRepository.findById(order.getRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Id", order.getRoom().getId()));

        // 5. Check for existing orders that overlap with the requested dates
        List<Order> existingOrders = orderRepository.findByRoomId(room.getId());
        for (Order existingOrder : existingOrders) {
            if ((orderDto.getCheckInDate().isBefore(existingOrder.getCheckOutDate()) ||
                    orderDto.getCheckInDate().isEqual(existingOrder.getCheckOutDate())) &&
                    (orderDto.getCheckOutDate().isAfter(existingOrder.getCheckInDate()) ||
                            orderDto.getCheckOutDate().isEqual(existingOrder.getCheckInDate()))) {
                throw new OrderException("The room is already booked for the selected dates.");
            }
        }

        // 6. Set status to "Pending" as the order is just created
        order.setOrderStatus(OrderStatus.PENDING);

        // 7. Set room and date
        order.setRoom(room); // Set the room for the order
        order.setOrderDate(LocalDate.now()); // Set order date to current date

        // 8. Save order
        Order savedOrder = orderRepository.save(order);

        // 9. Convert saved entity back to DTO and return
        return orderToDto(savedOrder);
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderUpdateRequest orderUpdateRequest) throws ResourceNotFoundException {
        // 1. Get the available order
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", " Id ", orderId));

        // 2. update order details
        OrderDto orderDto = orderUpdateRequest.getOrderDto();
        existingOrder.setOrderDate(orderDto.getOrderDate());
        existingOrder.setTotalAmount(orderDto.getTotalAmount());

        // 3. Set status depending on the business logic
        PaymentDto paymentDto = orderUpdateRequest.getPaymentDto();
        switch (paymentDto.getPaymentStatus()) {
            case PAID:
                existingOrder.setOrderStatus(OrderStatus.CONFIRMED);  // Agar to'lov amalga oshirilgan bo'lsa, buyurtmani tasdiqlash
                break;
            case FAILED:
                existingOrder.setOrderStatus(OrderStatus.CANCELLED); // Agar to'lov rad etilgan bo'lsa, buyurtmani bekor qilish
                break;
            case PENDING:
                existingOrder.setOrderStatus(OrderStatus.PENDING); // Agar to'lov hali bajarilmagan bo'lsa, holatni "Pending" deb belgilash
                break;
            default:
                existingOrder.setOrderStatus(OrderStatus.PENDING); // Boshqa holatlar uchun, holatni "Pending" deb belgilash
                break;
        }

        // 3. get Room by ID
        Room room = roomRepository.findById(orderDto.getRoomDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "Id", orderDto.getRoomDto().getId()));

        // 4. Add the updated Room to the order
        existingOrder.setRoom(room);

        // 5. Save updated order
        Order updatedOrder = orderRepository.save(existingOrder);

        // 6. Convert updated order entity to DTO and return
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
