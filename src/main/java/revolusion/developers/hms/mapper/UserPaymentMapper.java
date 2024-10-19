package revolusion.developers.hms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import revolusion.developers.hms.entity.UserPayment;
import revolusion.developers.hms.payload.UserPaymentDto;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPaymentMapper {

    @Mapping(target = "userDto", source = "user")
    UserPaymentDto userPaymentToDto(UserPayment userPayment);

    @Mapping(target = "user", source = "userDto")
    UserPayment dtoToUserPayment(UserPaymentDto userPaymentDto);

}
