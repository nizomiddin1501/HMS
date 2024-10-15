package revolusion.developers.hms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revolusion.developers.hms.entity.UserPayment;

public interface UserPaymentRepository extends BaseRepository<UserPayment, Long> {



    UserPayment findByUserId(Long userId);

    // UserPayment accountNumber exists check
    @Query(value = "select count(*) > 0 from user_payments u where u.account_number = :accountNumber", nativeQuery = true)
    boolean existsByAccountNumber(@Param("accountNumber") String accountNumber);


}
