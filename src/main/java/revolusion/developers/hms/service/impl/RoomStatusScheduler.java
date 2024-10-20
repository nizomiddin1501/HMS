package revolusion.developers.hms.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.entity.Room;
import revolusion.developers.hms.entity.enums.RoomStatus;
import revolusion.developers.hms.repository.OrderRepository;
import revolusion.developers.hms.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomStatusScheduler {

    private final RoomRepository roomRepository;
    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 86400000)
    public void updateRoomStatusAfterDeadline() {
        List<Order> expiredOrders = orderRepository.findAllByDeadlineBefore(LocalDateTime.now());

        for (Order order : expiredOrders) {
            Room room = order.getRoom();
            if (room != null && room.getRoomStatus() == RoomStatus.BOOKED) {
                room.setRoomStatus(RoomStatus.AVAILABLE);
                roomRepository.save(room);
            }
        }
    }

}
