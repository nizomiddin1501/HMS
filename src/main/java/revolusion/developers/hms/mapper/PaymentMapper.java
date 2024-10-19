package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.Payment;
import revolusion.developers.hms.payload.PaymentDto;

@Mapper(componentModel = "spring",
        uses = { OrderMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mapping(source = "order", target = "orderDto")
    PaymentDto paymentToDto(Payment payment);

    @Mapping(source = "orderDto", target = "order")
    Payment dtoToPayment(PaymentDto paymentDto);

}
