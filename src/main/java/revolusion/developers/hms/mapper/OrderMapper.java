package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.payload.OrderDto;

@Mapper(componentModel = "spring",
        uses = { UserMapper.class, RoomMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {


    @Mapping(source = "room", target = "roomDto")
    @Mapping(source = "user", target = "userDto")
    OrderDto orderToDto(Order order);

    @Mapping(source = "roomDto", target = "room")
    @Mapping(source = "userDto", target = "user")
    Order dtoToOrder(OrderDto orderDto);


}
