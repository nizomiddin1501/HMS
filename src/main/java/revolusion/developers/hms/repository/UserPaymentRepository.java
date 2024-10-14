package revolusion.developers.hms.repository;

import revolusion.developers.hms.entity.UserPayment;

public interface UserPaymentRepository extends BaseRepository<UserPayment, Long> {



    UserPayment findByUserId(Long userId);


}
